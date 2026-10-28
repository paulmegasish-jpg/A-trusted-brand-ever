package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.RosePrimary

@Composable
fun FittingGuideScreen(
    guideBust: Float,
    guideUnderbust: Float,
    recommendedSize: String,
    onMeasurementsChange: (bust: Float, underbust: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp)
    ) {
        // Header
        item {
            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                Text(
                    text = "Blouse Fitting & Style Guide",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = RosePrimary
                )
                Text(
                    text = "Calculate your perfect boutique size and explore designer neckline pairings.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Interactive Size Calculator Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(RosePrimary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Straighten,
                                contentDescription = "Calculator",
                                tint = RosePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Interactive Fit Calculator",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Slide to your measurements in inches",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bust Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Fullest Bust Measurement:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${guideBust.toInt()} inches",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = RosePrimary
                        )
                    }

                    Slider(
                        value = guideBust,
                        onValueChange = { onMeasurementsChange(it, guideUnderbust) },
                        valueRange = 30f..46f,
                        steps = 15,
                        colors = SliderDefaults.colors(
                            thumbColor = RosePrimary,
                            activeTrackColor = RosePrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Underbust Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Underbust (Ribcage):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${guideUnderbust.toInt()} inches",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChampagneGold
                        )
                    }

                    Slider(
                        value = guideUnderbust,
                        onValueChange = { onMeasurementsChange(guideBust, it) },
                        valueRange = 26f..42f,
                        steps = 15,
                        colors = SliderDefaults.colors(
                            thumbColor = ChampagneGold,
                            activeTrackColor = ChampagneGold
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Result Highlight Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = RosePrimary.copy(alpha = 0.08f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RosePrimary.copy(alpha = 0.3f)),
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
                                Text(
                                    text = "Recommended Lady Lebanan Size:",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = recommendedSize,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = RosePrimary
                                )
                                Text(
                                    text = "Tailored with +2\" alterable side margin",
                                    fontSize = 10.sp,
                                    color = ChampagneGold,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified Fit",
                                tint = RosePrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(18.dp)) }

        // Standard Size Chart
        item {
            Text(
                text = "Lady Lebanan Standard Size Chart",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    SizeRow("Size", "Bust", "Underbust", "Length", isHeader = true)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = DividerDefaults.color.copy(alpha = 0.4f))
                    SizeRow("32 (XS)", "32 inches", "27 inches", "13.5 inches")
                    SizeRow("34 (S)", "34 inches", "29 inches", "14.0 inches")
                    SizeRow("36 (M)", "36 inches", "31 inches", "14.5 inches")
                    SizeRow("38 (L)", "38 inches", "33 inches", "15.0 inches")
                    SizeRow("40 (XL)", "40 inches", "35 inches", "15.5 inches")
                    SizeRow("42 (XXL)", "42 inches", "37 inches", "16.0 inches")
                    SizeRow("44 (3XL)", "44 inches", "39 inches", "16.5 inches")
                }
            }
        }

        item { Spacer(modifier = Modifier.height(18.dp)) }

        // Neckline Style Guide
        item {
            Text(
                text = "Designer Neckline Guide",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))

            NeckGuideCard(
                title = "Boat Neckline",
                suitableFor = "Kanjeevaram & Banarasi Silk Sarees",
                tip = "Widened horizontal cut highlights clavicles and balances broad borders. Pairs magnificently with heavy chokers."
            )
            Spacer(modifier = Modifier.height(8.dp))

            NeckGuideCard(
                title = "Sweetheart Neckline",
                suitableFor = "Organza, Net & Georgette Drapes",
                tip = "Soft curved heart contour enhances feminine silhouette. Pairs effortlessly with drop earrings and minimalist necklaces."
            )
            Spacer(modifier = Modifier.height(8.dp))

            NeckGuideCard(
                title = "Deep V-Neck & Plunging",
                suitableFor = "Party Lehengas & Cocktail Sarees",
                tip = "Elongates torso and neck for an ultra-chic modern look. Handcrafted with non-slip stay tape."
            )
            Spacer(modifier = Modifier.height(8.dp))

            NeckGuideCard(
                title = "High Collar & Keyhole Back",
                suitableFor = "Handloom Cotton, Linen & Royal Heritage Silks",
                tip = "Regal, sophisticated look that draws emphasis to hairstyle and back latkan dori details."
            )
        }
    }
}

@Composable
fun SizeRow(col1: String, col2: String, col3: String, col4: String, isHeader: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val style = if (isHeader) {
            MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = RosePrimary)
        } else {
            MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)
        }

        Text(text = col1, style = style, modifier = Modifier.weight(1f))
        Text(text = col2, style = style, modifier = Modifier.weight(1f))
        Text(text = col3, style = style, modifier = Modifier.weight(1f))
        Text(text = col4, style = style, modifier = Modifier.weight(1f))
    }
}

@Composable
fun NeckGuideCard(title: String, suitableFor: String, tip: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(RosePrimary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Best For: $suitableFor",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = ChampagneGold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tip,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}
