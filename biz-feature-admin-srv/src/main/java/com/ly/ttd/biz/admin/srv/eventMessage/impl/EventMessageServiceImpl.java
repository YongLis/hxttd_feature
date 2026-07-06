package com.ly.ttd.biz.admin.srv.eventMessage.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.ttd.biz.admin.mybatis.entity.EventMessageEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.EventMessageMapper;
import com.ly.ttd.biz.admin.srv.eventMessage.EventMessageService;
import org.springframework.stereotype.Service;

/**
 * 事件消息服务实现
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Service
public class EventMessageServiceImpl extends ServiceImpl<EventMessageMapper, EventMessageEntity> implements EventMessageService {
}
