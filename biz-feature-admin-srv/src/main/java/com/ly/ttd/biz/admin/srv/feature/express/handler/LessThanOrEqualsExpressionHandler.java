package com.ly.ttd.biz.admin.srv.feature.express.handler;

import com.ly.ttd.biz.admin.srv.feature.express.AbstractExpressionService;
import com.ly.ttd.feature.common.enums.ObjectTypeEnum;
import com.ly.ttd.feature.common.enums.VelocityExpressionTypeEnum;
import com.ly.ttd.feature.common.enums.VelocityValueTypeEnum;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.common.model.DataFieldModel;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * LessThanOrEqualsExpressionHandler 类用于处理小于或等于表达式的逻辑。
 * 该类继承自 AbstractExpressionService，实现了获取操作符、检查不支持的数据类型、
 * 检查表达式值的合法性以及将表达式转换为 JEXL 表达式等功能。
 * <p>
 * 具体来说，该类支持处理动态值和固定值的小于或等于表达式，
 * 并对不支持的数据类型（如 MAP、LIST、BOOLEAN）进行了检查。
 * 当处理动态值时，要求左右表达式返回值类型一致；
 * 当处理固定值且左侧为数字类型时，要求右侧输入为数字。
 *
 * @author yong.li
 * @since 2026/4/8 22:54
 */
@Service
public class LessThanOrEqualsExpressionHandler extends AbstractExpressionService {
    @Override
    public String getOp() {
        return VelocityExpressionTypeEnum.LESS_THAN_OR_EQUAL.getCode();
    }

    private static final List<String> NOT_SUPPORTED_TYPES = Arrays.asList(
            ObjectTypeEnum.DICT.getCode(),
            ObjectTypeEnum.LIST.getCode(),
            ObjectTypeEnum.BOOLEAN.getCode()
    );

    @Override
    public List<String> notSupportTypes() {
        return NOT_SUPPORTED_TYPES;
    }

    /**
     * 左右2边均为表达式时，
     * 1、返回值类型必须一致
     * 2、其中一方为数字类型，表达式不允许选择In, contains
     * 固定值时
     */
    @Override
    public void checkValue(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) throws FeatureBizException {
        if (notSupportTypes().contains(left.getFieldType())) {
            throw new FeatureBizException("01", String.format("数据【%s】返回格式【%s】不支持该表达式【%s】", left.getFieldName(),
                    ObjectTypeEnum.getByCode(left.getFieldType()),
                    VelocityExpressionTypeEnum.getByCode(getOp())));
        }
        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(valueType)) {
            if (!left.getFieldType().equals(right.getFieldType())) {
                throw new FeatureBizException("01", String.format("表达式左【%s】右【%s】数据格式不一致",
                        ObjectTypeEnum.getByCode(left.getFieldType()),
                        ObjectTypeEnum.getByCode(right.getFieldType())));
            }
        }
        if (VelocityValueTypeEnum.FIX_VALUE.getCode().equals(valueType) && Arrays.asList(
                ObjectTypeEnum.DOUBLE.getCode(),
                ObjectTypeEnum.LONG.getCode()
        ).contains(left.getFieldType())
                && !NumberUtils.isDigits(fixValue)) {
            throw new FeatureBizException("01", "表达式左侧数字类型，右侧输入非数字");
        }

    }

    @Override
    public String convertJexl(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) {
        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(valueType)) {
            if (ObjectTypeEnum.DOUBLE.getCode().equals(left.getFieldType())
                    || ObjectTypeEnum.LONG.getCode().equals(left.getFieldType())) {
                return String.format(" %s <= %s ", left.getFieldCode(), right.getFieldCode());
            }

        }

        if (VelocityValueTypeEnum.FIX_VALUE.getCode().equals(valueType)) {
            if (ObjectTypeEnum.DOUBLE.getCode().equals(left.getFieldType())
                    || ObjectTypeEnum.LONG.getCode().equals(left.getFieldType())
                    || ObjectTypeEnum.DECIMAL.getCode().equals(left.getFieldType())
            ) {
                return left.getFieldCode() + " <= " + fixValue;
            }
        }
        return "";
    }
}
