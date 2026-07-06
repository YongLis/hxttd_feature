package com.ly.ttd.feature.srv.data;

import com.ly.ttd.feature.srv.data.dto.FeatureMiddleData;

/**
 * 特征中间数据存储服务
 *
 * @author yong.li
 * @since 2026/7/4 21:47
 */
public interface FeatureMiddleDataSaveService {


    /**
     * 保存特征中间数据
     */
    void saveData(FeatureMiddleData dto);

}
