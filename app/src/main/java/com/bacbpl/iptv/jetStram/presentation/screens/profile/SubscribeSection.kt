package com.bacbpl.iptv.jetStram.presentation.screens.profile

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bacbpl.iptv.R
import com.bacbpl.iptv.data.SharedPrefManager
import com.bacbpl.iptv.data.util.StringConstants
import com.bacbpl.iptv.ui.activities.HomeScreen
import com.bacbpl.iptv.ui.activities.subscribescreen.components.DynamicPlanCard
import com.bacbpl.iptv.ui.activities.subscribescreen.data.Plan
import com.bacbpl.iptv.ui.activities.subscribescreen.data.repositories.PlanRepository
import com.bacbpl.iptv.ui.activities.subscribescreen.viewmodels.PlanViewModel
import kotlinx.coroutines.delay

/**
 * Main SubscribeSection composable that matches the signature from profile package
 */
@Composable
fun SubscribeSection(
    isSubtitlesChecked: Boolean,
    onSubtitleCheckChange: (isChecked: Boolean) -> Unit
) {
    val planViewModel: PlanViewModel = viewModel()
    val monthlyPlans by planViewModel.monthlyPlans.observeAsState(emptyList())
    val quarterlyPlans by planViewModel.quarterlyPlans.observeAsState(emptyList())
    val halfYearlyPlans by planViewModel.halfYearlyPlans.observeAsState(emptyList())
    val isLoading by planViewModel.isLoading.observeAsState(false)
    val errorMessage by planViewModel.errorMessage.observeAsState()
    val subscribeResponse by planViewModel.subscribeResponse.observeAsState()
    val isSubscribing by planViewModel.isSubscribing.observeAsState(false)

    val context = LocalContext.current
    val sharedPrefManager = remember { SharedPrefManager(context) }

    // State for subtitle checkbox
    var localIsSubtitlesChecked by remember { mutableStateOf(isSubtitlesChecked) }

    // Handle subscription response
    LaunchedEffect(subscribeResponse) {
        subscribeResponse?.let { response ->
            val toastMessage = if (response.success || response.status == true) {
                "✅ Subscription successful!"
            } else {
                "❌ ${response.message}"
            }

            Toast.makeText(context, toastMessage + "  " + " " + response.message, Toast.LENGTH_LONG).show()

            // Clear response after showing
            planViewModel.clearSubscribeResponse()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050505))
            .padding(16.dp)
    ) {
        item { TopSection() }
        item { Spacer(modifier = Modifier.height(5.dp)) }
        item { BannerRow() }
        item { Spacer(modifier = Modifier.height(10.dp)) }

        // Dynamic Plan Section
        item {
            DynamicChoosePlanSection(
                monthlyPlans = monthlyPlans,
                quarterlyPlans = quarterlyPlans,
                halfYearlyPlans = halfYearlyPlans,
                isLoading = isLoading,
                isSubscribing = isSubscribing,
                errorMessage = errorMessage,
                onRetry = { planViewModel.loadAllPlans() },
                onPlanSelected = { plan ->
                    planViewModel.selectPlan(plan)

                    // Get mobile number from SharedPreferences
                    val mobile = sharedPrefManager.getUserMobile()
                    println("mobile_Print: $mobile")

                    // Remove country code (+91)
                    val cleanMobile = mobile?.replace("+91", "")?.trim()
                    println("cleanMobile_Print: $cleanMobile")

                    if (cleanMobile.isNullOrEmpty()) {
                        Toast.makeText(
                            context,
                            "User mobile number not found. Please login again.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@DynamicChoosePlanSection
                    }
                    // Show confirmation dialog or directly subscribe
                    planViewModel.subscribeToPlan(cleanMobile, plan.id)
                }
            )
        }
        item { Spacer(modifier = Modifier.height(10.dp)) }

        // Subtitle Section (renamed from recursive call)
        item {
            SubtitleSection(
                isSubtitlesChecked = localIsSubtitlesChecked,
                onSubtitleCheckChange = {
                    localIsSubtitlesChecked = it
                    onSubtitleCheckChange(it)
                }
            )
        }
        item { Spacer(modifier = Modifier.height(10.dp)) }
    }

    // Show error message if any
    errorMessage?.let { message ->
        LaunchedEffect(message) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            planViewModel.clearErrorMessage()
        }
    }

    // Show loading indicator for subscription
    if (isSubscribing) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Processing subscription...",
                    color = Color.White
                )
            }
        }
    }
}

/**
 * New Subtitle Section component to replace the recursive call
 */
