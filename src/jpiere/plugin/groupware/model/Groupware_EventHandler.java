package jpiere.plugin.groupware.model;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MOrder;
import org.compiere.model.MRequest;
import org.compiere.model.MRequestType;
import org.compiere.model.MStatus;
import org.compiere.model.MStatusCategory;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.osgi.service.event.Event;

public class Groupware_EventHandler extends AbstractEventHandler {

	@Override
	protected void initialize() {
		registerTableEvent(IEventTopics.DOC_AFTER_COMPLETE, "C_Order");
		registerTableEvent(IEventTopics.PO_AFTER_NEW, "R_Request");
		registerTableEvent(IEventTopics.PO_AFTER_CHANGE, "JP_ToDo");

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
		else if(event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) {
			PO po = getPO(event);
			if(po.get_TableName().equals("R_Request")) {
				MRequest request = (MRequest)po;
				MToDo jpTodo = null;
				
//				jpTodo = new Query(Env.getCtx(), MToDo.Table_Name, "R_Request_ID=?", null)
//						.setOnlyActiveRecords(true)
//						.setClient_ID()
//						.setParameters(request.getR_Request_ID())
//						.first();
//				if(jpTodo!=null) {
//					jpTodo.setJP_ToDo_ScheduledStartDate(request.getStartDate());
//					jpTodo.setJP_ToDo_ScheduledStartTime(request.getStartTime());
//					jpTodo.setJP_ToDo_ScheduledEndDate(request.getCloseDate());
//					jpTodo.setJP_ToDo_ScheduledEndTime(request.getEndTime());
//					
//					jpTodo.saveEx();
//				}
//				else {
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
				if(qty==null || qty.compareTo(BigDecimal.ZERO)==0)
					qty = BigDecimal.ONE;
				jpTodo.setQty(qty);
				LocalDateTime toDate_tmp =  jpTodo.getJP_ToDo_ScheduledStartDate().toLocalDateTime();
				toDate_tmp = toDate_tmp.plusHours(qty.longValue());
				Timestamp toDate = Timestamp.valueOf(toDate_tmp);
				jpTodo.setJP_ToDo_ScheduledEndDate(toDate);
				jpTodo.setJP_ToDo_ScheduledEndTime(toDate);
				
				jpTodo.saveEx();
//				}
			}
		}
		else if(event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE)) {
			PO po = getPO(event);
			if(po.get_TableName().equals("JP_ToDo") && po.get_ValueAsInt("R_Request_ID")>0 && 
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
				request.saveEx();
			}
		}
		
	}

}
