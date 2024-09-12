
# **SMS Translation Android Application**

This project is an Android application designed to translate SMS messages either on-demand or automatically. The app allows users to translate messages into their preferred language, making communication more accessible, especially when traveling or interacting with messages in foreign languages. It also includes offline translation capabilities powered by machine learning models, ensuring users can translate messages without an active internet connection.

---

## **Features**

- **Custom Splash Screen with Animation**: The app starts with a custom animated splash screen for a polished user experience.
  
- **SMS Translation**:
  - **On-Demand Translation**: Users can manually select individual SMS messages or translate their entire inbox with a single action.
  - **Automatic Background Translation**: A background service and broadcast receiver automatically translate new incoming messages even when the app is not running.
  - **Offline Translation**: Leverages Google ML Kit's machine learning models to provide translation without requiring an internet connection.

- **Settings Activity**: Users can customize app preferences, such as selecting the target translation language.

- **Inbox View**: Displays SMS messages in a clean, user-friendly format with the option to translate directly from the inbox.

---

## **Problem**

When traveling to countries where the primary language differs from your own, receiving important SMS notifications (such as from banks, hotels, or local businesses) can be challenging. Similarly, international business communication via SMS may be hard to understand due to language barriers.

For immigrants and expats, day-to-day communication through SMS with local services or neighbors can be overwhelming, leading to misunderstandings or missed information.

---

## **Solution**

**SMS Translation** solves the issue of language barriers in SMS communication by offering:

- **Translation on Demand**: Users can choose to translate individual messages or their entire SMS inbox, making it easier to navigate through messages in different languages.
  
- **Automatic Translation**: Even when the app is closed, the app automatically translates incoming SMS messages in the background.

- **Offline Translation**: Machine learning models are used to enable offline translation, allowing users to translate messages without an internet connection.

---

## **Technologies Used**

- **Java**: Core programming language used for the Android app development.
- **Android SDK**: Framework used to develop and manage the Android environment.
- **Google ML Kit**: Used for implementing machine learning models to handle offline SMS translation.
- **Broadcast Receiver & Service**: Handles background processing for automatic SMS translation.

---

## **App Summary**

- Developed an Android application that translates SMS messages into the user's preferred language.
- Supports both on-demand and automatic translation, ensuring users can understand SMS notifications even in foreign languages.
- Implemented machine learning models to enable offline translation capabilities.

---

## **Demo**

For a demo of the application, please refer to the attached video file: **Demo.mp4**.

---

### **Installation Instructions**

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/SMSTranslation-App.git
   ```

2. **Open in Android Studio**: Import the project into Android Studio.

3. **Configure ML Kit**: Follow the [Google ML Kit documentation](https://developers.google.com/ml-kit) to set up the machine learning models for offline translation.

4. **Build and Run**: Once configured, build and run the project on an Android device with SMS permissions enabled.

---

## **How to Contribute**

Feel free to fork this repository, submit pull requests, or open issues if you encounter any bugs or have suggestions for improvements.

---

## **Contact**

- **Name**: Eden Ovad
- **Email**: edenovad777@gmail.com
- **LinkedIn**: https://www.linkedin.com/in/edenovad/

