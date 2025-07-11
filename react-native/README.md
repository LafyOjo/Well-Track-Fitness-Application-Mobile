# WellTrack React Native

This directory contains a React Native implementation of the WellTrack app with Firebase integration. It provides a starting point for authentication and onboarding screens that sync user data to Firestore.

## Setup

1. Install dependencies with `npm install` (requires Node.js and Expo CLI).
2. Replace placeholder Firebase config values in `src/firebase/config.js`.
3. Run the app with `npm start` or `expo start`.

The app includes:

- Email authentication using Firebase Auth
- Onboarding screen to save fitness goals and level to Firestore
- Dashboard screen that reads the saved data in real time

Use this as a foundation to add the advanced fitness tracking and monetization features described in the project roadmap.
