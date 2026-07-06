package com.ly.ttd.biz.admin.srv.sequence.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ly.ttd.biz.admin.mybatis.entity.SequenceEntity;
import com.ly.ttd.biz.admin.mybatis.mapper.SequenceMapper;
import com.ly.ttd.biz.admin.srv.sequence.SequenceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 序列管理服务实现
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Service
public class SequenceServiceImpl extends ServiceImpl<SequenceMapper, SequenceEntity> implements SequenceService {

    @Override
    @Transactional
    public Long nextVal(String seqCode) {
        // 先查询是否存在
        SequenceEntity entity = getOne(
                new LambdaQueryWrapper<SequenceEntity>().eq(SequenceEntity::getSeqCode, seqCode)
        );

        if (entity == null) {
            // 不存在则创建，初始值 1
            entity = new SequenceEntity();
            entity.setSeqCode(seqCode);
            entity.setSeqName(seqCode);
            entity.setVal(1L);
            save(entity);
            return 1L;
        }

        // 原子递增
        boolean updated = update(
                new LambdaUpdateWrapper<SequenceEntity>()
                        .eq(SequenceEntity::getSeqCode, seqCode)
                        .setSql("val = val + 1")
        );

        if (!updated) {
            throw new RuntimeException("获取序列值失败: " + seqCode);
        }

        // 查询最新值
        entity = getOne(
                new LambdaQueryWrapper<SequenceEntity>().eq(SequenceEntity::getSeqCode, seqCode)
        );
        return entity != null ? entity.getVal() : 1L;
    }
}
