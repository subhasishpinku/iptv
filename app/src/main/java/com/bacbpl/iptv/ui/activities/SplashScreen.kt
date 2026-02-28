    //package com.bacbpl.iptv.ui.activities
    //
    //import android.animation.AnimatorSet
    //import android.animation.ObjectAnimator
    //import android.animation.ValueAnimator
    //import android.annotation.SuppressLint
    //import android.content.Intent
    //import android.graphics.drawable.AnimationDrawable
    //import android.os.Bundle
    //import android.os.Handler
    //import android.os.Looper
    //import android.view.animation.AccelerateDecelerateInterpolator
    //import android.view.animation.AlphaAnimation
    //import android.view.animation.AnimationSet
    //import android.view.animation.BounceInterpolator
    //import android.view.animation.DecelerateInterpolator
    //import android.view.animation.RotateAnimation
    //import android.view.animation.ScaleAnimation
    //import android.widget.ImageView
    //import android.widget.TextView
    //import androidx.constraintlayout.widget.ConstraintLayout
    //import androidx.fragment.app.FragmentActivity
    //import com.bacbpl.iptv.R
    //
    //class SplashActivity : FragmentActivity() {
    //
    //    private lateinit var logo: ImageView
    //    private lateinit var bottomLogo: ImageView
    //    private lateinit var rootLayout: ConstraintLayout
    //    private val splashTime: Long = 4000 // 4 seconds
    //
    //    @SuppressLint("MissingInflatedId")
    //    override fun onCreate(savedInstanceState: Bundle?) {
    //        super.onCreate(savedInstanceState)
    //        setContentView(R.layout.activity_splash)
    //
    //        logo = findViewById(R.id.logo)
    //        bottomLogo = findViewById(R.id.bottomLogo)
    //        rootLayout = findViewById(R.id.rootLayout)
    //
    //        // Start OTT-style animations
    //        animateBackground()
    //        animateMainLogo()
    //        animateBottomLogo()
    //
    //        // Navigate after animations
    //        Handler(Looper.getMainLooper()).postDelayed({
    //
    //            // Smooth exit animation
    //            val fadeOut = AlphaAnimation(1f, 0f)
    //            fadeOut.duration = 400
    //            logo.startAnimation(fadeOut)
    //            bottomLogo.startAnimation(fadeOut)
    //            rootLayout.startAnimation(fadeOut)
    //
    //            Handler(Looper.getMainLooper()).postDelayed({
    //                val intent = Intent(this, SignInActivity::class.java)
    //                startActivity(intent)
    //                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    //                finish()
    //            }, 400)
    //
    //        }, splashTime)
    //    }
    //
    //    private fun animateMainLogo() {
    //        // Initial state
    //        logo.alpha = 0f
    //        logo.scaleX = 0.2f
    //        logo.scaleY = 0.2f
    //
    //        // Complex animation sequence
    //        val fadeIn = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f).setDuration(800)
    //
    //        val scaleUp = ObjectAnimator.ofFloat(logo, "scaleX", 0.2f, 1.2f).setDuration(1000)
    //        val scaleUpY = ObjectAnimator.ofFloat(logo, "scaleY", 0.2f, 1.2f).setDuration(1000)
    //
    //        val scaleNormal = ObjectAnimator.ofFloat(logo, "scaleX", 1.2f, 1.0f).setDuration(400)
    //        val scaleNormalY = ObjectAnimator.ofFloat(logo, "scaleY", 1.2f, 1.0f).setDuration(400)
    //
    //        scaleUp.interpolator = AccelerateDecelerateInterpolator()
    //        scaleUpY.interpolator = AccelerateDecelerateInterpolator()
    //
    //        // Play first part
    //        val firstSet = AnimatorSet()
    //        firstSet.playTogether(fadeIn, scaleUp, scaleUpY)
    //        firstSet.start()
    //
    //        // Play second part after delay
    //        Handler(Looper.getMainLooper()).postDelayed({
    //            scaleNormal.start()
    //            scaleNormalY.start()
    //        }, 1000)
    //
    //        // Add floating animation
    //        Handler(Looper.getMainLooper()).postDelayed({
    //            val floatAnim = ObjectAnimator.ofFloat(logo, "translationY", 0f, -20f, 0f).apply {
    //                duration = 2000
    //                repeatCount = ValueAnimator.INFINITE
    //                repeatMode = ValueAnimator.REVERSE
    //                interpolator = AccelerateDecelerateInterpolator()
    //            }
    //            floatAnim.start()
    //        }, 1800)
    //    }
    //
    //    private fun animateBottomLogo() {
    //        // Slide and fade animation
    //        bottomLogo.alpha = 0f
    //        bottomLogo.scaleX = 0.8f
    //        bottomLogo.scaleY = 0.8f
    //
    //        bottomLogo.animate()
    //            .alpha(1f)
    //            .scaleX(1f)
    //            .scaleY(1f)
    //            .translationY(0f)
    //            .setDuration(1200)
    //            .setStartDelay(600)
    //            .setInterpolator(DecelerateInterpolator())
    //            .start()
    //    }
    //
    //    private fun animateBackground() {
    //        // Subtle background animation
    //        val bgAnimator = ValueAnimator.ofFloat(0.8f, 1.0f, 0.8f).apply {
    //            duration = 3000
    //            repeatCount = ValueAnimator.INFINITE
    //            addUpdateListener { animation ->
    //                val value = animation.animatedValue as Float
    //                rootLayout.alpha = value
    //            }
    //        }
    //        bgAnimator.start()
    //    }
    //}
    package com.bacbpl.iptv.ui.activities

    import androidx.compose.animation.core.*
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.alpha
    import androidx.compose.ui.draw.scale
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.unit.dp
    import androidx.constraintlayout.compose.ConstraintLayout
    import com.bacbpl.iptv.R
    import kotlinx.coroutines.delay
    import coil.compose.AsyncImage
    import kotlinx.coroutines.launch

    @Composable
    fun SplashScreen(
        onSplashFinished: () -> Unit
    ) {
        val context = LocalContext.current
        var startExit by remember { mutableStateOf(false) }
        var splashFinished by remember { mutableStateOf(false) }

        // Animations
        val mainLogoAlpha = remember { Animatable(0f) }
        val mainLogoScale = remember { Animatable(0.2f) }
        val bottomLogoAlpha = remember { Animatable(0f) }
        val bottomLogoScale = remember { Animatable(0.8f) }
        val backgroundAlpha = remember { Animatable(0.8f) }

        LaunchedEffect(Unit) {
            // Main logo animation sequence
            launch {
                mainLogoAlpha.animateTo(
                    1f,
                    animationSpec = tween(800, easing = FastOutSlowInEasing)
                )
            }

            launch {
                mainLogoScale.animateTo(
                    1.2f,
                    animationSpec = tween(1000, easing = FastOutSlowInEasing)
                )
                mainLogoScale.animateTo(
                    1.0f,
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
            }

            // Bottom logo animation with delay
            launch {
                delay(600)
                bottomLogoAlpha.animateTo(
                    1f,
                    animationSpec = tween(1200, easing = LinearOutSlowInEasing)
                )
                bottomLogoScale.animateTo(
                    1f,
                    animationSpec = tween(1200, easing = LinearOutSlowInEasing)
                )
            }

            // Background pulse animation
            launch {
                while (!startExit) {
                    backgroundAlpha.animateTo(
                        1f,
                        animationSpec = tween(3000, easing = FastOutSlowInEasing)
                    )
                    backgroundAlpha.animateTo(
                        0.8f,
                        animationSpec = tween(3000, easing = FastOutSlowInEasing)
                    )
                }
            }

            // Floating animation for main logo (fixed this part)
            // Floating animation for main logo - Simpler version
            launch {
                delay(1800)
                while (!startExit) {
                    // Animate to 0.5 and back to 1.0 repeatedly
                    mainLogoAlpha.animateTo(
                        0.5f,
                        animationSpec = tween(2000, easing = FastOutSlowInEasing)
                    )
                    mainLogoAlpha.animateTo(
                        1f,
                        animationSpec = tween(2000, easing = FastOutSlowInEasing)
                    )
                }
            }

            // Delay for splash duration
            delay(4000)
            startExit = true

            // Exit animation
            mainLogoAlpha.animateTo(
                0f,
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            )
            bottomLogoAlpha.animateTo(
                0f,
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            )
            backgroundAlpha.animateTo(
                0f,
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            )

            splashFinished = true
            onSplashFinished()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .alpha(backgroundAlpha.value)
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                val (logo, bottomLogo) = createRefs()

                // Main Logo
                AsyncImage(
                    model = R.drawable.logo1,
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .alpha(mainLogoAlpha.value)
                        .scale(mainLogoScale.value)
                )

                // Bottom Logo
                AsyncImage(
                    model = R.drawable.logo2,
                    contentDescription = "Powered By",
                    modifier = Modifier
                        .constrainAs(bottomLogo) {
                            bottom.linkTo(parent.bottom, margin = 40.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .alpha(bottomLogoAlpha.value)
                        .scale(bottomLogoScale.value)
                )
            }
        }
    }