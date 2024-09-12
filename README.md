# SMS Translation Android Application

This project is an **Android application** that translates SMS messages from your inbox either on demand or automatically. The app allows users to translate messages to their desired language, making communication more accessible, especially when traveling or for users who regularly interact with messages in different languages. The application includes **offline translation** capabilities using **machine learning models** to ensure users can translate messages without an internet connection.

## Features

- **Custom Splash Screen with Animation**: The app starts with a custom animated splash screen.
- **SMS Translation**:
  - **On-Demand Translation**: Users can manually select individual messages or translate their entire SMS inbox.
  - **Automatic Background Translation**: The app uses a background service and broadcast receiver to automatically translate new messages even when the app is closed.
- **Offline Translation**: Leverages machine learning models for translation without requiring an active internet connection.
- **Settings Activity**: Allows users to customize app preferences such as the translation language.
- **Inbox View**: Displays SMS messages in a user-friendly format with a translation option.

## Problem

When traveling to countries where the primary language differs from yours, receiving important SMS notifications (e.g., from banks, hotels, or local businesses) can be challenging. Similarly, business communication through SMS from international partners can be difficult to understand due to language barriers.

For immigrants and expats, day-to-day communication via SMS can be overwhelming. Messages from local services, neighbors, or community groups may be hard to comprehend, leading to potential miscommunication.

## Solution

Our **SMS Translation** app addresses the issue of language barriers in SMS communication by offering both on-demand and automatic translation features:

- **Translation on Demand**: Users can choose individual messages to translate or translate their entire inbox to easily navigate through messages in different languages.
- **Automatic Translation**: Even when the app is not open, the app automatically translates incoming messages in the background.
- **Offline Functionality**: Offline translation capabilities are enabled using machine learning models, so users can translate messages without needing an active internet connection.

## Technologies Used

- **Java**: Programming language used for building the Android app.
- **Android SDK**: For creating and managing the Android app environment.
- **Google ML Kit**: Machine learning models used for offline SMS translation.
- **Broadcast Receiver and Service**: To handle background SMS translation.

## App Summary

- **Developed an Android application that translates SMS messages into the user's preferred language**.
- **Supports both on-demand and automatic translation**, ensuring users can understand SMS notifications even in foreign languages.
- **Implemented machine learning models** to enable **offline translation capabilities**.

## Demo

For a demo of the application, please refer to the attached video file [Demo.mp4](Demo.mp4).


