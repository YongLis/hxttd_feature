package com.ly.ttd.biz.admin.srv.dict.req;

import com.ly.ttd.biz.admin.req.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典查询请求
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class DictQueryReq extends PageQuery {

    @Schema(description = "系统编码", example = "SYS_COMMON")
    private String systemCode;

    @Schema(description = "字典编码", example = "DICT_GENDER")
    private String dictCode;

    @Schema(description = "字典名称", example = "性别")
    private String dictName;

    @Schema(description = "是否删除", example = "true")
    private String deleted;
}
