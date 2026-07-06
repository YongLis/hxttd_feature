package com.ly.ttd.biz.admin.srv.feature.express.handler;

import com.ly.ttd.biz.admin.srv.feature.express.AbstractExpressionService;
import com.ly.ttd.consts.enums.ObjectTypeEnum;
import com.ly.ttd.consts.exception.BizException;
import com.ly.ttd.feature.common.enums.VelocityExpressionTypeEnum;
import com.ly.ttd.feature.common.enums.VelocityValueTypeEnum;
import com.ly.ttd.feature.common.model.DataFieldModel;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * LessThanExpressionHandler 类继承自 AbstractExpressionService，用于处理小于表达式的逻辑。
 * 该类提供了获取操作符、检查不支持的数据类型、验证表达式值以及将表达式转换为 JEXL 格式的功能。
 * <p>
 * 具体功能包括：
 * - 获取小于表达式的操作符代码。
 * - 定义不支持的数据类型，如 MAP、LIST 和 BOOLEAN。
 * - 检查表达式左右两侧的值是否符合要求，包括数据类型一致性和数值有效性。
 * - 将表达式转换为 JEXL 格式，以便在后续的逻辑中使用。
 *
 * @author yong.li
 * @since 2026/4/8 22:54
 */
@Service
public class LessThanExpressionHandler extends AbstractExpressionService {
    @Override
    public String getOp() {
        return VelocityExpressionTypeEnum.LESS_THAN.getCode();
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

    @Override
    public void checkValue(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) throws BizException {

        if (notSupportTypes().contains(left.getFieldType())) {
            throw new BizException("01", String.format("数据【%s】返回格式【%s】不支持该表达式【%s】", left.getFieldName(),
                    ObjectTypeEnum.getByCode(left.getFieldType()),
                    VelocityExpressionTypeEnum.getByCode(getOp())));
        }
        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(valueType)) {
            if (!left.getFieldType().equals(right.getFieldType())) {
                throw new BizException("01", String.format("表达式左【%s】右【%s】数据格式不一致", ObjectTypeEnum.getByCode(left.getFieldType()),
                        ObjectTypeEnum.getByCode(right.getFieldType())));
            }
        }
        if (VelocityValueTypeEnum.FIX_VALUE.getCode().equals(valueType) && Arrays.asList(
                ObjectTypeEnum.LONG.getCode(),
                ObjectTypeEnum.DOUBLE.getCode()
        ).contains(left.getFieldType())
                && !NumberUtils.isDigits(fixValue)) {
            throw new BizException("01", "表达式左侧数字类型，右侧输入非数字");
        }

    }

    @Override
    public String convertJexl(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) {
        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(valueType)) {
            if (ObjectTypeEnum.DOUBLE.getCode().equals(left.getFieldType())
                    || ObjectTypeEnum.LONG.getCode().equals(left.getFieldType())) {
                return String.format(" %s < %s ", left.getFieldCode(), right.getFieldCode());
            }

        }

        if (VelocityValueTypeEnum.FIX_VALUE.getCode().equals(valueType)) {
            if (ObjectTypeEnum.LONG.getCode().equals(left.getFieldType())
                    || ObjectTypeEnum.DOUBLE.getCode().equals(left.getFieldType())
            ) {
                return left.getFieldCode() + " < " + fixValue;
            }
        }
        return "";
    }
}
