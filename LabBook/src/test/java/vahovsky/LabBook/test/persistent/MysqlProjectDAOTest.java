package vahovsky.LabBook.test.persistent;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import vahovsky.LabBook.entities.Entity;
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
		for (Project p : all) {
			if (p.getEntityID().equals(testProject.getEntityID())) {
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

	@Test
	void testSave() {
		User testUser = new User();
		testUser.setName("testerSave");
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
		// create
		projectDAO.saveProject(project);
		assertNotNull(project.getEntityID());
		project.setName("testovaci_projekt_new");
		// update
		projectDAO.saveProject(project);
		List<Project> all = projectDAO.getAll();
		for (Project p : all) {
			if (p.getEntityID().equals(project.getEntityID())) {
				assertEquals("testovaci_projekt_new", p.getName());
				projectDAO.deleteEntity(p);
				userDAO.deleteEntity(testUser);
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
		long id = project.getEntityID();
		assertTrue(id == projectDAO.getByID(id).getEntityID());
		projectDAO.deleteEntity(project);
		userDAO.deleteEntity(testUser);
	}

}
