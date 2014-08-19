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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class JaxbHelper {

    @SuppressWarnings({"unchecked"})
    public static <T> T unmarshal(Class<T> clazz, File file) throws JaxbHelperException {
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
            throw new JaxbHelperException(e);
        }
        return odisee;
    }

    public static <T> void marshal(Class<T> clazz, T objectToMarshal, Writer writer) throws JaxbHelperException {
        try {
            // create a JAXBContext capable of handling classes generated into package
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz.getPackage().getName());
            // create a Marshaller and do marshal
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(objectToMarshal, writer);
            writer.flush();
        } catch (JAXBException | IOException e) {
            throw new JaxbHelperException(e);
        }
    }

    public static <T> void marshal(Class<T> clazz, T objectToMarshal, OutputStream stream) throws JaxbHelperException {
        try {
            // create a JAXBContext capable of handling classes generated into package
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz.getPackage().getName());
            // create a Marshaller and do marshal
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(objectToMarshal, stream);
        } catch (JAXBException e) {
            throw new JaxbHelperException(e);
        }
    }

    public static <T> void marshal(Class<T> clazz, T objectToMarshal, File file) throws JaxbHelperException {
        try {
            // create a JAXBContext capable of handling classes generated into package
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz.getPackage().getName());
            // create a Marshaller and do marshal
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(objectToMarshal, new FileOutputStream(file));
        } catch (FileNotFoundException | JAXBException e) {
            throw new JaxbHelperException(e);
        }
    }

}
