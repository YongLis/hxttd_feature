package com.ly.ttd.biz.admin.srv.connector.res;

import com.ly.ttd.biz.admin.mybatis.entity.ConnectorEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * 连接器列表查询响应
 *
 * @author yong.li
 * @since 2026-05-27
 */
@Data
public class ConnectorQueryRes {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "资源唯一标识键")
    private String resourceKey;

    @Schema(description = "资源名称")
    private String resourceName;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "所属项目ID")
    private Long projectId;

    @Schema(description = "连接器类型")
    private String connectorType;

    @Schema(description = "默认值")
    private String defaultValue;

    @Schema(description = "异常值")
    private String exceptionValue;

    @Schema(description = "超时时间（毫秒）")
    private Long timeout;

    @Schema(description = "创建人")
    private String crtUser;

    @Schema(description = "创建时间")
    private Date crtTime;

    @Schema(description = "更新人")
    private String uptUser;

    @Schema(description = "更新时间")
    private Date uptTime;

    @Schema(description = "关联的指标编码列表")
    private List<String> factorCodes;

    @Schema(description = "资源JSON配置")
    private String resourceJson;


    public static ConnectorQueryRes fromEntity(ConnectorEntity entity) {
        ConnectorQueryRes res = new ConnectorQueryRes();
        BeanUtils.copyProperties(entity, res);
        return res;
    }
}
