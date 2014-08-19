/*
 * beetlejuice
 * beetlejuice-persistence-xml
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 23.03.13 14:22
 */

package eu.artofcoding.beetlejuice.persistence.xml;

import org.base.examples.api.SimpleFSParser;
import org.basex.BaseXServer;
import org.basex.build.Parser;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.*;
import org.basex.server.ClientSession;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BaseXFacade {

    private BaseXServer server;

    private Context serverContext;

    /**
     * Session reference.
     */
    private static ClientSession session;

    public BaseXFacade() {
        serverContext = new Context();
    }

    public Context getServerContext() {
        return serverContext;
    }

    /**
     * Start server on default port 1984.
     * @throws IOException
     */
    public void startServer() throws IOException {
        server = new BaseXServer(serverContext);
        //server.start(1984);
    }

    /**
     * Stop the server.
     * @throws IOException
     */
    public void stopServer() throws IOException {
        if (null == server) {
            throw new IllegalStateException("No server");
        } else {
            server.stop();
        }
    }

    /**
     * Create a client session with host name, port, user name and password.
     * @param host     Hostname or ip address to connect to.
     * @param port     Port number.
     * @param username Username.
     * @param password Password.
     * @throws IOException
     */
    public void connect(String host, int port, String username, String password) throws IOException {
        session = new ClientSession(host, port, username, password);
    }

    /**
     * Close the client session.
     * @throws IOException
     */
    public void disconnect() throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            session.close();
        }
    }

    /**
     * Create a database.
     * @param name
     * @param input
     * @throws IOException
     */
    public void createDatabase(String name, String input) throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            session.execute(new CreateDB(name, input));
        }
    }

    public void createDatabaseFromDirectory(String name, String path) throws BaseXException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            final Parser parser = new SimpleFSParser(path, serverContext.prop);
            final CreateDB create = new CreateDB(name);
            create.setParser(parser);
            create.execute(serverContext);
        }
    }

    public int countElements(String database) throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            session.execute(new Open(database));
            String result = session.execute(new XQuery("fn:count(.)"));
            session.execute(new Close());
            return Integer.valueOf(result);
        }
    }

    public void createDatabaseImportDirectory(String name, String path, String extension) throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            session.execute(new Set("CREATEFILTER", "*" + extension));
            session.execute(new CreateDB(name));
            session.execute(new Add("", path));
            optimizeDatabase();
        }
    }

    /**
     * Close and drop the database.
     * @param name
     * @throws IOException
     */
    public void dropDatabase(String name) throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            session.execute(new DropDB(name));
        }
    }

    /**
     * @param user
     * @param passwordMd5
     * @throws IOException
     */
    public void createUser(String user, String passwordMd5) throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            session.execute(new CreateUser(user, passwordMd5));
        }
    }

    /**
     * Grant rights to a user.
     * @throws IOException
     */
    public void grant() throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            // ------------------------------------------------------------------------
            // Remove global user rights
            System.out.println("\n* Remove global user rights.");
            session.execute(new Grant("NONE", "user"));
            // ------------------------------------------------------------------------
            // Grant local user rights on database 'input'
            System.out.println("\n* Grant local user rights.");
            session.execute(new Grant("WRITE", "user", "input"));
        }
    }

    /**
     * Drop a user.
     * @param name Name of user.
     * @throws IOException
     */
    public void dropUser(String name) throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            session.execute(new DropUser(name));
        }
    }

    /**
     * Open a database.
     * @param name Name of database.
     * @throws IOException
     */
    public void openDatabase(String name) throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            session.execute(new Open(name));
        }
    }

    /**
     * Close actual database.
     * @throws IOException
     */
    public void closeDatabase() throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            session.execute(new Close());
        }
    }

    private void create(String target, String xmlFragment) throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            session.execute(new Add(target, xmlFragment));
        }
    }

    public String read(XQuery xquery) throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            return session.execute(xquery);
        }
    }

    /**
     * Run a query.
     * @param xpath
     * @return
     * @throws IOException
     */
    public String read(String xpath) throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            String execute = session.query(xpath).execute();
            return execute;
        }
    }

    /**
     * Faster version: specify an output stream and run a query.
     * @param xpath
     * @param stream
     * @throws IOException
     */
    public void readToStream(String xpath, OutputStream stream) throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            session.setOutputStream(stream);
            session.query(xpath).execute();
            // Reset output stream
            session.setOutputStream(null);
        }
    }

    public OutputStream readToStream(String xpath) throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            OutputStream stream = new ByteArrayOutputStream();
            readToStream(xpath, stream);
            return stream;
        }
    }

    public void update(XQuery xquery) throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            session.execute(xquery);
        }
    }

    public void delete() throws IOException {
    }

    /**
     * Optimizing database.
     * @throws IOException
     */
    public void optimizeDatabase() throws IOException {
        if (null == session) {
            throw new IllegalStateException("No session");
        } else {
            session.execute(new Optimize());
        }
    }

    public void query(String database, String xquery) throws IOException {
        openDatabase(database);
        String result = read(xquery);
        System.out.println(result);
        closeDatabase();
    }

    /**
     * Runs the example code.
     * @param args (ignored) command-line arguments
     * @throws Exception exception
     */
    public static void main(final String[] args) throws Exception {
        String database = "Ventplan";
        BaseXFacade baseXFacade = new BaseXFacade();
        try {
//            baseXFacade.startServer();
            baseXFacade.connect("localhost", 1984, "admin", "admin");
//            baseXFacade.createDatabaseImportDirectory("Ventplan", "/Users/rbe/Ventplan", ".vpx");
            //
            baseXFacade.openDatabase(database);
            baseXFacade.optimizeDatabase();
            System.out.println("Ventplan has " + baseXFacade.countElements(database) + " documents");
            baseXFacade.closeDatabase();
            //
            baseXFacade.query(database, "for $p in //zentralgerat" +
                    " return <zentralgerat>{$p/name/text()}</zentralgerat>");
            baseXFacade.disconnect();
        } finally {
//            baseXFacade.stopServer();
        }
    }

}
