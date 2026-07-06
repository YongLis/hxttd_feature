package com.ly.ttd.biz.admin.srv.feature.express.handler;

import com.ly.ttd.biz.admin.srv.feature.express.AbstractExpressionService;
import com.ly.ttd.feature.common.enums.ObjectTypeEnum;
import com.ly.ttd.feature.common.enums.VelocityExpressionTypeEnum;
import com.ly.ttd.feature.common.enums.VelocityValueTypeEnum;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.common.model.DataFieldModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * InExpressionHandler 类用于处理包含 `IN` 表达式的逻辑。
 * 它继承自 AbstractExpressionService 类，提供了对 `IN` 表达式的操作符获取、
 * 不支持类型检查、值检查以及转换为 JEXL 表达式的功能。
 *
 * @author yong.li
 * @since 2026/4/8 22:54
 */
@Service
public class InExpressionHandler extends AbstractExpressionService {
    @Override
    public String getOp() {
        return VelocityExpressionTypeEnum.IN.getCode();
    }

    private static final List<String> NOT_SUPPORTED_TYPES = Arrays.asList(
            ObjectTypeEnum.DICT.getCode(),
            ObjectTypeEnum.BOOLEAN.getCode()
    );

    @Override
    public List<String> notSupportTypes() {
        return NOT_SUPPORTED_TYPES;
    }

    @Override
    public void checkValue(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) throws FeatureBizException {

        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(valueType)) {
            if (left.getFieldType().equals(right.getFieldType())) {
                throw new FeatureBizException("01", "该表达式不支持左右2边返回类型一致");
            }
            if (!ObjectTypeEnum.LIST.getCode().equals(right.getFieldType())) {
                throw new FeatureBizException("01", "表达式右侧必须为集合类型");
            }
        } else {
            // 右侧为固定值时，固定值不允许为空
            if (StringUtils.isBlank(fixValue)) {
                throw new FeatureBizException("01", "表达式右侧固定值不允许为空");
            }
            // 右侧为固定值时，左侧表达式返回类型不能是集合类型
            if (ObjectTypeEnum.LIST.getCode().equals(left.getFieldType())) {
                throw new FeatureBizException("01", "表达式左侧返回值类型不能为集合类型");
            }
            // 右侧为固定值时，左侧表达式返回类型不能是日期类型
            if (ObjectTypeEnum.DATE.getCode().equals(left.getFieldType())) {
                throw new FeatureBizException("01", "表达式左侧返回值类型不能为日期类型");
            }
        }
    }

    @Override
    public String convertJexl(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) {
        String leftValue = VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(valueType)
                ? right.getFieldCode()
                : fixValue;

        return String.format("%s.contains(%s)", leftValue, left.getFieldCode());
    }
}
