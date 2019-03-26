package io.openmessaging.journalq.consumer.support;

import com.jd.journalq.client.internal.consumer.MessageListener;
import com.jd.journalq.client.internal.consumer.domain.ConsumeMessage;
import com.jd.journalq.client.internal.consumer.exception.IgnoreAckException;
import io.openmessaging.journalq.consumer.message.MessageConverter;
import io.openmessaging.message.Message;

/**
 * MessageListenerAdapter
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2019/2/20
 */
public class MessageListenerAdapter implements MessageListener {

    private io.openmessaging.consumer.MessageListener omsMessageListener;

    public MessageListenerAdapter(io.openmessaging.consumer.MessageListener omsMessageListener) {
        this.omsMessageListener = omsMessageListener;
    }

    @Override
    public void onMessage(ConsumeMessage message) {
        MessageListenerContextAdapter context = new MessageListenerContextAdapter();
        Message omsMessage = MessageConverter.convertMessage(message);
        omsMessageListener.onReceived(omsMessage, context);

        if (!context.isAck()) {
            throw new IgnoreAckException();
        }
    }

    @Override
    public int hashCode() {
        return omsMessageListener.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return omsMessageListener.equals(obj);
    }

    @Override
    public String toString() {
        return omsMessageListener.toString();
    }
}