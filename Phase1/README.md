# Setup Instructions:

## The repo to clone:
Use the following URL to clone the repo containing the GameCentre app:
    https://markus.teach.cs.toronto.edu/git/csc207-2018-09-reg/group_0552/
### How to clone the repo:
1. Install Android Studio if not already installed
2. At the Android Studio welcome screen, select "Check out project from
version control" and select Git from the dropdown menu.
3. Copy-paste the URL above into the Textbox for "URL" (test it to see
if it works), change the project directory if needed, and then select
"Clone". In the following steps, if prompted, do NOT add files to the
repo.
4. When prompted, select "Yes" to making a new Android Studio Project
5. When prompted to import project, check "Import project from external
sources" and "Android Gradle", then select "Finish"
6. When prompted to "configure with a Gradle wrapper", select "Yes".
7. The project should now be cloned onto your local machine.

## Run the app in an emulator in Android studio:
1. Open android studio navigate (File --> Open...)
2. Open the GameCentre project (navigate to where the repo was cloned,
open the folder Phase1 and select GameCentre, then press "OK")
    - If you have not yet set up the SDK and Android Virtual Device in
    Android studio yet, please do so. Set the SDK level to Android 8.1
    API 27 and create a Google Pixel 2 with API level 27 emulator.
    - You may be prompted that the specified SDK directory doesn't match
    Android Studio's SDK directory, select OK.
3. Run the app (Run --> Run app), you will be prompted to select
deployment target. Select the Google Pixel phone.
4. The app will be installed automatically onto the emulator and begin
to run.

## What to click when running the app: (Getting to play SlidingTiles)
1. Open the app from the phone's app drawer.
2. You will be prompted to log in. If you do not have an account and
you'd like to create an account, enter a Username (at least 3
characters) and Password (at least 3 characters) and select "Register
Using Credentials".
3. Login as Guest or Login with your Username and Password.
4. Select the SlidingTiles game (Red image button on the left)
5. Pick a saved game to play, or start a new game
    - You can adjust the board size and number of undo-steps as you
    would like
    - Board size maximum at 33 x 33
6. Play until solved, the scores are calculated from a maximum, bonus
points if you win faster
7. When the puzzle is solved, the app will go to the scoreboard where
you can see scores of all users in the Game Centre, or only look at your
own.
8. Start a new game with the same size board or go back to the game selection.

## Functionalities of GameCentre
1. Login and register activity. Users can register an account to save
unfinished games and pick up where they left off, or to play as a guest
with limited game features
2. Currently there is only Sliding Tiles available to play
3. In Sliding Tiles, registered players can select game board size and
number of undos steps. They can also load an image from a URL to solve
an image puzzle
4. In the Sliding Tiles game play, users can undo the a maximum number
of specified moves, and save their game play. Game will also autosave at
preset time intervals.
5. When the game is solved, player scores will be calculated
automatically and be directed to the scoreboard
6. The scoreboard will display the list of scores of all registered
players from highest to lowest. If the user is a guest, the guest score
will also be ordered into this list, but not saved.
7. On the scoreboard, registered users have the option of looking at
their own list of high scores, Guests do not have this option.
8. On the scoreboard, the user has the option of selecting a new game to
play, or to try the same board again.