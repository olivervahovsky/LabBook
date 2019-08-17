package vahovsky.LabBook.test.persistent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ItemDAO;
import vahovsky.LabBook.persistent.LaboratoryDAO;
import vahovsky.LabBook.persistent.NoteDAO;
import vahovsky.LabBook.persistent.UserDAO;

public class MysqlItemDAOTest {
	
	@Test
	void testGetAll() {
		List<Item> items = DAOfactory.INSTANCE.getItemDAO().getAll();
		assertNotNull(items);
		assertTrue(items.size() > 0);
	}
	
	@Test
	void addDeleteTest() {
		User testUser = new User();
		testUser.setName("testerAddDelete");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);
		
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
		
		Note note = new Note();
		note.setText("testovaci text");
		note.setTimestamp(LocalDateTime.now());
		note.setAuthor(testUser);
		note.setItem(testItem);
		NoteDAO noteDAO = DAOfactory.INSTANCE.getNoteDAO();
		noteDAO.addNote(note);
		
		boolean notThere = true;
		List<Item> all = itemDAO.getAll();
		for (Item i : all) {
			if (i.getItemID().equals(testItem.getItemID())) {
				notThere = false;
			}
		}
		assertTrue(notThere);

		itemDAO.addItem(testItem);
		all = itemDAO.getAll();
		boolean succesfullyAdded = false;
		for (Item i : all) {
			if (i.getItemID().equals(testItem.getItemID())) {
				succesfullyAdded = true;
			}
		}
		assertTrue(succesfullyAdded);

		itemDAO.deleteItem(testItem);
		laboratoryDAO.deleteLaboratory(lab);
		all = itemDAO.getAll();
		boolean successfullyDeleted = true;
		for (Item i : all) {
			if (i.getItemID().equals(testItem.getItemID())) {
				successfullyDeleted = false;
			}
		}
		assertTrue(successfullyDeleted);
		laboratoryDAO.deleteLaboratory(lab);
		noteDAO.deleteNote(note);
		userDAO.deleteUser(testUser);
	}
	
	@Test
	void testSave() {
		
		Item testItem = new Item();
		testItem.setName("test_item");
		testItem.setQuantity(10);
		testItem.setAvailable(true);

		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();
		// create
		itemDAO.saveItem(testItem);
		assertNotNull(testItem.getItemID());
		testItem.setName("test_item_new");
		// update
		itemDAO.saveItem(testItem);
		List<Item> all = itemDAO.getAll();
		for (Item u : all) {
			if (u.getItemID().equals(testItem.getItemID())) {
				assertEquals("test_item_new", u.getName());
				itemDAO.deleteItem(u);
				return;
			}
		}
		assertTrue(false, "update sa nepodaril");
	}
	
	@Test
	void testGetByID() {
		Item testItem = new Item();
		testItem.setName("test_item");
		testItem.setQuantity(10);
		testItem.setAvailable(true);
		
		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();
		itemDAO.addItem(testItem);
		long id = testItem.getItemID();
		assertTrue(id == itemDAO.getByID(id).getItemID());
		itemDAO.deleteItem(testItem);
	}

}
