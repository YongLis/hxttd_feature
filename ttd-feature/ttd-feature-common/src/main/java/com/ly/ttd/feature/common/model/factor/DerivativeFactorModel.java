package com.ly.ttd.feature.common.model.factor;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 衍生指标
 *
 * @author yong.li
 * @since 2026/5/23 21:06
 */
@Data
public class DerivativeFactorModel extends FactorModel {

    /**
     * 关联指标编码
     */
    private List<String> factorCodes;

    /**
     * 连接类型
     */
    private String connectorType;

    /**
     * 连接编码
     */
    private String connectorCode;

    /**
     * 连接参数
     */
    private List<ConnectorParamField> params;
    /**
     * 脚本语言(aviator/groovy)
     */
    private String language;

    /**
     * 前置脚本
     */
    private String conditionScript;


    /**
     * 计算脚本
     */
    private String script;


    /**
     * 指标依赖关系（越靠前的指标、优先计算）
     * A -> B
     * B->C,D
     * C->D,E
     * 将依赖关系转化为计算层级
     * 第一级：D,E
     * 第二级：C
     * 第三级：B
     * 第四级：A
     */
    private List<List<String>> refFactorList;

    /**
     * 返回分层计算顺序：每一层内的节点可以并行计算
     *
     * @param dependencyMap 依赖关系 Map，key 为因子名，value 为它依赖的因子列表
     * @return List<List < String>> 每层一个 List，层内节点可并行
     */
    public void levelTopologicalSort(Map<String, List<FactorDependencyModel>> dependencyMap) {

        Map<String, Set<String>> dependencies = new HashMap<>();
        dependencyMap.forEach((key, value) -> {
            dependencies.put(key, value.stream().map(FactorDependencyModel::getChild).collect(Collectors.toSet()));
        });

        // 1. 计算入度（依赖数）
        Map<String, Integer> inDegree = new HashMap<>();
        for (String node : dependencies.keySet()) {
            inDegree.putIfAbsent(node, 0);
            for (String dep : dependencies.get(node)) {
                inDegree.putIfAbsent(dep, 0);
                inDegree.put(dep, inDegree.get(dep) + 1);
            }
        }

        // 2. 初始化：入度为 0 的节点作为第一层（无依赖，可直接并行计算）
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        List<List<String>> levels = new ArrayList<>();
        int totalNodes = inDegree.size();

        // 3. 按层级处理
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<String> currentLevel = new ArrayList<>();

            // 取出当前层的所有节点（这些节点可并行）
            for (int i = 0; i < levelSize; i++) {
                String node = queue.poll();
                currentLevel.add(node);

                // 更新依赖该节点的下游节点入度
                for (String next : dependencies.keySet()) {
                    if (dependencies.get(next).contains(node)) {
                        int newDegree = inDegree.get(next) - 1;
                        inDegree.put(next, newDegree);
                        // 入度减为 0 时，成为下一层的候选
                        if (newDegree == 0) {
                            queue.offer(next);
                        }
                    }
                }
            }

            levels.add(currentLevel);
        }

        // 检测循环依赖
        int processed = levels.stream().mapToInt(List::size).sum();
        if (processed != totalNodes) {
            throw new IllegalStateException("检测到循环依赖！");
        }

        refFactorList = levels;

    }


    public void buildRefFactorList(Map<String, List<FactorDependencyModel>> dependencyMap) {
        refFactorList = new LinkedList<>();

        List<FactorDependencyModel> childDependencies = dependencyMap.get(getResourceKey());
        if (CollectionUtils.isEmpty(factorCodes)) {
            factorCodes = childDependencies.stream().map(FactorDependencyModel::getChild)
                    .toList();
        }
        for (String childCode : factorCodes) {
            getChildDependencies(childDependencies, dependencyMap, childCode);
        }
        Map<String, List<FactorDependencyModel>> depMap = childDependencies.stream()
                .collect(Collectors.groupingBy(FactorDependencyModel::getChild));

        // 计算顺序
        levelTopologicalSort(depMap);
    }

    private void getChildDependencies(List<FactorDependencyModel> childDependencies, Map<String, List<FactorDependencyModel>> dependencyMap, String parentCode) {
        if (!dependencyMap.containsKey(parentCode)) {
            return;
        }
        List<FactorDependencyModel> dependencies = dependencyMap.get(parentCode);
        if (CollectionUtils.isNotEmpty(dependencies)) {
            childDependencies.addAll(dependencies);
            Set<String> childCodes = dependencies.stream().map(FactorDependencyModel::getChild)
                    .collect(Collectors.toSet());

            for (String childCode : childCodes) {
                getChildDependencies(childDependencies, dependencyMap, childCode);
            }


        }


    }

}

