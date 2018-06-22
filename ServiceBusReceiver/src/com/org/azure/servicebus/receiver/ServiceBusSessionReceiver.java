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

public class ServiceBusSessionReceiver {

	private static final String connectionString = "xxx";
	private static String entityName = "xxx";
	private static final boolean usingQueue = false;

	public static void main(String[] args) throws InterruptedException, ServiceBusException, IOException {
		System.out.println("Press any key to stop receiver ...");

		// configure session handler
		if (usingQueue) {
			ConnectionStringBuilder csb = new ConnectionStringBuilder(connectionString, entityName);
			csb.setOperationTimeout(Duration.ofSeconds(300));
			QueueClient queueClient = new QueueClient(csb, ReceiveMode.PEEKLOCK);

			queueClient.registerSessionHandler(new SessionHandler(),
					new SessionHandlerOptions(1, 1, false, Duration.ofSeconds(300), Duration.ofSeconds(20)));

		} else {
			entityName = entityName + "//subscriptions//DomainEvents";

			ConnectionStringBuilder csb = new ConnectionStringBuilder(connectionString, entityName);
			csb.setOperationTimeout(Duration.ofSeconds(300));
			SubscriptionClient subscriptionClient = new SubscriptionClient(csb, ReceiveMode.PEEKLOCK);

			subscriptionClient.registerSessionHandler(new SessionHandler(),
					new SessionHandlerOptions(3, 1, false, Duration.ofSeconds(300), Duration.ofSeconds(20)));

		}

		System.out.println("Timestamp: " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())
				+ " *** Session Handler Registered...");

		System.in.read();
	}

	static class SessionHandler implements ISessionHandler {

		@Override
		public CompletableFuture<Void> onMessageAsync(IMessageSession session, IMessage message) {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String messageString = new String(message.getBody(), StandardCharsets.UTF_8);
			System.out.println("Timestamp: " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())
					+ " Processing completed for message: " + messageString + " for SessionId: " + message.getSessionId());
			return session.completeAsync(message.getLockToken());

		}

		@Override
		public CompletableFuture<Void> OnCloseSessionAsync(IMessageSession session) {
			System.out.println("Timestamp: " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())
					+ " *** Session closed - " + session.getSessionId() + "\n");
			return CompletableFuture.completedFuture(null);

		}

		@Override
		public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
			System.out.println(exceptionPhase + "-" + throwable.getMessage());
		}

	}

}
