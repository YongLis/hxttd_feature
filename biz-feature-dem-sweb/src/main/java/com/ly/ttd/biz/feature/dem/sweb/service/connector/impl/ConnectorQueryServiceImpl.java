package com.ly.ttd.biz.feature.dem.sweb.service.connector.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.base.result.PageResult;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.entity.ConnectorEntity;
import com.ly.ttd.biz.feature.dem.sweb.mybatis.rcs.slave.mapper.ConnectorMapper;
import com.ly.ttd.biz.feature.dem.sweb.service.connector.ConnectorQueryService;
import com.ly.ttd.biz.feature.dem.sweb.service.connector.req.ConnectorQueryReq;
import com.ly.ttd.biz.feature.dem.sweb.service.connector.res.ConnectorQueryRes;
import com.ly.ttd.feature.common.enums.ConnectorEnum;
import com.ly.ttd.feature.common.enums.FeatureResultCodeEnum;
import com.ly.ttd.feature.common.model.DataFieldModel;
import com.ly.ttd.feature.common.model.connector.ElasticsearchConnectorModel;
import com.ly.ttd.feature.common.model.connector.HttpConnectorModel;
import com.ly.ttd.feature.common.model.connector.JdbcConnectorModel;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 连接器查询服务实现
 *
 * @author yong.li
 * @since 2026-06-23
 */
@Service
public class ConnectorQueryServiceImpl implements ConnectorQueryService {
    @Resource
    private ConnectorMapper connectorMapper;

    @Override
    public PageResult<ConnectorQueryRes> pageQuery(ConnectorQueryReq req) {
        Page<ConnectorQueryRes> page = new Page<>(req.getCurrent(), req.getPageSize());
        List<ConnectorQueryRes> result = connectorMapper.pageQuery(page, req);

        if (CollectionUtils.isNotEmpty(result)) {
            for (ConnectorQueryRes res : result) {
                if (StringUtils.isNotBlank(res.getResourceJson())) {
                    res.setFactorCodes(parseFactorCodes(res.getConnectorType(), res.getResourceJson()));
                }
            }
        }

        PageResult<ConnectorQueryRes> pageResult = new PageResult<>();
        pageResult.setData(result);
        pageResult.setTotal(page.getTotal());
        pageResult.setCurrent(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setCode(FeatureResultCodeEnum.SUCCESS.getCode());
        return pageResult;
    }

    private List<String> parseFactorCodes(String connectorType, String resourceJson) {
        if (ConnectorEnum.HTTP.getCode().equals(connectorType)) {
            HttpConnectorModel connectorModel = HttpConnectorModel.convertResource(resourceJson);
            if (CollectionUtils.isNotEmpty(connectorModel.getFields())) {
                return connectorModel.getFields().stream().map(DataFieldModel::getFieldCode).collect(Collectors.toList());
            }
        } else if (ConnectorEnum.ES.getCode().equals(connectorType)) {
            ElasticsearchConnectorModel connectorModel = ElasticsearchConnectorModel.convertResource(resourceJson);
            if (CollectionUtils.isNotEmpty(connectorModel.getFields())) {
                return connectorModel.getFields().stream().map(DataFieldModel::getFieldCode).collect(Collectors.toList());
            }
        } else if (ConnectorEnum.JDBC.getCode().equals(connectorType)) {
            JdbcConnectorModel connectorModel = JdbcConnectorModel.convertResource(resourceJson);
            if (CollectionUtils.isNotEmpty(connectorModel.getFields())) {
                return connectorModel.getFields().stream().map(DataFieldModel::getFieldCode).collect(Collectors.toList());
            }
        } else {
            throw new RuntimeException("connector not support");
        }
        return new ArrayList<>();
    }


    @Override
    public ConnectorEntity getById(Long id) {
        return connectorMapper.selectById(id);
    }
}
