package com.ly.ttd.feature.srv.factor.velocity.vel.filter;

import com.ly.ttd.feature.common.ctx.TxnParamContext;
import com.ly.ttd.feature.common.enums.VelocityValueTypeEnum;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.common.model.vel.FeatureConfigModel;
import com.ly.ttd.feature.srv.factor.velocity.MetaFieldQueryService;
import com.ly.ttd.feature.srv.factor.velocity.dto.FeatureScriptResult;
import com.ly.ttd.feature.srv.factor.velocity.vel.sink.VelocityTimeOutBuilder;
import com.ly.ttd.language.srv.ScriptLanguageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * VelocityFilterServiceImpl 单元测试
 *
 * @author yong.li
 * @since 2026-07-04
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("VelocityFilterServiceImpl 单元测试")
class VelocityFilterServiceImplTest {

    @InjectMocks
    private VelocityFilterServiceImpl velocityFilterService;

    @Mock
    private ScriptLanguageService scriptLanguageService;

    @Mock
    private MetaFieldQueryService metaFieldQueryService;

    private MockedStatic<VelocityTimeOutBuilder> timeOutBuilderMock;

    private static final String TXN_ID = "TXN20260704001";
    private static final String POINT_CODE = "POINT_001";
    private static final String FEATURE_CODE = "FEA_001";

    @BeforeEach
    void setUp() {
        timeOutBuilderMock = mockStatic(VelocityTimeOutBuilder.class);
        timeOutBuilderMock.when(() -> VelocityTimeOutBuilder.getWindowExpireTime(any()))
                .thenReturn(new Date());
    }

    @AfterEach
    void tearDown() {
        timeOutBuilderMock.close();
    }

    // ==================== executeScript — 固定值 ====================

