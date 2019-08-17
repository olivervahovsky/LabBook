package vahovsky.LabBook.persistent;

import java.util.List;

import vahovsky.LabBook.entities.Project;

public interface ProjectDAO {

	// pridanie projektu do databazy
	void addProject(Project project);

	// zmena projektu v databaze
	void saveProject(Project project);

	// vrati zoznam projektov v databaze
	List<Project> getAll();

	// zmaze projekt
	void deleteProject(Project project);

	// vrati projekt podla id
	Project getByID(Long id);

}
