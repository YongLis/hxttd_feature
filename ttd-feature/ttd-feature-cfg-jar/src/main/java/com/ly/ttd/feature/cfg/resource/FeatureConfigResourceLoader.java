package com.ly.ttd.feature.cfg.resource;

import com.ly.ttd.feature.common.model.vel.FeatureConfigModel;

import java.util.List;

/**
 * 指标配置
 *
 * @author yong.li
 * @since 2026/6/22 15:20
 */
public interface FeatureConfigResourceLoader {

    /**
     * 加载实时特征配置资源(待实现)
     */
    public List<FeatureConfigModel> loadConfig(Long projectId);

}