@Composable
fun SubtitleSection(
    isSubtitlesChecked: Boolean,
    onSubtitleCheckChange: (isChecked: Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Subtitle Settings",
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSubtitleCheckChange(!isSubtitlesChecked) }
                .padding(vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (isSubtitlesChecked) Color.Red else Color.Gray,
                        RoundedCornerShape(4.dp)
                    )
                    .then(
                        if (isSubtitlesChecked) {
                            Modifier.border(2.dp, Color.White, RoundedCornerShape(4.dp))
                        } else {
                            Modifier
                        }
                    )
            ) {
                if (isSubtitlesChecked) {
                    Text(
                        text = "✓",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Enable Subtitles",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

// Rest of the code remains the same...
@Composable
fun DynamicChoosePlanSection(
    monthlyPlans: List<Plan>,
    quarterlyPlans: List<Plan>,
    halfYearlyPlans: List<Plan>,
    isLoading: Boolean,
    isSubscribing: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onPlanSelected: (Plan) -> Unit
) {
    var selectedPeriod by remember { mutableStateOf("monthly") }
    val selectedPlan by remember { mutableStateOf<Plan?>(null) }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Choose a Plan",
            color = Color.White,
            fontSize = 22.sp
        )

        Text(
            text = "TRENDING OFFER",
            color = Color(0xFF00FF66),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Period Selection Tabs
        DynamicPeriodTabs(
            selectedPeriod = selectedPeriod,
            onPeriodSelected = { selectedPeriod = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Loading indicator
        if (isLoading) {
            CircularProgressIndicator(color = Color.Red)
        }

        // Error message
        else if (!errorMessage.isNullOrEmpty()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp
                )
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Retry")
                }
            }
        }

        // Plans display
        else {
            // Get plans based on selected period
            val plansToShow = when (selectedPeriod) {
                "monthly" -> monthlyPlans
                "quarterly" -> quarterlyPlans
                "halfyearly" -> halfYearlyPlans
                else -> emptyList()
            }

            if (plansToShow.isEmpty()) {
                Text(
                    text = "No plans available",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            } else {
                // Sort plans by price (Premium first, then Gold, then Silver)
                val sortedPlans = plansToShow.sortedByDescending { plan ->
                    when (PlanRepository.getPlanTypeFromName(plan.sysPlanName)) {
                        PlanRepository.PLAN_TYPE_PREMIUM -> 1
                        PlanRepository.PLAN_TYPE_GOLD -> 2
                        PlanRepository.PLAN_TYPE_SILVER -> 3
                        else -> 0
                    }
                }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        items(sortedPlans.size) { index ->
                            DynamicPlanCard(
                                plan = sortedPlans[index],
                                onPlanSelected = { plan ->
                                    // Show plan info in Toast
                                    Toast.makeText(
                                        context,
                                        "Processing: ${plan.sysPlanName} - ₹${plan.sysPlanPrice}",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Call the subscription function
                                    onPlanSelected(plan)
                                },
                                isSelected = selectedPlan?.id == sortedPlans[index].id,
                                isSubscribing = isSubscribing
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DynamicPeriodTabs(
    selectedPeriod: String,
    onPeriodSelected: (String) -> Unit
) {
    val periods = listOf("Monthly", "Quarterly", "Half-Yearly")
    val periodKeys = listOf("monthly", "quarterly", "halfyearly")
    val interactionSources = remember { periods.map { MutableInteractionSource() } }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(Color(0xFF2A2A2A), RoundedCornerShape(50.dp))
            .padding(4.dp)
    ) {
        periods.forEachIndexed { index, period ->
            val isSelected = selectedPeriod == periodKeys[index]
            val interactionSource = interactionSources[index]
            val isFocused by interactionSource.collectIsFocusedAsState()

            Surface(
                modifier = Modifier
                    .focusable(interactionSource = interactionSource)
                    .then(
                        if (isFocused) {
                            Modifier.border(2.dp, Color.Red, RoundedCornerShape(50.dp))
                        } else {
                            Modifier
                        }
                    ),
                shape = RoundedCornerShape(50.dp),
                color = if (isSelected) Color(0xFFD60000) else Color.Transparent,
                onClick = { onPeriodSelected(periodKeys[index]) }
            ) {
                Text(
                    text = period,
                    color = if (isSelected) Color.White else Color.Gray,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun TopSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "My Plan",
            color = Color.White,
            fontSize = 28.sp
        )

        Text(
            text = "One Stop For All Your Favourite\nOTT Subscriptions",
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Composable
fun BannerRow() {
    val banners = listOf(
        R.drawable.plan_banner,
        R.drawable.banner2,
        R.drawable.banner3,
        R.drawable.plan_banner,
        R.drawable.banner2,
        R.drawable.banner4
    )

    val listState = rememberLazyListState()
    var currentPage by remember { mutableStateOf(0) }

    // Create an infinite list by repeating the banners
    val infiniteBanners = remember {
        List(100) { index -> banners[index % banners.size] }
    }

    // Auto scroll continuously
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)

            // Get current visible item
            val currentItem = listState.firstVisibleItemIndex

            // Calculate next item (just increment, never reset)
            val nextItem = currentItem + 1

            // Animate to next item
            listState.animateScrollToItem(nextItem)

            // Update current page based on actual banner (modulo)
            currentPage = nextItem % banners.size
        }
    }

    Column {
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4B2A2A))
                .padding(12.dp)
        ) {
            items(infiniteBanners.size) { index ->
                BannerImage(image = infiniteBanners[index])
            }
        }

        // Page Indicators (based on actual banner index)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(banners.size) { index ->
                Box(
                    modifier = Modifier
                        .size(if (currentPage == index) 10.dp else 6.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (currentPage == index)
                                Color.Red
                            else
                                Color.Gray.copy(alpha = 0.5f)
                        )
                        .padding(horizontal = 2.dp)
                )
            }
        }
    }
}

@Composable
fun BannerImage(image: Int) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Image(
        painter = painterResource(id = image),
        contentDescription = "",
        modifier = Modifier
            .width(300.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(10.dp))
            .focusable(interactionSource = interactionSource)
            .then(
                if (isFocused) {
                    Modifier.border(3.dp, Color.Red, RoundedCornerShape(10.dp))
                } else {
                    Modifier
                }
            )
            .clickable {
                // Handle banner click
            },
        contentScale = ContentScale.Crop
    )
}