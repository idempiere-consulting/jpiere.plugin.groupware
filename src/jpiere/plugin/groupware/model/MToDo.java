/******************************************************************************
 * Product: JPiere                                                            *
 * Copyright (C) Hideaki Hagiwara (h.hagiwara@oss-erp.co.jp)                  *
 *                                                                            *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY.                          *
 * See the GNU General Public License for more details.                       *
 *                                                                            *
 * JPiere is maintained by OSS ERP Solutions Co., Ltd.                        *
 * (http://www.oss-erp.co.jp)                                                 *
 *****************************************************************************/
package jpiere.plugin.groupware.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import org.compiere.model.MMessage;
import org.compiere.model.MResourceAssignment;
import org.compiere.model.Query;
import org.compiere.model.X_C_ContactActivity;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.Util;

import it.cnet.idempiere.resourceAttendance.util.UtilResource;

/**
 * JPIERE-0469: JPiere Groupware
 *
 * MToDo
 *
 * @author h.hagiwara
 *
 */
public class MToDo extends X_JP_ToDo implements I_ToDo {

	public MToDo(Properties ctx, int JP_ToDo_Team_ID, String trxName)
	{
		super(ctx, JP_ToDo_Team_ID, trxName);
	}


	public MToDo(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}


	@Override
	protected boolean beforeSave(boolean newRecord)
	{

		String msg = beforeSavePreCheck(newRecord);
		if(!Util.isEmpty(msg))
		{
			log.saveError("Error", msg);
			return false;
		}
		//iDempiereConsulting __27/01/2022 ---- Calculate costs 
		if(this.get_ColumnIndex("LIT_StandardHour")>0 && this.getQty()!=null && this.getJP_ToDo_ScheduledEndTime()!=null){
			UtilResource uResource = new UtilResource();
			int S_Resource_ID = DB.getSQLValue(null, "SELECT S_Resource_ID FROM S_Resource WHERE AD_Client_ID=? AND AD_User_ID=?", getAD_Client_ID(), getAD_User_ID());
			TreeMap<String, BigDecimal> costs =  uResource.calcolateExtra(S_Resource_ID, getJP_ToDo_ScheduledStartTime(), getJP_ToDo_ScheduledEndTime());
			
			set_ValueOfColumn("LIT_StandardHour",costs.get("LIT_StandardHour"));
			set_ValueOfColumn("LIT_StandardCost",costs.get("LIT_StandardCost"));
			set_ValueOfColumn("LIT_ExtraHour",costs.get("LIT_ExtraHour"));
			set_ValueOfColumn("LIT_ExtraCost",costs.get("LIT_ExtraCost"));
			set_ValueOfColumn("LIT_NightHour",costs.get("LIT_NightHour"));
			set_ValueOfColumn("LIT_NightCost",costs.get("LIT_NightCost"));
			set_ValueOfColumn("LIT_HolidayHour",costs.get("LIT_HolidayHour"));
			set_ValueOfColumn("LIT_HolidayCost",costs.get("LIT_HolidayCost"));
			set_ValueOfColumn("LIT_Holidaynighthour",costs.get("LIT_Holidaynighthour"));
			set_ValueOfColumn("LIT_HolidayNightCost",costs.get("LIT_HolidayNightCost"));
			set_ValueOfColumn("LIT_NightExtraHour",costs.get("LIT_NightExtraHour"));
			set_ValueOfColumn("LIT_NightExtraCost",costs.get("LIT_NightExtraCost"));
		}
		//iDempiereConsulting __27/01/2022 ---------- END

		return true;
	}

