# Paisa 📈

**Paisa** is a modern, feature-rich Android application designed to help users track the Indian stock market, manage their investment portfolios, and make informed financial decisions. Built with a focus on performance and a clean UI/UX.

---

## ✨ Features

- **Real-time Market Data:** Stay updated with the latest stock prices using Retrofit for API integration.
- **Portfolio Management:** Buy and sell stocks to manage your virtual or real investment portfolio.
- **Interactive Charts:** Visualize stock performance with beautiful, interactive charts powered by **MPAndroidChart**.
- **Secure Authentication:** User sign-in and data synchronization via **Firebase Authentication** and **Firestore**.
- **Modern UI Components:**
    - **Shimmer Effects:** Smooth loading states using Facebook Shimmer.
    - **Lottie Animations:** High-quality animations for a delightful user experience.
    - **Glide:** Efficient image loading for stock logos and user profiles.
- **Search Functionality:** Quickly find stocks by symbol or company name.
- **User Dashboard:** A comprehensive view of your balance, total investment, and current holdings.

---

## 🛠 Tech Stack

- **Language:** Java / Kotlin
- **Architecture:** MVVM (Model-View-ViewModel)
- **Networking:** Retrofit 2 & GSON
- **Backend:** Firebase (Auth, Firestore)
- **UI Libraries:** 
    - Material Components
    - Navigation Component
    - ViewBinding
    - Lottie Animations
    - Facebook Shimmer
    - Glide
- **Charts:** MPAndroidChart

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Giraffe or newer
- JDK 17+
- A Firebase Project (for Authentication and Firestore)

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/paisa.git
   ```
2. **Setup Firebase:**
   - Go to the [Firebase Console](https://console.firebase.google.com/).
   - Create a new project named "Paisa".
   - Register your Android app with the package name `com.example.paisa`.
   - Download the `google-services.json` file and place it in the `app/` directory.
   - Enable **Email/Password Authentication** and **Cloud Firestore**.

3. **Open in Android Studio:**
   - Launch Android Studio and select **Open**.
   - Navigate to the cloned directory and click **OK**.
   - Wait for Gradle sync to complete.

4. **Run the app:**
   - Connect an Android device or start an emulator.
   - Click the **Run** button in Android Studio.

---

## 📸 Screenshots

| Home Screen | Stock Detail | Portfolio |
| :---: | :---: | :---: |
| ![Home](https://via.placeholder.com/200x400?text=Home+Screen) | ![Detail](https://via.placeholder.com/200x400?text=Stock+Detail) | ![Portfolio](https://via.placeholder.com/200x400?text=Portfolio) |

---

## 📁 Project Structure

```text
com.example.paisa
├── activities      # Activity classes
├── adapters        # RecyclerView adapters
├── api             # Retrofit interfaces and API clients
├── fragments       # UI Fragments
├── models          # Data models (Stock, User, etc.)
├── repositories    # Data handling logic
├── utils           # Constants and helper classes
└── viewmodels      # ViewModel classes (MVVM)
```

---

## 🤝 Contributing

Contributions are welcome! If you find any bugs or have feature requests, please open an issue or submit a pull request.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

**Made with ❤️ for Investors**
