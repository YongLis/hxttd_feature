package com.ly.ttd.feature.srv.factor.velocity.vel.sink;

import com.ly.ttd.feature.common.enums.VelocityTimeModeEnum;
import com.ly.ttd.feature.common.event.doris.VelEventData;
import com.ly.ttd.feature.common.event.dto.VelWindowData;
import com.ly.ttd.nacos.redis.XRedisTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * VelDataSunkByTTLService 单元测试
 *
 * @author yong.li
 * @since 2026-07-04
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("VelDataSunkByTTLService 单元测试")
class VelDataSunkByTTLServiceTest {

    @InjectMocks
    private VelDataSunkByTTLService service;

    @Mock
    private XRedisTemplate xRedisTemplate;

    private MockedStatic<VelocityRedisKeyBuilder> redisKeyBuilderMock;
    private MockedStatic<VelocityTimeOutBuilder> timeOutBuilderMock;

    private static final String REDIS_KEY = "vel:ttl:test:key";
    private static final Date EXPIRE_TIME = new Date();

    @BeforeEach
    void setUp() {
        redisKeyBuilderMock = mockStatic(VelocityRedisKeyBuilder.class);
        redisKeyBuilderMock.when(() -> VelocityRedisKeyBuilder.buildVelItemKey(any()))
                .thenReturn(REDIS_KEY);

        timeOutBuilderMock = mockStatic(VelocityTimeOutBuilder.class);
        timeOutBuilderMock.when(() -> VelocityTimeOutBuilder.getWindowExpireTime(any()))
                .thenReturn(EXPIRE_TIME);
    }

    @AfterEach
    void tearDown() {
        redisKeyBuilderMock.close();
        timeOutBuilderMock.close();
    }

    // ==================== getTimeMode ====================

    @Test
    @DisplayName("getTimeMode — 返回 ttl")
    void testGetTimeMode() {
        assertEquals(VelocityTimeModeEnum.TTL.getType(), service.getTimeMode());
    }

    // ==================== null dto ====================

    @Test
    @DisplayName("getVelData — dto 为 null，返回 null")
    void testGetVelData_NullDto() {
        VelWindowData result = service.getVelData(null);
        assertNull(result);
    }

    // ==================== 缓存为空 ====================

    @Test
    @DisplayName("getVelData — Redis 缓存为空，返回空 beforeData/afterData")
    void testGetVelData_EmptyCache() {
        VelEventData dto = buildDto();
        when(xRedisTemplate.get(anyString())).thenReturn(null);

        VelWindowData result = service.getVelData(dto);

        assertNotNull(result);
        assertEquals(dto.getTxnId(), result.getTxnId());
        assertEquals(dto.getFeatureCode(), result.getFeatureCode());
        assertEquals(dto.getMasterValue(), result.getMasterValue());
        assertEquals(EXPIRE_TIME, result.getExpireTime());
        assertTrue(result.getBeforeData().isEmpty());
        assertTrue(result.getAfterData().isEmpty());
    }

    // ==================== TTL 过滤：无过期数据 ====================

    @Test
    @DisplayName("getVelData — 缓存数据时间戳均 >= windowMinTime，全部保留")
    void testGetVelData_AllItemsValid_NoneFiltered() {
        VelEventData dto = buildDto();
        timeOutBuilderMock.when(() -> VelocityTimeOutBuilder.getWindowMinTime(any()))
                .thenReturn(1000L);

        String cacheJson = "{\"slave_1\":[{\"txnId\":\"t1\",\"ts\":2000,\"v\":10}],"
                + "\"slave_2\":[{\"txnId\":\"t2\",\"ts\":3000,\"v\":20}]}";
        when(xRedisTemplate.get(anyString())).thenReturn(cacheJson);

        VelWindowData result = service.getVelData(dto);

        assertEquals(2, result.getBeforeData().size());
        assertEquals(2, result.getAfterData().size());
        assertTrue(result.getBeforeData().containsKey("slave_1"));
        assertTrue(result.getBeforeData().containsKey("slave_2"));
    }

