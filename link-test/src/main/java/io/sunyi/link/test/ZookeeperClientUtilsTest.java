package io.sunyi.link.test;

import io.sunyi.link.core.registry.zookeeper.ZookeeperClient;
import io.sunyi.link.core.registry.zookeeper.ZookeeperListener;

import java.io.IOException;
import java.util.List;

/**
 * @author sunyi
 */

public class ZookeeperClientUtilsTest {

	public static void main(String args[]) throws IOException {

		ZookeeperClient client = new ZookeeperClient("192.168.1.120:2181");

		client.createPersistent("/link");

		client.registerChangeListener("/link", new ZookeeperListener() {

			@Override
			public void handleDataDeleted(String dataPath) {
				System.out.println("handleDataDeleted,dataPath:" + dataPath);
			}

			@Override
			public void handleDataChange(String dataPath, Object data) {
				System.out.println("handleDataDeleted");
			}

			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) {
				System.out.println("handleChildChange");
				for (String child : currentChilds) {
					System.out.println(child);
				}
			}
		});


		System.in.read();
	}

}
