package de.thm.arsnova.connector.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import de.thm.arsnova.connector.model.IliasAnswer;
import de.thm.arsnova.connector.model.IliasCategoryNode;
import de.thm.arsnova.connector.model.IliasFeedback;
import de.thm.arsnova.connector.model.IliasQuestion;


public class IliasConnectorDaoImpl implements UniRepDao {

	@Autowired
	private DataSource dataSource;

	@Override
	public List<IliasCategoryNode> getTreeObjects(int refId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		List<IliasCategoryNode> nodeList = jdbcTemplate.query(
				"SELECT t2.*, d.type, d.title FROM tree AS t1"
						+ " JOIN tree AS t2 ON t2.lft BETWEEN t1.lft AND t1.rgt AND t1.tree = t2.tree"
						+ " JOIN object_reference AS r ON r.ref_id=t2.child "
						+ " JOIN object_data as d ON d.obj_id=r.obj_id"
						+ " WHERE t1.child=? ORDER BY parent;",
						new String[] {String.valueOf(refId)},
						new RowMapper<IliasCategoryNode>() {
							@Override
							public IliasCategoryNode mapRow(ResultSet resultSet, int row) throws SQLException {
								return new IliasCategoryNode(
										resultSet.getInt("child"),
										resultSet.getInt("parent"),
										resultSet.getString("title"),
										resultSet.getString("type")
										);
							}
						}
				);

		for (IliasCategoryNode node : nodeList) {
			for (IliasCategoryNode n : nodeList) {
				if (node.getParent() == n.getChild()) {
					n.addChild(node.getChild());
				}
			}
		}

		return nodeList;
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
}
