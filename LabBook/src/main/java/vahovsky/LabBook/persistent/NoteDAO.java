package vahovsky.LabBook.persistent;

import java.util.List;

import vahovsky.LabBook.entities.Note;

public interface NoteDAO {

	/**
	 * Method adding representation of entity object note into database
	 * 
	 * @param note entity object, the representation of which we want to add into
	 *             the database
	 */
	void addNote(Note note);

	/**
	 * Method listing all note entities, representations of which are currently
	 * present in database
	 * 
	 * @return list of entity objects based on their corresponding representations
	 *         in database
	 */
	List<Note> getAll();

	/**
	 * Save changes of note's parameters to its corresponding representation in
	 * database
	 * 
	 * @param note entity object, upon which changes were made
	 */
	void saveNote(Note note);

	/**
	 * Method to delete representation of note entity object from database
	 * 
	 * @param note entity object, representation of which is to be removed from
	 *             database
	 */
	void deleteNote(Note note);

}
