package io.sunyi.link.core.registry.zookeeper;

import java.util.List;

/**
 * @author sunyi
 */
public interface ZookeeperListener {

	void handleDataDeleted(String dataPath);

	void handleDataChange(String dataPath, Object data);

	void handleChildChange(String parentPath, List<String> currentChilds);
}
