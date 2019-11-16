package vahovsky.LabBook.test.persistent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ItemDAO;
import vahovsky.LabBook.persistent.LaboratoryDAO;
import vahovsky.LabBook.persistent.ProjectDAO;
import vahovsky.LabBook.persistent.TaskDAO;
import vahovsky.LabBook.persistent.UserDAO;

public class MysqlTaskDAOTest {

	@Test
	void testGetAll() {
		List<Task> tasks = DAOfactory.INSTANCE.getTaskDAO().getAll();
		assertNotNull(tasks);
		assertTrue(tasks.size() > 0);
	}

	@Test
	void addDeleteTest() {
		User testUser = new User();
		testUser.setName("testerGetByID");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);
		
		Project project = new Project();
		project.setName("testovaci_projekt");
		project.setActive(true);
		project.setDateFrom(LocalDate.now());
		project.setEachItemAvailable(false);
		project.setCreatedBy(testUser);
		ProjectDAO projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(project);
		
		Laboratory testLaboratory = new Laboratory();
		testLaboratory.setName("tester");
		testLaboratory.setLocation("testovacia");
		LaboratoryDAO laboratoryDAO = DAOfactory.INSTANCE.getLaboratoryDAO();
		laboratoryDAO.addLaboratory(testLaboratory);
		
		Item testItem = new Item();
		testItem.setName("test_item1");
		testItem.setQuantity(10);
		testItem.setAvailable(true);
		testItem.setLaboratory(testLaboratory);

		Item testItem2 = new Item();
		testItem2.setName("test_item2");
		testItem2.setQuantity(10);
		testItem2.setAvailable(true);
		
		Item testItem3 = new Item();
		testItem3.setName("test_item3");
		testItem3.setQuantity(10);
		testItem3.setAvailable(true);
		
		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();
		itemDAO.addItem(testItem);
		itemDAO.addItem(testItem2);
		itemDAO.addItem(testItem3);

		Task task = new Task();
		task.setProject(project);
		task.setName("testTask");
		task.setActive(true);
		task.setDateTimeFrom(LocalDate.now());
		task.setEachItemAvailable(false);
		task.setCreatedBy(testUser);
		task.setItems(Arrays.asList(testItem, testItem2, testItem3));
		TaskDAO taskDAO = DAOfactory.INSTANCE.getTaskDAO();
		taskDAO.addTask(task);
		boolean succesfullyAdded = false;
		List<Task> all = taskDAO.getAll();
		for (Task t : all) {
			if (t.getTaskID().equals(task.getTaskID())) {
				succesfullyAdded = true;
			}
		}
		assertTrue(succesfullyAdded);
		assertTrue(task.getItems().size() == 3);

