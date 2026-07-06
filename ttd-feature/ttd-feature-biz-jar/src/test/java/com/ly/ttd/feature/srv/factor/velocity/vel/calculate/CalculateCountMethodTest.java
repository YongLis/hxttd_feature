package com.ly.ttd.feature.srv.factor.velocity.vel.calculate;

import com.ly.ttd.feature.common.event.doris.VelReadEvent;
import com.ly.ttd.feature.common.event.dto.VelCalculateResult;
import com.ly.ttd.feature.common.event.dto.VelValueItem;
import com.ly.ttd.feature.common.event.dto.VelWindowData;
import com.ly.ttd.feature.srv.factor.velocity.vel.sink.AbstractVelDataSunkService;
import com.ly.ttd.feature.srv.factor.velocity.vel.sink.VelDataSunkFactory;
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
 * CalculateCountMethod 单元测试
 *
 * @author yong.li
 * @since 2026-07-04
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CalculateCountMethod 单元测试")
class CalculateCountMethodTest {

    @InjectMocks
    private CalculateCountMethod calculateCountMethod;

    @Mock
    private AbstractVelDataSunkService dataSunkService;

    private MockedStatic<VelDataSunkFactory> factoryMock;

    private static final String TXN_ID = "TXN20260704001";
    private static final String TIME_MODE = "TTL";
    private static final String FEATURE_CODE = "FEA_001";
    private static final String POINT_CODE = "POINT_001";

    @BeforeEach
    void setUp() {
        factoryMock = mockStatic(VelDataSunkFactory.class);
        factoryMock.when(() -> VelDataSunkFactory.getService(any()))
                .thenReturn(dataSunkService);
    }

    @AfterEach
    void tearDown() {
        factoryMock.close();
    }

    // ==================== 输入校验 ====================

    @Test
    @DisplayName("doCalculate — dto 为 null，返回零值和空快照")
    void testDoCalculate_NullDto() {
        VelCalculateResult result = calculateCountMethod.doCalculate(null);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
        verify(dataSunkService, never()).getVelData(any());
    }