	public String beforeSavePreCheck(boolean newRecord)
	{
		//** Check User**/
		if(!newRecord)
		{
			int loginUser  = Env.getAD_User_ID(getCtx());
			if(loginUser == getAD_User_ID() || loginUser == getCreatedBy())
			{
				;//Updatable

			}else{
				MMessage msg = MMessage.get(getCtx(), "AccessCannotUpdate");//You cannot update this record - You don't have the privileges
				return msg.get_Translation("MsgText") + " - "+ msg.get_Translation("MsgTip");
			}
		}


		//*** Check ToDo Type ***//
		if(Util.isEmpty(getJP_ToDo_Type()))
		{
			Object[] objs = new Object[]{Msg.getElement(Env.getCtx(), MToDo.COLUMNNAME_JP_ToDo_Type)};
			return Msg.getMsg(Env.getCtx(),"JP_Mandatory",objs);
		}


		//*** Check ToDo Category ***//
//		if(getJP_ToDo_Category_ID() != 0 && (newRecord || is_ValueChanged(MToDoTeam.COLUMNNAME_JP_ToDo_Category_ID)))
//		{
//			if(MToDoCategory.get(getCtx(), getJP_ToDo_Category_ID()).getAD_User_ID() != 0 && MToDoCategory.get(getCtx(), getJP_ToDo_Category_ID()).getAD_User_ID() != getAD_User_ID() )
//			{
//				return Msg.getMsg(getCtx(), "JP_OtherUserToDoCategory");//You can't use other user's ToDo Category.
//			}
//		}


		//*** Check Schedule Time ***//
		if(newRecord || is_ValueChanged(MToDo.COLUMNNAME_JP_ToDo_Type) || is_ValueChanged(MToDo.COLUMNNAME_IsStartDateAllDayJP) || is_ValueChanged(MToDo.COLUMNNAME_IsEndDateAllDayJP)
				|| is_ValueChanged(MToDo.COLUMNNAME_JP_ToDo_ScheduledStartDate) || is_ValueChanged(MToDo.COLUMNNAME_JP_ToDo_ScheduledEndDate)
				|| is_ValueChanged(MToDo.COLUMNNAME_JP_ToDo_ScheduledStartTime) || is_ValueChanged(MToDo.COLUMNNAME_JP_ToDo_ScheduledEndTime))
		{

			if(MToDo.JP_TODO_TYPE_Schedule.equals(getJP_ToDo_Type()))
			{
				if(getJP_ToDo_ScheduledStartDate() == null)
				{
					Object[] objs = new Object[]{Msg.getElement(Env.getCtx(), MToDo.COLUMNNAME_JP_ToDo_ScheduledStartDate)};
					return Msg.getMsg(Env.getCtx(),"JP_Mandatory",objs);
				}

				LocalDate localStartDate = getJP_ToDo_ScheduledStartDate().toLocalDateTime().toLocalDate();
				LocalTime localStartTime = null;
				if(isStartDateAllDayJP())
				{
					localStartTime = LocalTime.MIN;

				}else {

					if(getJP_ToDo_ScheduledStartTime() == null)
					{
						Object[] objs = new Object[]{Msg.getElement(Env.getCtx(), MToDo.COLUMNNAME_JP_ToDo_ScheduledStartTime)};
						return Msg.getMsg(Env.getCtx(),"JP_Mandatory",objs);
					}

					localStartTime = getJP_ToDo_ScheduledStartTime().toLocalDateTime().toLocalTime();
				}

				setJP_ToDo_ScheduledStartTime(Timestamp.valueOf(LocalDateTime.of(localStartDate,localStartTime)));
				setJP_ToDo_ScheduledStartDate(getJP_ToDo_ScheduledStartTime());
			}



			if( MToDo.JP_TODO_TYPE_Task.equals(getJP_ToDo_Type()) || MToDo.JP_TODO_TYPE_Schedule.equals(getJP_ToDo_Type()) )
			{
				if(getJP_ToDo_ScheduledEndDate() == null)
				{
					Object[] objs = new Object[]{Msg.getElement(Env.getCtx(), MToDo.COLUMNNAME_JP_ToDo_ScheduledEndDate)};
					return Msg.getMsg(Env.getCtx(),"JP_Mandatory",objs);
				}

				LocalDate localEndDate = getJP_ToDo_ScheduledEndDate().toLocalDateTime().toLocalDate();
				LocalTime localEndTime = null;
				if(isEndDateAllDayJP())
				{
					localEndTime = LocalTime.MIN;

				}else {

					if(getJP_ToDo_ScheduledEndTime() == null)
					{
						Object[] objs = new Object[]{Msg.getElement(Env.getCtx(), MToDo.COLUMNNAME_JP_ToDo_ScheduledEndTime)};
						return Msg.getMsg(Env.getCtx(),"JP_Mandatory",objs);
					}

					localEndTime = getJP_ToDo_ScheduledEndTime().toLocalDateTime().toLocalTime();

				}

				setJP_ToDo_ScheduledEndTime(Timestamp.valueOf(LocalDateTime.of(localEndDate,localEndTime)));
				setJP_ToDo_ScheduledEndDate(getJP_ToDo_ScheduledEndTime());

				if(MToDo.JP_TODO_TYPE_Schedule.equals(getJP_ToDo_Type()))
				{
					if(getJP_ToDo_ScheduledStartTime().after(getJP_ToDo_ScheduledEndTime()))
					{
						return Msg.getElement(getCtx(), "JP_ToDo_ScheduledStartTime") + " > " +  Msg.getElement(getCtx(), "JP_ToDo_ScheduledEndTime") ;
					}

				}else if(MToDo.JP_TODO_TYPE_Task.equals(getJP_ToDo_Type())) {

					setJP_ToDo_ScheduledStartDate(getJP_ToDo_ScheduledEndDate());
					setJP_ToDo_ScheduledStartTime(getJP_ToDo_ScheduledEndTime());
				}

			}else if(MToDo.JP_TODO_TYPE_Memo.equals(getJP_ToDo_Type())){

				setJP_ToDo_ScheduledStartDate(null);
				setJP_ToDo_ScheduledStartTime(null);
				setJP_ToDo_ScheduledEndDate(null);
				setJP_ToDo_ScheduledEndTime(null);
			}
		}


		//*** Check ToDo Status***//
		if(newRecord || is_ValueChanged(MToDoTeam.COLUMNNAME_JP_ToDo_Status))
		{
			if(Util.isEmpty(getJP_ToDo_Status()))
			{
				setJP_ToDo_Status(MToDo.JP_TODO_STATUS_NotYetStarted);
			}

			if(MToDoTeam.JP_TODO_STATUS_NotYetStarted.equals(getJP_ToDo_Status()))
			{
				setJP_ToDo_StartTime(null);
				setJP_ToDo_EndTime(null);
				setProcessed(false);

			}else if(MToDoTeam.JP_TODO_STATUS_WorkInProgress.equals(getJP_ToDo_Status())) {

				if(getJP_ToDo_StartTime() == null)
					setJP_ToDo_StartTime(new Timestamp(System.currentTimeMillis()));
				setJP_ToDo_EndTime(null);
				setProcessed(false);

			}else if(MToDoTeam.JP_TODO_STATUS_Completed.equals(getJP_ToDo_Status())) {

				if(getJP_ToDo_StartTime() == null)
					setJP_ToDo_StartTime(new Timestamp(System.currentTimeMillis()));
				setJP_ToDo_EndTime(new Timestamp(System.currentTimeMillis()));
				setProcessed(true);

				//*** Check Statistics info ***//
				if(getJP_ToDo_Team_ID() != 0)
				{
					MToDoTeam teamToDo = new MToDoTeam(getCtx(), getJP_ToDo_Team_ID(), get_TrxName());
					if(MToDoTeam.JP_MANDATORY_STATISTICS_INFO_None.equals(teamToDo.getJP_Mandatory_Statistics_Info()))
					{
						;//Noting to do;

					}else if(MToDoTeam.JP_MANDATORY_STATISTICS_INFO_YesNo.equals(teamToDo.getJP_Mandatory_Statistics_Info())){

						if(Util.isEmpty(getJP_Statistics_YesNo()))
						{
							String msg = Msg.getElement(getCtx(), MToDoTeam.COLUMNNAME_JP_Mandatory_Statistics_Info);
							Object[] objs = new Object[]{Msg.getElement(Env.getCtx(), MToDo.COLUMNNAME_JP_Statistics_YesNo)};
							return msg + " : " + Msg.getMsg(Env.getCtx(),"JP_Mandatory",objs);
						}

					}else if(MToDoTeam.JP_MANDATORY_STATISTICS_INFO_Choice.equals(teamToDo.getJP_Mandatory_Statistics_Info())){

						if(Util.isEmpty(getJP_Statistics_Choice()))
						{
							String msg = Msg.getElement(getCtx(), MToDoTeam.COLUMNNAME_JP_Mandatory_Statistics_Info);
							Object[] objs = new Object[]{Msg.getElement(Env.getCtx(), MToDo.COLUMNNAME_JP_Statistics_Choice)};
							return msg + " : " + Msg.getMsg(Env.getCtx(),"JP_Mandatory",objs);
						}

					}else if(MToDoTeam.JP_MANDATORY_STATISTICS_INFO_DateAndTime.equals(teamToDo.getJP_Mandatory_Statistics_Info())){

						if(getJP_Statistics_DateAndTime() == null)
						{
							String msg = Msg.getElement(getCtx(), MToDoTeam.COLUMNNAME_JP_Mandatory_Statistics_Info);
							Object[] objs = new Object[]{Msg.getElement(Env.getCtx(), MToDo.COLUMNNAME_JP_Statistics_DateAndTime)};
							return msg + " : " + Msg.getMsg(Env.getCtx(),"JP_Mandatory",objs);
						}

					}else if(MToDoTeam.JP_MANDATORY_STATISTICS_INFO_Number.equals(teamToDo.getJP_Mandatory_Statistics_Info())){

						if(get_Value("JP_Statistics_Number") == null)
						{
							String msg = Msg.getElement(getCtx(), MToDoTeam.COLUMNNAME_JP_Mandatory_Statistics_Info);
							Object[] objs = new Object[]{Msg.getElement(Env.getCtx(), MToDo.COLUMNNAME_JP_Statistics_Number)};
							return msg + " : " + Msg.getMsg(Env.getCtx(),"JP_Mandatory",objs);
						}
					}

				}//if(getJP_ToDo_Team_ID() != 0)
			}
		}//if(newRecord || is_ValueChanged(MToDoTeam.COLUMNNAME_JP_ToDo_Status))

		return null;
	}


