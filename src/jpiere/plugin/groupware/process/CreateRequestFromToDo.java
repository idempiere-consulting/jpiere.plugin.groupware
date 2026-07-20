package jpiere.plugin.groupware.process;

import java.sql.Timestamp;
import java.util.List;

import org.compiere.model.MProject;
import org.compiere.model.MRequest;
import org.compiere.model.MStatus;
import org.compiere.model.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import jpiere.plugin.groupware.model.MToDo;

public class CreateRequestFromToDo extends SvrProcess {

	private int p_todoID = 0;
	private int  p_requestTypeID = 0;
	@Override
	protected void prepare() {

//		ProcessInfoParameter[] para = getParameter();
//		for (int i = 0; i < para.length; i++)
//		{
//			String name = para[i].getParameterName();
//			if (para[i].getParameter() == null)
//				;
//			else if (name.equals("C_Project_ID"))
//				p_projectID = para[i].getParameterAsInt();
//			else if (name.equals("R_RequestType_ID"))
//				p_requestTypeID = para[i].getParameterAsInt();
//			else if (name.equals("Priority"))
//				p_priority = para[i].getParameterAsString();
//		}
		p_todoID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		
		Query qry = null;
		List<MToDo> listToDo = null;
		if(p_todoID>0) {
			qry = new Query(getCtx(), MToDo.Table_Name, "JP_ToDo_ID=?", null)
					.setClient_ID()
					.setParameters(p_todoID)
					.setOnlyActiveRecords(true);
		}else if(getProcessInfo().getAD_InfoWindow_ID()>0) {
			qry = new Query(getCtx(), MToDo.Table_Name, "", null)
					.setClient_ID()
					.setOnlyActiveRecords(true)
					.setOnlySelection(getAD_PInstance_ID())
					.setOrderBy("C_BPartner_ID, Created");
		}
		listToDo = qry.list();
		
		MProject prj = null;
		MRequest requestFrom = null;
		MRequest req_ticket = null;
		for (MToDo todo : listToDo) {
			if(todo.getR_Request_ID() < 0) {
				addLog(todo.getJP_ToDo_ID(), todo.getUpdated(), null, 
						"Cal - "+todo.getName()+ " [JP_TODO_ID: "+todo.getJP_ToDo_ID()+"], Ticket di origine non presente", 
						MToDo.Table_ID, todo.getJP_ToDo_ID());
				continue;
			}
			requestFrom = new MRequest(getCtx(), todo.getR_Request_ID(), get_TrxName());
//			if(todo.getC_Project_ID() > 0)
				prj = new MProject(getCtx(), todo.getC_Project_ID(), get_TrxName());
//			else
//				prj = new MProject(getCtx(), p_projectID, get_TrxName());
			
			req_ticket = new MRequest(getCtx(), 0, todo.get_TrxName());
			MRequest.copyValues(requestFrom, req_ticket);
			req_ticket.set_ValueNoCheck("AD_Client_ID", requestFrom.getAD_Client_ID());
			req_ticket.setAD_Org_ID(todo.getAD_Org_ID());
			req_ticket.set_ValueNoCheck("Created", new Timestamp(System.currentTimeMillis()));
			req_ticket.set_ValueNoCheck("Updated", new Timestamp(System.currentTimeMillis()));
			req_ticket.setProcessed (false);
			req_ticket.set_ValueNoCheck("DocumentNo", null);
			req_ticket.setR_Status_ID();
			//req_ticket.setR_Status_ID(MStatus.getDefault(getCtx(), p_requestTypeID).getR_Status_ID());
//			if(prj.get_ValueAsInt("M_Product_ID") > 0)
//				req_ticket.setM_Product_ID(prj.get_ValueAsInt("M_Product_ID"));
			req_ticket.set_ValueOfColumn("Name", ((todo.getName()==null||todo.getName().isEmpty())?"-":todo.getName()));
			String descrForSummary = todo.get_ValueAsString("activity_description");
			req_ticket.setSummary(((descrForSummary.isEmpty())?"-":descrForSummary));
			req_ticket.setQtyPlan(todo.getQty());
			if(req_ticket.getR_RequestRelated_ID()<=0) //se il 'copyFrom' non ha la richiesta correlata, la imposto
				req_ticket.setR_RequestRelated_ID(todo.getR_Request_ID());
			//per questa creazione NON LA DEVO ASSEGNARE, così NON SCATENO EventHandler 
			req_ticket.setSalesRep_ID(-1);
			req_ticket.setDateNextAction(null);
			//req_ticket.setAD_Role_ID(adRoleID);
			///////////
			
			req_ticket.save();
			
			String message = Msg.getMsg(Env.getCtx(), "R_Request_ID")+" "+req_ticket.getDocumentNo();
			addLog(req_ticket.getR_Request_ID(), req_ticket.getUpdated(), null, message, req_ticket.get_Table_ID(), req_ticket.getR_Request_ID());
			
			prj = null;
			
		}

		return (getProcessInfo().getLogInfo(false).equals(""))?"Process finished...":getProcessInfo().getLogInfo(false);
	}

}
