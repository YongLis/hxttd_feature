package com.ly.ttd.feature.sample.resource.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ly.ttd.feature.cfg.resource.DataStructResourceLoader;
import com.ly.ttd.feature.common.model.struct.DataStructModel;
import com.ly.ttd.feature.common.model.struct.FieldModel;
import com.ly.ttd.feature.sample.mybatis.entity.DataFieldEntity;
import com.ly.ttd.feature.sample.mybatis.entity.DataStructEntity;
import com.ly.ttd.feature.sample.mybatis.mapper.DataStructFieldMapper;
import com.ly.ttd.feature.sample.mybatis.mapper.DataStructMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据集资源加载实现
 * <p>
 * 从 MySQL（ttd_data_struct + ttd_data_field 表）读取数据集及字段定义，
 * 组装为引擎用的 {@link DataStructModel}（含 {@link FieldModel} 列表）。
 *
 * @author yong.li
 * @since 2026/6/29
 */
@Slf4j
@Service
public class DataStructResourceLoaderImpl implements DataStructResourceLoader {

    @Resource
    private DataStructMapper dataStructMapper;

    @Resource
    private DataStructFieldMapper dataStructFieldMapper;

    @Override
    public List<DataStructModel> loadDataStruct(Long projectId) {
        // 注: ttd_data_struct 表无 projectId 字段，此处加载全量数据集
        List<DataStructEntity> entities = dataStructMapper.selectList(new LambdaQueryWrapper<>());

        if (CollectionUtils.isEmpty(entities)) {
            log.info("未加载到数据集资源, projectId={}", projectId);
            return Collections.emptyList();
        }

        List<DataStructModel> models = entities.stream().map(this::convert).collect(Collectors.toList());
        log.info("加载数据集资源完成, projectId={}, count={}", projectId, models.size());
        return models;
    }

    private DataStructModel convert(DataStructEntity entity) {
        DataStructModel model = new DataStructModel();
        model.setResourceKey(entity.getResourceKey());
        model.setResourceName(entity.getResourceName());
        model.setVersion(entity.getVersion());
        model.setFieldModels(loadFields(entity.getResourceKey()));
        return model;
    }

    private List<FieldModel> loadFields(String dataStructCode) {
        List<DataFieldEntity> fields = dataStructFieldMapper.selectList(
                new LambdaQueryWrapper<DataFieldEntity>()
                        .eq(DataFieldEntity::getDataStructCode, dataStructCode)
                        .orderByAsc(DataFieldEntity::getSortOrder));

        if (CollectionUtils.isEmpty(fields)) {
            return Collections.emptyList();
        }
        return fields.stream().map(f -> {
            FieldModel fm = new FieldModel();
            fm.setFieldCode(f.getFieldCode());
            fm.setFactorCode(f.getFactorCode());
            fm.setObjectType(f.getObjectType());
            fm.setDefaultValue(f.getDefaultValue());
            return fm;
        }).collect(Collectors.toList());
    }
}
