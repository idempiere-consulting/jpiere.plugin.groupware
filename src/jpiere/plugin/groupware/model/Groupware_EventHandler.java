package jpiere.plugin.groupware.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MOrder;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.osgi.service.event.Event;

public class Groupware_EventHandler extends AbstractEventHandler {

	@Override
	protected void initialize() {
		registerTableEvent(IEventTopics.DOC_AFTER_COMPLETE, "C_Order");

	}

	@Override
	protected void doHandleEvent(Event event) {
		if(event.getTopic().equals(IEventTopics.DOC_AFTER_COMPLETE)) {
			PO po = getPO(event);
			
			if(po.get_TableName().equals("C_Order") && po.get_ValueAsBoolean("LIT_isWriteCalendarNow")) {
				MOrder order = (MOrder)po;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				
				MToDo jpTodo = new MToDo(Env.getCtx(), 0, null);
				jpTodo.setAD_Table_ID(order.Table_ID);
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
		
	}

}
