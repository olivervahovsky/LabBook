package vahovsky.LabBook.persistent;

import java.util.List;

import vahovsky.LabBook.entities.Project;

public interface ProjectDAO {

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
	 * Method to delete representation of project entity object from database
	 * 
	 * @param project entity object, representation of which is to be removed from
	 *                database
	 */
	void deleteProject(Project project);

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

}
