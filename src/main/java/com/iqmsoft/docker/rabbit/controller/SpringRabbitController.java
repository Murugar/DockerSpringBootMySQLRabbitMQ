
package com.iqmsoft.docker.rabbit.controller;


import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringRabbitController {


    
    private final static String QUEUE_NAME = "hello";
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://mysql:3306/mysql?autoReconnect=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASS = "123456";

    @RequestMapping(value = "/mockservice", method = RequestMethod.GET)
    public String index() {
        return "This is a mock service!!";
    }

    @RequestMapping(value = "/rabbitMQSendTest", method = RequestMethod.GET)
    public String rabbitMQSendTest(@RequestParam(value = "message", defaultValue = "Hello World!") String message) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("rabbitmq");
            factory.setPort(5672);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");

            channel.close();
            connection.close();

            return "rabbitMQSendTest Sent: " + message;
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(SpringRabbitController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "rabbitMQSendTest has been failed!!!";
    }

    @RequestMapping(value = "/rabbitMQReceiveTest", method = RequestMethod.GET)
    public String rabbitMQReceiveTest() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("rabbitmq");
            factory.setPort(5672);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                }
            };
            channel.basicConsume(QUEUE_NAME, true, consumer);

        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(SpringRabbitController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "Getting messages from RabbitMQ!!";
    }

    @RequestMapping(value = "/mySQLTest", method = RequestMethod.GET)
    public String mySQLTest() {
        java.sql.Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql = "SELECT User FROM user";
            ResultSet rs = stmt.executeQuery(sql);
            StringBuilder response = new StringBuilder("");

            while (rs.next()) {
                String user = rs.getString("User");
                response.append("\nUser: " + user);
                System.out.print("\nUser: " + user);
            }

            rs.close();
            stmt.close();
            conn.close();

            return response.toString();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return "mySQLTest has been failed!!";
    }

   


}
