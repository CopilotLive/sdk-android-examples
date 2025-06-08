package com.example.copilotapp

import CopilotAppearance
import CopilotConfig
import CopilotUser
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import live.copilot.client.Copilot
import live.copilot.client.helper.SafeOptions
import live.copilot.client.helper.event.TelemetryEvent
import live.copilot.client.ui.CopilotCallback
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private var copilotCallback: CopilotCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Initialize the navigation controller with the NavHostFragment
         **/
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        if (Timber.treeCount == 0) {
            Timber.plant(Timber.DebugTree())
        }


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
                Timber.tag("onReceiveTelemetry")
                    .e("Event: ${event.name} Parameters: ${event.parameters.raw()}")
            }

            /**
             * Called when a deep link is received from Copilot.
             * @param url The deep link URL received.
             */
            override fun onDeepLinkReceived(url: String) {
                Timber.tag("TAG").e("onDeepLinkReceived: $url")
            }

        }

        // Initialize Copilot SDK with configuration settings.
        Copilot.initialize(
            CopilotConfig(
                token = "",
                user = CopilotUser(
                    fullName = "", // The full name of the user
                    emailAddress = "",
                    phoneNumber = "",
                    profilePicURL = "",
                    userIdentifier = "",
                ),
                appearance = CopilotAppearance(
                    toolbarColor = "#FFFFFF",
                    backgroundColor = "#FFFFFF",
                    toolbarTintColor = "#000000",
                    titleText = "Copilot AI" // Title text for the Copilot interface,
                ),
            )
        )

        // Assign the current activity to Copilot and show the conversations.
        Copilot.setActivity(this)

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
            initialMessage = message
        )
    }

    fun setContext() {
        val option = SafeOptions()

        option.putAll(
            mapOf(
                "description" to "User is one profile screen",
                "name" to "User name",
                "email" to "email",
            )
        )

        Copilot.setContext(option)
    }

    fun unSetContext() {
        Copilot.unSetContext()
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
        val user = CopilotUser(
            fullName = "", // The full name of the user
            emailAddress = "",
            userIdentifier = "",
            phoneNumber = "",
            profilePicURL = "",
            additionalFields = SafeOptions().apply {
                putAll(
                    mapOf(
                        "description" to "User is one profile screen",
                    )
                )
            }
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
}
