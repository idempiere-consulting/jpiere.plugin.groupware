package jpiere.plugin.groupware.process;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.session.SessionManager;
import org.compiere.model.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import jpiere.plugin.groupware.form.ToDoCalendar;

public class CreateToDoForRequest extends SvrProcess {

	@Override
	protected void prepare() {

	}

	@Override
	protected String doIt() throws Exception {
		
		int[] listID = new Query(getCtx(), "R_Request", "", get_TrxName())
				.setOnlySelection(getAD_PInstance_ID())
				.getIDs();
		if(listID.length >1)
			return "@Error@ Selezionare una sola richiesta per creazione calendario";
		
		AEnv.executeAsyncDesktopTask(new Runnable() {
			
			@Override
			public void run() {
				//023de22e-b814-4271-8964-ceff5d5d2420
				int adFormID = DB.getSQLValue(get_TrxName(), "SELECT AD_Form_ID FROM AD_Form WHERE AD_Form_UU=?", "023de22e-b814-4271-8964-ceff5d5d2420"); //ToDo Calendar --- JPIERE-0471:JPPS
				if(adFormID > 0) {
					ADForm form = SessionManager.getAppDesktop().openForm(adFormID);
					ToDoCalendar cal = (ToDoCalendar)form.getICustomForm();
					cal.setRequestforCalendar(listID[0]);
				}
			}
		});
		
		return "";
	}

}
