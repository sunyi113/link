package io.sunyi.link.core.serialize;

/**
 * 序列化工厂
 * Created by sunyi on 15/9/23.
 */
public interface SerializeFactory {

	ObjectReader getObjectReader();

	ObjectWriter getObjectWriter();

}
