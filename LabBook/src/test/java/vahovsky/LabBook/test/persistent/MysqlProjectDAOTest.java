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

public class MysqlProjectDAOTest {

	@Test
	void testGetAll() {
		List<Project> projects = DAOfactory.INSTANCE.getProjectDAO().getAll();
		assertNotNull(projects);
		assertTrue(projects.size() > 0);
	}

	@Test
	void addDeleteTest() {
		User testUser = new User();
		testUser.setName("testerAddDelete");
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
		boolean notThere = true;
		List<Project> all = projectDAO.getAll();
		for (Project p : all) {
			if (p.getEntityID().equals(project.getEntityID())) {
				notThere = false;
			}
		}
		assertTrue(notThere);
		projectDAO.addProject(project);
		
		Note note = new Note();
		note.setText("testovaci text");
		note.setTimestamp(LocalDateTime.now());
		note.setAuthor(testUser);
		note.setProject(project);
		NoteDAO noteDAO = DAOfactory.INSTANCE.getNoteDAO();
		noteDAO.addNote(note);
		
		Note note2 = new Note();
		note2.setText("testovaci text 2");
		note2.setTimestamp(LocalDateTime.now());
		note2.setAuthor(testUser);
		note2.setProject(project);
		noteDAO.addNote(note2);
		
		Task task = new Task();
		task.setProject(project);
		task.setName("task taskovity");
		task.setActive(true);
		task.setEachItemAvailable(true);
		task.setCreatedBy(testUser);
		TaskDAO taskDao = DAOfactory.INSTANCE.getTaskDAO();
		taskDao.addTask(task);
		
		all = projectDAO.getAll();
		boolean succesfullyAdded = false;
		for (Project p : all) {
			if (p.getEntityID().equals(project.getEntityID())) {
				succesfullyAdded = true;
			}
		}
		assertTrue(succesfullyAdded);
		projectDAO.deleteEntity(project);
		userDAO.deleteEntity(testUser);
		all = projectDAO.getAll();
		boolean successfullyDeleted = true;
		for (Project p : all) {
			if (p.getEntityID().equals(project.getEntityID())) {
				successfullyDeleted = false;
			}
		}
		assertTrue(successfullyDeleted);
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
