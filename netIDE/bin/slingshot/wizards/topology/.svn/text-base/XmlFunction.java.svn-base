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

import java.io.File;

import java.util.List;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

/**
 * This is class to handle xml operation.
 * @author Hao Jiang
 */
public class XmlFunction 
{	
	final public static String GENERATORS = "Generators";
	final public static String GENERATOR = "Generator";
	final public static String GENERATORPATH = "Generator_Path";
	final public static String INPUTFILE = "Input_File";
	final public static String IMPORTFILE = "Import_File";
	final public static String EXPORTFILE = "Export_File";
	final public static String ATTRIBUTES = "Attributes";
	final public static String ATTRIBUTESGRP = "Attributes_Group";
	final public static String ATTRIBUTE = "Attribute";
	//final public static String EXPORT = "Export";
	//final public static String IMPORT = "Import";
	final public static String CONSTRAINT = "constraint";
	final public static String NAME = "name";	
	final public static String DESCRIPTION = "description";
	final public static String TYPE = "type";
	//Write document into file
	public static boolean doc2XmlFile(Document doc, String filename) 
	{ 
		boolean flag = true; 
	    try 
	    { 
	    	//write the document into file 
	        TransformerFactory tFactory = TransformerFactory.newInstance();    
	        Transformer transformer = tFactory.newTransformer();  
	        
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        
	        DOMSource source = new DOMSource(doc);  
	        StreamResult result = new StreamResult(new File(filename));    
	        transformer.transform(source, result);  
	     }
	     catch(Exception e) 
	     {
	    	 flag = false; 
	         e.printStackTrace(); 
	     } 
	     return flag;       
	}

	
	//Update xml file
	//modify Generator name
	public static void UpdateGeneratorName(String profile, 
			                               String oldgenername, String newgenername)
	{
		File file = new File(profile);
		if (file.exists())
		{
			try
			{
				// Create a factory
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        // Use the factory to create a builder
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(file);
		        doc.normalize();
		        	        
		        NodeList nodelist = doc.getElementsByTagName(GENERATOR);
		        if (nodelist.getLength() != 0)
		        {
		        	Element elem;
		        	for (int i = 0; i < nodelist.getLength(); i++)
		        	{
		        		elem = (Element)nodelist.item(i);
		        		//find the generator
		        		if (elem.getAttribute("name").equals(oldgenername))
		        		{
		        			elem.setAttribute("name", newgenername);
		        			doc2XmlFile(doc, profile);
		        			break;
		        		}
		        	}
		        }
		        
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				e.getStackTrace();
			}
		}
		 
	}
	//modify generator path
	public static void UpdateGeneratorPath(String profile, String genername,
			                               String generpath)
	{
		File file = new File(profile);
		if (file.exists())
		{
			try
			{
				// Create a factory
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        // Use the factory to create a builder
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(file);
		        doc.normalize();
		        
		        NodeList nodelist = doc.getElementsByTagName(GENERATOR);
		        if (nodelist.getLength() != 0)
		        {
		        	Element elem;
		        	for (int i = 0; i < nodelist.getLength(); i++)
		        	{
		        		elem = (Element)nodelist.item(i);
		        		//find the generator
		        		if (elem.getAttribute("name").equals(genername))
		        		{
		        			//find Generator path node
		        			nodelist = elem.getElementsByTagName(GENERATORPATH);
		        			if (nodelist.getLength() == 1)
		        			{
		        				elem = (Element)nodelist.item(0);
		        				if (elem.getFirstChild() != null)
		        					elem.getFirstChild().setNodeValue(generpath);
		        				doc2XmlFile(doc, profile);
		        			}
		        		}	
		        	}
		        }
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				e.getStackTrace();
			}
		}
	}
		
