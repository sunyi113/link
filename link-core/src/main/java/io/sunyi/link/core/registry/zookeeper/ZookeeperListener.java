package io.sunyi.link.core.registry.zookeeper;

import io.sunyi.link.core.registry.RegistryListener;
import io.sunyi.link.core.server.ServerConfig;

import java.util.List;

/**
 * @author sunyi
 */
public interface ZookeeperListener {

	void handleDataDeleted(String dataPath);

	void handleDataChange(String dataPath, Object data);

	void handleChildChange(String parentPath, List<String> currentChilds);

}
