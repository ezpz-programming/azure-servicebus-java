package com.org.azure.servicebus.sender;

import java.time.Duration;

import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

public class SessionMessageSender {
	private static final String connectionString = "xxx";
	private static final String entityName = "xxx";
	private static final boolean usingQueue = false;
	private static final int totalMsgCount = 10;

	public static void main(String[] args) throws InterruptedException, ServiceBusException {

		ConnectionStringBuilder csb = new ConnectionStringBuilder(connectionString, entityName);
		csb.setOperationTimeout(Duration.ofSeconds(10));
		if (usingQueue) {
			QueueClient queueClient = new QueueClient(csb, ReceiveMode.PEEKLOCK);

			int flightCount = 3;
			for (int i = 0; i < totalMsgCount; i++) {
				int iModValue = i % flightCount;
				Message message = new Message("Test message " + i + " Session-" + iModValue);
				message.setSessionId("Session" + iModValue);
				queueClient.sendAsync(message);
			}
		} else
		{
			TopicClient topicClient = new TopicClient(csb);

			int flightCount = 3;
			for (int i = 0; i < totalMsgCount; i++) {
				int iModValue = i % flightCount;
				Message message = new Message("Test message " + i + " Session-" + iModValue);
				message.setSessionId("Session" + iModValue);
				topicClient.sendAsync(message);
			}
		}
		
		System.out.println("Message published successfully");
	}
}
