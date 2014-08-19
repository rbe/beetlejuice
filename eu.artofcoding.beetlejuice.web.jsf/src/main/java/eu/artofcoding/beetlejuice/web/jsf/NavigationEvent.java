package eu.artofcoding.beetlejuice.web.jsf;

import java.io.Serializable;

public class NavigationEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String viewId;

    public NavigationEvent(String viewId) {
        this.viewId = viewId;
    }

    public String getViewId() {
        return viewId;
    }

}
