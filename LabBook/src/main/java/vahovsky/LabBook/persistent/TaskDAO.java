package vahovsky.LabBook.persistent;

import java.util.List;

import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Task;

public interface TaskDAO extends EntityDAO {

	/**
	 * Method adding representation of entity object task into database
	 * 
	 * @param task entity object, the representation of which we want to add into
	 *             the database
	 */
	void addTask(Task task);

	/**
	 * Save changes of task's parameters to its corresponding representation in
	 * database
	 * 
	 * @param task entity object, upon which changes were made
	 */
	void saveTask(Task task);

	/**
	 * Method listing all task entities, representations of which are currently
	 * present in database
	 * 
	 * @return list of entity objects based on their corresponding representations
	 *         in database
	 */
	List<Task> getAll();

	/**
	 * Method that returns entity object task. This object corresponds to the
	 * representation of task, identified by unique id, currently present in the
	 * database.
	 * 
	 * @param id unique identifier of representation of entity object in database
	 * @return entity object based on its corresponding representation
	 *         (characterized by "id") in database
	 */
	Task getByID(Long id);

	/**
	 * Method that gets items of the task according to the table task_has_item in
	 * the database.
	 * 
	 * @param task the task, identified uniquely in the table task_has_item, to
	 *             which we are getting items (according to the reference to this
	 *             task in the table task_has_item)
	 */
	List<Item> getItems(Task task);

	/**
	 * Tests if provided name of the task is unique among all the tasks in database.
	 * 
	 * @param name title of the task to be tested
	 * @return true, if provided name is unique, false if it is not
	 */
	boolean isNameAvailable(String name);
	
	/**
	 * Method returning list of notes belonging to a task.
	 * 
	 * @param task Task, notes belonging to which we want to see
	 * @return List of task's notes
	 */
	List<Note> getNotes(Task task);

}
