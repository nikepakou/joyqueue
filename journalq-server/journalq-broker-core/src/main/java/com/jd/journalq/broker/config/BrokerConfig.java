package com.jd.journalq.broker.config;

import com.jd.journalq.domain.Broker;
import com.jd.journalq.network.transport.config.ServerConfig;
import com.jd.journalq.network.transport.config.TransportConfigSupport;
import com.jd.journalq.toolkit.config.Property;
import com.jd.journalq.toolkit.config.PropertySupplier;
import com.jd.journalq.toolkit.config.PropertySupplierAware;
import com.jd.journalq.toolkit.io.Files;
import com.jd.journalq.toolkit.lang.Preconditions;
import com.jd.journalq.toolkit.network.IpUtil;

import java.io.File;

/**
 * Broker服务配置
 */
public class BrokerConfig implements PropertySupplierAware {
    public static final String BROKER_FRONTEND_SERVER_CONFIG_PREFIX = "broker.frontend-server.";
    public static final String BROKER_BACKEND_SERVER_CONFIG_PREFIX = "broker.backend-server.";
    public static final String BROKER_ID_FILE_NAME = "broker.id";
    public static final String ADMIN_USER = "broker.jmq.admin";
    public static final int INVALID_BROKER_ID = -1;

    /**
     * broker data root dir
     */
    private String dataPath;
    /**
     * broker
     */
    private Broker broker;
    /**
     * local ip
     */
    private String localIp;

    private String adminUser;

    /**
     * broker fronted server config
     */
    private ServerConfig frontendConfig;

    /**
     * broker backend server config
     */
    private ServerConfig backendConfig;
    /**
     * property supplier
     */
    private PropertySupplier propertySupplier;


    public BrokerConfig(PropertySupplier propertySupplier) {
        setSupplier(propertySupplier);
    }

    public String getAndCreateDataPath() {
        if (dataPath != null) {
            return dataPath;
        }

        // 只能初始化一次
        synchronized (this) {
            if (dataPath == null) {
                Property property = propertySupplier == null ? null : propertySupplier.getProperty(Property.APPLICATION_DATA_PATH);
                String path = property == null ? null : property.getString();
                Preconditions.checkArgument(path != null, "data path can not be null.");
                Files.createDirectory(new File(path));
                dataPath = path;
            }
        }

        return dataPath;
    }

    public String getBrokerIdFilePath() {
        return getAndCreateDataPath() + File.separator + BROKER_ID_FILE_NAME;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
        frontendConfig.setPort(broker.getPort());
        backendConfig.setPort(broker.getBackEndPort());
    }


    public Integer getBrokerId() {
        return broker == null ? INVALID_BROKER_ID : broker.getId();
    }


    @Deprecated
    public String getBrokerIp() {
        if (localIp == null) {
            localIp = IpUtil.getLocalIp();
        }
        return localIp;
    }

    @Override
    public void setSupplier(PropertySupplier propertySupplier) {
        this.propertySupplier = propertySupplier;
        this.frontendConfig = TransportConfigSupport.buildServerConfig(propertySupplier, BROKER_FRONTEND_SERVER_CONFIG_PREFIX);
        this.backendConfig = TransportConfigSupport.buildServerConfig(propertySupplier, BROKER_BACKEND_SERVER_CONFIG_PREFIX);
        Property adminUser = propertySupplier.getProperty(ADMIN_USER);
        if (null != adminUser) this.adminUser = adminUser.getString();
    }

    public Broker getBroker() {
        //TODO
        return broker;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public ServerConfig getFrontendConfig() {
        return frontendConfig;
    }

    public void setFrontendConfig(ServerConfig frontendConfig) {
        this.frontendConfig = frontendConfig;
    }

    public ServerConfig getBackendConfig() {
        return backendConfig;
    }

    public void setBackendConfig(ServerConfig backendConfig) {
        this.backendConfig = backendConfig;
    }
}