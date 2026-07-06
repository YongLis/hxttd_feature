# ttd-feature-cfg-jar — 特征配置容器

## 概述

`ttd-feature-cfg-jar`
是特征平台的配置中心模块，负责从数据库加载所有配置资源并维护为内存缓存，提供秒级热加载能力。所有模块通过 `FeatureConfigurationAware`
接口获取配置引用。

## 包结构

```
com.ly.ttd.feature.cfg/
├── config/                        # 配置类
│   ├── FeatureConfiguration.java       # 12 个 ConcurrentHashMap 缓存容器
│   └── ThreadPoolNames.java            # 线程池名称常量
├── resource/                      # 资源加载器（按资源类型）
│   ├── MetaFieldResourceLoader.java    # 元字段加载器
│   ├── FeatureConfigResourceLoader.java # 特征配置加载器
│   ├── FactorResourceLoader.java       # 指标加载器
│   ├── ConnectorResourceLoader.java    # 连接器加载器
│   ├── AccessPointResourceLoader.java  # 接入点加载器
│   ├── DataStructResourceLoader.java   # 数据集加载器
│   ├── DictResourceLoader.java         # 字典加载器
│   └── ProjectResourceLoader.java      # 项目加载器
└── connector/                     # 连接器配置
```

## 核心设计

### FeatureConfiguration 缓存结构

```java
public class FeatureConfiguration {
    // 12 个 ConcurrentHashMap，覆盖全部配置类型
    Map<String, MetaFieldModel>            metaFieldMap;          // resourceKey → MetaFieldModel
    Map<String, List<String>>              pointCodeMetaFieldMap; // pointCode → metaFieldKeys
    Map<String, FeatureConfigModel>        featureMap;            // featureCode → FeatureConfigModel
    Map<Long,   List<FeatureConfigModel>>  projectFeatureMap;     // projectId → FeatureConfigs
    Map<String, FactorModel>               factorMap;             // resourceKey → FactorModel
    Map<String, String>                    featureFactorMap;      // factorCode → refFeatureCode
    Map<String, ConnectorModel>            connectorMap;          // resourceKey → ConnectorModel
    Map<String, AccessPointModel>          accessPointMap;        // pointCode → AccessPointModel
    // ... 更多缓存
    ThreadPoolExecutor                    threadPool(ThreadPoolNames name);  // 动态线程池
}
```

### 资源加载与热刷新

```
Application启动
  → ResourceLoader.loadAll()  (从数据库全量加载)
    → FeatureConfiguration  (写入12个 ConcurrentHashMap)
      → CacheRefreshService  (定时间隔刷新，秒级生效)
```

### FeatureConfigurationAware 接口

```java
public interface FeatureConfigurationAware {
    void setFeatureConfiguration(FeatureConfiguration configuration);
}
```

所有需要配置的业务 Service 实现此接口，由 ResourceLoader 在加载完成后统一注入。

## 依赖

- `ttd-feature-common` — 模型定义

## 关键约定

- 所有缓存 Map 使用 `ConcurrentHashMap` 保证读写安全
- 资源加载器只在启动时由 ResourceLoader 调用，Service 层只读
- `setFeatureConfiguration` 是唯一的配置注入入口
