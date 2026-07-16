package com.ly.ttd.biz.feature.dem.sweb.service.dict.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 字典键值查询响应
 *
 * @author yong.li
 * @since 2026-05-24
 */
@Data
public class DictCodeQueryRes {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "字典ID")
    private Long dictId;

    @Schema(description = "字典键")
    private String dictKey;

    @Schema(description = "字典值")
    private String dictValue;

    @Schema(description = "是否删除")
    private Boolean deleted;

    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "创建时间")
    private Date crtTime;
}
