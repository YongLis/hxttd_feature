package com.ly.ttd.feature.srv.vel.compile.jexl3;

import org.apache.commons.jexl3.JexlContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地Map上下文（不可删除）
 *
 * @author yong.li
 * @since 2026/9/1 14:30
 */
public class LocalMapContext implements JexlContext {

    /**
     * The wrapped variable map.
     */
    private final Map<String, Object> map;

    /**
     * Creates a MapContext on an automatically allocated underlying HashMap.
     */
    public LocalMapContext() {
        this(null);
    }

    /**
     * Creates a MapContext wrapping an existing user provided map.
     *
     * @param vars the variable map
     */
    public LocalMapContext(Map<String, Object> vars) {
        map = vars == null ? new HashMap<String, Object>() : vars;
    }

    @Override
    public boolean has(String name) {
        return map.containsKey(name);
    }

    @Override
    public Object get(String name) {
        return map.get(name);
    }

    @Override
    public void set(String name, Object value) {
        map.put(name, value);
    }

    /**
     * Clears all variables.
     */
    public void clear() {
        map.clear();
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
