package vahovsky.LabBook.test.persistent;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.NoteDAO;
import vahovsky.LabBook.persistent.ProjectDAO;
import vahovsky.LabBook.persistent.TaskDAO;
import vahovsky.LabBook.persistent.UserDAO;

class MysqlUserDAOTest {

	/**
	 * Method that tests method <code>getAll()</code> in class
	 * <code>MysqlUserDAO</code>. Tests if the list of users returned from the
	 * database has size > 0.
	 */
	@Test
	void testGetAll() {
		List<User> users = DAOfactory.INSTANCE.getUserDAO().getAll();
		assertNotNull(users);
		assertTrue(users.size() > 0);
	}

	/**
	 * Method that tests method <code>addUser(User user)</code> and
	 * <code>deleteEntity(Entity user)</code> in a class <code>MysqlUserDAO</code>.
	 * Test user is created and it is checked if such a user is not already in the
	 * database. It is then added into the database and it is tested if the addition
	 * was successful. Test project, test task and test note is created, all
	 * belonging to a test user. In the end test user is removed from the database
	 * and it is tested whether the removal was successful as well as if his
	 * project, task and note was removed.
	 */
	@Test
	void addDeleteTest() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();

		boolean notThere = true;
		List<User> all = userDAO.getAll();
		for (User user : all) {
			if (user.getEntityID().equals(testUser.getEntityID())) {
				notThere = false;
			}
		}
		assertTrue(notThere);

		userDAO.addUser(testUser);

		all = userDAO.getAll();
		boolean succesfullyAdded = false;
		for (User user : all) {
			if (user.getEntityID().equals(testUser.getEntityID())) {
				succesfullyAdded = true;
			}
		}
		assertTrue(succesfullyAdded);

		Project testProject = new Project();
		testProject.setName("testovaci_projekt");
		testProject.setActive(true);
		testProject.setDateFrom(LocalDate.now());
		testProject.setEachItemAvailable(false);
		testProject.setCreatedBy(testUser);
		ProjectDAO projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(testProject);

		Note testNote = new Note();
		testNote.setText("testovaci text");
		testNote.setTimestamp(LocalDateTime.now());
		testNote.setAuthor(testUser);
		testNote.setProject(testProject);
		NoteDAO noteDAO = DAOfactory.INSTANCE.getNoteDAO();
		noteDAO.addNote(testNote);

		Task testTask = new Task();
		testTask.setProject(testProject);
		testTask.setName("task taskovity");
		testTask.setActive(true);
		testTask.setEachItemAvailable(true);
		testTask.setCreatedBy(testUser);
		TaskDAO taskDAO = DAOfactory.INSTANCE.getTaskDAO();
		taskDAO.addTask(testTask);

		userDAO.deleteEntity(testUser);

		all = userDAO.getAll();
		boolean successfullyDeleted = true;
		for (User user : all) {
			if (user.getEntityID().equals(testUser.getEntityID())) {
				successfullyDeleted = false;
			}
		}
		assertTrue(successfullyDeleted);

		List<Project> allProjects = projectDAO.getAll();
		successfullyDeleted = true;
		for (Project project : allProjects) {
			if (project.getEntityID().equals(testProject.getEntityID())) {
				successfullyDeleted = false;
			}
		}
		assertTrue(successfullyDeleted);

		List<Task> allTasks = taskDAO.getAll();
		successfullyDeleted = true;
		for (Task task : allTasks) {
			if (task.getEntityID().equals(testTask.getEntityID())) {
				successfullyDeleted = false;
			}
		}
		assertTrue(successfullyDeleted);