	@Override
	protected boolean afterSave(boolean newRecord, boolean success)
	{
		if(success && !newRecord && is_ValueChanged(MToDo.COLUMNNAME_JP_ToDo_Status))
		{
			if(MToDo.JP_TODO_STATUS_Completed.equals(getJP_ToDo_Status()))
			{
				processedReminders();
				//iDempiereConsulting __26/10/2021 --- Gestione S_ResourceAssignment
				if(MToDo.JP_TODO_TYPE_Schedule.equals(getJP_ToDo_Type()))
					createResourceAssignment();
				/////
				

			}else if(MToDo.JP_TODO_STATUS_Completed.equals(get_ValueOld(MToDo.COLUMNNAME_JP_ToDo_Status))){

				reprocessReminders();
			}

		}else if (success && !newRecord && is_ValueChanged(MToDo.COLUMNNAME_Processed) ) {

			if(isProcessed())
			{
				processedReminders();
			}else{
				reprocessReminders();
			}

		}

		if(success && !newRecord && is_ValueChanged(MToDo.COLUMNNAME_IsActive))
		{
			if(isActive())
			{
				reactiveReminders();
			}else {
				inactiveReminders();
			}

		}
		//iDempiereConsulting __26/10/2021 --- Gestione S_ResourceAssignment
		if(success && newRecord && MToDo.JP_TODO_STATUS_Completed.equals(getJP_ToDo_Status()) && MToDo.JP_TODO_TYPE_Schedule.equals(getJP_ToDo_Type()))
			createResourceAssignment();


		return true;

	}//afterSave


