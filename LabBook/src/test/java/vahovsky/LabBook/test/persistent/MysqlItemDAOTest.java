package vahovsky.LabBook.test.persistent;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ItemDAO;
import vahovsky.LabBook.persistent.LaboratoryDAO;

public class MysqlItemDAOTest {

	/**
	 * Method that tests method <code>getAll()</code> in class
	 * <code>MysqlItemDAO</code>. Tests if the list of items returned from the
	 * database has size > 0.
	 */
	@Test
	void testGetAll() {
		List<Item> items = DAOfactory.INSTANCE.getItemDAO().getAll();
		assertNotNull(items);
		assertTrue(items.size() > 0);
	}

	/**
	 * Method that tests method <code>addItem(Item item)</code> and
	 * <code>deleteEntity(Entity item)</code> in a class <code>MysqlItemDAO</code>.
	 * Test laboratory (each item has to belong to a laboratory) is created. Test
	 * item is created, its laboratory is set to be the test laboratory and it is
	 * checked if such an item is not already in the database. It is then added into
	 * the database and it is tested if the addition was successful. In the end it
	 * is removed from the database and it is tested if the removal was successful.
	 */
	@Test
	void testAddDelete() {

		Laboratory lab = new Laboratory();
		lab.setName("test_lab");
		LaboratoryDAO laboratoryDAO = DAOfactory.INSTANCE.getLaboratoryDAO();
		laboratoryDAO.addLaboratory(lab);

		Item testItem = new Item();
		testItem.setName("test_item");
		testItem.setQuantity(10);
		testItem.setAvailable(true);
		testItem.setLaboratory(lab);
		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();

		boolean notThere = true;
		List<Item> all = itemDAO.getAll();
		for (Item item : all) {
			if (item.getEntityID().equals(testItem.getEntityID())) {
				notThere = false;
			}
		}
		assertTrue(notThere);

		itemDAO.addItem(testItem);
		all = itemDAO.getAll();
		boolean succesfullyAdded = false;
		for (Item item : all) {
			if (item.getEntityID().equals(testItem.getEntityID())) {
				succesfullyAdded = true;
			}
		}
		assertTrue(succesfullyAdded);

		itemDAO.deleteEntity(testItem);
		laboratoryDAO.deleteEntity(lab);
		all = itemDAO.getAll();
		boolean successfullyDeleted = true;
		for (Item item : all) {
			if (item.getEntityID().equals(testItem.getEntityID())) {
				successfullyDeleted = false;
			}
		}
		assertTrue(successfullyDeleted);
		laboratoryDAO.deleteEntity(lab);

	}

	/**
	 * Method that tests method <code>saveItem(Item item)</code> in class
	 * <code>MysqlItemDAO</code>. Test item is made and saved into the database.
	 * First test runs to assert that it was correctly added into the database. Next
	 * some of its parameters are changed and saved. Then we look for the test item
	 * in the database (through his ID) and check, if its parameters really changed.
	 * In the end it is removed from the database.
	 */
	@Test
	void testSave() {

		Item testItem = new Item();
		testItem.setName("test_item");
		testItem.setQuantity(10);
		testItem.setAvailable(true);

		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();
		// create
		itemDAO.saveItem(testItem);
		assertNotNull(testItem.getEntityID());
		testItem.setName("test_item_new");
		// update
		itemDAO.saveItem(testItem);
		List<Item> all = itemDAO.getAll();
		for (Item item : all) {
			if (item.getEntityID().equals(testItem.getEntityID())) {
				assertEquals("test_item_new", item.getName());
				itemDAO.deleteEntity(item);
				return;
			}
		}
		assertTrue(false, "update sa nepodaril");
	}

	/**
	 * Method that tests method <code>getByID(Long id)</code> in class
	 * <code>MysqlItemDAO</code>. Test item is made and added into the database. ID
	 * of the test item is saved and compared to the ID of the item, that is
	 * returned by the method <code>getByID(Long id)</code>. Finally, test item is
	 * removed from the database.
	 */
	@Test
	void testGetByID() {
		Item testItem = new Item();
		testItem.setName("test_item");
		testItem.setQuantity(10);
		testItem.setAvailable(true);

		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();
		itemDAO.addItem(testItem);
		long id = testItem.getEntityID();
		assertTrue(id == itemDAO.getByID(id).getEntityID());
		itemDAO.deleteEntity(testItem);
	}

	/**
	 * Method that tests method <code>isNameAvailable(String name)</code> in class
	 * <code>MysqlItemDAO</code>. First, availability of the future test item's
	 * name is tested. Then test item with that name is added into the database.
	 * Next, availability of that name is tested again, this time negative result is
	 * expected. Finally, test item is removed from the database.
	 */
	@Test
	void testIsNameAvailable() {
		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();
		assertTrue(itemDAO.isNameAvailable("test_item"));
		Item testItem = new Item();
		testItem.setName("test_item");
		testItem.setQuantity(1);
		testItem.setAvailable(true);
		itemDAO.addItem(testItem);
		assertFalse(itemDAO.isNameAvailable("test_item"));
		itemDAO.deleteEntity(testItem);
	}

}
