package com.bacbpl.iptv.jetfit.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bacbpl.iptv.R
import com.bacbpl.iptv.jetfit.utils.deeplink.DeepLinkHandler
import com.bacbpl.iptv.jetfit.utils.deeplink.DeepLinkParams
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class OTTplayDeepLinkActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "OTTplayDeepLink"
    }

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnBackToPartner: MaterialButton
    private lateinit var tvAppName: TextView
    private lateinit var tvLoading: TextView

    private var deepLinkParams: DeepLinkParams? = null
    private lateinit var deepLinkHandler: DeepLinkHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ottplay_deeplink)

        initViews()
        setupDeepLinkHandler()
        processIntent()
    }

    private fun initViews() {
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        btnBackToPartner = findViewById(R.id.btnBackToPartner)
        tvAppName = findViewById(R.id.tvAppName)
        tvLoading = findViewById(R.id.tvLoading)

        btnBackToPartner.setOnClickListener {
            navigateBackToPartner()
        }
    }

    private fun setupDeepLinkHandler() {
        deepLinkHandler = DeepLinkHandler(this)
    }

    private fun processIntent() {
        when (intent?.action) {
            Intent.ACTION_VIEW -> {
                intent.data?.let { uri ->
                    parseAndProcessDeepLink(uri)
                } ?: run {
                    showErrorAndFinish("No deep link data found")
                }
            }
            else -> {
                val testUrl = intent.getStringExtra("test_url")
                if (!testUrl.isNullOrEmpty()) {
                    loadInWebView(testUrl)
                } else {
                    showErrorAndFinish("Invalid intent action")
                }
            }
        }
    }

    private fun parseAndProcessDeepLink(uri: Uri) {
        showLoading(true)

        val params = deepLinkHandler.parseDeepLink(uri)

        if (params == null) {
            showLoading(false)
            showErrorAndFinish("Invalid deep link format")
            return
        }

        deepLinkParams = params
        tvAppName.text = "Back to ${params.appName}"

        // টোকেন ভ্যালিডেট করুন
        validateToken(params)
    }

    private fun validateToken(params: DeepLinkParams) {
        lifecycleScope.launch {
            // ✅ সঠিক ফাংশন কল (callback ভার্সন না, suspend ভার্সন)
            val result = deepLinkHandler.validatePartnerTokenSuspend(params.token)

            when (result) {
                is DeepLinkHandler.PartnerTokenValidationResult.Valid -> {
                    showLoading(false)
                    loadOTTplayContent(params, result)
                }
                is DeepLinkHandler.PartnerTokenValidationResult.Invalid -> {
                    showLoading(false)
                    showErrorAndFinish("Invalid token: ${result.message}")
                }
                is DeepLinkHandler.PartnerTokenValidationResult.Error -> {
                    showLoading(false)
                    showErrorAndFinish("Error: ${result.message}")
                }
            }
        }
    }

    private fun loadOTTplayContent(
        params: DeepLinkParams,
        userData: DeepLinkHandler.PartnerTokenValidationResult.Valid
    ) {
        Log.d(TAG, "User validated: ${userData.name}, ${userData.mobile}")
        loadInWebView(params.contentUrl)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadInWebView(url: String) {
        webView.visibility = View.VISIBLE
        btnBackToPartner.visibility = View.VISIBLE

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowContentAccess = true
            allowFileAccess = true
            allowUniversalAccessFromFileURLs = true
            builtInZoomControls = true
            displayZoomControls = false
            useWideViewPort = true
            loadWithOverviewMode = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                safeBrowsingEnabled = true
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility = View.VISIBLE
                tvLoading.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
                injectBackButtonScript()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                request?.url?.let { uri ->
                    if (uri.toString().startsWith("ottplay://") ||
                        uri.toString().contains("ottplay.com/auth")) {
                        handleOTTplayDeepLink(uri)
                        return true
                    }

                    deepLinkParams?.backUrl?.let { backUrl ->
                        if (uri.toString().startsWith(backUrl)) {
                            navigateBackToPartner()
                            return true
                        }
                    }
                }
                return false
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
            }
        }

        // ✅ JavaScript ইন্টারফেস যোগ করুন
        webView.addJavascriptInterface(JavaScriptInterface(), "Android")

        Log.d(TAG, "Loading URL: $url")
        webView.loadUrl(url)
    }

    private fun injectBackButtonScript() {
        val appName = deepLinkParams?.appName ?: "Partner"
        val script = """
            javascript:(function() {
                var existingBtn = document.getElementById('ottplay-back-btn');
                if (existingBtn) return;
                
                var backButton = document.createElement('div');
                backButton.innerHTML = '<button id="ottplay-back-btn" style="position:fixed; bottom:20px; right:20px; z-index:9999; padding:12px 24px; background:#6200EE; color:white; border:none; border-radius:25px; font-size:16px; box-shadow:0 4px 8px rgba(0,0,0,0.3); cursor:pointer;">← Back to $appName</button>';
                
                document.body.appendChild(backButton);
                
                document.getElementById('ottplay-back-btn').onclick = function() {
                    Android.backToPartner();
                };
            })();
        """.trimIndent()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(script, null)
        } else {
            webView.loadUrl(script)
        }
    }

    private fun handleOTTplayDeepLink(uri: Uri) {
        Log.d(TAG, "Handling OTTplay deep link: $uri")

        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.ottplay.app")

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "OTTplay app not installed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateBackToPartner() {
        deepLinkParams?.backUrl?.let { backUrl ->
            Log.d(TAG, "Navigating back to partner: $backUrl")

            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(backUrl))
                startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Error navigating back: ${e.message}")
                Toast.makeText(this, "Cannot navigate back to partner app", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
            tvLoading.visibility = View.VISIBLE
            webView.visibility = View.GONE
            btnBackToPartner.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            tvLoading.visibility = View.GONE
        }
    }

    private fun showErrorAndFinish(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        Log.e(TAG, message)
        finish()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private inner class JavaScriptInterface {
        @android.webkit.JavascriptInterface
        fun backToPartner() {
            runOnUiThread {
                navigateBackToPartner()
            }
        }
    }
}