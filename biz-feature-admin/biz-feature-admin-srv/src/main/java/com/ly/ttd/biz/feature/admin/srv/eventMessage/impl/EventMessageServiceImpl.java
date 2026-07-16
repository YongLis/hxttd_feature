package com.ly.ttd.biz.feature.admin.srv.eventMessage.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.EventMessageEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.EventMessageMapper;
import com.ly.ttd.feature.admin.api.dto.EventMessageDto;
import com.ly.ttd.feature.admin.api.eventMessage.EventMessageService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 事件消息 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class EventMessageServiceImpl implements EventMessageService {
    @Resource
    private EventMessageMapper eventMessageMapper;

    @Override
    public Long add(EventMessageDto dto) throws BizException {
        EventMessageEntity entity = new EventMessageEntity();
        BeanUtils.copyProperties(dto, entity);
        eventMessageMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(EventMessageDto dto) throws BizException {
        EventMessageEntity entity = new EventMessageEntity();
        BeanUtils.copyProperties(dto, entity);
        eventMessageMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        eventMessageMapper.deleteById(id);
    }

    @Override
    public EventMessageDto queryById(Long id) throws BizException {
        EventMessageEntity entity = eventMessageMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        EventMessageDto dto = new EventMessageDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
