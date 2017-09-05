import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Scanner;

/**
 * Created by desenvolverdor on 04/09/17.
 */
public class Sender {

    //private static String subject = "SomSub"; // Queue Name.You can create any/many queue names as per your requirement.

    public void Sender(String subject, String msg) throws JMSException {
        // Getting JMS connection from the server and starting it
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://10.180.21.102:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();

        //Creating a non transactional session to send/receive JMS message.
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        //Destination represents here our queue 'JCG_QUEUE' on the JMS server.
        //The queue will be created automatically on the server.
        Destination destination = session.createQueue(subject);

        // MessageProducer is used for sending messages to the queue.
        MessageProducer producer = session.createProducer(destination);


        // We will send a small text message saying 'Hello World!!!'
        TextMessage message = session
                .createTextMessage(msg);

        // Here we are sending our message!
        producer.send(message);

        System.out.println("JCG printing@@ '" + message.getText() + "'");
        connection.close();
    }
}
