package io.sunyi.link.core.serialize.hessian;

import io.sunyi.link.core.serialize.Serialize;
import io.sunyi.link.core.serialize.SerializeFactory;

/**
 * Created by sunyi on 15/9/23.
 */
public class HessianSerializeFactory implements SerializeFactory {

	private HessianSerialize hessianSerialize;


	@Override
	public Serialize getSerialize() {
		if (hessianSerialize == null) {
			synchronized (HessianSerializeFactory.class) {
				if (hessianSerialize == null) {
					hessianSerialize = new HessianSerialize();
				}
			}
		}

		return hessianSerialize;
	}
}
