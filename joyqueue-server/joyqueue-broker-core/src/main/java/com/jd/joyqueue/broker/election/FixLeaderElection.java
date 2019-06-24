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
package com.jd.joyqueue.broker.election;

import com.jd.joyqueue.broker.cluster.ClusterManager;
import com.jd.joyqueue.broker.election.command.AppendEntriesRequest;
import com.jd.joyqueue.broker.election.command.AppendEntriesResponse;
import com.jd.joyqueue.broker.replication.ReplicaGroup;
import com.jd.joyqueue.network.transport.codec.JoyQueueHeader;
import com.jd.joyqueue.domain.PartitionGroup;
import com.jd.joyqueue.network.command.CommandType;
import com.jd.joyqueue.network.transport.command.Command;
import com.jd.joyqueue.network.transport.command.Direction;
import com.jd.joyqueue.store.replication.ReplicableStore;

import com.jd.joyqueue.toolkit.concurrent.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * author: zhuduohui
 * email: zhuduohui@jd.com
 * date: 2018/8/20
 */
public class FixLeaderElection extends LeaderElection {
    private static Logger logger = LoggerFactory.getLogger(FixLeaderElection.class);

    private List<DefaultElectionNode> allNodes;

    public FixLeaderElection(TopicPartitionGroup topicPartitionGroup, ElectionConfig electionConfig,
                             ElectionManager electionManager, ClusterManager clusterManager,
                             ElectionMetadataManager metadataManager, ReplicableStore replicableStore,
                             ReplicaGroup replicaGroup, EventBus<ElectionEvent> electionEventManager,
                             int leaderId, int localNodeId, List<DefaultElectionNode> allNodes) {
        this.topicPartitionGroup = topicPartitionGroup;
        this.electionConfig = electionConfig;
        this.electionManager = electionManager;
        this.clusterManager = clusterManager;
        this.electionMetadataManager = metadataManager;
        this.replicableStore = replicableStore;
        this.replicaGroup = replicaGroup;
        this.electionEventManager = electionEventManager;
        this.leaderId = leaderId;
        this.localNodeId = localNodeId;
        this.allNodes = allNodes;
    }

    @Override
    public void doStart() throws Exception{
        super.doStart();

        if (leaderId == localNodeId) {
            becomeLeader();
        } else {
            becomeFollower();
        }

        updateElectionMetadata();

        electionEventManager.add(new ElectionEvent(ElectionEvent.Type.LEADER_FOUND, 0,
                leaderId, topicPartitionGroup));
    }

    @Override
    public void doStop() {
        replicaGroup.stop();

        super.doStop();
    }

    /**
     * 获取参与选举的所有节点
     * @return 所有节点
     */
    @Override
    public Collection<DefaultElectionNode> getAllNodes() {
        return allNodes;
    }

    @Override
    public void setLeaderId(int leaderId) {
        if (leaderId != this.leaderId && this.leaderId != ElectionNode.INVALID_NODE_ID) {

            if (leaderId == localNodeId) {
                becomeLeader();
            } else {
                becomeFollower();
            }

            updateElectionMetadata();

            electionEventManager.add(new ElectionEvent(ElectionEvent.Type.LEADER_FOUND, 0,
                    leaderId, topicPartitionGroup));
        } else {
            this.leaderId = leaderId;
        }
    }

    /**
     * 更新选举元数据
     */
    private void updateElectionMetadata() {
        try (ElectionMetadata metadata = ElectionMetadata.Build.create(electionConfig.getMetadataPath(), topicPartitionGroup)
                .electionType(PartitionGroup.ElectType.fix)
                .allNodes(allNodes).leaderId(leaderId).localNode(localNodeId).build()) {
            electionMetadataManager.updateElectionMetadata(topicPartitionGroup, metadata);
        } catch (Exception e) {
            logger.warn("Partition group {}/node {} update election metadata fail",
                    topicPartitionGroup, localNodeId, e);
        }
    }

    private void becomeLeader() {
        replicaGroup.becomeLeader(0, leaderId);

        try {
            if (!replicableStore.serviceStatus()) {
                replicableStore.enable();
            }
        } catch (Exception e) {
            logger.info("Partition group {}/node {} enable store fail, exception is {}",
                    topicPartitionGroup, leaderId, e);
        }
    }

    private void becomeFollower() {
        replicaGroup.becomeFollower(0, leaderId);

        try {
            if (replicableStore.serviceStatus()) {
                replicableStore.disable();
            }
        } catch (Exception e) {
            logger.info("Partition group {}/node {} disable store fail, exception is {}",
                    topicPartitionGroup, leaderId, e);
        }
    }

    @Override
    public Command handleAppendEntriesRequest(AppendEntriesRequest request) {
        if (!isStarted()) {
            logger.warn("Partition group{}/node{} receive append entries request, election not started",
                    topicPartitionGroup, localNodeId);
            return new Command(new JoyQueueHeader(Direction.RESPONSE, CommandType.RAFT_APPEND_ENTRIES_RESPONSE),
                    new AppendEntriesResponse.Build().success(false).build());
        }

        logger.debug("Partition group{}/node {} receive append entries request from {}",
                topicPartitionGroup, localNodeId, request.getLeaderId());

        return  replicaGroup.appendEntries(request);
    }

}
