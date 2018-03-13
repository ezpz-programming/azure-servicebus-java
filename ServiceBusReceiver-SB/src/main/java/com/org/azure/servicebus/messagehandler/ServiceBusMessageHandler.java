package com.org.azure.servicebus.messagehandler;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import com.microsoft.azure.servicebus.*;
import org.springframework.stereotype.Component;

@Component
public class ServiceBusMessageHandler implements ISessionHandler
{
	@Override
	public void notifyException(Throwable exception, ExceptionPhase phase)
	{
        System.out.println(phase + " encountered exception:" + exception.getMessage());
    }

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
	
}