	//modify import file path
	public static void UpdateImportFile(String profile, String genername, String importfile)
	{
		File file = new File(profile);
		if (file.exists())
		{
			try
			{
				// Create a factory
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        // Use the factory to create a builder
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(file);
		        doc.normalize();
		        
		        NodeList nodelist = doc.getElementsByTagName(GENERATOR);
		        if (nodelist.getLength() != 0)
		        {
		        	Element elem;
		        	for (int i = 0; i < nodelist.getLength(); i++)
		        	{
		        		elem = (Element)nodelist.item(i);
		        		//find the generator
		        		if (elem.getAttribute("name").equals(genername))
		        		{
		        			//find import file node
		        			nodelist = elem.getElementsByTagName(IMPORTFILE);
		        			if (nodelist.getLength() == 1)
		        			{
		        				elem = (Element)nodelist.item(0);
		        				if (elem.getFirstChild() != null)
		        					elem.getFirstChild().setNodeValue(importfile);
		        				doc2XmlFile(doc, profile);
		        			}
		        		}	
		        	}
		        }
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				e.getStackTrace();
			}
		}
	}
	
	//modify export file path
	public static void UpdateExportFile(String profile, String genername, String exportfile)
	{
		File file = new File(profile);
		if (file.exists())
		{
			try
			{
				// Create a factory
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        // Use the factory to create a builder
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(file);
		        doc.normalize();
		        
		        NodeList nodelist = doc.getElementsByTagName(GENERATOR);
		        if (nodelist.getLength() != 0)
		        {
		        	Element elem;
		        	for (int i = 0; i < nodelist.getLength(); i++)
		        	{
		        		elem = (Element)nodelist.item(i);
		        		//find the generator
		        		if (elem.getAttribute("name").equals(genername))
		        		{
		        			//find export file node
		        			nodelist = elem.getElementsByTagName(EXPORTFILE);
		        			if (nodelist.getLength() == 1)
		        			{
		        				elem = (Element)nodelist.item(0);
		        				if (elem.getFirstChild() != null)
		        					elem.getFirstChild().setNodeValue(exportfile);
		        				doc2XmlFile(doc, profile);
		        			}
		        		}	
		        	}
		        }
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				e.getStackTrace();
			}
		}
	}
	
