# Copilot SDK for Android

[![Maven Central](https://img.shields.io/maven-central/v/live.copilot.client/sdk.svg?label=latest%20version)](https://central.sonatype.com/artifact/live.copilot.client/sdk)
[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/CopilotLive/sdk-android-examples)

## Overview

The Copilot SDK is a robust framework designed for seamless integration into Android applications,
offering a streamlined experience for developers. The minimum deployment target SDK is 21.

## SDK Requirements

1. **Login** to the [Copilot Platform](https://platform.copilot.live/)
2. **Open your Copilot** and Navigate to the **Deploy** section to retrieve the integration token.
   Refer to
   the [Mobile SDK Documentation](https://platform.copilot.live/docs/deployments/mobilesdk/) for
   more details.

## Features

- Easy integration with Maven dependency.
- Compatible with Android 5 and later.
- Modular and extensible design.
- Lightweight and optimized for performance.
- Provides **conversation interfaces**, **deep linking capabilities**, and **voice call assistance
  **.
- Supports **user authentication**, **telemetry observability**, and **UI appearance customization
  **.

## Requirements

- **Android Version:** minSdk 21+
- **Languages:** Kotlin

## Installation

### Maven dependency

To integrate the Copilot SDK to your project:

1. Add the Maven URL to the root `build.gradle`:

```gradle
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

2. Add the following dependency to your app module's `build.gradle` file:

```gradle
dependencies {
    implementation 'live.copilot.client:sdk:{{latest-version}}'
}
```

Replace `{{latest-version}}` with the latest version from:

- [Maven Central](https://central.sonatype.com/artifact/live.copilot.client/sdk/versions)

### Initialization

Add the following inside your `MainActivity` or early app lifecycle method:

```kotlin
val userData = CopilotUser(
  fullName = "",
  phoneNumber = "",
  profileImageUrl = "",
  emailAddress = "",
  userIdentifier = "",
  additionalFields = Map<String, Any>
)

val appearance = CopilotAppearance(
  toolbarColor = "#E9FBFB",
  backgroundColor = "#E9FBFB",
  toolbarTintColor = "#000000",
  titleText = "Copilot Assistant"
)

val config = CopilotConfig(
  token = "YOUR_COPILOT_TOKEN",
  user = userData,
  appearance = appearance,
  option = Map<String, Any>
)

Copilot.initialize(config = config)
Copilot.setActivity(this)
```

### Navigation Setup (Optional)

If using Jetpack Navigation:

```xml

<include app:graph="@navigation/copilot_nav" />
```

## Permissions

Add to your `AndroidManifest.xml`:

```xml

<uses-permission android:name="android.permission.INTERNET" /><uses-permission
android:name="android.permission.RECORD_AUDIO" />
```

## User Management

```kotlin
val user = CopilotUser(
  userIdentifier = "",
  fullName = "",
  phoneNumber = "",
  profileImageUrl = "",
  emailAddress = "",
)
```

Set user:

```kotlin
Copilot.setUser(user)
Copilot.notifyLoginSuccess(user)
```

UnsetUser:

```kotlin
Copilot.unsetUser()
```

Set Context:

```kotlin
const val context = Map<String, Any>
Copilot.setContext(context)
```

UnsetUser:

```kotlin
Copilot.unsetUser()
```

## Custom Appearance

```kotlin
val appearance = CopilotAppearance(
  toolbarColor = "#E9FBFB",
  backgroundColor = "#E9FBFB",
  toolbarTintColor = "#000000",
  titleText = "Copilot Assistant"
)
Copilot.setAppearance(appearance)
```

## Open Conversation

```kotlin
Copilot.open(
  navController = navController,
  callback = copilotCallback,
  initialMessage = "Hi there!"
)
```

## Close Conversation

```kotlin
Copilot.close()
```

## Make Call

```kotlin
Copilot.makeCall(
  navController = navController,
  callback = copilotCallback
)
```

## Telemetry & Analytics

The SDK provides comprehensive telemetry and analytics capabilities to help you monitor and
understand user interactions with the Copilot assistant. This section covers how to observe and
handle various telemetry events.

The SDK provides a sealed class `TelemetryEvent` that categorizes different types of events that can
be observed:

### Event Categories

- **WidgetEvent**: Events related to the Copilot widget UI
  - `Open`: When widget is opened
  - `Close`: When widget is closed
  - `Minimize`: When widget is minimized
  - `Maximize`: When widget is maximized

- **UserEvent**: Events related to user actions
  - `MessageSent`: When user sends a message
  - `MessageRead`: When user reads a message
  - `InputStarted`: When user starts typing
  - `InputEnded`: When user stops typing

- **AssistantEvent**: Events related to assistant actions
  - `MessageReceived`: When assistant sends a message
  - `Typing`: When assistant is typing
  - `SuggestionDisplayed`: When suggestions are shown
  - `ActionExecuted`: When assistant executes an action

- **CallEvent**: Events related to voice calls
  - `Started`: When call begins
  - `Ended`: When call ends
  - `Connected`: When call connects
  - `Disconnected`: When call disconnects
  - `Failed`: When call fails

- **CTAEvent**: Events related to Call-To-Action interactions
  - `Clicked`: When a CTA button is clicked
  - `Displayed`: When CTAs are displayed

### Observing Events

```kotlin
// This is to observe all telemetry events
observeAllTelemetry { event ->
  Timber.tag("Telemetry-All").d("${event.name}: ${event.parameters.raw()}")
}

// Observe specific telemetry event for widget close
observeTelemetry<TelemetryEvent.WidgetEvent.Close> { event ->
  // Log the event name and parameters when widget is closed
  Timber.tag("Telemetry-Close").d("${event.name}: ${event.parameters.raw()}")
}

observeTelemetrySection(
  onWidget = { event ->
    when (event) {
      is TelemetryEvent.WidgetEvent.Open -> Timber.d("Widget opened")
      is TelemetryEvent.WidgetEvent.Close -> Timber.d("Widget closed")
      else -> Timber.d("Widget event: ${event.name}")
    }
  },
  onUser = { event ->
    when (event) {
      is TelemetryEvent.UserEvent.MessageSent -> Timber.d("User sent message")
      is TelemetryEvent.UserEvent.MessageRead -> Timber.d("User read message")
      else -> Timber.d("User event: ${event.name}")
    }
  },
  onAssistant = { event ->
    when (event) {
      is TelemetryEvent.AssistantEvent.MessageReceived -> Timber.d("Assistant message received")
      is TelemetryEvent.AssistantEvent.Typing -> Timber.d("Assistant is typing")
      else -> Timber.d("Assistant event: ${event.name}")
    }
  },
  onCall = { event ->
    when (event) {
      is TelemetryEvent.CallEvent.Started -> Timber.d("Call started")
      is TelemetryEvent.CallEvent.Ended -> Timber.d("Call ended")
      else -> Timber.d("Call event: ${event.name}")
    }
  },
  onCtaClick = { event -> Timber.d("CTA clicked: ${event.name}") },
  onOther = { event -> Timber.d("Other telemetry event: ${event.name}") },
  onError = { err ->
    Timber.e(err, "Telemetry error occurred")
    // Additional error handling if needed
  }
)

TelemetryObserverConfig.onUnhandledException = { exception ->
  // Global error handler for unhandled telemetry exceptions
  Timber.e(exception, "Unhandled Telemetry Exception Occurred")
  // Add your custom error handling logic here
}
```

## Callback Handling

```kotlin
val copilotCallback = object : CopilotCallback {
  override fun hideToolBar() {
    Timber.d("Toolbar hidden")
  }

  override fun onError(error: String) {
    Timber.e("Conversation load error: $error")
  }

  override fun onDeepLinkReceived(url: String) {
    Timber.d("Deep link received: $url")
  }
}
```

## API Reference Summary

| Method                                         | Description                                    |
|------------------------------------------------|------------------------------------------------|
| `initialize(config)`                           | Initialize SDK with token, user, and UI config |
| `setActivity(activity)`                        | Set current activity context                   |
| `setUser(user)`                                | Set user dynamically                           |
| `notifyLoginSuccess(user)`                     | Notify SDK of a successful login               |
| `setAppearance(appearance)`                    | Update Copilot UI styling                      |
| `open()`                                       | Launch conversation UI                         |
| `makeCall()`                                   | Start voice-based assistant call               |
| `observeTelemetry*()`                          | Observe telemetry events                       |
| `TelemetryObserverConfig.onUnhandledException` | Global error fallback for telemetry            |

---

## Full Example (Client Integration)

See `MainActivity.kt`
in [this](https://github.com/CopilotLive/sdk-android-examples/blob/main/app/src/main/java/com/example/copilotapp/MainActivity.kt)
repo for a complete working example integrating telemetry observers, conversation UI, and voice
call. This ensures full lifecycle management and feature exposure from Copilot SDK.

For additional setup help, refer
to [Mobile SDK Docs](https://platform.copilot.live/docs/deployments/mobilesdk/).

---

For issues or feature requests, please reach out via your Copilot dashboard support.

---

© Copilot.live – All rights reserved.
