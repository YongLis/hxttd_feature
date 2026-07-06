package com.ly.ttd.feature.engine.cache;

/**
 * @author yong.li
 * @since 2026/6/22 16:04
 */
public interface CacheRefreshService {

    /**
     * 刷新接入点缓存
     *
     * @param projectId 项目ID
     */
    void loadAndRefreshAccessPoint(Long projectId);

    /**
     * 刷新实时特征配置缓存
     *
     * @param projectId 项目ID
     */
    void loadAndRefreshFeatureConfig(Long projectId);

    /**
     * 刷新元数据缓存
     *
     * @param projectId 项目ID
     */
    void loadAndRefreshMetaField(Long projectId);

    /**
     * 刷新元数据缓存
     *
     * @param projectId 项目ID
     */
    void loadAndRefreshFactor(Long projectId);

    /**
     * 刷新数据集
     */
    void loadAndRefreshDataStruct(Long projectId);


    public void refreshCache(Long projectId);

}
