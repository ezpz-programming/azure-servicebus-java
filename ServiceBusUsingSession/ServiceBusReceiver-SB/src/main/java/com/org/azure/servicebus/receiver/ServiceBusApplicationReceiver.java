package com.org.azure.servicebus.receiver;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.SessionHandlerOptions;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import com.org.azure.servicebus.messagehandler.ServiceBusMessageHandler;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;


@SpringBootApplication
public class ServiceBusApplicationReceiver implements CommandLineRunner 
{
	
	private QueueClient queueClient;

    public ServiceBusApplicationReceiver(QueueClient queueClient) 
    {
        this.queueClient = queueClient;
    }

    public static void main(String[] args) 
    {
        SpringApplication.run(ServiceBusApplicationReceiver.class);
    }

    public void run(String... var1) throws ServiceBusException, InterruptedException 
    {
		int maxSession=1;
		boolean autoComplete=false;
		Duration dur=Duration.ofSeconds(300);
		queueClient.registerSessionHandler(new ServiceBusMessageHandler(),new SessionHandlerOptions(maxSession, autoComplete, dur));       
		System.out.println("Timestamp: " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + " *** Session Handler Registered...");
		
    }

}

