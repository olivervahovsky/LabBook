package vahovsky.LabBook.persistent;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.cj.jdbc.MysqlDataSource;

public enum DAOfactory {

	INSTANCE;

	private JdbcTemplate jdbcTemplate;
	private UserDAO userDAO;
	private ProjectDAO projectDAO;
	private AdminDAO adminDAO;
	private TaskDAO taskDAO;
	private ItemDAO itemDAO;
	private LaboratoryDAO laboratoryDAO;
	private NoteDAO noteDAO;

	public UserDAO getUserDAO() {
		if (userDAO == null) {
			userDAO = new MysqlUserDAO(getJbdcTemplate());
		}
		return userDAO;
	}
	
	public AdminDAO getAdminDAO() {
		if (adminDAO == null) {
			adminDAO = new MysqlAdminDAO(getJbdcTemplate());
		}
		return adminDAO;
	}
	
	public ProjectDAO getProjectDAO() {
		if (projectDAO == null) {
			projectDAO = new MysqlProjectDAO(getJbdcTemplate());
		}
		return projectDAO;
	}
	
	public TaskDAO getTaskDAO() {
		if (taskDAO == null) {
			taskDAO = new MysqlTaskDAO(getJbdcTemplate());
		}
		return taskDAO;
	}
	
	public ItemDAO getItemDAO() {
		if (itemDAO == null) {
			itemDAO = new MysqlItemDAO(getJbdcTemplate());
		}
		return itemDAO;
	}
	
	public LaboratoryDAO getLaboratoryDAO() {
		if (laboratoryDAO == null) {
			laboratoryDAO = new MysqlLaboratoryDAO(getJbdcTemplate());
		}
		return laboratoryDAO;
	}
	
	public NoteDAO getNoteDAO() {
		if (noteDAO == null) {
			noteDAO = new MysqlNoteDAO(getJbdcTemplate());
		}
		return noteDAO;
	}
	
	private JdbcTemplate getJbdcTemplate() {
		if (jdbcTemplate == null) {
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setUser("paz1c");
			dataSource.setPassword("paz1cJeSuper");
			//dataSource.setDatabaseName("lab_book"); // nazov schemy
			dataSource.setUrl("jdbc:mysql://localhost/lab_book?serverTimezone=Europe/Bratislava");
			jdbcTemplate = new JdbcTemplate(dataSource);
		}
		return jdbcTemplate;
	}
	
}
