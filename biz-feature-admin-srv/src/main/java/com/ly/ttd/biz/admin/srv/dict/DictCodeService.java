package com.ly.ttd.biz.admin.srv.dict;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ly.ttd.biz.admin.mybatis.entity.DictCodeEntity;
import com.ly.ttd.biz.admin.srv.dict.req.DictCodeAddReq;
import com.ly.ttd.biz.admin.srv.dict.req.DictCodeUpdateReq;

import java.util.List;

/**
 * 字典键值管理服务
 *
 * @author yong.li
 * @since 2026-05-24
 */
public interface DictCodeService extends IService<DictCodeEntity> {

    boolean addDictCode(DictCodeAddReq req);

    boolean updateDictCode(DictCodeUpdateReq req);

    boolean deleteDictCode(Long id);

    List<DictCodeEntity> getByDictId(Long dictId);

    void addDictCode(Long dictId, String dictKey, String dictValue);

}
