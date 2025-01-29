package com.example.copilotapp

import CopilotAppearance
import CopilotConfig
import CopilotUser
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import live.copilot.client.Copilot
import live.copilot.client.ui.CopilotAPIResponseCallback

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private var apiResponseCallback: CopilotAPIResponseCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Initialize the navigation controller with the NavHostFragment
         **/
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        /**
         * Initialize the Copilot SDK
         **/
        initCopilot()
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

        apiResponseCallback = object : CopilotAPIResponseCallback {
            /**
             * Called when the app's custom toolbar should be hidden.
             */
            override fun hideToolBar() {
                //TODO("Not yet implemented")
            }

            /**
             * Called when the conversation fails to load.
             * @param error The error message describing the failure.
             */
            override fun onConversationFailedToLoad(error: String) {
                //TODO("Not yet implemented")
            }

            /**
             * Called when the conversation has successfully loaded.
             */
            override fun onConversationLoaded() {
                //TODO("Not yet implemented")
            }

            /**
             * Called when a deep link is received from Copilot.
             * @param url The deep link URL received.
             */
            override fun onDeepLinkReceived(url: String) {
                //TODO("Not yet implemented")
            }
        }

        // Initialize Copilot SDK with configuration settings.
        Copilot.initialize(
            CopilotConfig(
                url = "https://test.ai.copilot.live/", // Copilot URL
                user = CopilotUser(
                    fullName = "", // User's full name
                    phoneNumber = "", // User's phone number
                    profileImageUrl = "", // Profile image URL
                    emailAddress = "", // User's email address
                    userIdentifier = "" // Unique user identifier
                ),
                appearance = CopilotAppearance(
                    toolbarColor = "#FFFFFF",
                    backgroundColor = "#FFFFFF",
                    titleText = "Copilot AI" // Title text for the Copilot interface,
                )
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
        Copilot.showConversations(
            navController = navController,
            callback = apiResponseCallback,
            initialMessage = message
        )
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
            callback = apiResponseCallback,
        )
    }
}
