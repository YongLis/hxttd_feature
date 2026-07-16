package com.ly.ttd.biz.feature.dem.sweb.service.dataStruct;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.req.DataStructAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dataStruct.req.DataStructUpdateReq;

/**
 * 数据集 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
public interface DataStructAdminService {

    /**
     * 新增数据集
     *
     * @param req 请求参数
     * @throws BizException 业务异常
     */
    void add(DataStructAddReq req) throws BizException;

    /**
     * 更新数据集
     *
     * @param req 请求参数
     * @throws BizException 业务异常
     */
    void update(DataStructUpdateReq req) throws BizException;

    /**
     * 根据ID删除数据集
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

}
