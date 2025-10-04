# 📚 BookCast

<div align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin-purple.svg" alt="Language">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg" alt="UI">
</div>

A modern Android audiobook player app that brings the world of free public domain audiobooks from LibriVox to your fingertips. Built with the latest Android technologies and Material Design 3, BookCast offers a seamless listening experience with intelligent playback features and personalized recommendations.

## ✨ Features

### 🎧 Core Functionality
- **LibriVox Integration**: Access thousands of free public domain audiobooks
- **Advanced Audio Playback**: Powered by ExoPlayer with background playback support
- **Continue Listening**: Automatically saves your progress and lets you resume where you left off
- **Dual Player Interface**: Switch between mini-player for multitasking and full-screen player for immersive experience

### 📖 Discovery & Organization
- **Personalized Recommendations**: Get book suggestions tailored to your interests
- **New Releases**: Stay updated with the latest audiobook additions
- **Smart Search**: Find books by title or author quickly
- **Genre Browsing**: Explore audiobooks organized by genre
- **Favorites**: Save your favorite books for quick access using Firebase Firestore

### 🔐 User Experience
- **Firebase Authentication**: Secure login and registration
- **Guest Mode**: Start listening immediately without creating an account
- **Offline-First Architecture**: Access your library even without internet connection
- **Material Design 3**: Beautiful, intuitive interface following the latest design guidelines

## 🏗️ Architecture

BookCast follows modern Android development best practices with a clean, scalable architecture:

### Architecture Pattern
- **MVVM (Model-View-ViewModel)**: Clear separation of concerns
- **Feature-Based Modularization**: Organized codebase for better maintainability
- **Offline-First**: Room database ensures data availability

### Tech Stack

#### 🎨 UI Layer
- **Jetpack Compose**: Modern declarative UI toolkit
- **Material Design 3**: Latest design system for beautiful interfaces
- **Navigation Compose**: Type-safe navigation with the latest Navigation 3 library

#### 🔧 Business Logic
- **Kotlin Coroutines**: Asynchronous programming
- **Flows**: Reactive data streams
- **Hilt**: Dependency injection for clean architecture

#### 💾 Data Layer
- **Room**: Local database for offline-first architecture
- **Retrofit**: Network requests to LibriVox API
- **Firebase Authentication**: User management
- **Firebase Firestore**: Cloud persistence for favorites and user data

#### 🎵 Media
- **ExoPlayer**: Professional-grade audio playback with background support

## 📱 Screens

1. **Home Screen**: Continue listening, recommendations, and new releases
2. **Discover Screen**: Search functionality and genre exploration
3. **Search Screen**: Find audiobooks by title or author
4. **Genre Lists**: Browse books by category
5. **Favorites Screen**: Quick access to your saved audiobooks
6. **Details Page**: Comprehensive book information and playback controls
7. **Profile Page**: Manage your account and preferences
8. **Mini Player**: Persistent playback controls across the app
9. **Full Screen Player**: Immersive listening experience with full controls

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 11 or higher
- Android SDK with minimum API level 24 (Android 7.0)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/AndreOpollo/BookCast.git
   cd BookCast
   ```

2. **Firebase Configuration**
   - Create a new project in [Firebase Console](https://console.firebase.google.com/)
   - Add an Android app to your Firebase project
   - Download `google-services.json` and place it in the `app/` directory
   - Enable Authentication (Email/Password) and Firestore in Firebase Console

3. **Build and Run**
   - Open the project in Android Studio
   - Sync Gradle files
   - Run the app on an emulator or physical device

## 🔑 API

BookCast uses the [LibriVox API](https://librivox.org/api/info) to fetch audiobook data. No API key is required as LibriVox provides free access to their catalog.

## 📦 Modularization

The project follows a feature-based modular structure:

```
BookCast/
├── app/                    # Main application module
├── feature/
│   ├── home/              # Home screen feature
│   ├── discover/          # Discovery and search feature
│   ├── favorites/         # Favorites management
│   ├── player/            # Audio player feature
│   └── profile/           # User profile feature
├── core/
│   ├── data/              # Data layer (repositories, data sources)
│   ├── domain/            # Domain models and use cases
│   ├── network/           # Network module (Retrofit, API)
│   ├── database/          # Local database (Room)
│   └── ui/                # Shared UI components
└── buildSrc/              # Dependency management
```

## 🛠️ Technologies & Libraries

| Category | Technology |
|----------|-----------|
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| Architecture | MVVM, Clean Architecture |
| Dependency Injection | Hilt |
| Networking | Retrofit, OkHttp |
| Local Database | Room |
| Async Programming | Coroutines, Flow |
| Media Playback | ExoPlayer |
| Authentication | Firebase Auth |
| Cloud Database | Firebase Firestore |
| Navigation | Navigation Compose 3 |
| Design System | Material Design 3 |

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 🙏 Acknowledgments

- [LibriVox](https://librivox.org/) for providing free public domain audiobooks
- [ExoPlayer](https://github.com/google/ExoPlayer) for the excellent media playback library
- The Android development community for amazing tools and libraries

## 📧 Contact

Andre Apollo - [@AndreOpollo](https://github.com/AndreOpollo)

Project Link: [https://github.com/AndreOpollo/BookCast](https://github.com/AndreOpollo/BookCast)

---

<div align="center">
  Made with ❤️ and Kotlin
</div>
