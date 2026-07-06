package com.ly.ttd.feature.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 元字段计算方法
 *
 * @author yong.li
 * @since 2026/3/28 17:46
 */
@Getter
@AllArgsConstructor
public enum FactorFunctionMethodEnum {
    //    GET_PHONE_LOCATION_TWO_DIGIT_CODE("get_phone_location_two_digit_code", "获取手机归属地二字码"),
//    GET_PHONE_LOCATION_THREE_DIGIT_CODE("get_phone_location_three_digit_code", "获取手机归属地三字码"),
//    PHONE_AREA_CODE_TO_COUNTRY_TWO_DIGIT_CODE("phone_area_code_to_country_two_digit_code", "手机区号转国家二字码"),
//    PHONE_AREA_CODE_TO_COUNTRY_THREE_DIGIT_CODE("phone_area_code_to_country_three_digit_code", "手机区号转国家三字码"),
//    GET_IP_LOCATION_TWO_DIGIT_CODE("get_ip_location_two_digit_code", "IP归属地二字码"),
//    GET_IP_LOCATION_THREE_DIGIT_CODE("get_ip_location_three_digit_code", "IP归属地三字码"),
//    CARD_BIN_TRUNCATE_SIX_DIGITS("card_bin_truncate_six_digits", "卡bin截取6位"),
//    GET_CARD_ISSUING_COUNTRY("get_card_issuing_country", "获取发卡国"),
//    GET_CARD_ISSUING_INSTITUTION("get_card_issuing_institution", "获取发卡机构"),
    DECRYPT("decrypt", "解密"),
    ENCRYPT("encrypt", "加密"),
//    AIRPORT_TWO_DIGIT_CODE_TO_COUNTRY_TWO_DIGIT_CODE("airport_two_digit_code_to_country_two_digit_code", "机场二字码转国家二字码"),
//    AIRPORT_TWO_DIGIT_CODE_TO_COUNTRY_THREE_DIGIT_CODE("airport_two_digit_code_to_country_three_digit_code", "机场二字码转国家三字码")
    ;
    private final String code;
    private final String name;

    public static String getByCode(String code) {
        for (FactorFunctionMethodEnum factorGenerateTypeEnum : FactorFunctionMethodEnum.values()) {
            if (factorGenerateTypeEnum.getCode().equals(code)) {
                return factorGenerateTypeEnum.getName();
            }
        }
        return null;
    }
}
