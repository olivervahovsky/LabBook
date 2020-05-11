package vahovsky.LabBook.test.persistent;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import vahovsky.LabBook.business.UserIdentificationManager;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.NoteDAO;
import vahovsky.LabBook.persistent.ProjectDAO;
import vahovsky.LabBook.persistent.TaskDAO;
import vahovsky.LabBook.persistent.UserDAO;

public class MysqlProjectDAOTest {

	/**
	 * Method that tests method <code>getAll()</code> in class
	 * <code>MysqlProjectDAO</code>. Tests if the list of projects returned from the
	 * database has size > 0.
	 */
	@Test
	void testGetAll() {
		List<Project> projects = DAOfactory.INSTANCE.getProjectDAO().getAll();
		assertNotNull(projects);
		assertTrue(projects.size() > 0);
	}

	/**
	 * Method that tests method <code>addProject(Project project)</code> and
	 * <code>deleteEntity(Entity project)</code> in a class
	 * <code>MysqlProjectDAO</code>. First test user is created, so the test project
	 * has an author. Then test project is created, it is tested if such a project
	 * is not already in the database, then it is added into it. Some test notes and
	 * test tasks are created and connected to a test project. Next it is tested if
	 * the addition of the project into the database was successful. In the end the
	 * project is deleted from the database and it is tested if this removal was
	 * successful. Test user and project are removed as well.
	 */
	@Test
	void addDeleteTest() {
		User testUser = new User();
		testUser.setName("testerAddDelete");
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
		boolean notThere = true;
		List<Project> all = projectDAO.getAll();
		for (Project project : all) {
			if (project.getEntityID().equals(testProject.getEntityID())) {
				notThere = false;
			}
		}
		assertTrue(notThere);
		projectDAO.addProject(testProject);

		Note testNote = new Note();
		testNote.setText("testovaci text");
		testNote.setTimestamp(LocalDateTime.now());
		testNote.setAuthor(testUser);
		testNote.setProject(testProject);
		NoteDAO noteDAO = DAOfactory.INSTANCE.getNoteDAO();
		noteDAO.addNote(testNote);

		Note testNote2 = new Note();
		testNote2.setText("testovaci text 2");
		testNote2.setTimestamp(LocalDateTime.now());
		testNote2.setAuthor(testUser);
		testNote2.setProject(testProject);
		noteDAO.addNote(testNote2);

		Task testTask = new Task();
		testTask.setProject(testProject);
		testTask.setName("task taskovity");
		testTask.setActive(true);
		testTask.setEachItemAvailable(true);
		testTask.setCreatedBy(testUser);
		TaskDAO taskDao = DAOfactory.INSTANCE.getTaskDAO();
		taskDao.addTask(testTask);

		all = projectDAO.getAll();
		boolean succesfullyAdded = false;
		for (Project project : all) {
			if (project.getEntityID().equals(testProject.getEntityID())) {
				succesfullyAdded = true;
			}
		}
		assertTrue(succesfullyAdded);
		projectDAO.deleteEntity(testProject);
		all = projectDAO.getAll();
		boolean successfullyDeleted = true;
		for (Project project : all) {
			if (project.getEntityID().equals(testProject.getEntityID())) {
				successfullyDeleted = false;
			}
		}
		assertTrue(successfullyDeleted);
		userDAO.deleteEntity(testUser);
	}

	/**
	 * Method that tests method <code>saveProject(Project project)</code> in class
	 * <code>MysqlProjectDAO</code>. First test user is created, so the test project
	 * has an author. Then test project is created and saved into the database.
	 * First test runs to assert that it was correctly added into the database. Next
	 * some of its parameters are changed and saved. Then we look for the test
	 * project in the database (through its ID) and check, if its parameters really
	 * changed. In the end the test project and test user are removed from the
	 * database.
	 */
	@Test
	void testSave() {
		User testUser = new User();
		testUser.setName("testerSave");
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
		// create
		projectDAO.saveProject(testProject);
		assertNotNull(testProject.getEntityID());
		testProject.setName("testovaci_projekt_new");
		// update
		projectDAO.saveProject(testProject);
		List<Project> all = projectDAO.getAll();
		for (Project project : all) {
			if (project.getEntityID().equals(testProject.getEntityID())) {
				assertEquals("testovaci_projekt_new", project.getName());
				projectDAO.deleteEntity(project);
				userDAO.deleteEntity(testUser);
				return;
			}
		}
		assertTrue(false, "update sa nepodaril");
	}

