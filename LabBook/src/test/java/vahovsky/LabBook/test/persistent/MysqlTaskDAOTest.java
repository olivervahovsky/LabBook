package vahovsky.LabBook.test.persistent;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ItemDAO;
import vahovsky.LabBook.persistent.LaboratoryDAO;
import vahovsky.LabBook.persistent.NoteDAO;
import vahovsky.LabBook.persistent.ProjectDAO;
import vahovsky.LabBook.persistent.TaskDAO;
import vahovsky.LabBook.persistent.UserDAO;

public class MysqlTaskDAOTest {

	/**
	 * Method that tests method <code>getAll()</code> in class
	 * <code>MysqlTaskDAO</code>. Tests if the list of tasks returned from the
	 * database has size > 0.
	 */
	@Test
	void testGetAll() {
		List<Task> tasks = DAOfactory.INSTANCE.getTaskDAO().getAll();
		assertNotNull(tasks);
		assertTrue(tasks.size() > 0);
	}

	/**
	 * Method that tests method <code>insertItems(Task task)</code> in class
	 * <code>MysqlTaskDAO</code>. Test user and test project are created so that
	 * test task has an author and a project it belongs to. Next test task with no
	 * assigned items is created and added into the database. It is tested if method
	 * <code>getItems(Task task)</code> in a class <code>MysqlTaskDAO</code> returns
	 * null. Two test items are created and added into the database as well as
	 * inserted into the test task. Test task is saved and it is tested if the
	 * number of items assigned to it is now 2. Finally, all test objects are
	 * deleted.
	 */
	@Test
	void testInsertItems() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		Project testProject = new Project();
		testProject.setName("testovaci_projekt");
		testProject.setActive(true);
		testProject.setDateFrom(LocalDate.now());
		testProject.setEachItemAvailable(false);
		testProject.setCreatedBy(testUser);
		ProjectDAO projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(testProject);

		Task testTask = new Task();
		testTask.setProject(testProject);
		testTask.setName("testTask");
		testTask.setActive(true);
		testTask.setDateTimeFrom(LocalDate.now());
		testTask.setEachItemAvailable(false);
		testTask.setCreatedBy(testUser);
		TaskDAO taskDAO = DAOfactory.INSTANCE.getTaskDAO();
		taskDAO.addTask(testTask);

		assertTrue(testTask.getItems() == null);

		Item testItem = new Item();
		testItem.setName("test_item1");
		testItem.setQuantity(10);
		testItem.setAvailable(true);

		Item testItem2 = new Item();
		testItem2.setName("test_item2");
		testItem2.setQuantity(10);
		testItem2.setAvailable(true);

		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();
		itemDAO.addItem(testItem);
		itemDAO.addItem(testItem2);

		testTask.setItems(Arrays.asList(testItem, testItem2));
		taskDAO.saveTask(testTask);

		assertTrue(testTask.getItems().size() == 2);

