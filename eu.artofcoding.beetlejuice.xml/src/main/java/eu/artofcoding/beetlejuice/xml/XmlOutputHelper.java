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

import org.w3c.dom.Node;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.OutputStream;
import java.io.StringWriter;

public final class XmlOutputHelper {

    //private static final XPath xpath;

    /**
     * Non-transforming, non-validating Transformer
     */
    private static Transformer transformer;

/*
    static {
        // Initialize non-transforming, non-validating Transformer
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, XmlHelper.YES);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        // Initialize XPath
        xpath = XPathFactory.newInstance().newXPath();
    }
*/

    private XmlOutputHelper() {
        throw new AssertionError();
    }

    /**
     * Transforms the given Source into an String representation
     * @param result XML Source
     * @return Transformed Source as String
     * @throws eu.artofcoding.beetlejuice.xml.XmlHelperException
     */
    public static String xmlToString(Source result) throws XmlHelperException {
        try {
            StringWriter sw = new StringWriter();
            transformer.transform(result, new StreamResult(sw));
            return sw.toString();
        } catch (TransformerException e) {
            throw new XmlHelperException(XmlHelper.EMPTY_STRING, e);
        }
    }

    /**
     * Output XML node
     * @param node
     * @param encoding
     * @return {@link String}
     */
    public static String output(Node node, String encoding) throws XsltHelperException {
        return XsltHelper.xsltTo(node, null, encoding);
    }

    /**
     * @param node
     * @return {@link String}
     */
    public static String output(Node node) throws XsltHelperException {
        return XsltHelper.xsltTo(node, null, XmlHelper.ENCODING);
    }

    /**
     * @param node         The XML {@link org.w3c.dom.Node}.
     * @param outputStream Stream to write Node to.
     * @throws eu.artofcoding.beetlejuice.xml.XmlHelperException
     */
    public static void output(Node node, OutputStream outputStream) throws XmlHelperException {
        output(node, outputStream, null);
    }

    /**
     * @param node         The XML document
     * @param outputStream
     * @param encoding     The encoding. If null default encoding will be taken
     * @throws eu.artofcoding.beetlejuice.xml.XmlHelperException
     */
    public static void output(Node node, OutputStream outputStream, String encoding) throws XmlHelperException {
        try {
            transformer.setOutputProperty(OutputKeys.ENCODING, XmlHelper.getEncoding(encoding));
            transformer.transform(new DOMSource(node), new StreamResult(outputStream));
        } catch (TransformerFactoryConfigurationError | TransformerException e) {
            throw new XmlHelperException(XmlHelper.EMPTY_STRING, e);
        }
    }

    /**
     * Saves the given XML <code>Document</code> to the given file path.
     * @param node The XML {@link org.w3c.dom.Node}.
     * @param file The output {@link java.io.File}.
     * @throws eu.artofcoding.beetlejuice.xml.XmlHelperException
     */
    public static void output(Node node, File file) throws XmlHelperException {
        output(node, file, null);
    }

    /**
     * Saves the given XML <code>Document</code> to the given file path.
     * @param node     The XML {@link org.w3c.dom.Node}.
     * @param file     The output {@link java.io.File}.
     * @param encoding Charset.
     * @throws eu.artofcoding.beetlejuice.xml.XmlHelperException
     */
    public static void output(Node node, File file, String encoding) throws XmlHelperException {
        try {
            transformer.setOutputProperty(OutputKeys.ENCODING, XmlHelper.getEncoding(encoding));
            transformer.transform(new DOMSource(node), new StreamResult(file));
        } catch (TransformerException | TransformerFactoryConfigurationError e) {
            throw new XmlHelperException(XmlHelper.EMPTY_STRING, e);
        }
    }
}
