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
 * This class define attributes group use to combine attributes
 * @author Hao Jiang
 */
public class GeneratorAttriGrp 
{
	//Attributes group name
	private String grpname;
	//List of attributes
	
	private List<GeneratorAttri> attrislist;
	private List<GeneratorAttriGrp> childattrislist;
	
	public String getAttriGrpName()
	{
		return this.grpname;
	}
	
	public List<GeneratorAttri> getAttrisList()
	{
		return this.attrislist;
	}
	
	public List<GeneratorAttriGrp> getChildAttriGrpList()
	{
		return this.childattrislist;
	}
	
	//add attribute into group
	public void addAttri(GeneratorAttri attri)
	{
		if (attri != null && this.attrislist != null)
		{
			if (Contains(attri) == -1)
				this.attrislist.add(attri);
		}
	}
	
	public void addAttriGrp(GeneratorAttriGrp attrisgrp)
	{
		if (attrisgrp != null && this.childattrislist != null)
			this.childattrislist.add(attrisgrp);
	}
	//get the attribute from group
	public GeneratorAttri getAttri(String attriname)
	{
		GeneratorAttri attri;
		String name;
		int i = 0;
		while (i < this.attrislist.size())
		{
			attri = this.attrislist.get(i);
			name = attri.getAttriName();
			i++;
			if (name.equals(attriname))
				return attri;
		}
		
		return null;
	}
	
	public GeneratorAttri getAttri(int index)
	{
		
		if (index >=0 && index <=this.attrislist.size() - 1)
			return this.attrislist.get(index);
				
		return null;
	}
	
	//init function
	public GeneratorAttriGrp(String name)
	{
		this.grpname = name;
		this.attrislist = new ArrayList<GeneratorAttri>();
		this.childattrislist = new ArrayList<GeneratorAttriGrp>();
	}
	
	public GeneratorAttriGrp(GeneratorAttriGrp grp)
	{
		this.grpname = grp.getAttriGrpName();
		
		List<GeneratorAttri> list = grp.getAttrisList();
		List<GeneratorAttriGrp> childgrplist = grp.getChildAttriGrpList();
		
		this.attrislist = new ArrayList<GeneratorAttri>(list);
		this.childattrislist = new ArrayList<GeneratorAttriGrp>(childgrplist);
	}
	
	//validate the value of each attributes 
	public boolean Validate()
	{
		GeneratorAttri attri;
		GeneratorAttriGrp grp;
		int i;
		for (i = 0; i < this.attrislist.size(); i++)
		{
			attri = this.attrislist.get(i);
			if (!attri.Validate())
			{
				return false;
			}
		}
		
		for (i = 0; i < this.childattrislist.size(); i++)
		{
			grp = this.childattrislist.get(i);
			if (!grp.Validate())
				return false;
		}
		
		return true;
	}
	
	//check if the attributes group contains a certain attribute
	//return the index of the attribute or -1
	public int Contains(String attriname)
	{
		int index = -1;
		GeneratorAttri attri;
		for (int i = 0; i < this.attrislist.size(); i++)
		{
			attri = (GeneratorAttri)this.attrislist.get(i);
			if (attri.getAttriName().equals(attriname))
			{
				index = i;
				return index;
			}
		}
		return index;
	}
	
	public int Contains(GeneratorAttri attri)
	{
		int index = -1;
		GeneratorAttri tmpattri;
		for (int i = 0; i < this.attrislist.size(); i++)
		{
			tmpattri = (GeneratorAttri)this.attrislist.get(i);
			if (tmpattri.equals(attri))
			{
				index = i;
				return index;
			}
		}
		return index;
	}
	
	public void Clear()
	{
		this.attrislist.clear();
		this.childattrislist.clear();
	}
}
