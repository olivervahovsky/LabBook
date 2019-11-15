package vahovsky.LabBook.persistent;

import java.util.List;

import vahovsky.LabBook.entities.Item;

public interface ItemDAO {

	/**
	 * Method adding representation of entity object item into database
	 * 
	 * @param item entity object, the representation of which we want to add into
	 *             the database
	 */
	void addItem(Item item);

	/**
	 * Save changes of item's parameters to its corresponding representation in
	 * database
	 * 
	 * @param item entity object, upon which changes were made
	 */
	void saveItem(Item item);

	/**
	 * Method listing all item entities, representations of which are currently
	 * present in database
	 * 
	 * @return list of entity objects based on their corresponding representations
	 *         in database
	 */
	List<Item> getAll();

	/**
	 * Method to delete representation of item entity object from database
	 * 
	 * @param item entity object, representation of which is to be removed from
	 *             database
	 */
	void deleteItem(Item item);

	/**
	 * Method that returns entity object item. This object corresponds to the
	 * representation of item, identified by unique id, currently present in the
	 * database.
	 * 
	 * @param id unique identifier of representation of entity object in database
	 * @return entity object based on its corresponding representation (characterized by "id") in database
	 */
	Item getByID(Long id);

}
