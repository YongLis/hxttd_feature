package com.ly.ttd.biz.feature.dem.sweb.service.access;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.access.req.AccessPointAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.access.req.AccessPointUpdateReq;

/**
 * @author yong.li
 * @since 2026/7/13 11:34
 */
public interface AccessPointAdminService {

    /**
     * 添加
     */
    void add(AccessPointAddReq req) throws BizException;

    /**
     * 更新接入点
     *
     * @param req 请求参数
     * @throws BizException 业务异常
     */
    void update(AccessPointUpdateReq req) throws BizException;

    /**
     * 根据ID删除接入点
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;


}
