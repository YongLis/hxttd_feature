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
 * CalculateSumMethod 单元测试
 *
 * @author yong.li
 * @since 2026-07-04
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CalculateSumMethod 单元测试")
class CalculateSumMethodTest {

    @InjectMocks
    private CalculateSumMethod calculateSumMethod;

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
        VelCalculateResult result = calculateSumMethod.doCalculate(null);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
        verify(dataSunkService, never()).getVelData(any());
    }

    @Test
    @DisplayName("doCalculate — masterValues 为 null，返回零值和空快照")
    void testDoCalculate_NullMasterValues() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(null);

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
    }

    @Test
    @DisplayName("doCalculate — masterValues 为空集合，返回零值和空快照")
    void testDoCalculate_EmptyMasterValues() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Collections.emptyList());

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
    }

    @Test
    @DisplayName("doCalculate — slaveValues 为 null，返回零值")
    void testDoCalculate_NullSlaveValues() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));
        dto.setSlaveValues(null);

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
    }

    @Test
    @DisplayName("doCalculate — slaveValues 为空集合，返回零值")
    void testDoCalculate_EmptySlaveValues() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));
        dto.setSlaveValues(Collections.emptyList());

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
    }

    // ==================== 多主维度拒绝 ====================

    @Test
    @DisplayName("doCalculate — masterValues 超过 1 个，SUM 不支持多主维度，返回零值")
    void testDoCalculate_MultipleMasters_NotSupported() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A", "master_B"));

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
        verify(dataSunkService, never()).getVelData(any());
    }

    @Test
    @DisplayName("doCalculate — masterValues 有 3 个元素，依然拒绝，不调用 getVelData")
    void testDoCalculate_ThreeMasters_NotSupported() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("A", "B", "C"));

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

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

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
        verify(dataSunkService, times(1)).getVelData(any());
    }

    // ==================== 正常求和 ====================

    @Test
    @DisplayName("doCalculate — 3 个 key 值分别为 1/10/5， velCurFlag=1 时，求和 = 16")
    void testDoCalculate_ThreeKeys_SumOfValues() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_1", Arrays.asList(new VelValueItem("t1", 1000L, BigDecimal.ONE)));
        afterData.put("slave_2", Arrays.asList(new VelValueItem("t2", 2000L, BigDecimal.TEN)));
        afterData.put("slave_3", Arrays.asList(new VelValueItem("t3", 3000L, BigDecimal.valueOf(5))));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        // 1 + 10 + 5 = 16
        assertEquals(BigDecimal.valueOf(16), result.getResult());
        assertNotNull(result.getSampleSnapshot());
    }

    @Test
    @DisplayName("doCalculate — 1 个 key 下有 2 条交易 [3, 7], velCurFlag=0 时，求和 = 10")
    void testDoCalculate_SingleKey_MultipleItems() {
        VelReadEvent dto = buildBaseDto();
        dto.setVelCurFlag("0");
        dto.setMasterValues(Arrays.asList("master_A"));

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_A", Arrays.asList(
                new VelValueItem("t1", 1000L, BigDecimal.valueOf(3)),
                new VelValueItem("t2", 2000L, BigDecimal.valueOf(7))
        ));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        // 3 + 7 = 10
        assertEquals(BigDecimal.valueOf(10), result.getResult());
    }

    @Test
    @DisplayName("doCalculate — 1 个 key 下 1 条交易,velCurFlag=1 时，求和 = 该值")
    void testDoCalculate_SingleKey_SingleItem() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_A", Arrays.asList(
                new VelValueItem("t1", 1000L, BigDecimal.valueOf(42))
        ));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        assertEquals(BigDecimal.valueOf(42), result.getResult());
    }

    @Test
    @DisplayName("doCalculate — dataMap 为 null，求和 = 0")
    void testDoCalculate_NullDataMap() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        VelWindowData windowData = new VelWindowData();
        windowData.setAfterData(null);
        when(dataSunkService.getVelData(any())).thenReturn(windowData);

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
    }

    @Test
    @DisplayName("doCalculate — dataMap 为空 Map，velCurFlag=0 时，求和 = 0")
    void testDoCalculate_EmptyDataMap() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        stubGetVelData(new HashMap<>());

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
    }

    // ==================== filterData 交互 ====================

    @Test
    @DisplayName("doCalculate — velCurFlag=0 时 filterData 排除当前交易，求和排除对应值")
    void testDoCalculate_VelCurFlagZero_ExcludesCurrentTxn() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));
        dto.setSlaveValues(Arrays.asList("slave_A", "slave_B"));
        dto.setVelCurFlag("0");
        dto.setTxnId(TXN_ID);

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        List<VelValueItem> itemsA = new ArrayList<>();
        itemsA.add(new VelValueItem(TXN_ID, 2000L, BigDecimal.TEN));       // 当前交易 → 应被移除
        itemsA.add(new VelValueItem("OTHER_1", 1000L, BigDecimal.ONE));    // 保留
        afterData.put("slave_A", itemsA);

        List<VelValueItem> itemsB = new ArrayList<>();
        itemsB.add(new VelValueItem(TXN_ID, 3000L, BigDecimal.valueOf(5))); // 当前交易 → key 被移除
        afterData.put("slave_B", itemsB);

        afterData.put("slave_C", Arrays.asList(new VelValueItem("OTHER_2", 4000L, BigDecimal.valueOf(7)))); // 保留

        stubGetVelData(afterData);

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        // slave_A: 移除 TXN_ID(10), 保留 OTHER_1(1) → 1
        // slave_B: 仅 TXN_ID(5) → key 被移除 → 0
        // slave_C: OTHER_2(7) → 7
        // sum = 1 + 7 = 8
        assertEquals(BigDecimal.valueOf(8), result.getResult());
    }

    @Test
    @DisplayName("doCalculate — velCurFlag=0 所有 key 的从维度都被移除，求和 = 0")
    void testDoCalculate_VelCurFlagZero_AllKeysRemoved() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));
        dto.setSlaveValues(Arrays.asList("slave_A"));
        dto.setVelCurFlag("0");
        dto.setTxnId(TXN_ID);

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_A", new ArrayList<>(Arrays.asList(
                new VelValueItem(TXN_ID, 1000L, BigDecimal.valueOf(99))
        )));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
    }

    @Test
    @DisplayName("doCalculate — velCurFlag=1 且 TXN_ID 已存在，不重复求和")
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

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        // TXN_ID 已存在，不重复添加 → sum = 10 (不变)
        assertEquals(BigDecimal.TEN, result.getResult());
    }

    @Test
    @DisplayName("doCalculate — velCurFlag=1 从维度存在但列表中无当前交易，新增当前交易值")
    void testDoCalculate_VelCurFlagOne_SlaveExistsWithoutCurrentTxn_AddsTxn() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));
        dto.setSlaveValues(Arrays.asList("slave_A"));
        dto.setVelCurFlag("1");
        dto.setTxnId(TXN_ID);
        dto.setTxnTime(new Date());
        dto.setVelValue(BigDecimal.valueOf(30));

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_A", new ArrayList<>(Arrays.asList(
                new VelValueItem("OTHER_1", 1000L, BigDecimal.valueOf(5)),
                new VelValueItem("OTHER_2", 2000L, BigDecimal.valueOf(15))
        )));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        // 原有 5 + 15 = 20，加上新增的 30 → sum = 50
        assertEquals(BigDecimal.valueOf(50), result.getResult());
    }

    @Test
    @DisplayName("doCalculate — velCurFlag=1 从维度不在 dataMap 中，filterData 新建 key 并计入")
    void testDoCalculate_VelCurFlagOne_SlaveNotInDataMap_CreatesNewKey() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));
        dto.setSlaveValues(Arrays.asList("slave_new"));
        dto.setVelCurFlag("1");
        dto.setTxnId(TXN_ID);
        dto.setTxnTime(new Date());
        dto.setVelValue(BigDecimal.valueOf(25));

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_existing", Arrays.asList(
                new VelValueItem("OTHER", 1000L, BigDecimal.valueOf(8))
        ));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        // slave_existing: 8 + slave_new 新建的 25 → sum = 33
        assertEquals(BigDecimal.valueOf(33), result.getResult());
    }

    // ==================== 无从维度 filterData 不生效 ====================

    @Test
    @DisplayName("doCalculate — slaveValues 为空时 filterData 不做任何过滤，直接求和")
    void testDoCalculate_NoSlaveValues_NoFilter() {
        // Sum 在 slaveValues 为空时直接返回零值（第 64-66 行 guard），不需要进入 filterData
        // 此场景已在 testDoCalculate_NullSlaveValues / testDoCalculate_EmptySlaveValues 中覆盖
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));
        dto.setSlaveValues(Collections.emptyList());

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
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

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

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

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);

        assertEquals("null", result.getSampleSnapshot());
    }

    // ==================== getCalculateMethod ====================

    @Test
    @DisplayName("getCalculateMethod — 返回 SUM")
    void testGetCalculateMethod_ReturnsSum() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        stubGetVelData(new HashMap<>());

        VelCalculateResult result = calculateSumMethod.doCalculate(dto);
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
        dto.setAggregateMode("SUM");
        dto.setVelCurFlag("0");
        dto.setVelValue(BigDecimal.ONE);
        dto.setSlaveValues(Arrays.asList("slave_default"));
        return dto;
    }

    private void stubGetVelData(Map<String, List<VelValueItem>> afterData) {
        VelWindowData windowData = new VelWindowData();
        windowData.setAfterData(afterData);
        windowData.setRedisKey("redis:key:" + System.nanoTime());
        when(dataSunkService.getVelData(any())).thenReturn(windowData);
    }
}
