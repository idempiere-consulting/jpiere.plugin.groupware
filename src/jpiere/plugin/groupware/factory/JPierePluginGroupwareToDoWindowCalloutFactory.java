package jpiere.plugin.groupware.factory;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IColumnCalloutFactory;

import jpiere.plugin.groupware.callout.JPierePluginGroupwareTodoWindowDateEndCallout;
import jpiere.plugin.groupware.callout.JPierePluginGroupwareTodoWindowDateStartCallout;
import jpiere.plugin.groupware.model.MToDo;

public class JPierePluginGroupwareToDoWindowCalloutFactory implements IColumnCalloutFactory{

	@Override
	public IColumnCallout[] getColumnCallouts(String tableName, String columnName) {
		// TODO Auto-generated method stub
		
		List<IColumnCallout> list = new ArrayList<IColumnCallout>();
		
		if(tableName.equalsIgnoreCase(MToDo.Table_Name) && (columnName.equalsIgnoreCase(MToDo.COLUMNNAME_JP_ToDo_ScheduledStartDate)) ) {
			list.add(new JPierePluginGroupwareTodoWindowDateStartCallout());
		}
		
		if(tableName.equalsIgnoreCase(MToDo.Table_Name) && (columnName.equalsIgnoreCase(MToDo.COLUMNNAME_JP_ToDo_ScheduledEndDate)) ) {
			list.add(new JPierePluginGroupwareTodoWindowDateEndCallout());
		}
		
		
		return list != null ? list.toArray(new IColumnCallout[0]) : new IColumnCallout[0];
	}

}
