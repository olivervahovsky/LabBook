package vahovsky.LabBook.business;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.commons.lang3.RandomStringUtils;

import vahovsky.LabBook.entities.User;
import vahovsky.LabBook.persistent.DAOfactory;
import vahovsky.LabBook.persistent.UserDAO;

// https://stackoverflow.com/questions/46663/how-can-i-send-an-email-by-java-application-using-gmail-yahoo-or-hotmail#47452

public class ForgottenPasswordManagerSimple {

	private static String USER_NAME = "ovlv.projekt"; // GMail user name (just the part before "@gmail.com")
	private static String PASSWORD = "LabBook17ViVa"; // GMail password

	public static void sendPassword(String email) {
		UserDAO userDAO = DAOfactory.INSTANCE.getUserDAO();

		User user = userDAO.getByEmail(email);
		String newPassword = RandomStringUtils.randomAscii(10);
		user.setPassword(newPassword);
		userDAO.saveUser(user);

		String from = USER_NAME;
		String pass = PASSWORD;
		String[] to = { email }; // list of recipient email addresses
		String subject = "Forgotten Password";
		String body = "Your new password is " + userDAO.getByEmail(email).getPassword();

		sendFromGMail(from, pass, to, subject, body);
	}

	private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[to.length];

			// To get the array of addresses
			for (int i = 0; i < to.length; i++) {
				toAddress[i] = new InternetAddress(to[i]);
			}

			for (int i = 0; i < toAddress.length; i++) {
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}

			message.setSubject(subject);
			message.setText(body);
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
}