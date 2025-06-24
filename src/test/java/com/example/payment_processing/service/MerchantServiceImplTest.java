package com.example.payment_processing.service;

import com.example.payment_processing.dto.MerchantRevenueDTO;
import com.example.payment_processing.model.Merchant;
import com.example.payment_processing.model.Payment;
import com.example.payment_processing.repository.MerchantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
public class MerchantServiceImplTest {

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private MerchantServiceImpl merchantService;

    private Merchant merchant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        merchant = new Merchant("ShopName", "shop@example.com");
        merchant.setId(1L);

        Payment payment = new Payment();
        payment.setAmount(100.0);
        merchant.setPayments(Collections.singletonList(payment));
    }

    @Test
    void testCreateMerchant_Success() {
        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);

        Merchant createdMerchant = merchantService.createMerchant(merchant);

        assertNotNull(createdMerchant);
        assertEquals("ShopName", createdMerchant.getMerchantName());
        verify(merchantRepository, times(1)).save(any(Merchant.class));
    }

    @Test
    void testGetMerchantById_Success() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));

        Merchant foundMerchant = merchantService.getMerchantById(1L);

        assertNotNull(foundMerchant);
        assertEquals("ShopName", foundMerchant.getMerchantName());
        verify(merchantRepository, times(1)).findById(1L);
    }

    @Test
    void testGetMerchantById_NotFound() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            merchantService.getMerchantById(1L);
        });

        assertTrue(exception.getMessage().contains("Merchant not found"));
        verify(merchantRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllMerchants_Success() {
        when(merchantRepository.findAll()).thenReturn(Collections.singletonList(merchant));

        List<Merchant> merchants = merchantService.getAllMerchants();

        assertNotNull(merchants);
        assertEquals(1, merchants.size());
        verify(merchantRepository, times(1)).findAll();
    }

    @Test
    void testUpdateMerchant_Success() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        when(merchantRepository.save(any(Merchant.class))).thenReturn(merchant);

        Merchant updatedMerchant = new Merchant("UpdatedShop", "updated@example.com");

        Merchant result = merchantService.updateMerchant(1L, updatedMerchant);

        assertNotNull(result);
        assertEquals("UpdatedShop", result.getMerchantName());
        assertEquals("updated@example.com", result.getMerchantEmail());
        verify(merchantRepository, times(1)).findById(1L);
        verify(merchantRepository, times(1)).save(any(Merchant.class));
    }

    @Test
    void testDeleteMerchant_Success() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));
        doNothing().when(merchantRepository).deleteById(1L);

        assertDoesNotThrow(() -> merchantService.deleteMerchant(1L));
        verify(merchantRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCalculateTotalRevenue_Success() {
        when(merchantRepository.findById(1L)).thenReturn(Optional.of(merchant));

        MerchantRevenueDTO revenueDTO = merchantService.calculateTotalRevenue(1L);

        assertNotNull(revenueDTO);
        assertEquals(100.0, revenueDTO.getTotalRevenue());
        assertEquals("ShopName", revenueDTO.getMerchantName());
        verify(merchantRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTopMerchants_Success() {
        Object[] topMerchantData = {1L, "ShopName", 100.0};
        when(merchantRepository.findTopMerchants()).thenReturn(Collections.singletonList(topMerchantData));

        List<Map<String, Object>> topMerchants = merchantService.getTopMerchants();

        assertNotNull(topMerchants);
        assertEquals(1, topMerchants.size());
        assertEquals("ShopName", topMerchants.get(0).get("merchantName"));
        verify(merchantRepository, times(1)).findTopMerchants();
    }
}