	//modify attributes group name
	/*
	public static void UpateAttriGrpName(String profile, String genername, 
										 String oldgrpname, String newgrpname)
	{
		File file = new File(profile);
		if (file.exists())
		{
			try
			{
				// Create a factory
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        // Use the factory to create a builder
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(file);
		        doc.normalize();
		        
		        int i;
		        NodeList nodelist = doc.getElementsByTagName(GENERATOR);
		        if (nodelist.getLength() != 0)
		        {
		        	Element elem;
		        	for (i = 0; i < nodelist.getLength(); i++)
		        	{
		        		elem = (Element)nodelist.item(i);
		        		//find the generator
		        		if (elem.getAttribute("name").equals(genername))
		        		{
		        			nodelist = elem.getElementsByTagName(ATTRIBUTESGRP);
		        			if (nodelist.getLength() != 0)
		        			{
		        				for (i = 0; i < nodelist.getLength(); i++)
		        				{
		        					elem = (Element)nodelist.item(i);
		        					//find the attributes group
		        					if (elem.getAttribute("name").equals(oldgrpname))
		        					{
		        						elem.setAttribute("name", newgrpname);
		        						doc2XmlFile(doc, profile);
		        						break;
		        					}
		        				}
		        			}
		        		}	
		        	}
		        }
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				e.getStackTrace();
			}
		}
	}
	
	//modify attribute value
	public static void UpdateAttriValue(String profile, String genername, String grpname, 
										String attriname, String value)
	{
		File file = new File(profile);
		if (file.exists())
		{
			try
			{
				// Create a factory
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        // Use the factory to create a builder
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(file);
		        doc.normalize();
		        
		        int i, j;
		        NodeList nodelist = doc.getElementsByTagName(GENERATOR);
		        if (nodelist.getLength() != 0)
		        {
		        	Element elem;
		        	for (i = 0; i < nodelist.getLength(); i++)
		        	{
		        		elem = (Element)nodelist.item(i);
		        		//find the generator
		        		if (elem.getAttribute("name").equals(genername))
		        		{
		        			nodelist = elem.getElementsByTagName(ATTRIBUTESGRP);
		        			if (nodelist.getLength() != 0)
		        			{
		        				for (i = 0; i < nodelist.getLength(); i++)
		        				{
		        					elem = (Element)nodelist.item(i);
		        					//find the attributes group
		        					if (elem.getAttribute("name").equals(grpname))
		        					{
		        						
		        						nodelist = elem.getElementsByTagName(ATTRIBUTE);
		        						for (j = 0; j < nodelist.getLength(); j++)
		        						{
		        							elem = (Element)nodelist.item(j);
			        						if (elem.getAttribute("name").equals(attriname))
			        						{
			        							if (elem.getFirstChild() != null)
			        								elem.getFirstChild().setNodeValue(value);
			        							else
			        							{
			        								Text textseg = doc.createTextNode(value);
			        				        		elem.appendChild(textseg);
			        							}
			        							doc2XmlFile(doc, profile);
			        						}
		        						}
		        					}
		        				}
		        			}
		        		}	
		        	}
		        }
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				e.getStackTrace();
			}
		}
	}
	
	//modify Generator Attribute's xml attribute
	public static void UpdateGenerAttriXMLAttri(String profile, String genername, String grpname,
												String generattriname, String xmlattriname, 
												String xmlattrivalue)
	{
		File file = new File(profile);
		if (file.exists())
		{
			try
			{
				// Create a factory
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        // Use the factory to create a builder
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(file);
		        doc.normalize();
		        
		        int i, j;
		        NodeList nodelist = doc.getElementsByTagName(GENERATOR);
		        if (nodelist.getLength() != 0)
		        {
		        	Element elem;
		        	for (i = 0; i < nodelist.getLength(); i++)
		        	{
		        		elem = (Element)nodelist.item(i);
		        		//find the generator
		        		if (elem.getAttribute("name").equals(genername))
		        		{
		        			nodelist = elem.getElementsByTagName(ATTRIBUTESGRP);

	        				for (i = 0; i < nodelist.getLength(); i++)
	        				{
	        					elem = (Element)nodelist.item(i);
	        					//find the attributes group
	        					if (elem.getAttribute("name").equals(grpname))
	        					{
	        						nodelist = elem.getElementsByTagName(ATTRIBUTE);
	        						
	        						for (j = 0; j < nodelist.getLength(); j++)
	        						{
	        							elem = (Element)nodelist.item(j);
	        							//find the attribute
	        							if (elem.getAttribute("name").equals(generattriname))
		        						{
		        							elem.setAttribute(xmlattriname, xmlattrivalue);
		        							doc2XmlFile(doc, profile);
		        						}
	        						}
	        						
	        					}
	        				}	        			
		        		}	
		        	}
		        }
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				e.getStackTrace();
			}
		}
	}
	*/
	
