package vahovsky.LabBook.persistent;

import vahovsky.LabBook.entities.Entity;

public interface EntityDAO {

	/**
	 * Method to delete entity
	 * 
	 * @param entity Entity to be deleted
	 */
	void deleteEntity(Entity entity);

}