		taskDAO.deleteTask(task);
		all = taskDAO.getAll();
		boolean successfullyDeleted = true;
		for (Task t : all) {
			if (t.getTaskID().equals(task.getTaskID())) {
				successfullyDeleted = false;
			}
		}
		projectDAO.deleteProject(project);
		userDAO.deleteUser(testUser);
		itemDAO.deleteItem(testItem);
		itemDAO.deleteItem(testItem2);
		itemDAO.deleteItem(testItem3);
		laboratoryDAO.deleteLaboratory(testLaboratory);
		assertTrue(successfullyDeleted);
	}

	@Test
	void testSave() {
		User testUser = new User();
		testUser.setName("testerGetByID");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);
		
		Project project = new Project();
		project.setName("testovaci_projekt");
		project.setActive(true);
		project.setDateFrom(LocalDate.now());
		project.setEachItemAvailable(false);
		project.setCreatedBy(testUser);
		ProjectDAO projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(project);
		
		Item testItem = new Item();
		testItem.setName("test_item1");
		testItem.setQuantity(10);
		testItem.setAvailable(true);
		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();
		itemDAO.addItem(testItem);

		Task task = new Task();
		task.setProject(project);
		task.setName("testTask");
		task.setActive(true);
		task.setDateTimeFrom(LocalDate.now());
		task.setEachItemAvailable(false);
		task.setCreatedBy(testUser);
		task.setItems(Arrays.asList(testItem));
		TaskDAO taskDAO = DAOfactory.INSTANCE.getTaskDAO();
		// create
		taskDAO.saveTask(task);
		assertNotNull(task.getTaskID());
		assertTrue(task.getItems().size() == 1);
		task.setName("testovaci_task_new");
		// update
		taskDAO.saveTask(task);
		List<Task> all = taskDAO.getAll();
		for (Task t : all) {
			if (t.getTaskID().equals(task.getTaskID())) {
				assertEquals("testovaci_task_new", t.getName());
				userDAO.deleteUser(testUser);
				itemDAO.deleteItem(testItem);
				return;
			}
		}
		assertTrue(false, "update sa nepodaril");
	}
	
	@Test
	void testGetByID() {
		User testUser = new User();
		testUser.setName("testerGetByID");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);
		
		Project project = new Project();
		project.setName("testovaci_projekt");
		project.setActive(true);
		project.setDateFrom(LocalDate.now());
		project.setEachItemAvailable(false);
		project.setCreatedBy(testUser);
		ProjectDAO projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(project);
		
		Laboratory testLaboratory = new Laboratory();
		testLaboratory.setName("tester");
		testLaboratory.setLocation("testovacia");
		LaboratoryDAO laboratoryDAO = DAOfactory.INSTANCE.getLaboratoryDAO();
		laboratoryDAO.addLaboratory(testLaboratory);
		
		Item testItem = new Item();
		testItem.setName("test_item1");
		testItem.setQuantity(10);
		testItem.setAvailable(true);
		testItem.setLaboratory(testLaboratory);

		Item testItem2 = new Item();
		testItem2.setName("test_item2");
		testItem2.setQuantity(10);
		testItem2.setAvailable(true);
		
		Item testItem3 = new Item();
		testItem3.setName("test_item3");
		testItem3.setQuantity(10);
		testItem3.setAvailable(true);
		
		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();
		itemDAO.addItem(testItem);
		itemDAO.addItem(testItem2);
		itemDAO.addItem(testItem3);

		Task task = new Task();
		task.setProject(project);
		task.setName("testTask");
		task.setActive(true);
		task.setDateTimeFrom(LocalDate.now());
		task.setEachItemAvailable(false);
		task.setCreatedBy(testUser);
		task.setItems(Arrays.asList(testItem, testItem2, testItem3));
		TaskDAO taskDAO = DAOfactory.INSTANCE.getTaskDAO();
		taskDAO.addTask(task);
		
		Task vratenyTask = taskDAO.getByID(task.getTaskID());
		assertTrue(vratenyTask != null);
		assertTrue(vratenyTask.getItems().size() == 3);
		
		taskDAO.deleteTask(task);
		projectDAO.deleteProject(project);
		userDAO.deleteUser(testUser);
		itemDAO.deleteItem(testItem);
		itemDAO.deleteItem(testItem2);
		itemDAO.deleteItem(testItem3);
		laboratoryDAO.deleteLaboratory(testLaboratory);
	}
	
	@Test
	void testGetItems() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);
		
		Project project = new Project();
		project.setName("testovaci_projekt");
		project.setActive(true);
		project.setDateFrom(LocalDate.now());
		project.setEachItemAvailable(false);
		project.setCreatedBy(testUser);
		ProjectDAO projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(project);
		
		Laboratory testLaboratory = new Laboratory();
		testLaboratory.setName("tester");
		testLaboratory.setLocation("testovacia");
		LaboratoryDAO laboratoryDAO = DAOfactory.INSTANCE.getLaboratoryDAO();
		laboratoryDAO.addLaboratory(testLaboratory);
		
		Item testItem = new Item();
		testItem.setName("test_item1");
		testItem.setQuantity(10);
		testItem.setAvailable(true);
		testItem.setLaboratory(testLaboratory);

		Item testItem2 = new Item();
		testItem2.setName("test_item2");
		testItem2.setQuantity(10);
		testItem2.setAvailable(true);
		
		Item testItem3 = new Item();
		testItem3.setName("test_item3");
		testItem3.setQuantity(10);
		testItem3.setAvailable(true);
		
		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();
		itemDAO.addItem(testItem);
		itemDAO.addItem(testItem2);
		itemDAO.addItem(testItem3);

		Task task = new Task();
		task.setProject(project);
		task.setName("testTask");
		task.setActive(true);
		task.setDateTimeFrom(LocalDate.now());
		task.setEachItemAvailable(false);
		task.setCreatedBy(testUser);
		task.setItems(Arrays.asList(testItem, testItem2, testItem3));
		TaskDAO taskDAO = DAOfactory.INSTANCE.getTaskDAO();
		taskDAO.addTask(task);
		assertTrue(taskDAO.getItems(task).size() == 3);

		taskDAO.deleteTask(task);
		projectDAO.deleteProject(project);
		userDAO.deleteUser(testUser);
		itemDAO.deleteItem(testItem);
		itemDAO.deleteItem(testItem2);
		itemDAO.deleteItem(testItem3);
		laboratoryDAO.deleteLaboratory(testLaboratory);
	}

}
