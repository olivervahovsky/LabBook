package vahovsky.LabBook.business;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import vahovsky.LabBook.entities.Note;
import vahovsky.LabBook.entities.Project;
import vahovsky.LabBook.entities.Task;
import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.UserDAO;

public class ExportUserDataToExcelManager {

	// https://www.callicoder.com/java-write-excel-file-apache-poi/

	private static String[] projectColumns = { "Name", "Active", "Start of the project", "End of the project",
			"Each item is available" };
	private static String[] taskColumns = { "Project", "Name", "Active", "Start of the project", "End of the project",
			"Each item is available", "Laboratory" };
	private static String[] noteColumns = { "Text", "Timestamp", "Task", "Item" };

	private static UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();

	/**
	 * Method to export user data (his projects, tasks and comments of these
	 * projects) into an excel file
	 * 
	 * @param user User, whose data are to be exported
	 * @throws IOException
	 */
	public static void exportUserData(User user) throws IOException {
		// Create a Workbook
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		/*
		 * CreationHelper helps us create instances of various things like DataFormat,
		 * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
		 */
		CreationHelper createHelper = workbook.getCreationHelper();

		// create a sheet about the projects
		Sheet sheet = workbook.createSheet("Projects");
		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.BLUE.getIndex());
		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		// Create a Row
		Row headerRow = sheet.createRow(0);
		// Create cells
		for (int i = 0; i < projectColumns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(projectColumns[i]);
			cell.setCellStyle(headerCellStyle);
		}
		// Create Cell Style for formatting Date
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

		// listing the projects, their status (active?), time schedule into the
		// "projects" sheet
		List<Project> projects = userDAO.getProjects(user);
		int rowNum = 1;
		for (Project project : projects) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(project.getName());
			row.createCell(1).setCellValue(project.isActive());
			Cell dateFrom = row.createCell(2);
			if (project.getDateFrom() != null) {
				dateFrom.setCellValue(
						// https://stackoverflow.com/questions/22929237/convert-java-time-localdate-into-java-util-date-type
						Date.from(project.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				dateFrom.setCellStyle(dateCellStyle);
			} else
				row.createCell(2).setCellValue("Not defined");
			Cell dateUntil = row.createCell(3);
			if (project.getDateUntil() != null) {
				dateUntil.setCellValue(
						Date.from(project.getDateUntil().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				dateUntil.setCellStyle(dateCellStyle);
			} else
				row.createCell(3).setCellValue("Not defined");
			row.createCell(4).setCellValue(project.isEachItemAvailable());
		}

		// adjust size of the columns to fit the content
		for (int i = 0; i < projectColumns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// create sheet with the list of the tasks
		sheet = workbook.createSheet("Tasks");

		headerRow = sheet.createRow(0);

		for (int i = 0; i < taskColumns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(taskColumns[i]);
			cell.setCellStyle(headerCellStyle);
		}

		List<Task> tasks = userDAO.getTasksOfUser(user);
		rowNum = 1;

		// list the tasks according to projects they are a part of, their status
		// (active?), time schedule, laboratory here the were/are carried out
		for (Task task : tasks) {
			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(task.getProject().getName());
			row.createCell(1).setCellValue(task.getName());
			row.createCell(2).setCellValue(task.isActive());
			Cell dateTimeFrom = row.createCell(3);
			if (task.getDateFrom() != null) {
				dateTimeFrom.setCellValue(
						// https://stackoverflow.com/questions/22929237/convert-java-time-localdate-into-java-util-date-type
						Date.from(task.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				dateTimeFrom.setCellStyle(dateCellStyle);
			} else
				row.createCell(3).setCellValue("Not defined");
			Cell dateTimeUntil = row.createCell(4);
			if (task.getDateUntil() != null) {
				dateTimeUntil.setCellValue(
						Date.from(task.getDateUntil().atStartOfDay(ZoneId.systemDefault()).toInstant()));
				dateTimeUntil.setCellStyle(dateCellStyle);
			} else
				row.createCell(4).setCellValue("Not defined");
			row.createCell(5).setCellValue(task.isEachItemAvailable());
			if (task.getLaboratory() != null)
				row.createCell(6).setCellValue(task.getLaboratory().getName());
			else
				row.createCell(6).setCellValue("Not defined");
		}

		for (int i = 0; i < taskColumns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// create sheet listing the notes
		sheet = workbook.createSheet("Notes");

		headerRow = sheet.createRow(0);

		for (int i = 0; i < noteColumns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(noteColumns[i]);
			cell.setCellStyle(headerCellStyle);
		}

		// list the notes + the date and time of their creation, project, task or item they belong to
		List<Note> notes = userDAO.getNotes(user);
		rowNum = 1;
		for (Note note : notes) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(note.getText());
			Cell timeStamp = row.createCell(1);
			// https://stackoverflow.com/questions/19431234/converting-between-java-time-localdatetime-and-java-util-date
			timeStamp.setCellValue(Date.from(note.getTimestamp().atZone(ZoneId.systemDefault()).toInstant()));
			timeStamp.setCellStyle(dateCellStyle);
			if (note.getProject() != null)
				row.createCell(2).setCellValue(note.getProject().getName());
			else
				row.createCell(2).setCellValue("Not defined");
			if (note.getTask() != null)
				row.createCell(2).setCellValue(note.getTask().getName());
			else
				row.createCell(2).setCellValue("Not defined");
			if (note.getItem() != null)
				row.createCell(3).setCellValue(note.getItem().getName());
			else
				row.createCell(3).setCellValue("Not defined");
		}

		for (int i = 0; i < noteColumns.length; i++) {
			sheet.autoSizeColumn(i);
		}

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream("userData.xlsx");
		workbook.write(fileOut);
		fileOut.close();

		// Closing the workbook
		workbook.close();

	}

}
