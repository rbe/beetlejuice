package eu.artofcoding.emf;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.emf.mapping.xsd2ecore.util.XSD2EcoreResourceFactoryImpl;
import org.eclipse.xsd.ecore.XSDEcoreBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

public final class EModelHelper {

    private static final EModelHelper E_HELPER = new EModelHelper();

    private final EcoreFactory eCoreFactory = EcoreFactory.eINSTANCE;

    private EModelHelper() {
    }

    public static EModelHelper eINSTANCE() {
        return E_HELPER;
    }

    public EPackage createEPackage(String name, String namespacePrefix, String namespaceUri) {
        EPackage ePackage = eCoreFactory.createEPackage();
        ePackage.setName(name);
        ePackage.setNsPrefix(namespacePrefix);
        ePackage.setNsURI(namespaceUri);
        return ePackage;
    }

    public void addEClassifier(EPackage ePackage, EClass... eClasses) {
        for (EClass eClass : eClasses) {
            ePackage.getEClassifiers().add(eClass);
        }
    }

    public EClass createEClass(String name) {
        EClass eClass = eCoreFactory.createEClass();
        eClass.setName(name);
        return eClass;
    }

    public void addEStructuralFeature(EClass eClass, EStructuralFeature... eStructuralFeature) {
        for (EStructuralFeature feature : eStructuralFeature) {
            eClass.getEStructuralFeatures().add(feature);
        }
    }

    public EAttribute createEAttribute(String name, EClassifier eType) {
        EAttribute eAttribute = eCoreFactory.createEAttribute();
        eAttribute.setName(name);
        eAttribute.setEType(eType);
        return eAttribute;
    }

    public EAttribute createEAttribute(EObject eObject, String name, Object value) {
        EClass eClass = eObject.eClass();
        EAttribute eAttribute = (EAttribute) eClass.getEStructuralFeature(name);
        eObject.eSet(eAttribute, value);
        return eAttribute;
    }

    public EReference createEReference(String name, EClassifier eClassifier) {
        EReference eReference = eCoreFactory.createEReference();
        eReference.setName(name);
        eReference.setEType(eClassifier);
        return eReference;
    }

    /*
    public void setupAndSaveEMFInstanceResource() {
        ResourceSet rs = new ResourceSetImpl();
        // Here the resource is created, with fileextensions "gast" and "xml" (adapt this to use your own file extension).
        Resource gastResource = createAndAddResource("C:/file.gast", new String[]{"gast", "xml"}, rs);
        // The root object is created by using (adapt this to create your own root object)
        Root root = coreFactory.eINSTANCE.createRoot();
        gastResource.getContents().add(root);
        saveResource(gastResource);
    }

    public Resource createAndAddResource(ResourceSet resourceSet, String outputFile, String[] extensions) {
        for (String ext : extensions) {
            resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(ext, new XMLResourceFactoryImpl());
        }
        URI uri = URI.createFileURI(outputFile);
        Resource resource = resourceSet.createResource(uri);
        ((ResourceImpl) resource).setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());
        return resource;
    }

    public void saveResource(Resource resource) {
        Map<Object, Object> options = ((XMLResource) resource).getDefaultSaveOptions();
        options.put(XMLResource.OPTION_CONFIGURATION_CACHE, Boolean.TRUE);
        options.put(XMLResource.OPTION_USE_CACHED_LOOKUP_TABLE, new ArrayList());
        try {
            resource.save(options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    */

    public void save(EPackage ePackage, Path path) {
        ResourceSet metaResourceSet = new ResourceSetImpl();
        // Register XML Factory implementation to handle .ecore files
        metaResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMLResourceFactoryImpl());
        // Create empty resource with the given URI
        URI uri = URI.createURI(path.toUri().toString());
        Resource metaResource = metaResourceSet.createResource(uri);
        // Add bookStoreEPackage to contents list of the resource 
        metaResource.getContents().add(ePackage);
        try {
            metaResource.save(Collections.EMPTY_MAP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void xsdToEcore(Path sourcename, Path targetname) {
        ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XSD2EcoreResourceFactoryImpl());
        XSDEcoreBuilder xsdEcoreBuilder = new XSDEcoreBuilder();
        URI xsdFileUri = URI.createFileURI(sourcename.toUri().toString().replaceAll("file://", ""));
        Collection<EObject> eCorePackages = xsdEcoreBuilder.generate(xsdFileUri);
        URI ecoreFileUri = URI.createFileURI(targetname.toUri().toString());
        Resource resource = resourceSet.createResource(ecoreFileUri);
        for (EObject eCorePackage : eCorePackages) {
            EPackage element = (EPackage) eCorePackage;
            EList<EObject> contents = resource.getContents();
            contents.add(element);
        }
        try {
            resource.save(Collections.EMPTY_MAP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
