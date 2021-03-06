package vahovsky.LabBook.test.persistent;

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
	
	@Test
	void testGetAll() {
		List<Laboratory> laboratories = DAOfactory.INSTANCE.getLaboratoryDAO().getAll();
		assertNotNull(laboratories);
		assertTrue(laboratories.size() > 0);
	}
	
	@Test
	void addDeleteTest() {
		Laboratory testLaboratory = new Laboratory();
		testLaboratory.setName("tester");
		testLaboratory.setLocation("testovacia");
		LaboratoryDAO laboratoryDAO = DAOfactory.INSTANCE.getLaboratoryDAO();
		boolean notThere = true;
		List<Laboratory> all = laboratoryDAO.getAll();
		for (Laboratory l : all) {
			if (l.getLaboratoryID().equals(testLaboratory.getLaboratoryID())) {
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
		for (Laboratory l : all) {
			if (l.getLaboratoryID().equals(testLaboratory.getLaboratoryID())) {
				succesfullyAdded = true;
			}
		}
		assertTrue(succesfullyAdded);

		laboratoryDAO.deleteLaboratory(testLaboratory);
		all = laboratoryDAO.getAll();
		boolean successfullyDeleted = true;
		for (Laboratory l : all) {
			if (l.getLaboratoryID().equals(testLaboratory.getLaboratoryID())) {
				successfullyDeleted = false;
			}
		}
		assertTrue(successfullyDeleted);
		itemDAO.deleteItem(testItem);
	}
	
	@Test
	void testSave() {
		Laboratory testLaboratory = new Laboratory();
		testLaboratory.setName("tester");
		testLaboratory.setLocation("testovacia");
		LaboratoryDAO laboratoryDAO = DAOfactory.INSTANCE.getLaboratoryDAO();
		// create
		laboratoryDAO.saveLaboratory(testLaboratory);
		assertNotNull(testLaboratory.getLaboratoryID());
		testLaboratory.setName("tester_new");
		// update
		laboratoryDAO.saveLaboratory(testLaboratory);
		List<Laboratory> all = laboratoryDAO.getAll();
		for (Laboratory l : all) {
			if (l.getLaboratoryID().equals(testLaboratory.getLaboratoryID())) {
				assertEquals("tester_new", l.getName());
				laboratoryDAO.deleteLaboratory(l);
				return;
			}
		}
		assertTrue(false, "update sa nepodaril");
	}
	
	@Test
	void getItemsOfLaboratoryTest() {
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

		List<Item> items = laboratoryDAO.getItemsOfLaboratory(testLaboratory);
		int numberOfItems = 0;
		for (Item i : items) {
			if (i.getLaboratory().getLaboratoryID().equals(testLaboratory.getLaboratoryID())) {
				numberOfItems++;
			}
		}
		assertEquals(2, numberOfItems);

		// zmazem testovacie data
		laboratoryDAO.deleteLaboratory(testLaboratory);
		itemDAO.deleteItem(testItem);
		itemDAO.deleteItem(testItem1);

	}

}
