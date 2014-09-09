package slingshot.wizards.topology;

/*
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */

import java.util.ArrayList;
import java.util.List;


/**
 * This class define the constraint of topology generate input attributes.
 * The purpose of this class is to verify user input based on the constraint 
 * @author Hao Jiang
 */
public class AttriConstraint 
{
	/** The type of constraint enum = 1,inequation = 2 undefined = 0 **/
	private int type = 0;
	/** List of enum in constraint **/
	private List<EnumType> enumlist;
	/** Inequation type and = 1, or = 2, undefined = 0 **/
	private int inequtype = 0;
	/** List of in equation in constraint **/
	private List<String> inequlist;
	
	public int getType()
	{
		return this.type;
	}
	
	public List<EnumType> getEnumList()
	{
		return this.enumlist;
	}
	
	public AttriConstraint(String constraint)
	{		
		String tmpstr;
		String [] tmpstrarray1 = null;
		String [] tmpstrarray2 = null;
		
		this.enumlist = new ArrayList<EnumType>();
		this.inequlist = new ArrayList<String>();

		if (constraint.startsWith("Enum"))
			type = 1;
		else if (constraint.startsWith("Inequ"))
			type = 2;
		else
			type = 3;
			
		if (type == 1)
		{	
			String name;
			String value;
			tmpstr = constraint.substring(5);
			tmpstrarray1 = tmpstr.split(";");
			
			for (int i = 0; i < tmpstrarray1.length; i++)
			{
				tmpstr = tmpstrarray1[i];
				tmpstrarray2 = tmpstr.split("=");
				name = tmpstrarray2[0];
				value = tmpstrarray2[1];
				
				EnumType tmpe = new EnumType(name, Integer.parseInt(value));
				this.enumlist.add(tmpe);
			}
		}
		
		if (type == 2)
		{
			tmpstr = constraint.substring(6);
			//single relation
			if (!tmpstr.contains(",") && !tmpstr.contains(";"))
			{
				this.inequtype = 0;
				this.inequlist.add(tmpstr);
			}
			//and relation between each inequation
			if (tmpstr.contains(","))
			{
				tmpstrarray1 = tmpstr.split(",");
				this.inequtype = 1;
			}
			//or relation between each inequation
			if (tmpstr.contains(";"))
			{
				tmpstrarray1 = tmpstr.split(";");
				this.inequtype = 2;
			}
			if (tmpstrarray1 != null)
			{
				for (int i = 0; i < tmpstrarray1.length; i++)
				{
					this.inequlist.add(tmpstrarray1[i]);
				}
			}
		}
	}
	
	public boolean Valiate(String value)
	{
		int i;
		
		if (this.type == 1)
		{
			for (i = 0; i < this.enumlist.size(); i++)
			{
				if (ValidateEnum(value, this.enumlist.get(i)))
					return true;
			}
			
			return false;
		}
		
		if (this.type == 2)
		{
			if (this.inequtype == 0)
				return ValidateInequ(value, this.inequlist.get(0));
			if (this.inequtype == 1)
			{
				for (i = 0; i < this.inequlist.size(); i++)
				{
					if (!ValidateInequ(value, this.inequlist.get(i)))
						return false;	
				}
				return true;
			}
			else if (this.inequtype == 2)
			{
				for (i = 0; i < this.inequlist.size(); i++)
				{
					if (ValidateInequ(value, this.inequlist.get(i)))
						return true;	
				}
				return false;
			}
		}
		
		return true;
	}
	
	private boolean ValidateEnum(String value, EnumType e)
	{
		String name;
		String v;
		
		name = e.getEnumName();
		v = String.valueOf(e.getEnumValue());
		
		if (value.equals(name) || value.equals(v))
			return true;
		else
	        return false;
	}
	
	//We now just consider inequation in the form of Type(G/GE/L/LE)Value
	private boolean ValidateInequ(String value, String inequation)
	{
		String str1, str2;
		String [] strarray = null;
		int relation = 0;
		
		if (inequation.contains("(G)"))
		{
			strarray = inequation.split("(G)");
			relation = 1;
		}
		else if (inequation.contains("(GE)"))
		{
			strarray = inequation.split("(GE)");
			relation = 2;
		}
		else if (inequation.contains("(L)"))
		{
			strarray = inequation.split("(L)");
			relation = 3;
		}
		else if (inequation.contains("(LE)"))
		{
			strarray = inequation.split("(LE)");
			relation = 4;
		}
		else if (inequation.contains("(=)"))	
		{
			strarray = inequation.split("(=)");
			relation = 5;
		}
		
		str1 = strarray[0];
		str1 = str1.substring(0, str1.length() - 1);
		str2 = strarray[1];
		str2 = str2.substring(1);
		
		float fvalue, fconstraint;
		int ivalue, iconstraint;
		
		try 
		{
			if (str1.equals("Float"))
			{
				fvalue = Float.parseFloat(value);
				fconstraint = Float.parseFloat(str2);
				

				switch (relation)
				{
				case 1:
					if (fvalue > fconstraint)
						return true;
					break;
				case 2:
					if (fvalue >= fconstraint)
						return true;
					break;
				case 3:
					if (fvalue < fconstraint)
						return true;
					break;
				case 4:
					if (fvalue <= fconstraint)
						return true;
					break;
				case 5:
					if (fvalue == fconstraint)
						return true;
					break;
				}
			}
			else if (str1.equals("Integer"))
			{
				ivalue = Integer.parseInt(value);
				iconstraint = Integer.parseInt(str2);
				
				switch (relation)
				{
				case 1:
					if (ivalue > iconstraint)
						return true;
					break;
				case 2:
					if (ivalue >= iconstraint)
						return true;
					break;
				case 3:
					if (ivalue < iconstraint)
						return true;
					break;
				case 4:
					if (ivalue <= iconstraint)
						return true;
					break;
				case 5:
					if (ivalue == iconstraint)
						return true;
					break;
				}
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
			return false;
		}
		
		return false;
	}
}