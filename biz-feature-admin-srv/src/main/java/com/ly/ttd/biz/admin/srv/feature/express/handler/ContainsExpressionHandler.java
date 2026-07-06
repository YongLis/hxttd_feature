package com.ly.ttd.biz.admin.srv.feature.express.handler;

import com.ly.ttd.biz.admin.srv.feature.express.AbstractExpressionService;
import com.ly.ttd.feature.common.enums.ObjectTypeEnum;
import com.ly.ttd.feature.common.enums.VelocityExpressionTypeEnum;
import com.ly.ttd.feature.common.enums.VelocityValueTypeEnum;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.common.model.DataFieldModel;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * ContainsExpressionHandler 类是用于处理包含表达式的服务类，它继承自 AbstractExpressionService。
 * 该类主要用于验证数据是否符合包含表达式的要求，并将其转换为 JEXL 表达式。
 * <p>
 * 具体功能包括：
 * 1. 通过 getOp 方法获取当前表达式的操作符代码，这里为 VelocityExpressionTypeEnum.CONTAINS。
 * 2. 定义不支持的数据类型，如 MAP、BOOLEAN、LIST、LONG、DOUBLE 等，通过 notSupportTypes 方法返回。
 * 3. 在 checkValue 方法中，对传入的数据进行合法性检查，确保左侧返回值类型为字符串，且当右侧为动态值时也为字符串。
 * 4. convertJexl 方法根据不同的情况（动态值或固定值）将表达式转换为 JEXL 表达式。
 *
 * @author yong.li
 * @since 2026/4/8 22:54
 */
@Service
public class ContainsExpressionHandler extends AbstractExpressionService {
    @Override
    public String getOp() {
        return VelocityExpressionTypeEnum.CONTAINS.getCode();
    }


    private static final List<String> NOT_SUPPORTED_TYPES = Arrays.asList(
            ObjectTypeEnum.DICT.getCode(),
            ObjectTypeEnum.BOOLEAN.getCode(),
            ObjectTypeEnum.LIST.getCode(),
            ObjectTypeEnum.LONG.getCode(),
            ObjectTypeEnum.DOUBLE.getCode(),
            ObjectTypeEnum.DECIMAL.getCode()
    );

    @Override
    public List<String> notSupportTypes() {
        return NOT_SUPPORTED_TYPES;
    }

    @Override
    public void checkValue(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) throws FeatureBizException {
        if (notSupportTypes().contains(left.getFieldType())) {
            throw new FeatureBizException("01", String.format("数据【%s】返回格式【%s】不支持该表达式【%s】", left.getFieldName(), ObjectTypeEnum.getByCode(left.getFieldType()), VelocityExpressionTypeEnum.getByCode(getOp())));
        }
        if (!ObjectTypeEnum.STRING.getCode().equals(left.getFieldType())) {
            throw new FeatureBizException("01", "该表达式左侧返回值类型必须为字符串");
        }

        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(valueType)) {
            if (!ObjectTypeEnum.STRING.getCode().equals(right.getFieldType())) {
                throw new FeatureBizException("01", "该表达式右侧返回值类型必须为字符串");
            }
        }
    }

    @Override
    public String convertJexl(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) {
        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(valueType)) {
            return String.format("%s.contains(%s)", left.getFieldCode(), right.getFieldCode());
        }

        if (VelocityValueTypeEnum.FIX_VALUE.getCode().equals(valueType)) {
            return String.format("%s.contains(%s)", left.getFieldCode(), fixValue);
        }
        return "";
    }
}
