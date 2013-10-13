/*
 * DeployException.java
 *
 * Created on 13. Dezember 2006, 18:00
 *
 */

package eu.artofcoding.beetlejuice.tomcat;

public class DeployException extends TomcatException {

    public DeployException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DeployException(String message) {
        super(message, null);
    }

}
