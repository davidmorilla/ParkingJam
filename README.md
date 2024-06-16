# Project: The Parking Jam Game

## Authors

This project has been implemented by **Iván Moratalla Rivera**,  **David Morilla Sorlí**, **Daniel Pina Rica** and **Fernando Prados Vicente**.

## Description

The goal of this project is the development of the Parking Jam game using a Java program. This project uses the model-view-controller (MVC) design pattern. Parking Jam is a sliding blocks puzzle game inspired by Klotski, a classic game from the early 20th century. The objective of the game is to move the vehicles in a parking lot to allow the red car to exit. This README provides an overview of the game's features, installation instructions, and how to play.

## Features

- **Multiple Levels**: The game includes several levels, each with a unique board layout.
- **Board Elements**: The board is divided into squares containing walls, vehicles, the red car, and the parking exit.
- **Vehicle Movement**: Vehicles can only move forward or backward, not laterally.
- **Automatic Level Detection**: The game automatically detects when the red car has exited the parking lot and allows the user to advance to the next level.
- **Level Files**: Levels are read from plain text files named `levelN.txt` (where `N` is a positive number).
- **Error Handling**: The game checks for level correctness and shows an error message if a level is invalid.
- **Scoring**: The game tracks the number of moves made and provides a total score for all levels.
- **Undo and Save**: Players can undo moves, save their progress, and load saved games.
- **Game Menu**: Options to start a new game, restart the current level, undo the last move, save the game, open a saved game, and close the application.

## Interface Description

The `ControllerInterface` interface provides the following public methods:

### `public void loadNewLevel()`

Loads a new level in the game.

- **Exceptions:**
  - `IllegalExitsNumberException`: When there's an illegal number of exits (there can only be one exit).
  - `IllegalCarDimensionException `: When a car dimensions are illegal (different from 1xN or Nx1, Red car only 2x1 or 1x2).

### `public int loadSavedLevel()`

Loads a saved level in the game and returns the number of the loaded level.

- **Exceptions:**
  - `IllegalExitsNumberException`: When there's an illegal number of exits (there can only be one exit).
  - `IllegalCarDimensionException `: When a car dimensions are illegal (different from 1xN or Nx1, red car only 2x1 or 1x2).

### `public char[][] moveCar(char car, int length, char way)`

Moves a car on the board and returns the updated board state after the move.

- **Parameters:**
  - `car ` (char): Specifies the unique identifier of the car to move.
  - `length ` (int): Specifies the length to move the car.
  - `way` (char): Specifies the direction to move the car.
- **Exceptions:**
  - `IllegalCarDimensionException `: When a car dimensions are illegal (different from 1xN or Nx1, red car only 2x1 or 1x2).
  - `SameMovementException`: When the same movement is attempted consecutively.

### `public char[][] undoMovement()`

Undoes the last car movement done and returns the updated board state after undoing the movement.

- **Exceptions:**
  - `IllegalCarDimensionException `: When a car dimensions are illegal (different from 1xN or Nx1, red car only 2x1 or 1x2).
  - `SameMovementException`: When the same movement is attempted consecutively.
  - `CannotUndoMovementException` : When there are no movements to be undone.

### `public char[][] getBoard()`

Returns the current state of the board.

### `public Pair<Integer, Integer> getBoardDimensions()`

Returns a pair containing the width and height of the board.

### `public Map<Character,Car> getCars()`

Returns a map of car identifiers mapped to car instances.

### `public int getGameScore()`

Returns the total game score.

### `public int getLevelScore()`

Returns the current level score.

### `public int getLevelNumber()`

Returns the current level number.

### `public void resetLevel()`

Resets the current level.

### `public boolean isMoveValid(char car, Coordinates newCoord, char way)`

Checks if a car movement is valid.It returns true if the movement is valid, false otherwise.

- **Parameters:**
  - `car ` (char): Specifies the unique identifier of the car to move.
  - `newCoord  ` (Coordinates): Specifies the new coordinates for the car.
  - `way` (char): Specifies the direction to move the car.
- **Exceptions:**
  - `SameMovementException`: When the same movement is attempted consecutively.

### `public boolean isLevelFinished()`

Checks if the current level is finished. It returns true if the level is finished, false otherwise.

### `public void setPunctuation(int score)`

Sets the score for the current level.

- **Parameters:**
  - `score ` (int): Specifies the score to set for the current level.

### `public void saveGame()`

Saves the current game state.

### `public void setGameScore(int totalPoints)`

Sets the total game score.

- **Parameters:**
  - `totalPoints` (int): Specifies the total points to set as the game score.

### `public void resetOriginalLevel()`

Resets the level to its original state.


### `public void loadLevel(int levelNumber)`

Loads a specific level by its number.

- **Parameters:**
  - `levelNumber` (int): Specifies the number of the level to load.

***

## Installation

1. Clone the repository using the following command or download the source code.
   ```bash
    git clone [project_repository_url]
   ```
2. Go to the parking-jam directory using the following command.
   ```bash
   cd parking-jam
   ```
3. Compile the source code and execute the game using the following commands:
   ```bash
   mvn compile
   mvn exec:java
   ```
## Running test suite

To execute the test suite for the project, follow these steps:
1. Clone the repository using the following command or download the source code.
   ```bash
    git clone [project_repository_url]
   ```
2. Go to the parking-jam directory using the following command.
   ```bash
   cd parking-jam
   ```
3. Finally, run the tests using the following command.
    ``` bash
    mvn test
    ```
## Level Files

The level files must be located inside the parking-jam/src/main/resources/levels folder.
The level files must be named `levelN.txt` (where `N` is a positive number).

### Format

- First Line: Level + number of the level
- Second Line: Dimensions of the board (nRows nColumns)
- Following Lines: Board elements, with each line containing nColumns characters representing:
    - + : Wall
    - . : Empty square
    - [a..z] : Vehicle identifier
    - * : Red car
    - @ : Exit

### Example level

   ```txt
    Level 1
    8 8
    ++++++++
    +aabbbc+
    +  *  c+
    +d *   +
    +d fff +
    +de    +
    + e ggg+
    ++++@+++
   ```

## Other input files

The program uses other input files for playing sounds and showing images. These images are not to be modified. They are lo cated inside the folders:
    1. parking-jam/src/main/resources/cars folder
    2. parking-jam/src/main/resources/exits folder
    3. parking-jam/src/main/resources/ folder
    
## Output files format

## Requirements

- Java Development Kit (JDK) 8 or higher

## Dependencies

This project requires JUnit 5.6 for testing purposes, Java Swing for the visuals and SLF4J for logging.
