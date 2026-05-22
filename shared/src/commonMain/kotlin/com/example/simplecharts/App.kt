package com.example.simplecharts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.netguru.multiplatform.charts.ChartAnimation
import com.netguru.multiplatform.charts.bar.BarChartCategory
import com.netguru.multiplatform.charts.bar.BarChartData
import com.netguru.multiplatform.charts.bar.BarChartEntry
import com.netguru.multiplatform.charts.bar.BarChartWithLegend
import com.netguru.multiplatform.charts.line.LineChartData
import com.netguru.multiplatform.charts.line.LineChartPoint
import com.netguru.multiplatform.charts.line.LineChartSeries
import com.netguru.multiplatform.charts.line.LineChartWithLegend
import com.netguru.multiplatform.charts.pie.LegendIcon
import com.netguru.multiplatform.charts.pie.LegendOrientation
import com.netguru.multiplatform.charts.pie.PieChartConfig
import com.netguru.multiplatform.charts.pie.PieChartData
import com.netguru.multiplatform.charts.pie.PieChartWithLegend
import com.netguru.multiplatform.charts.theme.ChartColors
import com.netguru.multiplatform.charts.theme.LocalChartColors

private const val HOUR_MS = 3600000L

@Composable
fun App() {

    val darkBackground = Color(0xFF0B0F19)
    val cardBackground = Color(0xFF161D30)
    val borderStrokeColor = Color(0xFF223150)
    
    val neonCyan = Color(0xFF00E5FF)
    val neonMagenta = Color(0xFFD500F9)
    val neonEmerald = Color(0xFF00E676)
    val neonAmber = Color(0xFFFFB300)
    
    // Create the shared chart colors configuration to inject via CompositionLocal
    val customChartColors = remember {
        ChartColors(
            primary = neonCyan,
            surface = cardBackground,
            grid = borderStrokeColor.copy(alpha = 0.4f),
            emptyGasBottle = Color(0xFFEF4444),
            fullGasBottle = neonEmerald,
            overlayLine = neonCyan
        )
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Performance", "Device Allocation")

    // Theme wrapper
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = neonCyan,
            background = darkBackground,
            surface = cardBackground,
            onSurface = Color.White,
            onBackground = Color(0xFF94A3B8)
        )
    ) {
        CompositionLocalProvider(LocalChartColors provides customChartColors) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(darkBackground, Color(0xFF060913))
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeContentPadding()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header Area
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "SimpleCharts KMP",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "High-performance interactive charting on Android & iOS",
                                fontSize = 12.sp,
                                color = Color(0xFF94A3B8),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        
                        // Connection Status Bulb
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(borderStrokeColor.copy(alpha = 0.5f))
                                .border(1.dp, borderStrokeColor, RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(neonEmerald)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Live Metrics",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Capsule Segmented Control Tab Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(cardBackground)
                            .border(1.dp, borderStrokeColor, RoundedCornerShape(16.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        tabs.forEachIndexed { index, title ->
                            val isSelected = selectedTab == index
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) neonCyan.copy(alpha = 0.12f) else Color.Transparent)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) neonCyan.copy(alpha = 0.4f) else Color.Transparent,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { selectedTab = index }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = title,
                                    color = if (isSelected) neonCyan else Color(0xFF94A3B8),
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Main Active Chart Content
                    when (selectedTab) {
                        0 -> OverviewDashboard(
                            borderStrokeColor = borderStrokeColor,
                            neonCyan = neonCyan,
                            neonMagenta = neonMagenta,
                            neonEmerald = neonEmerald
                        )
                        1 -> PerformanceDashboard(
                            borderStrokeColor = borderStrokeColor,
                            neonCyan = neonCyan,
                            neonMagenta = neonMagenta,
                            neonEmerald = neonEmerald
                        )
                        2 -> DeviceAllocationDashboard(
                            borderStrokeColor = borderStrokeColor,
                            neonCyan = neonCyan,
                            neonMagenta = neonMagenta,
                            neonEmerald = neonEmerald,
                            neonAmber = neonAmber
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun OverviewDashboard(
    borderStrokeColor: Color,
    neonCyan: Color,
    neonMagenta: Color,
    neonEmerald: Color
) {
    // Construct Bar Chart Data representing sales, expenses, and profit per quarter
    val barChartData = remember {
        BarChartData(
            categories = listOf(
                BarChartCategory(
                    name = "Q1",
                    entries = listOf(
                        BarChartEntry(x = "Sales", y = 420f, color = neonCyan),
                        BarChartEntry(x = "Expenses", y = 280f, color = neonMagenta),
                        BarChartEntry(x = "Net Profit", y = 140f, color = neonEmerald)
                    )
                ),
                BarChartCategory(
                    name = "Q2",
                    entries = listOf(
                        BarChartEntry(x = "Sales", y = 510f, color = neonCyan),
                        BarChartEntry(x = "Expenses", y = 310f, color = neonMagenta),
                        BarChartEntry(x = "Net Profit", y = 200f, color = neonEmerald)
                    )
                ),
                BarChartCategory(
                    name = "Q3",
                    entries = listOf(
                        BarChartEntry(x = "Sales", y = 680f, color = neonCyan),
                        BarChartEntry(x = "Expenses", y = 390f, color = neonMagenta),
                        BarChartEntry(x = "Net Profit", y = 290f, color = neonEmerald)
                    )
                ),
                BarChartCategory(
                    name = "Q4",
                    entries = listOf(
                        BarChartEntry(x = "Sales", y = 850f, color = neonCyan),
                        BarChartEntry(x = "Expenses", y = 450f, color = neonMagenta),
                        BarChartEntry(x = "Net Profit", y = 400f, color = neonEmerald)
                    )
                )
            )
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        // Chart Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF161D30).copy(alpha = 0.6f))
                .border(1.dp, borderStrokeColor, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Financial Growth Overview",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Consolidated quarterly report in thousand USD (Interactive)",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    BarChartWithLegend(
                        data = barChartData,
                        animation = ChartAnimation.Simple(),
                        xAxisLabel = { value ->
                            Text(
                                text = value.toString(),
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8),
                                modifier = Modifier.padding(top = 4.dp),
                                textAlign = TextAlign.Center
                            )
                        },
                        yAxisLabel = { value ->
                            Text(
                                text = "$value",
                                fontSize = 10.sp,
                                color = Color(0xFF64748B),
                                textAlign = TextAlign.End,
                                modifier = Modifier.padding(end = 6.dp)
                            )
                        },
                        legendItemLabel = { labelName ->
                            Text(
                                text = labelName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                modifier = Modifier.padding(start = 4.dp, end = 12.dp)
                            )
                        }
                    )
                }
            }
        }

        // Summary Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Total Annual Revenue",
                value = "$2.46M",
                change = "+24.5%",
                accentColor = neonCyan,
                borderStrokeColor = borderStrokeColor
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Annual Net Profit",
                value = "$1.03M",
                change = "+38.1%",
                accentColor = neonEmerald,
                borderStrokeColor = borderStrokeColor
            )
        }
    }
}

