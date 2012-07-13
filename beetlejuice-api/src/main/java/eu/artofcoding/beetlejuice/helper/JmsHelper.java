package eu.artofcoding.beetlejuice.helper;

import javax.jms.*;

/**
 *
 */
public class JmsHelper {

    /**
     * The JMS connection factory, e.g. got from container through @Resource annotation.
     */
    private ConnectionFactory connectionFactory;

    /**
     * The JMS connection.
     */
    private Connection connection;

    /**
     * The JMS session.
     */
    private Session session;

    /**
     * The message producer.
     */
    private MessageProducer messageProducer;

    /**
     * Factory method to create a JmsHelper for a topic. After usage close with {@link #close()}.
     * @param connectionFactory The JMS connection factory, e.g. got from container through @Resource annotation.
     * @param topic The topic, e.g. got from container through @Resource annotation
     * @return A JmsHelper.
     * @throws javax.jms.JMSException
     */
    public static JmsHelper createTopicProducer(ConnectionFactory connectionFactory, Topic topic) throws JMSException {
        JmsHelper jmsHelper = new JmsHelper();
        jmsHelper.connectionFactory = connectionFactory;
        // Connection
        jmsHelper.connection = connectionFactory.createConnection();
        // Producer
        jmsHelper.session = jmsHelper.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        jmsHelper.messageProducer = jmsHelper.session.createProducer(topic);
        // Return helper
        return jmsHelper;
    }

    /**
     * Private constructor, use one of the factory methods.
     */
    private JmsHelper() {
    }

    public Connection getConnection() {
        return connection;
    }

    public Session getSession() {
        return session;
    }

    public MessageProducer getMessageProducer() {
        return messageProducer;
    }

    /**
     * Close a JMS connection.
     */
    public void close() {
        if (null != messageProducer) {
            try {
                messageProducer.close();
            } catch (JMSException e) {
                // ignore
            }
        }
        if (null != session) {
            try {
                session.close();
            } catch (JMSException e) {
                // ignore
            }
        }
        if (null != connection) {
            try {
                connection.close();
            } catch (JMSException e) {
                // ignore
            }
        }
    }

}
