package io.sunyi.link.core.registry;

import io.sunyi.link.core.server.ServerConfig;

import java.util.List;

/**
 * @author sunyi
 */
public interface RegistryListener {

	void onServerChange(List<ServerConfig> serverConfigs);

}
