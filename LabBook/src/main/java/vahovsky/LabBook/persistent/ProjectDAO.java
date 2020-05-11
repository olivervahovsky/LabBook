package vahovsky.LabBook.persistent;

import java.util.List;

import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;

public interface ProjectDAO extends EntityDAO {

	/**
	 * Method adding representation of entity object project into database
	 * 
	 * @param project entity object, the representation of which we want to add into
	 *                the database
	 */
	void addProject(Project project);

	/**
	 * Save changes of project's parameters to its corresponding representation in
	 * database
	 * 
	 * @param project entity object, upon which changes were made
	 */
	void saveProject(Project project);

	/**
	 * Method listing all project entities, representations of which are currently
	 * present in database
	 * 
	 * @return list of entity objects based on their corresponding representations
	 *         in database
	 */
	List<Project> getAll();

	/**
	 * Method that returns entity object project. This object corresponds to the
	 * representation of project, identified by unique id, currently present in the
	 * database.
	 * 
	 * @param id unique identifier of representation of entity object in database
	 * @return entity object based on its corresponding representation
	 *         (characterized by "id") in database
	 */
	Project getByID(Long id);

	/**
	 * Method that returns projects of currently logged-in user/administrator
	 * 
	 * @return List of projects of currently logged-in user/administrator
	 */
	List<Project> getProjectsOfLoggedUser();

	/**
	 * Method, that returns all the tasks of the project specified.
	 * 
	 * @param project, whose tasks we want to have returned
	 * @return list of all the tasks of the project specified
	 */
	List<Task> getTasksOfProject(Project project);

}