	public boolean reactiveReminders()
	{
		MToDoReminder[] reminders = getReminders(true);
		for(int i = 0;  i < reminders.length; i++)
		{
			reminders[i].setIsActive(true);
			if(MToDoReminder.JP_TODO_REMINDERTYPE_BroadcastMessage.equals(reminders[i].getJP_ToDo_ReminderType()))
			{
				int AD_BroadcastMessage_ID = reminders[i].sendMessageRemainder();
				reminders[i].setAD_BroadcastMessage_ID(AD_BroadcastMessage_ID);
				reminders[i].setIsSentReminderJP(true);
			}

			reminders[i].saveEx();
		}

		return true;
	}

	public boolean inactiveReminders()
	{
		MToDoReminder[] reminders = getReminders(true);
		for(int i = 0;  i < reminders.length; i++)
		{
			reminders[i].setIsActive(false);
			reminders[i].stopBroadcastMessage();
			reminders[i].saveEx();
		}

		return true;
	}

	public boolean processedReminders()
	{
		MToDoReminder[] reminders = getReminders(true);
		for(int i = 0;  i < reminders.length; i++)
		{
			reminders[i].processedReminder();
		}

		return true;
	}


	public boolean reprocessReminders()
	{
		MToDoReminder[] reminders = getReminders(true);
		for(int i = 0;  i < reminders.length; i++)
		{
			reminders[i].reprocessReminder();
		}

		return true;
	}


	@Override
	protected boolean beforeDelete()
	{

		String msg = beforeDeletePreCheck();
		if(!Util.isEmpty(msg))
		{
			log.saveError("Error", msg);
			return false;
		}

		return true;
	}

	public String beforeDeletePreCheck()
	{
		//** Check User**/
		int loginUser  = Env.getAD_User_ID(getCtx());
		if(loginUser == getAD_User_ID() || loginUser == getCreatedBy())
		{
			//Deleteable;

		}else{

			MMessage msg = MMessage.get(getCtx(), "AccessCannotUpdate");
			return msg.get_Translation("MsgText") + " - "+ msg.get_Translation("MsgTip");
		}


		return null;
	}