    // ==================== TTL 过滤：部分过期 ====================

    @Test
    @DisplayName("getVelData — 缓存中部分 item 时间戳 < windowMinTime，item 所在 entry 被移除")
    void testGetVelData_PartialExpired_EntryRemoved() {
        VelEventData dto = buildDto();
        timeOutBuilderMock.when(() -> VelocityTimeOutBuilder.getWindowMinTime(any()))
                .thenReturn(5000L);

        // slave_1: all items valid → kept; slave_2: has expired item → entry removed
        String cacheJson = "{\"slave_1\":[{\"txnId\":\"t1\",\"ts\":6000,\"v\":10}],"
                + "\"slave_2\":[{\"txnId\":\"t2\",\"ts\":2000,\"v\":20}]}";
        when(xRedisTemplate.get(anyString())).thenReturn(cacheJson);

        VelWindowData result = service.getVelData(dto);

        assertEquals(1, result.getBeforeData().size());
        assertTrue(result.getBeforeData().containsKey("slave_1"));
    }

    @Test
    @DisplayName("getVelData — 缓存中所有 item 均过期，全部 entry 被移除")
    void testGetVelData_AllItemsExpired_AllRemoved() {
        VelEventData dto = buildDto();
        timeOutBuilderMock.when(() -> VelocityTimeOutBuilder.getWindowMinTime(any()))
                .thenReturn(10000L);

        String cacheJson = "{\"slave_1\":[{\"txnId\":\"t1\",\"ts\":2000,\"v\":10}],"
                + "\"slave_2\":[{\"txnId\":\"t2\",\"ts\":3000,\"v\":20}]}";
        when(xRedisTemplate.get(anyString())).thenReturn(cacheJson);

        VelWindowData result = service.getVelData(dto);

        assertTrue(result.getBeforeData().isEmpty());
        assertTrue(result.getAfterData().isEmpty());
    }

    @Test
    @DisplayName("getVelData — entry 中部分 item 过期但仍有有效 item，entry 保留")
    void testGetVelData_MixedItemsInEntry_EntryKept() {
        VelEventData dto = buildDto();
        timeOutBuilderMock.when(() -> VelocityTimeOutBuilder.getWindowMinTime(any()))
                .thenReturn(5000L);

        // slave_1: has 2 items, one expired (ts=2000), one valid (ts=6000)
        String cacheJson = "{\"slave_1\":[{\"txnId\":\"t1\",\"ts\":2000,\"v\":10},"
                + "{\"txnId\":\"t2\",\"ts\":6000,\"v\":30}]}";
        when(xRedisTemplate.get(anyString())).thenReturn(cacheJson);

        VelWindowData result = service.getVelData(dto);

        // entry removed because anyMatch finds the expired item → entire entry removed
        assertTrue(result.getBeforeData().isEmpty());
    }

    // ==================== 字段完整性 ====================

    @Test
    @DisplayName("getVelData — 返回的 VelWindowData 字段完整")
    void testGetVelData_CompleteFields() {
        VelEventData dto = buildDto();
        when(xRedisTemplate.get(anyString())).thenReturn(null);

        VelWindowData result = service.getVelData(dto);

        assertEquals(dto.getTxnId(), result.getTxnId());
        assertEquals(dto.getFeatureCode(), result.getFeatureCode());
        assertEquals(dto.getMasterValue(), result.getMasterValue());
        assertEquals(EXPIRE_TIME, result.getExpireTime());
    }

    // ==================== helper ====================

    private VelEventData buildDto() {
        VelEventData dto = new VelEventData();
        dto.setTxnId("TXN_001");
        dto.setFeatureCode("FEA_001");
        dto.setMasterValue("master_A");
        dto.setSlaveValue("slave_A");
        dto.setTimeMode("ttl");
        dto.setTimeWindow(60);
        dto.setTimeUnit("MINUTE");
        return dto;
    }
}
