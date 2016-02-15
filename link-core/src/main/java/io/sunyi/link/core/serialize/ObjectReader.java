package io.sunyi.link.core.serialize;

import java.io.IOException;

/**
 * @author sunyi
 */
public interface ObjectReader {

	Object read(byte[] bytes) throws IOException;

	<T> T read(byte[] bytes, Class<T> cl) throws IOException;

}