	/**
	 * getToDoReminder
	 */
	protected MToDoReminder[] m_ToDoReminders = null;

	public MToDoReminder[] getReminders()
	{
		return getReminders(false);
	}

	public MToDoReminder[] getReminders(boolean requery)
	{

		if (m_ToDoReminders != null && m_ToDoReminders.length >= 0 && !requery)	//	re-load
			return m_ToDoReminders;
		//

		StringBuilder whereClauseFinal = new StringBuilder("JP_ToDo_ID=? AND IsActive = 'Y'");

		//
		List<MToDoReminder> list = new Query(getCtx(), MToDoReminder.Table_Name, whereClauseFinal.toString(), get_TrxName())
										.setParameters(get_ID())
										//.setOrderBy(orderClause)
										.list();

		m_ToDoReminders = list.toArray(new MToDoReminder[list.size()]);

		return m_ToDoReminders;

	}

	public static ArrayList<MToDo>  getRelatedToDos(Properties ctx, MToDo m_ToDo, ArrayList<MToDo> list_ToDo, Timestamp time, boolean isIncludingIndirectRelationships, String trxName)
	{
		if(list_ToDo == null)
		{
			list_ToDo = new ArrayList<MToDo>();
		}

		StringBuilder whereClauseFinal = null;
		String orderClause = MToDo.COLUMNNAME_JP_ToDo_ScheduledStartTime;
		List<MToDo> list = null;

		if(m_ToDo.getJP_Processing1().equals("N"))
		{
			whereClauseFinal = new StringBuilder(" JP_ToDo_Related_ID = ? AND JP_ToDo_ID <> ? ");
			if(time == null)
			{
				list = new Query(ctx, MToDo.Table_Name, whereClauseFinal.toString(), trxName)
						.setParameters(m_ToDo.getJP_ToDo_Related_ID(), m_ToDo.getJP_ToDo_ID() )
						.setOrderBy(orderClause)
						.list();
			}else {

				whereClauseFinal = whereClauseFinal.append(" AND JP_ToDo_ScheduledStartTime >= ?");
				list = new Query(ctx, MToDo.Table_Name, whereClauseFinal.toString(), trxName)
						.setParameters(m_ToDo.getJP_ToDo_Related_ID(), m_ToDo.getJP_ToDo_ID(), time)
						.setOrderBy(orderClause)
						.list();
			}

		}else {

			whereClauseFinal = new StringBuilder(" (JP_ToDo_Related_ID = ? OR JP_ToDo_Related_ID = ?) AND JP_ToDo_ID <> ? ");
			if(time == null)
			{
				list = new Query(ctx, MToDo.Table_Name, whereClauseFinal.toString(), trxName)
						.setParameters(m_ToDo.getJP_ToDo_Related_ID() ,m_ToDo.getJP_ToDo_ID() ,m_ToDo.getJP_ToDo_ID())
						.setOrderBy(orderClause)
						.list();

			}else {

				whereClauseFinal = whereClauseFinal.append(" AND JP_ToDo_ScheduledStartTime >= ?");
				list = new Query(ctx, MToDo.Table_Name, whereClauseFinal.toString(), trxName)
						.setParameters(m_ToDo.getJP_ToDo_Related_ID() ,m_ToDo.getJP_ToDo_ID() ,m_ToDo.getJP_ToDo_ID(), time)
						.setOrderBy(orderClause)
						.list();

			}
		}

		boolean isContained = false;
		for(MToDo todo : list)
		{
			isContained = false;
			for(MToDo td : list_ToDo)
			{
				if(todo.getJP_ToDo_ID() == td.getJP_ToDo_ID())
				{
					isContained = true;
				}
			}

			if(isContained)
				continue;

			list_ToDo.add(todo);
			if(isIncludingIndirectRelationships)
			{
				if(todo.getJP_Processing1().equals("Y"))
				{
					list_ToDo = MToDo.getRelatedToDos(ctx, todo, list_ToDo, time, true, trxName);
				}
			}

		}

		return list_ToDo;
	}
	
