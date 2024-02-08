package jpiere.plugin.groupware.callout;

import java.sql.Timestamp;
import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

import jpiere.plugin.groupware.model.MToDo;

public class JPierePluginGroupwareTodoWindowDateEndCallout implements IColumnCallout{

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		// TODO Auto-generated method stub
		
		if(value != null) {
			//SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Timestamp d1;
			Timestamp d2;
			try {
				 d1 = (Timestamp)value;
				 d2 = (Timestamp) mTab.getValue(MToDo.COLUMNNAME_JP_ToDo_ScheduledStartDate);
				 if(d1.compareTo(d2) < 0) {
					 mTab.setValue(MToDo.COLUMNNAME_JP_ToDo_ScheduledStartDate, value);
					}
					
			} catch (Exception e) {
				// TODO Auto-generated catch block
				mTab.setValue(MToDo.COLUMNNAME_JP_ToDo_ScheduledStartDate, value);
				e.printStackTrace();
			}
		}
		
		return null;
	}

}
