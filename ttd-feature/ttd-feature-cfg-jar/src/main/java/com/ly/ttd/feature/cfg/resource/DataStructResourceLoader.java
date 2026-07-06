package com.ly.ttd.feature.cfg.resource;

import com.ly.ttd.feature.common.model.struct.DataStructModel;

import java.util.List;

/**
 * @author yong.li
 * @since 2026/6/22 16:19
 */
public interface DataStructResourceLoader {

    public List<DataStructModel> loadDataStruct(Long projectId);
}