		List<Note> allNotes = noteDAO.getAll();
		successfullyDeleted = true;
		for (Note note : allNotes) {
			if (note.getEntityID().equals(testNote.getEntityID())) {
				successfullyDeleted = false;
			}
		}
		assertTrue(successfullyDeleted);
	}

	/**
	 * Method that tests method <code>saveUser(User user)</code> in class
	 * <code>MysqlUserDAO</code>. Test user is made and saved into the database.
	 * First test runs to assert that it was correctly added into the database. Next
	 * some of its parameters are changed and saved. Then we look for the test user
	 * in the database (through its ID) and check, if its parameters really changed.
	 * In the end it is removed from the database.
	 */
	@Test
	void testSave() {
		User testUser = new User();
		testUser.setName("tester");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		// create
		userDAO.saveUser(testUser);
		assertNotNull(testUser.getEntityID());
		// update
		testUser.setName("tester_new");
		Project testProject = new Project();
		testProject.setName("testovaci_projekt");
		testProject.setActive(true);
		testProject.setDateFrom(LocalDate.now());
		testProject.setEachItemAvailable(false);
		testProject.setCreatedBy(testUser);
		ProjectDAO projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(testProject);

		userDAO.saveUser(testUser);
		List<User> all = userDAO.getAll();
		for (User user : all) {
			if (user.getEntityID().equals(testUser.getEntityID())) {
				assertEquals("tester_new", user.getName());
				assertTrue(testProject.getCreatedBy().getName().equals(user.getName()));
				userDAO.deleteEntity(user);
				return;
			}
		}
		assertTrue(false, "update sa nepodaril");
	}

	/**
	 * Method that tests method <code>getByID(Long id)</code> in class
	 * <code>MysqlUserDAO</code>. Test user is made and added into the database. ID
	 * of the test user is saved and compared to the ID of the user that is returned
	 * by the method <code>getByID(Long id)</code>. Finally, test user is removed
	 * from the database.
	 */
	@Test
	void testGetByID() {
		User testUser = new User();
		testUser.setName("testerGetByID");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		long id = testUser.getEntityID();
		assertTrue(id == userDAO.getByID(id).getEntityID());
		userDAO.deleteEntity(testUser);
	}

	/**
	 * Method that tests method <code>getTasksOfUser(Entity user)</code> in a class
	 * <code>MysqlUserDAO</code>. Test user is created and added into the database.
	 * Next, test projects are created and some tasks of these projects are created.
	 * Project and their respective tasks are added into the database. Finally, the
	 * size of the list of tasks which is a result of the method
	 * <code>getTasksOfUser(Entity user)</code> is compared to the number of test
	 * tasks. Test user is subsequently removed from the database along with all of
	 * his projects and tasks.
	 */
	@Test
	void testGetTasksOfUser() {
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
		testTask.setName("task taskovity");
		testTask.setActive(true);
		testTask.setEachItemAvailable(true);
		testTask.setCreatedBy(testUser);
		TaskDAO taskDao = DAOfactory.INSTANCE.getTaskDAO();
		taskDao.addTask(testTask);

		Project testProject2 = new Project();
		testProject2.setName("testovaci_projekt2");
		testProject2.setActive(true);
		testProject2.setDateFrom(LocalDate.now());
		testProject2.setEachItemAvailable(false);
		testProject2.setCreatedBy(testUser);
		projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(testProject2);

		Task testTask2 = new Task();
		testTask2.setProject(testProject2);
		testTask2.setName("task taskovity2");
		testTask2.setActive(true);
		testTask2.setEachItemAvailable(true);
		testTask2.setCreatedBy(testUser);
		taskDao = DAOfactory.INSTANCE.getTaskDAO();
		taskDao.addTask(testTask2);

		assertTrue(userDAO.getTasksOfUser(testUser).size() == 2);

		userDAO.deleteEntity(testUser);
	}

	/**
	 * Method that tests method <code>getNotes(User user)</code> in a class
	 * <code>MysqlUserDAO</code>. Test user is created and added into the database.
	 * Next, test project, test task and some notes to these tasks are created. All
	 * of it is added into the database. Finally, the size of the list of notes
	 * which is a result of the method <code>getNotes(User user)</code> is compared
	 * to the number of test tasks. Test user is subsequently removed from the
	 * database along with all of his projects and tasks.
	 */
	@Test
	void testGetNotes() {
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

		Note testNote = new Note();
		testNote.setText("testovaci text");
		testNote.setTimestamp(LocalDateTime.now());
		testNote.setAuthor(testUser);
		testNote.setProject(testProject);
		NoteDAO noteDAO = DAOfactory.INSTANCE.getNoteDAO();
		noteDAO.addNote(testNote);

		Note testNote2 = new Note();
		testNote2.setText("testovaci text");
		testNote2.setTimestamp(LocalDateTime.now());
		testNote2.setAuthor(testUser);
		testNote2.setTask(testTask);
		noteDAO.addNote(testNote2);

		assertTrue(userDAO.getNotes(testUser) != null);
		assertTrue(userDAO.getNotes(testUser).size() == 2);

		userDAO.deleteEntity(testUser);

	}

	/**
	 * Method that tests method <code>getProjects(User user)</code> in a class
	 * <code>MysqlUserDAO</code>. Test user is created and added into the database.
	 * Next, test projects are created and added into the database. Finally, the
	 * size of the list of projects which is a result of the method
	 * <code>getProjects(User user)</code> is compared to the number of test
	 * projects. Test user is subsequently removed from the database along with all
	 * of his projects.
	 */
	@Test
	void testGetProjects() {
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

		Project testProject2 = new Project();
		testProject2.setName("testovaci_projekt2");
		testProject2.setActive(true);
		testProject2.setDateFrom(LocalDate.now());
		testProject2.setEachItemAvailable(false);
		testProject2.setCreatedBy(testUser);
		projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(testProject2);

		assertTrue(userDAO.getProjects(testUser) != null);
		assertTrue(userDAO.getProjects(testUser).size() == 2);

		userDAO.deleteEntity(testUser);

	}

	/**
	 * Method that tests method <code>getByEmail(String email)</code> in class
	 * <code>MysqlUserDAO</code>. Test user is made and added into the database. ID
	 * of the test user is saved and compared to the ID of the user that is returned
	 * by the method <code>getByEmail(String email)</code>. Finally, test user is
	 * removed from the database.
	 */
	@Test
	void testGetByEmail() {
		User testUser = new User();
		testUser.setName("testGetByEmail");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		Long id = testUser.getEntityID();
		assertTrue(id.equals(userDAO.getByEmail("tester.testovaci@test.com").getEntityID()));
		userDAO.deleteEntity(testUser);
	}

	/**
	 * Method that tests method <code>getAllEmails()</code> in class
	 * <code>MysqlUserDAO</code>. Size of the list that method
	 * <code>getAllEmails()</code> returns is compared to the size of the list
	 * returned after one more user is added into the database. If the new size
	 * equals the original size + 1, the method is assumed to work correctly.
	 */
	@Test
	void testGetAllEmails() {
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		int origNumber = userDAO.getAllEmails().size();
		User testUser = new User();
		testUser.setName("testGetAllEmails");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		userDAO.addUser(testUser);
		assertTrue(userDAO.getAllEmails().size() == origNumber + 1);
		userDAO.deleteEntity(testUser);
	}

	/**
	 * Method that tests method <code>getAllNames()</code> in class
	 * <code>MysqlUserDAO</code>. Size of the list that method
	 * <code>getAllNames()</code> returns is compared to the size of the list
	 * returned after one more user is added into the database. If the new size
	 * equals the original size + 1, the method is assumed to work correctly.
	 */
	@Test
	void testGetAllNames() {
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		int origNumber = userDAO.getAllNames().size();
		User testUser = new User();
		testUser.setName("testGetAllNames");
		testUser.setPassword("1234");
		testUser.setEmail("tester.testovaci@test.com");
		userDAO.addUser(testUser);
		assertTrue(userDAO.getAllNames().size() == origNumber + 1);
		userDAO.deleteEntity(testUser);
	}

}
