package com.ly.ttd.feature.admin.api.dict;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.DictCodeDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

import java.util.List;

/**
 * 字典项 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface DictCodeService {

    /**
     * 新增字典项
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(DictCodeDto dto) throws BizException;

    /**
     * 更新字典项
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(DictCodeDto dto) throws BizException;

    /**
     * 根据ID删除字典项
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询字典项
     *
     * @param id 主键ID
     * @return 字典项信息
     * @throws BizException 业务异常
     */
    DictCodeDto queryById(Long id) throws BizException;

    void addDictCode(Long id, String pointCode, String name);

    List<DictCodeDto> getByDictId(Long dictId);
}
