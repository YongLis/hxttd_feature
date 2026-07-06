package com.ly.ttd.feature.common.tip;


import java.util.List;

/**
 * @author yong.li
 * @since 2026/1/26 13:08
 */
public class FunctionTip {
    /**
     * 函数分组
     */
    private String group;
    /**
     * 函数名称
     */
    private String methodName;

    /**
     * 函数描述
     */
    private String description;

    /**
     * 函数参数
     */
    private List<ParamObj> params;

    /**
     * 函数返回值
     */
    private String returnObj;

    /**
     * 快捷代码
     */
    private String shortcutCode;
    /**
     * 提示时展示信息
     */
    private String info;


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ParamObj> getParams() {
        return params;
    }

    public void setParams(List<ParamObj> params) {
        this.params = params;
    }

    public String getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(String returnObj) {
        this.returnObj = returnObj;
    }

    public FunctionTip() {
    }

    public FunctionTip(String group, String methodName, String description, List<ParamObj> params, String returnObj) {
        this.group = group;
        this.methodName = methodName;
        this.description = description;
        this.params = params;
        this.returnObj = returnObj;
    }

    public String getShortcutCode() {
        StringBuilder builder = new StringBuilder();
        builder.append(methodName)
                .append("(");
        if (params != null && !params.isEmpty()) {
            for (ParamObj param : params) {
                builder.append(param.getType())
                        .append(" ")
                        .append(param.getName())
                        .append(",");
            }
        }
        return builder.toString().substring(0, builder.toString().length() - 1) + ")";
    }

    public String getInfo() {
        StringBuilder builder = new StringBuilder(getShortcutCode());
        builder.append("\n")
                .append("返回值类型：")
                .append(returnObj);

        return builder.toString();


    }
}
