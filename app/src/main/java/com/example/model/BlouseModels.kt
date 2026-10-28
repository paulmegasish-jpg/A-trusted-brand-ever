package com.example.model

import com.example.R

enum class BlouseCategory(val displayName: String, val iconDescription: String) {
    ALL("All Blouses", "Full collection"),
    SILK_ZARI("Silk & Zari", "Royal wedding & festive wear"),
    ORGANZA_PUFF("Organza & Puffs", "Trendy sheer and modern chic"),
    VELVET_PARTY("Velvet Luxury", "Winter & reception glamour"),
    COTTON_DAILY("Cotton & Handblock", "Breathable daily elegance"),
    BRIDAL_COUTURE("Bridal Couture", "Heavy Maggam & Sabyasachi cuts")
}

data class BlouseColor(
    val name: String,
    val hexColor: Long
)

data class BlouseItem(
    val id: String,
    val name: String,
    val category: BlouseCategory,
    val price: Double,
    val originalPrice: Double,
    val rating: Float,
    val reviewsCount: Int,
    val imageRes: Int,
    val description: String,
    val fabric: String,
    val neckStyle: String,
    val sleeveStyle: String,
    val backDesign: String,
    val padding: String,
    val margin: String = "2-inch extra inner margin for easy alteration",
    val colors: List<BlouseColor>,
    val sizes: List<String> = listOf("32 (XS)", "34 (S)", "36 (M)", "38 (L)", "40 (XL)", "42 (XXL)", "44 (3XL)"),
    val tags: List<String> = listOf("Bestseller", "Handcrafted"),
    val isPadded: Boolean = true,
    val inStock: Boolean = true
) {
    val discountPercent: Int
        get() = if (originalPrice > price) (((originalPrice - price) / originalPrice) * 100).toInt() else 0
}

data class CustomMeasurements(
    val bustInches: Float = 36f,
    val underBustInches: Float = 30f,
    val waistInches: Float = 28f,
    val frontNeckDepthInches: Float = 6.5f,
    val backNeckDepthInches: Float = 9.0f,
    val blouseLengthInches: Float = 14.5f,
    val sleeveLengthInches: Float = 10.5f,
    val notes: String = ""
)

data class CartItem(
    val cartItemId: String,
    val blouse: BlouseItem,
    val selectedSize: String,
    val selectedColor: String,
    val quantity: Int,
    val customMeasurements: CustomMeasurements? = null,
    val isPadded: Boolean = true
) {
    val totalPrice: Double
        get() = blouse.price * quantity
}

enum class PaymentType(val title: String, val subtitle: String) {
    UPI("UPI / Instant QR", "Google Pay, PhonePe, Paytm, Any UPI ID"),
    CARD("Credit / Debit Card", "Visa, MasterCard, RuPay, Maestro"),
    NET_BANKING("Net Banking", "All major Indian and International banks"),
    WALLET("Digital Wallets", "Paytm Wallet, Amazon Pay, PhonePe"),
    CASH_ON_DELIVERY("Cash On Delivery (COD)", "Pay with cash or QR at doorstep")
}

data class ShippingAddress(
    val fullName: String = "Ananya Sharma",
    val phone: String = "+91 98765 43210",
    val street: String = "Flat 402, Lotus Palace, MG Road",
    val city: String = "Bengaluru",
    val state: String = "Karnataka",
    val pinCode: String = "560001",
    val landmark: String = "Near Trinity Metro Station"
)

enum class OrderStatus(val label: String) {
    PLACED("Order Placed"),
    TAILORING("Master Tailor Stitching"),
    QUALITY_CHECK("Quality & Finishing Check"),
    DISPATCHED("Dispatched via Express"),
    DELIVERED("Delivered")
}

data class TrackingStep(
    val status: OrderStatus,
    val title: String,
    val description: String,
    val dateText: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)

data class Order(
    val orderId: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val discount: Double,
    val couponCode: String?,
    val shippingFee: Double,
    val total: Double,
    val shippingAddress: ShippingAddress,
    val paymentType: PaymentType,
    val paymentDetails: String,
    val currentStatus: OrderStatus,
    val orderDate: String,
    val estimatedDeliveryDate: String,
    val trackingSteps: List<TrackingStep>
)

