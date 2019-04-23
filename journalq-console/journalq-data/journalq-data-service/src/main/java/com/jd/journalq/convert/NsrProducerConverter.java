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
package com.jd.journalq.convert;

import com.jd.journalq.domain.ClientType;
import com.jd.journalq.model.domain.Identity;
import com.jd.journalq.model.domain.Namespace;
import com.jd.journalq.model.domain.Producer;
import com.jd.journalq.model.domain.ProducerConfig;
import com.jd.journalq.model.domain.Topic;
import com.jd.journalq.util.NullUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by wangxiaofei1 on 2019/1/2.
 */
public class NsrProducerConverter extends Converter<Producer, com.jd.journalq.domain.Producer>{

    @Override
    protected com.jd.journalq.domain.Producer forward(Producer producer) {
        com.jd.journalq.domain.Producer nsrProducer = new com.jd.journalq.domain.Producer();
        nsrProducer.setApp(producer.getApp().getCode());
        nsrProducer.setClientType(ClientType.valueOf(producer.getClientType()));
        nsrProducer.setTopic(CodeConverter.convertTopic(producer.getNamespace(),producer.getTopic()));
        if(!NullUtil.isEmpty(producer.getConfig())){
            nsrProducer.setProducerPolicy(com.jd.journalq.domain.Producer.ProducerPolicy.Builder.build()
                    .nearby(producer.getConfig().isNearBy())
                    .single(producer.getConfig().isSingle())
                    .blackList(producer.getConfig().getBlackList())
                    .archive(producer.isArchive())
                    .weight(producer.getConfig().getWeight())
                    //.timeout()
                    .create());
        }
        return nsrProducer;
    }

    @Override
    protected Producer backward(com.jd.journalq.domain.Producer nsrProducer) {
        Producer producer = new Producer();
        producer.setId(nsrProducer.getId());
        producer.setApp(new Identity(nsrProducer.getApp()));
        producer.setClientType(nsrProducer.getClientType().value());
        producer.setTopic(new Topic(nsrProducer.getTopic().getCode()));
        producer.setNamespace(new Namespace(nsrProducer.getTopic().getNamespace()));
        ProducerConfig producerConfig = new ProducerConfig();
        com.jd.journalq.domain.Producer.ProducerPolicy producerPolicy = nsrProducer.getProducerPolicy();
        if (producerPolicy != null) {
            producerConfig.setNearBy(producerPolicy.getNearby());
            producerConfig.setBlackList(StringUtils.join(producerPolicy.getBlackList(), ","));
            producerConfig.setArchive(producerPolicy.getArchive());
//            producerConfig.setProducerId();
            producerConfig.setSingle(producerPolicy.isSingle());
            Map<String,Short> map = producerPolicy.getWeight();
            if (map != null) {
                List<String> weightList = map.entrySet().stream().map(entry -> (entry.getKey()+":"+ entry.getValue()) ).collect(Collectors.toList());
                producerConfig.setWeight(StringUtils.join(weightList,","));
            }
        }
        producer.setConfig(producerConfig);

        return producer;
    }
}
