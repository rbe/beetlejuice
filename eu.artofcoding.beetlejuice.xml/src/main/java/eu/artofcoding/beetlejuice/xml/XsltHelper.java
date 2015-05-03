/*
 * beetlejuice
 * beetlejuice-xml
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 04.04.13 09:49
 */

package eu.artofcoding.beetlejuice.xml;

import org.w3c.dom.Node;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;

public final class XsltHelper {

    /**
     * Non-transforming, non-validating Transformer
     */
    private static Transformer transformer;

    static {
        // Initialize non-transforming, non-validating Transformer
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, XmlHelper.YES);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Do not create an instance of this class.
     */
    private XsltHelper() {
    }

    /**
     * Output XML node (using XSLT)
     * @param node
     * @param xslt     Use XSLT for transformation - can be null for no transformation
     * @param encoding
     * @return
     * @throws XsltHelperException
     */
    public static String xsltTo(Node node, Source xslt, String encoding) throws XsltHelperException {
        String s = null;
        try {
            Transformer myTransformer = null;
            if (null != xslt) {
                myTransformer = TransformerFactory.newInstance().newTransformer(xslt);
            } else {
                myTransformer = XsltHelper.transformer;
            }
            myTransformer.setOutputProperty(OutputKeys.ENCODING, XmlHelper.getEncoding(encoding));
            StringWriter stringWriter = new StringWriter();
            myTransformer.transform(new DOMSource(node), new StreamResult(stringWriter));
            s = stringWriter.toString();
        } catch (TransformerException | TransformerFactoryConfigurationError e) {
            throw new XsltHelperException(XmlHelper.EMPTY_STRING, e);
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
     * @throws XsltHelperException
     */
    public static String output(Node node, Source xslt, String encoding) throws TransformerException, XsltHelperException {
        return xsltTo(node, xslt, encoding);
    }

    /**
     * @param node     The XML {@link org.w3c.dom.Node}.
     * @param xslt
     * @param dest
     * @param encoding
     * @throws TransformerException
     */
    public static void output(Node node, File xslt, File dest, String encoding) throws TransformerException {
        Transformer myTransformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xslt));
        myTransformer.setOutputProperty(OutputKeys.ENCODING, XmlHelper.getEncoding(encoding));
        myTransformer.setOutputProperty(OutputKeys.INDENT, XmlHelper.YES);
        myTransformer.transform(new DOMSource(node), new StreamResult(dest));
    }

    /**
     * @param source   {@link java.io.File} to read from.
     * @param xslt
     * @param dest
     * @param encoding Charset.
     *                 v
     */
    public static void output(File source, File xslt, File dest, String encoding) throws TransformerException {
        Transformer myTransformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xslt));
        myTransformer.setOutputProperty(OutputKeys.ENCODING, XmlHelper.getEncoding(encoding));
        myTransformer.setOutputProperty(OutputKeys.INDENT, XmlHelper.YES);
        myTransformer.transform(new StreamSource(source), new StreamResult(dest));
    }

    /**
     * @param in       Stream to read from.
     * @param xslt
     * @param encoding Charset.
     * @return
     * @throws XsltHelperException
     */
    public static String transform(InputStream in, InputStream xslt, String encoding) throws XsltHelperException {
        StringWriter writer = new StringWriter();
        try {
            Transformer myTransformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xslt));
            myTransformer.setOutputProperty(OutputKeys.ENCODING, XmlHelper.getEncoding(encoding));
            myTransformer.setOutputProperty(OutputKeys.INDENT, XmlHelper.YES);
            myTransformer.transform(new StreamSource(in), new StreamResult(writer));
        } catch (TransformerException e) {
            throw new XsltHelperException("Could not tranform XML", e);
        }
        return writer.toString();
    }

}
