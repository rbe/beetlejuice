package eu.artofcoding.emf;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.AESCipherImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class EncryptedXMIResourceFactory extends XMIResourceFactoryImpl {

    private final String secret;

    public EncryptedXMIResourceFactory(String secret) {
        this.secret = secret;
    }

    @Override
    public Resource createResource(URI uri) {
        XMIResourceFactoryImpl resFactory = new XMIResourceFactoryImpl();
        XMIResource resource = (XMIResource) resFactory.createResource(uri);
        try {
            resource.getDefaultLoadOptions().put(Resource.OPTION_CIPHER, new AESCipherImpl(secret));
            resource.getDefaultSaveOptions().put(Resource.OPTION_CIPHER, new AESCipherImpl(secret));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resource;
    }

}
