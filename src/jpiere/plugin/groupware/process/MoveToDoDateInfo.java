package jpiere.plugin.groupware.process;

import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;

import org.compiere.model.Query;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

import jpiere.plugin.groupware.model.MToDo;

public class MoveToDoDateInfo extends SvrProcess {

	private int p_addDays = 0;
	private Timestamp p_dateTo = null;
	private int p_userID = -1;
	private int p_projectID = -1;
	private int p_bpartnerID = -1;
	private int p_todoCategoryID = -1;
	
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if(name.equals("AddDays"))
				p_addDays = para[i].getParameterAsInt();
			else if(name.equals("DateTo"))
				p_dateTo = para[i].getParameterAsTimestamp();
			else if(name.equals("Resource")) //AD_User_ID
				p_userID  =  para[i].getParameterAsInt();
			else if(name.equals("Project")) //C_Project_ID
				p_projectID  =  para[i].getParameterAsInt();
			else if(name.equals("BPartner")) //C_BPartner_ID
				p_bpartnerID  =  para[i].getParameterAsInt();
			else if(name.equals("Todo_Category")) //JP_ToDo_Category_ID
				p_todoCategoryID  =  para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
		
	}

	@Override
	protected String doIt() throws Exception {
		String msg = "";
		List<MToDo> listEventTODO = new Query(getCtx(), "JP_ToDo", "IsActive='Y'", null)
				.setClient_ID()
				.setOnlySelection(getAD_PInstance_ID())
				.setOrderBy("JP_ToDo_ScheduledStartDate")
				.list();
		int sizeList = listEventTODO.size();
		int count = 0;
		if(sizeList > 0) {
			for (MToDo dateEvent : listEventTODO) {
				if(dateEvent.getJP_ToDo_Status().equals("NY")) {
					if(p_addDays>0) {
						dateEvent.setJP_ToDo_ScheduledStartDate(Timestamp.valueOf(dateEvent.getJP_ToDo_ScheduledStartDate().toLocalDateTime().plusDays(Long.parseLong(""+p_addDays))));
						dateEvent.setJP_ToDo_ScheduledEndDate(Timestamp.valueOf(dateEvent.getJP_ToDo_ScheduledEndDate().toLocalDateTime().plusDays(Long.parseLong(""+p_addDays))));
					}
					else if(p_dateTo!=null) {
						dateEvent.setJP_ToDo_ScheduledStartDate(p_dateTo);
						dateEvent.setJP_ToDo_ScheduledEndDate(p_dateTo);
					}
					if(p_userID > 0)
						dateEvent.setAD_User_ID(p_userID);
					if(p_projectID > 0)
						dateEvent.setC_Project_ID(p_projectID);
					if(p_bpartnerID > 0)
						dateEvent.setC_BPartner_ID(p_bpartnerID);
					if(p_todoCategoryID > 0)
						dateEvent.setJP_ToDo_Category_ID(p_todoCategoryID);
					dateEvent.saveEx();
					count++;
				}
			}
		}
		int diff = sizeList - count;
		if(diff > 0)
			msg = "Eventi calendario spostati: "+count+" -- Eventi calendario NON spostati: "+diff+", perche' NON 'Not yet started'";
		else
			msg = "Eventi calendario spostati: "+count;
		
		return msg;
	}

}