	//iDempiereConsulting __26/10/2021 --- Gestione S_ResourceAssignment
	private void createResourceAssignment() {
		//Controllo per evitare doppinoni di resource assignment su stesso schedule
		int result = DB.getSQLValueEx(null, "SELECT S_ResourceAssignment_ID FROM S_ResourceAssignment WHERE AD_Client_ID=? AND JP_ToDo_ID=?", getAD_Client_ID(), getJP_ToDo_ID());
		if(result>0)
			return;
		/////
		
		MResourceAssignment resAssignment = new MResourceAssignment(getCtx(), 0, null);
		X_C_ContactActivity cTask = new X_C_ContactActivity(getCtx(), getC_ContactActivity_ID(), null);
		
		//by pass per EventHandler plug-in resourceAttendance
		resAssignment.setQty(BigDecimal.ZERO);
		//////
		resAssignment.set_ValueOfColumn("JP_ToDo_ID", getJP_ToDo_ID());
		resAssignment.setAD_Org_ID(cTask.getAD_Org_ID());
		resAssignment.set_ValueOfColumn("C_ContactActivity_ID",cTask.getC_ContactActivity_ID());
		resAssignment.setAssignDateFrom(getJP_ToDo_ScheduledStartTime());

		//per calcolo/impostazione direttamente da evento calendario di quantità ore addebitate
		//resAssignment.setAssignDateTo(getJP_ToDo_ScheduledEndTime());
		long minutes = (getQty().multiply(new BigDecimal(60))).longValue();
		LocalDateTime start = getJP_ToDo_ScheduledStartTime().toLocalDateTime();
		resAssignment.setAssignDateTo(Timestamp.valueOf(start.plusMinutes(minutes)));
		/////
		
		resAssignment.setName(getName());
		resAssignment.setDescription(getDescription());
		int resourceID = DB.getSQLValue(null, "SELECT S_Resource_ID FROM S_Resource WHERE isActive='Y' AND AD_Client_ID=? AND AD_User_ID=?", cTask.getAD_Client_ID(),getAD_User_ID());
		resAssignment.setS_Resource_ID(resourceID);
		resAssignment.set_ValueOfColumn("M_Product_ID",cTask.get_ValueAsInt("M_Product_ID"));
		if(cTask.get_ValueAsInt("C_BPartner_ID")>0)
			resAssignment.set_ValueOfColumn("C_BPartner_ID", cTask.get_ValueAsInt("C_BPartner_ID"));
		resAssignment.set_ValueOfColumn("isDoNotInvoice", cTask.get_ValueAsBoolean("isDoNotInvoice"));
		resAssignment.set_ValueOfColumn("Percent", new BigDecimal(100));
		resAssignment.set_ValueOfColumn("PlannedQty", BigDecimal.ZERO);
		if(cTask.get_ValueAsInt("C_Project_ID")>0)
			resAssignment.set_ValueOfColumn("C_Project_ID",cTask.get_ValueAsInt("C_Project_ID"));
		//resAssignment.set_ValueOfColumn("Priority",p_priority); TODO 
		resAssignment.set_ValueOfColumn("IsApproved",false);
		resAssignment.set_ValueOfColumn("IsInvoiced",false);
		if(getProductTransfer_ID()>0)
			resAssignment.set_ValueOfColumn("ProductTransfer_ID", getProductTransfer_ID());
		resAssignment.saveEx();
		
	}
	//iDempiereConsulting __26/10/2021 -------END
	
	@Override
	public int getParent_Team_ToDo_ID()
	{
		return getJP_ToDo_Team_ID();
	}


	@Override
	public void setJP_Mandatory_Statistics_Info(String JP_Mandatory_Statistics_Info)
	{
		return ;
	}


	@Override
	public String getJP_Mandatory_Statistics_Info()
	{
		return null;
	}


	@Override
	public void setJP_Team_ID(int JP_Team_ID)
	{
		return ;
	}


	@Override
	public int getJP_Team_ID()
	{
		return 0;
	}


	@Override
	public void setUpdated(Timestamp updated)
	{
		set_ValueNoCheck("Updated", updated);
	}


	@Override
	public boolean isCreatedToDoRepeatedly()
	{
		return getJP_Processing1().equals("Y");
	}


	@Override
	public void setisCreatedToDoRepeatedly(boolean Processed)
	{
		setJP_Processing1(Processed == true? "Y" : "N");
	}


	@Override
	public int getRelated_ToDo_ID()
	{
		return getJP_ToDo_Related_ID();
	}


	@Override
	public void setS_ResourceAssignment_ID(int S_ResourceAssignment_ID) {
		return ;
	}


	@Override
	public int getS_ResourceAssignment_ID() {
		return 0;
	}




}
