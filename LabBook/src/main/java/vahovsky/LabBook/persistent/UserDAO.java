package vahovsky.LabBook.persistent;

import java.util.List;

import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.entities.User;

public interface UserDAO {

	/**
	 * Method adding representation of entity object user into database
	 * 
	 * @param user entity object, the representation of which we want to add into
	 *             the database
	 */
	void addUser(User user);

	/**
	 * Method listing all user entities, representations of which are currently
	 * present in database
	 * 
	 * @return list of entity objects based on their corresponding representations
	 *         in database
	 */
	List<User> getAll();

	/**
	 * Save changes of user's parameters to its corresponding representation in
	 * database
	 * 
	 * @param user entity object, upon which changes were made
	 */
	void saveUser(User user);

	/**
	 * Method to delete representation of user entity object from database
	 * 
	 * @param user entity object, representation of which is to be removed from
	 *             database
	 */
	void deleteUser(User user);

	/**
	 * Method that returns entity object user. This object corresponds to the
	 * representation of user, identified by unique id, currently present in the
	 * database.
	 * 
	 * @param id unique identifier of representation of entity object in database
	 * @return entity object based on its corresponding representation
	 *         (characterized by "id") in database
	 */
	User getByID(Long id);

	/**
	 * Method that returns entity object user. This object corresponds to the
	 * representation of user, identified by unique email, currently present in the
	 * database.
	 * 
	 * @param email unique email of representation of entity object in database
	 * @return entity object based on its corresponding representation
	 *         (characterized by "email") in database
	 */
	User getByEmail(String email);

	/**
	 * Method that returns list of the projects of defined user.
	 * 
	 * @param user User, whose projects we want to list.
	 * @return implementation of List<> containing all the projects of defined user.
	 */
	List<Project> getProjects(User user);

	/**
	 * Method that returns list of the tasks of defined user.
	 * 
	 * @param user User, whose projects we want to list.
	 * @return implementation of List<> containing all the tasks of defined user.
	 */
	List<Task> getTasks(User user);

	/**
	 * Method that returns list of the notes of defined user.
	 * 
	 * @param user User, whose notes we want to list.
	 * @return implementation of List<> containing all the notes of defined user.
	 */
	List<Note> getNotes(User user);

	/**
	 * Method that returns list of all user emails in database.
	 * 
	 * @return implementation of List<> containing all user emails currently present
	 *         in database.
	 */
	List<String> getAllEmails();

	/**
	 * Method that returns list of all usernames in database.
	 * 
	 * @return implementation of List<> containing all usernames currently present
	 *         in database.
	 */
	List<String> getAllNames();

}
