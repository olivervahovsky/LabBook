package vahovsky.LabBook.persistent;

import java.util.List;

import vahovsky.LabBook.entities.Task;

public interface TaskDAO {

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
	 * Method to delete representation of task entity object from database
	 * 
	 * @param task entity object, representation of which is to be removed from
	 *             database
	 */
	void deleteTask(Task task);

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

}
