package com.ly.ttd.feature.cfg.resource;

import com.ly.ttd.feature.common.model.AccessPointModel;

import java.util.List;

/**
 * @author yong.li
 * @since 2026/6/22 15:20
 */
public interface AccessPointResourceLoader {

    /**
     * 加载接入点(待实现)
     */
    public List<AccessPointModel> loadResource(Long projectId);

}
