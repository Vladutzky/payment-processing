package com.example.payment_processing.controller;

import com.example.payment_processing.dto.MerchantRevenueDTO;
import com.example.payment_processing.model.Merchant;
import com.example.payment_processing.service.MerchantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MerchantControllerTest {

    @Mock
    private MerchantService merchantService;

    @InjectMocks
    private MerchantController merchantController;

    private Merchant merchant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        merchant = new Merchant("ShopName", "shop@example.com");
        merchant.setId(1L);
    }

    @Test
    void testCreateMerchant_Success() {
        when(merchantService.createMerchant(any(Merchant.class))).thenReturn(merchant);

        ResponseEntity<Merchant> response = merchantController.createMerchant(merchant);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ShopName", response.getBody().getMerchantName());
    }

    @Test
    void testGetMerchant_Success() {
        when(merchantService.getMerchantById(1L)).thenReturn(merchant);

        ResponseEntity<Merchant> response = merchantController.getMerchant(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("ShopName", response.getBody().getMerchantName());
    }

    @Test
    void testCalculateTotalRevenue_Success() {
        MerchantRevenueDTO revenueDTO = new MerchantRevenueDTO(1L, "ShopName", 1000.0, List.of(101L, 102L));
        when(merchantService.calculateTotalRevenue(1L)).thenReturn(revenueDTO);

        ResponseEntity<MerchantRevenueDTO> response = merchantController.calculateTotalRevenue(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1000.0, response.getBody().getTotalRevenue());
    }

    @Test
    void testGetAllMerchants_Success() {
        when(merchantService.getAllMerchants()).thenReturn(List.of(merchant));

        ResponseEntity<List<Merchant>> response = merchantController.getAllMerchants();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetTopMerchants_Success() {
        Map<String, Object> topMerchant = Map.of("merchantId", 1L, "merchantName", "ShopName", "totalRevenue", 1000.0);
        when(merchantService.getTopMerchants()).thenReturn(List.of(topMerchant));

        ResponseEntity<List<Map<String, Object>>> response = merchantController.getTopMerchants();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("ShopName", response.getBody().get(0).get("merchantName"));
    }

    @Test
    void testUpdateMerchant_Success() {
        Merchant updatedMerchant = new Merchant("UpdatedShop", "updated@example.com");
        when(merchantService.updateMerchant(eq(1L), any(Merchant.class))).thenReturn(updatedMerchant);

        ResponseEntity<Merchant> response = merchantController.updateMerchant(1L, updatedMerchant);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("UpdatedShop", response.getBody().getMerchantName());
    }

    @Test
    void testDeleteMerchant_Success() {
        doNothing().when(merchantService).deleteMerchant(1L);

        ResponseEntity<Void> response = merchantController.deleteMerchant(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(merchantService, times(1)).deleteMerchant(1L);
    }
}
