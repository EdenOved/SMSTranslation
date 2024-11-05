# **SMS Translation Android Application**

This project is an **Android application** designed to translate SMS messages either on-demand or automatically. It allows users to translate messages into their preferred language, making communication more accessible, especially while traveling or receiving messages in foreign languages. It also includes **offline translation** capabilities powered by machine learning models, ensuring users can translate messages without an internet connection.

---

## **Features**

- 🖼️ **Custom Splash Screen with Animation**: The app begins with an animated splash screen, providing a polished user experience.
  
- 📲 **SMS Translation**:
  - 🔄 **On-Demand Translation**: Users can manually translate individual SMS messages or their entire inbox with a single action.
  - 🔧 **Automatic Background Translation**: A background service and broadcast receiver automatically translate new incoming messages, even when the app is closed.
  - 📶 **Offline Translation**: Uses Google ML Kit's machine learning models for translation without requiring an internet connection.

- ⚙️ **Settings Activity**: Allows users to customize app preferences, such as selecting their target translation language.

- 📩 **Inbox View**: Displays SMS messages in a clean, user-friendly format, with an option to translate directly from the inbox.

---

## **Problem**

When traveling or living in a country with a different primary language, receiving important SMS notifications (such as from banks, hotels, or local services) can be challenging. This language barrier may lead to misunderstandings or missed information, especially in critical communications.

---

## **Solution**

**SMS Translation** addresses language barriers in SMS communication by providing:

- 🔄 **Translation on Demand**: Users can translate individual messages or their entire SMS inbox, simplifying navigation through messages in different languages.
  
- 🛠️ **Automatic Translation**: Even when the app is closed, it automatically translates incoming SMS messages in the background.

- 📶 **Offline Translation**: By leveraging machine learning models, users can translate messages without needing internet connectivity.

---

## **Technologies Used**

- ☕ **Java**: Core programming language for Android app development.
- 📱 **Android SDK**: Framework for developing and managing the Android environment.
- 🤖 **Google ML Kit**: Machine learning framework for enabling offline SMS translation.
- 🔄 **Broadcast Receiver & Service**: Manages background processing for automatic SMS translation.

---

## **App Summary**

- Built an Android app that translates SMS messages into the user's preferred language.
- Supports both on-demand and automatic translation to ensure users can interpret SMS notifications in any language.
- Offline translation is powered by machine learning models, making it ideal for areas with limited internet connectivity.

---

## **Demo**

To see the application in action, refer to the attached demo video: **Demo.mp4**.

---

### **Installation Instructions**

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/SMSTranslation-App.git
   ```

2. **Open in Android Studio**: Import the project into Android Studio.

3. **Configure ML Kit**: Follow the [Google ML Kit documentation](https://developers.google.com/ml-kit) to set up machine learning models for offline translation.

4. **Build and Run**: Once configured, build and run the project on an Android device with SMS permissions enabled.

---

## **How to Contribute**

We welcome contributions! Feel free to fork this repository, submit pull requests, or open issues for bug reports or suggestions.

---

## **Contact**

- **Name**: Eden Oved
- **Email**: edenoved.swe@gmail.com
- **LinkedIn**: [https://www.linkedin.com/in/edenoved/](https://www.linkedin.com/in/edenoved/)

---
