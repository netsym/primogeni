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

import org.w3c.dom.Element;

/**
 * This class define topology generator input attribute.
 * @author Hao Jiang
 */
public class GeneratorAttri 
{
	public static final String INT = "int";
	public static final String FLOAT = "float";

	//attribute name
	private String name;
	//attribute type
	private String type;
	//the constraint of the attribute value
	private String constraint;
	//description of the attribute
	private String description;
	//default value
	private String defaultvalue;
	//attribute value
	private String value;

	public void setAttriName(String name)
	{
		this.name = name;
	}
	
	public String getAttriName()
	{
		return this.name;
	}
	
	public void setAttriType(String type)
	{
		this.type = type;
	}
	
	public String getAttriType()
	{
		return this.type;
	}
		
	public void setAttriDescri(String descri)
	{
		this.description = descri;
	}
	
	public String getAttriDescri()
	{
		return this.description;
	}
		
	public String getAttriConstraint()
	{
		return this.constraint;
	}
	
	public void setAttriConstraint(String constraint)
	{
		this.constraint = constraint;
	}
	
	public void setDefaultValue(String value)
	{
		this.defaultvalue = value;
	}
	
	public String getDefaultValue()
	{
		return this.defaultvalue;
	}
	
	public void setAttriValue(String value)
	{
		this.value = value;
	}
	
	public String getAttriValue()
	{
		return this.value;
	}
	
	//init functions
	public GeneratorAttri(String name,
						  String type, 
						  String constraint, String description,
						  String defaultvalue, String value)
	{
		this.name = name;
		this.type = type;
		this.constraint = constraint;
		this.description = description;
		this.defaultvalue = defaultvalue;
		this.value = value;
	}
	
	public GeneratorAttri()//String genername, String grpname)
	{		
		this.name = "";
		this.type = "";
		this.constraint = "";
		this.description = "";
		this.defaultvalue = "";
		this.value = "";
	}
	
	public GeneratorAttri(GeneratorAttri attri)
	{
		//this.generator_name = attri.getGenerName();
		this.name = attri.getAttriName();
		//this.groupname = attri.getAttriGrpName();
		this.type = attri.getAttriType();
		this.constraint = attri.getAttriConstraint();
		this.description = attri.getAttriDescri();
		this.defaultvalue = attri.getDefaultValue();
		this.value = attri.getAttriValue();
	}
	
	public GeneratorAttri(//String genername, String grpname, 
			              Element elem)
	{
		this.name = elem.getAttribute("name");
		this.type = elem.getAttribute("type");
		this.constraint = elem.getAttribute("constraint");
		this.description = elem.getAttribute("description");
		this.defaultvalue = elem.getAttribute("defaultvalue");
		if (elem.getFirstChild() != null)
			this.value = elem.getFirstChild().getNodeValue();
		else
			this.value = "";
	}
	
	//check if the two attribute are same(same group name, attribute name, generator name)
	public boolean Equal(GeneratorAttri attri)
	{
		if (attri != null)
		{
			if (//this.generator_name.equals(attri.getGenerName()) &&
				//this.groupname.equals(attri.getAttriGrpName()) &&
				this.name.equals(attri.getAttriName()))
				return true;
		}
		return false;
	}
		
	//validate the attribute value based on constraint
	public boolean Validate()
	{
		AttriConstraint constraint = new AttriConstraint(this.constraint);
		return constraint.Valiate(this.value);
	}
}
