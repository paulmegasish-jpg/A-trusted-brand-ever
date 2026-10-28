package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CartItem
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.RosePrimary

@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    subtotal: Double,
    discountAmount: Double,
    shippingFee: Double,
    grandTotal: Double,
    appliedCoupon: String?,
    couponDiscountPercent: Int,
    onQuantityChange: (cartItemId: String, delta: Int) -> Unit,
    onRemoveItem: (cartItemId: String) -> Unit,
    onApplyCoupon: (String) -> Boolean,
    onRemoveCoupon: () -> Unit,
    onProceedToCheckout: () -> Unit,
    onExploreShop: () -> Unit,
    modifier: Modifier = Modifier
) {
    var couponInput by remember { mutableStateOf("") }
    var couponError by remember { mutableStateOf(false) }

    if (cartItems.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(RosePrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Empty Bag",
                    tint = RosePrimary,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Your Shopping Bag is Empty",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Add designer silk, organza, or velvet blouses to tailor your dream look.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onExploreShop,
                colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
            ) {
                Text("Start Shopping")
            }
        }
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Free Shipping banner progress
                item {
                    val remainingForFree = (1500.0 - subtotal).coerceAtLeast(0.0)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (remainingForFree == 0.0) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (remainingForFree == 0.0) Color(0xFF2E7D32) else Color(0xFFE65100),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (remainingForFree == 0.0) "Yay! You unlocked FREE Express Delivery!" else "Add ₹${remainingForFree.toInt()} more to get FREE Express Delivery!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (remainingForFree == 0.0) Color(0xFF2E7D32) else Color(0xFFE65100)
                            )
                        }
                    }
                }

                // Cart Items
                items(cartItems, key = { it.cartItemId }) { item ->
                    CartItemCard(
                        cartItem = item,
                        onQuantityChange = { delta -> onQuantityChange(item.cartItemId, delta) },
                        onRemove = { onRemoveItem(item.cartItemId) }
                    )
                }

                // Coupon Code Section
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = "Coupon",
                                    tint = RosePrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Boutique Coupons & Offers",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            if (appliedCoupon != null) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFE8F5E9),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = "Coupon '$appliedCoupon' Applied!",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = Color(0xFF2E7D32)
                                            )
                                            Text(
                                                text = "$couponDiscountPercent% Discount applied to your blouse order",
                                                fontSize = 11.sp,
                                                color = Color(0xFF388E3C)
                                            )
                                        }

                                        IconButton(onClick = onRemoveCoupon, modifier = Modifier.size(32.dp)) {
                                            Icon(Icons.Default.Close, contentDescription = "Remove coupon", tint = Color.Gray)
                                        }
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = couponInput,
                                        onValueChange = {
                                            couponInput = it
                                            couponError = false
                                        },
                                        placeholder = { Text("Enter Coupon Code (e.g. LADYFIRST)", fontSize = 11.sp) },
                                        singleLine = true,
                                        isError = couponError,
                                        shape = RoundedCornerShape(8.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = RosePrimary
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = {
                                            val success = onApplyCoupon(couponInput)
                                            if (success) {
                                                couponInput = ""
                                                couponError = false
                                            } else {
                                                couponError = true
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Apply", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                if (couponError) {
                                    Text(
                                        text = "Invalid coupon. Try 'LADYFIRST' for 20% off or 'BLOUSE15'",
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Quick tap coupons
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    QuickCouponChip("LADYFIRST", "20% OFF") {
                                        onApplyCoupon("LADYFIRST")
                                    }
                                    QuickCouponChip("BLOUSE15", "15% OFF") {
                                        onApplyCoupon("BLOUSE15")
                                    }
                                }
                            }
                        }
                    }
                }

                // Bill Details
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Order Bill Summary",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            BillLineItem("Bag Subtotal", "₹${subtotal.toInt()}")
                            if (discountAmount > 0) {
                                BillLineItem("Coupon Discount", "- ₹${discountAmount.toInt()}", isDiscount = true)
                            }
                            BillLineItem("Express Delivery Fee", if (shippingFee == 0.0) "FREE" else "₹${shippingFee.toInt()}")
                            BillLineItem("Master Tailoring & Alteration Guarantee", "FREE (Included)")

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 10.dp),
                                color = DividerDefaults.color.copy(alpha = 0.5f)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total Payable Amount",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "₹${grandTotal.toInt()}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = RosePrimary
                                )
                            }
                        }
                    }
                }

                // Trust Footer
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Safe",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Safe & Secure 256-Bit SSL Checkout • Multi-Payment Options",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Sticky Bottom Checkout Button
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Total Payable", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = "₹${grandTotal.toInt()}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = RosePrimary
                        )
                    }

                    Button(
                        onClick = onProceedToCheckout,
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("cart_proceed_checkout_btn"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RosePrimary)
                    ) {
                        Text(
                            text = "Select Payment & Checkout",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = cartItem.blouse.imageRes),
                contentDescription = cartItem.blouse.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = cartItem.blouse.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Remove",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Size: ${cartItem.selectedSize}  |  Color: ${cartItem.selectedColor}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (cartItem.isPadded) {
                    Text(
                        text = "• Padded Cups Included",
                        fontSize = 10.sp,
                        color = ChampagneGold,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (cartItem.customMeasurements != null) {
                    Text(
                        text = "• Custom Fit Specs Saved",
                        fontSize = 10.sp,
                        color = RosePrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${cartItem.totalPrice.toInt()}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = RosePrimary
                    )

                    // Quantity Stepper
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(2.dp)
                    ) {
                        IconButton(
                            onClick = { onQuantityChange(-1) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(14.dp))
                        }
                        Text(
                            text = "${cartItem.quantity}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                        IconButton(
                            onClick = { onQuantityChange(1) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BillLineItem(title: String, value: String, isDiscount: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDiscount) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun QuickCouponChip(code: String, discountText: String, onApply: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = RosePrimary.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, RosePrimary.copy(alpha = 0.3f)),
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable { onApply() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = code, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = RosePrimary)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "($discountText)", fontSize = 9.sp, color = ChampagneGold, fontWeight = FontWeight.Bold)
        }
    }
}