	//Delete
	/*
	//delete Generator
	public static void DelGenerator(String profile, String genername)
	{
		File file = new File(profile);
		if (file.exists())
		{
			try
			{
				// Create a factory
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        // Use the factory to create a builder
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(file);
		        doc.normalize();
		        
		        int i, j;
		        NodeList nodelist = doc.getElementsByTagName(GENERATOR);
		        if (nodelist.getLength() != 0)
		        {
		        	Element elem;
		        	for (i = 0; i < nodelist.getLength(); i++)
		        	{
		        		elem = (Element)nodelist.item(i);
		        		//find the generator
		        		if (elem.getAttribute("name").equals(genername))
		        		{
		        			NodeList tmplist = elem.getChildNodes();
		        			for (j = 0; j < tmplist.getLength(); j++)
		        			{
		        				elem.removeChild(tmplist.item(j));
		        			}
		        			elem.getParentNode().removeChild(elem);
		        			doc2XmlFile(doc, profile);
		        			break;
		        		}	
		        	}
		        }
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				e.getStackTrace();
			}
		}
	}
	
	//delete attributes group
	public static void DelAttriGrp(String profile, String genername, String grpname)
	{
		File file = new File(profile);
		if (file.exists())
		{
			try
			{
				// Create a factory
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        // Use the factory to create a builder
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(file);
		        doc.normalize();
		        
		        int i, j;
		        NodeList nodelist = doc.getElementsByTagName(GENERATOR);
		        if (nodelist.getLength() != 0)
		        {
		        	Element elem;
		        	for (i = 0; i < nodelist.getLength(); i++)
		        	{
		        		elem = (Element)nodelist.item(i);
		        		//find the generator
		        		if (elem.getAttribute("name").equals(genername))
		        		{
		        			nodelist = elem.getElementsByTagName(ATTRIBUTESGRP);
		        			if (nodelist.getLength() != 0)
		        			{
		        				for (i = 0; i < nodelist.getLength(); i++)
		        				{
		        					elem = (Element)nodelist.item(i);
		        					//find the attributes group
		        					if (elem.getAttribute("name").equals(grpname))
		        					{
		        						NodeList tmplist = elem.getChildNodes();
		        						for (j = 0; j < tmplist.getLength(); j++)
		        							elem.removeChild(tmplist.item(j));
		        						elem.getParentNode().removeChild(elem);
		        						doc2XmlFile(doc, profile);
		        						break;
		        					}
		        				}
		        			}
		        		}	
		        	}
		        }
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				e.getStackTrace();
			}
		}
	}
	//delete attribute
	public static void DelAttri(String profile, String genername, String grpname, String attriname)
	{
		File file = new File(profile);
		if (file.exists())
		{
			try
			{
				// Create a factory
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        // Use the factory to create a builder
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(file);
		        doc.normalize();
		        
		        int i, j;
		        NodeList nodelist = doc.getElementsByTagName(GENERATOR);
		        if (nodelist.getLength() != 0)
		        {
		        	Element elem;
		        	for (i = 0; i < nodelist.getLength(); i++)
		        	{
		        		elem = (Element)nodelist.item(i);
		        		//find the generator
		        		if (elem.getAttribute("name").equals(genername))
		        		{
		        			nodelist = elem.getElementsByTagName(ATTRIBUTESGRP);
		        			if (nodelist.getLength() != 0)
		        			{
		        				for (i = 0; i < nodelist.getLength(); i++)
		        				{
		        					elem = (Element)nodelist.item(i);
		        					//find the attributes group
		        					if (elem.getAttribute("name").equals(grpname))
		        					{
		        						nodelist = elem.getElementsByTagName(ATTRIBUTE);
		        						//find the attribute
		        						for (j = 0; j < nodelist.getLength(); j++)
		        						{
		        							elem = (Element)nodelist.item(j);
			        						if (elem.getAttribute("name").equals(attriname))
			        						{
			        							elem.getParentNode().removeChild(elem);
			        							doc2XmlFile(doc, profile);
			        						}
		        						}
		        					}
		        				}
		        			}
		        		}	
		        	}
		        }
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				e.getStackTrace();
			}
		}
	}
	*/
	
