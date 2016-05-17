package io.sunyi.link.core.serialize;

import io.sunyi.link.core.commons.LinkScalableComponent;

/**
 * 序列化工厂
 * Created by sunyi on 15/9/23.
 */
public interface SerializeFactory extends LinkScalableComponent {

	Serialize getSerialize();


}
