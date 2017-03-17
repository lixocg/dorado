package com.experian.comp.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class StringConsumer implements Consumer {
	private ConsumerHandler handler;

	public StringConsumer(ConsumerHandler handler) {
		this.handler = handler;
	}

	public ConsumerHandler getHandler() {
		return handler;
	}

	public void setHandler(ConsumerHandler handler) {
		this.handler = handler;
	}

	@Override
	public void handleConsumeOk(String consumerTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleCancelOk(String consumerTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties arg2, byte[] arg3) throws IOException {
		handler.handle(new String(arg3));
	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleRecoverOk(String consumerTag) {
		// TODO Auto-generated method stub

	}

}
