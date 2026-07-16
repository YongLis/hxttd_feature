package com.ly.ttd.biz.feature.admin.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ly.ttd.biz.feature.admin.mybatis.entity.UserAccountEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户账号 Mapper
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccountEntity> {
    List<UserAccountEntity> selectAll();

}
