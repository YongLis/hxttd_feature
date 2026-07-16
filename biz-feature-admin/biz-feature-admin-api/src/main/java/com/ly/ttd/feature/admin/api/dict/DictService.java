package com.ly.ttd.feature.admin.api.dict;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.DictDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 字典 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface DictService {

    /**
     * 新增字典
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(DictDto dto) throws BizException;

    /**
     * 更新字典
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(DictDto dto) throws BizException;

    /**
     * 根据ID删除字典
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询字典
     *
     * @param id 主键ID
     * @return 字典信息
     * @throws BizException 业务异常
     */
    DictDto queryById(Long id) throws BizException;

    DictDto getDictCode(String systemCode, String dictCode);

}
