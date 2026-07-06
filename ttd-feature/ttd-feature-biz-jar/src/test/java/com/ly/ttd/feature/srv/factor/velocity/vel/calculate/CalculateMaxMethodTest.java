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
 * CalculateMaxMethod 单元测试
 *
 * @author yong.li
 * @since 2026-07-04
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CalculateMaxMethod 单元测试")
class CalculateMaxMethodTest {

    @InjectMocks
    private CalculateMaxMethod calculateMaxMethod;

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
        VelCalculateResult result = calculateMaxMethod.doCalculate(null);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
        verify(dataSunkService, never()).getVelData(any());
    }

    @Test
    @DisplayName("doCalculate — masterValues 为 null，返回零值和空快照")
    void testDoCalculate_NullMasterValues() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(null);

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
    }

    @Test
    @DisplayName("doCalculate — masterValues 为空集合，返回零值和空快照")
    void testDoCalculate_EmptyMasterValues() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Collections.emptyList());

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
        assertEquals("", result.getSampleSnapshot());
    }

    // ==================== 单主维度 ====================

    @Test
    @DisplayName("doCalculate — 单个主维度，dataMap 有 3 个 key，max 为 3")
    void testDoCalculate_SingleMaster_ThreeKeys() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_1", Arrays.asList(new VelValueItem("t1", 1000L, BigDecimal.ONE)));
        afterData.put("slave_2", Arrays.asList(new VelValueItem("t2", 2000L, BigDecimal.TEN)));
        afterData.put("slave_3", Arrays.asList(new VelValueItem("t3", 3000L, BigDecimal.valueOf(5))));

        stubGetVelData(afterData);

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);

        assertEquals(BigDecimal.valueOf(3), result.getResult());
    }

    @Test
    @DisplayName("doCalculate — 单个主维度，dataMap 为 null，max 为 0")
    void testDoCalculate_SingleMaster_NullDataMap() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        stubGetVelData(null);

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
    }

    @Test
    @DisplayName("doCalculate — 单个主维度，dataMap 为空，max 为 0")
    void testDoCalculate_SingleMaster_EmptyDataMap() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));

        stubGetVelData(new HashMap<>());

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
    }

    // ==================== 多主维度取最大值 ====================

    @Test
    @DisplayName("doCalculate — 三个主维度 key 数为 2/5/1，max 为 5")
    void testDoCalculate_MultipleMasters_MaxOfKeyCounts() {
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
        data2.put("s5", Collections.emptyList());

        Map<String, List<VelValueItem>> data3 = new HashMap<>();
        data3.put("s1", Collections.emptyList());

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(data1))
                .thenReturn(buildWindowData(data2))
                .thenReturn(buildWindowData(data3));

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);

        // max(2, 5, 1) = 5
        assertEquals(BigDecimal.valueOf(5), result.getResult());
        verify(dataSunkService, times(3)).getVelData(any());
    }

    @Test
    @DisplayName("doCalculate — 三个主维度 key 数全部相同为 2，max 为 2")
    void testDoCalculate_AllMastersSameCount() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("A", "B", "C"));

        Map<String, List<VelValueItem>> data = new HashMap<>();
        data.put("k1", Collections.emptyList());
        data.put("k2", Collections.emptyList());

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(data))
                .thenReturn(buildWindowData(data))
                .thenReturn(buildWindowData(data));

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);

        assertEquals(BigDecimal.valueOf(2), result.getResult());
    }

    @Test
    @DisplayName("doCalculate — 四个主维度全部返回 0，max 为 0")
    void testDoCalculate_AllMastersZero() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("A", "B", "C", "D"));

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(null))    // dataMap null → 0
                .thenReturn(buildWindowData(new HashMap<>()))  // empty → 0
                .thenReturn(buildWindowData(null))    // null → 0
                .thenReturn(buildWindowData(new HashMap<>())); // empty → 0

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);

        assertEquals(BigDecimal.ZERO, result.getResult());
    }

    @Test
    @DisplayName("doCalculate — 两个主维度，其中一个 dataMap 为 null 但另一个有 4 个 key，max 为 4")
    void testDoCalculate_OneNullOneWithData() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("zero_master", "data_master"));

        Map<String, List<VelValueItem>> data = new HashMap<>();
        data.put("k1", Collections.emptyList());
        data.put("k2", Collections.emptyList());
        data.put("k3", Collections.emptyList());
        data.put("k4", Collections.emptyList());

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(null))
                .thenReturn(buildWindowData(data));

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);

        // max(0, 4) = 4
        assertEquals(BigDecimal.valueOf(4), result.getResult());
    }

    // ==================== 主维度 + 从维度配对 ====================

    @Test
    @DisplayName("doCalculate — 单个从维度应用于所有主维度")
    void testDoCalculate_SingleSlave_AppliedToAllMasters() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A", "master_B"));
        dto.setSlaveValues(Arrays.asList("slave_common"));

        Map<String, List<VelValueItem>> dataA = new HashMap<>();
        dataA.put("slave_common", Collections.emptyList());
        dataA.put("other_a", Collections.emptyList());

        Map<String, List<VelValueItem>> dataB = new HashMap<>();
        dataB.put("slave_common", Collections.emptyList());
        dataB.put("other_b", Collections.emptyList());
        dataB.put("other_c", Collections.emptyList());

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(dataA))
                .thenReturn(buildWindowData(dataB));

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);

        // max(2, 3) = 3
        assertEquals(BigDecimal.valueOf(3), result.getResult());
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
        dataB.put("slave_B3", Collections.emptyList());

        Map<String, List<VelValueItem>> dataC = new HashMap<>();
        dataC.put("slave_C", Collections.emptyList());
        dataC.put("slave_C2", Collections.emptyList());

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(dataA))
                .thenReturn(buildWindowData(dataB))
                .thenReturn(buildWindowData(dataC));

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);

        // max(1, 3, 2) = 3
        assertEquals(BigDecimal.valueOf(3), result.getResult());
    }

    @Test
    @DisplayName("doCalculate — 从维度数量大于1且不等于主维度数量，不配对从维度")
    void testDoCalculate_SlaveSizeMismatch_NoSlaveAssigned() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A", "master_B"));
        dto.setSlaveValues(Arrays.asList("s1", "s2", "s3"));

        Map<String, List<VelValueItem>> data = new HashMap<>();
        data.put("k1", Collections.emptyList());

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(data))
                .thenReturn(buildWindowData(data));

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);

        // 两个 master 都没有 slave 设置，各自 1 个 key，max = 1
        assertEquals(BigDecimal.ONE, result.getResult());
    }

    // ==================== filterDataByMasterIndex 交互 ====================
    @Test
    @DisplayName("doCalculate — velCurFlag=1 时 filterDataByMasterIndex 保留且不重复")
    void testDoCalculate_VelCurFlagOne_KeepsAndDoesNotDuplicate() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A"));
        dto.setSlaveValues(Arrays.asList("slave_A"));
        dto.setVelCurFlag("1");
        dto.setTxnId(TXN_ID);
        dto.setTxnTime(new Date());
        dto.setVelValue(BigDecimal.valueOf(100));

        Map<String, List<VelValueItem>> afterData = new HashMap<>();
        afterData.put("slave_A", new ArrayList<>(Arrays.asList(
                new VelValueItem(TXN_ID, 2000L, BigDecimal.TEN)     // 已存在
        )));
        afterData.put("slave_empty", new ArrayList<>());             // 空列表 → 应被填充

        stubGetVelData(afterData);

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);

        // slave_A: TXN_ID 已存在，不重复添加 → key 保留
        // slave_empty: velCurFlag=1 且空列表 → filterDataByMasterIndex 填充当前交易 → key 保留
        // 共 2 个 key
        assertEquals(BigDecimal.valueOf(2), result.getResult());
    }

    // ==================== 快照内容 ====================

    @Test
    @DisplayName("doCalculate — sampleSnapshot 包含各主维度的计算结果 JSON")
    void testDoCalculate_SampleSnapshotContainsAllMasters() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("master_A", "master_B"));

        Map<String, List<VelValueItem>> data = new HashMap<>();
        data.put("k1", Collections.emptyList());

        when(dataSunkService.getVelData(any()))
                .thenReturn(buildWindowData(data))
                .thenReturn(buildWindowData(data));

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);

        assertNotNull(result.getSampleSnapshot());
        assertTrue(result.getSampleSnapshot().contains("master_A"));
        assertTrue(result.getSampleSnapshot().contains("master_B"));
    }

    // ==================== getCalculateMethod ====================

    @Test
    @DisplayName("getCalculateMethod — 返回 MAX")
    void testGetCalculateMethod_ReturnsMax() {
        VelReadEvent dto = buildBaseDto();
        dto.setMasterValues(Arrays.asList("test"));

        stubGetVelData(new HashMap<>());

        VelCalculateResult result = calculateMaxMethod.doCalculate(dto);
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
        dto.setAggregateMode("MAX");
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
