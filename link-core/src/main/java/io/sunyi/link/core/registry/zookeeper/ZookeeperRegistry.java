package io.sunyi.link.core.registry.zookeeper;

import com.alibaba.fastjson.JSONObject;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.server.ServerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunyi
 */
public class ZookeeperRegistry implements Registry {

	private String zkUrl;

	private ZookeeperClient client;

	private static final String BASE_DIR_PATH = "/link";
	private static final String SERVER_DIR_PATH = "/server";


	public void init() {
		if (client == null) {
			client = new ZookeeperClient(zkUrl);
		}
	}

	@Override
	public void publishServer(ServerConfig serverConfig) {

		String interfaceClassName = serverConfig.getInterfaceClass().getName();

		String classDir = BASE_DIR_PATH + "/" + interfaceClassName + SERVER_DIR_PATH;
		client.createPersistent(classDir);


		String serverDir = classDir + "/" + JSONObject.toJSONString(serverConfig);
		client.createEphemeral(serverDir);

	}

	@Override
	public List<ServerConfig> getServerList(Class interfaceClass) {

		String interfaceClassName = interfaceClass.getName();
		String classDir = BASE_DIR_PATH + "/" + interfaceClassName + SERVER_DIR_PATH;

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
	}

	public void setZkUrl(String zkUrl) {
		this.zkUrl = zkUrl;
	}
}
