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

	/**
	 * Method that tests method <code>getAll()</code> in class
	 * <code>MysqlNoteDAO</code>. Tests if the list of notes returned from the
	 * database has size > 0.
	 */
	@Test
	void testGetAll() {
		List<Note> notes = DAOfactory.INSTANCE.getNoteDAO().getAll();
		assertNotNull(notes);
		assertTrue(notes.size() > 0);
	}

	/**
	 * Method that tests method <code>addNote(Note note)</code> and
	 * <code>deleteEntity(Entity note)</code> in a class <code>MysqlNoteDAO</code>.
	 * First test user and his test project are created, so the test note have an
	 * author (test user) and the entity (test project) to which it corresponds.
	 * Than the test note itself is created and it is checked if such a note is not
	 * already in the database. It is then added into the database and it is tested
	 * if the addition was successful. In the end the note is removed from the
	 * database and it is tested if the removal was successful. Test user and
	 * project are removed as well.
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

		Note testNote = new Note();
		testNote.setText("testovaci text");
		testNote.setTimestamp(LocalDateTime.now());
		testNote.setAuthor(testUser);
		testNote.setProject(testProject);
		NoteDAO noteDAO = DAOfactory.INSTANCE.getNoteDAO();
		noteDAO.addNote(testNote);
		boolean succesfullyAdded = false;
		List<Note> all = noteDAO.getAll();
		for (Note n : all) {
			if (n.getEntityID().equals(testNote.getEntityID())) {
				succesfullyAdded = true;
			}
		}
		assertTrue(succesfullyAdded);

		noteDAO.deleteEntity(testNote);
		all = noteDAO.getAll();
		boolean successfullyDeleted = true;
		for (Note note : all) {
			if (note.getEntityID().equals(testNote.getEntityID())) {
				successfullyDeleted = false;
			}
		}
		projectDAO.deleteEntity(testProject);
		userDAO.deleteEntity(testUser);
		assertTrue(successfullyDeleted);
	}

	/**
	 * Method that tests method <code>saveNote(Note note)</code> in class
	 * <code>MysqlNoteDAO</code>. First test user and his test project are created,
	 * so the test note have an author (test user) and the entity (test project) to
	 * which it corresponds. Then test note is made and saved into the database.
	 * First test runs to assert that it was correctly added into the database. Next
	 * some of its parameters are changed and saved. Then we look for the test note
	 * in the database (through its ID) and check, if its parameters really changed.
	 * In the end the test note, test project and test user are removed from the
	 * database.
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

		Note testNote = new Note();
		testNote.setText("testovaci text");
		testNote.setTimestamp(LocalDateTime.now());
		testNote.setAuthor(testUser);
		testNote.setProject(testProject);
		NoteDAO noteDAO = DAOfactory.INSTANCE.getNoteDAO();
		// create
		noteDAO.saveNote(testNote);
		assertNotNull(testNote.getEntityID());
		testNote.setText("testovaci_text_new");
		// update
		noteDAO.saveNote(testNote);
		List<Note> all = noteDAO.getAll();
		for (Note note : all) {
			if (note.getEntityID().equals(testNote.getEntityID())) {
				assertEquals("testovaci_text_new", note.getText());
				noteDAO.deleteEntity(note);
				projectDAO.deleteEntity(testProject);
				userDAO.deleteEntity(testUser);
				return;
			}
		}
		assertTrue(false, "update sa nepodaril");
	}

}
