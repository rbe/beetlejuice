/*
 * beetlejuice
 * beetlejuice-xml
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 18.02.13 19:49
 */

package eu.artofcoding.beetlejuice.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class XmlHelper {

    private static final Logger logger = Logger.getLogger(XmlHelper.class.getName());

    protected static final String EMPTY_STRING = "";

    protected static final String YES = "yes";

    /**
     * Standard encoding
     */
    protected static final String ENCODING = "UTF-8";

    /**
     * XPath
     */
    private static final XPath xpath;

    static {
        // Initialize XPath
        xpath = XPathFactory.newInstance().newXPath();
    }

    /**
     * Do not create an instance of XmlHelper
     */
    private XmlHelper() {
    }

    /**
     * @param encoding
     * @return {@link String}
     */
    protected static String getEncoding(String encoding) {
        String enc = null;
        if (null != encoding) {
            enc = encoding;
        } else {
            enc = ENCODING;
        }
        return enc;
    }

    /**
     * Creates a new DOM Document from the given XML String.
     * @param xmlText String that represents the XML
     * @return Returns a {@link Document}
     * @throws XmlHelperException
     */
    public static Document createDocument(String xmlText) throws XmlHelperException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlText)));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new XmlHelperException(EMPTY_STRING, e);
        }
    }

    /**
     * @param url
     * @return {@link Document}
     * @throws XmlHelperException
     */
    public static Document createDocument(URL url) throws XmlHelperException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // TODO Unpack file?
            return builder.parse(new InputSource(url.openStream()));
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new XmlHelperException(EMPTY_STRING, e);
        }
    }

    /**
     * Create Document object from file
     * @param file
     * @return {@link Document}
     * @throws XmlHelperException
     */
    public static Document createDocument(File file) throws XmlHelperException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // TODO Unpack file?
            return builder.parse(file);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new XmlHelperException(EMPTY_STRING, e);
        }
    }

    /**
     * @param expression
     * @param node
     * @return {@link String}
     * @throws XPathExpressionException
     */
    public static String xpathToString(String expression, Node node) throws XPathExpressionException {
        return (String) xpath.compile(expression).evaluate(node, XPathConstants.STRING);
    }

    /**
     * @param expression
     * @param node
     * @return {@link Node}
     * @throws XPathExpressionException
     */
    public static Node xpathToNode(String expression, Node node) throws XPathExpressionException {
        return (Node) xpath.compile(expression).evaluate(node, XPathConstants.NODE);
    }

    /**
     * @param expression
     * @param node
     * @return {@link NodeList}
     * @throws XPathExpressionException
     */
    public static NodeList xpathToNodeList(String expression, Node node) throws XPathExpressionException {
        return (NodeList) xpath.compile(expression).evaluate(node, XPathConstants.NODESET);
    }

    /**
     * Get a string vector from the url path with the given xpath search string, id and value
     * @param xmlUrl       The URL to the xml file.
     * @param xpathSearch  The xpath search string
     * @param id           The key to search for
     * @param value        The value key to get the result from
     * @param addIdToValue Add the id-key value to the result string if true.
     * @return Returns a sorted string vector.
     * @throws XmlHelperException
     */
    public static Vector<String> getVectorFromXML(URL xmlUrl, String xpathSearch, String id, String value, boolean addIdToValue) throws XmlHelperException {
        Document document = XmlHelper.createDocument(xmlUrl);
        return getVectorFromXML(document, xpathSearch, id, value, addIdToValue);
    }

    /**
     * Get a string vector from the document with the given xpath search string, id and value
     * @param document     The document to search in
     * @param xpathSearch  The xpath search string
     * @param id           The key to search for
     * @param value        The value key to get the result from
     * @param addIdToValue Add the id-key value to the result string if true.
     * @return Returns a string vector.
     */
    public static Vector<String> getVectorFromXML(Document document, String xpathSearch, String id, String value, boolean addIdToValue) {
        return generateVectorFromXML(document, xpathSearch, id, value, addIdToValue, false);
    }

    /**
     * Get a sorted string vector from the url path with the given xpath search string, id and value
     * @param xmlUrl       The URL to the xml file.
     * @param xpathSearch  The xpath search string
     * @param id           The key to search for
     * @param value        The value key to get the result from
     * @param addIdToValue Add the id-key value to the result string if true.
     * @return Returns a sorted string vector.
     * @throws XmlHelperException
     */
    public static Vector<String> getSortedVectorFromXML(URL xmlUrl, String xpathSearch, String id, String value, boolean addIdToValue) throws XmlHelperException {
        Document document = XmlHelper.createDocument(xmlUrl);
        return getSortedVectorFromXML(document, xpathSearch, id, value, addIdToValue);
    }

    /**
     * Get a sorted string vector from the document with the given xpath search string, id and value
     * @param document     The document to search in
     * @param xpathSearch  The xpath search string
     * @param id           The key to search for
     * @param value        The value key to get the result from
     * @param addIdToValue Add the id-key value to the result string if true.
     * @return Returns a sorted string vector.
     */
    public static Vector<String> getSortedVectorFromXML(Document document, String xpathSearch, String id, String value, boolean addIdToValue) {
        return generateVectorFromXML(document, xpathSearch, id, value, addIdToValue, true);
    }

    /**
     * Get a sorted/unsorted string vector from the document with the given xpath search string, id and value
     * @param document     The document to search in
     * @param xpathSearch  The xpath search string
     * @param id           The key to search for
     * @param value        The value key to get the result from
     * @param addIdToValue Add the id-key value to the result string if true.
     * @return Returns a string vector.
     */
    private static Vector<String> generateVectorFromXML(Document document, String xpathSearch, String id, String value, boolean addIdToValue, boolean sorted) {
        Vector<String> vector = null;
        try {
            //            XPath xpath = XPathFactory.newInstance().newXPath();
            //            NodeList resultNodeList = (NodeList) xpath.evaluate(xpathSearch, document, javax.xml.xpath.XPathConstants.NODESET);
            NodeList resultNodeList = xpathToNodeList(xpathSearch, document);
            for (int i = 0; i < resultNodeList.getLength(); i++) {
                if (vector == null) {
                    vector = new Vector<>();
                }
                Node node = resultNodeList.item(i);
                String attrId = node.getAttributes().getNamedItem(id).getNodeValue();
                String attrValue = node.getAttributes().getNamedItem(value).getNodeValue();
                if (addIdToValue) {
                    if ((attrId == null || (attrId.isEmpty() && attrId.length() == 0)) && (attrValue == null || (attrValue.isEmpty() && attrValue.length() == 0))) {
                        vector.add(EMPTY_STRING);
                    } else {
                        vector.add(String.format("%s - %s", attrId, attrValue));
                    }
                } else {
                    vector.add(attrValue);
                }
            }
            if (vector != null && !vector.isEmpty() && sorted) {
                Collections.sort(vector);
                return vector;
            } // else the vector will not be sorted.
        } catch (XPathExpressionException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return vector;
    }

    /**
     * @param xmlUrl      The URL to the xml document to search in
     * @param xpathSearch The xpath search string
     * @return Returns a string value that was found
     * @throws XmlHelperException
     */
    public static String getValueFromKey(URL xmlUrl, String xpathSearch) throws XmlHelperException {
        String result = EMPTY_STRING;
        try {
            Document document = XmlHelper.createDocument(xmlUrl);
            //            XPath xpath = XPathFactory.newInstance().newXPath();
            //            result = (String) xpath.evaluate(xpathSearch, document, javax.xml.xpath.XPathConstants.STRING);
            result = xpathToString(xpathSearch, document);
        } catch (XPathExpressionException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * @param document    The document to search in
     * @param xpathSearch The xpath search string
     * @return Returns a string value that was found
     */
    public static String getValueFromKey(Document document, String xpathSearch) {
        String result = EMPTY_STRING;
        try {
            //            XPath xpath = XPathFactory.newInstance().newXPath();
            //            result = (String) xpath.evaluate(xpathSearch, document, javax.xml.xpath.XPathConstants.STRING);
            result = xpathToString(xpathSearch, document);
        } catch (XPathExpressionException e) {
            logger.log(Level.SEVERE, null, e);
        }
        return result;
    }

}