@Composable
fun PerformanceDashboard(
    borderStrokeColor: Color,
    neonCyan: Color,
    neonMagenta: Color,
    neonEmerald: Color
) {
    // Generate beautiful sequential Line points representing metrics across hours
    val lineChartData = remember {
        val cpuPoints = listOf(
            LineChartPoint(x = 0L, y = 14f),
            LineChartPoint(x = 1L * HOUR_MS, y = 32f),
            LineChartPoint(x = 2L * HOUR_MS, y = 22f),
            LineChartPoint(x = 3L * HOUR_MS, y = 45f),
            LineChartPoint(x = 4L * HOUR_MS, y = 29f),
            LineChartPoint(x = 5L * HOUR_MS, y = 58f),
            LineChartPoint(x = 6L * HOUR_MS, y = 35f)
        )
        val memoryPoints = listOf(
            LineChartPoint(x = 0L, y = 45f),
            LineChartPoint(x = 1L * HOUR_MS, y = 48f),
            LineChartPoint(x = 2L * HOUR_MS, y = 52f),
            LineChartPoint(x = 3L * HOUR_MS, y = 50f),
            LineChartPoint(x = 4L * HOUR_MS, y = 61f),
            LineChartPoint(x = 5L * HOUR_MS, y = 65f),
            LineChartPoint(x = 6L * HOUR_MS, y = 63f)
        )
        val networkPoints = listOf(
            LineChartPoint(x = 0L, y = 8f),
            LineChartPoint(x = 1L * HOUR_MS, y = 15f),
            LineChartPoint(x = 2L * HOUR_MS, y = 40f),
            LineChartPoint(x = 3L * HOUR_MS, y = 12f),
            LineChartPoint(x = 4L * HOUR_MS, y = 85f),
            LineChartPoint(x = 5L * HOUR_MS, y = 42f),
            LineChartPoint(x = 6L * HOUR_MS, y = 98f)
        )

        LineChartData(
            series = listOf(
                LineChartSeries(
                    dataName = "CPU Core Temp (°C)",
                    lineColor = neonCyan,
                    listOfPoints = cpuPoints
                ),
                LineChartSeries(
                    dataName = "RAM Usage (%)",
                    lineColor = neonMagenta,
                    listOfPoints = memoryPoints
                ),
                LineChartSeries(
                    dataName = "Net IO (MB/s)",
                    lineColor = neonEmerald,
                    listOfPoints = networkPoints
                )
            )
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF161D30).copy(alpha = 0.6f))
                .border(1.dp, borderStrokeColor, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Real-time Diagnostics",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Simulated resource usage timeline. Slide or touch to view stats.",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    LineChartWithLegend(
                        lineChartData = lineChartData,
                        animation = ChartAnimation.Simple(),
                        xAxisLabel = { value ->
                            val timestamp = value as? Long ?: 0L
                            val hours = (timestamp / HOUR_MS).toInt()
                            Text(
                                text = "${10 + hours}:00", // Displays hours cleanly starting at 10 AM
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        },
                        yAxisLabel = { value ->
                            Text(
                                text = value.toString(),
                                fontSize = 10.sp,
                                color = Color(0xFF64748B),
                                textAlign = TextAlign.End,
                                modifier = Modifier.padding(end = 6.dp)
                            )
                        },
                        overlayHeaderLabel = { value ->
                            val timestamp = value as? Long ?: 0L
                            val hours = (timestamp / HOUR_MS).toInt()
                            Text(
                                text = "Time: ${10 + hours}:00",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = neonCyan,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        },
                        overlayDataEntryLabel = { labelName, valObj ->
                            Text(
                                text = "$labelName: $valObj",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        },
                        legendItemLabel = { labelName ->
                            Text(
                                text = labelName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                modifier = Modifier.padding(start = 4.dp, end = 12.dp)
                            )
                        }
                    )
                }
            }
        }

        // Stats grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MiniMetricCard(
                title = "CPU Core Max",
                value = "58°C",
                accentColor = neonCyan,
                borderStrokeColor = borderStrokeColor,
                modifier = Modifier.weight(1f)
            )
            MiniMetricCard(
                title = "RAM Peak",
                value = "65%",
                accentColor = neonMagenta,
                borderStrokeColor = borderStrokeColor,
                modifier = Modifier.weight(1f)
            )
            MiniMetricCard(
                title = "Net Spike",
                value = "98MB/s",
                accentColor = neonEmerald,
                borderStrokeColor = borderStrokeColor,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DeviceAllocationDashboard(
    borderStrokeColor: Color,
    neonCyan: Color,
    neonMagenta: Color,
    neonEmerald: Color,
    neonAmber: Color
) {
    // Construct Pie Chart Data
    val pieChartData = remember {
        listOf(
            PieChartData(name = "Apple iOS App", value = 35.0, color = neonMagenta),
            PieChartData(name = "Google Android App", value = 40.0, color = neonCyan),
            PieChartData(name = "Web Dashboard", value = 20.0, color = neonEmerald),
            PieChartData(name = "Desktop Client", value = 5.0, color = neonAmber)
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF161D30).copy(alpha = 0.6f))
                .border(1.dp, borderStrokeColor, RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Global Session Allocation",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                Text(
                    text = "Active sessions split across platforms (Interactive Donut)",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    textAlign = TextAlign.Start
                )

                Box(
                    modifier = Modifier
                        .size(310.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    PieChartWithLegend(
                        pieChartData = pieChartData,
                        animation = ChartAnimation.Simple(),
                        config = PieChartConfig(
                            thickness = 32.dp,
                            gap = 3.dp,
                            legendOrientation = LegendOrientation.HORIZONTAL,
                            legendIcon = LegendIcon.CIRCLE,
                            legendPadding = 20.dp,
                            numberOfColsInLegend = 2
                        ),
                        legendItemLabel = { data ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                            ) {
                                Text(
                                    text = "${data.name} (${data.value.toInt()}%)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                    )
                }
            }
        }

        // Summary details card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF161D30))
                .border(1.dp, borderStrokeColor, RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Key Takeaways",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            HorizontalDivider(color = borderStrokeColor)
            Text(
                text = "• Mobile platforms account for 75% of total active sessions.",
                fontSize = 12.sp,
                color = Color(0xFF94A3B8)
            )
            Text(
                text = "• Web Dashboard traffic shows a solid 15% month-on-month improvement.",
                fontSize = 12.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    change: String,
    accentColor: Color,
    borderStrokeColor: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF161D30))
            .border(1.dp, borderStrokeColor, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF94A3B8)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = change,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
    }
}

@Composable
fun MiniMetricCard(
    title: String,
    value: String,
    accentColor: Color,
    borderStrokeColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF161D30))
            .border(1.dp, borderStrokeColor, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF94A3B8)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )
    }
}