/*
 * beetlejuice
 * beetlejuice-osgi
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 28.12.12 09:08
 */

package eu.artofcoding.beetlejuice.osgi;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleReference;
import org.osgi.framework.ServiceReference;

public class OsgiHelper {

    public static <T> T getService(Class<T> type) {
        BundleContext ctx = BundleReference.class.cast(type.getClassLoader()).getBundle().getBundleContext();
        ServiceReference<T> ref = ctx.getServiceReference(type);
        return ref != null ? type.cast(ctx.getService(ref)) : null;
    }

}
