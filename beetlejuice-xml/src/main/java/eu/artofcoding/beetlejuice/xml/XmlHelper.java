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
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class XmlHelper {

    /**
     * Standard encoding
     */
    private static final String ENCODING = "UTF-8";

    /**
     * Non-transforming, non-validating Transformer
     */
    private static Transformer transformer;

    /**
     * XPath
     */
    private static final XPath xpath;

    /**
     * Do not create an instance of XmlHelper
     */
    private XmlHelper() {
    }

    static {
        // Initialize non-transforming, non-validating Transformer
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        // Initialize XPath
        xpath = XPathFactory.newInstance().newXPath();
    }

    /**
     * @param encoding
     * @return
     */
    private static String getEncoding(String encoding) {
        String enc = null;
        if (null != encoding) {
            enc = encoding;
        } else {
            enc = ENCODING;
        }
        return enc;
    }

    /**
     * Transforms the given Source into an String representation
     * @param result XML Source
     * @return transformed Source as String
     * @throws XmlHelperException
     */
    public static String xmlToString(Source result) throws XmlHelperException {
        try {
            StringWriter sw = new StringWriter();
            transformer.transform(result, new StreamResult(sw));
            return sw.toString();
        } catch (TransformerException e) {
            throw new XmlHelperException("", e);
        }
    }

    /**
     * Output XML node (using XSLT)
     * @param node
     * @param xslt     Use XSLT for transformation - can be null for no transformation
     * @param encoding
     * @return
     * @throws XmlHelperException
     */
    public static String xsltTo(Node node, Source xslt, String encoding) throws XmlHelperException {
        String s = null;
        try {
            Transformer myTransformer = null;
            if (null != xslt) {
                myTransformer = TransformerFactory.newInstance().newTransformer(xslt);
            } else {
                myTransformer = transformer;
            }
            myTransformer.setOutputProperty(OutputKeys.ENCODING, getEncoding(encoding));
            StringWriter stringWriter = new StringWriter();
            myTransformer.transform(new DOMSource(node), new StreamResult(stringWriter));
            s = stringWriter.toString();
        } catch (TransformerException | TransformerFactoryConfigurationError e) {
            throw new XmlHelperException("", e);
        }
        return s;
    }

    /**
     * Output XML node
     * @param node
     * @param xslt
     * @param encoding
     * @return
     * @throws TransformerException
     * @throws XmlHelperException
     */
    public static String output(Node node, Source xslt, String encoding) throws TransformerException, XmlHelperException {
        return xsltTo(node, xslt, encoding);
    }

    /**
     * Output XML node
     * @param node
     * @param encoding
     * @return
     * @throws TransformerException
     * @throws XmlHelperException
     */
    public static String output(Node node, String encoding) throws TransformerException, XmlHelperException {
        return xsltTo(node, null, encoding);
    }

    /**
     * @param node
     * @return
     * @throws TransformerException
     * @throws XmlHelperException
     */
    public static String output(Node node) throws TransformerException, XmlHelperException {
        return xsltTo(node, null, ENCODING);
    }

    /**
     * @param node         The XML document
     * @param outputStream
     * @param encoding     The encoding. If null default encoding will be taken
     * @throws XmlHelperException
     */
    public static void output(Node node, OutputStream outputStream, String encoding) throws XmlHelperException {
        try {
            transformer.setOutputProperty(OutputKeys.ENCODING, getEncoding(encoding));
            transformer.transform(new DOMSource(node), new StreamResult(outputStream));
        } catch (TransformerFactoryConfigurationError | TransformerException e) {
            throw new XmlHelperException("", e);
        }
    }

    /**
     * @param node         The XML {@link Node}.
     * @param outputStream Stream to write Node to.
     * @throws XmlHelperException
     */
    public static void output(Node node, OutputStream outputStream) throws XmlHelperException {
        output(node, outputStream, null);
    }

    /**
     * Saves the given XML <code>Document</code> to the given file path.
     * @param node     The XML {@link Node}.
     * @param file     The output {@link File}.
     * @param encoding Charset.
     * @throws XmlHelperException
     */
    public static void output(Node node, File file, String encoding) throws XmlHelperException {
        try {
            transformer.setOutputProperty(OutputKeys.ENCODING, getEncoding(encoding));
            transformer.transform(new DOMSource(node), new StreamResult(file));
        } catch (TransformerException | TransformerFactoryConfigurationError e) {
            throw new XmlHelperException("", e);
        }
    }

    /**
     * Saves the given XML <code>Document</code> to the given file path.
     * @param node The XML {@link Node}.
     * @param file The output {@link File}.
     * @throws XmlHelperException
     */
    public static void output(Node node, File file) throws XmlHelperException {
        output(node, file, null);
    }

    /**
     * @param node     The XML {@link Node}.
     * @param xslt
     * @param dest
     * @param encoding
     * @throws javax.xml.transform.TransformerException
     *
     */
    public static void output(Node node, File xslt, File dest, String encoding) throws TransformerException {
        Transformer myTransformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xslt));
        myTransformer.setOutputProperty(OutputKeys.ENCODING, getEncoding(encoding));
        myTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        myTransformer.transform(new DOMSource(node), new StreamResult(dest));
    }

    /**
     * @param source   {@link File} to read from.
     * @param xslt
     * @param dest
     * @param encoding Charset.
     * @throws javax.xml.transform.TransformerException
     *
     */
    public static void output(File source, File xslt, File dest, String encoding) throws TransformerException {
        Transformer myTransformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xslt));
        myTransformer.setOutputProperty(OutputKeys.ENCODING, getEncoding(encoding));
        myTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        myTransformer.transform(new StreamSource(source), new StreamResult(dest));
    }

    /**
     * @param in       Stream to read from.
     * @param xslt
     * @param encoding Charset.
     * @return
     * @throws XmlHelperException
     */
    public static String transform(InputStream in, InputStream xslt, String encoding) throws XmlHelperException {
        StringWriter writer = new StringWriter();
        try {
            Transformer myTransformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xslt));
            myTransformer.setOutputProperty(OutputKeys.ENCODING, getEncoding(encoding));
            myTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
            myTransformer.transform(new StreamSource(in), new StreamResult(writer));
        } catch (TransformerException e) {
            throw new XmlHelperException("Could not tranform XML", e);
        }
        return writer.toString();
    }

    /**
     * Creates a new DOM Document from the given XML String.
     * @param xmlText String that represents the XML
     * @return Returns a <code>org.w3c.dom.Document</code>
     * @throws XmlHelperException
     */
    public static Document createDocument(String xmlText) throws XmlHelperException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlText)));
        } catch (SAXException e) {
            throw new XmlHelperException("", e);
        } catch (IOException e) {
            throw new XmlHelperException("", e);
        } catch (ParserConfigurationException e) {
            throw new XmlHelperException("", e);
        }
    }

    /**
     * @param url
     * @return
     * @throws XmlHelperException
     */
    public static Document createDocument(URL url) throws XmlHelperException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // TODO Unpack file?
            return builder.parse(new InputSource(url.openStream()));
        } catch (SAXException e) {
            throw new XmlHelperException("", e);
        } catch (IOException e) {
            throw new XmlHelperException("", e);
        } catch (ParserConfigurationException e) {
            throw new XmlHelperException("", e);
        }
    }

    /**
     * Create Document object from file
     * @param file
     * @return
     * @throws XmlHelperException
     */
    public static Document createDocument(File file) throws XmlHelperException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // TODO Unpack file?
            return builder.parse(file);
        } catch (SAXException e) {
            throw new XmlHelperException("", e);
        } catch (IOException e) {
            throw new XmlHelperException("", e);
        } catch (ParserConfigurationException e) {
            throw new XmlHelperException("", e);
        }
    }

    /**
     * @param expression
     * @param node
     * @return
     * @throws XPathExpressionException
     */
    public static String xpathToString(String expression, Node node) throws XPathExpressionException {
        return (String) xpath.compile(expression).evaluate(node, XPathConstants.STRING);
    }

    /**
     * @param expression
     * @param node
     * @return
     * @throws XPathExpressionException
     */
    public static Node xpathToNode(String expression, Node node) throws XPathExpressionException {
        return (Node) xpath.compile(expression).evaluate(node, XPathConstants.NODE);
    }

    /**
     * @param expression
     * @param node
     * @return
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
                    vector = new Vector<String>();
                }
                Node node = resultNodeList.item(i);
                String attrId = node.getAttributes().getNamedItem(id).getNodeValue();
                String attrValue = node.getAttributes().getNamedItem(value).getNodeValue();
                if (addIdToValue) {
                    if ((attrId == null || (attrId != null && attrId.isEmpty() && attrId.length() == 0))
                            && (attrValue == null || (attrValue != null && attrValue.isEmpty() && attrValue.length() == 0))) {
                        vector.add("");
                    } else {
                        vector.add(attrId + " - " + attrValue);
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
            Logger.getLogger(XmlHelper.class.getName()).log(Level.SEVERE, null, e);
        }
        return vector;
    }

    /**
     * @param document
     * @param xpathSearch
     * @param value
     * @param synonym
     * @return
     */
    public static Map<String, Vector<String>> getUnitSynonyms(Document document, String xpathSearch, /* String id,*/ String value, String synonym) {
        Map<String, Vector<String>> map = new HashMap<String, Vector<String>>();
        try {
            NodeList resultNodeList = xpathToNodeList(xpathSearch, document);
            for (int i = 0; i < resultNodeList.getLength(); i++) {
                Vector<String> vector = new Vector<String>();
                Node node = resultNodeList.item(i);
                //String attrId = node.getAttributes().getNamedItem(id).getNodeValue();
                String attrValue = node.getAttributes().getNamedItem(value).getNodeValue();
                try {
                    String attrSynonym = node.getAttributes().getNamedItem(synonym).getNodeValue();
                    String[] synonyms = attrSynonym.split(",");
                    for (int j = 0; j < synonyms.length; j++) {
                        vector.add(synonyms[j]);
                    }
                    map.put(attrValue, vector);
                } catch (NullPointerException e) {
                    Logger.getLogger(XmlHelper.class.getName()).log(Level.FINER, null, e);
                }
            }
        } catch (XPathExpressionException e) {
            Logger.getLogger(XmlHelper.class.getName()).log(Level.SEVERE, null, e);
        }
        return map;
    }

    /**
     * @param xmlUrl      The URL to the xml document to search in
     * @param xpathSearch The xpath search string
     * @return Returns a string value that was found
     * @throws XmlHelperException
     */
    public static String getValueFromKey(URL xmlUrl, String xpathSearch) throws XmlHelperException {
        String result = "";
        try {
            Document document = XmlHelper.createDocument(xmlUrl);
            //            XPath xpath = XPathFactory.newInstance().newXPath();
            //            result = (String) xpath.evaluate(xpathSearch, document, javax.xml.xpath.XPathConstants.STRING);
            result = xpathToString(xpathSearch, document);
        } catch (XPathExpressionException e) {
            Logger.getLogger(XmlHelper.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    /**
     * @param document    The document to search in
     * @param xpathSearch The xpath search string
     * @return Returns a string value that was found
     */
    public static String getValueFromKey(Document document, String xpathSearch) {
        String result = "";
        try {
            //            XPath xpath = XPathFactory.newInstance().newXPath();
            //            result = (String) xpath.evaluate(xpathSearch, document, javax.xml.xpath.XPathConstants.STRING);
            result = xpathToString(xpathSearch, document);
        } catch (XPathExpressionException e) {
            Logger.getLogger(XmlHelper.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

}
