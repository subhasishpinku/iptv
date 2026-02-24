//package com.bacbpl.iptv.ui.activities
//import android.content.Intent
//import android.os.Bundle
//import android.os.CountDownTimer
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.view.KeyEvent
//import android.view.View
//import android.widget.EditText
//import android.widget.ImageButton
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.bacbpl.iptv.JetStreamActivity
//import com.bacbpl.iptv.R
//import com.google.android.material.button.MaterialButton
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import com.google.firebase.Firebase
//import com.google.firebase.FirebaseException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
//import com.google.firebase.auth.PhoneAuthCredential
//import com.google.firebase.auth.PhoneAuthOptions
//import com.google.firebase.auth.PhoneAuthProvider
//import com.google.firebase.auth.auth
//
//import java.util.concurrent.TimeUnit
//
//class OTPActivity : AppCompatActivity() {
//
//    private lateinit var otp1: EditText
//    private lateinit var otp2: EditText
//    private lateinit var otp3: EditText
//    private lateinit var otp4: EditText
//    private lateinit var otp5: EditText
//    private lateinit var otp6: EditText
//    private lateinit var btnSubmit: MaterialButton
//    private lateinit var tvTimer: TextView
//    private lateinit var tvResend: TextView
//    private lateinit var tvSubtitle: TextView
//    private lateinit var progressBar: View
//    private lateinit var btnBack: ImageButton
//    private lateinit var tvManualInstructions: TextView
//
//    private val otpEditTexts = arrayListOf<EditText>()
//    private var timer: CountDownTimer? = null
//
//    // Firebase Authentication
//    private lateinit var auth: FirebaseAuth
//    private var verificationId: String? = null
//    private var resendingToken: PhoneAuthProvider.ForceResendingToken? = null
//    private lateinit var phoneNumber: String
//    private var isVerificationInProgress = false
//
//    companion object {
//        private const val TAG = "OTPActivity"
//        private const val ERROR_PLAY_INTEGRITY = -14
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_otp)
//
//        // Initialize Firebase Auth
//        auth = Firebase.auth
//
//        // Get phone number from intent
//        phoneNumber = intent.getStringExtra("phone_number") ?: ""
//
//        if (phoneNumber.isEmpty()) {
//            Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
//
//        initViews()
//        setupOTPInputs()
//
//        // Try to send verification code
//        sendVerificationCode()
//    }
//
//    private fun initViews() {
//        otp1 = findViewById(R.id.otp1)
//        otp2 = findViewById(R.id.otp2)
//        otp3 = findViewById(R.id.otp3)
//        otp4 = findViewById(R.id.otp4)
//        otp5 = findViewById(R.id.otp5)
//        otp6 = findViewById(R.id.otp6)
//        btnSubmit = findViewById(R.id.btnSubmit)
//        tvTimer = findViewById(R.id.tvTimer)
//        tvResend = findViewById(R.id.tvResend)
//        tvSubtitle = findViewById(R.id.tvSubtitle)
//        progressBar = findViewById(R.id.progressBar)
//        btnBack = findViewById(R.id.btnBack)
//        tvManualInstructions = findViewById(R.id.tvManualInstructions)
//
//        otpEditTexts.addAll(listOf(otp1, otp2, otp3, otp4, otp5, otp6))
//
//        // Format phone number for display
//        val formattedPhone = formatPhoneNumber(phoneNumber)
//        tvSubtitle.text = "Enter the OTP sent to $formattedPhone"
//
//        btnSubmit.setOnClickListener {
//            val otp = getEnteredOTP()
//            verifyOTP(otp)
//        }
//
//        tvResend.setOnClickListener {
//            resendOTP()
//        }
//
//        btnBack.setOnClickListener {
//            finish()
//        }
//
//        // Initially disable submit button
//        btnSubmit.isEnabled = false
//        btnSubmit.alpha = 0.5f
//    }
//
//    private fun setupOTPInputs() {
//        for (i in otpEditTexts.indices) {
//            otpEditTexts[i].addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//                override fun afterTextChanged(s: Editable?) {
//                    if (s?.length == 1) {
//                        if (i < otpEditTexts.size - 1) {
//                            otpEditTexts[i + 1].requestFocus()
//                        } else {
//                            btnSubmit.requestFocus()
//                        }
//                    }
//                    checkOTPCompletion()
//                }
//            })
//
//            otpEditTexts[i].setOnKeyListener { _, keyCode, event ->
//                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
//                    if (otpEditTexts[i].text.isEmpty() && i > 0) {
//                        otpEditTexts[i - 1].requestFocus()
//                        otpEditTexts[i - 1].text.clear()
//                    }
//                }
//                false
//            }
//
//            otpEditTexts[i].setOnFocusChangeListener { _, hasFocus ->
//                if (hasFocus) {
//                    otpEditTexts[i].text.clear()
//                }
//            }
//        }
//    }
//
//    private fun checkOTPCompletion() {
//        val isComplete = otpEditTexts.all { it.text.length == 1 }
//        btnSubmit.isEnabled = isComplete
//        btnSubmit.alpha = if (isComplete) 1.0f else 0.5f
//    }
//
//    private fun getEnteredOTP(): String {
//        return otpEditTexts.joinToString("") { it.text.toString() }
//    }
//
//    private fun formatPhoneNumber(phone: String): String {
//        return if (phone.length >= 10) {
//            val lastFour = phone.takeLast(4)
//            val masked = phone.dropLast(4).replace(Regex("[0-9]"), "*")
//            masked + lastFour
//        } else {
//            phone
//        }
//    }
//
//    private fun sendVerificationCode() {
//        if (isVerificationInProgress) return
//
//        isVerificationInProgress = true
//        showLoading(true)
//        tvManualInstructions.visibility = View.GONE
//
//        try {
//            val options = PhoneAuthOptions.newBuilder(auth)
//                .setPhoneNumber(phoneNumber)
//                .setTimeout(60L, TimeUnit.SECONDS)
//                .setActivity(this)
//                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                        Log.d(TAG, "onVerificationCompleted")
//                        isVerificationInProgress = false
//                        showLoading(false)
//                        handleAutoVerification(credential)
//                    }
//
//                    override fun onVerificationFailed(e: FirebaseException) {
//                        Log.e(TAG, "onVerificationFailed", e)
//                        isVerificationInProgress = false
//                        showLoading(false)
//                        handleVerificationFailure(e)
//                    }
//
//                    override fun onCodeSent(
//                        verificationId: String,
//                        token: PhoneAuthProvider.ForceResendingToken
//                    ) {
//                        Log.d(TAG, "onCodeSent")
//                        isVerificationInProgress = false
//                        showLoading(false)
//                        this@OTPActivity.verificationId = verificationId
//                        this@OTPActivity.resendingToken = token
//
//                        Toast.makeText(
//                            this@OTPActivity,
//                            "OTP sent successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        startTimer()
//                    }
//                })
//                .build()
//
//            PhoneAuthProvider.verifyPhoneNumber(options)
//        } catch (e: Exception) {
//            Log.e(TAG, "Error sending verification code", e)
//            isVerificationInProgress = false
//            showLoading(false)
//            handleVerificationFailure(e)
//        }
//    }
//
//    private fun handleVerificationFailure(e: Exception) {
//        val errorMessage = e.message ?: "Unknown error"
//        Log.e(TAG, "Verification failed: $errorMessage")
//
//        when {
//            // Check for Play Integrity errors
//            errorMessage.contains("Play Integrity", ignoreCase = true) ||
//                    errorMessage.contains("INTEGRITY", ignoreCase = true) ||
//                    errorMessage.contains("CANNOT_BIND_TO_SERVICE", ignoreCase = true) ||
//                    errorMessage.contains("-14", ignoreCase = true) -> {
//                showPlayStoreUpdateDialog()
//            }
//
//            // Check for reCAPTCHA fallback
//            errorMessage.contains("RECAPTCHA", ignoreCase = true) ||
//                    errorMessage.contains("reCAPTCHA", ignoreCase = true) -> {
//                showManualVerificationDialog(
//                    "Google's verification service encountered an issue. " +
//                            "Please wait for SMS OTP and enter it manually."
//                )
//            }
//
//            // Network errors
//            errorMessage.contains("network", ignoreCase = true) ||
//                    errorMessage.contains("timeout", ignoreCase = true) -> {
//                showRetryDialog("Network error. Please check your connection and try again.")
//            }
//
//            // Default case
//            else -> {
//                showManualVerificationDialog(
//                    "Unable to automatically verify your number. " +
//                            "Please wait for SMS and enter the OTP manually.\n\n" +
//                            "Error: ${e.message}"
//                )
//            }
//        }
//    }
//
//    private fun showPlayStoreUpdateDialog() {
//        MaterialAlertDialogBuilder(this)
//            .setTitle("Play Store Update Required")
//            .setMessage("Your Google Play Store app needs to be updated for automatic verification.\n\n" +
//                    "You can still receive OTP via SMS and enter it manually.")
//            .setPositiveButton("Update Play Store") { _, _ ->
//                openPlayStoreForUpdate()
//            }
//            .setNeutralButton("Manual Entry") { _, _ ->
//                enableManualMode()
//            }
//            .setNegativeButton("Try Again") { _, _ ->
//                sendVerificationCode()
//            }
//            .setCancelable(false)
//            .show()
//    }
//
//    private fun showRetryDialog(message: String) {
//        MaterialAlertDialogBuilder(this)
//            .setTitle("Verification Failed")
//            .setMessage(message)
//            .setPositiveButton("Try Again") { _, _ ->
//                sendVerificationCode()
//            }
//            .setNegativeButton("Manual Entry") { _, _ ->
//                enableManualMode()
//            }
//            .setCancelable(false)
//            .show()
//    }
//
//    private fun openPlayStoreForUpdate() {
//        try {
//            // Try to open Play Store app
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = android.net.Uri.parse("market://details?id=com.android.vending")
//            intent.setPackage("com.android.vending")
//            startActivity(intent)
//        } catch (e: Exception) {
//            // Fallback to browser
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = android.net.Uri.parse("https://play.google.com/store/apps/details?id=com.android.vending")
//            startActivity(intent)
//        }
//
//        // Show manual mode option after opening Play Store
//        Toast.makeText(
//            this,
//            "After updating, return to the app and try again",
//            Toast.LENGTH_LONG
//        ).show()
//    }
//
//    private fun showManualVerificationDialog(message: String) {
//        MaterialAlertDialogBuilder(this)
//            .setTitle("Manual Verification")
//            .setMessage(message)
//            .setPositiveButton("Continue Manually") { _, _ ->
//                enableManualMode()
//            }
//            .setNegativeButton("Try Again") { _, _ ->
//                sendVerificationCode()
//            }
//            .setCancelable(false)
//            .show()
//    }
//
//    private fun enableManualMode() {
//        tvManualInstructions.visibility = View.VISIBLE
//        tvResend.visibility = View.VISIBLE
//        tvTimer.visibility = View.GONE
//
//        // Clear any existing OTP
//        otpEditTexts.forEach { it.text.clear() }
//        otp1.requestFocus()
//
//        // Show instructions
//        Toast.makeText(
//            this,
//            "SMS with OTP has been sent. Please enter it manually.",
//            Toast.LENGTH_LONG
//        ).show()
//
//        // Start timer for manual mode
//        startTimer()
//    }
//
//    private fun handleAutoVerification(credential: PhoneAuthCredential) {
//        val code = credential.smsCode
//        if (code != null) {
//            fillOTPFields(code)
//            verifyOTP(code)
//        } else {
//            signInWithPhoneAuthCredential(credential)
//        }
//    }
//
//    private fun resendOTP() {
//        if (isVerificationInProgress) return
//
//        if (resendingToken != null) {
//            isVerificationInProgress = true
//            showLoading(true)
//
//            val options = PhoneAuthOptions.newBuilder(auth)
//                .setPhoneNumber(phoneNumber)
//                .setTimeout(60L, TimeUnit.SECONDS)
//                .setActivity(this)
//                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                        isVerificationInProgress = false
//                        showLoading(false)
//                        val code = credential.smsCode
//                        if (code != null) {
//                            fillOTPFields(code)
//                            verifyOTP(code)
//                        }
//                    }
//
//                    override fun onVerificationFailed(e: FirebaseException) {
//                        isVerificationInProgress = false
//                        showLoading(false)
//                        Log.e(TAG, "Resend failed", e)
//                        Toast.makeText(
//                            this@OTPActivity,
//                            "Resend failed. Please try again.",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//
//                    override fun onCodeSent(
//                        verificationId: String,
//                        token: PhoneAuthProvider.ForceResendingToken
//                    ) {
//                        isVerificationInProgress = false
//                        showLoading(false)
//                        this@OTPActivity.verificationId = verificationId
//                        this@OTPActivity.resendingToken = token
//
//                        Toast.makeText(
//                            this@OTPActivity,
//                            "OTP resent successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        otpEditTexts.forEach { it.text.clear() }
//                        btnSubmit.isEnabled = false
//                        btnSubmit.alpha = 0.5f
//                        otp1.requestFocus()
//
//                        startTimer()
//                    }
//                })
//                .setForceResendingToken(resendingToken!!)
//                .build()
//
//            PhoneAuthProvider.verifyPhoneNumber(options)
//        } else {
//            sendVerificationCode()
//        }
//    }
//
//    private fun verifyOTP(otp: String) {
//        if (verificationId == null) {
//            Toast.makeText(this, "Please request OTP first", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        showLoading(true)
//
//        try {
//            val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
//            signInWithPhoneAuthCredential(credential)
//        } catch (e: Exception) {
//            showLoading(false)
//            Log.e(TAG, "OTP verification error", e)
//            Toast.makeText(
//                this,
//                "Invalid OTP format",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//
//    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                showLoading(false)
//
//                if (task.isSuccessful) {
//                    Log.d(TAG, "signInWithCredential:success")
//                    Toast.makeText(
//                        this,
//                        "Authentication successful",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                    val intent = Intent(this, JetStreamActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
//                    finish()
//                } else {
//                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//
//                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                        Toast.makeText(this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show()
//                        otpEditTexts.forEach { it.text.clear() }
//                        otp1.requestFocus()
//                        btnSubmit.isEnabled = false
//                        btnSubmit.alpha = 0.5f
//                    } else {
//                        Toast.makeText(
//                            this,
//                            "Authentication failed: ${task.exception?.message}",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//                }
//            }
//    }
//
//    private fun fillOTPFields(code: String) {
//        for (i in code.indices) {
//            if (i < otpEditTexts.size) {
//                otpEditTexts[i].setText(code[i].toString())
//            }
//        }
//    }
//
//    private fun startTimer() {
//        tvTimer.visibility = View.VISIBLE
//        tvResend.visibility = View.GONE
//
//        timer?.cancel()
//        timer = object : CountDownTimer(30000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                val seconds = millisUntilFinished / 1000
//                tvTimer.text = String.format("00:%02d", seconds)
//            }
//
//            override fun onFinish() {
//                tvTimer.visibility = View.GONE
//                tvResend.visibility = View.VISIBLE
//            }
//        }.start()
//    }
//
//    private fun showLoading(show: Boolean) {
//        progressBar.visibility = if (show) View.VISIBLE else View.GONE
//        btnSubmit.isEnabled = if (show) false else btnSubmit.isEnabled
//        btnBack.isEnabled = !show
//
//        // Disable OTP fields while loading
//        otpEditTexts.forEach { it.isEnabled = !show }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        timer?.cancel()
//    }
//}
//
//package com.bacbpl.iptv.ui.activities
//import android.os.Bundle
//import android.os.CountDownTimer
//import android.text.Editable
//import android.text.TextWatcher
//import android.view.KeyEvent
//import android.view.View
//import android.widget.EditText
//import android.widget.ImageButton
//import android.widget.TextView  // Add this import
//import androidx.appcompat.app.AppCompatActivity
//import com.bacbpl.iptv.R
//import com.google.android.material.button.MaterialButton
//
//class OTPActivity : AppCompatActivity() {
//
//    private lateinit var otp1: EditText
//    private lateinit var otp2: EditText
//    private lateinit var otp3: EditText
//    private lateinit var otp4: EditText
//    private lateinit var otp5: EditText
//    private lateinit var otp6: EditText
//    private lateinit var btnSubmit: MaterialButton
//    private lateinit var tvTimer: TextView  // Changed from MaterialTextView
//    private lateinit var tvResend: TextView // Changed from MaterialTextView
//    private lateinit var tvSubtitle: TextView // Add this
//
//    private val otpEditTexts = arrayListOf<EditText>()
//    private var timer: CountDownTimer? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_otp)
//
//        initViews()
//        setupOTPInputs()
//        startTimer()
//
//        // Get phone number from intent
//        val phoneNumber = intent.getStringExtra("phone_number") ?: "+91 ******7890"
//        tvSubtitle.text = "Enter the OTP sent to $phoneNumber"
//    }
//
//    private fun initViews() {
//        otp1 = findViewById(R.id.otp1)
//        otp2 = findViewById(R.id.otp2)
//        otp3 = findViewById(R.id.otp3)
//        otp4 = findViewById(R.id.otp4)
//        otp5 = findViewById(R.id.otp5)
//        otp6 = findViewById(R.id.otp6)
//        btnSubmit = findViewById(R.id.btnSubmit)
//        tvTimer = findViewById(R.id.tvTimer)
//        tvResend = findViewById(R.id.tvResend)
//        tvSubtitle = findViewById(R.id.tvSubtitle)  // Initialize this
//
//        otpEditTexts.addAll(listOf(otp1, otp2, otp3, otp4, otp5, otp6))
//
//        btnSubmit.setOnClickListener {
//            val otp = getEnteredOTP()
//            verifyOTP(otp)
//        }
//
//        tvResend.setOnClickListener {
//            resendOTP()
//        }
//
//        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
//            finish()
//        }
//    }
//
//    private fun setupOTPInputs() {
//        for (i in otpEditTexts.indices) {
//            otpEditTexts[i].addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//                override fun afterTextChanged(s: Editable?) {
//                    if (s?.length == 1) {
//                        // Move to next EditText
//                        if (i < otpEditTexts.size - 1) {
//                            otpEditTexts[i + 1].requestFocus()
//                        } else {
//                            // Last OTP field - hide keyboard or submit
//                            btnSubmit.requestFocus()
//                        }
//                    }
//                    checkOTPCompletion()
//                }
//            })
//
//            otpEditTexts[i].setOnKeyListener { _, keyCode, event ->
//                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
//                    if (otpEditTexts[i].text.isEmpty() && i > 0) {
//                        // Move to previous field and clear it
//                        otpEditTexts[i - 1].requestFocus()
//                        otpEditTexts[i - 1].text.clear()
//                    }
//                }
//                false
//            }
//
//            // TV remote DPAD focus handling
//            otpEditTexts[i].setOnFocusChangeListener { _, hasFocus ->
//                if (hasFocus) {
//                    otpEditTexts[i].text.clear() // Clear field when focused for TV
//                }
//            }
//        }
//    }
//
//    private fun checkOTPCompletion() {
//        val isComplete = otpEditTexts.all { it.text.length == 1 }
//        btnSubmit.isEnabled = isComplete
//        btnSubmit.alpha = if (isComplete) 1.0f else 0.5f
//    }
//
//    private fun getEnteredOTP(): String {
//        return otpEditTexts.joinToString("") { it.text.toString() }
//    }
//
//    private fun startTimer() {
//        tvTimer.visibility = View.VISIBLE
//        tvResend.visibility = View.GONE
//
//        timer = object : CountDownTimer(30000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                val seconds = millisUntilFinished / 1000
//                tvTimer.text = String.format("00:%02d", seconds)
//            }
//
//            override fun onFinish() {
//                tvTimer.visibility = View.GONE
//                tvResend.visibility = View.VISIBLE
//            }
//        }.start()
//    }
//
//    private fun resendOTP() {
//        // Clear all OTP fields
//        otpEditTexts.forEach { it.text.clear() }
//        btnSubmit.isEnabled = false
//        btnSubmit.alpha = 0.5f
//        otp1.requestFocus()
//
//        // Implement OTP resend logic here
//        startTimer()
//
//        // Show message (optional)
//        // Toast.makeText(this, "OTP resent successfully", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun verifyOTP(otp: String) {
//        // Implement OTP verification logic here
//        // Example: API call to verify OTP
//
//        // On success:
//        // val intent = Intent(this, NextActivity::class.java)
//        // startActivity(intent)
//        // finish()
//
//        // On failure:
//        // Show error and clear fields
//        // otpEditTexts.forEach { it.text.clear() }
//        // otp1.requestFocus()
//        // Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        timer?.cancel()
//    }
//}
package com.bacbpl.iptv.ui.activities.otpscreen

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.bacbpl.iptv.R
import android.widget.Toast
import com.bacbpl.iptv.ui.activities.otpscreen.viewmodel.OtpUiState
import com.bacbpl.iptv.ui.activities.otpscreen.viewmodel.OtpViewModel
import com.bacbpl.iptv.ui.activities.otpscreen.viewmodel.OtpViewModelFactory
import com.bacbpl.iptv.ui.activities.otpscreen.viewmodel.VerifyOtpUiState
import com.bacbpl.iptv.utils.UserSession

