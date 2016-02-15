package io.sunyi.link.core.serialize.hessian;

import com.caucho.hessian.io.Hessian2Input;
import io.sunyi.link.core.serialize.ObjectReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by sunyi on 15/9/22.
 */
public class HessianObjectReader implements ObjectReader {

	@Override
	public Object read(byte[] bytes) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		Hessian2Input input = new Hessian2Input(bais);
		try {
			return input.readObject();
		} finally {
			input.close();
		}

	}

	@Override
	public <T> T read(byte[] bytes, Class<T> cl) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		Hessian2Input input = new Hessian2Input(bais);
		try {
			return (T) input.readObject();
		} finally {
			input.close();
		}
	}
}
