package com.ly.ttd.feature.admin.api.system;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.UserAccountDto;
import com.ly.ttd.feature.admin.api.dto.UserAccountUpdatePwd;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

import java.util.List;

/**
 * 用户账号 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface UserAccountService {

    /**
     * 新增用户账号
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(UserAccountDto dto) throws BizException;

    /**
     * 更新用户账号
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(UserAccountDto dto) throws BizException;

    /**
     * 根据ID删除用户账号
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询用户账号
     *
     * @param id 主键ID
     * @return 用户账号信息
     * @throws BizException 业务异常
     */
    UserAccountDto queryById(Long id) throws BizException;


    List<UserAccountDto> queryAll() throws BizException;

    void updatePassword(UserAccountUpdatePwd updatePwd) throws BizException;
}
