/*
 * TomcatServer.java
 *
 * Created on 13. Dezember 2006, 17:26
 *
 */

package eu.artofcoding.beetlejuice.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.connector.Connector;

import java.io.File;
import java.net.InetAddress;

public interface TomcatServer {
    
    public void setTomcatHome(File tomcatHome);
    
    public void setWebApplicationHome(File webApplicationHome);

    public void setInetAddress(InetAddress[] inetAdress);

    public void setPort(int port);

    public Host createVirtualHost(String hostName, String relativePath);

    public Host createDefaultVirtualHost(String hostName);

    public Host activateDefaultVirtualHost();

    public Host getDefaultVirtualHost();

    public Host getVirtualHost(String hostName);

    public Context createRootContext(String relativePath);

    public Context activateDefaultRootContext(Host host);

    public Context createManagerContext(String relativePath);

    public Context activateDefaultManagerContext(Host host);

    public Connector createHttpConnector(InetAddress inetAddress, int port) throws TomcatException;
    
    public void initializeTomcat();

    public void startTomcat() throws TomcatException;

    public void stopTomcat() throws TomcatException;

    public Context registerContext(Host host, String tomcatContextRoot, File file) throws DeployException;

    public Context reregisterContext(Host host, String tomcatContextRoot, File file) throws DeployException;
    
    public Context registerRootContext(Host host, File file) throws DeployException;

    public void unregisterContext(Host host, String tomcatContextRoot) throws DeployException;

    public void unregisterRootContext(Host host) throws DeployException;
    
}
