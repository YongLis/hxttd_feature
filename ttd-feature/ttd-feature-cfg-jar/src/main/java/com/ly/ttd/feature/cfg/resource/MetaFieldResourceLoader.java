package com.ly.ttd.feature.cfg.resource;

import com.ly.ttd.feature.common.model.meta.MetaFieldModel;

import java.util.List;

/**
 * @author yong.li
 * @since 2026/6/22 15:20
 */
public interface MetaFieldResourceLoader {

    /**
     * 加载元字段(待实现)
     */
    public List<MetaFieldModel> loadResource(Long projectId);

}