    @Test
    @DisplayName("executeScript — 固定值类型，条件=true，master/slave 为单值")
    void testExecuteScript_FixedValue_SingleMasterSlave() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        config.setValueType("FIXED");
        config.setFixValue("100");
        Map<String, Object> params = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)           // condition
                .thenReturn("master_A")     // master dim
                .thenReturn("slave_A");     // slave dim

        FeatureScriptResult result = velocityFilterService.executeScript(ctx, config, params);

        assertTrue(result.getCondition());
        assertEquals(Collections.singletonList("master_A"), result.getMaster());
        assertEquals(Collections.singletonList("slave_A"), result.getSlave());
        assertEquals(BigDecimal.valueOf(100), result.getValue());
        verify(scriptLanguageService, times(3)).execute(any());
    }

    @Test
    @DisplayName("executeScript — 动态值类型，value 由脚本计算返回")
    void testExecuteScript_DynamicValue_FromScript() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        config.setValueType(VelocityValueTypeEnum.DYNAMIC_VALUE.getCode());
        config.setValueScript("return 250;");
        Map<String, Object> params = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(false)          // condition → false
                .thenReturn("m1")           // master dim
                .thenReturn("s1")           // slave dim
                .thenReturn(BigDecimal.valueOf(250)); // value script

        FeatureScriptResult result = velocityFilterService.executeScript(ctx, config, params);

        assertEquals(BigDecimal.valueOf(250), result.getValue());
        verify(scriptLanguageService, times(4)).execute(any());
    }

    @Test
    @DisplayName("executeScript — master 返回 null，formatReturnValue 返回空列表")
    void testExecuteScript_NullMaster_ReturnsEmptyList() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        config.setValueType("FIXED");
        config.setFixValue("50");
        Map<String, Object> params = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)           // condition
                .thenReturn(null)           // master → null
                .thenReturn("slave_A");     // slave

        FeatureScriptResult result = velocityFilterService.executeScript(ctx, config, params);

        assertEquals(Collections.emptyList(), result.getMaster());
        assertEquals(Collections.singletonList("slave_A"), result.getSlave());
    }

    @Test
    @DisplayName("executeScript — slave 返回 null，formatReturnValue 返回空列表")
    void testExecuteScript_NullSlave_ReturnsEmptyList() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        config.setValueType("FIXED");
        config.setFixValue("50");
        Map<String, Object> params = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)           // condition
                .thenReturn("master_A")     // master
                .thenReturn(null);          // slave → null

        FeatureScriptResult result = velocityFilterService.executeScript(ctx, config, params);

        assertEquals(Collections.singletonList("master_A"), result.getMaster());
        assertEquals(Collections.emptyList(), result.getSlave());
    }

    @Test
    @DisplayName("executeScript — master 返回 Collection，formatReturnValue 转为 List<String>")
    void testExecuteScript_CollectionMaster_ConvertedToList() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        config.setValueType("FIXED");
        config.setFixValue("10");
        Map<String, Object> params = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)                                           // condition
                .thenReturn(Arrays.asList("m1", "m2", "m3"))                // master → Collection
                .thenReturn("slave_A");                                     // slave

        FeatureScriptResult result = velocityFilterService.executeScript(ctx, config, params);

        assertEquals(Arrays.asList("m1", "m2", "m3"), result.getMaster());
        assertEquals(Collections.singletonList("slave_A"), result.getSlave());
    }

    // ==================== filterData 条件判断 ====================

    @Test
    @DisplayName("filterData — 条件脚本返回 false，返回空列表")
    void testFilterData_ConditionFalse_ReturnsEmpty() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        Map<String, Object> metaValues = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(false); // condition → false

        List<VelEventData> result = velocityFilterService.buildVelEventData(ctx, config, metaValues);

        assertTrue(result.isEmpty());
        verify(scriptLanguageService, times(1)).execute(any());
    }

    @Test
    @DisplayName("filterData — 异常时捕获并返回空列表")
    void testFilterData_Exception_ReturnsEmpty() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        Map<String, Object> metaValues = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenThrow(new RuntimeException("script error"));

        List<VelEventData> result = velocityFilterService.buildVelEventData(ctx, config, metaValues);

        assertTrue(result.isEmpty());
    }

    // ==================== filterData: 一对一 / 一对多 / 多对一 / 多对多 ====================

    @Test
    @DisplayName("filterData — master=1, slave=3 (一对多)，生成 3 条 VelEventData")
    void testFilterData_OneMaster_MultiSlave_GeneratesThreeEvents() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        Map<String, Object> metaValues = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)                                   // condition
                .thenReturn("master_A")                             // master dim → single
                .thenReturn(Arrays.asList("s1", "s2", "s3"));       // slave dim → 3 values

        List<VelEventData> result = velocityFilterService.buildVelEventData(ctx, config, metaValues);

        assertEquals(3, result.size());
        for (VelEventData dto : result) {
            assertEquals("master_A", dto.getMasterValue());
        }
        assertEquals("s1", result.get(0).getSlaveValue());
        assertEquals("s2", result.get(1).getSlaveValue());
        assertEquals("s3", result.get(2).getSlaveValue());
    }

    @Test
    @DisplayName("filterData — master=3, slave=1 (多对一)，生成 3 条 VelEventData")
    void testFilterData_MultiMaster_OneSlave_GeneratesThreeEvents() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        Map<String, Object> metaValues = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)                                   // condition
                .thenReturn(Arrays.asList("m1", "m2", "m3"))        // master dim → 3 values
                .thenReturn("slave_A");                             // slave dim → single

        List<VelEventData> result = velocityFilterService.buildVelEventData(ctx, config, metaValues);

        assertEquals(3, result.size());
        for (VelEventData dto : result) {
            assertEquals("slave_A", dto.getSlaveValue());
        }
        assertEquals("m1", result.get(0).getMasterValue());
        assertEquals("m2", result.get(1).getMasterValue());
        assertEquals("m3", result.get(2).getMasterValue());
    }

    @Test
    @DisplayName("filterData — master=3, slave=3 (多对多按下标配对)，生成 3 条 VelEventData")
    void testFilterData_MultiMaster_MultiSlave_PairedByIndex() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        Map<String, Object> metaValues = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)                                   // condition
                .thenReturn(Arrays.asList("m1", "m2", "m3"))        // master dim → 3 values
                .thenReturn(Arrays.asList("s1", "s2", "s3"));       // slave dim → 3 values

        List<VelEventData> result = velocityFilterService.buildVelEventData(ctx, config, metaValues);

        assertEquals(3, result.size());
        assertEquals("m1", result.get(0).getMasterValue());
        assertEquals("s1", result.get(0).getSlaveValue());
        assertEquals("m2", result.get(1).getMasterValue());
        assertEquals("s2", result.get(1).getSlaveValue());
        assertEquals("m3", result.get(2).getMasterValue());
        assertEquals("s3", result.get(2).getSlaveValue());
    }

    @Test
    @DisplayName("filterData — master=2, slave=3 (数量不匹配)，返回空列表")
    void testFilterData_SizeMismatch_ReturnsEmpty() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        Map<String, Object> metaValues = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)                                   // condition
                .thenReturn(Arrays.asList("m1", "m2"))              // master → 2 values
                .thenReturn(Arrays.asList("s1", "s2", "s3"));       // slave → 3 values

        List<VelEventData> result = velocityFilterService.buildVelEventData(ctx, config, metaValues);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("filterData — master=1, slave=1 (一对一)，生成 1 条 VelEventData")
    void testFilterData_OneMaster_OneSlave() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        Map<String, Object> metaValues = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)           // condition
                .thenReturn("master_A")     // master → single
                .thenReturn("slave_A");     // slave → single

        List<VelEventData> result = velocityFilterService.buildVelEventData(ctx, config, metaValues);

        assertEquals(1, result.size());
        assertEquals("master_A", result.get(0).getMasterValue());
        assertEquals("slave_A", result.get(0).getSlaveValue());
    }

    // ==================== filterData: 空值校验 ====================

    @Test
    @DisplayName("filterData — master 为空列表，返回空列表")
    void testFilterData_EmptyMaster_ReturnsEmpty() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        Map<String, Object> metaValues = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)                   // condition
                .thenReturn(Collections.emptyList()) // master → empty
                .thenReturn("slave_A");             // slave

        List<VelEventData> result = velocityFilterService.buildVelEventData(ctx, config, metaValues);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("filterData — slave 为空列表，返回空列表")
    void testFilterData_EmptySlave_ReturnsEmpty() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        Map<String, Object> metaValues = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)                   // condition
                .thenReturn("master_A")             // master
                .thenReturn(Collections.emptyList()); // slave → empty

        List<VelEventData> result = velocityFilterService.buildVelEventData(ctx, config, metaValues);

        assertTrue(result.isEmpty());
    }

    // ==================== filterData: 动态值 ====================

    @Test
    @DisplayName("filterData — 动态值类型，value 由脚本计算")
    void testFilterData_DynamicValue_FromScript() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        config.setValueType(VelocityValueTypeEnum.DYNAMIC_VALUE.getCode());
        config.setValueScript("return 99;");
        Map<String, Object> metaValues = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)                   // condition
                .thenReturn("master_A")             // master
                .thenReturn("slave_A")              // slave
                .thenReturn(BigDecimal.valueOf(99)); // value script

        List<VelEventData> result = velocityFilterService.buildVelEventData(ctx, config, metaValues);

        assertEquals(1, result.size());
        assertEquals(BigDecimal.valueOf(99), result.get(0).getVelValue());
    }

    // ==================== filterData: VelEventData 字段完整性 ====================

    @Test
    @DisplayName("filterData — 返回的 VelEventData 包含完整的上下文字段")
    void testFilterData_VelEventData_CompleteFields() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        config.setAggregateMode("SUM");
        config.setTimeMode("TTL");
        config.setTimeUnit("MINUTE");
        config.setTimeWindow(30);
        Map<String, Object> metaValues = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)           // condition
                .thenReturn("master_A")     // master
                .thenReturn("slave_A");     // slave

        List<VelEventData> result = velocityFilterService.buildVelEventData(ctx, config, metaValues);

        assertEquals(1, result.size());
        VelEventData dto = result.get(0);
        assertEquals(POINT_CODE, dto.getPointCode());
        assertEquals(TXN_ID, dto.getTxnId());
        assertNotNull(dto.getTxnTime());
        assertEquals(FEATURE_CODE, dto.getFeatureCode());
        assertEquals("master_A", dto.getMasterValue());
        assertEquals("slave_A", dto.getSlaveValue());
        assertEquals(new BigDecimal(config.getFixValue()), dto.getVelValue());
        assertEquals("SUM", dto.getAggregateMode());
        assertEquals("TTL", dto.getTimeMode());
        assertEquals("MINUTE", dto.getTimeUnit());
        assertEquals(Integer.valueOf(30), dto.getTimeWindow());
        assertNotNull(dto.getExpireTime());
    }

    // ==================== buildVelEventData(single) 上下文构建 ====================

    @Test
    @DisplayName("buildVelEventData(single) — ctx.txnTime 为 null 时自动填充当前时间")
    void testBuildVelEventDataSingle_NullTxnTime_FilledWithNow() {
        TxnParamContext ctx = buildCtx();
        ctx.setTxnTime(null);
        FeatureConfigModel config = buildConfig();
        Map<String, Object> metaValues = new HashMap<>();

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)           // condition
                .thenReturn("master_A")     // master
                .thenReturn("slave_A");     // slave

        List<VelEventData> result = velocityFilterService.buildVelEventData(ctx, config, metaValues);

        assertEquals(1, result.size());
        assertNotNull(result.get(0).getTxnTime());
    }

    @Test
    @DisplayName("buildVelEventData(single) — metaValues 被追加 txnId 和 pointCode")
    void testBuildVelEventDataSingle_AppendsTxnIdAndPointCode() {
        TxnParamContext ctx = buildCtx();
        FeatureConfigModel config = buildConfig();
        Map<String, Object> metaValues = new HashMap<>();
        metaValues.put("custom_key", "custom_value");

        when(scriptLanguageService.execute(any()))
                .thenReturn(true)           // condition
                .thenReturn("master_A")     // master
                .thenReturn("slave_A");     // slave

        velocityFilterService.buildVelEventData(ctx, config, metaValues);

        assertEquals(TXN_ID, metaValues.get("txnId"));
        assertEquals(POINT_CODE, metaValues.get("pointCode"));
        assertEquals("custom_value", metaValues.get("custom_key"));
    }

    // ==================== 辅助方法 ====================

    private TxnParamContext buildCtx() {
        TxnParamContext ctx = new TxnParamContext();
        ctx.setTxnId(TXN_ID);
        ctx.setPointCode(POINT_CODE);
        ctx.setTxnTime(new Date());
        ctx.setProjectId(1001L);
        return ctx;
    }

    private FeatureConfigModel buildConfig() {
        FeatureConfigModel config = new FeatureConfigModel();
        config.setFeatureCode(FEATURE_CODE);
        config.setConditionScript("txnId != null");
        config.setMainDimScript("merchantId");
        config.setSlaveDimScript("cardBin");
        config.setValueType("FIXED");
        config.setFixValue("10");
        config.setAggregateMode("SUM");
        config.setTimeMode("TTL");
        config.setTimeUnit("MINUTE");
        config.setTimeWindow(60);
        return config;
    }
}
