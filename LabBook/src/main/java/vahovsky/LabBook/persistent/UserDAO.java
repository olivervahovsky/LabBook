package vahovsky.LabBook.persistent;

import java.util.List;

import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.entities.User;

public interface UserDAO {

	// pridanie usera do databazy
	void addUser(User user);

	// vrati vsetkych userov z databazy
	List<User> getAll();

	// zmena usera v databaze
	void saveUser(User user);

	// vymazanie usera z databazy
	void deleteUser(User user);

	// vrati usera podla id
	User getByID(Long id);

	// vrati pouzivatela podla emailu
	User getByEmail(String email);
	
	// vrati projekty daneho usera
	List<Project> getProjects(User user);

	// vrati tasky daneho usera
	List<Task> getTasks(User user);

	// vrati notes daneho usera
	List<Note> getNotes(User user);

	// vrati zoznam emailov vsetkych userov
	List<String> getAllEmails();

	// vrati zoznam prihlasovacich mien vsetkych userov
	List<String> getAllNames();

}
