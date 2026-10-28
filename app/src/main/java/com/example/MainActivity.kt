package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BrandHeader
import com.example.ui.screens.BlouseDetailScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutDialog
import com.example.ui.screens.FittingGuideScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RosePrimary
import com.example.viewmodel.LadyLebananViewModel
import com.example.viewmodel.MainTab
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                LadyLebananApp()
            }
        }
    }
}

@Composable
fun LadyLebananApp(viewModel: LadyLebananViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Handle back button when inside detail screen
    BackHandler(enabled = uiState.selectedBlouseForDetail != null) {
        viewModel.closeBlouseDetail()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (uiState.selectedBlouseForDetail == null) {
                BrandHeader(
                    wishlistCount = uiState.wishlistCount,
                    cartCount = uiState.cartItemCount,
                    onWishlistClick = { viewModel.onTabSelected(MainTab.WISHLIST) },
                    onCartClick = { viewModel.onTabSelected(MainTab.BAG) }
                )
            }
        },
        bottomBar = {
            if (uiState.selectedBlouseForDetail == null) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    val tabs = listOf(
                        Triple(MainTab.SHOP, Icons.Filled.Store, Icons.Outlined.Store),
                        Triple(MainTab.FITTING, Icons.Filled.Straighten, Icons.Outlined.Straighten),
                        Triple(MainTab.WISHLIST, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
                        Triple(MainTab.BAG, Icons.Filled.ShoppingBag, Icons.Outlined.ShoppingBag),
                        Triple(MainTab.ORDERS, Icons.Filled.LocalShipping, Icons.Outlined.LocalShipping)
                    )

                    tabs.forEach { (tab, selectedIcon, unselectedIcon) ->
                        val isSelected = uiState.activeTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.onTabSelected(tab) },
                            icon = {
                                if (tab == MainTab.BAG && uiState.cartItemCount > 0) {
                                    BadgedBox(badge = {
                                        Badge(containerColor = RosePrimary, contentColor = Color.White) {
                                            Text("${uiState.cartItemCount}")
                                        }
                                    }) {
                                        Icon(
                                            imageVector = if (isSelected) selectedIcon else unselectedIcon,
                                            contentDescription = tab.title
                                        )
                                    }
                                } else if (tab == MainTab.WISHLIST && uiState.wishlistCount > 0) {
                                    BadgedBox(badge = {
                                        Badge(containerColor = RosePrimary, contentColor = Color.White) {
                                            Text("${uiState.wishlistCount}")
                                        }
                                    }) {
                                        Icon(
                                            imageVector = if (isSelected) selectedIcon else unselectedIcon,
                                            contentDescription = tab.title
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = if (isSelected) selectedIcon else unselectedIcon,
                                        contentDescription = tab.title
                                    )
                                }
                            },
                            label = { Text(text = tab.title, fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = RosePrimary,
                                selectedTextColor = RosePrimary,
                                indicatorColor = RosePrimary.copy(alpha = 0.12f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.selectedBlouseForDetail != null) {
                BlouseDetailScreen(
                    blouse = uiState.selectedBlouseForDetail!!,
                    isWishlisted = uiState.wishlistIds.contains(uiState.selectedBlouseForDetail!!.id),
                    onBackClick = { viewModel.closeBlouseDetail() },
                    onToggleWishlist = { viewModel.toggleWishlist(uiState.selectedBlouseForDetail!!.id) },
                    onAddToCart = { size, color, measurements, isPadded ->
                        viewModel.addToCart(
                            blouse = uiState.selectedBlouseForDetail!!,
                            size = size,
                            color = color,
                            customMeasurements = measurements,
                            isPadded = isPadded
                        )
                        scope.launch {
                            snackbarHostState.showSnackbar("Added to your shopping bag!")
                        }
                    },
                    onBuyNow = { size, color, measurements, isPadded ->
                        viewModel.addToCart(
                            blouse = uiState.selectedBlouseForDetail!!,
                            size = size,
                            color = color,
                            customMeasurements = measurements,
                            isPadded = isPadded
                        )
                        viewModel.closeBlouseDetail()
                        viewModel.startCheckout()
                    },
                    onOpenSizeGuide = {
                        viewModel.closeBlouseDetail()
                        viewModel.onTabSelected(MainTab.FITTING)
                    }
                )
            } else {
                AnimatedContent(
                    targetState = uiState.activeTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_transition"
                ) { targetTab ->
                    when (targetTab) {
                        MainTab.SHOP -> {
                            ShopScreen(
                                blouses = uiState.filteredBlouses,
                                searchQuery = uiState.searchQuery,
                                selectedCategory = uiState.selectedCategory,
                                selectedFabricFilter = uiState.selectedFabricFilter,
                                selectedNeckFilter = uiState.selectedNeckFilter,
                                wishlistIds = uiState.wishlistIds,
                                onSearchChange = { viewModel.onSearchQueryChanged(it) },
                                onCategorySelect = { viewModel.onCategorySelected(it) },
                                onFabricSelect = { viewModel.onFabricFilterSelected(it) },
                                onNeckSelect = { viewModel.onNeckFilterSelected(it) },
                                onBlouseClick = { viewModel.openBlouseDetail(it) },
                                onToggleWishlist = { viewModel.toggleWishlist(it) },
                                onApplyCouponQuick = { coupon ->
                                    val ok = viewModel.applyCoupon(coupon)
                                    if (ok) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Coupon '$coupon' applied (Flat 20% Off)!")
                                        }
                                    }
                                }
                            )
                        }

                        MainTab.FITTING -> {
                            FittingGuideScreen(
                                guideBust = uiState.guideBustInput,
                                guideUnderbust = uiState.guideUnderbustInput,
                                recommendedSize = uiState.recommendedSize,
                                onMeasurementsChange = { bust, underbust ->
                                    viewModel.updateGuideMeasurements(bust, underbust)
                                }
                            )
                        }

                        MainTab.WISHLIST -> {
                            val wishlistedBlouses = uiState.blouses.filter { uiState.wishlistIds.contains(it.id) }
                            WishlistScreen(
                                wishlistedBlouses = wishlistedBlouses,
                                onBlouseClick = { viewModel.openBlouseDetail(it) },
                                onRemoveFromWishlist = { viewModel.toggleWishlist(it) },
                                onMoveToCart = { blouse ->
                                    viewModel.addToCart(
                                        blouse = blouse,
                                        size = "36 (M)",
                                        color = blouse.colors.firstOrNull()?.name ?: "Default"
                                    )
                                    viewModel.toggleWishlist(blouse.id)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Moved to bag!")
                                    }
                                },
                                onExploreShop = { viewModel.onTabSelected(MainTab.SHOP) }
                            )
                        }

                        MainTab.BAG -> {
                            CartScreen(
                                cartItems = uiState.cartItems,
                                subtotal = uiState.cartSubtotal,
                                discountAmount = uiState.discountAmount,
                                shippingFee = uiState.shippingFee,
                                grandTotal = uiState.cartTotal,
                                appliedCoupon = uiState.appliedCoupon,
                                couponDiscountPercent = uiState.couponDiscountPercent,
                                onQuantityChange = { cartId, delta -> viewModel.updateCartQuantity(cartId, delta) },
                                onRemoveItem = { cartId -> viewModel.removeCartItem(cartId) },
                                onApplyCoupon = { code ->
                                    val ok = viewModel.applyCoupon(code)
                                    if (ok) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Coupon applied successfully!")
                                        }
                                    }
                                    ok
                                },
                                onRemoveCoupon = { viewModel.removeCoupon() },
                                onProceedToCheckout = { viewModel.startCheckout() },
                                onExploreShop = { viewModel.onTabSelected(MainTab.SHOP) }
                            )
                        }

                        MainTab.ORDERS -> {
                            OrdersScreen(
                                orders = uiState.orders,
                                trackingOrder = uiState.trackingOrder,
                                onTrackClick = { viewModel.openTracking(it) },
                                onCloseTracking = { viewModel.closeTracking() },
                                onExploreShop = { viewModel.onTabSelected(MainTab.SHOP) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Checkout Flow Dialog with Payment Options
    CheckoutDialog(
        isOpen = uiState.isCheckoutVisible,
        currentStep = uiState.checkoutStep,
        shippingAddress = uiState.shippingAddress,
        selectedPaymentType = uiState.selectedPaymentType,
        upiId = uiState.upiIdInput,
        selectedUpiApp = uiState.selectedUpiApp,
        cardNumber = uiState.cardNumberInput,
        cardHolder = uiState.cardHolderInput,
        cardExpiry = uiState.cardExpiryInput,
        cardCvv = uiState.cardCvvInput,
        selectedBank = uiState.selectedBank,
        totalAmount = uiState.cartTotal,
        isProcessing = uiState.isProcessingPayment,
        processingMessage = uiState.processingMessage,
        latestOrder = uiState.latestConfirmedOrder,
        onClose = { viewModel.closeCheckout() },
        onAddressSubmit = { address ->
            viewModel.updateShippingAddress(address)
            viewModel.goToPaymentSelection()
        },
        onGoToAddressStep = { viewModel.goToAddressStep() },
        onSelectPaymentType = { viewModel.setPaymentType(it) },
        onUpiIdChange = { viewModel.setUpiId(it) },
        onUpiAppChange = { viewModel.setUpiApp(it) },
        onCardDetailsChange = { num, holder, exp, cvv ->
            viewModel.setCardDetails(num, holder, exp, cvv)
        },
        onBankChange = { viewModel.setBank(it) },
        onConfirmOrder = { viewModel.confirmPaymentAndPlaceOrder() },
        onTrackOrder = { order ->
            viewModel.closeCheckout()
            viewModel.onTabSelected(MainTab.ORDERS)
            viewModel.openTracking(order)
        }
    )
}
