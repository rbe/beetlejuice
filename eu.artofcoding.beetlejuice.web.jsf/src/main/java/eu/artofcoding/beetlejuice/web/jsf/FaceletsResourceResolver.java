package eu.artofcoding.beetlejuice.web.jsf;

import javax.faces.view.facelets.ResourceResolver;
import java.net.URL;

public class FaceletsResourceResolver extends ResourceResolver {

    private ResourceResolver parent;

    private String basePath;

    public FaceletsResourceResolver(ResourceResolver parent) {
        this.parent = parent;
        this.basePath = "/META-INF/resources";
    }

    @Override
    public URL resolveUrl(String path) {
        // Resolves from war
        URL url = parent.resolveUrl(path);
        if (url == null) {
            // Resolves from jar
            url = getClass().getResource(basePath + path);
        }
        return url;
    }

}
