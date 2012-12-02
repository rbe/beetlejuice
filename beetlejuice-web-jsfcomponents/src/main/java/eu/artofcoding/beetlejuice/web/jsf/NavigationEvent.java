package eu.artofcoding.beetlejuice.web.jsf;

import java.io.Serializable;

public class NavigationEvent implements Serializable {

    private final String viewId;

    public NavigationEvent(String viewId) {
        this.viewId = viewId;
    }

    public String getViewId() {
        return viewId;
    }

}
