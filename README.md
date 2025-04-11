
# Mine Sweeper Game

A course project in my uni study. This is a Minesweeper game for fun, implemented using Java with gradle and processing library. 

---

## 🚀 Set-up of the Program

### Prerequisites

- Java 8 or higher
- Gradle (or use the Gradle wrapper)
- [Processing Core Library](https://processing.org/)

Make sure to include the `processing-core` dependency in `build.gradle`:

```groovy
dependencies {
    implementation 'org.processing:core:3.3.7'
}
```

### Running the Game

To run with Gradle:

```bash
./gradlew run
```

Or compile and run manually:

```bash
javac -cp path_to_processing_core.jar src/main/java/minesweeper/*.java
java -cp .:path_to_processing_core.jar minesweeper.App
```

---

## 🎮 Controls

- **Left Click**: Reveal tile
- **Right Click**: Flag/unflag tile

---

## 🧪 Development Notes

- The `App.java` handles initialisation, user input, game loop, and rendering.
- The game uses a 2D array of `Tile` objects for board logic.
- Mine count and colour configurations are stored in `mineCountColour`.

---

Enjoy playing this version of Minesweeper!
