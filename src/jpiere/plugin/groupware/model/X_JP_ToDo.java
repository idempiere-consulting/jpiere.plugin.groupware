/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package jpiere.plugin.groupware.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for JP_ToDo
 *  @author iDempiere (generated) 
 *  @version Release 10 - $Id$ */
@org.adempiere.base.Model(table="JP_ToDo")
public class X_JP_ToDo extends PO implements I_JP_ToDo, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20220222L;

    /** Standard Constructor */
    public X_JP_ToDo (Properties ctx, int JP_ToDo_ID, String trxName)
    {
      super (ctx, JP_ToDo_ID, trxName);
      /** if (JP_ToDo_ID == 0)
        {
			setAD_User_ID (0);
			setIsEndDateAllDayJP (false);
// N
			setIsOpenToDoJP (true);
// Y
			setIsStartDateAllDayJP (false);
// N
			setJP_Processing1 (null);
// N
			setJP_ToDo_ID (0);
			setJP_ToDo_Status (null);
// NY
			setJP_ToDo_Type (null);
// T
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_JP_ToDo (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 7 - System - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder ("X_JP_ToDo[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Table getAD_Table() throws RuntimeException
	{
		return (org.compiere.model.I_AD_Table)MTable.get(getCtx(), org.compiere.model.I_AD_Table.Table_ID)
			.getPO(getAD_Table_ID(), get_TrxName());
	}

	/** Set Table.
		@param AD_Table_ID Database Table information
	*/
	public void setAD_Table_ID (int AD_Table_ID)
	{
		if (AD_Table_ID < 1)
			set_Value (COLUMNNAME_AD_Table_ID, null);
		else
			set_Value (COLUMNNAME_AD_Table_ID, Integer.valueOf(AD_Table_ID));
	}

	/** Get Table.
		@return Database Table information
	  */
	public int getAD_Table_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Table_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_User getAD_User() throws RuntimeException
	{
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_ID)
			.getPO(getAD_User_ID(), get_TrxName());
	}

	/** Set User/Contact.
		@param AD_User_ID User within the system - Internal or Business Partner Contact
	*/
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1)
			set_Value (COLUMNNAME_AD_User_ID, null);
		else
			set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
	{
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_ID)
			.getPO(getC_BPartner_ID(), get_TrxName());
	}

	/** Set Business Partner.
		@param C_BPartner_ID Identifies a Business Partner
	*/
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner.
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ContactActivity getC_ContactActivity() throws RuntimeException
	{
		return (org.compiere.model.I_C_ContactActivity)MTable.get(getCtx(), org.compiere.model.I_C_ContactActivity.Table_ID)
			.getPO(getC_ContactActivity_ID(), get_TrxName());
	}

	/** Set Contact Activity.
		@param C_ContactActivity_ID Events, tasks, communications related to a contact
	*/
	public void setC_ContactActivity_ID (int C_ContactActivity_ID)
	{
		if (C_ContactActivity_ID < 1)
			set_Value (COLUMNNAME_C_ContactActivity_ID, null);
		else
			set_Value (COLUMNNAME_C_ContactActivity_ID, Integer.valueOf(C_ContactActivity_ID));
	}

	/** Get Contact Activity.
		@return Events, tasks, communications related to a contact
	  */
	public int getC_ContactActivity_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ContactActivity_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Comments.
		@param Comments Comments or additional information
	*/
	public void setComments (String Comments)
	{
		set_Value (COLUMNNAME_Comments, Comments);
	}

	/** Get Comments.
		@return Comments or additional information
	  */
	public String getComments()
	{
		return (String)get_Value(COLUMNNAME_Comments);
	}

	public org.compiere.model.I_C_Project getC_Project() throws RuntimeException
	{
		return (org.compiere.model.I_C_Project)MTable.get(getCtx(), org.compiere.model.I_C_Project.Table_ID)
			.getPO(getC_Project_ID(), get_TrxName());
	}

	/** Set Project.
		@param C_Project_ID Financial Project
	*/
	public void setC_Project_ID (int C_Project_ID)
	{
		if (C_Project_ID < 1)
			set_Value (COLUMNNAME_C_Project_ID, null);
		else
			set_Value (COLUMNNAME_C_Project_ID, Integer.valueOf(C_Project_ID));
	}

	/** Get Project.
		@return Financial Project
	  */
	public int getC_Project_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ProjectPhase getC_ProjectPhase() throws RuntimeException
	{
		return (org.compiere.model.I_C_ProjectPhase)MTable.get(getCtx(), org.compiere.model.I_C_ProjectPhase.Table_ID)
			.getPO(getC_ProjectPhase_ID(), get_TrxName());
	}

	/** Set Project Phase.
		@param C_ProjectPhase_ID Phase of a Project
	*/
	public void setC_ProjectPhase_ID (int C_ProjectPhase_ID)
	{
		if (C_ProjectPhase_ID < 1)
			set_Value (COLUMNNAME_C_ProjectPhase_ID, null);
		else
			set_Value (COLUMNNAME_C_ProjectPhase_ID, Integer.valueOf(C_ProjectPhase_ID));
	}

	/** Get Project Phase.
		@return Phase of a Project
	  */
	public int getC_ProjectPhase_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ProjectPhase_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_ProjectTask getC_ProjectTask() throws RuntimeException
	{
		return (org.compiere.model.I_C_ProjectTask)MTable.get(getCtx(), org.compiere.model.I_C_ProjectTask.Table_ID)
			.getPO(getC_ProjectTask_ID(), get_TrxName());
	}

	/** Set Project Task.
		@param C_ProjectTask_ID Actual Project Task in a Phase
	*/
	public void setC_ProjectTask_ID (int C_ProjectTask_ID)
	{
		if (C_ProjectTask_ID < 1)
			set_Value (COLUMNNAME_C_ProjectTask_ID, null);
		else
			set_Value (COLUMNNAME_C_ProjectTask_ID, Integer.valueOf(C_ProjectTask_ID));
	}

	/** Get Project Task.
		@return Actual Project Task in a Phase
	  */
	public int getC_ProjectTask_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ProjectTask_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Description.
		@param Description Optional short description of the record
	*/
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription()
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Complete.
		@param IsComplete It is complete
	*/
	public void setIsComplete (boolean IsComplete)
	{
		set_Value (COLUMNNAME_IsComplete, Boolean.valueOf(IsComplete));
	}

	/** Get Complete.
		@return It is complete
	  */
	public boolean isComplete()
	{
		Object oo = get_Value(COLUMNNAME_IsComplete);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set All Day.
		@param IsEndDateAllDayJP All Day
	*/
	public void setIsEndDateAllDayJP (boolean IsEndDateAllDayJP)
	{
		set_Value (COLUMNNAME_IsEndDateAllDayJP, Boolean.valueOf(IsEndDateAllDayJP));
	}

	/** Get All Day.
		@return All Day	  */
	public boolean isEndDateAllDayJP()
	{
		Object oo = get_Value(COLUMNNAME_IsEndDateAllDayJP);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Open ToDo.
		@param IsOpenToDoJP Open ToDo
	*/
	public void setIsOpenToDoJP (boolean IsOpenToDoJP)
	{
		set_Value (COLUMNNAME_IsOpenToDoJP, Boolean.valueOf(IsOpenToDoJP));
	}

	/** Get Open ToDo.
		@return Open ToDo	  */
	public boolean isOpenToDoJP()
	{
		Object oo = get_Value(COLUMNNAME_IsOpenToDoJP);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set All Day.
		@param IsStartDateAllDayJP All Day
	*/
	public void setIsStartDateAllDayJP (boolean IsStartDateAllDayJP)
	{
		set_Value (COLUMNNAME_IsStartDateAllDayJP, Boolean.valueOf(IsStartDateAllDayJP));
	}

	/** Get All Day.
		@return All Day	  */
	public boolean isStartDateAllDayJP()
	{
		Object oo = get_Value(COLUMNNAME_IsStartDateAllDayJP);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param JP_Processing1 Process Now
	*/
	public void setJP_Processing1 (String JP_Processing1)
	{
		set_Value (COLUMNNAME_JP_Processing1, JP_Processing1);
	}

	/** Get Process Now.
		@return Process Now	  */
	public String getJP_Processing1()
	{
		return (String)get_Value(COLUMNNAME_JP_Processing1);
	}

	/** A = A */
	public static final String JP_STATISTICS_CHOICE_A = "A";
	/** B = B */
	public static final String JP_STATISTICS_CHOICE_B = "B";
	/** C = C */
	public static final String JP_STATISTICS_CHOICE_C = "C";
	/** D = D */
	public static final String JP_STATISTICS_CHOICE_D = "D";
	/** E = E */
	public static final String JP_STATISTICS_CHOICE_E = "E";
	/** Set Choice.
		@param JP_Statistics_Choice Choice
	*/
	public void setJP_Statistics_Choice (String JP_Statistics_Choice)
	{

		set_Value (COLUMNNAME_JP_Statistics_Choice, JP_Statistics_Choice);
	}

	/** Get Choice.
		@return Choice	  */
	public String getJP_Statistics_Choice()
	{
		return (String)get_Value(COLUMNNAME_JP_Statistics_Choice);
	}

	/** Set Date and Time.
		@param JP_Statistics_DateAndTime Date and Time
	*/
	public void setJP_Statistics_DateAndTime (Timestamp JP_Statistics_DateAndTime)
	{
		set_Value (COLUMNNAME_JP_Statistics_DateAndTime, JP_Statistics_DateAndTime);
	}

	/** Get Date and Time.
		@return Date and Time	  */
	public Timestamp getJP_Statistics_DateAndTime()
	{
		return (Timestamp)get_Value(COLUMNNAME_JP_Statistics_DateAndTime);
	}

	/** Set Number.
		@param JP_Statistics_Number Number
	*/
	public void setJP_Statistics_Number (BigDecimal JP_Statistics_Number)
	{
		set_Value (COLUMNNAME_JP_Statistics_Number, JP_Statistics_Number);
	}

	/** Get Number.
		@return Number	  */
	public BigDecimal getJP_Statistics_Number()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_JP_Statistics_Number);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** JP_Statistics_YesNo AD_Reference_ID=319 */
	public static final int JP_STATISTICS_YESNO_AD_Reference_ID=319;
	/** No = N */
	public static final String JP_STATISTICS_YESNO_No = "N";
	/** Yes = Y */
	public static final String JP_STATISTICS_YESNO_Yes = "Y";
	/** Set Yes / No.
		@param JP_Statistics_YesNo Yes / No
	*/
	public void setJP_Statistics_YesNo (String JP_Statistics_YesNo)
	{

		set_Value (COLUMNNAME_JP_Statistics_YesNo, JP_Statistics_YesNo);
	}

	/** Get Yes / No.
		@return Yes / No	  */
	public String getJP_Statistics_YesNo()
	{
		return (String)get_Value(COLUMNNAME_JP_Statistics_YesNo);
	}

	public I_JP_ToDo_Category getJP_ToDo_Category() throws RuntimeException
	{
		return (I_JP_ToDo_Category)MTable.get(getCtx(), I_JP_ToDo_Category.Table_ID)
			.getPO(getJP_ToDo_Category_ID(), get_TrxName());
	}

	/** Set ToDo Category.
		@param JP_ToDo_Category_ID ToDo Category
	*/
	public void setJP_ToDo_Category_ID (int JP_ToDo_Category_ID)
	{
		if (JP_ToDo_Category_ID < 1)
			set_Value (COLUMNNAME_JP_ToDo_Category_ID, null);
		else
			set_Value (COLUMNNAME_JP_ToDo_Category_ID, Integer.valueOf(JP_ToDo_Category_ID));
	}

	/** Get ToDo Category.
		@return ToDo Category	  */
	public int getJP_ToDo_Category_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_JP_ToDo_Category_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set End Time.
		@param JP_ToDo_EndTime End Time
	*/
	public void setJP_ToDo_EndTime (Timestamp JP_ToDo_EndTime)
	{
		set_Value (COLUMNNAME_JP_ToDo_EndTime, JP_ToDo_EndTime);
	}

	/** Get End Time.
		@return End Time	  */
	public Timestamp getJP_ToDo_EndTime()
	{
		return (Timestamp)get_Value(COLUMNNAME_JP_ToDo_EndTime);
	}

	/** Set Personal ToDo.
		@param JP_ToDo_ID Personal ToDo
	*/
	public void setJP_ToDo_ID (int JP_ToDo_ID)
	{
		if (JP_ToDo_ID < 1)
			set_ValueNoCheck (COLUMNNAME_JP_ToDo_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_JP_ToDo_ID, Integer.valueOf(JP_ToDo_ID));
	}

	/** Get Personal ToDo.
		@return Personal ToDo	  */
	public int getJP_ToDo_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_JP_ToDo_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_JP_ToDo getJP_ToDo_Related() throws RuntimeException
	{
		return (I_JP_ToDo)MTable.get(getCtx(), I_JP_ToDo.Table_ID)
			.getPO(getJP_ToDo_Related_ID(), get_TrxName());
	}

	/** Set Related ToDo.
		@param JP_ToDo_Related_ID Related ToDo
	*/
	public void setJP_ToDo_Related_ID (int JP_ToDo_Related_ID)
	{
		if (JP_ToDo_Related_ID < 1)
			set_Value (COLUMNNAME_JP_ToDo_Related_ID, null);
		else
			set_Value (COLUMNNAME_JP_ToDo_Related_ID, Integer.valueOf(JP_ToDo_Related_ID));
	}

	/** Get Related ToDo.
		@return Related ToDo	  */
	public int getJP_ToDo_Related_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_JP_ToDo_Related_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Scheduled End Date.
		@param JP_ToDo_ScheduledEndDate Scheduled End Date
	*/
	public void setJP_ToDo_ScheduledEndDate (Timestamp JP_ToDo_ScheduledEndDate)
	{
		set_Value (COLUMNNAME_JP_ToDo_ScheduledEndDate, JP_ToDo_ScheduledEndDate);
	}

	/** Get Scheduled End Date.
		@return Scheduled End Date	  */
	public Timestamp getJP_ToDo_ScheduledEndDate()
	{
		return (Timestamp)get_Value(COLUMNNAME_JP_ToDo_ScheduledEndDate);
	}

	/** Set Scheduled End Time.
		@param JP_ToDo_ScheduledEndTime Scheduled End Time
	*/
	public void setJP_ToDo_ScheduledEndTime (Timestamp JP_ToDo_ScheduledEndTime)
	{
		set_Value (COLUMNNAME_JP_ToDo_ScheduledEndTime, JP_ToDo_ScheduledEndTime);
	}

	/** Get Scheduled End Time.
		@return Scheduled End Time	  */
	public Timestamp getJP_ToDo_ScheduledEndTime()
	{
		return (Timestamp)get_Value(COLUMNNAME_JP_ToDo_ScheduledEndTime);
	}

	/** Set Scheduled Start Date.
		@param JP_ToDo_ScheduledStartDate Scheduled Start Date
	*/
	public void setJP_ToDo_ScheduledStartDate (Timestamp JP_ToDo_ScheduledStartDate)
	{
		set_Value (COLUMNNAME_JP_ToDo_ScheduledStartDate, JP_ToDo_ScheduledStartDate);
	}

	/** Get Scheduled Start Date.
		@return Scheduled Start Date	  */
	public Timestamp getJP_ToDo_ScheduledStartDate()
	{
		return (Timestamp)get_Value(COLUMNNAME_JP_ToDo_ScheduledStartDate);
	}

	/** Set Scheduled Start Time.
		@param JP_ToDo_ScheduledStartTime Scheduled Start Time
	*/
	public void setJP_ToDo_ScheduledStartTime (Timestamp JP_ToDo_ScheduledStartTime)
	{
		set_Value (COLUMNNAME_JP_ToDo_ScheduledStartTime, JP_ToDo_ScheduledStartTime);
	}

	/** Get Scheduled Start Time.
		@return Scheduled Start Time	  */
	public Timestamp getJP_ToDo_ScheduledStartTime()
	{
		return (Timestamp)get_Value(COLUMNNAME_JP_ToDo_ScheduledStartTime);
	}

	/** Set Start Time.
		@param JP_ToDo_StartTime Start Time
	*/
	public void setJP_ToDo_StartTime (Timestamp JP_ToDo_StartTime)
	{
		set_Value (COLUMNNAME_JP_ToDo_StartTime, JP_ToDo_StartTime);
	}

	/** Get Start Time.
		@return Start Time	  */
	public Timestamp getJP_ToDo_StartTime()
	{
		return (Timestamp)get_Value(COLUMNNAME_JP_ToDo_StartTime);
	}

	/** Completed = CO */
	public static final String JP_TODO_STATUS_Completed = "CO";
	/** Not yet started = NY */
	public static final String JP_TODO_STATUS_NotYetStarted = "NY";
	/** Work in progress = WP */
	public static final String JP_TODO_STATUS_WorkInProgress = "WP";
	/** Set ToDo Status.
		@param JP_ToDo_Status ToDo Status
	*/
	public void setJP_ToDo_Status (String JP_ToDo_Status)
	{

		set_Value (COLUMNNAME_JP_ToDo_Status, JP_ToDo_Status);
	}

	/** Get ToDo Status.
		@return ToDo Status	  */
	public String getJP_ToDo_Status()
	{
		return (String)get_Value(COLUMNNAME_JP_ToDo_Status);
	}

	public I_JP_ToDo_Team getJP_ToDo_Team() throws RuntimeException
	{
		return (I_JP_ToDo_Team)MTable.get(getCtx(), I_JP_ToDo_Team.Table_ID)
			.getPO(getJP_ToDo_Team_ID(), get_TrxName());
	}

	/** Set Team ToDo.
		@param JP_ToDo_Team_ID Team ToDo
	*/
	public void setJP_ToDo_Team_ID (int JP_ToDo_Team_ID)
	{
		if (JP_ToDo_Team_ID < 1)
			set_ValueNoCheck (COLUMNNAME_JP_ToDo_Team_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_JP_ToDo_Team_ID, Integer.valueOf(JP_ToDo_Team_ID));
	}

	/** Get Team ToDo.
		@return Team ToDo	  */
	public int getJP_ToDo_Team_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_JP_ToDo_Team_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Memo = M */
	public static final String JP_TODO_TYPE_Memo = "M";
	/** Schedule = S */
	public static final String JP_TODO_TYPE_Schedule = "S";
	/** Task = T */
	public static final String JP_TODO_TYPE_Task = "T";
	/** Set ToDo Type.
		@param JP_ToDo_Type ToDo Type
	*/
	public void setJP_ToDo_Type (String JP_ToDo_Type)
	{

		set_Value (COLUMNNAME_JP_ToDo_Type, JP_ToDo_Type);
	}

	/** Get ToDo Type.
		@return ToDo Type	  */
	public String getJP_ToDo_Type()
	{
		return (String)get_Value(COLUMNNAME_JP_ToDo_Type);
	}

	/** Set JP_ToDo_UU.
		@param JP_ToDo_UU JP_ToDo_UU
	*/
	public void setJP_ToDo_UU (String JP_ToDo_UU)
	{
		set_Value (COLUMNNAME_JP_ToDo_UU, JP_ToDo_UU);
	}

	/** Get JP_ToDo_UU.
		@return JP_ToDo_UU	  */
	public String getJP_ToDo_UU()
	{
		return (String)get_Value(COLUMNNAME_JP_ToDo_UU);
	}

	public org.compiere.model.I_AD_User getLIT_Ad_User_Lead() throws RuntimeException
	{
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_ID)
			.getPO(getLIT_Ad_User_Lead_ID(), get_TrxName());
	}

	/** Set Lead ID.
		@param LIT_Ad_User_Lead_ID Lead ID
	*/
	public void setLIT_Ad_User_Lead_ID (int LIT_Ad_User_Lead_ID)
	{
		if (LIT_Ad_User_Lead_ID < 1)
			set_Value (COLUMNNAME_LIT_Ad_User_Lead_ID, null);
		else
			set_Value (COLUMNNAME_LIT_Ad_User_Lead_ID, Integer.valueOf(LIT_Ad_User_Lead_ID));
	}

	/** Get Lead ID.
		@return Lead ID	  */
	public int getLIT_Ad_User_Lead_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LIT_Ad_User_Lead_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ERP Object Task.
		@param LIT_ERPObjectTask_ID ERP Object Task
	*/
	public void setLIT_ERPObjectTask_ID (int LIT_ERPObjectTask_ID)
	{
		if (LIT_ERPObjectTask_ID < 1)
			set_Value (COLUMNNAME_LIT_ERPObjectTask_ID, null);
		else
			set_Value (COLUMNNAME_LIT_ERPObjectTask_ID, Integer.valueOf(LIT_ERPObjectTask_ID));
	}

	/** Get ERP Object Task.
		@return ERP Object Task	  */
	public int getLIT_ERPObjectTask_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LIT_ERPObjectTask_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Name.
		@param Name Alphanumeric identifier of the entity
	*/
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName()
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** Set Percent.
		@param Percent Percentage
	*/
	public void setPercent (BigDecimal Percent)
	{
		set_Value (COLUMNNAME_Percent, Percent);
	}

	/** Get Percent.
		@return Percentage
	  */
	public BigDecimal getPercent()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Percent);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Priority AD_Reference_ID=154 */
	public static final int PRIORITY_AD_Reference_ID=154;
	/** Urgent = 1 */
	public static final String PRIORITY_Urgent = "1";
	/** High = 3 */
	public static final String PRIORITY_High = "3";
	/** Medium = 5 */
	public static final String PRIORITY_Medium = "5";
	/** Low = 7 */
	public static final String PRIORITY_Low = "7";
	/** Minor = 9 */
	public static final String PRIORITY_Minor = "9";
	/** Set Priority.
		@param Priority Indicates if this request is of a high, medium or low priority.
	*/
	public void setPriority (String Priority)
	{

		set_Value (COLUMNNAME_Priority, Priority);
	}

	/** Get Priority.
		@return Indicates if this request is of a high, medium or low priority.
	  */
	public String getPriority()
	{
		return (String)get_Value(COLUMNNAME_Priority);
	}

	/** Set Processed.
		@param Processed The document has been processed
	*/
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed()
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public org.compiere.model.I_M_Product getProductTransfer() throws RuntimeException
	{
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_ID)
			.getPO(getProductTransfer_ID(), get_TrxName());
	}

	/** Set Product Code Transfer.
		@param ProductTransfer_ID Product Code Transfer
	*/
	public void setProductTransfer_ID (int ProductTransfer_ID)
	{
		if (ProductTransfer_ID < 1)
			set_Value (COLUMNNAME_ProductTransfer_ID, null);
		else
			set_Value (COLUMNNAME_ProductTransfer_ID, Integer.valueOf(ProductTransfer_ID));
	}

	/** Get Product Code Transfer.
		@return Product Code Transfer	  */
	public int getProductTransfer_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ProductTransfer_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Quantity.
		@param Qty Quantity
	*/
	public void setQty (BigDecimal Qty)
	{
		set_Value (COLUMNNAME_Qty, Qty);
	}

	/** Get Quantity.
		@return Quantity
	  */
	public BigDecimal getQty()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Qty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Record ID.
		@param Record_ID Direct internal record ID
	*/
	public void setRecord_ID (int Record_ID)
	{
		if (Record_ID < 0)
			set_Value (COLUMNNAME_Record_ID, null);
		else
			set_Value (COLUMNNAME_Record_ID, Integer.valueOf(Record_ID));
	}

	/** Get Record ID.
		@return Direct internal record ID
	  */
	public int getRecord_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Record_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_R_Request getR_Request() throws RuntimeException
	{
		return (org.compiere.model.I_R_Request)MTable.get(getCtx(), org.compiere.model.I_R_Request.Table_ID)
			.getPO(getR_Request_ID(), get_TrxName());
	}

	/** Set Request.
		@param R_Request_ID Request from a Business Partner or Prospect
	*/
	public void setR_Request_ID (int R_Request_ID)
	{
		if (R_Request_ID < 1)
			set_ValueNoCheck (COLUMNNAME_R_Request_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_R_Request_ID, Integer.valueOf(R_Request_ID));
	}

	/** Get Request.
		@return Request from a Business Partner or Prospect
	  */
	public int getR_Request_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_Request_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set URL.
		@param URL Full URL address - e.g. http://www.idempiere.org
	*/
	public void setURL (String URL)
	{
		set_Value (COLUMNNAME_URL, URL);
	}

	/** Get URL.
		@return Full URL address - e.g. http://www.idempiere.org
	  */
	public String getURL()
	{
		return (String)get_Value(COLUMNNAME_URL);
	}
}