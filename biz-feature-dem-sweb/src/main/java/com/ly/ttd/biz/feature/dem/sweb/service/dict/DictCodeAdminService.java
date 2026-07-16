package com.ly.ttd.biz.feature.dem.sweb.service.dict;


import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictCodeAddReq;
import com.ly.ttd.biz.feature.dem.sweb.service.dict.req.DictCodeUpdateReq;
import com.ly.ttd.feature.admin.api.dto.DictCodeDto;

import java.util.List;

/**
 * 字典键值管理服务
 *
 * @author yong.li
 * @since 2026-05-24
 */
public interface DictCodeAdminService {

    void addDictCode(DictCodeAddReq req);

    void updateDictCode(DictCodeUpdateReq req);

    void deleteDictCode(Long id);

    List<DictCodeDto> getByDictId(Long dictId);

    List<DictCodeDto> getDictCode(String systemCode, String dictCode);

}