object SampleBlouseData {
    val blouseList = listOf(
        BlouseItem(
            id = "lb-001",
            name = "Royal Zari Embroidered Silk Blouse",
            category = BlouseCategory.SILK_ZARI,
            price = 2499.0,
            originalPrice = 3999.0,
            rating = 4.9f,
            reviewsCount = 348,
            imageRes = R.drawable.img_blouse_silk_zari,
            description = "Exquisite handcrafted pure Kanjeevaram silk blouse adorned with fine golden zari maggam needlework. Features a regal boat neckline with delicate gold piping and a deep circular back cutout accented with handmade pearl latkans.",
            fabric = "Pure Kanjeevaram Silk with Cotton Lining",
            neckStyle = "Royal Boat Neck",
            sleeveStyle = "Elbow Length (11 inches) with Zari Border",
            backDesign = "Deep Round Cutout with Pearl Latkan Dori",
            padding = "Pre-padded with premium breathable shape cups",
            colors = listOf(
                BlouseColor("Rani Magenta", 0xFFC2185B),
                BlouseColor("Royal Navy", 0xFF1A237E),
                BlouseColor("Emerald Green", 0xFF004D40),
                BlouseColor("Crimson Red", 0xFFB71C1C)
            ),
            tags = listOf("Bestseller", "Handcrafted", "Bridal Favorite")
        ),
        BlouseItem(
            id = "lb-002",
            name = "Lavender Sheer Organza Puffed Blouse",
            category = BlouseCategory.ORGANZA_PUFF,
            price = 1899.0,
            originalPrice = 2899.0,
            rating = 4.8f,
            reviewsCount = 212,
            imageRes = R.drawable.img_blouse_organza_puff,
            description = "Dreamy pastel fairytale design featuring translucent organza puff sleeves with gathered cuffs and pearl droplet fringes. Tailored with a flattering sweetheart neckline that pairs effortlessly with sarees and lehengas.",
            fabric = "Glass Organza & Shimmer Crepe Lining",
            neckStyle = "Sweetheart Neckline",
            sleeveStyle = "Voluminous Gathered Puff Sleeve",
            backDesign = "Illusion Net with Functional Fabric Potli Buttons",
            padding = "Lightly padded for clean bust contour",
            colors = listOf(
                BlouseColor("Lilac Lavender", 0xFFBA68C8),
                BlouseColor("Blush Rose", 0xFFF06292),
                BlouseColor("Sage Mint", 0xFF81C784),
                BlouseColor("Ivory Pearl", 0xFFFFF9C4)
            ),
            tags = listOf("Trending", "Cocktail Wear", "Lightweight")
        ),
        BlouseItem(
            id = "lb-003",
            name = "Regal Emerald Velvet Boat-Neck Blouse",
            category = BlouseCategory.VELVET_PARTY,
            price = 2199.0,
            originalPrice = 3499.0,
            rating = 4.9f,
            reviewsCount = 184,
            imageRes = R.drawable.img_blouse_velvet_boat,
            description = "Sumptuous Micro-Velvet 9000 blouse featuring opulent antique zardozi work along the neckline and hem. Designed with deep back opening and royal golden hanging tassels for winter weddings and evening parties.",
            fabric = "Micro Velvet 9000 with Butter Cotton Lining",
            neckStyle = "Designer Boat Neck with Gold Piping",
            sleeveStyle = "Sleek Elbow Length Sleeves",
            backDesign = "Deep V-Back with Heavy Golden Latkans",
            padding = "Padded with removable cups",
            colors = listOf(
                BlouseColor("Emerald Forest", 0xFF004D40),
                BlouseColor("Midnight Black", 0xFF212121),
                BlouseColor("Maroon Wine", 0xFF4A148C),
                BlouseColor("Royal Peacock Blue", 0xFF0D47A1)
            ),
            tags = listOf("Party Wear", "Rich Velvet", "Winter Pick")
        ),
        BlouseItem(
            id = "lb-004",
            name = "Bridal Heritage Maggam Cutout Blouse",
            category = BlouseCategory.BRIDAL_COUTURE,
            price = 3899.0,
            originalPrice = 5999.0,
            rating = 5.0f,
            reviewsCount = 420,
            imageRes = R.drawable.img_blouse_silk_zari,
            description = "Masterpiece wedding blouse embellished with intricate kardana beads, mirror work, and 3D floral bullion embroidery. Crafted specifically for wedding pattu sarees with custom armhole fit.",
            fabric = "Raw Silk with Micro-Stitched Interlining",
            neckStyle = "Temple Jewellery Curved Neck",
            sleeveStyle = "Maggam Border Elbow Length",
            backDesign = "Grand Oval Cutout with Double Dori Bells",
            padding = "Firm Structured Padded Bustier Support",
            colors = listOf(
                BlouseColor("Bridal Scarlet", 0xFFD32F2F),
                BlouseColor("Golden Mustard", 0xFFFFA000),
                BlouseColor("Wine Plum", 0xFF880E4F)
            ),
            tags = listOf("Bridal Exclusive", "Heavy Maggam", "Pure Silk")
        ),
        BlouseItem(
            id = "lb-005",
            name = "Handblock Indigo Kalamkari Cotton Blouse",
            category = BlouseCategory.COTTON_DAILY,
            price = 1199.0,
            originalPrice = 1799.0,
            rating = 4.7f,
            reviewsCount = 165,
            imageRes = R.drawable.img_blouse_organza_puff,
            description = "Authentic natural vegetable-dyed Kalamkari handblock printed pure cotton blouse. Soft, ultra-breathable, and tailored with classic princess cut seam lines for everyday chic comfort.",
            fabric = "100% Breathable Organic Cotton",
            neckStyle = "Round Neck with Fabric Piping",
            sleeveStyle = "Comfort Cap Sleeves (6 inches)",
            backDesign = "Classic Keyhole with Wooden Button",
            padding = "Non-padded with soft double lining",
            colors = listOf(
                BlouseColor("Indigo Navy", 0xFF1A237E),
                BlouseColor("Earthy Terracotta", 0xFFBF360C),
                BlouseColor("Mustard Turmeric", 0xFFF57F17)
            ),
            tags = listOf("Organic Cotton", "Daily Wear", "Breathable")
        ),
        BlouseItem(
            id = "lb-006",
            name = "Shimmer Sequin Sweetheart Party Blouse",
            category = BlouseCategory.VELVET_PARTY,
            price = 1999.0,
            originalPrice = 2999.0,
            rating = 4.8f,
            reviewsCount = 275,
            imageRes = R.drawable.img_blouse_velvet_boat,
            description = "Dazzling champagne gold sequin embellished blouse reflecting soft glitter under ambient lighting. Designed for cocktail sarees, pre-pleated drapes, and modern net lehengas.",
            fabric = "All-over Micro Sequin Georgette",
            neckStyle = "Plunging Sweetheart",
            sleeveStyle = "Sleeveless with Soft Edge Binding",
            backDesign = "Criss-Cross Ribbon Tie-Back",
            padding = "Double Padded with Wire-Free Cups",
            colors = listOf(
                BlouseColor("Champagne Gold", 0xFFD4AF37),
                BlouseColor("Metallic Silver", 0xFF9E9E9E),
                BlouseColor("Rose Gold", 0xFFB388FF)
            ),
            tags = listOf("Cocktail Special", "Glamour", "Sleeveless")
        )
    )

