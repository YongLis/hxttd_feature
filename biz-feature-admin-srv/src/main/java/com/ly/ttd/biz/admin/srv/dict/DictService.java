package com.ly.ttd.biz.admin.srv.dict;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.ttd.biz.admin.mybatis.entity.DictEntity;
import com.ly.ttd.biz.admin.srv.dict.req.DictAddReq;
import com.ly.ttd.biz.admin.srv.dict.req.DictUpdateReq;

/**
 * 字典管理服务
 *
 * @author yong.li
 * @since 2026-05-24
 */
public interface DictService extends IService<DictEntity> {

    /**
     * 添加字典
     */
    boolean addDict(DictAddReq req);

    /**
     * 更新字典
     */
    boolean updateDict(DictUpdateReq req);

    /**
     * 删除字典（逻辑删除）
     */
    boolean deleteDict(Long id);

    DictEntity getDictCode(String systemCode, String dictCode);
}