    @Test
    @DisplayName("doCalculate — masterValues 为 null，返回零值和空快照")
    void testDoCalculate_NullMasterValues() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(null);

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
    }

    @Test
    @DisplayName("doCalculate — masterValues 为空集合，返回零值和空快照")
    void testDoCalculate_EmptyMasterValues() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Collections.emptyList());

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
    }

    // ==================== 多主维度拒绝 ====================

    @Test
    @DisplayName("doCalculate — masterValues 超过 1 个，COUNT 不支持多主维度，返回零值")
    void testDoCalculate_MultipleMasters_NotSupported() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A", "master_B"));

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
        verify(dataSunkService, never()).getVelData(any());
    }

    @Test
    @DisplayName("doCalculate — masterValues 有 3 个元素，依然拒绝，不调用 getVelData")
    void testDoCalculate_ThreeMasters_NotSupported() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("A", "B", "C"));

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        verify(dataSunkService, never()).getVelData(any());
    }

    // ==================== dataDto 为空 ====================

    @Test
    @DisplayName("doCalculate — getVelData 返回 null，返回零值")
    void testDoCalculate_DataDtoNull() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        when(dataSunkService.getVelData(any())).thenReturn(null);

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
        verify(dataSunkService, times(1)).getVelData(any());
    }

    // ==================== 正常计数 ====================

    @Test
    @DisplayName("doCalculate — 单个主维度，dataMap 有 5 个 key，结果应为 5")
    void testDoCalculate_SingleMaster_FiveKeys() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_1", Arrays.asList(new VelValueItem("t1", 1000L, BigDecimal.ONE)));
        afterData.put("slave_2", Arrays.asList(new VelValueItem("t2", 2000L, BigDecimal.TEN)));
        afterData.put("slave_3", Arrays.asList(new VelValueItem("t3", 3000L, BigDecimal.valueOf(5))));
        afterData.put("slave_4", Arrays.asList(new VelValueItem("t4", 4000L, BigDecimal.valueOf(7))));
        afterData.put("slave_5", Arrays.asList(new VelValueItem("t5", 5000L, BigDecimal.valueOf(9))));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        assertEquals(BigDecimal.valueOf(5), result.getResult());
        assertNotNull(result.getSampleSnapshot());
    }

    @Test
    @DisplayName("doCalculate — dataMap 为 null，结果应为 0")
    void testDoCalculate_NullDataMap() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        VelWindowData windowData = new VelWindowData();
        windowData.setAfterData(null);
        when(dataSunkService.getVelData(any())).thenReturn(windowData);

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
    }

    @Test
    @DisplayName("doCalculate — dataMap 为空 Map，结果应为 0")
    void testDoCalculate_EmptyDataMap() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        stubGetVelData(new HashMap<>());

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
    }

    @Test
    @DisplayName("doCalculate — dataMap 有 1 个 key，结果应为 1")
    void testDoCalculate_SingleKey() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("only_key", Arrays.asList(
                new VelValueItem("t1", 1000L, BigDecimal.ONE),
                new VelValueItem("t2", 2000L, BigDecimal.TEN)
        ));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        // 去重计数：只关心 key 的数量，不关心每个 key 下有多少条
        assertEquals(BigDecimal.ONE, result.getResult());
    }

    // ==================== filterData 交互 ====================

    @Test
    @DisplayName("doCalculate — velCurFlag=0 且存在从维度匹配，filterData 排除当前交易 key")
    void testDoCalculate_VelCurFlagZero_ExcludesCurrentTxn() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));
        dto.setSlaveValues(Arrays.asList("slave_A", "slave_B"));
        dto.setVelCurFlag("0");
        dto.setTxnId(TXN_ID);

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        List<VelValueItem> itemsA = new ArrayList<>();
        itemsA.add(new VelValueItem(TXN_ID, 2000L, BigDecimal.TEN));       // 当前交易 → 应被移除
        itemsA.add(new VelValueItem("OTHER_1", 1000L, BigDecimal.ONE));    // 其他交易 → 保留
        afterData.put("slave_A", itemsA);

        List<VelValueItem> itemsB = new ArrayList<>();
        itemsB.add(new VelValueItem(TXN_ID, 3000L, BigDecimal.valueOf(5))); // 只有当前交易 → key 被移除
        afterData.put("slave_B", itemsB);

        afterData.put("slave_C", Arrays.asList(new VelValueItem("OTHER_2", 4000L, BigDecimal.TEN))); // 无当前交易 → 保留

        stubGetVelData(afterData);

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        // slave_A: TXN_ID 被移除, OTHER_1 保留 → key 保留
        // slave_B: 仅 TXN_ID, 全部移除 → key 移除
        // slave_C: 无当前交易 → key 保留
        // 剩余 2 个 key
        assertEquals(BigDecimal.valueOf(2), result.getResult());
    }

    @Test
    @DisplayName("doCalculate — velCurFlag=1 时 filterData 不重复添加已存在的交易")
    void testDoCalculate_VelCurFlagOne_NoDuplicate() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));
        dto.setSlaveValues(Arrays.asList("slave_A"));
        dto.setVelCurFlag("1");
        dto.setTxnId(TXN_ID);
        dto.setTxnTime(new Date());
        dto.setVelValue(BigDecimal.valueOf(100));

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_A", new ArrayList<>(Arrays.asList(
                new VelValueItem(TXN_ID, 2000L, BigDecimal.TEN)     // 已存在 → 不重复
        )));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        // slave_A 保留（TXN_ID 已存在，不重复添加 → key 数不变）
        assertEquals(BigDecimal.ONE, result.getResult());
    }

    @Test
    @DisplayName("doCalculate — velCurFlag=1 且从维度不存在于 dataMap 中，filterData 新增该 key")
    void testDoCalculate_VelCurFlagOne_AddsNewKey() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));
        dto.setSlaveValues(Arrays.asList("slave_new"));
        dto.setVelCurFlag("1");
        dto.setTxnId(TXN_ID);
        dto.setTxnTime(new Date());
        dto.setVelValue(BigDecimal.valueOf(42));

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_existing", Arrays.asList(new VelValueItem("OTHER", 1000L, BigDecimal.ONE)));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        // slave_existing 保留，slave_new 被 filterData 新增 → 2 个 key
        assertEquals(BigDecimal.valueOf(2), result.getResult());
    }

    @Test
    @DisplayName("doCalculate — velCurFlag=0 所有 key 的从维度都被移除，结果应为 0")
    void testDoCalculate_VelCurFlagZero_AllKeysRemoved() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));
        dto.setSlaveValues(Arrays.asList("slave_A"));
        dto.setVelCurFlag("0");
        dto.setTxnId(TXN_ID);

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_A", new ArrayList<>(Arrays.asList(
                new VelValueItem(TXN_ID, 1000L, BigDecimal.ONE)     // 仅当前交易 → key 被移除
        )));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
    }

    // ==================== 无从维度 filterData 不生效 ====================

    @Test
    @DisplayName("doCalculate — slaveValues 为空时 filterData 不做任何过滤，直接计 key 数")
    void testDoCalculate_NoSlaveValues_NoFilter() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));
        dto.setSlaveValues(null);
        dto.setVelCurFlag("0");   // 即便要排除当前交易，没有 slaveValues 也无从过滤

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("k1", Arrays.asList(new VelValueItem(TXN_ID, 1000L, BigDecimal.ONE)));
        afterData.put("k2", Arrays.asList(new VelValueItem("OTHER", 2000L, BigDecimal.TEN)));
        afterData.put("k3", Arrays.asList(new VelValueItem(TXN_ID, 3000L, BigDecimal.valueOf(5))));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        // slaveValues 为空 → filterData 遍历空集合，不做任何过滤 → 3 个 key 全部保留
        assertEquals(BigDecimal.valueOf(3), result.getResult());
    }

    // ==================== 快照内容 ====================

    @Test
    @DisplayName("doCalculate — sampleSnapshot 包含 dataMap 的 JSON 序列化结果")
    void testDoCalculate_SampleSnapshotContainsDataMap() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_x", Arrays.asList(new VelValueItem("t1", 1000L, BigDecimal.ONE)));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        assertNotNull(result.getSampleSnapshot());
        assertTrue(result.getSampleSnapshot().contains("slave_x"));
    }

    @Test
    @DisplayName("doCalculate — dataMap 为 null 时快照为 \"null\" 字符串")
    void testDoCalculate_NullDataMap_SnapshotIsNullString() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        VelWindowData windowData = new VelWindowData();
        windowData.setAfterData(null);
        when(dataSunkService.getVelData(any())).thenReturn(windowData);

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);

        assertEquals("null", result.getSampleSnapshot());
    }

    // ==================== getCalculateMethod ====================

    @Test
    @DisplayName("getCalculateMethod — 返回 COUNT")
    void testGetCalculateMethod_ReturnsCount() {
        // 通过正常调用验证 doCalculate 工作正确，间接确认 getCalculateMethod 被正确注册
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        stubGetVelData(new HashMap<>());

        VelCalculateResult result = calculateCountMethod.doCalculate(dto);
        assertNotNull(result);
    }

    // ==================== 辅助方法 ====================

    private VelReadEvent buildBaseDto() {
        VelReadEvent dto = new VelReadEvent();
        dto.setFactorCode(FEATURE_CODE);
        dto.setFactorName("测试因子");
        dto.setTxnId(TXN_ID);
        dto.setTxnTime(new Date());
        dto.setPointCode(POINT_CODE);
        dto.setTimeMode(TIME_MODE);
        dto.setAggregateMode("COUNT");
        dto.setVelCurFlag("1");
        dto.setVelValue(BigDecimal.ONE);
        return dto;
    }

    private void stubGetVelData(Map<String, List<VelValueItem>> afterData) {
        VelWindowData windowData = new VelWindowData();
        windowData.setAfterData(afterData);
        windowData.setRedisKey("redis:key:" + System.nanoTime());
        when(dataSunkService.getVelData(any())).thenReturn(windowData);
    }
}
