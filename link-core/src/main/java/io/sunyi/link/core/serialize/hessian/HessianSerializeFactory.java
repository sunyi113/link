package io.sunyi.link.core.serialize.hessian;

import io.sunyi.link.core.serialize.ObjectReader;
import io.sunyi.link.core.serialize.ObjectWriter;
import io.sunyi.link.core.serialize.SerializeFactory;

/**
 * Created by sunyi on 15/9/23.
 */
public class HessianSerializeFactory implements SerializeFactory {

	private HessianObjectReader reader;
	private HessianObjectWriter writer;

	@Override
	public ObjectReader getObjectReader() {

		if (reader == null) {
			synchronized (HessianSerializeFactory.class) {
				if (reader == null) {
					reader = new HessianObjectReader();
				}
			}
		}

		return reader;
	}

	@Override
	public ObjectWriter getObjectWriter() {

		if (writer == null) {
			synchronized (HessianSerializeFactory.class) {
				if (writer == null) {
					writer = new HessianObjectWriter();
				}
			}
		}

		return writer;
	}


}
