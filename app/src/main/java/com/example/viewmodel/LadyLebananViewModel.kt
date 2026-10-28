package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.BlouseCategory
import com.example.model.BlouseItem
import com.example.model.CartItem
import com.example.model.CustomMeasurements
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.PaymentType
import com.example.model.SampleBlouseData
import com.example.model.ShippingAddress
import com.example.model.TrackingStep
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class MainTab(val title: String) {
    SHOP("Shop"),
    FITTING("Fit Guide"),
    WISHLIST("Wishlist"),
    BAG("Bag"),
    ORDERS("Orders")
}

enum class CheckoutStep {
    ADDRESS,
    PAYMENT_METHOD,
    PROCESSING,
    SUCCESS
}

data class LadyLebananUiState(
    val blouses: List<BlouseItem> = SampleBlouseData.blouseList,
    val searchQuery: String = "",
    val selectedCategory: BlouseCategory = BlouseCategory.ALL,
    val selectedFabricFilter: String? = null,
    val selectedNeckFilter: String? = null,
    val activeTab: MainTab = MainTab.SHOP,
    val selectedBlouseForDetail: BlouseItem? = null,
    val wishlistIds: Set<String> = setOf("lb-001", "lb-002"),
    val cartItems: List<CartItem> = emptyList(),
    val appliedCoupon: String? = null,
    val couponDiscountPercent: Int = 0,
    val orders: List<Order> = SampleBlouseData.createInitialOrders(),
    val trackingOrder: Order? = null,

    // Checkout State
    val isCheckoutVisible: Boolean = false,
    val checkoutStep: CheckoutStep = CheckoutStep.ADDRESS,
    val shippingAddress: ShippingAddress = ShippingAddress(),
    val selectedPaymentType: PaymentType = PaymentType.UPI,
    val upiIdInput: String = "ananya@oksbi",
    val selectedUpiApp: String = "Google Pay",
    val cardNumberInput: String = "",
    val cardHolderInput: String = "Ananya Sharma",
    val cardExpiryInput: String = "08/29",
    val cardCvvInput: String = "",
    val selectedBank: String = "HDFC Bank",
    val codConfirmed: Boolean = true,
    val isProcessingPayment: Boolean = false,
    val processingMessage: String = "Contacting payment gateway...",
    val latestConfirmedOrder: Order? = null,

    // Size Guide Tool State
    val guideBustInput: Float = 36f,
    val guideUnderbustInput: Float = 30f,
    val recommendedSize: String = "36 (M)"
) {
    val filteredBlouses: List<BlouseItem>
        get() = blouses.filter { blouse ->
            val matchesCategory = selectedCategory == BlouseCategory.ALL || blouse.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                blouse.name.contains(searchQuery, ignoreCase = true) ||
                blouse.fabric.contains(searchQuery, ignoreCase = true) ||
                blouse.neckStyle.contains(searchQuery, ignoreCase = true) ||
                blouse.description.contains(searchQuery, ignoreCase = true)
            val matchesFabric = selectedFabricFilter == null || blouse.fabric.contains(selectedFabricFilter, ignoreCase = true)
            val matchesNeck = selectedNeckFilter == null || blouse.neckStyle.contains(selectedNeckFilter, ignoreCase = true)

            matchesCategory && matchesSearch && matchesFabric && matchesNeck
        }

    val cartSubtotal: Double
        get() = cartItems.sumOf { it.totalPrice }

    val discountAmount: Double
        get() = (cartSubtotal * couponDiscountPercent) / 100.0

    val shippingFee: Double
        get() = if (cartSubtotal > 1500.0 || cartItems.isEmpty()) 0.0 else 99.0

    val cartTotal: Double
        get() = (cartSubtotal - discountAmount + shippingFee).coerceAtLeast(0.0)

    val cartItemCount: Int
        get() = cartItems.sumOf { it.quantity }

    val wishlistCount: Int
        get() = wishlistIds.size
}

class LadyLebananViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LadyLebananUiState())
    val uiState: StateFlow<LadyLebananUiState> = _uiState.asStateFlow()

    init {
        // Preload an item into cart for quick user trial
        val initialItem = CartItem(
            cartItemId = "cart-item-preview",
            blouse = SampleBlouseData.blouseList[1],
            selectedSize = "34 (S)",
            selectedColor = "Lilac Lavender",
            quantity = 1,
            isPadded = true
        )
        _uiState.update { it.copy(cartItems = listOf(initialItem)) }
    }

    fun onTabSelected(tab: MainTab) {
        _uiState.update { it.copy(activeTab = tab, selectedBlouseForDetail = null) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onCategorySelected(category: BlouseCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onFabricFilterSelected(fabric: String?) {
        _uiState.update { it.copy(selectedFabricFilter = if (it.selectedFabricFilter == fabric) null else fabric) }
    }

    fun onNeckFilterSelected(neck: String?) {
        _uiState.update { it.copy(selectedNeckFilter = if (it.selectedNeckFilter == neck) null else neck) }
    }

    fun openBlouseDetail(blouse: BlouseItem) {
        _uiState.update { it.copy(selectedBlouseForDetail = blouse) }
    }

    fun closeBlouseDetail() {
        _uiState.update { it.copy(selectedBlouseForDetail = null) }
    }

    fun toggleWishlist(blouseId: String) {
        _uiState.update { state ->
            val updated = if (state.wishlistIds.contains(blouseId)) {
                state.wishlistIds - blouseId
            } else {
                state.wishlistIds + blouseId
            }
            state.copy(wishlistIds = updated)
        }
    }

    fun addToCart(
        blouse: BlouseItem,
        size: String,
        color: String,
        quantity: Int = 1,
        customMeasurements: CustomMeasurements? = null,
        isPadded: Boolean = true
    ) {
        _uiState.update { state ->
            val existingIndex = state.cartItems.indexOfFirst {
                it.blouse.id == blouse.id && it.selectedSize == size && it.selectedColor == color && it.customMeasurements == customMeasurements
            }

            val updatedCart = if (existingIndex >= 0) {
                state.cartItems.mapIndexed { index, item ->
                    if (index == existingIndex) item.copy(quantity = item.quantity + quantity) else item
                }
            } else {
                state.cartItems + CartItem(
                    cartItemId = UUID.randomUUID().toString(),
                    blouse = blouse,
                    selectedSize = size,
                    selectedColor = color,
                    quantity = quantity,
                    customMeasurements = customMeasurements,
                    isPadded = isPadded
                )
            }
            state.copy(cartItems = updatedCart)
        }
    }

    fun updateCartQuantity(cartItemId: String, delta: Int) {
        _uiState.update { state ->
            val updated = state.cartItems.mapNotNull { item ->
                if (item.cartItemId == cartItemId) {
                    val newQty = item.quantity + delta
                    if (newQty > 0) item.copy(quantity = newQty) else null
                } else item
            }
            state.copy(cartItems = updated)
        }
    }

    fun removeCartItem(cartItemId: String) {
        _uiState.update { state ->
            state.copy(cartItems = state.cartItems.filterNot { it.cartItemId == cartItemId })
        }
    }

    fun applyCoupon(code: String): Boolean {
        val cleanCode = code.trim().uppercase()
        return when (cleanCode) {
            "LADYFIRST" -> {
                _uiState.update { it.copy(appliedCoupon = cleanCode, couponDiscountPercent = 20) }
                true
            }
            "BLOUSE15" -> {
                _uiState.update { it.copy(appliedCoupon = cleanCode, couponDiscountPercent = 15) }
                true
            }
            "FREESHIP" -> {
                _uiState.update { it.copy(appliedCoupon = cleanCode, couponDiscountPercent = 10) }
                true
            }
            else -> false
        }
    }

    fun removeCoupon() {
        _uiState.update { it.copy(appliedCoupon = null, couponDiscountPercent = 0) }
    }

    // Checkout Flow Actions
    fun startCheckout() {
        if (_uiState.value.cartItems.isEmpty()) return
        _uiState.update {
            it.copy(
                isCheckoutVisible = true,
                checkoutStep = CheckoutStep.ADDRESS
            )
        }
    }

    fun closeCheckout() {
        _uiState.update { it.copy(isCheckoutVisible = false, isProcessingPayment = false) }
    }

    fun updateShippingAddress(address: ShippingAddress) {
        _uiState.update { it.copy(shippingAddress = address) }
    }

    fun goToPaymentSelection() {
        _uiState.update { it.copy(checkoutStep = CheckoutStep.PAYMENT_METHOD) }
    }

    fun goToAddressStep() {
        _uiState.update { it.copy(checkoutStep = CheckoutStep.ADDRESS) }
    }

    fun setPaymentType(paymentType: PaymentType) {
        _uiState.update { it.copy(selectedPaymentType = paymentType) }
    }

    fun setUpiId(id: String) {
        _uiState.update { it.copy(upiIdInput = id) }
    }

    fun setUpiApp(app: String) {
        _uiState.update { it.copy(selectedUpiApp = app) }
    }

    fun setCardDetails(number: String, holder: String, expiry: String, cvv: String) {
        _uiState.update {
            it.copy(
                cardNumberInput = number,
                cardHolderInput = holder,
                cardExpiryInput = expiry,
                cardCvvInput = cvv
            )
        }
    }

    fun setBank(bank: String) {
        _uiState.update { it.copy(selectedBank = bank) }
    }

    fun confirmPaymentAndPlaceOrder() {
        val state = _uiState.value
        if (state.cartItems.isEmpty()) return

        _uiState.update {
            it.copy(
                isProcessingPayment = true,
                checkoutStep = CheckoutStep.PROCESSING,
                processingMessage = "Connecting with secure payment gateway..."
            )
        }

        viewModelScope.launch {
            delay(800)
            _uiState.update {
                it.copy(
                    processingMessage = when (it.selectedPaymentType) {
                        PaymentType.UPI -> "Awaiting confirmation from ${it.selectedUpiApp} (${it.upiIdInput})..."
                        PaymentType.CARD -> "Authorizing 3D Secure Card Tokenization..."
                        PaymentType.NET_BANKING -> "Connecting to ${it.selectedBank} portal..."
                        PaymentType.WALLET -> "Verifying digital wallet balance..."
                        PaymentType.CASH_ON_DELIVERY -> "Generating COD order verification OTP..."
                    }
                )
            }
            delay(1200)
            _uiState.update { it.copy(processingMessage = "Payment Approved! Stitching queue registered...") }
            delay(600)

            val newOrderId = "LL-BL-${(10000..99999).random()}"
            val nowFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
            val estDeliveryFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(System.currentTimeMillis() + 4 * 24 * 3600 * 1000L))

            val paymentDetailsStr = when (state.selectedPaymentType) {
                PaymentType.UPI -> "${state.selectedUpiApp} (${state.upiIdInput})"
                PaymentType.CARD -> "Card ending in ${if (state.cardNumberInput.length >= 4) state.cardNumberInput.takeLast(4) else "4829"}"
                PaymentType.NET_BANKING -> "${state.selectedBank} NetBanking"
                PaymentType.WALLET -> "Digital Wallet"
                PaymentType.CASH_ON_DELIVERY -> "Cash on Delivery (Verified)"
            }

            val newOrder = Order(
                orderId = newOrderId,
                items = state.cartItems,
                subtotal = state.cartSubtotal,
                discount = state.discountAmount,
                couponCode = state.appliedCoupon,
                shippingFee = state.shippingFee,
                total = state.cartTotal,
                shippingAddress = state.shippingAddress,
                paymentType = state.selectedPaymentType,
                paymentDetails = paymentDetailsStr,
                currentStatus = OrderStatus.PLACED,
                orderDate = nowFormat,
                estimatedDeliveryDate = estDeliveryFormat,
                trackingSteps = listOf(
                    TrackingStep(
                        status = OrderStatus.PLACED,
                        title = "Order Confirmed",
                        description = "Payment received. Measurements & design verified.",
                        dateText = nowFormat,
                        isCompleted = true,
                        isCurrent = false
                    ),
                    TrackingStep(
                        status = OrderStatus.TAILORING,
                        title = "Master Tailor Cutting",
                        description = "Pattern cutting, neck shaping & armhole alignment in progress.",
                        dateText = "Starting Today",
                        isCompleted = false,
                        isCurrent = true
                    ),
                    TrackingStep(
                        status = OrderStatus.QUALITY_CHECK,
                        title = "Quality & Finishing",
                        description = "Padding check, inner 2-inch margin inspection & pressing.",
                        dateText = "Expected in 2 days",
                        isCompleted = false,
                        isCurrent = false
                    ),
                    TrackingStep(
                        status = OrderStatus.DISPATCHED,
                        title = "Courier Pickup",
                        description = "Packed in Lady Lebanan signature box.",
                        dateText = "Expected in 3 days",
                        isCompleted = false,
                        isCurrent = false
                    ),
                    TrackingStep(
                        status = OrderStatus.DELIVERED,
                        title = "Delivery",
                        description = "Delivered to ${state.shippingAddress.city}.",
                        dateText = estDeliveryFormat,
                        isCompleted = false,
                        isCurrent = false
                    )
                )
            )

            _uiState.update {
                it.copy(
                    isProcessingPayment = false,
                    checkoutStep = CheckoutStep.SUCCESS,
                    latestConfirmedOrder = newOrder,
                    orders = listOf(newOrder) + it.orders,
                    cartItems = emptyList(),
                    appliedCoupon = null,
                    couponDiscountPercent = 0
                )
            }
        }
    }

    fun openTracking(order: Order) {
        _uiState.update { it.copy(trackingOrder = order) }
    }

    fun closeTracking() {
        _uiState.update { it.copy(trackingOrder = null) }
    }

    // Size Guide Tool
    fun updateGuideMeasurements(bust: Float, underbust: Float) {
        val recommended = when {
            bust < 33f -> "32 (XS)"
            bust < 35f -> "34 (S)"
            bust < 37f -> "36 (M)"
            bust < 39f -> "38 (L)"
            bust < 41f -> "40 (XL)"
            bust < 43f -> "42 (XXL)"
            else -> "44 (3XL)"
        }
        _uiState.update {
            it.copy(
                guideBustInput = bust,
                guideUnderbustInput = underbust,
                recommendedSize = recommended
            )
        }
    }
}
