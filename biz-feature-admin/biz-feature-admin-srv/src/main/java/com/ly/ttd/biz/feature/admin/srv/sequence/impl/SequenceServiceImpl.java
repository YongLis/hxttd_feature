package com.ly.ttd.biz.feature.admin.srv.sequence.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.admin.mybatis.entity.SequenceEntity;
import com.ly.ttd.biz.feature.admin.mybatis.mapper.SequenceMapper;
import com.ly.ttd.feature.admin.api.dto.SequenceDto;
import com.ly.ttd.feature.admin.api.sequence.SequenceService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 序列 RPC 服务实现
 *
 * @author yong.li
 * @since 2026-06-24
 */
@Service
public class SequenceServiceImpl implements SequenceService {
    @Resource
    private SequenceMapper sequenceMapper;

    @Override
    public Long nextVal(String seqCode) {
        // 先查询是否存在
        SequenceEntity entity = sequenceMapper.selectOne(
                new LambdaQueryWrapper<SequenceEntity>().eq(SequenceEntity::getSeqCode, seqCode)
        );

        if (entity == null) {
            // 不存在则创建，初始值 1
            entity = new SequenceEntity();
            entity.setSeqCode(seqCode);
            entity.setSeqName(seqCode);
            entity.setVal(1L);
            sequenceMapper.insert(entity);
            return 1L;
        }

        // 原子递增
        int updated = sequenceMapper.update(
                new LambdaUpdateWrapper<SequenceEntity>()
                        .eq(SequenceEntity::getSeqCode, seqCode)
                        .setSql("val = val + 1")
        );

        if (updated == 0) {
            throw new RuntimeException("获取序列值失败: " + seqCode);
        }

        // 查询最新值
        entity = sequenceMapper.selectOne(
                new LambdaQueryWrapper<SequenceEntity>().eq(SequenceEntity::getSeqCode, seqCode)
        );
        return entity != null ? entity.getVal() : 1L;
    }

    @Override
    public String generateSeq(String prefix, int length, String seqCode) {

        Long seqVal = nextVal(seqCode);

        StringBuilder builder = new StringBuilder(prefix);
        if (length > 0) {
            for (int i = 0; i < length - prefix.length() - seqVal.toString().length(); i++) {
                builder.append("0");
            }
        }
        builder.append(seqVal);
        return builder.toString();
    }

    @Override
    public Long add(SequenceDto dto) throws BizException {
        SequenceEntity entity = new SequenceEntity();
        BeanUtils.copyProperties(dto, entity);
        sequenceMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void update(SequenceDto dto) throws BizException {
        SequenceEntity entity = new SequenceEntity();
        BeanUtils.copyProperties(dto, entity);
        sequenceMapper.updateById(entity);
    }

    @Override
    public void delete(Long id, String opUser) throws BizException {
        sequenceMapper.deleteById(id);
    }

    @Override
    public SequenceDto queryById(Long id) throws BizException {
        SequenceEntity entity = sequenceMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        SequenceDto dto = new SequenceDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
