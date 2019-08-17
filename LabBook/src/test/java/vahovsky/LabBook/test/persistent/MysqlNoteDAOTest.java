package vahovsky.LabBook.test.persistent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.NoteDAO;
import vahovsky.LabBook.persistent.ProjectDAO;
import vahovsky.LabBook.persistent.UserDAO;

public class MysqlNoteDAOTest {
	
	@Test
	void testGetAll() {
		List<Note> notes = DAOfactory.INSTANCE.getNoteDAO().getAll();
		assertNotNull(notes);
		assertTrue(notes.size() > 0);
	}
	
	@Test
	void addDeleteTest() {
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
		
		Note note = new Note();
		note.setText("testovaci text");
		note.setTimestamp(LocalDateTime.now());
		note.setAuthor(testUser);
		note.setProject(project);
		NoteDAO noteDAO = DAOfactory.INSTANCE.getNoteDAO();
		noteDAO.addNote(note);
		boolean succesfullyAdded = false;
		List<Note> all = noteDAO.getAll();
		for (Note n : all) {
			if (n.getNoteID().equals(note.getNoteID())) {
				succesfullyAdded = true;
			}
		}
		assertTrue(succesfullyAdded);

		noteDAO.deleteNote(note);
		all = noteDAO.getAll();
		boolean successfullyDeleted = true;
		for (Note n : all) {
			if (n.getNoteID().equals(note.getNoteID())) {
				successfullyDeleted = false;
			}
		}
		projectDAO.deleteProject(project);
		userDAO.deleteUser(testUser);
		assertTrue(successfullyDeleted);
	}
	
	@Test
	void testSave() {
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
		
		Note note = new Note();
		note.setText("testovaci text");
		note.setTimestamp(LocalDateTime.now());
		note.setAuthor(testUser);
		note.setProject(project);
		NoteDAO noteDAO = DAOfactory.INSTANCE.getNoteDAO();
		// create
		noteDAO.saveNote(note);
		assertNotNull(note.getNoteID());
		note.setText("testovaci_task_new");
		// update
		noteDAO.saveNote(note);
		List<Note> all = noteDAO.getAll();
		for (Note n : all) {
			if (n.getNoteID().equals(note.getNoteID())) {
				assertEquals("testovaci_task_new", n.getText());
				noteDAO.deleteNote(n);
				projectDAO.deleteProject(project);
				userDAO.deleteUser(testUser);
				return;
			}
		}
		assertTrue(false, "update sa nepodaril");
	}

}
