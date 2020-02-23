package vahovsky.LabBook.persistent;

import java.util.List;

import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;

public interface LaboratoryDAO extends EntityDAO {

	/**
	 * Method adding representation of entity object laboratory into database
	 * 
	 * @param laboratory entity object, the representation of which we want to add
	 *                   into the database
	 */
	void addLaboratory(Laboratory laboratory);

	/**
	 * Method listing all laboratory entities, representations of which are
	 * currently present in database
	 * 
	 * @return list of entity objects based on their corresponding representations
	 *         in database
	 */
	List<Laboratory> getAll();

	/**
	 * Save changes of laboratory's parameters to its corresponding representation
	 * in database
	 * 
	 * @param laboratory entity object, upon which changes were made
	 */
	void saveLaboratory(Laboratory laboratory);

	/**
	 * Method that returns entity object laboratory. This object corresponds to the
	 * representation of laboratory, identified by unique id, currently present in
	 * the database.
	 * 
	 * @param id unique identifier of representation of entity object in database
	 * @return entity object based on its corresponding representation
	 *         (characterized by "id") in database
	 */
	Laboratory getLaboratoryByID(Long id);
	
	/** Method returning the ArrayList of all items stored in a defined Laboratory 
	 * @param laboratory Laboratory, which items we want to list.
	 * @return the list of the items of this laboratory
	 */
	List<Item> getItemsOfLaboratory(Laboratory laboratory);
	
	/**
	 * Method that tests, if the input parameter "name" already exists in the
	 * database as a name of the entity Laboratory.
	 * 
	 * @param name
	 * @return true if the name is not in the database (hence it is available), false if it is
	 */
	boolean isNameAvailable(String name); 

}
