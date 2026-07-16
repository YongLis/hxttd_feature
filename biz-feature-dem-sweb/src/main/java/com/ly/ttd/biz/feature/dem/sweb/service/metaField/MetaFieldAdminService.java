package com.ly.ttd.biz.feature.dem.sweb.service.metaField;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.MetaFieldAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.metaField.req.MetaFieldUpdateReq;

/**
 * 元字段 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
public interface MetaFieldAdminService {

    /**
     * 新增元字段
     *
     * @param req 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    void add(MetaFieldAddReq req) throws BizException;

    /**
     * 更新元字段
     *
     * @param req 请求参数
     * @throws BizException 业务异常
     */
    void update(MetaFieldUpdateReq req) throws BizException;

    /**
     * 根据ID删除元字段
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

}
