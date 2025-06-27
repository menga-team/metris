# Metris

A old-school, feature-rich Tetris clone built with Java and libGDX. Metris offers both singleplayer and multiplayer modes, a innovative graphical interface, and
classic Tetris mechanics with a few modern twists.

---

## Members

- **Mair Emil**
- **Unterweger Gabriel**
- **Vandini Elia**
- **Vladislav Yegorov**

---

# Installation

> The following steps will get Metris running on your Ubuntu machine:

1. **Install Java and Git and Maven:**
   ```bash
   sudo apt update && sudo apt upgrade -y
   sudo apt install openjdk-21-jdk git mvn
   ```

2. **Clone the repository:**
   ```bash
   git clone https://github.com/menga-team/metris.git
   cd metris
   ```

3. **Build and run the game:**
   ```bash
   mvn package && java -jar Gui/target/METRIS-full.jar
   ```

---

# 🧭 Overview

Metris is a desktop Tetris game that faithfully recreates the classic experience while adding modern usability features. The game is built with Java and libGDX,
and supports both solo play and local multiplayer. The project is modular, separating core game logic from GUI code, and is designed for easy extension.

---

# 📑 User's Guide

- **Launching:** Start the game to access the main menu, where you can choose between singleplayer, multiplayer, settings, help, about, or exit.
- **Controls:**
    - **Move:** Arrow keys
    - **Hard Drop:** Space (doubble-tap for instant drop)
    - **Hold Piece:** rC
    - **Rotate:** X
- **Settings:** Adjust whether space places blocks..
- **(TODO) Multiplayer:** Play head-to-head on the same machine (networked multiplayer is not implemented).

---

# 🛠 Project Structure

```plaintext
.
├── Common/
│   └── src/main/java/dev/menga/metris/...
│   └── src/main/test/java/dev/menga/metris/Vec2iTest.java
├── Gui/
│   └── src/main/java/dev/menga/metris/...
│   └── src/main/assets/...
├── README.md
├── pom.xml
├── PROTOCOL.org
└── ...
```

## 🧩 Components

- **Common:** Core Tetris logic (game state, field, tetrominoes, scoring, etc.)
- **Gui:** All graphical interface code, screens, widgets, and menu logic
- **Assets:** Sprites, music, and other resources (e.g., `tiles.atlas`, `background.atlas`, `titleMusic.mp3`)
- **Entry Point:** `MetrisGui.java`

---

# 🎮 Game Features

- **Classic Tetris gameplay:** 7-bag randomizer, SRS rotation, hold, hard/soft drop, scoring, and level progression.
- **Modern GUI:** Responsive menus, custom cursor, and smooth transitions.
- **Multiplayer:** Two boards rendered side-by-side for local competitive play.
- **Audio:** Background music and sound effects with adjustable volume.

---

# 📦 External Libraries

- [libGDX](https://libgdx.com/) – Cross-platform game framework
- [Lombok](https://projectlombok.org/) – Reduces Java boilerplate

---

# 🖥️ Algorithms & Techniques

- **7-bag randomizer:** Ensures fair tetromino distribution.
- **Super Rotation System (SRS):** Standardized piece rotation and wall kicks.
- **Line clear & scoring:** Classic Tetris rules, including Tetris bonuses.

---

# 🧑‍💻 Team Experience

> *This project was a deep dive into game architecture, GUI design, and the nuances of Tetris mechanics. We learned to balance classic gameplay with modern
usability, and to structure code for clarity and extensibility. Challenges included implementing SRS rotation, managing game state transitions, and optimizing
asset loading for smooth user experience.*

---

# 🗃️ Git Usage

- The project uses Git for version control.

---