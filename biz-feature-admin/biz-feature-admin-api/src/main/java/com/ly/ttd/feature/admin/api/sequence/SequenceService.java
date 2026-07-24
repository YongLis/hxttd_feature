package com.ly.ttd.feature.admin.api.sequence;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.feature.admin.api.ServiceGroup;
import com.ly.ttd.feature.admin.api.dto.SequenceDto;
import com.ly.ttd.inf.rpc.api.annotation.RpcService;

/**
 * 序列 RPC 服务接口
 *
 * @author yong.li
 * @since 2026-07-10
 */
@RpcService(serviceName = ServiceGroup.SERVICE_NAME)
public interface SequenceService {

    /**
     * 获取下一个序列值（原子递增）
     *
     * @param seqCode 序列编码
     * @return 新序列值
     */
    Long nextVal(String seqCode);

    /**
     * 生成序号
     *
     * @param prefix  前缀
     * @param length  序号长度
     * @param seqCode 序列编码
     */
    public String generateSeq(String prefix, Integer length, String seqCode);


    /**
     * 新增序列
     *
     * @param dto 请求参数
     * @return 主键ID
     * @throws BizException 业务异常
     */
    Long add(SequenceDto dto) throws BizException;

    /**
     * 更新序列
     *
     * @param dto 请求参数
     * @throws BizException 业务异常
     */
    void update(SequenceDto dto) throws BizException;

    /**
     * 根据ID删除序列
     *
     * @param id     主键ID
     * @param opUser 操作人
     * @throws BizException 业务异常
     */
    void delete(Long id, String opUser) throws BizException;

    /**
     * 根据ID查询序列
     *
     * @param id 主键ID
     * @return 序列信息
     * @throws BizException 业务异常
     */
    SequenceDto queryById(Long id) throws BizException;
}