    fun createInitialOrders(): List<Order> {
        val item1 = CartItem(
            cartItemId = "cart-init-1",
            blouse = blouseList[0],
            selectedSize = "36 (M)",
            selectedColor = "Rani Magenta",
            quantity = 1,
            customMeasurements = CustomMeasurements(
                bustInches = 36f,
                underBustInches = 31f,
                waistInches = 29f,
                frontNeckDepthInches = 6.5f,
                backNeckDepthInches = 9.0f,
                blouseLengthInches = 14.5f
            ),
            isPadded = true
        )

        return listOf(
            Order(
                orderId = "LL-BL-84920",
                items = listOf(item1),
                subtotal = 2499.0,
                discount = 499.8,
                couponCode = "LADYFIRST",
                shippingFee = 0.0,
                total = 1999.2,
                shippingAddress = ShippingAddress(),
                paymentType = PaymentType.UPI,
                paymentDetails = "Google Pay (Verified - ananya@okaxis)",
                currentStatus = OrderStatus.TAILORING,
                orderDate = "22 Oct 2026, 04:30 PM",
                estimatedDeliveryDate = "27 Oct 2026",
                trackingSteps = listOf(
                    TrackingStep(
                        status = OrderStatus.PLACED,
                        title = "Order Confirmed",
                        description = "Payment received. Design specs & custom measurements verified.",
                        dateText = "22 Oct, 04:30 PM",
                        isCompleted = true,
                        isCurrent = false
                    ),
                    TrackingStep(
                        status = OrderStatus.TAILORING,
                        title = "In Master Tailoring",
                        description = "Silk fabric hand-cut, neckline shaped, and zari borders aligned.",
                        dateText = "23 Oct, 11:15 AM",
                        isCompleted = false,
                        isCurrent = true
                    ),
                    TrackingStep(
                        status = OrderStatus.QUALITY_CHECK,
                        title = "Finishing & Latkan Attachment",
                        description = "Cup padding alignment, 2-inch margin stitching, and iron steaming.",
                        dateText = "Expected 24 Oct",
                        isCompleted = false,
                        isCurrent = false
                    ),
                    TrackingStep(
                        status = OrderStatus.DISPATCHED,
                        title = "Courier Handover",
                        description = "Dispatched in luxury rigid box with fragrance paper.",
                        dateText = "Expected 25 Oct",
                        isCompleted = false,
                        isCurrent = false
                    ),
                    TrackingStep(
                        status = OrderStatus.DELIVERED,
                        title = "Doorstep Delivery",
                        description = "Delivered to recipient with contactless delivery.",
                        dateText = "Expected 27 Oct",
                        isCompleted = false,
                        isCurrent = false
                    )
                )
            )
        )
    }
}
