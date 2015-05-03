package eu.artofcoding.emf;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class EInstanceHelper {

    private static final Map<EFactory, EInstanceHelper> eMAP = new ConcurrentHashMap<>();

    private final EFactory eFactory;

    private EInstanceHelper(final EFactory eFactory) {
        this.eFactory = eFactory;
    }

    public static synchronized EInstanceHelper eINSTANCE(final EFactory eFactory) {
        if (null != eFactory) {
            EInstanceHelper eHelper = eMAP.get(eFactory);
            if (null == eHelper) {
                eHelper = new EInstanceHelper(eFactory);
                eMAP.put(eFactory, eHelper);
            }
            return eHelper;
        } else {
            throw new IllegalArgumentException("No EFactory");
        }
    }

    public EObject createEObject(EPackage ePackage, String classifier) {
        final EClass eClass = (EClass) ePackage.getEClassifier(classifier);
        return eFactory.create(eClass);
    }

    public EAttribute createEAttribute(EObject eObject, String name, Object value) {
        final EClass eClass = eObject.eClass();
        final EAttribute eAttribute = (EAttribute) eClass.getEStructuralFeature(name);
        eObject.eSet(eAttribute, value);
        return eAttribute;
    }

    public EAttribute getEAttribute(EObject eObject, String name) {
        final EClass eClass = eObject.eClass();
        return (EAttribute) eClass.getEStructuralFeature(name);
    }

    public <T> T getEAttributeValue(EObject eObject, String name, Class<T> klass) {
        final EClass eClass = eObject.eClass();
        final EAttribute feature = (EAttribute) eClass.getEStructuralFeature(name);
        final Object value = eObject.eGet(feature);
        return klass.cast(value);
    }

    public EObject load(String ePackageURL, EPackage ePackage, Path path) {
        ResourceSet resourceSet = new ResourceSetImpl();
        // Register XML Factory implementation using DEFAULT_EXTENSION
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMLResourceFactoryImpl());
        // Add EPackage to package registry
        resourceSet.getPackageRegistry().put(ePackageURL, ePackage);
        // Load resource by URI
        URI fileUri = URI.createURI(path.toUri().toString());
        Resource resource = resourceSet.getResource(fileUri, true);
        // Read serialized instance
        return resource.getContents().get(0);
    }

    public void save(EObject eObject, Path path) {
        final ResourceSet resourceSet = new ResourceSetImpl();
        // Register XML Factory implementation using DEFAULT_EXTENSION
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMLResourceFactoryImpl());
        // Create empty resource with the given URI
        final URI uri = URI.createURI(path.toUri().toString());
        final Resource resource = resourceSet.createResource(uri);
        // Add bookStoreObject to contents list of the resource 
        resource.getContents().add(eObject);
        // Save the resource using OPTION_SCHEMA_LOCATION save option to produce 
        // xsi:schemaLocation attribute in the document
        final Map<Object, Object> options = new HashMap<>();
        options.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);
        try {
            resource.save(options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
