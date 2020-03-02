package vahovsky.LabBook.test.business;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import vahovsky.LabBook.business.ExportUserDataToExcelManager;
import vahovsky.LabBook.entities.Item;
import vahovsky.LabBook.entities.Laboratory;
import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.ItemDAO;
import vahovsky.LabBook.persistent.LaboratoryDAO;
import vahovsky.LabBook.persistent.NoteDAO;
import vahovsky.LabBook.persistent.ProjectDAO;
import vahovsky.LabBook.persistent.TaskDAO;
import vahovsky.LabBook.persistent.UserDAO;

public class ExportUserDataToExcelManagerTest {

	/**
	 * Method tests if method <code>exportUserData(User user)</code> in class
	 * <code>ExportUserDataToExcelManager</code> works properly. Test user, his
	 * project and items, laboratory, tasks and note of the project are created.
	 * Data are exported into excel file and consequently number of sheets and rows
	 * in sheets are compared to expected values.
	 */
	@Test
	void excelTest() {

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

		Laboratory testLaboratory = new Laboratory();
		testLaboratory.setName("tester");
		testLaboratory.setLocation("testovacia");
		LaboratoryDAO laboratoryDAO = DAOfactory.INSTANCE.getLaboratoryDAO();
		laboratoryDAO.addLaboratory(testLaboratory);

		Item testItem = new Item();
		testItem.setName("test_item1");
		testItem.setQuantity(10);
		testItem.setAvailable(true);
		testItem.setLaboratory(testLaboratory);

		Item testItem2 = new Item();
		testItem2.setName("test_item2");
		testItem2.setQuantity(10);
		testItem2.setAvailable(true);

		Item testItem3 = new Item();
		testItem3.setName("test_item3");
		testItem3.setQuantity(10);
		testItem3.setAvailable(true);

		ItemDAO itemDAO = DAOfactory.INSTANCE.getItemDAO();
		itemDAO.addItem(testItem);
		itemDAO.addItem(testItem2);
		itemDAO.addItem(testItem3);

		Task task = new Task();
		task.setProject(project);
		task.setName("testTask");
		task.setActive(true);
		task.setDateTimeFrom(LocalDate.now());
		task.setEachItemAvailable(false);
		task.setCreatedBy(testUser);
		task.setItems(Arrays.asList(testItem, testItem2, testItem3));
		TaskDAO taskDAO = DAOfactory.INSTANCE.getTaskDAO();
		taskDAO.addTask(task);

		Task task1 = new Task();
		task1.setProject(project);
		task1.setName("testTask");
		task1.setActive(true);
		task1.setDateTimeFrom(LocalDate.now());
		task1.setEachItemAvailable(false);
		task1.setCreatedBy(testUser);
		task1.setItems(Arrays.asList(testItem, testItem2, testItem3));
		taskDAO.addTask(task1);

		Note note = new Note();
		note.setText("testovaci text");
		note.setTimestamp(LocalDateTime.now());
		note.setAuthor(testUser);
		note.setProject(project);
		NoteDAO noteDAO = DAOfactory.INSTANCE.getNoteDAO();
		noteDAO.addNote(note);

		try {
			ExportUserDataToExcelManager.exportUserData(testUser);

			// https://stackoverflow.com/questions/6896435/count-number-of-worksheets-in-excel-file
			File excelWorkbook = new File("userData.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("userData.xlsx"));
			assertTrue(workbook.getNumberOfSheets() == 3);
			assertTrue(workbook.getSheetAt(0).getPhysicalNumberOfRows() == 2);
			assertTrue(workbook.getSheetAt(1).getPhysicalNumberOfRows() == 3);
			assertTrue(workbook.getSheetAt(2).getPhysicalNumberOfRows() == 2);
			workbook.close();
			excelWorkbook.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}

		itemDAO.deleteEntity(testItem);
		itemDAO.deleteEntity(testItem2);
		itemDAO.deleteEntity(testItem3);
		laboratoryDAO.deleteEntity(testLaboratory);

		userDAO.deleteEntity(testUser);
	}

}
