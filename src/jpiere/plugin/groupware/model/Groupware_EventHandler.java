package jpiere.plugin.groupware.model;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MOrder;
import org.compiere.model.MRequest;
import org.compiere.model.MRequestType;
import org.compiere.model.MStatus;
import org.compiere.model.MStatusCategory;
import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.osgi.service.event.Event;

import jpiere.plugin.groupware.ics.SendMail_ICS;

public class Groupware_EventHandler extends AbstractEventHandler {
	
	private boolean byPass = false;
	private boolean byPass_ics = false;

	@Override
	protected void initialize() {
		registerTableEvent(IEventTopics.DOC_AFTER_COMPLETE, "C_Order");
		registerTableEvent(IEventTopics.PO_AFTER_NEW, "R_Request");
		registerTableEvent(IEventTopics.PO_AFTER_CHANGE, "R_Request");
		registerTableEvent(IEventTopics.PO_AFTER_CHANGE, "JP_ToDo");
		registerTableEvent(IEventTopics.PO_AFTER_NEW, "JP_ToDo");
		registerTableEvent(IEventTopics.PO_BEFORE_DELETE, "JP_ToDo");

	}

	@Override
	protected void doHandleEvent(Event event) {
		if(event.getTopic().equals(IEventTopics.DOC_AFTER_COMPLETE)) {
			PO po = getPO(event);
			
			if(po.get_TableName().equals("C_Order") && po.get_ValueAsBoolean("LIT_isWriteCalendarNow")) {
				MOrder order = (MOrder)po;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				
				MToDo jpTodo = new MToDo(Env.getCtx(), 0, null);
				jpTodo.setAD_Table_ID(MOrder.Table_ID);
				jpTodo.setRecord_ID(order.getC_Order_ID());
				jpTodo.setJP_ToDo_Type(MToDo.JP_TODO_TYPE_Schedule);
				jpTodo.setJP_ToDo_Status(MToDo.JP_TODO_STATUS_NotYetStarted);
				jpTodo.setName(order.getDocumentNo()+"   "+order.getDateOrdered().toLocalDateTime().toLocalDate().format(formatter));
				if(order.getDescription()!=null && !order.getDescription().isEmpty())
					jpTodo.setDescription(order.getDescription());
				jpTodo.setC_BPartner_ID(order.getC_BPartner_ID());
				jpTodo.setAD_User_ID(order.getSalesRep_ID());
				LocalDateTime date = order.getDatePromised().toLocalDateTime().with(LocalTime.of(10, 0));
				jpTodo.setJP_ToDo_ScheduledStartDate(Timestamp.valueOf(date));
				jpTodo.setJP_ToDo_ScheduledStartTime(Timestamp.valueOf(date));
				jpTodo.setJP_ToDo_ScheduledEndDate(Timestamp.valueOf(date.plusHours(1)));
				jpTodo.setJP_ToDo_ScheduledEndTime(Timestamp.valueOf(date.plusHours(1)));
				jpTodo.setQty(BigDecimal.ONE);
				
				jpTodo.saveEx();
			}
		}
		else if(event.getTopic().equals(IEventTopics.PO_AFTER_NEW) || event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE)) {
			PO po = getPO(event);
			if(!byPass && po.get_TableName().equals("R_Request") && po.get_ValueAsInt("SalesRep_ID")>0 && po.get_Value("DateNextAction")!=null) {
				MRequest request = (MRequest)po;
				MToDo jpTodo = null;
				
				jpTodo = new Query(Env.getCtx(), MToDo.Table_Name, "R_Request_ID=?", null)
						.setOnlyActiveRecords(true)
						.setClient_ID()
						.setParameters(request.getR_Request_ID())
						.first();
				if(jpTodo!=null) {
					jpTodo.setAD_User_ID(request.getSalesRep_ID());
					jpTodo.setJP_ToDo_ScheduledStartDate(request.getDateNextAction());
					jpTodo.setJP_ToDo_ScheduledStartTime(request.getDateNextAction());
					BigDecimal qty = request.getQtyPlan();
					calcQty_EndTime(qty, jpTodo);
					
					byPass = true;
					jpTodo.saveEx();
					byPass = false;
				}
				else {
					Trx trx = Trx.get(request.get_TrxName(), false);
					try {
						trx.commit(true);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					request.load(request.get_TrxName());
					jpTodo = new MToDo(Env.getCtx(), 0, null);
					jpTodo.setAD_Table_ID(MRequest.Table_ID);
					jpTodo.setRecord_ID(request.getR_Request_ID());
					jpTodo.setR_Request_ID(request.getR_Request_ID());
					jpTodo.setJP_ToDo_Type(MToDo.JP_TODO_TYPE_Schedule);
					jpTodo.setJP_ToDo_Status(MToDo.JP_TODO_STATUS_NotYetStarted);
					jpTodo.setName(request.getDocumentNo()+"   "+((MRequestType)request.getR_RequestType()).getName());
					if(request.getSummary()!=null && !request.getSummary().isEmpty())
						jpTodo.setDescription(request.getSummary());
					jpTodo.setC_BPartner_ID(request.getC_BPartner_ID());
					jpTodo.setAD_User_ID(request.getSalesRep_ID());
					jpTodo.setJP_ToDo_ScheduledStartDate(request.getDateNextAction());
					jpTodo.setJP_ToDo_ScheduledStartTime(request.getDateNextAction());
					BigDecimal qty = request.getQtyPlan();
					calcQty_EndTime(qty, jpTodo);
					
					jpTodo.saveEx();
				}
			}
			else if(!byPass && po.get_TableName().equals("JP_ToDo") && po.get_ValueAsInt("R_Request_ID")>0 && 
					(po.is_ValueChanged("AD_User_ID") ||  po.is_ValueChanged("JP_ToDo_ScheduledStartDate") || po.is_ValueChanged("JP_ToDo_ScheduledStartTime") 
							|| po.is_ValueChanged("JP_ToDo_ScheduledEndDate") || po.is_ValueChanged("JP_ToDo_ScheduledEndTime") || po.is_ValueChanged("isComplete"))) {
				MToDo personalTODO = (MToDo)po;
				MRequest request = new MRequest(Env.getCtx(), personalTODO.getR_Request_ID(), null);
				LocalDateTime dateT = LocalDateTime.of(personalTODO.getJP_ToDo_ScheduledStartDate().toLocalDateTime().toLocalDate(), personalTODO.getJP_ToDo_ScheduledStartTime().toLocalDateTime().toLocalTime());
				request.setDateNextAction(Timestamp.valueOf(dateT));
				request.setQtyPlan(personalTODO.getQty());
				request.setSalesRep_ID(personalTODO.getAD_User_ID());
				if(personalTODO.isComplete()) {
					MStatus[] stats = ((MStatusCategory)request.getR_Status().getR_StatusCategory()).getStatus(true);
					MStatus statusClose = Arrays.stream(stats)
							.filter(x -> x.isClosed())
							.findFirst()
							.get();
					if(statusClose!=null) {
						request.setR_Status_ID(statusClose.getR_Status_ID());
						
						request.setResult("CLOSE");
						request.setCloseDate(new Timestamp(System.currentTimeMillis()));
					}
				}
				byPass = true;
				request.saveEx();
				byPass = false;
			}
			
			if(!byPass_ics && po.get_TableName().equals("JP_ToDo") && po.get_ValueAsBoolean("SendIt")) {
				MToDo personalTODO = (MToDo)po;
				
				String uid = personalTODO.getSource_UUID();
				if(uid == null || uid.isEmpty())
					uid = UUID.randomUUID().toString();
				BigDecimal seq = personalTODO.getSequence();
				if(seq == null || seq.compareTo(BigDecimal.ZERO)==0)
					seq = BigDecimal.ZERO;
				else
					seq = seq.add(BigDecimal.ONE);
				String recipient = personalTODO.getRecipientTo();
				List<String>contactEmail = new ArrayList<String>();
				if(recipient.split(",").length>0)
					contactEmail.addAll(Arrays.asList(recipient.split(",")));
				contactEmail.add(MUser.get(personalTODO.getAD_User_ID()).getEMail());//utente assegnato il calendario principale
				String description = personalTODO.getDescription();
				
				SendMail_ICS mail_ICS = new SendMail_ICS()
						.withEmailContacts(contactEmail)
						.withEmailFrom(MUser.get(personalTODO.getCreatedBy()).getEMail())
						.withSubject(personalTODO.getName())
						.withMessage(description)
						.withStartDate(personalTODO.getJP_ToDo_ScheduledStartTime())
						.withEndDate(personalTODO.getJP_ToDo_ScheduledEndTime());
				if(mail_ICS.sendMail_withICS(uid, seq.toString(), false)) {
					try {
						DB.commit(false, personalTODO.get_TrxName());
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					personalTODO.setDescription(description);
					personalTODO.setSequence(seq);
					if(personalTODO.getSource_UUID()==null || personalTODO.getSource_UUID().isEmpty())
						personalTODO.setSource_UUID(uid);
					byPass_ics = true;
					personalTODO.saveEx();
					byPass_ics = false;
					//DB.executeUpdateEx("UPDATE JP_ToDo SET Description=?, Sequence=? WHERE AD_Client_ID=? AND JP_ToDo_ID=?", new Object[] {description, seq, personalTODO.getAD_Client_ID(), personalTODO.getJP_ToDo_ID()}, null);
					//DB.executeUpdateEx("UPDATE JP_ToDo SET Source_UUID=? WHERE AD_Client_ID=? AND JP_ToDo_ID=? AND Source_UUID IS NULL", new Object[] {uid, personalTODO.getAD_Client_ID(), personalTODO.getJP_ToDo_ID()}, null);
				}
			}
		}
		else if(event.getTopic().equals(IEventTopics.PO_BEFORE_DELETE)) {
			PO po = getPO(event);
			if(po.get_TableName().equals("JP_ToDo") && po.get_ValueAsBoolean("SendIt")) {
				MToDo personalTODO = (MToDo)po;
				
				String uid = personalTODO.getSource_UUID();
				if(uid == null || uid.isEmpty())
					uid = UUID.randomUUID().toString();
				BigDecimal seq = personalTODO.getSequence();
				if(seq == null || seq.compareTo(BigDecimal.ZERO)==0)
					seq = BigDecimal.ZERO;
				else
					seq = seq.add(BigDecimal.ONE);
				String recipient = personalTODO.getRecipientTo();
				List<String>contactEmail = new ArrayList<String>();
				if(recipient.split(",").length>0)
					contactEmail.addAll(Arrays.asList(recipient.split(",")));
				contactEmail.add(MUser.get(personalTODO.getAD_User_ID()).getEMail());//utente assegnato il calendario principale
				String description = personalTODO.getDescription();
				
				SendMail_ICS mail_ICS = new SendMail_ICS()
						.withEmailContacts(contactEmail)
						.withEmailFrom(MUser.get(personalTODO.getCreatedBy()).getEMail())
						.withSubject(personalTODO.getName())
						.withMessage(description)
						.withStartDate(personalTODO.getJP_ToDo_ScheduledStartTime())
						.withEndDate(personalTODO.getJP_ToDo_ScheduledEndTime());
				
				mail_ICS.sendMail_withICS(uid, seq.toString(), true);
			}
		}
	}
	
	private void calcQty_EndTime(BigDecimal qty, MToDo jpTodo) {
		if(qty==null || qty.compareTo(BigDecimal.ZERO)==0)
			qty = BigDecimal.ONE;
		jpTodo.setQty(qty);
		LocalDateTime toDate_tmp =  jpTodo.getJP_ToDo_ScheduledStartDate().toLocalDateTime();
		toDate_tmp = toDate_tmp.plusHours(qty.longValue());
		Timestamp toDate = Timestamp.valueOf(toDate_tmp);
		jpTodo.setJP_ToDo_ScheduledEndDate(toDate);
		jpTodo.setJP_ToDo_ScheduledEndTime(toDate);
	}

}
