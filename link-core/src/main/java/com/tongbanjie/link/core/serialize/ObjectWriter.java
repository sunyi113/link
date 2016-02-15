package com.tongbanjie.link.core.serialize;

import java.io.IOException;

/**
 * Created by sunyi on 15/9/22.
 */
public interface ObjectWriter {

	byte[] write(Object object) throws IOException;

}
