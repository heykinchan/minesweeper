
# Mine Sweeper Game

This is a Java-based implementation of the classic Minesweeper game using the **Processing** graphics library.

## 🧠 Game Concept

- The board is a grid of tiles. Some tiles contain mines.
- The objective is to uncover all the tiles that do not contain mines.
- A number is shown on a tile indicating how many neighbouring tiles contain mines.
- Right-click to flag suspected mines. Left-click to reveal tiles.
- The game ends in either a win (all non-mine tiles uncovered) or loss (mine revealed).

---

## 📁 Project Structure

```
Mine Sweeper/
├── build.gradle                             # Gradle build configuration
├── src/
│   ├── main/
│   │   ├── java/minesweeper/
│   │   │   ├── App.java                    # Main application class
│   │   │   └── Tile.java                   # Represents each tile on the board
│   │   └── resources/minesweeper/
│   │       └── *.png                       # Sprite assets (tiles, mines, flags)
```

---

## 🚀 Getting Started

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

## 🎨 Assets

All graphics (e.g., mines, tiles, flags) are stored in:

```
src/main/resources/minesweeper/
```

These are loaded dynamically through `App.java` and used during rendering.

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

Enjoy playing and modifying this version of Minesweeper!
