package com.org.azure.servicebus.receiver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

public class ServiceBusSessionReceiver 
{

    private static final String connectionString = "secrete_connectionstring_key";
    private static final String queueName = "secrete_queue_name";
    
	public static void main(String[] args) throws InterruptedException, ServiceBusException, IOException 
	{
		System.out.println("Press any key to sop receiver ...");

		// create client
		ConnectionStringBuilder csb = new ConnectionStringBuilder(connectionString, queueName);
		csb.setOperationTimeout(Duration.ofSeconds(5));
		QueueClient queueClient = new QueueClient(csb, ReceiveMode.PEEKLOCK);
		
		// configure session handler
		int maxSession=1;
		boolean autoComplete=false;
		Duration dur=Duration.ofSeconds(300);
		queueClient.registerSessionHandler(new SessionHandler(),new SessionHandlerOptions(maxSession, autoComplete, dur));
		System.out.println("Timestamp: " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + " *** Session Handler Registered...");
		
		System.in.read();
		System.out.println("Closing queue client.");
		queueClient.close();
	}
	
	static class SessionHandler implements ISessionHandler 
	{

        @Override
	    	public CompletableFuture<Void> onMessageAsync(IMessageSession session, IMessage message) 
	    	{
	    		String messageString = new String(message.getBody(), StandardCharsets.UTF_8);
	    		System.out.println("Received message: " + messageString + " for SessionId: " + message.getSessionId());
	    		return session.completeAsync(message.getLockToken());
	
	    	}
	
	    	@Override
	    	public CompletableFuture<Void> OnCloseSessionAsync(IMessageSession session) 
	    	{
	    		System.out.println("Timestamp: " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + " *** Session closed - " + session.getSessionId() + "\n");
	    		return session.closeAsync();
	
	    	}
	    	
        @Override
        public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
        		System.out.println(exceptionPhase + "-" + throwable.getMessage());
        }
        

    }

}
