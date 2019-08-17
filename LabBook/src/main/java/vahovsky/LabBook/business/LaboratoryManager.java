package vahovsky.LabBook.business;

import java.util.ArrayList;
import java.util.List;

import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ItemDAO;

public class LaboratoryManager {
	
	public static List<Item> getItemsOfLaboratory(Laboratory laboratory) {
		ItemDAO itemDao = DAOfactory.INSTANCE.getItemDAO();
		List<Item> items = new ArrayList<>();
		if (itemDao.getAll() != null) {
			List<Item> allItems = itemDao.getAll();
			for (Item i : allItems) {
				if (i.getLaboratory() != null)
					if (i.getLaboratory().getLaboratoryID().equals(laboratory.getLaboratoryID())) {
						items.add(i);
					}
			}
		}
		return items;
	}
}
