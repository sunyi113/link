package io.sunyi.link.core.serialize;

import io.sunyi.link.core.commons.LinkScalableComponent;

import java.io.IOException;

/**
 * @author sunyi
 */
public interface Serialize extends LinkScalableComponent {

	Object read(byte[] bytes) throws IOException;

	<T> T read(byte[] bytes, Class<T> cl) throws IOException;

	byte[] write(Object object) throws IOException;

}
