package com.ly.ttd.biz.feature.admin.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ly.ttd.biz.feature.admin.mybatis.entity.DictEntity;
import com.ly.ttd.feature.admin.api.dto.DictDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典配置 Mapper
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Mapper
public interface DictMapper extends BaseMapper<DictEntity> {
    DictDto getDictCode(String systemCode, String dictCode);
}