		userDAO.deleteEntity(testUser);
		itemDAO.deleteEntity(testItem);
		itemDAO.deleteEntity(testItem2);

	}

	/**
	 * Method that tests method <code>addTask(Task task)</code> and
	 * <code>deleteEntity(Entity task)</code> in a class <code>MysqlTaskDAO</code>.
	 * First, test user is created, so the test project has an author. Then test
	 * project and test items that are going to be associated with the test task are
	 * created. Finally, test task is created and added into the database. Next, it
	 * is tested if the addition of the task into the database was successful. In
	 * the end the task is deleted from the database and it is tested if this
	 * removal was successful. Test user, test project and test items are removed as
	 * well.
	 */
	@Test
	void addDeleteTest() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		Project testProject = new Project();
		testProject.setName("testovaci_projekt");
		testProject.setActive(true);
		testProject.setDateFrom(LocalDate.now());
		testProject.setEachItemAvailable(false);
		testProject.setCreatedBy(testUser);
		ProjectDAO projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(testProject);

		Item testItem = new Item();
		testItem.setName("test_item1");
		testItem.setQuantity(10);
		testItem.setAvailable(true);

		Item testItem2 = new Item();
		testItem2.setName("test_item2");
		testItem2.setQuantity(10);
		testItem2.setAvailable(true);

		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();
		itemDAO.addItem(testItem);
		itemDAO.addItem(testItem2);

		Task testTask = new Task();
		testTask.setProject(testProject);
		testTask.setName("testTask");
		testTask.setActive(true);
		testTask.setDateTimeFrom(LocalDate.now());
		testTask.setEachItemAvailable(false);
		testTask.setCreatedBy(testUser);
		testTask.setItems(Arrays.asList(testItem, testItem2));
		TaskDAO taskDAO = DAOfactory.INSTANCE.getTaskDAO();
		taskDAO.addTask(testTask);
		boolean succesfullyAdded = false;
		List<Task> all = taskDAO.getAll();
		for (Task task : all) {
			if (task.getEntityID().equals(testTask.getEntityID())) {
				succesfullyAdded = true;
			}
		}
		assertTrue(succesfullyAdded);
		assertTrue(testTask.getItems().size() == 2);

		taskDAO.deleteEntity(testTask);
		all = taskDAO.getAll();
		boolean successfullyDeleted = true;
		for (Task task : all) {
			if (task.getEntityID().equals(testTask.getEntityID())) {
				successfullyDeleted = false;
			}
		}
		userDAO.deleteEntity(testUser);
		itemDAO.deleteEntity(testItem);
		itemDAO.deleteEntity(testItem2);
		assertTrue(successfullyDeleted);
	}

	/**
	 * Method that tests method <code>saveTask(Task task)</code> in class
	 * <code>MysqlTaskDAO</code>. First, test user is created, so the test project
	 * has an author. Then test project that is going to be associated with the test
	 * task is created. Finally, test task is created and added into the database.
	 * First test runs to assert that the task was correctly added into the
	 * database. Next, some of its parameters are changed and saved. Then we look
	 * for the test task in the database (through its ID) and check, if its
	 * parameters really changed. In the end the test task and test user are removed
	 * from the database.
	 */
	@Test
	void testSave() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		Project testProject = new Project();
		testProject.setName("testovaci_projekt");
		testProject.setActive(true);
		testProject.setDateFrom(LocalDate.now());
		testProject.setEachItemAvailable(false);
		testProject.setCreatedBy(testUser);
		ProjectDAO projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(testProject);

		Task testTask = new Task();
		testTask.setProject(testProject);
		testTask.setName("testTask");
		testTask.setActive(true);
		testTask.setDateTimeFrom(LocalDate.now());
		testTask.setEachItemAvailable(false);
		testTask.setCreatedBy(testUser);
		TaskDAO taskDAO = DAOfactory.INSTANCE.getTaskDAO();
		// create
		taskDAO.saveTask(testTask);
		assertNotNull(testTask.getEntityID());
		testTask.setName("testovaci_task_new");
		// update
		taskDAO.saveTask(testTask);
		List<Task> all = taskDAO.getAll();
		for (Task task : all) {
			if (task.getEntityID().equals(testTask.getEntityID())) {
				assertEquals("testovaci_task_new", task.getName());
				userDAO.deleteEntity(testUser);
				return;
			}
		}
		assertTrue(false, "update sa nepodaril");
	}

	/**
	 * Method that tests method <code>getByID(Long id)</code> in class
	 * <code>MysqlTaskDAO</code>. First test user is created, so the test project
	 * has an author. Then test project that is going to be associated with the test
	 * task is created. Finally, test task is created and added into the database.
	 * ID of the test task is saved and compared to the ID of the task that is
	 * returned by the method <code>getByID(Long id)</code>. In the end the test
	 * user along with the test project and test task are removed from the database.
	 */
	@Test
	void testGetByID() {
		User testUser = new User();
		testUser.setName("testerGetByID");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		Project testProject = new Project();
		testProject.setName("testovaci_projekt");
		testProject.setActive(true);
		testProject.setDateFrom(LocalDate.now());
		testProject.setEachItemAvailable(false);
		testProject.setCreatedBy(testUser);
		ProjectDAO projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(testProject);

		Task testTask = new Task();
		testTask.setProject(testProject);
		testTask.setName("testTask");
		testTask.setActive(true);
		testTask.setDateTimeFrom(LocalDate.now());
		testTask.setEachItemAvailable(false);
		testTask.setCreatedBy(testUser);
		TaskDAO taskDAO = DAOfactory.INSTANCE.getTaskDAO();
		taskDAO.addTask(testTask);
		long id = testTask.getEntityID();

		Task returnedTask = taskDAO.getByID(testTask.getEntityID());
		assertTrue(returnedTask != null);
		assertTrue(id == returnedTask.getEntityID());

		userDAO.deleteEntity(testUser);
	}

	/**
	 * Method that tests method <code>getItems(Task task)</code> in a class
	 * <code>MysqlTaskDAO<code>. Test user is created and added into the database,
	 * so the test project has an author. Next, test project is created and added
	 * into the database, so the task has a project to belong to. Then few items are
	 * created and added into the database. Finally, the test task is created and
	 * added in to the database. It is tested, if number of its items is zero. Next,
	 * test items are added to the task and task is saved so the changes are
	 * reflected in the database. Now it is tested, whether the number of test
	 * task's items is equal to the number of added items. Eventually, test user is
	 * deleted along with his test project and test task and test items are deleted
	 * as well.
	 */
	@Test
	void testGetItems() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		Project testProject = new Project();
		testProject.setName("testovaci_projekt");
		testProject.setActive(true);
		testProject.setDateFrom(LocalDate.now());
		testProject.setEachItemAvailable(false);
		testProject.setCreatedBy(testUser);
		ProjectDAO projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(testProject);

		Item testItem = new Item();
		testItem.setName("test_item1");
		testItem.setQuantity(10);
		testItem.setAvailable(true);

		Item testItem2 = new Item();
		testItem2.setName("test_item2");
		testItem2.setQuantity(10);
		testItem2.setAvailable(true);

		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();
		itemDAO.addItem(testItem);
		itemDAO.addItem(testItem2);

		Task testTask = new Task();
		testTask.setProject(testProject);
		testTask.setName("testTask");
		testTask.setActive(true);
		testTask.setDateTimeFrom(LocalDate.now());
		testTask.setEachItemAvailable(false);
		testTask.setCreatedBy(testUser);

		TaskDAO taskDAO = DAOfactory.INSTANCE.getTaskDAO();
		taskDAO.addTask(testTask);
		assertTrue(taskDAO.getItems(testTask).size() == 0);
		testTask.setItems(Arrays.asList(testItem, testItem2));
		taskDAO.saveTask(testTask);
		assertTrue(taskDAO.getItems(testTask).size() == 2);

		userDAO.deleteEntity(testUser);
		itemDAO.deleteEntity(testItem);
		itemDAO.deleteEntity(testItem2);
	}

	/**
	 * Method that tests method <code>getNotes(Task task)</code> in a class
	 * <code>MysqlTaskDAO<code>. Test user is created and added into the database,
	 * so the test project has an author. Next, test project is created and added
	 * into the database, so the task has a project to belong to. Finally, the test
	 * task is created and added into the database. It is tested, if number of its
	 * notes is zero. Next, some test notes referencing the test task are added into
	 * the database. Now it is tested, whether the number of test task's notes is
	 * equal to the number of added notes. Eventually, test user is deleted along
	 * with his test project, test task and task's test notes.
	 */
	@Test
	void testGetNotes() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		Project testProject = new Project();
		testProject.setName("testovaci_projekt");
		testProject.setActive(true);
		testProject.setEachItemAvailable(true);
		testProject.setCreatedBy(testUser);
		ProjectDAO projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(testProject);

		Task testTask = new Task();
		testTask.setProject(testProject);
		testTask.setName("testTask");
		testTask.setActive(false);
		testTask.setEachItemAvailable(false);
		testTask.setCreatedBy(testUser);
		TaskDAO taskDAO = DAOfactory.INSTANCE.getTaskDAO();
		taskDAO.addTask(testTask);

		assertEquals(0, taskDAO.getNotes(testTask).size());

		NoteDAO noteDAO = DAOfactory.INSTANCE.getNoteDAO();
		Note testNote1 = new Note();
		testNote1.setText("testovaci text 1");
		testNote1.setTimestamp(LocalDateTime.now());
		testNote1.setTask(testTask);
		testNote1.setAuthor(testUser);
		noteDAO.addNote(testNote1);

		Note testNote2 = new Note();
		testNote2.setText("testovaci text 2");
		testNote2.setTimestamp(LocalDateTime.now());
		testNote2.setTask(testTask);
		testNote2.setAuthor(testUser);
		noteDAO.addNote(testNote2);

		assertEquals(2, taskDAO.getNotes(testTask).size());

		userDAO.deleteEntity(testUser);
		assertEquals(0, taskDAO.getNotes(testTask).size());
	}

	/**
	 * Method that tests method <code>isNameAvailable(String name)</code> in class
	 * <code>MysqlTaskDAO</code>. Test user is created and added into the database,
	 * so the test project has an author. Next, test project is created and added
	 * into the database, so the task has a project to belong to. Availability of
	 * the future test task's name is tested. Then test task with that name is added
	 * into the database. Next, availability of that name is tested again, this time
	 * negative result is expected. Finally, test user along with his project and
	 * its task is removed from the database.
	 */
	@Test
	void testIsNameAvailable() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		Project testProject = new Project();
		testProject.setName("testovaci_projekt");
		testProject.setActive(true);
		testProject.setEachItemAvailable(true);
		testProject.setCreatedBy(testUser);
		ProjectDAO projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(testProject);

		TaskDAO taskDAO = DAOfactory.INSTANCE.getTaskDAO();
		assertTrue(taskDAO.isNameAvailable("test_task"));

		Task testTask = new Task();
		testTask.setProject(testProject);
		testTask.setName("test_task");
		testTask.setActive(false);
		testTask.setEachItemAvailable(false);
		testTask.setCreatedBy(testUser);

		taskDAO.addTask(testTask);
		assertFalse(taskDAO.isNameAvailable("test_task"));

		userDAO.deleteEntity(testUser);
	}

}
