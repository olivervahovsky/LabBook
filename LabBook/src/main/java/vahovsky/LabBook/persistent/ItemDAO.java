package vahovsky.LabBook.persistent;

import java.util.List;

import vahovsky.LabBook.entities.Item;

public interface ItemDAO {

	// pridanie tasku do databazy
	void addItem(Item item);

	// zmena tasku v databaze
	void saveItem(Item item);

	// vrati zoznam taskov v databaze
	List<Item> getAll();

	// zmaze task
	void deleteItem(Item item);

	// vrati item podla id
	Item getByID(Long id);

}
