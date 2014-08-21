/*
 * AbstractTomcatServer.java
 *
 * Created on 15. Dezember 2006, 13:44
 *
 */

package eu.artofcoding.beetlejuice.tomcat;

import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

public class AbstractTomcatServer implements TomcatServer {

    private File tomcatHome;

    private int port;

    private InetAddress[] inetAddress;

    private File webApplicationHome;

    private Tomcat tomcat;

    private Engine engine;

    private Host defaultVirtualHost;

    private List<Host> virtualHost;

    private Context rootContext;

    private Context managerContext;

    /**
     * Creates a new instance of AbstractTomcatServer.
     */
    public AbstractTomcatServer() {
        virtualHost = new LinkedList<Host>();
    }

    public Context activateDefaultManagerContext(Host host) {
        createManagerContext("/webapps/manager");
        host.addChild(managerContext);
        return managerContext;
    }

    public Context activateDefaultRootContext(Host host) {
        rootContext = createDefaultRootContext();
        host.addChild(rootContext);
        return rootContext;
    }

    public Host activateDefaultVirtualHost() {
        defaultVirtualHost = createDefaultVirtualHost("localhost");
        engine.addChild(defaultVirtualHost);
        return defaultVirtualHost;
    }

    protected Context createDefaultRootContext() {
        return createRootContext("/webapps/ROOT");
    }

    public Host createDefaultVirtualHost(String hostName) {
        return createVirtualHost(hostName, "/webapps");
    }

    /**
     * Create "Tomcat manager" application Context
     * @param relativePath
     * @return
     */
    public Context createManagerContext(String relativePath) {
/*
        managerContext = tomcat.createContext("/manager", tomcatHome.getAbsolutePath() + relativePath);
        managerContext.setPrivileged(true);
*/
        return managerContext;

    }

    public Context createRootContext(String relativePath) {
        Context rootContext = null;
/*
        rootContext = tomcat.createContext("", tomcatHome.getAbsolutePath() + relativePath);
        rootContext.setReloadable(false);
        rootContext.addWelcomeFile("index.jsp");
*/
        return rootContext;
    }

    /**
     * Create a virtual host
     * @return
     */
    public Host createVirtualHost(String hostName, String relativePath) {
        Host host = null;
/*
        host = tomcat.createHost(hostName, tomcatHome.getAbsolutePath() + relativePath);
        virtualHost.add(host);
*/
        return host;
    }

    public Host getDefaultVirtualHost() {
        return defaultVirtualHost;
    }

    public Host getVirtualHost(String hostName) {
        Host foundHost = null;
        if (defaultVirtualHost.getName().equals(hostName)) {
            foundHost = defaultVirtualHost;
        } else {
            for (Host host : virtualHost) {
                if (host.getName().equals(hostName)) {
                    foundHost = host;
                    break;
                }
            }
        }
        return foundHost;
    }

    public void initializeTomcat() {
/*
        // Create an tomcat server
        tomcat = new Tomcat();
        tomcat.setCatalinaHome(tomcatHome.getAbsolutePath());
        // Set the memory realm
        MemoryRealm memoryRealm = new MemoryRealm();
        tomcat.setRealm(memoryRealm);
        // Create an engine
        engine = tomcat.createEngine();
        engine.setDefaultHost("localhost");
        // Install the assembled container hierarchy
        tomcat.addEngine(engine);
*/
    }

    /**
     * Registers a WAR with the container.
     * @param host
     * @param file
     * @param contextPath the context path under which the application will be registered
     * @return
     */
    public Context registerContext(Host host, String contextPath, File file) throws DeployException {
        Context context = null;
/*
        context = tomcat.createContext(contextPath, file.getAbsolutePath());
        context.setReloadable(false);
        try {
            host.addChild(context);
        } catch (IllegalArgumentException e) {
            throw new DeployException(String.format("Cannot register context '%s' (already deployed?)", contextPath), e);
        }
*/
        return context;
    }

    /**
     * Registers a WAR with the container.
     * @param host
     * @param file
     * @param contextPath the context path under which the application will be registered
     * @return
     */
    public Context reregisterContext(Host host, String contextPath, File file) throws DeployException {
        unregisterContext(host, contextPath);
        Context context = registerContext(host, contextPath, file);
        return context;
    }

    public Context registerRootContext(Host host, File file) throws DeployException {
        return registerContext(host, "", file);
    }

    public void unregisterRootContext(Host host) throws DeployException {
        unregisterContext(host, "");
    }

    /**
     * Unregisters a WAR from the web server.
     * @param contextPath the context path to be removed
     */
    public void unregisterContext(Host host, String contextPath) throws DeployException {
/*
        Context context = host.map(contextPath);
        if (context != null) {
            tomcat.removeContext(context);
        } else {
            throw new DeployException(String.format("Context %s does not exist", contextPath));
        }
*/
    }

    /**
     * Set Session scope variable
     * @param name Session variable name
     * @param obj  Session variable value
     */
    public void setRootContextSessionAttribute(String name, Object obj) {
        Session sessions[] = rootContext.getManager().findSessions();
        for (int i = 0, size = sessions.length; i < size; i++) {
            sessions[i].getSession().setAttribute(name, obj);
        }
    }

    /**
     * Set Application scope variable
     * @param name Application variable name
     * @param obj  Application variable value
     */
    public void setRootContextAttribute(String name, Object obj) {
        rootContext.getServletContext().setAttribute(name, obj);
    }

    /**
     * Get Application scope variable
     * @param name Application variable name
     * @return Application variable value
     */
    public Object getRootContextAttribute(String name) {
        return rootContext.getServletContext().getAttribute(name);
    }

    /**
     * Remove Application scope variable
     * @param name Application variable name
     */
    public void removeRootContextAttribute(String name) {
        rootContext.getServletContext().removeAttribute(name);
    }

    public void setInetAddress(InetAddress[] inetAddress) {
        this.inetAddress = inetAddress;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setTomcatHome(File tomcatHome) {
        this.tomcatHome = tomcatHome;
    }

    public void setWebApplicationHome(File webApplicationHome) {
        this.webApplicationHome = webApplicationHome;
    }

    public Connector createHttpConnector(InetAddress inetAddress, int port)
            throws TomcatException {
        Connector connector = null;
        //        tomcat.createConnector(...)
        //        seems to be broken.. it always returns a null connector.
        //        see work around below
        //        tomcat.createConnector(addr, port, false);
        try {
            connector = new Connector();
            connector.setScheme("http");
            connector.setSecure(false);
            connector.setProperty("address", inetAddress.getHostAddress());
            connector.setProperty("port", "" + port);
            connector.setEnableLookups(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TomcatException(ex.getMessage(), ex);
        }
        return connector;
    }

    /**
     * This method Starts the Tomcat server.
     * @throws TomcatException
     */
    public void startTomcat() throws TomcatException {
        Connector connector = createHttpConnector(inetAddress[0], port);
        try {
            // Add connector to tomcat server
            // TODO tomcat.addConnector(connector);
            // Start the tomcat server
            tomcat.start();
        } catch (LifecycleException ex) {
            ex.printStackTrace();
            throw new TomcatException("Could not start Tomcat", ex);
        }
    }

    /**
     * This method stops the Tomcat server.
     */
    public void stopTomcat() throws TomcatException {
        try {
            tomcat.stop();
        } catch (LifecycleException ex) {
            ex.printStackTrace();
            throw new TomcatException(ex.getMessage(), ex);
        }
    }

}
