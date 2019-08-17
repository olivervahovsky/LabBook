package vahovsky.LabBook.persistent;

import java.util.List;

import vahovsky.LabBook.entities.Note;

public interface NoteDAO {

	// pridanie note do databazy
	void addNote(Note note);

	// vrati vsetky note z databazy
	List<Note> getAll();

	// zmena note v databaze
	void saveNote(Note note);

	// vymazanie note z databazy
	void deleteNote(Note note);

}
