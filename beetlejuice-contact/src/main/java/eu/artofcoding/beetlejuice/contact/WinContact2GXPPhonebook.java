/*
 * beetlejuice
 * beetlejuice-contact
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 12.04.13 15:27
 */

package eu.artofcoding.beetlejuice.contact;

import eu.artofcoding.beetlejuice.xml.XmlHelperException;
import eu.artofcoding.beetlejuice.xml.XmlOutputHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WinContact2GXPPhonebook {

    private static final Logger logger = Logger.getLogger(WinContact2GXPPhonebook.class.getName());

    public static class MyNamespaceContext implements NamespaceContext {

        public String getNamespaceURI(String prefix) {
            if (prefix.equals("c")) {
                return "http://schemas.microsoft.com/Contact";
            } else {
                return XMLConstants.NULL_NS_URI;
            }
        }

        public String getPrefix(String namespace) {
            if (namespace.equals("http://schemas.microsoft.com/Contact")) {
                return "c";
            } else {
                return null;
            }
        }

        public Iterator getPrefixes(String namespace) {
            return null;
        }

    }

    public static void main(String[] args) {
        // Output document; the phonebook
        Document addressBookDocument = null;
        Element addressBook = null;
        try {
            addressBookDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            addressBook = addressBookDocument.createElement("AddressBook");
            addressBookDocument.appendChild(addressBook);
        } catch (ParserConfigurationException e) {
            logger.log(Level.SEVERE, null, e);
        }
        //
        File contactDir = new File("C:/Users/rbe/Contacts");
        XPath xpath = XPathFactory.newInstance().newXPath();
        NamespaceContext namespaceContext = new MyNamespaceContext();
        xpath.setNamespaceContext(namespaceContext);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        Document document = null;
        for (File contact : contactDir.listFiles()) {
            try {
                // Addressbook Contact
                Element abContact = addressBookDocument.createElement("Contact");
                // Parse contact XML
                document = documentBuilderFactory.newDocumentBuilder().parse(contact);
                // Name
                Element givennameElement = (Element) xpath.evaluate("//c:GivenName", document, XPathConstants.NODE);
                Element firstnameElement = addressBookDocument.createElement("FirstName");
                if (null != givennameElement && givennameElement.getTextContent().length() > 0) {
                    firstnameElement.setTextContent(givennameElement.getTextContent());
                } else {
                    firstnameElement.setTextContent("-");
                }
                abContact.appendChild(firstnameElement);
                Element familyNameElement = (Element) xpath.evaluate("//c:FamilyName", document, XPathConstants.NODE);
                Element lastnameElement = addressBookDocument.createElement("LastName");
                if (null != familyNameElement && familyNameElement.getTextContent().length() > 0) {
                    lastnameElement.setTextContent(familyNameElement.getTextContent());
                } else {
                    lastnameElement.setTextContent("-");
                }
                abContact.appendChild(lastnameElement);
                // Address
                Element abAddressElement = addressBookDocument.createElement("Address");
                abContact.appendChild(abAddressElement);
                Element address1Element = addressBookDocument.createElement("address1");
                address1Element.setTextContent("address");
                abAddressElement.appendChild(address1Element);
                Element cityElement = addressBookDocument.createElement("city");
                cityElement.setTextContent("city");
                abAddressElement.appendChild(cityElement);
                Element zipcodeElement = addressBookDocument.createElement("zipcode");
                zipcodeElement.setTextContent("12345");
                abAddressElement.appendChild(zipcodeElement);
                Element stateElement = addressBookDocument.createElement("state");
                stateElement.setTextContent("state");
                abAddressElement.appendChild(stateElement);
                Element countryElement = addressBookDocument.createElement("country");
                countryElement.setTextContent("country");
                abAddressElement.appendChild(countryElement);
                // PhoneNumber
                NodeList phonenumberList = (NodeList) xpath.evaluate("//c:PhoneNumber", document, XPathConstants.NODESET);
                for (int i = 0; i < phonenumberList.getLength(); i++) {
                    //
                    Element abPhoneElement = addressBookDocument.createElement("Phone");
                    Element abPhonenumberElement = addressBookDocument.createElement("phonenumber");
                    abPhoneElement.appendChild(abPhonenumberElement);
                    Element abAccountindexElement = addressBookDocument.createElement("accountindex");
                    abAccountindexElement.setTextContent("0");
                    abPhoneElement.appendChild(abAccountindexElement);
                    //
                    NodeList pnChild = phonenumberList.item(i).getChildNodes();
                    String number = null;
                    for (int j = 0; j < pnChild.getLength(); j++) {
                        if (pnChild.item(j).getNodeName().equals("c:Number")) {
                            number = pnChild.item(j).getTextContent();
                        } else if (pnChild.item(j).getNodeName().equals("c:LabelCollection")) {
                            NodeList labelNode = ((Element) pnChild.item(j)).getElementsByTagName("c:Label");
                            boolean voice = false;
                            boolean business = false;
                            for (int k = 0; k < labelNode.getLength(); k++) {
                                if (labelNode.item(k).getTextContent().equals("Voice")) {
                                    voice = true;
                                }
                                if (labelNode.item(k).getTextContent().equals("Business")) {
                                    business = true;
                                }
                            }
                            if (voice && business) {
                                abPhonenumberElement.setTextContent(number.replaceAll(" ", ""));
                                abContact.appendChild(abPhoneElement);
                                addressBook.appendChild(abContact);
                            }
                        }
                    }
                    try {
                        XmlOutputHelper.output(addressBookDocument, new File("d:/tmp/pb.xml"), "ISO-8859-1");
                    } catch (XmlHelperException e) {
                        logger.log(Level.SEVERE, null, e);
                    }
                }
            } catch (ParserConfigurationException e) {
                logger.log(Level.SEVERE, null, e);
            } catch (SAXException e) {
                logger.log(Level.SEVERE, null, e);
            } catch (IOException e) {
                logger.log(Level.SEVERE, null, e);
            } catch (XPathExpressionException e) {
                logger.log(Level.SEVERE, null, e);
            }
        }
    }

}
