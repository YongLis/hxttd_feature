package com.ly.ttd.biz.admin.srv.feature.res;

import com.alibaba.fastjson.JSON;
import com.ly.ttd.biz.admin.mybatis.entity.FeatureConfigEntity;
import com.ly.ttd.biz.admin.srv.feature.req.FeatureConfigForm;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * 特征配置实体
 *
 * @author yong.li
 * @since 2026-05-16
 */
@Data
public class FeatureConfigDetailRes {

    @Schema(description = "主键ID")
    private Long id;

    /**
     * 资源唯一标识键
     */
    @Schema(description = "资源唯一标识键")
    private String resourceKey;

    /**
     * 资源名称
     */
    @Schema(description = "资源名称")
    private String resourceName;

    /**
     * 资源版本号
     */
    @Schema(description = "资源版本号")
    private String version;

    /**
     * 所属项目ID
     */
    @Schema(description = "所属项目ID")
    private Long projectId;

    /**
     * 特征编码
     */
    @Schema(description = "特征编码")
    private String featureCode;

    @Schema(description = "默认值")
    private String defaultValue;

    @Schema(description = "异常值")
    private String exceptionValue;

    @Schema(description = "超时时间（毫秒）")
    private Long timeout;

    /**
     * 脚本语言
     */
    @Schema(description = "脚本语言")
    private String language;

    /**
     * 条件脚本
     */
    @Schema(description = "条件脚本")
    private String conditionScript;

    /**
     * 主维度脚本
     */
    @Schema(description = "主维度脚本")
    private String mainDimScript;

    /**
     * 从维度脚本
     */
    @Schema(description = "从维度脚本")
    private String slaveDimScript;

    /**
     * 依赖元数据字段(JSON数组)
     */
    @Schema(description = "依赖元数据字段(JSON数组)")
    private String metaFields;

    /**
     * 返回值类型
     */
    @Schema(description = "返回值类型")
    private String returnType;

    /**
     * 指标值类型(FIX/DYNAMIC)
     */
    @Schema(description = "指标值类型(FIX/DYNAMIC)")
    private String valueType;

    /**
     * 指标值脚本
     */
    @Schema(description = "指标值脚本")
    private String valueScript;

    /**
     * 固定值
     */
    @Schema(description = "固定值")
    private String fixValue;

    /**
     * 聚合函数(SUM/COUNT/AVG/MIN/MAX)
     */
    @Schema(description = "聚合函数(SUM/COUNT/AVG/MIN/MAX)")
    private String aggregateMode;

    /**
     * 时间模式(TTL/DAY/MONTH/YEAR)
     */
    @Schema(description = "时间模式(TTL/DAY/MONTH/YEAR)")
    private String timeMode;

    /**
     * 时间单位
     */
    @Schema(description = "时间单位")
    private String timeUnit;

    /**
     * 时间窗口
     */
    @Schema(description = "时间窗口")
    private Integer timeWindow;


    /**
     * 资源JSON
     */
    @Schema(description = "资源JSON配置")
    private FeatureConfigForm configForm;


    @Schema(description = "主维度")
    private String mainDimension;

    @Schema(description = "从维度")
    private String slaveDimension;


    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "更新人")
    private String uptUser;

    @Schema(description = "创建时间")
    private Date crtTime;

    @Schema(description = "更新时间")
    private Date uptTime;

    @Schema(description = "是否删除")
    private Boolean deleted;


    public static FeatureConfigDetailRes fromJson(String json) {
        if (StringUtils.isEmpty(json)) return null;

        FeatureConfigEntity entity = JSON.parseObject(json, FeatureConfigEntity.class);

        FeatureConfigDetailRes res = new FeatureConfigDetailRes();
        BeanUtils.copyProperties(entity, res);
        res.setConfigForm(JSON.parseObject(entity.getResourceJson(), FeatureConfigForm.class));
        return res;
    }

    /**
     * Entity → Res（含 configForm 解析）
     */
    public static FeatureConfigDetailRes fromEntity(FeatureConfigEntity entity) {
        if (entity == null) return null;

        FeatureConfigDetailRes res = new FeatureConfigDetailRes();
        BeanUtils.copyProperties(entity, res);
        if (StringUtils.isNotEmpty(entity.getResourceJson())) {
            FeatureConfigForm form = JSON.parseObject(entity.getResourceJson(), FeatureConfigForm.class);
            res.setConfigForm(form);
            if (form != null) {
                res.setMainDimension(form.getMainDimension());
                res.setSlaveDimension(form.getSlaveDimension());
            }
        }
        return res;
    }

}