@Composable
fun OTPActivity(
    phoneNumber: String,
    onBack: () -> Unit,
    onSubmit: (String) -> Unit,
    onResend: () -> Unit
) {
    val viewModel: OtpViewModel = viewModel(
        factory = OtpViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    val otpLength = 4
    val otpValues = remember { mutableStateListOf("", "", "", "") }
    val focusRequesters = List(otpLength) { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val sendOtpState by viewModel.sendOtpState.collectAsState()
    val verifyOtpState by viewModel.verifyOtpState.collectAsState()
    val timerState by viewModel.timerState.collectAsState()

    var isSubmitting by remember { mutableStateOf(false) }

    // Auto-send OTP when screen loads
    LaunchedEffect(Unit) {
        viewModel.sendOtp(phoneNumber)
    }

    // Handle send OTP state
    LaunchedEffect(sendOtpState) {
        when (sendOtpState) {
            is OtpUiState.Success -> {
                val response = (sendOtpState as OtpUiState.Success).response
                Toast.makeText(
                    context,
                    "OTP: ${response.otp} (For testing)",
                    Toast.LENGTH_LONG
                ).show()
            }
            is OtpUiState.Error -> {
                Toast.makeText(
                    context,
                    (sendOtpState as OtpUiState.Error).message,
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {}
        }
    }

    // Handle verify OTP state
    LaunchedEffect(verifyOtpState) {
        when (verifyOtpState) {
            is VerifyOtpUiState.Success -> {
                isSubmitting = false
                val response = (verifyOtpState as VerifyOtpUiState.Success).response
                Toast.makeText(
                    context,
                    response.message,
                    Toast.LENGTH_LONG
                ).show()


                if (response.status) {
                    // Update UserSession after successful login
                    UserSession.updateSession(context)

                    Toast.makeText(
                        context,
                        "Login Successful! Welcome ${response.user?.name ?: "User"}",
                        Toast.LENGTH_LONG
                    ).show()

                    // Navigate to main screen
                    onSubmit(otpValues.joinToString(""))
                }
            }
            is VerifyOtpUiState.Error -> {
                isSubmitting = false
                Toast.makeText(
                    context,
                    (verifyOtpState as VerifyOtpUiState.Error).message,
                    Toast.LENGTH_LONG
                ).show()

                // Clear OTP fields on error
                otpValues.forEachIndexed { index, _ ->
                    otpValues[index] = ""
                }
                focusRequesters[0].requestFocus()
            }
            is VerifyOtpUiState.Loading -> {
                isSubmitting = true
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        AsyncImage(
            model = R.drawable.bg_movies,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Dark Overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.7f))
        )

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .width(500.dp)
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A1A)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    // Back Button
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title
                    Text(
                        text = "OTP Verification",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Subtitle
                    Text(
                        text = "Enter the 4-digit OTP sent to",
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = phoneNumber,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE50914)
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // Loading indicator for sending OTP
                    if (sendOtpState is OtpUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.CenterHorizontally),
                            color = Color(0xFFE50914)
                        )
                    } else {
                        // OTP Input Fields - 4 boxes
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            for (i in 0 until otpLength) {
                                OtpTextField(
                                    value = otpValues[i],
                                    onValueChange = { newValue ->
                                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                            otpValues[i] = newValue
                                            if (newValue.isNotEmpty() && i < otpLength - 1) {
                                                focusRequesters[i + 1].requestFocus()
                                            } else if (newValue.isNotEmpty() && i == otpLength - 1) {
                                                // Auto-submit when last digit is entered
                                                val otp = (otpValues.toList().subList(0, otpLength - 1) + newValue).joinToString("")
                                                if (otp.length == otpLength) {
                                                    viewModel.verifyOtp(phoneNumber, otp)
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .size(80.dp)
                                        .focusRequester(focusRequesters[i])
                                        .padding(end = if (i < otpLength - 1) 16.dp else 0.dp),
                                    enabled = !isSubmitting
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Timer
                        if (timerState.isRunning) {
                            Text(
                                text = String.format("00:%02d", timerState.seconds),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }

                        // Progress Bar for verification
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(48.dp)
                                    .align(Alignment.CenterHorizontally),
                                color = Color(0xFFE50914)
                            )
                        }

                        // Resend Button
                        if (!timerState.isRunning) {
                            TextButton(
                                onClick = {
                                    viewModel.sendOtp(phoneNumber)
                                    viewModel.resetTimer()
                                    otpValues.forEachIndexed { index, _ ->
                                        otpValues[index] = ""
                                    }
                                    focusRequesters[0].requestFocus()
                                    onResend()
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                enabled = !isSubmitting
                            ) {
                                Text(
                                    text = "Resend OTP",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE50914)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Submit Button
                        Button(
                            onClick = {
                                val otp = otpValues.joinToString("")
                                if (otp.length == otpLength) {
                                    viewModel.verifyOtp(phoneNumber, otp)
                                }
                            },
                            enabled = otpValues.all { it.isNotEmpty() } &&
                                    !isSubmitting &&
                                    timerState.isRunning,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE50914),
                                disabledContainerColor = Color(0xFFE50914).copy(alpha = 0.5f)
                            )
                        ) {
                            Text(
                                text = "Verify OTP",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OtpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 36.sp,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.Gray,
            disabledBorderColor = Color.DarkGray
        ),
        singleLine = true,
        maxLines = 1,
        enabled = enabled
    )
}