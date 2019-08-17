package vahovsky.LabBook.persistent;

import java.util.List;

import vahovsky.LabBook.entities.Task;

public interface TaskDAO {

	// pridanie tasku do databazy
	void addTask(Task task);

	// zmena tasku v databaze
	void saveTask(Task task);

	// vrati zoznam taskov v databaze
	List<Task> getAll();

	// zmaze task
	void deleteTask(Task task);
	
	// vrati Task podla id
	Task getByID(Long id);

}
