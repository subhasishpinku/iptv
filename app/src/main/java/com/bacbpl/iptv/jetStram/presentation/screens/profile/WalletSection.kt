package com.bacbpl.iptv.jetStram.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bacbpl.iptv.R
@Composable
fun WalletSection(
    isSubtitlesChecked: Boolean,
    onSubtitleCheckChange: (isChecked: Boolean) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.bg_image), // Replace with your background image
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // or ContentScale.FillBounds, ContentScale.Fit etc.
        )

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF101010).copy(alpha = 0.7f)) // Semi-transparent overlay
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Rest of your content remains the same...
            Text(
                text = "Connect Mobile with TV",
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Mobile QR Scan Section
                Card(
                    modifier = Modifier
                        .width(180.dp)
                        .height(320.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent), // কলামের ব্যাকগ্রাউন্ড ট্রান্সপারেন্ট
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ph_image),
                            contentDescription = "Mobile QR",
                            modifier = Modifier
                                .width(250.dp)
                                .height(550.dp),
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Scan QR",
                            fontSize = 16.sp,
                            color = Color.White // টেক্সট সাদা রাখতে চাইলে
                        )
                    }
                }

                // TV QR Section
                Card(
                    modifier = Modifier
                        .width(500.dp)
                        .height(320.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .background(Color.Transparent),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.tv_image),
//                            contentDescription = "TV QR",
//                            modifier = Modifier
//                                .width(450.dp)
//                                .height(250.dp),
//                            contentScale = ContentScale.Crop // ইমেজ ক্রপ করবে যদি সাইজ মেলে
//                        )
                        TvLoginScreen()
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Enable Subtitles",
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(10.dp))

                Switch(
                    checked = isSubtitlesChecked,
                    onCheckedChange = onSubtitleCheckChange
                )
            }
        }
    }
}
//
//@Composable
//fun TvLoginScreen() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(Color(0xFF2B0505), Color(0xFF000000))
//                )
//            )
//            .padding(40.dp)
//    ) {
//        // Netflix Logo Placeholder in top right
//        Text(
//            text = "BACBPL",
//            color = Color.Red,
//            fontSize = 24.sp,
//            fontWeight = FontWeight.Black,
//            modifier = Modifier.align(Alignment.TopEnd)
//
//        )
//
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Choose how to sign in",
//                color = Color.White,
//                fontSize = 22.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            // Tab Selector (Use Phone / Use Remote)
//            Row(
//                modifier = Modifier
//                    .background(Color(0xFF333333).copy(alpha = 0.5f), RoundedCornerShape(20.dp))
//                    .padding(4.dp)
//            ) {
//                Box(
//                    modifier = Modifier
//                        .background(Color.White, RoundedCornerShape(20.dp))
//                        .padding(horizontal = 16.dp, vertical = 6.dp)
//                ) {
//                    Text("Use Phone", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
//                }
//                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
//                    Text("Use Remote", color = Color.Gray, fontSize = 14.sp)
//                }
//            }
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.Top
//            ) {
//                // STEP 1: QR SECTION
//                Row(modifier = Modifier.weight(1f)) {
//                    StepNumber(number = "1")
//                    Column(modifier = Modifier.padding(start = 12.dp)) {
//                        Text(
//                            text = "Point the camera on your phone or\ntablet at this code, or go to\nnetflix.com/tv9",
//                            color = Color.White,
//                            fontSize = 10.sp,
//                            lineHeight = 20.sp
//                        )
//                        Spacer(modifier = Modifier.height(10.dp))
//                        Image(
//                            painter = painterResource(id = R.drawable.qr_code), // Ensure you have ph_image/qr_code here
//                            contentDescription = "QR",
//                            modifier = Modifier
//                                .size(100.dp)
////                                .background(Color.White,
////                                    RoundedCornerShape(4.dp))
////                                .padding(8.dp)
//                        )
//                    }
//                }
//
//                // STEP 2: CODE SECTION
//                Row(modifier = Modifier.weight(1f)) {
//                    StepNumber(number = "2")
//                    Column(modifier = Modifier.padding(start = 12.dp)) {
//                        Text(
//                            text = "Confirm this code on your phone or\ntablet",
//                            color = Color.White,
//                            fontSize = 10.sp,
//                            lineHeight = 20.sp
//                        )
//                        Spacer(modifier = Modifier.height(10.dp))
//                        Text(
//                            text = "4321 - 5678",
//                            color = Color.White,
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold,
//                            letterSpacing = 2.sp
//                        )
//                    }
//                }
//            }
//        }
//
//        // Bottom Footer
//        Column(
//            modifier = Modifier.align(Alignment.BottomEnd),
//            horizontalAlignment = Alignment.End
//        ) {
//            Text("Need help signing in?", color = Color.Gray, fontSize = 12.sp)
//            Text("Visit help.netflix.com", color = Color.Gray, fontSize = 12.sp)
//        }
//    }
//}
//
//@Composable
//fun StepNumber(number: String) {
//    Box(
//        modifier = Modifier
//            .size(28.dp)
//            .background(Color.White.copy(alpha = 0.2f), CircleShape)
//            .border(1.dp, Color.Gray.copy(alpha = 0.5f), CircleShape),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(text = number, color = Color.Gray, fontSize = 14.sp)
//    }
//}
@Composable
fun TvLoginScreen() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF3A0000),
                        Color(0xFF1A0000),
                        Color.Black
                    )
                )
            )
            .padding(horizontal = 80.dp, vertical = 60.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // TITLE
            Text(
                text = "Choose how to sign in",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // TOGGLE
            Row(
                modifier = Modifier
                    .background(
                        Color(0xFF3A2A2A),
                        RoundedCornerShape(50)
                    )
                    .padding(4.dp)
            ) {

                Box(
                    modifier = Modifier
                        .background(
                            Color.White,
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 26.dp, vertical = 10.dp)
                ) {
                    Text(
                        "Use Phone",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(horizontal = 26.dp, vertical = 10.dp)
                ) {
                    Text(
                        "Use Remote",
                        color = Color.LightGray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(70.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(120.dp)
            ) {

                // STEP 1
                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        StepCircle("1")

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "Point the camera on your phone or tablet\n" +
                                    "at this code, or go to bacbpl.com/tv",
                            color = Color.LightGray,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .background(
                                Color.White,
                                RoundedCornerShape(10.dp)
                            )
                            .padding(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.qr_code),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // STEP 2
                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        StepCircle("2")

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = "Confirm this code on your phone\nor tablet",
                            color = Color.LightGray,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = "4321-5678",
                        color = Color.White,
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp
                    )
                }
            }
        }

        // FOOTER
        Column(
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {

            Text(
                text = "Need help signing in?",
                color = Color.Gray,
                fontSize = 12.sp
            )

            Text(
                text = "Visit help.bacbpl.com",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}
@Composable
fun StepCircle(number: String) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(
                color = Color(0xFF444444),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}