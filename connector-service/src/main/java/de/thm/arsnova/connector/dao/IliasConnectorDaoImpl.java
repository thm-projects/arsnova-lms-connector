package de.thm.arsnova.connector.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import de.thm.arsnova.connector.dao.UniRepDao.Filter;
import de.thm.arsnova.connector.dao.UniRepDao.Filter.Type;
import de.thm.arsnova.connector.model.IliasAnswer;
import de.thm.arsnova.connector.model.IliasCategoryNode;
import de.thm.arsnova.connector.model.IliasFeedback;
import de.thm.arsnova.connector.model.IliasQuestion;

@Filter(Type.QUESTION_POOL)
public class IliasConnectorDaoImpl implements UniRepDao {

	@Autowired
	private DataSource dataSource;

	@Override
	public List<IliasCategoryNode> getTreeObjects(int refId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		String typefilter = "";

		if (this.getClass().getAnnotation(UniRepDao.Filter.class) != null) {
			switch (this.getClass().getAnnotation(UniRepDao.Filter.class).value()) {
			case QUESTION_POOL:
				typefilter = " AND type <> 'tst'";
				break;
			case TEST:
				typefilter = " AND type <> 'qpl'";
				break;
			}
		}

		final Map<Integer, Integer> questionCount = this.getQuestionCount(refId);
		List<IliasCategoryNode> nodeList = jdbcTemplate.query(
				"SELECT t2.*, d.type, d.title FROM tree AS t1"
						+ " JOIN tree AS t2 ON t2.lft BETWEEN t1.lft AND t1.rgt AND t1.tree = t2.tree"
						+ " JOIN object_reference AS r ON r.ref_id=t2.child "
						+ " JOIN object_data as d ON d.obj_id=r.obj_id"
						+ typefilter
						+ " WHERE t1.child=? ORDER BY parent;",
						new String[] {String.valueOf(refId)},
						new RowMapper<IliasCategoryNode>() {
							@Override
							public IliasCategoryNode mapRow(ResultSet resultSet, int row) throws SQLException {
								return new IliasCategoryNode(
										resultSet.getInt("child"),
										resultSet.getInt("parent"),
										resultSet.getString("title"),
										resultSet.getString("type"),
										questionCount.get(resultSet.getInt("child")) == null ? 0 : questionCount.get(resultSet.getInt("child"))
										);

							}
						}
				);

		for (IliasCategoryNode node : nodeList) {
			for (IliasCategoryNode parentNode : nodeList) {
				if (node.getParent() == parentNode.getId()) {
					parentNode.addChild(node);
				}
			}
		}

		List<IliasCategoryNode> result = new ArrayList<>();
		if (nodeList.size() > 0) result.add(nodeList.get(0));

		return result;
	}


	@Override
	public List<IliasQuestion> getQuestion(int refId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		return jdbcTemplate.query(
				"SELECT * FROM qpl_questions JOIN object_reference ON (obj_fi = obj_id) WHERE ref_id=?;",
				new String[] {String.valueOf(refId)},
				new RowMapper<IliasQuestion>() {
					@Override
					public IliasQuestion mapRow(ResultSet resultSet, int row) throws SQLException {
						IliasQuestion q = new IliasQuestion();
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
	public List<IliasQuestion> getRandomTestQuestions(int refId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		List<IliasQuestion> iliasQuestions = jdbcTemplate.query(
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
						public IliasQuestion mapRow(ResultSet resultSet, int row) throws SQLException {
							IliasQuestion q = new IliasQuestion();
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
	public Map<String, String> getReferenceIdsWithMetaDataFlag(String metaDataTitle) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		final Map<String, String> result = new HashMap<String, String>();

		jdbcTemplate.query(
				"SELECT value, ref_id FROM adv_md_values AS v JOIN object_reference AS r ON (v.obj_id = r.obj_id) WHERE field_id = ? AND value = 'yes' OR value = 'no'",
				new String[] {getMetaDataFieldId(metaDataTitle)},
				new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet resultSet, int row) throws SQLException {
						return result.put(
								resultSet.getString("ref_id"),
								resultSet.getString("value"));
					}
				}
				);

		return result;
	}

	public List<IliasFeedback> getFeedback(int questionId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		return jdbcTemplate.query(
				"SELECT correctness, feedback FROM qpl_fb_generic WHERE question_fi = ?",
				new String[] {String.valueOf(questionId)},
				new RowMapper<IliasFeedback>() {
					@Override
					public IliasFeedback mapRow(ResultSet resultSet, int row) throws SQLException {
						return new IliasFeedback(
								resultSet.getString("feedback"),
								resultSet.getBoolean("correctness")
								);
					}
				}
				);
	}

	@Override
	public boolean isRandomQuestionSet(int refId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		jdbcTemplate.query(
				"SELECT question_set_type FROM tree AS t1 "
						+ "JOIN tree AS t2 ON t2.lft BETWEEN t1.lft AND t1.rgt AND t1.tree = t2.tree "
						+ "JOIN object_reference AS r ON r.ref_id=t2.child "
						+ "JOIN object_data as d ON d.obj_id=r.obj_id "
						+ "JOIN tst_tests as tests ON tests.obj_fi=r.obj_id "
						+ "WHERE t1.child = ?",
						new String[] {String.valueOf(refId)},
						new ResultSetExtractor<Boolean>() {
							@Override
							public Boolean extractData(ResultSet resultSet)
									throws SQLException, DataAccessException {
								return "RANDOM_QUEST_SET".equals(resultSet.getString("question_set_type"));
							}
						}
				);
		return false;
	}

	private List<IliasAnswer> getAnswers(int questionId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		return jdbcTemplate.query(
				"SELECT answertext, points FROM qpl_a_sc WHERE question_fi = ? "
						+ "UNION SELECT answertext, points FROM qpl_a_mc WHERE question_fi = ?",
						new String[] {String.valueOf(questionId), String.valueOf(questionId)},
						new RowMapper<IliasAnswer>() {
							@Override
							public IliasAnswer mapRow(ResultSet resultSet, int row) throws SQLException {
								return new IliasAnswer(
										resultSet.getString("answertext"),
										resultSet.getInt("points")
										);
							}
						}
				);
	}

	private String getMetaDataFieldId(String metaDataTitle) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<String> result = jdbcTemplate.query(
				"SELECT field_id, rec.title FROM adv_md_record AS rec JOIN adv_mdf_definition AS def ON (rec.record_id = def.record_id) WHERE rec.title = ?",
				new String[] {metaDataTitle},
				new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet resultSet, int row) throws SQLException {
						return resultSet.getString("field_id");
					}
				}
				);

		if (result.size() == 1) {
			return result.get(0);
		}

		return null;
	}

	private Map<Integer, Integer> getQuestionCount(int refId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

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
							public Integer mapRow(ResultSet resultSet, int row) throws SQLException {
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
