package com.ly.ttd.biz.admin.srv.metaField;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.ttd.biz.admin.mybatis.entity.MetaFieldEntity;
import com.ly.ttd.biz.admin.srv.metaField.req.MetaFieldAddReq;
import com.ly.ttd.biz.admin.srv.metaField.req.MetaFieldUpdateReq;

/**
 * 元字段服务
 *
 * @author yong.li
 * @since 2026-05-16
 */
public interface MetaFieldService extends IService<MetaFieldEntity> {

    /**
     * 添加元字段
     */
    boolean addMetaField(MetaFieldAddReq req);

    /**
     * 更新元字段
     */
    boolean updateMetaField(MetaFieldUpdateReq req);

    /**
     * 删除元字段（逻辑删除）
     */
    boolean deleteMetaField(Long id);

}
