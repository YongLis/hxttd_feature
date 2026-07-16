package com.ly.ttd.feature.admin.api.factor;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.FactorDependencyDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

import java.util.List;

/**
 * 指标血缘 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface FactorDependencyService {

    /**
     * 新增指标血缘关系
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(FactorDependencyDto dto) throws BizException;

    /**
     * 更新指标血缘关系
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(FactorDependencyDto dto) throws BizException;

    /**
     * 根据ID删除指标血缘关系
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询指标血缘关系
     *
     * @param id 主键ID
     * @return 指标血缘关系信息
     * @throws BizException 业务异常
     */
    FactorDependencyDto queryById(Long id) throws BizException;

    List<FactorDependencyDto> queryDependency(Long projectId, String parent);

    void removeDependency(Long projectId, String parent);

    void addDependency(Long projectId, String parent, String parentType, List<String> childs, String user);

    List<FactorDependencyDto> queryUpstreamDependency(Long projectId, String child);
}
