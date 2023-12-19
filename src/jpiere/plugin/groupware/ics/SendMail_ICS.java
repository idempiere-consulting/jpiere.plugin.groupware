package jpiere.plugin.groupware.ics;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.mail.util.ByteArrayDataSource;

import org.compiere.model.MClient;
import org.compiere.util.EMail;
import org.compiere.util.Env;

public class SendMail_ICS {
	
	private List<String> emailsContact;
	private String emailFrom;
	private String subject;
	private String message;
	private Timestamp startDate; 
	private Timestamp endDate;
	
	public SendMail_ICS withEmailContacts(List<String> eContacts) {
		emailsContact = eContacts;
		return this;
	}
	public SendMail_ICS withEmailFrom(String eFrom) {
		emailFrom = eFrom;
		return this;
	}
	public SendMail_ICS withSubject(String strSubject) {
		subject = strSubject;
		return this;
	}
	public SendMail_ICS withMessage(String strMessage) {
		message = strMessage;
		return this;
	}
	public SendMail_ICS withStartDate(Timestamp start) {
		startDate = start;
		return this;
	}
	public SendMail_ICS withEndDate(Timestamp end) {
		endDate = end;
		return this;
	}

	public boolean sendMail_withICS(String uid, String seq, boolean deleteCalendar) {
		MClient client = MClient.get(Env.getCtx());
		String msg = "";
		int countSend = 0;
		EMail email = null;
		
		for (String eContact : emailsContact) {
			if(eContact == null || eContact.isEmpty())
				continue;
			email = client.createEMail(eContact, "TestW", null);
			email.setFrom(/*"marco.longo@idempiere.consulting"*/emailFrom);
			email.setMessageHTML(subject, message);
			email.setHeader("method", "REQUEST");
			email.setHeader("charset","UTF-8");
			email.setHeader("component","VEVENT");
			CalendarRequest calendarRequest = new CalendarRequest.Builder()
					.withSubject(subject)
		            .withBody(message.replaceAll("\n", "\\\\n")) //se no mi da errore sul .ics, devono essere visibili eventuali ritorni a capo 
		            .withToEmail(eContact)
		            .withMeetingStartTime(startDate.toLocalDateTime())
		            .withMeetingEndTime(endDate.toLocalDateTime())
		            .build();
			email.addAttachment(createICS(/*"marco.longo@idempiere.consulting"*/emailFrom, calendarRequest, uid, seq, deleteCalendar));
			msg = email.send();
			
			if (msg.equals(EMail.SENT_OK))
				countSend++;
			
		}
		return countSend==emailsContact.size();
	}
	
	private ByteArrayDataSource createICS (String fromEmail, CalendarRequest calendarRequest, String uid, String seq, boolean deleteCalendar) {
		ByteArrayDataSource ics = null;
		String status = "CONFIRMED";
		String method = "REQUEST";
		String mailTO = calendarRequest.getToEmail();
		String description = calendarRequest.getBody();
		String subject = calendarRequest.getSubject();
				
		if(uid==null || uid.isEmpty())
			uid = calendarRequest.getUid();
		if(deleteCalendar) {
			status = "CANCELLED";
			method = "CANCEL";
			subject = status + ":" +subject;
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
        StringBuilder builder = new StringBuilder();
        builder.append("BEGIN:VCALENDAR\n" +
        		"METHOD:"+ method +"\n" +
        		"PRODID:Microsoft Exchange Server 2010\n" +
        		"VERSION:2.0\n" +
        		"BEGIN:VTIMEZONE\n" +
        		//"TZID:W. Europe Standard Time\n" +
        		"TZID:Europe/Rome\n" +
        		"BEGIN:STANDARD\n" +
        		"DTSTART:16010101T030000\n" +
        		"TZOFFSETFROM:+0200\n" +
        		"TZOFFSETTO:+0100\n" +
        		"RRULE:FREQ=YEARLY;INTERVAL=1;BYDAY=-1SU;BYMONTH=10\n" +
        		"END:STANDARD\n" +
        		"BEGIN:DAYLIGHT\n" +
        		"DTSTART:16010101T020000\n" +
        		"TZOFFSETFROM:+0100\n" +
        		"TZOFFSETTO:+0200\n" +
        		"RRULE:FREQ=YEARLY;INTERVAL=1;BYDAY=-1SU;BYMONTH=3\n" +
        		"END:DAYLIGHT\n" +
        		"END:VTIMEZONE\n" +
                "BEGIN:VEVENT\n" +
                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:" + mailTO + "\n" +
                "ORGANIZER;CN=Foo:MAILTO:" + fromEmail + "\n" +
                "DESCRIPTION;LANGUAGE=it-IT:" + description + " \n" +
                "UID:"+ uid +"\n" +
                "SUMMARY;LANGUAGE=it-IT:"+ subject +"\n" +
                "DTSTART;TZID=Europe/Rome Time:" + formatter.format(calendarRequest.getMeetingStartTime()).replace(" ", "T") + "\n" +
                "DTEND;TZID=Europe/Rome Time:" + formatter.format(calendarRequest.getMeetingEndTime()).replace(" ", "T") + "\n" +
                "CLASS:PUBLIC\n" +
                "PRIORITY:5\n" +
                //"DTSTAMP:20231214T092425Z\n" +
                "DTSTAMP:"+formatter.format(LocalDateTime.now()).replace(" ", "T") +"Z\n" +
                "TRANSP:OPAQUE\n" +
                "STATUS:"+ status +"\n" +
                //"CREATED:20231213T162437Z\n" +
                //"LAST-MODIFIED:20231213T162624Z\n" +
                "SEQUENCE:"+ seq +"\n" +
                //"LOCATION;LANGUAGE=it-IT:Microsoft Teams Meeting\n" +
                "X-MICROSOFT-CDO-APPT-SEQUENCE:"+ seq +"\n" +
                "X-MICROSOFT-CDO-OWNERAPPTID:2122084900\n" +
                "X-MICROSOFT-CDO-BUSYSTATUS:TENTATIVE\n"  +
                "X-MICROSOFT-CDO-INTENDEDSTATUS:BUSY\n"  +
                //"X-MICROSOFT-CDO-ALLDAYEVENT:FALSE\n"  +
                "X-MICROSOFT-CDO-IMPORTANCE:1\n"  +
                "X-MICROSOFT-CDO-INSTTYPE:0"  +
                "X-MICROSOFT-DONOTFORWARDMEETING:FALSE\n"  +
                "X-MICROSOFT-DISALLOW-COUNTER:FALSE\n"  +
                "X-MICROSOFT-REQUESTEDATTENDANCEMODE:DEFAULT\n"  +
                "X-MICROSOFT-ISRESPONSEREQUESTED:TRUE\n"  +
                "BEGIN:VALARM\n" +
                "DESCRIPTION:REMINDER\n" +
                "TRIGGER;RELATED=START:-PT30M\n" +
                "ACTION:DISPLAY\n" +
                "END:VALARM\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR");
		
        try {
			ics = new ByteArrayDataSource(builder.toString(), "text/calendar;method=REQUEST;name=\"invite.ics\"");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		return ics;
	}
}
