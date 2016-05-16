package io.sunyi.link.core.registry.zookeeper;

import com.alibaba.fastjson.JSONObject;
import io.sunyi.link.core.exception.LinkException;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.registry.RegistryListener;
import io.sunyi.link.core.server.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于 zookeeper 实现的注册中心
 *
 * @author sunyi
 */
public class ZookeeperRegistry implements Registry {

	private Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

	private String zkUrl;

	private ZookeeperClient client;

	private static final String BASE_DIR_PATH = "/link";
	private static final String SERVER_DIR_PATH = "/server";

	@Override
	public void init() {
		if (zkUrl == null || zkUrl.length() == 0) {
			throw new LinkException("The ZookeeperRegistry need specify a zk url, @see LinkApplicationContext.setRegistryUrl(String)");
		}

		if (client == null) {
			client = new ZookeeperClient(zkUrl);
			logger.info(ZookeeperRegistry.class.getSimpleName() + " initialized.");
		}
	}

	@Override
	public void setRegistryUrl(String url) {
		this.zkUrl = url;
	}

	@Override
	public void exportServer(ServerConfig serverConfig) {


		client.createPersistent(BASE_DIR_PATH);

		String interfaceClassName = serverConfig.getInterfaceClass().getName();
		client.createPersistent(BASE_DIR_PATH + File.separator + interfaceClassName);


		String classDir = getClassDir(serverConfig.getInterfaceClass());
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

		return JSONObject.toJSONString(newServerConfig);

	}

	@Override
	public List<ServerConfig> getServerList(Class interfaceClass) {

		String classDir = getClassDir(interfaceClass);

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
	public List<ServerConfig> watching(Class interfaceClass, final RegistryListener listener) {

		String classDir = getClassDir(interfaceClass);

		client.registerChangeListener(classDir, new ZookeeperListener() {
			@Override
			public void handleDataDeleted(String dataPath) {
				//
			}

			@Override
			public void handleDataChange(String dataPath, Object data) {
				//
			}

			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) {
				List<ServerConfig> list = null;
				if (currentChilds != null) {
					list = new ArrayList<ServerConfig>(currentChilds.size());
					for (String json : currentChilds) {
						list.add(JSONObject.parseObject(json, ServerConfig.class));
					}
				} else {
					list = new ArrayList<ServerConfig>(0);
				}

				listener.onServerChange(list);
			}
		});

		return getServerList(interfaceClass);
	}

	@Override
	public void close() {
		client.doClose();
		logger.info(ZookeeperRegistry.class.getSimpleName() + " closed.");
	}

	private String getClassDir(Class interfaceClass) {
		String interfaceClassName = interfaceClass.getName();
		String classDir = BASE_DIR_PATH + File.separator + interfaceClassName + SERVER_DIR_PATH;

		return classDir;
	}

	public void setZkUrl(String zkUrl) {
		this.zkUrl = zkUrl;
	}
}