	//Insert
	//insert a attribute
	/*
	public static void AddAttri(String profile, GeneratorAttri attri)
	{
		File file = new File(profile);
		if (file.exists() && attri != null)
		{
			try
			{
				// Create a factory
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        // Use the factory to create a builder
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(file);
		        doc.normalize();
		        
		        int i, j;
		        NodeList generlist, grplist;
		        Element generelem, grpelem;
		        
		        generlist = doc.getElementsByTagName(GENERATOR);
		        for (i = 0; i < generlist.getLength(); i++)
		        {
		        	generelem = (Element)generlist.item(i);
		        	//find the generator
		        	if (generelem.getAttribute("name").equals(attri.getGenerName()))
		        	{
		        		grplist = generelem.getElementsByTagName(ATTRIBUTESGRP);
		        		for (j = 0; j < grplist.getLength(); j++)
		        		{
		        			grpelem = (Element)grplist.item(j);
		        			//find the attributes group
		        			if (grpelem.getAttribute("name").equals(attri.getAttriGrpName()))
		        			{
		        				Element attrielem = doc.createElement(ATTRIBUTE);
				        		attrielem.setAttribute("name", attri.getAttriName());
				        		attrielem.setAttribute("type", attri.getAttriType());
				        		attrielem.setAttribute("constraint", attri.getAttriConstraint());
				        		attrielem.setAttribute("defaultvalue", attri.getDefaultValue());
				        		attrielem.setAttribute("description", attri.getAttriDescri());
				        		Text textseg = doc.createTextNode(attri.getAttriValue());
				        		attrielem.appendChild(textseg);
				        		
				        		grpelem.appendChild(attrielem);
				        		
				        		break;
		        			}
		        		}
		        		
		        		break;
		        	}
		        }
		        
		        doc2XmlFile(doc, profile);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	//insert a attribute group
	public static void AddAttriGrp(String profile, String genername, GeneratorAttriGrp grp)
	{
		File file = new File(profile);
		if (file.exists())
		{
			try
			{
				// Create a factory
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        // Use the factory to create a builder
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(file);
		        doc.normalize();
		        
		        int i, j;
		        NodeList generlist, nodelist;
		        Element generelem, attriselem, grpelem;
		        GeneratorAttri attri;
		        Text textseg;
		        
		        generlist = doc.getElementsByTagName(GENERATOR);
		        for (i = 0; i < generlist.getLength(); i++)
		        {
		        	generelem = (Element)generlist.item(i);
		        	//find the generator
		        	if (generelem.getAttribute("name").equals(genername))
		        	{
		        		//find the attributes node
		        		nodelist = generelem.getElementsByTagName(ATTRIBUTES);
		        		if (nodelist.getLength() >= 1)
		        		{
		        			attriselem = (Element)nodelist.item(0);
		        		}
		        		else
		        		{
		        			attriselem = doc.createElement(ATTRIBUTES);
		        			generelem.appendChild(attriselem);
		        		}
		        		
		        		grpelem = doc.createElement(ATTRIBUTESGRP);	        	
			        	grpelem.setAttribute("name", grp.getAttriGrpName());
			          	
			        	for (j = 0; j < grp.getAttrisList().size(); j++)
			        	{
			        		attri = grp.getAttrisList().get(j);
			        		
			        		Element attrielem = doc.createElement(ATTRIBUTE);
			        		attrielem.setAttribute("name", attri.getAttriName());
			        		attrielem.setAttribute("type", attri.getAttriType());
			        		attrielem.setAttribute("constraint", attri.getAttriConstraint());
			        		attrielem.setAttribute("defaultvalue", attri.getDefaultValue());
			        		attrielem.setAttribute("description", attri.getAttriDescri());
			        		textseg = doc.createTextNode(attri.getAttriValue());
			        		attrielem.appendChild(textseg);
			        		
			        		grpelem.appendChild(attrielem);
			        	}	
			        	attriselem.appendChild(grpelem);
		        	}
		        }
		        
		        doc2XmlFile(doc, profile);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	//insert a generator
	public static void AddGenerator(String profile, Generator generator)
	{
		File file = new File(profile);
		if (file.exists())
		{
			try
			{
				// Create a factory
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        // Use the factory to create a builder
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        Document doc = builder.parse(file);
		        doc.normalize();
		        
		        Text textseg;
		        GeneratorAttriGrp attrisgrp;
		        GeneratorAttri attri;
		        int i, j;
		        //generator node
		        Element gener = doc.createElement(GENERATOR);
		        gener.setAttribute("name", generator.getGeneratorName());
		        //generator path node
		        Element path = doc.createElement(GENERATORPATH);
		        textseg = doc.createTextNode(generator.getGeneratorPath());
		        path.appendChild(textseg);
		        gener.appendChild(path);
		        //generator input file node
		        //Element input = doc.createElement(INPUTFILE);
		        //textseg = doc.createTextNode(generator.getInputfile());
		        //input.appendChild(textseg);
		        //gener.appendChild(input);
		        //generator import file node
		        Element importf = doc.createElement(IMPORTFILE);
		        textseg = doc.createTextNode(generator.getImportfilePath());
		        importf.appendChild(textseg);
		        gener.appendChild(importf);
		        //generator export file node
		        Element exportf = doc.createElement(EXPORTFILE);
		        textseg = doc.createTextNode(generator.getExportfilePath());
		        exportf.appendChild(textseg);
		        gener.appendChild(exportf);
		        //generator attributes node
		        Element attris = doc.createElement(ATTRIBUTES);
		        
		        //traverse create each attributes group node
		        for (i = 0; i < generator.getAttrisGrplist().size(); i++)
		        {
		        	attrisgrp = (GeneratorAttriGrp)generator.getAttrisGrplist().get(i);
		        	
		        	Element grpelem = doc.createElement(ATTRIBUTESGRP);
		        	grpelem.setAttribute("name", attrisgrp.getAttriGrpName());
		        	//traverse create each attribute node
		        	for (j = 0; j < attrisgrp.getAttrisList().size(); j++)
		        	{
		        		attri = attrisgrp.getAttrisList().get(j);
		        		
		        		Element attrielem = doc.createElement(ATTRIBUTE);
		        		attrielem.setAttribute("name", attri.getAttriName());
		        		attrielem.setAttribute("type", attri.getAttriType());
		        		attrielem.setAttribute("constraint", attri.getAttriConstraint());
		        		attrielem.setAttribute("defaultvalue", attri.getDefaultValue());
		        		attrielem.setAttribute("description", attri.getAttriDescri());
		        		if (attri.getAttriValue() != "")
		        		{
		        			textseg = doc.createTextNode(attri.getAttriValue());
		        			attrielem.appendChild(textseg);
		        		}
		        		grpelem.appendChild(attrielem);
		        	}	
		        	attris.appendChild(grpelem);
		        }
		        gener.appendChild(attris);
		        
		        if (doc.getFirstChild() != null)
		        {
		        	if (doc.getFirstChild().getNodeName().equals("Generators"))
		        		doc.getFirstChild().appendChild(gener);
		        }
		        
		        doc2XmlFile(doc, profile);
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				e.getStackTrace();
			}
		}
	}
	*/
	
