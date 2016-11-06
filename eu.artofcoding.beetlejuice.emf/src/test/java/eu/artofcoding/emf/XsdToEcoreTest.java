package eu.artofcoding.emf;

import org.junit.Test;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XsdToEcoreTest {

    @Test
    public void xsdToEcore() throws Exception {
        URI uri = XsdToEcoreTest.class.getResource("/request.xsd").toURI();
        Path xsd = Paths.get(uri);
        Path ecore = Paths.get("request.ecore");
        EModelHelper emh = EModelHelper.eINSTANCE();
        emh.xsdToEcore(xsd, ecore);
    }

}
