package de.thm.arsnova.connector.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

import de.thm.arsnova.connector.model.IliasCategoryNode;
import de.thm.arsnova.connector.model.IliasQuestion;

public interface UniRepDao {
	/** Returns a flat dump of the Ilias repository tree.
	 *
	 * @param refId The root nodes ID as reference ID
	 * @return A list of category nodes
	 */
	List<IliasCategoryNode> getTreeObjects(int refId);

	/** Returns a list of questions identified by the parent question pool reference ID
	 *
	 * @param refId The reference id of the question pool containing this question
	 * @return A list of questions containing the question, possible answers and feedback.
	 */
	List<IliasQuestion> getQuestion(int refId);
	
	List<IliasQuestion> getRandomTestQuestions(int refId);

	/** Returns a map containing reference ids and the value of the meta data
	 *
	 * @param metaDataTitle The meta data to be returned
	 * @return A map with reference id - meta data value pairs
	 */
	Map<String, String> getReferenceIdsWithMetaDataFlag(String metaDataTitle);

	/** Returns TRUE if referenced repository object is a test with random question selection
	 *
	 * @param refId The reference id of the test to be checked
	 * @return TRUE if this object is a test with random question selection
	 */
	boolean isRandomQuestionSet(int refId);

	/** Marks classes implementing {@link UniRepDao} to filter results
	 *
	 * @author Paul-Christian Volkmer
	 * @since 0.50.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface Filter {

		/** Usable values for {@link Filter}
		 */
		public enum Type {
			/** Filter to limit result set to question pools */
			QUESTION_POOL,
			/** Filter to limit result set to tests */
			TEST
		}

		/** Declares wich kind of tree objects should be returned
		 */
		public Type value();
	}

	
}
