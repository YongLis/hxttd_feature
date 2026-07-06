package com.ly.ttd.biz.admin.srv.feature.express;

import com.ly.ttd.feature.common.enums.VelocityValueTypeEnum;
import com.ly.ttd.feature.common.exception.FeatureBizException;
import com.ly.ttd.feature.common.model.DataFieldModel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 表达式符号处理抽象类
 *
 * @author yong.li
 * @since 2026/4/3 10:45
 */
public abstract class AbstractExpressionService {
    /**
     * 获取表达式
     */
    public abstract String getOp();

    /**
     * 表达式不支持的数据类型
     */
    public abstract List<String> notSupportTypes();

    /**
     * 表达式左右数值检查
     */
    public abstract void checkValue(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) throws FeatureBizException;

    /**
     * 获取jexl
     */
    public abstract String convertJexl(DataFieldModel left, DataFieldModel right, String valueType, String fixValue);


    /**
     * 数据验证并生成jexl
     */
    public String checkThenConvertJexl(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) throws FeatureBizException {

        if (null == left || StringUtils.isBlank(left.getFieldCode())) {
            throw new FeatureBizException("01", "表达式左侧为空");
        }

        if (StringUtils.isBlank(left.getFieldType())) {
            throw new FeatureBizException("01", "表达式左侧返回值类型为空");
        }


        if (StringUtils.isBlank(valueType)) {
            throw new FeatureBizException("01", "表达式右侧值类型为空");
        }
        if (VelocityValueTypeEnum.FIX_VALUE.getCode().equals(valueType) && StringUtils.isBlank(fixValue)) {
            throw new FeatureBizException("01", "固定值为空");
        }
        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(valueType)
                && (null == right || StringUtils.isBlank(right.getFieldCode()))) {
            throw new FeatureBizException("01", "表达式右侧为动态值时元字段不允许为空");
        }

        checkValue(left, right, valueType, fixValue);
        return convertJexl(left, right, valueType, fixValue);
    }
}
