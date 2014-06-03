package de.thm.arsnova.connector.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import de.thm.arsnova.connector.dao.UniRepDao.Filter;
import de.thm.arsnova.connector.dao.UniRepDao.Filter.Type;
import de.thm.arsnova.connector.model.IliasAnswer;
import de.thm.arsnova.connector.model.IliasCategoryNode;
import de.thm.arsnova.connector.model.IliasFeedback;
import de.thm.arsnova.connector.model.IliasQuestion;

@Filter(Type.TEST)
public class IliasConnectorDaoImpl implements UniRepDao {

	@Autowired
	private DataSource dataSource;

	@Override
	public List<IliasCategoryNode> getTreeObjects(final int refId) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		String typefilter = null;

		if (this.getClass().getAnnotation(UniRepDao.Filter.class) != null) {
			switch (this.getClass().getAnnotation(UniRepDao.Filter.class).value()) {
			case QUESTION_POOL:
				typefilter = " AND type <> 'tst'";
				break;
			case TEST:
				typefilter = " AND type <> 'qpl'";
				break;
			default:
				typefilter = "";
				break;
			}
		}

		final Map<Integer, Integer> questionCount = getQuestionCount(refId);
		final Map<Integer, Integer> randomQuestionAmount = getRandomQuestionAmount(refId);

		final List<IliasCategoryNode> nodeList = jdbcTemplate.query(
				"SELECT t2.*, d.type, d.title FROM tree AS t1"
						+ " JOIN tree AS t2 ON t2.lft BETWEEN t1.lft AND t1.rgt AND t1.tree = t2.tree"
						+ " JOIN object_reference AS r ON r.ref_id=t2.child "
						+ " JOIN object_data as d ON d.obj_id=r.obj_id"
						+ typefilter
						+ " WHERE t1.child=? ORDER BY parent;",
						new String[] {String.valueOf(refId)},
						new RowMapper<IliasCategoryNode>() {
							@Override
							public IliasCategoryNode mapRow(
									final ResultSet resultSet,
									final int row
									) throws SQLException {
								final IliasCategoryNode node = new IliasCategoryNode(
										resultSet.getInt("child"),
										resultSet.getInt("parent"),
										resultSet.getString("title"),
										resultSet.getString("type"),
										questionCount.get(resultSet.getInt("child")) == null ? 0 :
											questionCount.get(resultSet.getInt("child"))
										);
								if ("tst".equals(node.getType())) {
									if (randomQuestionAmount.get(node.getId()) == null) {
										node.setIsRandomTest(false);
									} else {
										node.setIsRandomTest(true);
										node.setRandomQuestionCount(randomQuestionAmount.get(node.getId()));
									}
								}
								return node;
							}
						}
				);

		for (final IliasCategoryNode node : nodeList) {
			for (final IliasCategoryNode parentNode : nodeList) {
				if (node.getParent() == parentNode.getId()) {
					parentNode.addChild(node);
				}
			}
		}

		final List<IliasCategoryNode> result = new ArrayList<>();
		if (nodeList.size() > 0) {
			result.add(nodeList.get(0));
		}

