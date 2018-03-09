package com.org.azure.servicebus.sender;

import java.time.Duration;

import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;


public class SessionMessageSender
{
	private static final String connectionString = "secrete_connectionstring_key";
	private static final String queueName = "secrete_queue_name";
	
    public static void main( String[] args ) throws InterruptedException, ServiceBusException 
    {
    	
		ConnectionStringBuilder csb = new ConnectionStringBuilder(connectionString, queueName);
		csb.setOperationTimeout(Duration.ofSeconds(10));
		QueueClient queueClient = new QueueClient(csb, ReceiveMode.PEEKLOCK);
		 
	    int flightCount=3;
        for (int i=0; i<10; i++)
        {
            int iModValue=i%flightCount;
            Message message = new Message("Test message " + i +" Session-"+iModValue);
            message.setSessionId("Session"+iModValue);
            queueClient.sendAsync(message);
        }
        
        System.out.println("Message published successfully");
    }
}
