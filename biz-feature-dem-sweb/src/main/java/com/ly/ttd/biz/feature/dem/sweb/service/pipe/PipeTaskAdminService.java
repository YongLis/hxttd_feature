package com.ly.ttd.biz.feature.dem.sweb.service.pipe;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.dto.PipeTaskDto;

/**
 * 数据管道任务 管理服务接口
 *
 * @author yong.li
 * @since 2026-07-22
 */
public interface PipeTaskAdminService {

    /**
     * 新增管道任务（提交审核）
     */
    void add(PipeTaskDto dto) throws BizException;

    /**
     * 更新管道任务（提交审核）
     */
    void update(PipeTaskDto dto) throws BizException;

    /**
     * 删除管道任务（提交审核）
     */
    void delete(String id) throws BizException;
}
