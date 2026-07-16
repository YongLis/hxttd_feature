package com.ly.ttd.biz.feature.admin.srv.feature.express.impl;

import com.ly.ttd.biz.feature.admin.srv.feature.express.ExpressionHandleFactory;
import com.ly.ttd.biz.feature.admin.srv.feature.express.FeatureConfigFormService;
import com.ly.ttd.feature.admin.api.dto.ColumnCondition;
import com.ly.ttd.feature.admin.api.dto.ConditionMeta;
import com.ly.ttd.feature.admin.api.dto.FeatureConfigForm;
import com.ly.ttd.feature.admin.api.dto.MetaFieldDto;
import com.ly.ttd.feature.admin.api.metaField.MetaFieldService;
import com.ly.ttd.feature.common.consts.FeatureConsts;
import com.ly.ttd.feature.common.enums.ScriptType;
import com.ly.ttd.feature.common.enums.VelocityValueTypeEnum;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.common.model.DataFieldModel;
import com.ly.ttd.feature.common.model.vel.FeatureConfigModel;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author yong.li
 * @since 2026/6/2 16:03
 */
@Service
public class FeatureConfigFormServiceImpl implements FeatureConfigFormService {
    @Resource
    private MetaFieldService metaFieldService;

    @Override
    public FeatureConfigModel convertForm(FeatureConfigForm form) throws FeatureBizException {
        Map<String, MetaFieldDto> map = metaFieldService.getByProjectId(form.getProjectId());
        FeatureConfigModel configModel = new FeatureConfigModel();
//        configModel.setId();
        configModel.setResourceKey(form.getResourceKey());
        configModel.setResourceName(form.getResourceName());
        configModel.setVersion(form.getVersion());
        configModel.setProjectId(form.getProjectId());
        configModel.setFeatureCode(form.getFeatureCode());
        configModel.setLanguage(ScriptType.JEXL.getCode());
        configModel.setConditionScript(convertCondition(form.getConditions(), map));
        configModel.setMainDimScript(form.getMainDimension());
        configModel.setSlaveDimScript(form.getSlaveDimension());
        configModel.setMetaFields(parseMetaFields(form).stream().toList());
        configModel.setReturnType(form.getReturnType());
        configModel.setValueType(form.getValueType());
        configModel.setValueScript(form.getValueDimension());
        configModel.setFixValue(form.getFixValue());
        configModel.setAggregateMode(form.getAggregateMode());
        configModel.setTimeMode(form.getTimeMode());
        configModel.setTimeUnit(form.getTimeUnit());
        configModel.setTimeWindow(form.getTimeWindow());
        configModel.setDefaultValue(form.getDefaultValue());
        configModel.setExceptionValue(form.getExceptionValue());
        configModel.setTimeout(form.getTimeout());
        configModel.setMainDimension(form.getMainDimension());
        configModel.setSlaveDimension(form.getSlaveDimension());
        return configModel;
    }

    private String convertCondition(List<ColumnCondition> conditions, Map<String, MetaFieldDto> metaFieldMap) throws FeatureBizException {
        List<String> condScripts = new ArrayList<>();
        for (ColumnCondition condition : conditions) {
            List<String> scripts = new ArrayList<>();
            for (ConditionMeta meta : condition.getColumns()) {
                DataFieldModel leftField = meta.getLeftField(metaFieldMap);
                DataFieldModel rightField = meta.getRightField(metaFieldMap);

                String script = ExpressionHandleFactory.getInstance(meta.getOp())
                        .checkThenConvertJexl(leftField, rightField, meta.getRightType(), meta.getRight());

                if (StringUtils.isEmpty(script)) {
                    throw new FeatureBizException("01", "特征配置转换异常");
                }
                scripts.add(script);
            }

            if (CollectionUtils.isNotEmpty(scripts)) {
                condScripts.add(StringUtils.join(scripts, " && "));
            }
        }

        if (CollectionUtils.isEmpty(condScripts)) {
            throw new FeatureBizException("01", "特征配置转换异常");
        }
        return StringUtils.join(condScripts, " || ");
    }

    /**
     * 解析条件中元字段
     */
    private Set<String> parseMetaFields(FeatureConfigForm form) {
        Set<String> metaFields = new java.util.HashSet<>();
        List<ColumnCondition> conditions = form.getConditions();
        for (ColumnCondition condition : conditions) {
            for (ConditionMeta meta : condition.getColumns()) {
                metaFields.add(meta.getLeft());
                if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(meta.getRightType())) {
                    metaFields.add(meta.getRight());
                }
            }
        }
        metaFields.add(FeatureConsts.TRANSACTION_ID);
        metaFields.add(FeatureConsts.ACCESS_POINT);
        metaFields.add(form.getMainDimension());
        metaFields.add(form.getSlaveDimension());
        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(form.getValueType())) {
            metaFields.add(form.getValueDimension());
        }
        return metaFields;
    }
}
