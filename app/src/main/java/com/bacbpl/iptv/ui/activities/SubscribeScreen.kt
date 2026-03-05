package com.bacbpl.iptv.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bacbpl.iptv.R

class SubscribeScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SubscribeUI()
        }
    }
}

@Composable
fun SubscribeUI() {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050505))
            .padding(16.dp)
    ) {

        item { TopSection() }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item { BannerRow() }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        item { SubscribeSection() }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        item { ChoosePlanSection() }

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
        R.drawable.banner3
    )

    val listState = rememberLazyListState()

    // Auto Scroll
    LaunchedEffect(Unit) {

        while (true) {

            kotlinx.coroutines.delay(3000)

            val nextIndex = (listState.firstVisibleItemIndex + 1) % banners.size

            listState.animateScrollToItem(nextIndex)
        }
    }

    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4B2A2A))
            .padding(12.dp)
    ) {

        items(banners) { image ->

            BannerImage(image)

        }

    }
}

@Composable
fun BannerImage(image: Int) {

    Image(
        painter = painterResource(id = image),
        contentDescription = "",
        modifier = Modifier
            .width(300.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(10.dp))
            .focusable()
            .clickable { },
        contentScale = ContentScale.Crop
    )
}

@Composable
fun SubscribeSection() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4B2A2A))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "SUBSCRIBE TO WATCH",
            color = Color.White,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {},
            modifier = Modifier.focusable(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            )
        ) {

            Text(
                text = "SUBSCRIBE NOW",
                color = Color.Black
            )
        }
    }
}

@Composable
fun ChoosePlanSection() {

    Column {

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

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(listOf("99", "199", "299")) { price ->

                PlanCard(price)

            }

        }
    }
}

@Composable
fun PlanCard(price: String) {

    Card(
        modifier = Modifier
            .width(250.dp)
            .focusable()
            .clickable { },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEDEDED)
        )
    ) {

        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "24 OTTs",
                fontSize = 16.sp
            )

            Text(
                text = "for ₹$price Monthly",
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.z5),
                    contentDescription = "",
                    modifier = Modifier.size(36.dp)
                )

                Image(
                    painter = painterResource(R.drawable.sonyliv),
                    contentDescription = "",
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Image(
                painter = painterResource(R.drawable.ott_grid),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .focusable(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD60000)
                )
            ) {

                Text(
                    text = "Subscribe Now for ₹$price",
                    color = Color.White
                )
            }
        }
    }
}