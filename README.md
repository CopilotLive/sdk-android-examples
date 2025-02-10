
# Copilot SDK for Android

## Overview

The Copilot SDK is a robust framework designed for seamless integration into Android applications, offering a streamlined experience for developers. The minimum deployment target SDK is 21.

## SDK Requirements

1. **Login** to the [Copilot Platform](https://platform.copilot.live/)

2. **Open your Copilot** and Navigate to the **Deploy** section to retrieve the integration token.

## Features

- Easy integration with Maven dependency.
- Compatible with Android 5 and later.
- Modular and extensible design.
- Lightweight and optimized for performance.
- Provides **conversation interfaces**, **deep linking capabilities**, and **voice call assistance**.
- Supports **user authentication** and **UI appearance customization**.

## Requirements

- **Android Version:** minSdk 21+
- **Languages:** Kotlin

## Installation

### Maven dependency

To integrate the Copilot SDK to your project:

1. Add the maven URL to the root `build.gradle` (project/build.gradle)

   
```ruby
   allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
   }
```
   
2. Add the following dependency to your app module's `build.gradle` file (project/app/build.gradle):
   ```ruby
   dependencies {
    implementation 'live.copilot.client:sdk:{{latest-version}}'
   }
   ```

   Replace {{latest-version}} with the latest version of the SDK from the link below
  
  - [Maven central repository](https://central.sonatype.com/artifact/live.copilot.client/sdk/versions)
    
   For e.g. implementation 'live.copilot.client:sdk:1.1.0' 

### Initialization

To initialize the Copilot SDK, add the following code in your onCreate of the `MainActivity` or at the start of your app lifecycle:

```kotlin
import Copilot

fun initializeCopilotSDK() {
     // Create a user object with placeholder data. Replace these values with real user data.
     val userData = CopilotUser(
         fullName = "", // The full name of the user
         phoneNumber = "", // The user's phone number
         profileImageUrl = "", // URL for the user's profile image
         emailAddress = "", // The user's email address
         userIdentifier = "" // A unique identifier for the user
      )
        
      // Define appearance settings for the Copilot UI
      val appearance = CopilotAppearance(
          toolbarColor = "#E9FBFB", // Background color for the toolbar bar
          backgroundColor = "#E9FBFB", // Background for the main UI
          titleText = "Copilot Assistant" // Title displayed in the tool bar
      )
        
      // Create a configuration object with the token, user data, and appearance settings
      val copilotConfig = CopilotConfig(
          token = "YOUR_COPILOT_TOKEN", // Replace with your actual Copilot token
          user = userData, // Pass the user data
          appearance = appearance // Pass the appearance settings
      )
        
      // Initialize the Copilot SDK with the configuration
      Copilot.initialize(config = copilotConfig)

      // Assign the current activity to Copilot before showing the conversations.
      Copilot.setActivity(this)
}

```

## Permissions

#### Microphone Permission

To ensure proper functionality, you must add the following permissions to your AndroidManifest.xml file:

```ruby
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
```


### User Management

```kotlin
val user = CopilotUser(
    fullName: "",        // The full name of the user
    phoneNumber: "",     // The user's phone number
    profileImageUrl: "", // URL for the user's profile image
    emailAddress: "",    // The user's email address
    userIdentifier: ""   // A unique identifier for the user
)
```
#### Setting the User in Copilot SDK
The Copilot SDK provides multiple ways to set the user for tracking and personalization purposes. You can choose the method that best fits your app's workflow:

#### 1. Set the User During Initialization
You can set the user directly when initializing the Copilot SDK. This is ideal when the user information is readily available at the time of app launch or SDK setup.

```kotlin
Copilot.initialize(
    config = CopilotConfig(
        user = user  // Providing user details in config
    )
)
```

#### 2. Set the User After Initialization
If the user information becomes available later in the app lifecycle, you can set the user after the SDK has already been initialized.

```kotlin
Copilot.setUser(user)
```

#### 3. Set the User on Login Success
When a user successfully logs into your app, you can notify the SDK to set the user. This ensures that the user's session is correctly tracked from the moment they log in.

```kotlin
Copilot.notifyLoginSuccess(user)
```
This approach is particularly helpful in apps where user login is optional or delayed until a specific interaction.

#### Logging Out

To log out the current user:

```kotlin
Copilot.logout()
```

### Customizing Appearance

To set a custom appearance:

```kotlin
val appearance = CopilotAppearance(
                   toolbarColor = "#E9FBFB", // Background for the toolbar bar
                   backgroundColor = "#E9FBFB", // Background for the copilot view
                   titleText = "Copilot Assistant" // Title displayed in the toolbar bar
        )
Copilot.setAppearance(appearance)
```

### Set the Activity Context

Assigns the hosting activity's context to the Copilot SDK. This is required to ensure proper integration with the Copilot screen. The provided context must be an instance of AppCompatActivity.

```kotlin
   Copilot.setActivity(this@MainActivity)
```

### Conversations

#### CopilotAPIResponseCallback Implementation

To handle events and responses from the CoPilot SDK, implement the CopilotAPIResponseCallback interface. This callback provides methods to manage the conversation lifecycle, deep links, and UI changes.

```kotlin
val copilotAPIResponseCallback = object : CopilotAPIResponseCallback {
    
    // Called when the conversation UI is successfully loaded
    override fun onConversationLoaded() {
        // Handle UI or logic when the conversation is ready
        println("Conversation successfully loaded.")
    }

    // Called when the conversation fails to load
    override fun onConversationFailedToLoad(error: String) {
        // Handle error scenarios such as network issues or invalid configurations
        println("Failed to load conversation: $error")
    }

    // Called when a deep link is received within the conversation
    override fun onDeepLinkReceived(url: String) {
        // Handle the received deep link URL
        println("Deep link received: $url")
    }

    // Called to hide your app's custom toolbar if needed
    override fun hideToolBar() {
        // Implement toolbar hiding logic here if required
        println("Toolbar should be hidden.")
    }
}

```
Once implemented, pass the copilotAPIResponseCallback instance when invoking `CoPilotSDK.showConversations()` to handle these events.

#### Displaying Conversations

This feature enables users to interact with the Copilot assistant through a text-based conversation interface. Users can send and receive messages within the app, making it easy to communicate with the assistant for support, guided workflows, or general assistance. The conversation history is maintained, allowing users to resume past interactions seamlessly. This ensures a smooth and real-time messaging experience without switching between different platforms.

```kotlin

fun showConversation() {
  CoPilotSDK.showConversations(
      navController = navController,        // Required if using a navigation graph
      fragmentManager = fragmentManager,    // Required if not using a navigation graph
      containerId = containerId,            // Layout container ID for the fragment
      callBack = copilotAPIResponseCallback,       // To handle API responses
      transactionType = TransactionType.REPLACE, // Default transaction type
      initialMessage = "Initial Message"           // Optional initial message
      initialValue = "",                   //Optional initial value to provide context or predefined input for the conversation.
  )
}

```

#### Make a Call

This feature allows users to communicate with the Copilot assistant using voice instead of text. It is useful for hands-free interactions, accessibility needs, or situations where speaking is more convenient than typing. Users can initiate a real-time voice conversation within the app, making it easy to get assistance without manually typing queries. The call feature provides a seamless experience by enabling direct access to voice-based support without additional steps.

To enable this feature, ensure that microphone access permission is added in `AndroidManifest.xml`.

```kotlin
fun makeCall() {
  CoPilotSDK.makeCall(
      navController = navController,        // Required if using a navigation graph
      fragmentManager = fragmentManager,    // Required if not using a navigation graph
      containerId = containerId,            // Layout container ID for the fragment
      callBack = copilotAPIResponseCallback,       // To handle API responses
      transactionType = TransactionType.REPLACE, // Default transaction type
  )
}

```

## API Reference

### `initialize`

Initializes the SDK with the provided configuration.

- **Parameters:**
  - `config` (CopilotConfig): The configuration containing the token, user details, and UI appearance.
 
### `setActivity`

Before displaying the conversation interface, you need to set the current activity using `Copilot.setActivity()`. This step is essential for the SDK to properly handle UI interactions and lifecycle events.

- **Parameters:**
  - `context` (Context): context of the current activity instance in which the conversation will be displayed.
 
### `setUser`

Sets the authenticated user for the SDK.

- **Parameters:**
  - `user` (CopilotUser): The user object to be set.

### `logout`

Logs out the currently authenticated user.

### `notifyLoginSuccess`

Notifies the SDK of a successful user login.

- **Parameter:**
  - `user` (CopilotUser): The user object representing the successfully logged-in user.

### `setAppearance`

Sets the UI appearance for the SDK.

- **Parameter:**
  - `appearance` (CopilotAppearance): The appearance object to be set.
 
### `refreshView`
 
Refreshes the View used by the Copilot SDK. Call this method to reload the content in the WebView.    

### `makeCall`

Initiates a voice call through the Copilot SDK.


- **Parameters:**
  - `navController` (NavController): A `NavController` for navigating within a NavGraph. Use this when using navigation components. (Optional).
  - `fragmentManager` (FragmentManager?): A `FragmentManager` for adding the WebView fragment to an activity. Required if `NavController` is not used. (Optional).
  -  `containerId` (Int?): The container view ID where the WebView fragment should be added. Required when using `FragmentManager`. (Optional)
  - `callback` (CopilotAPIResponseCallback?): A callback for receiving API responses from the Copilot SDK.
  - `transactionType` (TransactionType): The type of fragment transaction (`TransactionType.ADD` or `TransactionType.REPLACE`). Defaults to `TransactionType.REPLACE`.


### `showConversations`

Opens the conversation interface provided by the Copilot SDK.

- **Parameters:**
  - `navController` (NavController): A `NavController` for navigating within a NavGraph. Use this when using navigation components. (Optional).
  - `fragmentManager` (FragmentManager?): A `FragmentManager` for adding the WebView fragment to an activity. Required if `NavController` is not used. (Optional).
  -  `containerId` (Int?): The container view ID where the WebView fragment should be added. Required when using `FragmentManager`. (Optional)
  - `callback` (CopilotAPIResponseCallback?): A callback for receiving API responses from the Copilot SDK.
  - `transactionType` (TransactionType): The type of fragment transaction (`TransactionType.ADD` or `TransactionType.REPLACE`). Defaults to `TransactionType.REPLACE`.
  - `initialMessage` (String?) : An optional message that will be displayed in the conversation interface.
  - `initialValue` (String?) : An optional initial value to provide context or predefined input for the conversation.
    


