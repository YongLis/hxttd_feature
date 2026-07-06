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
 * VelDataSunkByPersistService 单元测试
 *
 * @author yong.li
 * @since 2026-07-04
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("VelDataSunkByPersistService 单元测试")
class VelDataSunkByPersistServiceTest {

    @InjectMocks
    private VelDataSunkByPersistService service;

    @Mock
    private XRedisTemplate xRedisTemplate;

    private MockedStatic<VelocityRedisKeyBuilder> redisKeyBuilderMock;
    private MockedStatic<VelocityTimeOutBuilder> timeOutBuilderMock;

    private static final String REDIS_KEY = "vel:persist:test:key";
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
    @DisplayName("getTimeMode — 返回 CURRENT_YEAR")
    void testGetTimeMode() {
        assertEquals(VelocityTimeModeEnum.CURRENT_YEAR.getType(), service.getTimeMode());
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

    // ==================== 缓存命中 ====================

    @Test
    @DisplayName("getVelData — 缓存命中，beforeData 和 afterData 均为缓存数据")
    void testGetVelData_CacheHit_ReturnsCacheData() {
        VelEventData dto = buildDto();
        String cacheJson = "{\"slave_P\":[{\"txnId\":\"t1\",\"ts\":5000,\"v\":200}]}";
        when(xRedisTemplate.get(anyString())).thenReturn(cacheJson);

        VelWindowData result = service.getVelData(dto);

        assertEquals(1, result.getBeforeData().size());
        assertEquals(1, result.getAfterData().size());
        assertTrue(result.getBeforeData().containsKey("slave_P"));
        assertTrue(result.getAfterData().containsKey("slave_P"));
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
        dto.setTxnId("TXN_004");
        dto.setFeatureCode("FEA_004");
        dto.setMasterValue("master_D");
        dto.setSlaveValue("slave_D");
        dto.setTimeMode("cy");
        dto.setTimeWindow(1);
        dto.setTimeUnit("PERSIST");
        return dto;
    }
}
