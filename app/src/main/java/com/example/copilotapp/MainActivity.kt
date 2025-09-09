package com.example.copilotapp

import CopilotAppearance
import CopilotConfig
import CopilotUser
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import live.copilot.client.Copilot
import live.copilot.client.helper.SafeOptions
import live.copilot.client.helper.event.TelemetryEvent
import live.copilot.client.ui.CopilotCallback
import timber.log.Timber
import androidx.navigation.findNavController
import live.copilot.client.data.CopilotSchema
import live.copilot.client.data.CopilotSchemaField
import live.copilot.client.data.CopilotTool
import live.copilot.client.data.CopilotToolResult
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private var copilotCallback: CopilotCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Initialize the navigation controller with the NavHostFragment
         **/
        navController = this.findNavController(R.id.nav_host_fragment)

        /**
         * Initialize the Copilot SDK
         **/
        initCopilot()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    /**
     * Opens the Copilot AI chat interface with an optional initial message.
     *
     * This method:
     * 1. Sets up an API response callback to handle various Copilot events.
     * 2. Initializes the Copilot SDK with user data and appearance settings.
     * 3. Displays the Copilot chat interface using the provided NavController.
     *
     * @param message Optional initial message to prefill in the chat.
     */
    private fun initCopilot() {

        copilotCallback = object : CopilotCallback {
            /**
             * Called when the app's custom toolbar should be hidden.
             */
            override fun hideToolBar() {
                Timber.tag("TAG").d("hideToolBar: ")
            }

            /**
             * Called when the conversation fails to load.
             * @param error The error message describing the failure.
             */
            override fun onError(error: String) {
                Timber.tag("TAG").e("onConversationFailedToLoad: $error")
            }

            override fun onReceiveTelemetry(event: TelemetryEvent) {
                Timber.tag("onReceiveTelemetry ")
                    .e("Event: ${event.name} Parameters: ${event.parameters.raw()} ")
            }

            /**
             * Called when a deep link is received from Copilot.
             * @param url The deep link URL received.
             */
            override fun onDeepLinkReceived(url: String) {
                Timber.tag("TAG").e("onDeepLinkReceived: $url")
                Copilot.close()
            }

        }

        val addToCartTool = CopilotTool(
            name = "add_to_cart",
            description = "Add a product to the cart",
            parameters = CopilotSchema(
                properties = mapOf(
                    "product_id" to CopilotSchemaField(
                        type = "string",
                        description = "The ID of the product"
                    ),
                    "quantity" to CopilotSchemaField(
                        type = "number",
                        description = "How many units to add"
                    )
                ),
                required = listOf("product_id", "quantity")
            ),
            handler = { params ->
                Timber.tag("TAG").e("add_to_cart: $params")
                val productId = params?.get("product_id") as? String
                    ?: return@CopilotTool CopilotToolResult(false, "Missing product_id")

                val quantity = (params["quantity"] as? Number)?.toInt() ?: 1

                CopilotToolResult(true, "Added $quantity of $productId to cart")
            }
        )

        val openCartTool = CopilotTool(
            name = "open_cart",
            description = "Opens the user's cart screen",
            handler = { params ->
                Timber.tag("TAG").e("open_cart: $params")
                CopilotToolResult(true, "Cart opened successfully")
            }
        )

        Copilot.registerTool(addToCartTool)

        Copilot.registerTool(openCartTool)
    }


    /**
     * Opens the Copilot AI chat interface.
     *
     * @param message An optional initial message to prefill in the chat interface.
     *
     * This method:
     * 1. Displays the Copilot chat interface using the navigation controller.
     * 2. Passes the callback interface to handle events triggered by the SDK.
     */
    fun openChat(message: String? = null) {
        Copilot.open(
            navController = navController,
            callback = copilotCallback,
            initialMessage = message,
        )
    }

    fun makeVideoCall() {
        Copilot.makeVideoCall(
            navController = navController,
            callback = copilotCallback,
        )
    }

    fun setContext() {
        val option = SafeOptions().apply {
            putAll(
                mapOf(
                    "store" to "luxe",
                    "headers" to mapOf(
                        "ad_id" to "41f8b99d-068d-492b-99ed-ad3ff16d6332",
                        "userCohortValues" to "",
                        "RequestId" to "PLPSearchProducts",
                        "Accept" to "application/json",
                        "os" to "1",
                        "User-Agent" to "Android",
                        "ai" to "com.ril.ajio",
                        "client_type" to "Android",
                        "client_version" to "9.22000.0",
                        "ua" to "Mozilla/5.0 (Linux; Android 11; RMX1921 Build/RKQ1.201217.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/136.0.7103.127 Mobile Safari/537.36",
                        "vr" to "AN-2.1.21"
                    ),
                    "query" to mapOf(
                        "advfilter" to false,
                        "urgencyDriverEnabled" to false,
                        "pageSize" to 30,
                        "store" to "luxe",
                        "currentPage" to 0,
                        "fields" to "SITE",
                        "facets" to false,
                        "ospreySearch" to false,
                        "vertexEnabled" to false,
                        "pincode" to "560050",
                        "offer_price_ab_enabled" to false,
                        "platform" to "android",
                        "segmentIds" to "16,18,9,21",
                        "is_ads_enable_slp" to true,
                        "is_ads_enable_plp" to false,
                        "RelExp2" to "instockold",
                        "RelExp3" to "couture_relevance",
                        "SearchFlag6" to true,
                        "stemFlag" to false,
                        "SearchFlag5" to false,
                        "SearchFlag1" to true
                    )
                )
            )
        }
        Copilot.setContext(option)
    }

    fun unSetContext() {
        Copilot.unSetContext()
    }

    fun removeTool() {
        Copilot.unregisterTool("add_to_cart")
    }

    fun removeTools() {
        Copilot.unregisterTools(listOf("add_to_cart", "open_cart"))
    }

    fun removeAllTools() {
        Copilot.unregisterAllTools()
    }

    /**
     * Initiates a voice call using the Copilot SDK.
     *
     * This method:
     * 1. Initiates a call via the Copilot SDK.
     * 2. Uses the provided `NavController` for navigation.
     * 3. Passes the callback interface to handle SDK events.
     */
    fun makeCall() {

        Copilot.setAppearance(CopilotAppearance(
            titleText = "Copilot AI",
        ))
        Copilot.makeCall(
            navController = navController,
            callback = copilotCallback,
        )
    }

    /**
     * Logs in a user to the Copilot SDK.
     *
     * The SDK will use this information to personalize interactions and manage sessions.
     */
    fun login() {

        val option = SafeOptions().apply {
            putAll(
                mapOf(
                    "description" to "User is one profile screen",
                    "Company" to "Fynd, Bangalore",
                    "empplyoee" to "Raj Jadon",
                )
            )
        }

        val user = CopilotUser(
            fullName = "Jadon", // The full name of the user
            emailAddress = "Raj@gmail.com",
            userIdentifier = "2206",
            phoneNumber = "",
            profilePicURL = "",
            additionalFields = option
        )

        Copilot.setUser(user)
        // Notify Copilot SDK about the authenticated user's details
        //Copilot.notifyLoginSuccess(user)
    }

    /**
     * Logs out the current user from the Copilot SDK.
     *
     * This method clears the user's session and associated data within the SDK.
     * After calling this, the user will be considered logged out, and any active sessions
     * will be terminated.
     */
    fun logout() {
        Copilot.logout()
    }

    override fun onStop() {
        super.onStop()
    }
}
