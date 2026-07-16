package com.ly.ttd.biz.feature.dem.sweb.service.feature.express.handler;

import com.alibaba.fastjson2.JSONArray;
import com.ly.ttd.base.exception.BizException;
import com.ly.ttd.biz.feature.dem.sweb.service.feature.express.AbstractExpressionService;
import com.ly.ttd.feature.common.enums.ObjectTypeEnum;
import com.ly.ttd.feature.common.enums.VelocityExpressionTypeEnum;
import com.ly.ttd.feature.common.enums.VelocityValueTypeEnum;
import com.ly.ttd.feature.common.model.DataFieldModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 存在交集
 * 表达式左右均为集合
 *
 * @author yong.li
 * @since 2026/4/8 22:54
 */
@Service
public class ExistExpressionHandler extends AbstractExpressionService {
    @Override
    public String getOp() {
        return VelocityExpressionTypeEnum.EXIST.getCode();
    }

    private static final List<String> NOT_SUPPORTED_TYPES = Arrays.asList(
            ObjectTypeEnum.DICT.getCode(),
            ObjectTypeEnum.BOOLEAN.getCode(),
            ObjectTypeEnum.STRING.getCode(),
            ObjectTypeEnum.LONG.getCode(),
            ObjectTypeEnum.DOUBLE.getCode()
    );

    @Override
    public List<String> notSupportTypes() {
        return NOT_SUPPORTED_TYPES;
    }

    @Override
    public void checkValue(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) throws BizException {

        if (!ObjectTypeEnum.LIST.getCode().equals(left.getFieldType())) {
            throw new BizException("01", "该表达式左侧返回值类型必须为集合");
        }

        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(valueType)) {
            if (!ObjectTypeEnum.LIST.getCode().equals(right.getFieldType())) {
                throw new BizException("01", "该表达式右侧返回值类型必须为集合");
            }
        } else {
            if (StringUtils.isBlank(fixValue)) {
                throw new BizException("01", "该表达式右侧类型必须为集合");
            }
            try {
                List tmp = JSONArray.parseArray(fixValue);
                if (CollectionUtils.isEmpty(tmp)) {
                    throw new BizException("01", "表达式固定值集合数据不允许为空");
                }

            } catch (Exception e) {
                throw new BizException("01", "表达式固定值集合数据不合规范");
            }
        }
    }

    @Override
    public String convertJexl(DataFieldModel left, DataFieldModel right, String valueType, String fixValue) {
        if (VelocityValueTypeEnum.DYNAMIC_VALUE.getCode().equals(valueType)) {
            return String.format("fn.existIntersection(%s,%s)", left.getFieldCode(), right.getFieldCode());
        }

        if (VelocityValueTypeEnum.FIX_VALUE.getCode().equals(valueType)) {
            return String.format("fn.existIntersection(%s,%s)", left.getFieldCode(), fixValue);
        }
        return "";
    }
}
