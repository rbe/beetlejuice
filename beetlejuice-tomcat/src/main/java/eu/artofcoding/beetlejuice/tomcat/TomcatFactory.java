/*
 * TomcatFactory.java
 *
 * Created on 13. Dezember 2006, 17:26
 *
 */

package eu.artofcoding.beetlejuice.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.Host;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TomcatFactory {

    /**
     * Home directory of Tomcat installation
     */
    private static File tomcatHome;

    private static InetAddress inetAddress;

    private static int port = 8080;

    static {
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new instance of TomcatFactory
     */
    private TomcatFactory() {
    }

    public static void setTomcatHome(File tomcatHome) {
        TomcatFactory.tomcatHome = tomcatHome;
    }

    public static TomcatServer createMinimalTomcatServer(Class tomcatServerClass, File tomcatHome) throws UnknownHostException, TomcatException {
        TomcatServer tomcatServer = null;
        try {
            tomcatServer = (TomcatServer) tomcatServerClass.newInstance();
            tomcatServer.setTomcatHome(tomcatHome);
            tomcatServer.setWebApplicationHome(new File(tomcatHome, "/webapps"));
            tomcatServer.setInetAddress(new InetAddress[]{inetAddress});
            tomcatServer.setPort(port);
            tomcatServer.initializeTomcat();
            tomcatServer.activateDefaultVirtualHost();
        } catch (InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return tomcatServer;
    }

    public static TomcatServer createMinimalTomcatServer(Class tomcatServerClass, InetAddress inetAddress, File tomcatHome) throws UnknownHostException, TomcatException {
        TomcatFactory.inetAddress = inetAddress;
        return createMinimalTomcatServer(tomcatServerClass, tomcatHome);
    }

    public static TomcatServer createMinimalTomcatServer(Class tomcatServerClass, InetAddress inetAddress, int port, File tomcatHome) throws UnknownHostException, TomcatException {
        TomcatServer tomcatServer = null;
        TomcatFactory.inetAddress = inetAddress;
        TomcatFactory.port = port;
        return createMinimalTomcatServer(tomcatServerClass, tomcatHome);
    }

    public static TomcatServer createStandardTomcatServer(Class tomcatServerClass, InetAddress inetAddress, File tomcatHome) throws UnknownHostException, TomcatException {
        TomcatFactory.inetAddress = inetAddress;
        TomcatServer tomcatServer = createMinimalTomcatServer(tomcatServerClass, tomcatHome);
        Host defaultVirtualHost = tomcatServer.getDefaultVirtualHost();
        tomcatServer.activateDefaultRootContext(defaultVirtualHost);
        tomcatServer.activateDefaultManagerContext(defaultVirtualHost);
        return tomcatServer;
    }

    public static TomcatServer createStandardTomcatServer(Class tomcatServerClass, InetAddress inetAddress, int port, File tomcatHome) throws UnknownHostException, TomcatException {
        TomcatFactory.inetAddress = inetAddress;
        TomcatFactory.port = port;
        TomcatServer tomcatServer = createMinimalTomcatServer(tomcatServerClass, tomcatHome);
        Host defaultVirtualHost = tomcatServer.activateDefaultVirtualHost();
        tomcatServer.activateDefaultRootContext(defaultVirtualHost);
        tomcatServer.activateDefaultManagerContext(defaultVirtualHost);
        return tomcatServer;
    }

    public static TomcatServer createMinimalTomcat55ServerOnLocalhost(int port, File tomcatHome) throws UnknownHostException, TomcatException {
        return createMinimalTomcatServer(Tomcat55.class, InetAddress.getLocalHost(), port, tomcatHome);
    }

    public static TomcatServer createMinimalTomcat55ServerOnLocalhost(File tomcatHome) throws UnknownHostException, TomcatException {
        return createMinimalTomcatServer(Tomcat55.class, tomcatHome);
    }

    public static void main(String[] args) throws UnknownHostException, TomcatException {
        File tomcatHome = new File("C:/workspace/netbeans/Apache/apache-tomcat-5.5.20-embed");
        TomcatServer t = TomcatFactory.createMinimalTomcat55ServerOnLocalhost(tomcatHome);
        Host h = t.getDefaultVirtualHost();
        t.startTomcat();
        File rootFile = new File(tomcatHome, "/webapps/ROOT");
        Context ctx = t.registerRootContext(h, rootFile);
//        ErrorPage error404 = new ErrorPage();
//        error404.setErrorCode("404");
//        error404.setErrorCode(404);
//        error404.setLocation("/blabla");
//        ctx.addErrorPage(error404);
    }

}
