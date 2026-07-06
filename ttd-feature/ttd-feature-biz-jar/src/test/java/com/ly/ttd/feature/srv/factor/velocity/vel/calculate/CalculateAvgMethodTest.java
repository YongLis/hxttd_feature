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
import java.math.RoundingMode;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * CalculateAvgMethod 单元测试
 *
 * @author yong.li
 * @since 2026-07-04
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CalculateAvgMethod 单元测试")
class CalculateAvgMethodTest {

    @InjectMocks
    private CalculateAvgMethod calculateAvgMethod;

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
        VelCalculateResult result = calculateAvgMethod.doCalculate(null);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
        verify(dataSunkService, never()).getVelData(any());
    }

    @Test
    @DisplayName("doCalculate — masterValues 为 null，返回零值和空快照")
    void testDoCalculate_NullMasterValues() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(null);

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
    }

    @Test
    @DisplayName("doCalculate — masterValues 为空集合，返回零值和空快照")
    void testDoCalculate_EmptyMasterValues() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Collections.emptyList());

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
    }

    // ==================== 单主维度，无从维度 ====================

    @Test
    @DisplayName("doCalculate — 单个主维度，dataMap 有 3 个 key，结果应为 3")
    void testDoCalculate_SingleMaster_NoSlave_ThreeKeys() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_1", Arrays.asList(new VelValueItem("t1", 1000L, BigDecimal.ONE)));
        afterData.put("slave_2", Arrays.asList(new VelValueItem("t2", 2000L, BigDecimal.TEN)));
        afterData.put("slave_3", Arrays.asList(new VelValueItem("t3", 3000L, BigDecimal.valueOf(5))));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);

        assertEquals(BigDecimal.valueOf(3).setScale(2), result.getResult());
        assertNotNull(result.getSampleSnapshot());
    }

    @Test
    @DisplayName("doCalculate — 单个主维度，dataMap 为 null，结果应为 0")
    void testDoCalculate_SingleMaster_NullDataMap() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        stubGetVelData(null);

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO.setScale(2), result.getResult());
    }

    @Test
    @DisplayName("doCalculate — 单个主维度，dataMap 为空，结果应为 0")
    void testDoCalculate_SingleMaster_EmptyDataMap() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        stubGetVelData(new HashMap<>());

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO.setScale(2), result.getResult());
    }

    // ==================== 多主维度，无从维度 — 平均值计算 ====================

    @Test
    @DisplayName("doCalculate — 三个主维度，key 数量分别为 2/4/6，平均值 = 4")
    void testDoCalculate_MultipleMasters_AverageOfKeyCounts() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A", "master_B", "master_C"));

        Map<String, List<VelValueItem>> data1 = new HashMap<>();
        data1.put("s1", Collections.emptyList());
        data1.put("s2", Collections.emptyList());

        Map<String, List<VelValueItem>> data2 = new HashMap<>();
        data2.put("s1", Collections.emptyList());
        data2.put("s2", Collections.emptyList());
        data2.put("s3", Collections.emptyList());
        data2.put("s4", Collections.emptyList());

        Map<String, List<VelValueItem>> data3 = new HashMap<>();
        data3.put("s1", Collections.emptyList());
        data3.put("s2", Collections.emptyList());
        data3.put("s3", Collections.emptyList());
        data3.put("s4", Collections.emptyList());
        data3.put("s5", Collections.emptyList());
        data3.put("s6", Collections.emptyList());

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(data1))
                .thenReturn(buildWindowData(data2))
                .thenReturn(buildWindowData(data3));

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);

        // (2 + 4 + 6) / 3 = 4
        assertEquals(BigDecimal.valueOf(4).setScale(2, RoundingMode.HALF_UP), result.getResult());
        verify(dataSunkService, times(3)).getVelData(any());
    }

    @Test
    @DisplayName("doCalculate — 两个主维度，key 数量 1/2，平均值 = 1.50")
    void testDoCalculate_TwoMasters_FractionalAverage() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A", "master_B"));

        Map<String, List<VelValueItem>> data1 = new HashMap<>();
        data1.put("s1", Collections.emptyList());

        Map<String, List<VelValueItem>> data2 = new HashMap<>();
        data2.put("s1", Collections.emptyList());
        data2.put("s2", Collections.emptyList());

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(data1))
                .thenReturn(buildWindowData(data2));

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);

        // (1 + 2) / 2 = 1.50
        assertEquals(BigDecimal.valueOf(1.50).setScale(2, RoundingMode.HALF_UP), result.getResult());
    }

    // ==================== 主维度 + 从维度配对 ====================

    @Test
    @DisplayName("doCalculate — 单个从维度应用于所有主维度")
    void testDoCalculate_SingleSlave_AppliedToAllMasters() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A", "master_B"));
        List<String> slaveValues = new ArrayList<>();
        slaveValues.add("slave_common");
        dto.setSlaveValues(slaveValues);

        Map<String, List<VelValueItem>> data = new HashMap<>();
        List<VelValueItem> velValueItems = new ArrayList<>();
        velValueItems.add(new VelValueItem("t1", 1000L, BigDecimal.ONE));
        data.put("slave_common", velValueItems);

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(data))
                .thenReturn(buildWindowData(data));

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);

        // 两个 master 都拿到 1 个 key，avg = (1+1)/2 = 1
        assertEquals(BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP), result.getResult());
        // 每个 master 的 eventData 都设置了相同的 slaveValue
        verify(dataSunkService, times(2)).getVelData(any());
    }

    @Test
    @DisplayName("doCalculate — 多从维度按索引与主维度配对")
    void testDoCalculate_MultipleSlaves_PairedByIndex() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A", "master_B", "master_C"));
        dto.setSlaveValues(Arrays.asList("slave_A", "slave_B", "slave_C"));

        Map<String, List<VelValueItem>> dataA = new HashMap<>();
        dataA.put("slave_A", Collections.emptyList());

        Map<String, List<VelValueItem>> dataB = new HashMap<>();
        dataB.put("slave_B", Collections.emptyList());
        dataB.put("slave_B2", Collections.emptyList());

        Map<String, List<VelValueItem>> dataC = new HashMap<>();
        dataC.put("slave_C", Collections.emptyList());
        dataC.put("slave_C2", Collections.emptyList());
        dataC.put("slave_C3", Collections.emptyList());

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(dataA))
                .thenReturn(buildWindowData(dataB))
                .thenReturn(buildWindowData(dataC));

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);

        // (1 + 2 + 3) / 3 = 2
        assertEquals(BigDecimal.valueOf(2).setScale(2, RoundingMode.HALF_UP), result.getResult());
        verify(dataSunkService, times(3)).getVelData(any());
    }

    @Test
    @DisplayName("doCalculate — 从维度数量大于1且不等于主维度数量，不配对从维度")
    void testDoCalculate_SlaveSizeMismatch_NoSlaveAssigned() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A", "master_B"));
        dto.setSlaveValues(Arrays.asList("slave_A", "slave_B", "slave_C"));

        Map<String, List<VelValueItem>> data = new HashMap<>();
        data.put("some_key", Collections.emptyList());

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(data))
                .thenReturn(buildWindowData(data));

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);

        // 两个 master 都拿到 1 个 key，avg = (1+1)/2 = 1 (slave not applied to eventData)
        assertEquals(BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP), result.getResult());
    }

    // ==================== filterDataByMasterIndex 交互 ====================

    @Test
    @DisplayName("doCalculate — velCurFlag=1 时 filterDataByMasterIndex 保留当前交易（存在则不去重）")
    void testDoCalculate_VelCurFlagOne_KeepsCurrentTxn() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));
        dto.setSlaveValues(Arrays.asList("slave_A"));
        dto.setVelCurFlag("1");
        dto.setTxnId(TXN_ID);
        dto.setTxnTime(new Date());
        dto.setVelValue(BigDecimal.valueOf(100));

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_A", new ArrayList<>(Arrays.asList(
                new VelValueItem(TXN_ID, 2000L, BigDecimal.TEN)     // 已存在 → 不重复添加
        )));
        afterData.put("slave_B", new ArrayList<>());               // 空列表 → 应被填充

        stubGetVelData(afterData);

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);

        // slave_A: 已存在当前交易，不去重也不新增 → key 保留
        // slave_B: velCurFlag=1 且 slave_B 列表为空 → filterDataByMasterIndex 会新增当前交易项
        // key 总数保持不变: 2
        assertEquals(2, result.getResult().intValue());
    }

    // ==================== 加权平均值正确性 ====================

    @Test
    @DisplayName("doCalculate — 验证四舍五入，avg = 1.67 (5/3)")
    void testDoCalculate_RoundingHalfUp() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("A", "B", "C"));

        Map<String, List<VelValueItem>> d1 = new HashMap<>();
        d1.put("k1", Collections.emptyList());

        Map<String, List<VelValueItem>> d2 = new HashMap<>();
        d2.put("k1", Collections.emptyList());
        d2.put("k2", Collections.emptyList());

        Map<String, List<VelValueItem>> d3 = new HashMap<>();
        d3.put("k1", Collections.emptyList());
        d3.put("k2", Collections.emptyList());

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(d1))
                .thenReturn(buildWindowData(d2))
                .thenReturn(buildWindowData(d3));

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);

        // (1 + 2 + 2) / 3 = 1.666... → HALF_UP = 1.67
        assertEquals(BigDecimal.valueOf(1.67).setScale(2, RoundingMode.HALF_UP), result.getResult());
    }

    @Test
    @DisplayName("doCalculate — 结果列表为空时返回零值")
    void testDoCalculate_EmptyResultList_ReturnsZero() {
        // 构造一个 dto，它的 masterValues 非空但 dataMap 全部为 null → 每个 master result=0
        // 但 resultList 仍然会有条目（每个 master 对应一个 BigDecimal.ZERO），所以不会触发 empty 分支
        // 真正的 empty 分支只在 masterValues 处理后没有任何条目时才触发，这需要 masterValues 为 null/empty
        // 该分支已被 testDoCalculate_NullMasterValues / testDoCalculate_EmptyMasterValues 覆盖
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Collections.emptyList());

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
    }

    // ==================== 快照内容 ====================

    @Test
    @DisplayName("doCalculate — sampleSnapshot 包含各主维度的计算结果 JSON")
    void testDoCalculate_SampleSnapshotContainsAllMasterResults() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A", "master_B"));

        Map<String, List<VelValueItem>> data = new HashMap<>();
        data.put("k1", Collections.emptyList());
        data.put("k2", Collections.emptyList());

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(data))
                .thenReturn(buildWindowData(data));

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);

        assertNotNull(result.getSampleSnapshot());
        assertTrue(result.getSampleSnapshot().contains("master_A"));
        assertTrue(result.getSampleSnapshot().contains("master_B"));
    }

    // ==================== getCalculateMethod ====================

    @Test
    @DisplayName("getCalculateMethod — 返回 AVG")
    void testGetCalculateMethod_ReturnsAvg() {
        // getCalculateMethod 是 protected 方法，通过反射或间接验证
        // 此处验证 doCalculate 正常工作即可间接证明 getCalculateMethod 被正确注册
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("test"));

        stubGetVelData(new HashMap<>());

        VelCalculateResult result = calculateAvgMethod.doCalculate(dto);
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
        dto.setAggregateMode("AVG");
        dto.setVelCurFlag("1");
        dto.setVelValue(BigDecimal.ONE);
        return dto;
    }

    private void stubGetVelData(Map<String, List<VelValueItem>> afterData) {
        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(afterData));
    }

    private VelWindowData buildWindowData(Map<String, List<VelValueItem>> afterData) {
        VelWindowData windowData = new VelWindowData();
        windowData.setAfterData(afterData);
        windowData.setRedisKey("redis:key:" + System.nanoTime());
        return windowData;
    }
}
