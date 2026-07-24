package com.ly.ttd.biz.feature.dem.sweb.service.pipe.impl;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.pipe.PipeTaskAdminService;
import com.ly.ttd.biz.feature.dem.sweb.service.user.LoginUser;
import com.ly.ttd.feature.admin.api.consts.SequenceConst;
import com.ly.ttd.feature.admin.api.dto.PipeTaskDto;
import com.ly.ttd.feature.admin.api.pipe.PipeTaskService;
import com.ly.ttd.feature.admin.api.sequence.SequenceService;
import com.ly.ttd.inf.rpc.api.annotation.Rpcwired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 数据管道任务 管理服务实现
 *
 * @author yong.li
 * @since 2026-07-22
 */
@Slf4j
@Service
public class PipeTaskAdminServiceImpl implements PipeTaskAdminService {

    @Rpcwired
    private PipeTaskService pipeTaskService;
    @Rpcwired
    private SequenceService sequenceService;

    @Override
    public void add(PipeTaskDto dto) throws BizException {

        String taskCode = sequenceService.generateSeq("Task", 0, SequenceConst.DATA_PIPE_TASK);
        dto.setTaskCode(taskCode);
        dto.setCrtUser(LoginUser.getLoginUserName());
        dto.setDeleted(false);
        pipeTaskService.add(dto);
    }

    @Override
    public void update(PipeTaskDto dto) throws BizException {
        dto.setUptUser(LoginUser.getLoginUserName());
        pipeTaskService.update(dto);
    }

    @Override
    public void delete(String id) throws BizException {
        pipeTaskService.delete(id, LoginUser.getLoginUserName());
    }
}
