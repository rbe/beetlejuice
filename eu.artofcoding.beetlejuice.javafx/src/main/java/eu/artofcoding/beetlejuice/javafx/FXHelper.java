/*
 * beetlejuice
 * beetlejuice-email
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 08.12.12 14:04
 */

package eu.artofcoding.beetlejuice.javafx;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.util.Duration;

import java.io.IOException;

public class FXHelper {

    public static FXMLLoader getLoader(String... name) {
        String url = String.format("/fxml/%s.fxml", name[0]);
        return new FXMLLoader(FXHelper.class.getResource(url));
    }

    public static Parent getPanel(String... name) throws IOException {
        String url = String.format("/fxml/%s.fxml", name[0]);
        return FXMLLoader.load(FXHelper.class.getResource(url));
    }

    /**
     * Fade component in.
     * @param node {@link Node}.
     * @param duration Duration for effect in milliseconds.
     */
    public static void fadeIn(Node node, int duration) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), node);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    /**
     * Fade component out.
     * @param node {@link Node}.
     * @param duration Duration for effect in milliseconds.
     */
    public static void fadeOut(Node node, int duration) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), node);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }

}