	/**
	 * Method that tests method <code>getByID(Long id)</code> in class
	 * <code>MysqlProjectDAO</code>. First test user is created, so the test project
	 * has an author. Then test project is created and saved into the database. ID
	 * of the test project is saved and compared to the ID of the project that is
	 * returned by the method <code>getByID(Long id)</code>. In the end the test
	 * project and test user are removed from the database.
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
		long id = testProject.getEntityID();
		assertTrue(id == projectDAO.getByID(id).getEntityID());
		projectDAO.deleteEntity(testProject);
		userDAO.deleteEntity(testUser);
	}

	/**
	 * Method that tests method <code>getProjectsOfLoggedUser()</code> in a class
	 * <code>MysqlProjectDAO<code>. Test user is created and logged in. It is
	 * tested, if number of his projects is zero. Next, test project is created and
	 * added to a user. Now it is tested, whether the number of test user's projects
	 * is 1. Eventually, test user is deleted along with his test project.
	 */
	@Test
	void testGetProjectsOfLoggedUser() {
		User testUser = new User();
		String userName = "testerGetByID";
		testUser.setName(userName);
		String password = "1234";
		testUser.setPassword(password);
		testUser.setEmail("tester.testovaci@test.com");
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();
		userDAO.addUser(testUser);

		UserIdentificationManager.setUserOrAdmin(userName, password);
		assertTrue(DAOfactory.INSTANCE.getProjectDAO().getProjectsOfLoggedUser().size() == 0);

		Project project = new Project();
		project.setName("testovaci_projekt");
		project.setActive(true);
		project.setDateFrom(LocalDate.now());
		project.setEachItemAvailable(false);
		project.setCreatedBy(testUser);
		ProjectDAO projectDAO = DAOfactory.INSTANCE.getProjectDAO();
		projectDAO.addProject(project);

		assertTrue(DAOfactory.INSTANCE.getProjectDAO().getProjectsOfLoggedUser().size() == 1);

		userDAO.deleteEntity(testUser);
	}

	/**
	 * Method that tests method <code>getTasksOfProject(Project project)</code> in a
	 * class <code>MysqlProjectDAO<code>. Test user is created and added into the
	 * database, so the test project has an author. Next, test project is created
	 * and added into the database. It is tested, if number of its tasks is zero.
	 * Next, two test tasks are created and added to a project and into the
	 * database. Now it is tested, whether the number of test project's tasks is 2.
	 * Eventually, test user is deleted along with his test project.
	 */
	@Test
	void testgetTasksOfProject() {
		User testUser = new User();
		testUser.setName("testerAddDelete");
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

		assertTrue(projectDAO.getTasksOfProject(testProject).size() == 0);

		Task testTask = new Task();
		testTask.setProject(testProject);
		testTask.setName("task taskovity");
		testTask.setActive(true);
		testTask.setEachItemAvailable(true);
		testTask.setCreatedBy(testUser);
		TaskDAO taskDao = DAOfactory.INSTANCE.getTaskDAO();
		taskDao.addTask(testTask);

		Task testTask2 = new Task();
		testTask2.setProject(testProject);
		testTask2.setName("task taskovity2");
		testTask2.setActive(true);
		testTask2.setEachItemAvailable(true);
		testTask2.setCreatedBy(testUser);
		taskDao.addTask(testTask);

		assertTrue(projectDAO.getTasksOfProject(testProject).size() == 2);

		userDAO.deleteEntity(testUser);

	}

}