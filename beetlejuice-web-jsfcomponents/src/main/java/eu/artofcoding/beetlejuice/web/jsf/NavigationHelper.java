/*
 * beetlejuice
 * beetlejuice-web-jsfcomponents
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 15.02.13 18:36
 */

package eu.artofcoding.beetlejuice.web.jsf;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;

@Named("nav")
//@SessionScoped
public class NavigationHelper implements Serializable {


    private transient Logger logger;

//    @Inject
//    private Event<NavigationEvent> events;

    private String[] views = {};

    private int actualViewIndex = -1;

    public NavigationHelper() {
        logger = Logger.getLogger(NavigationHelper.class.getName());
    }

    public String[] getViews() {
        return views;
    }

    public void setViews(String[] views) {
        this.views = views;
    }

    public int getActualViewIndex() {
        return actualViewIndex;
    }

    public String getActualViewId() {
        return views[actualViewIndex];
    }

    /**
     * Find index of actual Faces' view id in views array. Sets this.actualViewIndex.
     * Use it e.g. in JSF pre-render-view event like this:
     * <p>
     * &lt;f:metadata><br/>
     * &lt;f:event type="preRenderView" listener="#{bean.preRenderView}"/><br/>
     * &lt;/f:metadata>
     * </p>
     */
    public void findPageIndexByViewId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String _viewId = facesContext.getViewRoot().getViewId();
        String[] splitByXhtml = _viewId.split(".xhtml");
        String[] splitBySlash = splitByXhtml[0].split("/");
        String viewId = splitBySlash[splitBySlash.length - 1];
        for (int i = 0; i < views.length; i++) {
            String p = views[i];
            if (p.equals(viewId)) {
                actualViewIndex = i;
                break;
            }
        }
        logger.info(String.format("viewId=%s actualViewIndex=%d", viewId, actualViewIndex));
    }

    public String previousPage() {
        actualViewIndex--;
        if (actualViewIndex < 0) {
            actualViewIndex = 0;
        }
        String outcome = views[actualViewIndex];
        // TODO Execute action depending on navigation case
        logger.info(String.format("Navigating to page %s", outcome));
//        // Fire NavigationEvent
//        events.fire(new NavigationEvent(outcome));
        return outcome;
    }

    public String nextPage() {
        actualViewIndex++;
        if (actualViewIndex > views.length) {
            actualViewIndex--;
        }
        String outcome = views[actualViewIndex];
        // TODO Execute action depending on navigation case
        logger.info(String.format("Navigating to page %s", outcome));
//        // Fire NavigationEvent
//        events.fire(new NavigationEvent(outcome));
        return outcome;
    }

}
