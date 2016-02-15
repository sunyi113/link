package com.tongbanjie.link.core.serialize.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.tongbanjie.link.core.serialize.ObjectReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
