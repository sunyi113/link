package io.sunyi.link.core.registry.zookeeper;

import com.alibaba.fastjson.JSONObject;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.server.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sunyi
 */
public class ZookeeperRegistry implements Registry {

	private Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

	private String zkUrl;

	private ZookeeperClient client;

	private static final String BASE_DIR_PATH = "/link";
	private static final String SERVER_DIR_PATH = "/server";


	public ZookeeperRegistry(String zkUrl) {
		this.zkUrl = zkUrl;
		this.init();
	}

	private void init() {
		if (client == null) {
			client = new ZookeeperClient(zkUrl);
			logger.info(ZookeeperRegistry.class.getSimpleName() + " initialized.");
		}

	}

	@Override
	public void exportServer(ServerConfig serverConfig) {


		client.createPersistent(BASE_DIR_PATH);

		String interfaceClassName = serverConfig.getInterfaceClass().getName();
		client.createPersistent(BASE_DIR_PATH + File.separator + interfaceClassName);


		String classDir = BASE_DIR_PATH + File.separator + interfaceClassName + SERVER_DIR_PATH;
		client.createPersistent(classDir);

		String serverConfigJson = getServerConfigJson(serverConfig);

		String serverDir = classDir + File.separator + serverConfigJson;
		client.createEphemeral(serverDir);

	}

	private String getServerConfigJson(ServerConfig serverConfig) {

		ServerConfig newServerConfig = new ServerConfig();
		newServerConfig.setIp(serverConfig.getIp());
		newServerConfig.setPort(serverConfig.getPort());
		newServerConfig.setPid(serverConfig.getPid());
		newServerConfig.setInterfaceClass(serverConfig.getInterfaceClass());
		newServerConfig.setPriority(serverConfig.getPriority());

		return JSONObject.toJSONString(newServerConfig);

	}

	@Override
	public List<ServerConfig> getServerList(Class interfaceClass) {

		String interfaceClassName = interfaceClass.getName();
		String classDir = BASE_DIR_PATH + File.separator + interfaceClassName + SERVER_DIR_PATH;

		List<String> children = client.getChildren(classDir);

		if (children != null) {
			List<ServerConfig> list = new ArrayList<ServerConfig>(children.size());
			for (String json : children) {
				list.add(JSONObject.parseObject(json, ServerConfig.class));
			}
			return list;
		} else {
			return new ArrayList<ServerConfig>(0);
		}
	}

	@Override
	public void close() {
		client.doClose();
		logger.info(ZookeeperRegistry.class.getSimpleName() + " closed.");
	}

	public void setZkUrl(String zkUrl) {
		this.zkUrl = zkUrl;
	}
}
