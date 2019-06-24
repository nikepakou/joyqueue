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
package com.jd.joyqueue.server.retry.api;

import com.jd.joyqueue.domain.ConsumeRetry;
import com.jd.joyqueue.exception.JoyQueueException;
import com.jd.joyqueue.model.PageResult;
import com.jd.joyqueue.server.retry.model.RetryQueryCondition;
import com.jd.joyqueue.server.retry.model.RetryStatus;

/**
 * Created by chengzhiliang on 2019/2/20.
 */
public interface ConsoleMessageRetry<T> extends MessageRetry<T> {

    PageResult<ConsumeRetry> queryConsumeRetryList(RetryQueryCondition retryQueryCondition) throws JoyQueueException;

    ConsumeRetry getConsumeRetryById(Long id) throws JoyQueueException;

    void updateStatus(String topic, String app, T[] messageId, RetryStatus status, long updateTime, int updateBy) throws Exception;

}
