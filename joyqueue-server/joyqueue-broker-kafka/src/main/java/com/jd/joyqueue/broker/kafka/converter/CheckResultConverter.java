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
package com.jd.joyqueue.broker.kafka.converter;

import com.jd.joyqueue.broker.kafka.KafkaErrorCode;
import com.jd.joyqueue.exception.JoyQueueCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CheckResultConverter
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2019/1/14
 */
public class CheckResultConverter {

    protected static final Logger logger = LoggerFactory.getLogger(CheckResultConverter.class);

    public static short convertProduceCode(JoyQueueCode code) {
        switch (code) {
            case FW_TOPIC_NOT_EXIST:
            case FW_PRODUCER_NOT_EXISTS: {
//                return KafkaErrorCode.TOPIC_AUTHORIZATION_FAILED.getCode();
                return KafkaErrorCode.UNKNOWN_TOPIC_OR_PARTITION.getCode();
            }
            case FW_PUT_MESSAGE_TOPIC_NOT_WRITE:
            case FW_PRODUCE_MESSAGE_BROKER_NOT_LEADER:
            case FW_TOPIC_NO_PARTITIONGROUP: {
                return KafkaErrorCode.NOT_LEADER_FOR_PARTITION.getCode();
            }
            default : {
                logger.warn("unknown produce code {}", code);
                return KafkaErrorCode.UNKNOWN_TOPIC_OR_PARTITION.getCode();
            }
        }
    }

    public static short convertFetchCode(JoyQueueCode code) {
        switch (code) {
            case FW_FETCH_TOPIC_MESSAGE_BROKER_NOT_LEADER:
            case FW_TOPIC_NO_PARTITIONGROUP: {
                return KafkaErrorCode.NOT_LEADER_FOR_PARTITION.getCode();
            }
            case FW_FETCH_TOPIC_MESSAGE_PAUSED:
            case FW_GET_MESSAGE_APP_CLIENT_IP_NOT_READ: {
                return KafkaErrorCode.NONE.getCode();
            }
            case FW_TOPIC_NOT_EXIST:
            case FW_CONSUMER_NOT_EXISTS: {
//                return KafkaErrorCode.TOPIC_AUTHORIZATION_FAILED.getCode();
                return KafkaErrorCode.UNKNOWN_TOPIC_OR_PARTITION.getCode();
            }
            default : {
                logger.warn("unknown fetch code {}", code);
                return KafkaErrorCode.UNKNOWN_TOPIC_OR_PARTITION.getCode();
            }
        }
    }
}