		return result;
	}


	@Override
	public List<IliasQuestion> getQuestion(final int refId) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		return jdbcTemplate.query(
				"SELECT * FROM qpl_questions JOIN object_reference ON (obj_fi = obj_id) WHERE ref_id=?;",
				new String[] {String.valueOf(refId)},
				new RowMapper<IliasQuestion>() {
					@Override
					public IliasQuestion mapRow(final ResultSet resultSet, final int row) throws SQLException {
						final IliasQuestion q = new IliasQuestion();
						q.setDescription(resultSet.getString("description"));
						q.setId(resultSet.getInt("question_id"));
						q.setPoints(resultSet.getInt("points"));
						q.setText(resultSet.getString("question_text"));
						q.setTimestamp(resultSet.getInt("tstamp"));
						q.setTitle(resultSet.getString("title"));
						q.setType(resultSet.getInt("question_type_fi"));
						q.setAnswers(getAnswers(resultSet.getInt("question_id")));
						q.setFeedback(getFeedback(resultSet.getInt("question_id")));
						return q;
					}
				}
				);
	}

	@Override
	public List<IliasQuestion> getRandomTestQuestions(final int refId) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		final List<IliasQuestion> iliasQuestions = jdbcTemplate.query(
				"SELECT * FROM qpl_questions JOIN object_reference ON (obj_fi = obj_id) WHERE question_id IN "
						+ "(SELECT qst_fi FROM tree AS t1 "
						+ "JOIN tree AS t2 ON t2.lft BETWEEN t1.lft AND t1.rgt AND t1.tree = t2.tree "
						+ "JOIN object_reference AS r ON r.ref_id=t2.child "
						+ "JOIN object_data as d ON d.obj_id=r.obj_id "
						+ "JOIN tst_tests as tests ON tests.obj_fi=r.obj_id "
						+ "JOIN tst_rnd_cpy as rnd ON rnd.tst_fi=tests.test_id "
						+ "WHERE t1.child=?);",
						new String[] {String.valueOf(refId)},
						new RowMapper<IliasQuestion>() {
							@Override
							public IliasQuestion mapRow(final ResultSet resultSet, final int row) throws SQLException {
								final IliasQuestion q = new IliasQuestion();
								q.setDescription(resultSet.getString("description"));
								q.setId(resultSet.getInt("question_id"));
								q.setPoints(resultSet.getInt("points"));
								q.setText(resultSet.getString("question_text"));
								q.setTimestamp(resultSet.getInt("tstamp"));
								q.setTitle(resultSet.getString("title"));
								q.setType(resultSet.getInt("question_type_fi"));
								q.setAnswers(getAnswers(resultSet.getInt("question_id")));
								q.setFeedback(getFeedback(resultSet.getInt("question_id")));
								return q;
							}
						}
				);

		return iliasQuestions;
	}

	@Override
	public Map<String, String> getReferenceIdsWithMetaDataFlag(final String metaDataTitle) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		final Map<String, String> result = new HashMap<String, String>();

		jdbcTemplate.query(
				"SELECT value, ref_id FROM adv_md_values AS v "
						+ "JOIN object_reference AS r ON (v.obj_id = r.obj_id) "
						+ "WHERE field_id = ? AND value = 'yes' OR value = 'no'",
						new String[] {getMetaDataFieldId(metaDataTitle)},
						new RowMapper<String>() {
							@Override
							public String mapRow(final ResultSet resultSet, final int row) throws SQLException {
								return result.put(
										resultSet.getString("ref_id"),
										resultSet.getString("value"));
							}
						}
				);

		return result;
	}

	public List<IliasFeedback> getFeedback(final int questionId) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		return jdbcTemplate.query(
				"SELECT correctness, feedback FROM qpl_fb_generic WHERE question_fi = ?",
				new String[] {String.valueOf(questionId)},
				new RowMapper<IliasFeedback>() {
					@Override
					public IliasFeedback mapRow(final ResultSet resultSet, final int row) throws SQLException {
						return new IliasFeedback(
								resultSet.getString("feedback"),
								resultSet.getBoolean("correctness")
								);
					}
				}
				);
	}

	@Override
	public boolean isRandomQuestionSet(final int refId) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		return jdbcTemplate.queryForObject(
				"SELECT question_set_type FROM tree AS t1 "
						+ "JOIN tree AS t2 ON t2.lft BETWEEN t1.lft AND t1.rgt AND t1.tree = t2.tree "
						+ "JOIN object_reference AS r ON r.ref_id=t2.child "
						+ "JOIN object_data as d ON d.obj_id=r.obj_id "
						+ "JOIN tst_tests as tests ON tests.obj_fi=r.obj_id "
						+ "WHERE t1.child = ?",
						new String[] {String.valueOf(refId)},
						new RowMapper<Boolean>() {
							@Override
							public Boolean mapRow(final ResultSet resultSet, final int row) throws SQLException {
								return "RANDOM_QUEST_SET".equals(resultSet.getString("question_set_type"));
							}
						}
				);
	}

	@Override
	public int getQuestionAmountPerTest(final int refId) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		final List<Integer> list = jdbcTemplate.query(
				"SELECT quest_amount_per_test FROM tree AS t1 "
						+ "JOIN tree AS t2 ON t2.lft BETWEEN t1.lft AND t1.rgt AND t1.tree = t2.tree "
						+ "JOIN object_reference AS r ON r.ref_id=t2.child "
						+ "JOIN object_data as d ON d.obj_id=r.obj_id "
						+ "JOIN tst_tests as tests ON tests.obj_fi=r.obj_id "
						+ "JOIN tst_rnd_quest_set_cfg AS rq ON rq.test_fi=tests.test_id "
						+ "WHERE t1.child = ?",
						new String[] {String.valueOf(refId)},
						new RowMapper<Integer>() {
							@Override
							public Integer mapRow(final ResultSet resultSet, final int row)
									throws SQLException {
								return resultSet.getInt("quest_amount_per_test");
							}
						}
				);

		if (list.size() == 1) {
			return list.get(0);
		}

		return 0;
	}

	private List<IliasAnswer> getAnswers(final int questionId) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		return jdbcTemplate.query(
				"SELECT answertext, points FROM qpl_a_sc WHERE question_fi = ? "
						+ "UNION SELECT answertext, points FROM qpl_a_mc WHERE question_fi = ?",
						new String[] {String.valueOf(questionId), String.valueOf(questionId)},
						new RowMapper<IliasAnswer>() {
							@Override
							public IliasAnswer mapRow(final ResultSet resultSet, final int row) throws SQLException {
								return new IliasAnswer(
										resultSet.getString("answertext"),
										resultSet.getInt("points")
										);
							}
						}
				);
	}

	private String getMetaDataFieldId(final String metaDataTitle) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		final List<String> result = jdbcTemplate.query(
				"SELECT field_id, rec.title FROM adv_md_record AS rec "
						+ "JOIN adv_mdf_definition AS def ON (rec.record_id = def.record_id) "
						+ "WHERE rec.title = ?",
						new String[] {metaDataTitle},
						new RowMapper<String>() {
							@Override
							public String mapRow(final ResultSet resultSet, final int row) throws SQLException {
								return resultSet.getString("field_id");
							}
						}
				);

		if (result.size() == 1) {
			return result.get(0);
		}

		return null;
	}

	private Map<Integer, Integer> getRandomQuestionAmount(final int refId) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		final Map<Integer, Integer> result = new HashMap<Integer, Integer>();

		jdbcTemplate.query(
				"SELECT r.ref_id, quest_amount_per_test AS count FROM tree AS t1"
						+ " JOIN tree AS t2 ON t2.lft BETWEEN t1.lft AND t1.rgt AND t1.tree = t2.tree"
						+ " JOIN object_reference AS r ON r.ref_id=t2.child "
						+ " JOIN object_data as d ON d.obj_id=r.obj_id"
						+ " JOIN qpl_questions as q ON q.obj_fi=r.obj_id"
						+ " JOIN tst_tests as tests ON tests.obj_fi=r.obj_id"
						+ " JOIN tst_rnd_quest_set_cfg AS rq ON rq.test_fi=tests.test_id"
						+ " WHERE t1.child=? GROUP BY r.ref_id;",
						new String[] {String.valueOf(refId)},
						new RowMapper<Integer>() {
							@Override
							public Integer mapRow(final ResultSet resultSet, final int row) throws SQLException {
								result.put(
										resultSet.getInt("ref_id"),
										resultSet.getInt("count"));
								return 0;
							}
						}
				);

		return result;
	}

	private Map<Integer, Integer> getQuestionCount(final int refId) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		final Map<Integer, Integer> result = new HashMap<Integer, Integer>();

		jdbcTemplate.query(
				"SELECT r.ref_id, COUNT(r.ref_id) AS count FROM tree AS t1"
						+ " JOIN tree AS t2 ON t2.lft BETWEEN t1.lft AND t1.rgt AND t1.tree = t2.tree"
						+ " JOIN object_reference AS r ON r.ref_id=t2.child "
						+ " JOIN object_data as d ON d.obj_id=r.obj_id"
						+ " JOIN qpl_questions as q ON q.obj_fi=r.obj_id"
						+ " WHERE t1.child=? GROUP BY r.ref_id;",
						new String[] {String.valueOf(refId)},
						new RowMapper<Integer>() {
							@Override
							public Integer mapRow(final ResultSet resultSet, final int row) throws SQLException {
								result.put(
										resultSet.getInt("ref_id"),
										resultSet.getInt("count"));
								return 0;
							}
						}
				);

		return result;
	}
}
