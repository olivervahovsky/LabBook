package vahovsky.LabBook.test.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import vahovsky.LabBook.business.LaboratoryManager;
import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ItemDAO;
import vahovsky.LabBook.persistent.LaboratoryDAO;

public class LaboratoryManagerTest {

	@Test
	void addDeleteTest() {
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

		List<Item> items = LaboratoryManager.getItemsOfLaboratory(testLaboratory);
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
