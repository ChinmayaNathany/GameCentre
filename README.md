
### INTRODUCTION
This is an Android Game Suite with multiple games:
1. Sliding Tiles
2. Card Matching
3. Simon

The Features we are supposed to add in this version of the project:
1. JUnit Testing: Write tests that cover as much of the code as possible (code coverage)
2. Remove Unsolvable Boards: Change 'Sliding Tiles' so that it never generates unsolvable boards.
3. Additional Games: Add 2 more games of roughly the same complexity as Sliding Tiles, make scoreboard incorporate them.
        At least 1 of the new games should have auto save points and at least 1 should have undo possible.
4. Design Patterns: Find places in the project that can use design patterns and add them
5. Refactoring: Remove code smells and refactor existing code
6. Fix Style Issues

## The Way the Games Work
1. Sliding Tiles:
    * The game starts with a shuffled board of randomly-ordered tiles and one blank tile.
    * All tiles around the blank tiles can be moved and replaced with the blank tile.
    * The game ends when the tiles are in ascending orders, with the blank tile being at the bottom right.
    * The player can undo a move using the undo button on the game screen.
    * The game can be saved with the save button on the game screen.
    * The game can be loaded from the save/load menu.
    * There is a score that is calculated based on number of moves, and the user who completes the game with
    the lowest number of moves gets the highest score.

2. Matching Cards:
    * The game starts with all cards faced-down.
    * The player must flip over two cards in efforts to create a match.
    * The game ends when all cards are matched and there are no cards left.
    * The game can be saved with the save button on the game screen.
    * The game can be loaded from the save/load menu.

3. The Simon Game:
    * The game starts with a pattern being shown to the player.
    * The player must replicate the same pattern to proceed to the next round in which a new pattern will be shown.
    * The game ends when the player does not replicate the pattern correctly.
    * The player can undo a move using the undo button on the game screen.
    * The game can be saved with the save button on the game screen.
    * The game can be loaded from the save/load menu.

## Various Classes and Functions Explained
1. CreateAccountActivity: Handles create account activity
1. LoginActivity: Handles login
2. GameActivity: Parent of SlidingTileActivity, it is an abstract class
3. SlidingTilesStartingActivity: Handles main menu screen after login ing
4. SlidingTileActivity : Handles the sliding tiles game board
5. SlidingTileEndActivity: Handles the end game screen
6. SlidingTileGameScoreboardActivity: Handles the general (game) scoreboard
7. SlidingTileUserScoreboardActivity: Handles the personalized (user) scoreboard
8. SavedGamesView: Handles the save select screen
9. SlidingTilesSettingsActivity: Handles the settings screen
10. Score: Represents the score that a user got for a game
11. Scoreboard: Represents a scoreboard full of scores
12. UserAccount: Class for each user and its data