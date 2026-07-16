package com.ly.ttd.biz.feature.dem.sweb.service.feature.express.handler;

import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.express.AbstractExpressionService;
import com.ly.ttd.feature.common.enums.ObjectTypeEnum;
import com.ly.ttd.feature.common.enums.VelocityExpressionTypeEnum;
import com.ly.ttd.feature.common.enums.VelocityValueTypeEnum;
import com.ly.ttd.feature.common.model.DataFieldModel;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * NotEqualExpressionHandler 类用于处理不等于表达式的逻辑。
 * 它继承自 AbstractExpressionService 类，实现了获取操作符、检查不支持的数据类型、
 * 检查值的合法性以及将表达式转换为 JEXL 表达式等功能。
 * <p>
 * 该类支持动态值和固定值的不等于表达式处理，并且会对不支持的数据类型和不合法的值进行检查，
 * 若不符合条件将抛出 AppBizException 异常。
 *
 * @author yong.li
 * @since 2026/4/8 22:54
 */
@Service
public class NotEqualExpressionHandler extends AbstractExpressionService {
    @Override
    public String getOp() {
        return VelocityExpressionTypeEnum.NOT_EQUAL.getCode();
    }

    private static final List<String> NOT_SUPPORTED_TYPES = Arrays.asList(
            ObjectTypeEnum.DICT.getCode(),
            ObjectTypeEnum.LIST.getCode(),
            ObjectTypeEnum.DATE.getCode()
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
        if (VelocityValueTypeEnum.FIX_VALUE.getCode().equals(valueType)) {
            if (Arrays.asList(ObjectTypeEnum.DOUBLE.getCode(),
                    ObjectTypeEnum.LONG.getCode()
            ).contains(left.getFieldType())
                    && !NumberUtils.isDigits(fixValue)) {
                throw new BizException("01", "表达式左侧数字类型，右侧输入非数字");
            }
        }
    }

    @Override
    public String convertJexl(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) {
        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(valueType)) {
            return String.format(" %s != %s ", left.getFieldCode(), right.getFieldCode());
        }

        if (VelocityValueTypeEnum.FIX_VALUE.getCode().equals(valueType)) {
            return fixValue + " != " + left.getFieldCode();
        }
        return "";
    }
}
