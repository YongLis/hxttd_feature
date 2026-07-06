package com.ly.ttd.biz.admin.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ly.ttd.biz.admin.mybatis.entity.DictCodeEntity;
import com.ly.ttd.biz.admin.srv.dict.req.DictCodeQueryReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 字典键值 Mapper
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Mapper
public interface DictCodeMapper extends BaseMapper<DictCodeEntity> {

    IPage<DictCodeEntity> pageQuery(Page<DictCodeEntity> page, @Param("req") DictCodeQueryReq req);
}
