package com.guilherme.etlfw.xml.reader;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.guilherme.etlfw.xml.element.XMLElement;

public class XMLReader {
	private Document document;
	private NodeList list;
	private int position;
	
	public XMLReader(String fileName)  {
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			document = documentBuilder.parse(new File(fileName));
			
			list = document.getDocumentElement().getChildNodes();
			
			cleanDocument();

			first();
		} 
		catch (SAXException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();} 
		catch (ParserConfigurationException e) {e.printStackTrace();}
	}

	private void cleanDocument() {
		for(int i = 0; i < list.getLength(); i++) {
			if(list.item(i).getNodeType() == Node.TEXT_NODE) {
				document.getDocumentElement().removeChild(list.item(i));
			}
		}
	}

	public String getRootNodeName() {
		return document.getDocumentElement().getNodeName();
	}

	public int countNodes() {
		return list.getLength();
	}

	public String getValueForKey(String value) {
		XMLElement element = new XMLElement(list.item(position));
        return element.getChildValueByTagName(value);
	}

	public boolean next() {
		if(++position < list.getLength()) 
			return true;
		last();
		return false;
	}
	
	public boolean previous() {
		if(--position >= 0)
			return true;
		first();
		return false;
	}
	
	public void first() {
		position = 0;
	}
	
	public void last() {
		position = list.getLength()-1;
	}

	public XMLElement getElement() {
		return new XMLElement(list.item(position));
	}

}
