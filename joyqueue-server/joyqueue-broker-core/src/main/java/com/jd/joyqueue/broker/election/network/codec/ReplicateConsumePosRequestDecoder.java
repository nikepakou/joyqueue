/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jd.joyqueue.broker.election.network.codec;

import com.jd.joyqueue.broker.election.command.ReplicateConsumePosRequest;
import com.jd.joyqueue.network.transport.codec.JoyQueueHeader;
import com.jd.joyqueue.network.transport.codec.PayloadDecoder;
import com.jd.joyqueue.network.command.CommandType;
import com.jd.joyqueue.network.serializer.Serializer;
import com.jd.joyqueue.network.transport.command.Type;
import io.netty.buffer.ByteBuf;


/**
 * author: zhuduohui
 * email: zhuduohui@jd.com
 * date: 2018/9/29
 */
public class ReplicateConsumePosRequestDecoder implements PayloadDecoder<JoyQueueHeader>, Type {
    @Override
    public Object decode(final JoyQueueHeader header, final ByteBuf buffer) throws Exception {
        String consumePositions;
        if (header.getVersion() == JoyQueueHeader.VERSION1) {
            consumePositions = Serializer.readString(buffer, Serializer.SHORT_SIZE);
        } else {
            consumePositions = Serializer.readString(buffer, Serializer.INT_SIZE);
        }
        return new ReplicateConsumePosRequest(consumePositions);
    }

    @Override
    public int type() {
        return CommandType.REPLICATE_CONSUME_POS_REQUEST;
    }
}
