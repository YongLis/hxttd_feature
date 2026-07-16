package com.ly.ttd.biz.feature.admin.srv.feature.express.handler;

import com.ly.ttd.biz.feature.admin.srv.feature.express.AbstractExpressionService;
import com.ly.ttd.feature.common.enums.ObjectTypeEnum;
import com.ly.ttd.feature.common.enums.ObjectTypes;
import com.ly.ttd.feature.common.enums.VelocityExpressionTypeEnum;
import com.ly.ttd.feature.common.enums.VelocityValueTypeEnum;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.common.model.DataFieldModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * EqualExpressionHandler 类用于处理相等表达式的逻辑。
 * 它继承自 AbstractExpressionService 类，提供了判断相等操作的具体实现。
 * 该类负责检查操作数类型是否支持、操作数类型是否匹配，以及将操作转换为 JEXL 表达式。
 *
 * @author yong.li
 * @since 2026/4/8 22:54
 */
@Service
public class EqualExpressionHandler extends AbstractExpressionService {
    /**
     * 获取当前表达式处理器支持的操作符代码。
     *
     * @return 表示相等操作符的代码，从 VelocityExpressionTypeEnum.EQUAL 获取。
     */
    @Override
    public String getOp() {
        return VelocityExpressionTypeEnum.EQUAL.getCode();
    }

    // 定义不支持的对象类型列表
    private static final List<String> NOT_SUPPORTED_TYPES = Arrays.asList(
            ObjectTypeEnum.DICT.getCode(),
            ObjectTypeEnum.LIST.getCode()
    );

    /**
     * 获取不支持的对象类型列表。
     *
     * @return 包含不支持的对象类型代码的列表。
     */
    @Override
    public List<String> notSupportTypes() {
        return NOT_SUPPORTED_TYPES;
    }

    /**
     * 检查左右操作数的值是否符合相等表达式的要求。
     * 当左右两边均为表达式时，返回值类型必须一致；当右侧为固定值时，若左侧为数字类型，固定值必须为数字。
     *
     * @param left      左侧操作数的数据帧对象。
     * @param right     右侧操作数的数据帧对象。
     * @param valueType 操作数类型，动态值或固定值。
     * @param fixValue  右侧固定值（如果适用）。
     * @throws FeatureBizException 如果操作数类型不支持或不匹配，抛出业务异常。
     */
    @Override
    public void checkValue(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) throws FeatureBizException {
        // 检查左侧返回值类型是否支持
        if (notSupportTypes().contains(left.getFieldType())) {
            throw new FeatureBizException("01", String.format("数据【%s】返回格式【%s】不支持该表达式【%s】",
                    left.getFieldName(),
                    ObjectTypeEnum.getByCode(left.getFieldType()),
                    VelocityExpressionTypeEnum.getByCode(getOp())));
        }

        if (StringUtils.isBlank(left.getFieldType())) {
            throw new FeatureBizException("01", "表达式左侧返回值类型为空");
        }


        // 当左右两边均为动态值时，检查返回值类型是否一致
        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(valueType)
                && !left.getFieldType().equals(right.getFieldType())) {
            throw new FeatureBizException("01", String.format("表达式左【%s】右【%s】数据格式不一致",
                    ObjectTypeEnum.getByCode(left.getFieldType()),
                    ObjectTypeEnum.getByCode(right.getFieldType())));
        }

        // 当右侧为固定值时，检查左侧为数字类型时，固定值是否为数字
        if (VelocityValueTypeEnum.FIX_VALUE.getCode().equals(valueType) &&
                (Arrays.asList(ObjectTypeEnum.LONG.getCode(),
                        ObjectTypeEnum.DOUBLE.getCode()).contains(left.getFieldType())) &&
                !NumberUtils.isDigits(fixValue)) {
            throw new FeatureBizException("01", "表达式左侧数字类型，右侧输入非数字");
        }
    }

    /**
     * 将相等操作转换为 JEXL 表达式。
     *
     * @param left      左侧操作数的数据帧对象。
     * @param right     右侧操作数的数据帧对象。
     * @param valueType 操作数类型，动态值或固定值。
     * @param fixValue  右侧固定值（如果适用）。
     * @return 表示相等操作的 JEXL 表达式字符串。
     */
    @Override
    public String convertJexl(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) {
        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(valueType)) {
            return String.format(" %s == %s ", left.getFieldCode(), right.getFieldCode());
        }

        if (VelocityValueTypeEnum.FIX_VALUE.getCode().equals(valueType)) {
            return switch (left.getFieldType()) {
                case ObjectTypes.STRING -> String.format(" \"%s\" == %s ", fixValue, left.getFieldCode());
                case ObjectTypes.LONG, ObjectTypes.DOUBLE -> fixValue + " == " + left.getFieldCode();
                case ObjectTypes.BOOLEAN -> fixValue.toLowerCase() + " == " + left.getFieldCode();
                // 假设日期类型以字符串形式存储，需要根据实际情况调整
                case ObjectTypes.DATE -> String.format(" \"%s\" == %s ", fixValue, left.getFieldCode());
                default -> "";
            };
        }
        return "";
    }
}