	//Write
	public static void WriteXML(String xmlfile, List<Generator> generlist)
	{
		try
		{
			// Create a factory
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        // Use the factory to create a builder
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.newDocument();
	        
	        Element geners = doc.createElement("Generators");
	        doc.appendChild(geners);
	        
	        Generator generator;
	        Text textseg;
	        int i;
	        
	        for (i = 0; i < generlist.size(); i++)
	        {
	        	generator = (Generator)generlist.get(i);
	        	
	        	//generator node
		        Element gener = doc.createElement(GENERATOR);
		        gener.setAttribute("name", generator.getGeneratorName());
		        //generator path node
		        Element path = doc.createElement(GENERATORPATH);
		        textseg = doc.createTextNode(generator.getGeneratorPath());
		        path.appendChild(textseg);
		        gener.appendChild(path);
		        //generator import file node
		        Element importf = doc.createElement(IMPORTFILE);
		        textseg = doc.createTextNode(generator.getImportfilePath());
		        importf.appendChild(textseg);
		        gener.appendChild(importf);
		        //generator export file node
		        Element exportf = doc.createElement(EXPORTFILE);
		        textseg = doc.createTextNode(generator.getExportfilePath());
		        exportf.appendChild(textseg);
		        gener.appendChild(exportf);
	        	//attributes node
		        Element attris = doc.createElement(ATTRIBUTES);
		        gener.appendChild(attris);
		        
		        WriteAttrisGrp(doc, attris, generator.getAttrisGrp());
		        //import attributes node
		        //Element importattrisgrp = doc.createElement(ATTRIBUTESGRP);
		        //importattrisgrp.setAttribute("name", IMPORT);
		        //attris.appendChild(importattrisgrp);
		        
		        //WriteAttrisGrp(doc, importattrisgrp, generator.getImportAttrisGrp());
		        
		        //export attributes node
		        //Element exportattrisgrp = doc.createElement(ATTRIBUTESGRP);
		        //exportattrisgrp.setAttribute("name", EXPORT);
		        //attris.appendChild(exportattrisgrp);
		        
		        //WriteAttrisGrp(doc, exportattrisgrp, generator.getExportAttrisGrp());
		        
		        geners.appendChild(gener);
	        }
	        
	        doc2XmlFile(doc, xmlfile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void WriteAttrisGrp(Document doc, Element attrigrpnode, GeneratorAttriGrp attrisgrp)
	{
		GeneratorAttriGrp grp;
		GeneratorAttri attri;
		
		List<GeneratorAttri> attrislist = attrisgrp.getAttrisList();
		List<GeneratorAttriGrp> childattrislist = attrisgrp.getChildAttriGrpList();
		
		int i;
		for (i = 0; i < attrislist.size(); i++)
		{
			attri = (GeneratorAttri)attrislist.get(i);
			
			Element attrielem = doc.createElement(ATTRIBUTE);
			if (!attri.getAttriName().equals(""))
				attrielem.setAttribute("name", attri.getAttriName());
    		if (!attri.getAttriType().equals(""))
    			attrielem.setAttribute("type", attri.getAttriType());
    		if (!attri.getAttriConstraint().equals(""))
    			attrielem.setAttribute("constraint", attri.getAttriConstraint());
    		if (!attri.getDefaultValue().equals(""))
    			attrielem.setAttribute("defaultvalue", attri.getDefaultValue());
    		if (!attri.getDefaultValue().equals(""))
    			attrielem.setAttribute("description", attri.getAttriDescri());
    		if (!attri.getAttriValue().equals(""))
    		{
    			Text textseg = doc.createTextNode(attri.getAttriValue());
    			attrielem.appendChild(textseg);
    		}
    		attrigrpnode.appendChild(attrielem);
		}
		
		for (i = 0; i < childattrislist.size(); i++)
		{
			grp = (GeneratorAttriGrp)childattrislist.get(i);
			Element elem = doc.createElement(ATTRIBUTESGRP);
			elem.setAttribute("name", grp.getAttriGrpName());
			
			attrigrpnode.appendChild(elem);
			WriteAttrisGrp(doc, elem, grp);
		}
	}
	
	//Read xml file 
	public static List<Generator> ReadXmlfile(String xmlfile)
	{
		List<Generator> generators = null;
		
		File file = new File(xmlfile);
		try
		{
			generators = new ArrayList<Generator>();
				
			// Create a factory
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        // Use the factory to create a builder
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(file);
	        
	        doc.normalize();
	        
	        NodeList generlist = doc.getElementsByTagName(GENERATOR);
	        
	        NodeList nodelist, elemgrplist;
	        String name, path, importfile, exportfile;
	        //String inputfile;
	    	Element generelem, elem;
	    	int i;
	       	
	    	//traverse each generator
	        for (i = 0; i < generlist.getLength(); i++)
	        {
	        	Generator gener = new Generator();
	        	generelem = (Element)generlist.item(i);
	        	//get the generator name
	        	name = generelem.getAttribute("name");
	        	gener.setGeneratorName(name);
	        	//get generator path
	        	nodelist = generelem.getElementsByTagName(GENERATORPATH);
	        	if (nodelist.getLength() == 1)
	        	{
	        		elem = (Element)nodelist.item(0);
	        		path = elem.getFirstChild().getNodeValue();
	        		gener.setGeneratorPath(path);
	        	}	
	        	//nodelist = generelem.getElementsByTagName(INPUTFILE);
	        	//if (nodelist.getLength() == 1)
	        	//{
	        	//	elem = (Element)nodelist.item(0);
	        	//	inputfile = elem.getFirstChild().getNodeValue();
	        	//	gener.setInputfile(inputfile);
	        	//}
	        	//get import file path
	        	nodelist = generelem.getElementsByTagName(IMPORTFILE);
	        	if (nodelist.getLength() == 1)
	        	{
	        		elem = (Element)nodelist.item(0);
	        		if (elem.getFirstChild() !=null)
	        		{
	        			importfile = elem.getFirstChild().getNodeValue();
	        			gener.setImportfilePath(importfile);
	        		}
	        		else
	        			importfile = "";
	        	}
	        	//get export file path
	        	nodelist = generelem.getElementsByTagName(EXPORTFILE);
	        	if (nodelist.getLength() == 1)
	        	{
	        		elem = (Element)nodelist.item(0);
	        		if (elem.getFirstChild() != null)
	        		{
		        		exportfile = elem.getFirstChild().getNodeValue();
		        		gener.setExportfilePath(exportfile);
	        		}
	        		else
	        			exportfile = "default";
	        	}
	        	
	        	elemgrplist = generelem.getElementsByTagName(ATTRIBUTES);
	        	{
	        		if (elemgrplist.getLength() != 1)
	        			throw new RuntimeException("Each generator must have a Attributes node");
	        		elem = (Element)elemgrplist.item(0);
	        		ReadAttrisGrp(elem, gener.getAttrisGrp());
	        		/*
	        		//traverse each attributes group
	        		for (j = 0; j < elemgrplist.getLength(); j++)
	        		{
	        			elem = (Element)elemgrplist.item(j);
	        			attrisgrpname = elem.getAttribute("name");
	        			
	        			//get the import attributes group
	        			if (attrisgrpname.equals(IMPORT))
	        			{
	        				ReadAttrisGrp(elem, gener.getImportAttrisGrp());
	        			}
	        			
	        			//get the export attributes group
	        			if (attrisgrpname.equals(EXPORT))
	        			{
	        				ReadAttrisGrp(elem, gener.getExportAttrisGrp());
	        			}
	        		}
	        		*/
	        	}
	        	
	        	generators.add(gener);
	        }
	        
	        return generators;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static void ReadAttrisGrp(Element attrisgrpnode, GeneratorAttriGrp attrisgrp)
	{
		NodeList nodelist;
		Node node;
		Element elem;
		String name;
		int i;
		
		nodelist = attrisgrpnode.getChildNodes();
		for (i = 0; i < nodelist.getLength(); i++)
		{
			node = nodelist.item(i);
			//find a attribute node
			if (node.getNodeType() == Node.ELEMENT_NODE &&
					node.getNodeName().equals(ATTRIBUTE))
			{
				elem = (Element)node;
				GeneratorAttri attri = new GeneratorAttri(elem);
				attrisgrp.addAttri(attri);
				
			}
			
			//find a attributes group node
			if (node.getNodeType() == Node.ELEMENT_NODE &&
					node.getNodeName().equals(ATTRIBUTESGRP))
			{
				elem = (Element)node;
				name = elem.getAttribute("name");
				
				GeneratorAttriGrp grp = new GeneratorAttriGrp(name);
				attrisgrp.addAttriGrp(grp);
				
				ReadAttrisGrp(elem, grp);
			}
		}
	}
	
	public static boolean isSelXmlNode(Element elem)
	{
		NodeList childrens = elem.getElementsByTagName(XmlFunction.ATTRIBUTESGRP);
		if ((elem.getNodeName().equals(XmlFunction.ATTRIBUTESGRP) || 
			 elem.getNodeName().equals(XmlFunction.ATTRIBUTES)) &&
			childrens.getLength() > 1 && 
			elem.getAttribute(XmlFunction.CONSTRAINT).equals("Single"))
			return true;
		else
			return false;
	}
}
