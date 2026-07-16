package com.ly.ttd.biz.feature.admin.srv.connector.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.ConnectorEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.ConnectorMapper;
import com.ly.ttd.feature.admin.api.connector.JdbcConnectorService;
import com.ly.ttd.feature.admin.api.dto.JdbcConnectorDto;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 连接器 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class JdbcConnectorServiceImpl implements JdbcConnectorService {
    @Resource
    private ConnectorMapper connectorMapper;

    @Override
    public Long add(JdbcConnectorDto dto) throws BizException {
        ConnectorEntity entity = new ConnectorEntity();
        BeanUtils.copyProperties(dto, entity);
        connectorMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(JdbcConnectorDto dto) throws BizException {
        ConnectorEntity entity = new ConnectorEntity();
        BeanUtils.copyProperties(dto, entity);
        connectorMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        connectorMapper.deleteById(id);
    }

    @Override
    public JdbcConnectorDto queryById(Long id) throws BizException {
        ConnectorEntity entity = connectorMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        JdbcConnectorDto dto = new JdbcConnectorDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
