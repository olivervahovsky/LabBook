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

public class MysqlLaboratoryDAOTest {

	/**
	 * Method that tests method <code>getAll()</code> in class
	 * <code>MysqlLaboratoryDAO</code>. Tests if the list of laboratories returned
	 * from the database has size > 0.
	 */
	@Test
	void testGetAll() {
		List<Laboratory> laboratories = DAOfactory.INSTANCE.getLaboratoryDAO().getAll();
		assertNotNull(laboratories);
		assertTrue(laboratories.size() > 0);
	}

	/**
	 * Method that tests method <code>addLaboratory(Laboratory laboratory)</code>
	 * and <code>deleteEntity(Entity laboratory)</code> in a class
	 * <code>MysqlLaboratoryDAO</code>. Test laboratory is created and it is checked
	 * if such a laboratory is not already in the database. It is then added into
	 * the database and it is tested if the addition was successful. Test item is
	 * created, its laboratory is set to be the test laboratory. In the end both the
	 * laboratory and the item are removed from the database and it is tested if the
	 * removal was successful.
	 */
	@Test
	void testAddDelete() {
		Laboratory testLaboratory = new Laboratory();
		testLaboratory.setName("tester");
		testLaboratory.setLocation("testovacia");
		LaboratoryDAO laboratoryDAO = DAOfactory.INSTANCE.getLaboratoryDAO();
		boolean notThere = true;
		List<Laboratory> all = laboratoryDAO.getAll();
		for (Laboratory laboratory : all) {
			if (laboratory.getEntityID().equals(testLaboratory.getEntityID())) {
				notThere = false;
			}
		}
		assertTrue(notThere);

		laboratoryDAO.addLaboratory(testLaboratory);

		Item testItem = new Item();
		testItem.setName("test_item");
		testItem.setQuantity(10);
		testItem.setAvailable(true);
		testItem.setLaboratory(testLaboratory);
		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();
		itemDAO.addItem(testItem);

		all = laboratoryDAO.getAll();
		boolean succesfullyAdded = false;
		for (Laboratory laboratory : all) {
			if (laboratory.getEntityID().equals(testLaboratory.getEntityID())) {
				succesfullyAdded = true;
			}
		}
		assertTrue(succesfullyAdded);

		laboratoryDAO.deleteEntity(testLaboratory);
		all = laboratoryDAO.getAll();
		boolean successfullyDeleted = true;
		for (Laboratory l : all) {
			if (l.getEntityID().equals(testLaboratory.getEntityID())) {
				successfullyDeleted = false;
			}
		}
		assertTrue(successfullyDeleted);
		itemDAO.deleteEntity(testItem);
	}

	/**
	 * Method that tests method <code>saveLaboratory(Laboratory laboratory)</code>
	 * in class <code>MysqlLaboratoryDAO</code>. Test laboratory is made and saved
	 * into the database. First test runs to assert that it was correctly added into
	 * the database. Next some of its parameters are changed and saved. Then we look
	 * for the test laboratory in the database (through its ID) and check, if its
	 * parameters really changed. In the end it is removed from the database.
	 */
	@Test
	void testSave() {
		Laboratory testLaboratory = new Laboratory();
		testLaboratory.setName("tester");
		testLaboratory.setLocation("testovacia");
		LaboratoryDAO laboratoryDAO = DAOfactory.INSTANCE.getLaboratoryDAO();
		// create
		laboratoryDAO.saveLaboratory(testLaboratory);
		assertNotNull(testLaboratory.getEntityID());
		testLaboratory.setName("tester_new");
		// update
		laboratoryDAO.saveLaboratory(testLaboratory);
		List<Laboratory> all = laboratoryDAO.getAll();
		for (Laboratory laboratory : all) {
			if (laboratory.getEntityID().equals(testLaboratory.getEntityID())) {
				assertEquals("tester_new", laboratory.getName());
				laboratoryDAO.deleteEntity(laboratory);
				return;
			}
		}
		assertTrue(false, "update sa nepodaril");
	}

	/**
	 * Method that tests method
	 * <code>getItemsOfLaboratory(Laboratory laboratory)</code> in a class
	 * <code>MysqlLaboratoryDAO</code>. Test laboratory is created and added into
	 * the database. Next, some test items are created and their laboratory is set
	 * to be the test laboratory created previously. Finally, the output of the
	 * method <code>getItemsOfLaboratory(Laboratory laboratory)</code> - a list of
	 * items stored in the laboratory is tested, if each of the items' laboratory ID
	 * equals to test laboratory's ID. Test items and test laboratory are
	 * subsequently removed from the database.
	 */
	@Test
	void testGetItemsOfLaboratory() {
		Laboratory testLaboratory = new Laboratory();
		testLaboratory.setName("tester");
		testLaboratory.setLocation("testovacia");
		LaboratoryDAO laboratoryDAO = DAOfactory.INSTANCE.getLaboratoryDAO();

		laboratoryDAO.addLaboratory(testLaboratory);

		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();

		Item testItem = new Item();
		testItem.setName("test_item");
		testItem.setQuantity(10);
		testItem.setAvailable(true);
		testItem.setLaboratory(testLaboratory);
		itemDAO.addItem(testItem);

		Item testItem1 = new Item();
		testItem1.setName("test_item1");
		testItem1.setQuantity(11);
		testItem1.setAvailable(true);
		testItem1.setLaboratory(testLaboratory);
		itemDAO.addItem(testItem1);

		assertEquals(2, laboratoryDAO.getItemsOfLaboratory(testLaboratory).size());

		itemDAO.deleteEntity(testItem);
		itemDAO.deleteEntity(testItem1);
		laboratoryDAO.deleteEntity(testLaboratory);
	}

	/**
	 * Method that tests method <code>getLaboratoryByID(Long id)</code> in class
	 * <code>MysqlLaboratoryDAO</code>. Test laboratory is made and added into the
	 * database. ID of the test laboratory is saved and compared to the ID of the
	 * laboratory, that is returned by the method
	 * <code>getLaboratoryByID(Long id)</code>. Finally, test laboratory is removed
	 * from the database.
	 */
	@Test
	void testGetLaboratoryByID() {
		Laboratory laboratory = new Laboratory();
		laboratory.setName("test_laboratory");
		laboratory.setLocation("testovacia");

		LaboratoryDAO laboratoryDAO = DAOfactory.INSTANCE.getLaboratoryDAO();
		laboratoryDAO.addLaboratory(laboratory);
		long id = laboratory.getEntityID();
		assertTrue(id == laboratoryDAO.getLaboratoryByID(id).getEntityID());
		laboratoryDAO.deleteEntity(laboratory);
	}

	/**
	 * Method that tests method <code>isNameAvailable(String name)</code> in class
	 * <code>MysqlLaboratoryDAO</code>. First, availability of the future test
	 * laboratory's name is tested. Then test item with that name is added into the
	 * database. Next, availability of that name is tested again, this time negative
	 * result is expected. Finally, test item is removed from the database.
	 */
	@Test
	void testIsNameAvailable() {
		LaboratoryDAO laboratoryDAO = DAOfactory.INSTANCE.getLaboratoryDAO();
		assertTrue(laboratoryDAO.isNameAvailable("test_laboratory"));
		Laboratory testLaboratory = new Laboratory();
		testLaboratory.setName("test_laboratory");
		testLaboratory.setLocation("testovacia");
		laboratoryDAO.addLaboratory(testLaboratory);
		assertFalse(laboratoryDAO.isNameAvailable("test_laboratory"));
		laboratoryDAO.deleteEntity(testLaboratory);
	}

}
