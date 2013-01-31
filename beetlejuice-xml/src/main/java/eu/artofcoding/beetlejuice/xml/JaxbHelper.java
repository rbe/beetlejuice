/*
 * beetlejuice
 * beetlejuice-xml
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 13.01.13 12:54
 */

package eu.artofcoding.beetlejuice.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class JaxbHelper {

    @SuppressWarnings({"unchecked"})
    public static <T> T unmarshal(Class<T> clazz, File file) throws XmlException {
        T odisee;
        try {
            // create a JAXBContext capable of handling classes generated into package
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz.getPackage().getName());
            // create an Unmarshaller
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            // unmarshal an instance document into a tree of Java content
            // objects composed of classes from the package.
            odisee = (T) unmarshaller.unmarshal(new FileInputStream(file));
        } catch (IOException | JAXBException e) {
            throw new XmlException(e);
        }
        return odisee;
    }

    public static <T> void marshal(Class<T> clazz, T objectToMarshal, Writer writer) throws XmlException {
        try {
            // create a JAXBContext capable of handling classes generated into package
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz.getPackage().getName());
            // create a Marshaller and do marshal
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(objectToMarshal, writer);
            writer.flush();
        } catch (JAXBException e) {
            throw new XmlException(e);
        } catch (IOException e) {
            throw new XmlException(e);
        }
    }

    public static <T> void marshal(Class<T> clazz, T objectToMarshal, OutputStream stream) throws XmlException {
        try {
            // create a JAXBContext capable of handling classes generated into package
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz.getPackage().getName());
            // create a Marshaller and do marshal
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(objectToMarshal, stream);
        } catch (JAXBException e) {
            throw new XmlException(e);
        }
    }

    public static <T> void marshal(Class<T> clazz, T objectToMarshal, File file) throws XmlException {
        try {
            // create a JAXBContext capable of handling classes generated into package
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz.getPackage().getName());
            // create a Marshaller and do marshal
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(objectToMarshal, new FileOutputStream(file));
        } catch (FileNotFoundException | JAXBException e) {
            throw new XmlException(e);
        }
    }

}