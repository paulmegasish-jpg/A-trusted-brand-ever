package com.example

import com.example.model.BlouseCategory
import com.example.model.SampleBlouseData
import com.example.viewmodel.LadyLebananViewModel
import com.example.model.PaymentType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LadyLebananUnitTest {

    private lateinit var viewModel: LadyLebananViewModel

    @Before
    fun setup() {
        viewModel = LadyLebananViewModel()
    }

    @Test
    fun testBlouseCatalogInitialization() {
        val state = viewModel.uiState.value
        assertTrue("Blouse catalog should not be empty", state.blouses.isNotEmpty())
        assertTrue("Should contain silk zari blouse", state.blouses.any { it.category == BlouseCategory.SILK_ZARI })
    }

    @Test
    fun testCouponApplication() {
        val validCoupon = viewModel.applyCoupon("LADYFIRST")
        assertTrue("LADYFIRST coupon should be valid", validCoupon)
        assertEquals(20, viewModel.uiState.value.couponDiscountPercent)

        viewModel.removeCoupon()
        assertEquals(0, viewModel.uiState.value.couponDiscountPercent)
    }

    @Test
    fun testSizeGuideCalculation() {
        viewModel.updateGuideMeasurements(36f, 30f)
        assertEquals("36 (M)", viewModel.uiState.value.recommendedSize)

        viewModel.updateGuideMeasurements(40f, 34f)
        assertEquals("40 (XL)", viewModel.uiState.value.recommendedSize)
    }

    @Test
    fun testAddToCartAndQuantity() {
        val blouse = SampleBlouseData.blouseList[0]
        val initialCount = viewModel.uiState.value.cartItemCount

        viewModel.addToCart(blouse, "36 (M)", "Rani Magenta", 1)
        assertTrue("Cart count should increase", viewModel.uiState.value.cartItemCount > initialCount)
    }

    @Test
    fun testPaymentTypeSelection() {
        viewModel.setPaymentType(com.example.model.PaymentType.CARD)
        assertEquals(com.example.model.PaymentType.CARD, viewModel.uiState.value.selectedPaymentType)

        viewModel.setPaymentType(com.example.model.PaymentType.CASH_ON_DELIVERY)
        assertEquals(com.example.model.PaymentType.CASH_ON_DELIVERY, viewModel.uiState.value.selectedPaymentType)
    }
}
