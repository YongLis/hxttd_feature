package com.ly.ttd.feature.common.event.hbase;

/**
 * @author yong.li
 * @since 2026/6/22 13:38
 */
public class HbaseData {

    private String rowKey;
    private String family;
    private String qualifier;
    private String value;

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
