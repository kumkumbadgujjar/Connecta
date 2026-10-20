# Connecta - Web Application

Connecta is a modern messaging, updates, community posts, and voice/video calling application rewritten from Android Jetpack Compose to React + TypeScript + Vite with Tailwind CSS.

## Features

- **Authentication Flow**:
  - Splash screen with branded logo and animation
  - Welcome screen with QR scanning illustration and end-to-end encryption assurance
  - Phone number verification with country selector (+91, +1, +86, +92) and OTP input
  - User profile setup with name, status, and avatar customizer
- **Chats (`HomeScreen`)**:
  - Real-time search filter for contacts and message content
  - Conversation list (College, School, Him, Her, It, They, You, WE, MOM)
  - Interactive chat modal with message history, instant reply, delivery receipts, and attachment actions
  - New chat contact picker
- **Updates (`UpdateScreen`)**:
  - "My status" item with camera/story creator
  - Recent updates (Ananya Sharma, Rohan Verma, Neha Patil, Travel Diaries, etc.) with teal status ring
  - Viewed updates with gray status ring
  - Interactive full-screen status/story viewer with auto-progress bar, pause/resume, and reply bar
- **Posts (`PostScreen`)**:
  - Community feed with user avatars, timestamps, and captions
  - High-resolution photo posts (Sunset, Food, Architecture)
  - Interactive like system with live heart animations and counters
  - Comment drawer with threaded discussion and commenting
  - Floating action button to publish new community posts
- **Calls (`CallScreen`)**:
  - Filtering by "All Calls", "Missed", "Outgoing", and "Incoming"
  - Interactive call logs with color-coded call status (Missed in red, Incoming/Outgoing in teal)
  - Interactive dialpad keypad modal
  - Full simulated audio and video call screen with ringing, connected duration timer, mute, speaker, and end call controls
- **UI & Experience**:
  - True-to-original Connecta color scheme (Teal `#06B6D4`, dark text `#102A32`, secondary `#607D86`, background `#FFFFFF`)
  - Desktop responsive frame with mobile phone simulator toggle
  - Persistent storage in localStorage

## Getting Started

```bash
npm install
npm run dev
```

App runs on port 3000 at `http://0.0.0.0:3000`.
