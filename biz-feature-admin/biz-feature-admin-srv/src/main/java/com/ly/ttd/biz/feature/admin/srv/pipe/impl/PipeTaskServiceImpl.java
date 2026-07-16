package com.ly.ttd.biz.feature.admin.srv.pipe.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.PipeTask;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.PipeTaskMapper;
import com.ly.ttd.biz.feature.admin.srv.resource.ResourceOpFactory;
import com.ly.ttd.feature.admin.api.dto.PipeTaskDto;
import com.ly.ttd.feature.admin.api.pipe.PipeTaskService;
import com.ly.ttd.feature.common.enums.FeatureResourceType;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 数据管道任务 RPC 服务实现（接入统一审核）
 *
 * @author yong.li
 * @since 2026-07-14
 */
@Service
public class PipeTaskServiceImpl implements PipeTaskService {
    @Resource
    private PipeTaskMapper pipeTaskMapper;

    @Override
    public String add(PipeTaskDto dto) throws BizException {
        ResourceOpFactory.getService(FeatureResourceType.PIPE_TASK.getType())
                .add(dto);
        return null;
    }

    @Override
    public void update(PipeTaskDto dto) throws BizException {
        ResourceOpFactory.getService(FeatureResourceType.PIPE_TASK.getType())
                .update(dto);
    }

    @Override
    public void delete(String id, String opUser) throws BizException {
        ResourceOpFactory.getService(FeatureResourceType.PIPE_TASK.getType())
                .delete(Long.valueOf(id), opUser);
    }

    @Override
    public PipeTaskDto queryById(String id) throws BizException {
        PipeTask entity = pipeTaskMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        PipeTaskDto dto = new PipeTaskDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
