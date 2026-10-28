package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.model.Order
import com.example.model.PaymentType
import com.example.model.ShippingAddress
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.RosePrimary
import com.example.viewmodel.CheckoutStep

@Composable
fun CheckoutDialog(
    isOpen: Boolean,
    currentStep: CheckoutStep,
    shippingAddress: ShippingAddress,
    selectedPaymentType: PaymentType,
    upiId: String,
    selectedUpiApp: String,
    cardNumber: String,
    cardHolder: String,
    cardExpiry: String,
    cardCvv: String,
    selectedBank: String,
    totalAmount: Double,
    isProcessing: Boolean,
    processingMessage: String,
    latestOrder: Order?,
    onClose: () -> Unit,
    onAddressSubmit: (ShippingAddress) -> Unit,
    onGoToAddressStep: () -> Unit,
    onSelectPaymentType: (PaymentType) -> Unit,
    onUpiIdChange: (String) -> Unit,
    onUpiAppChange: (String) -> Unit,
    onCardDetailsChange: (number: String, holder: String, expiry: String, cvv: String) -> Unit,
    onBankChange: (String) -> Unit,
    onConfirmOrder: () -> Unit,
    onTrackOrder: (Order) -> Unit
) {
    if (!isOpen) return

    Dialog(
        onDismissRequest = {
            if (!isProcessing) onClose()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 28.dp),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Header
                Surface(
                    shadowElevation = 2.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (currentStep == CheckoutStep.PAYMENT_METHOD && !isProcessing) {
                                IconButton(
                                    onClick = onGoToAddressStep,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            Column {
                                Text(
                                    text = when (currentStep) {
                                        CheckoutStep.ADDRESS -> "Step 1: Delivery Address"
                                        CheckoutStep.PAYMENT_METHOD -> "Step 2: Select Payment Option"
                                        CheckoutStep.PROCESSING -> "Authorizing Payment..."
                                        CheckoutStep.SUCCESS -> "Order Placed Successfully!"
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = RosePrimary
                                )
                                Text(
                                    text = "Lady Lebanan Secure Checkout",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (!isProcessing && currentStep != CheckoutStep.SUCCESS) {
                            IconButton(onClick = onClose) {
                                Icon(Icons.Default.Close, contentDescription = "Close Checkout")
                            }
                        }
                    }
                }

                // Body content based on step
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    when (currentStep) {
                        CheckoutStep.ADDRESS -> {
                            AddressStepContent(
                                initial = shippingAddress,
                                onContinue = onAddressSubmit
                            )
                        }
                        CheckoutStep.PAYMENT_METHOD -> {
                            PaymentStepContent(
                                selectedPaymentType = selectedPaymentType,
                                upiId = upiId,
                                selectedUpiApp = selectedUpiApp,
                                cardNumber = cardNumber,
                                cardHolder = cardHolder,
                                cardExpiry = cardExpiry,
                                cardCvv = cardCvv,
                                selectedBank = selectedBank,
                                totalAmount = totalAmount,
                                onSelectPaymentType = onSelectPaymentType,
                                onUpiIdChange = onUpiIdChange,
                                onUpiAppChange = onUpiAppChange,
                                onCardDetailsChange = onCardDetailsChange,
                                onBankChange = onBankChange,
                                onPayNow = onConfirmOrder
                            )
                        }
                        CheckoutStep.PROCESSING -> {
                            ProcessingPaymentContent(message = processingMessage)
                        }
                        CheckoutStep.SUCCESS -> {
                            latestOrder?.let { order ->
                                OrderSuccessContent(
                                    order = order,
                                    onTrackClick = { onTrackOrder(order) },
                                    onContinueShopping = onClose
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddressStepContent(
    initial: ShippingAddress,
    onContinue: (ShippingAddress) -> Unit
) {
    var name by remember { mutableStateOf(initial.fullName) }
    var phone by remember { mutableStateOf(initial.phone) }
    var street by remember { mutableStateOf(initial.street) }
    var city by remember { mutableStateOf(initial.city) }
    var state by remember { mutableStateOf(initial.state) }
    var pinCode by remember { mutableStateOf(initial.pinCode) }
    var landmark by remember { mutableStateOf(initial.landmark) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = RosePrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Where should we deliver your handcrafted blouses?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Contact Phone (for delivery SMS) *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = street,
            onValueChange = { street = it },
            label = { Text("Flat / House No. / Street Name *") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("City *") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = pinCode,
                onValueChange = { pinCode = it },
                label = { Text("Pin Code *") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = state,
                onValueChange = { state = it },
                label = { Text("State") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = landmark,
                onValueChange = { landmark = it },
                label = { Text("Landmark (Optional)") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onContinue(
                    ShippingAddress(
                        fullName = name.ifBlank { "Ananya Sharma" },
                        phone = phone.ifBlank { "+91 98765 43210" },
                        street = street.ifBlank { "MG Road, Flat 402" },
                        city = city.ifBlank { "Bengaluru" },
                        state = state.ifBlank { "Karnataka" },
                        pinCode = pinCode.ifBlank { "560001" },
                        landmark = landmark
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("checkout_continue_payment_btn")
        ) {
            Text(
                text = "Continue to Payment Options",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun PaymentStepContent(
    selectedPaymentType: PaymentType,
    upiId: String,
    selectedUpiApp: String,
    cardNumber: String,
    cardHolder: String,
    cardExpiry: String,
    cardCvv: String,
    selectedBank: String,
    totalAmount: Double,
    onSelectPaymentType: (PaymentType) -> Unit,
    onUpiIdChange: (String) -> Unit,
    onUpiAppChange: (String) -> Unit,
    onCardDetailsChange: (number: String, holder: String, expiry: String, cvv: String) -> Unit,
    onBankChange: (String) -> Unit,
    onPayNow: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Amount to pay highlight
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = RosePrimary.copy(alpha = 0.08f),
            border = androidx.compose.foundation.BorderStroke(1.dp, RosePrimary.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Payable Amount", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "₹${totalAmount.toInt()}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = RosePrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Encrypted",
                        tint = ChampagneGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "256-Bit SSL Secured",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ChampagneGold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select Payment Option",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(10.dp))

        // 1. UPI / QR Option
        PaymentOptionCard(
            type = PaymentType.UPI,
            title = PaymentType.UPI.title,
            subtitle = PaymentType.UPI.subtitle,
            icon = Icons.Default.QrCode2,
            isSelected = selectedPaymentType == PaymentType.UPI,
            onSelect = { onSelectPaymentType(PaymentType.UPI) }
        ) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(text = "Choose UPI App:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    listOf("Google Pay", "PhonePe", "Paytm", "BHIM UPI").forEach { app ->
                        val isAppSelected = selectedUpiApp == app
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isAppSelected) RosePrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isAppSelected) androidx.compose.foundation.BorderStroke(1.dp, RosePrimary) else null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onUpiAppChange(app) }
                        ) {
                            Text(
                                text = app,
                                fontSize = 11.sp,
                                fontWeight = if (isAppSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isAppSelected) RosePrimary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = upiId,
                    onValueChange = onUpiIdChange,
                    label = { Text("Or Enter Virtual Payment Address (UPI ID)", fontSize = 11.sp) },
                    placeholder = { Text("e.g. mobile@upi", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Credit & Debit Card Option
        var num by remember { mutableStateOf(cardNumber.ifBlank { "4532 8921 7361 4829" }) }
        var holder by remember { mutableStateOf(cardHolder) }
        var expiry by remember { mutableStateOf(cardExpiry) }
        var cvv by remember { mutableStateOf(cardCvv.ifBlank { "839" }) }

        PaymentOptionCard(
            type = PaymentType.CARD,
            title = PaymentType.CARD.title,
            subtitle = PaymentType.CARD.subtitle,
            icon = Icons.Default.CreditCard,
            isSelected = selectedPaymentType == PaymentType.CARD,
            onSelect = { onSelectPaymentType(PaymentType.CARD) }
        ) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                OutlinedTextField(
                    value = num,
                    onValueChange = {
                        num = it
                        onCardDetailsChange(it, holder, expiry, cvv)
                    },
                    label = { Text("Card Number", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = holder,
                    onValueChange = {
                        holder = it
                        onCardDetailsChange(num, it, expiry, cvv)
                    },
                    label = { Text("Name on Card", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = expiry,
                        onValueChange = {
                            expiry = it
                            onCardDetailsChange(num, holder, it, cvv)
                        },
                        label = { Text("MM / YY", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = {
                            cvv = it
                            onCardDetailsChange(num, holder, expiry, it)
                        },
                        label = { Text("CVV", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3. Net Banking Option
        PaymentOptionCard(
            type = PaymentType.NET_BANKING,
            title = PaymentType.NET_BANKING.title,
            subtitle = PaymentType.NET_BANKING.subtitle,
            icon = Icons.Default.AccountBalance,
            isSelected = selectedPaymentType == PaymentType.NET_BANKING,
            onSelect = { onSelectPaymentType(PaymentType.NET_BANKING) }
        ) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(text = "Select Bank:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                listOf("HDFC Bank", "ICICI Bank", "State Bank of India (SBI)", "Axis Bank", "Kotak Mahindra").forEach { bank ->
                    val isBankSelected = selectedBank == bank
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onBankChange(bank) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isBankSelected,
                            onClick = { onBankChange(bank) },
                            colors = RadioButtonDefaults.colors(selectedColor = RosePrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = bank, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 4. Digital Wallets
        PaymentOptionCard(
            type = PaymentType.WALLET,
            title = PaymentType.WALLET.title,
            subtitle = PaymentType.WALLET.subtitle,
            icon = Icons.Default.AccountBalanceWallet,
            isSelected = selectedPaymentType == PaymentType.WALLET,
            onSelect = { onSelectPaymentType(PaymentType.WALLET) }
        ) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    text = "Pay via linked Paytm or Amazon Pay Wallet instantly.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 5. Cash on Delivery (COD)
        PaymentOptionCard(
            type = PaymentType.CASH_ON_DELIVERY,
            title = PaymentType.CASH_ON_DELIVERY.title,
            subtitle = PaymentType.CASH_ON_DELIVERY.subtitle,
            icon = Icons.Default.CurrencyRupee,
            isSelected = selectedPaymentType == PaymentType.CASH_ON_DELIVERY,
            onSelect = { onSelectPaymentType(PaymentType.CASH_ON_DELIVERY) }
        ) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "• You can pay via Cash or UPI QR upon receiving your blouse delivery package.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Pay Button
        Button(
            onClick = onPayNow,
            colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("checkout_pay_now_btn")
        ) {
            Text(
                text = when (selectedPaymentType) {
                    PaymentType.CASH_ON_DELIVERY -> "Confirm COD Order (₹${totalAmount.toInt()})"
                    else -> "Pay ₹${totalAmount.toInt()} Securely"
                },
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun PaymentOptionCard(
    type: PaymentType,
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit,
    expandedContent: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) RosePrimary.copy(alpha = 0.04f) else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) RosePrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSelect() }
            .testTag("payment_opt_${type.name.lowercase()}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) RosePrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = if (isSelected) RosePrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isSelected) RosePrimary else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                RadioButton(
                    selected = isSelected,
                    onClick = onSelect,
                    colors = RadioButtonDefaults.colors(selectedColor = RosePrimary)
                )
            }

            AnimatedVisibility(visible = isSelected) {
                expandedContent()
            }
        }
    }
}

@Composable
fun ProcessingPaymentContent(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = RosePrimary,
            strokeWidth = 3.dp,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Processing Your Order",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
fun OrderSuccessContent(
    order: Order,
    onTrackClick: () -> Unit,
    onContinueShopping: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F5E9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = Color(0xFF2E7D32),
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Order Placed Successfully!",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = RosePrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Order ID: ${order.orderId}",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = ChampagneGold
        )

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Delivery & Tailoring Summary",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Estimated Delivery: ${order.estimatedDeliveryDate}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Delivering to: ${order.shippingAddress.fullName}, ${order.shippingAddress.city} - ${order.shippingAddress.pinCode}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Payment: ${order.paymentDetails} • ₹${order.total.toInt()}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onTrackClick,
            colors = ButtonDefaults.buttonColors(containerColor = RosePrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("success_track_order_btn")
        ) {
            Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Track Tailoring Progress", fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onContinueShopping,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Continue Shopping", fontWeight = FontWeight.SemiBold)
        }
    }
}
