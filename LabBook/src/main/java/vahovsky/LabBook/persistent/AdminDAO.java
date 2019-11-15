package vahovsky.LabBook.persistent;

import java.util.List;

import vahovsky.LabBook.entities.Admin;

public interface AdminDAO {

	/**
	 * Method adding representation of entity object admin into database
	 * 
	 * @param admin entity object, the representation of which we want to add into
	 *              the database
	 */
	void addAdmin(Admin admin);

	/**
	 * Method listing all admin entities, representations of which are currently
	 * present in database
	 * 
	 * @return list of entity objects based on their corresponding representations
	 *         in database
	 */
	List<Admin> getAll();

	/**
	 * Save changes of admin's parameters to its corresponding representation in
	 * database
	 * 
	 * @param admin entity object, upon which changes were made
	 */
	void saveAdmin(Admin admin);

	/**
	 * Method to delete representation of admin entity object from database
	 * 
	 * @param admin entity object, representation of which is to be removed from
	 *              database
	 */
	void deleteAdmin(Admin admin);

}
