/*
 * beetlejuice
 * beetlejuice-api
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 25.03.13 08:43
 */

package eu.artofcoding.beetlejuice.helper;

import org.junit.Assert;
import org.junit.Test;

public class GravatarHelperTest {

    @Test
    public void testGetImageURL() throws Exception {
        String url = GravatarHelper.getImageURL("ralf@bensmann.com", 200);
        Assert.assertEquals(url, "http://www.gravatar.com/avatar/8aefabac033f9a1d05af5a7524ac762d.jpg?size=200");
    }

}
