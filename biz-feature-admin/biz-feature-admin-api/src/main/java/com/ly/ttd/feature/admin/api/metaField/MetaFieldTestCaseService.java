package com.ly.ttd.feature.admin.api.metaField;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.MetaFieldTestCaseDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 元字段测试用例 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface MetaFieldTestCaseService {

    /**
     * 新增元字段测试用例
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(MetaFieldTestCaseDto dto) throws BizException;

    /**
     * 更新元字段测试用例
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(MetaFieldTestCaseDto dto) throws BizException;

    /**
     * 根据ID删除元字段测试用例
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询元字段测试用例
     *
     * @param id 主键ID
     * @return 元字段测试用例信息
     * @throws BizException 业务异常
     */
    MetaFieldTestCaseDto queryById(Long id) throws BizException;
}
