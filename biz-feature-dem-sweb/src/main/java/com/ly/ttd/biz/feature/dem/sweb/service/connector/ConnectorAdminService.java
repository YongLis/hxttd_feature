package com.ly.ttd.biz.feature.dem.sweb.service.connector;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.connector.req.*;

/**
 * 连接器 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
public interface ConnectorAdminService {

    /**
     * 新增连接器(jdbc)
     *
     * @param req 请求参数
     * @throws BizException 业务异常
     */
    void addJdbc(JdbcConnectorAddReq req) throws BizException;

    /**
     * 更新连接器(jdbc)
     *
     * @param req 请求参数
     * @throws BizException 业务异常
     */
    void updateJdbc(JdbcConnectorUpdateReq req) throws BizException;


    /**
     * 新增连接器(http)
     *
     * @param req 请求参数
     * @throws BizException 业务异常
     */
    void addHttp(HttpConnectorAddReq req) throws BizException;

    /**
     * 更新连接器(http)
     *
     * @param req 请求参数
     * @throws BizException 业务异常
     */
    void updateHttp(HttpConnectorUpdateReq req) throws BizException;


    /**
     * 新增连接器(es)
     *
     * @param req 请求参数
     * @throws BizException 业务异常
     */
    void addEs(EsConnectorAddReq req) throws BizException;

    /**
     * 更新连接器(es)
     *
     * @param req 请求参数
     * @throws BizException 业务异常
     */
    void updateEs(EsConnectorUpdateReq req) throws BizException;


    /**
     * 根据ID删除连接器
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

}
