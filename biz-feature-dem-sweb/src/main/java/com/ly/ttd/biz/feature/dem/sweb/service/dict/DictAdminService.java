package com.ly.ttd.biz.feature.dem.sweb.service.dict;


import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictUpdateReq;
import com.ly.ttd.feature.admin.api.dto.DictDto;

/**
 * 字典管理服务
 *
 * @author yong.li
 * @since 2026-05-24
 */
public interface DictAdminService{

    /**
     * 添加字典
     */
    void addDict(DictAddReq req);

    /**
     * 更新字典
     */
    void updateDict(DictUpdateReq req);

    /**
     * 删除字典（逻辑删除）
     */
    void deleteDict(Long id);

    DictDto getDictCode(String systemCode, String dictCode);
}
