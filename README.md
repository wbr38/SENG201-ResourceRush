# SENG201 Template Project Overview
Welcome to the template project for SENG201 which you will transform into your own.
This README file includes some useful information to help you get started.
However, we expect that this README becomes your own

## Authors
- SENG201 Teaching team
- William Brown
- Corey Hines

## Prerequisites
- JDK >= 17 [click here to get the latest stable OpenJDK release (as of writing this README)](https://jdk.java.net/18/)
- *(optional)* Gradle [Download](https://gradle.org/releases/) and [Install](https://gradle.org/install/)


## What's Included
This project comes with some basic examples of the following (including dependencies in the build.gradle file):
- JavaFX
- Junit 5

We have also included a basic setup of the Gradle project and Tasks required for the course including:
- Required dependencies for the functionality above
- Test Coverage with JaCoCo
- Build plugins:
    - JavaFX Gradle plugin for working with (and packaging) JavaFX applications easily

You are expected to understand the content provided and build your application on top of it. If there is anything you
would like more information about please reach out to the tutors.

## Importing Project (Using IntelliJ)
IntelliJ has built-in support for Gradle. To import your project:

- Launch IntelliJ and choose `Open` from the start-up window.
- Select the project and click open
- At this point in the bottom right notifications you may be prompted to 'load gradle scripts', If so, click load

**Note:** *If you run into dependency issues when running the app or the Gradle pop up doesn't appear then open the Gradle sidebar and click the Refresh icon.*

## Run Project 
1. Open a command line interface inside the project directory and run `./gradlew run` to run the app.
2. The app should then open a new window, this may not be displayed over IntelliJ but can be easily selected from the taskbar

## Build and Run Jar
1. Open a command line interface inside the project directory and run `./gradlew jar` to create a packaged Jar. The Jar file is located at build/libs/seng201_team53-1.0-SNAPSHOT.jar
2. Navigate to the build/libs/ directory (you can do this with `cd build/libs`)
3. Run the command `java -jar seng201_team53-1.0-SNAPSHOT.jar` to open the application.

## Run Tests
1. Open a command line interface inside the project directory and run `./gradlew test` to run the tests.
2. Test results should be printed to the command line

**Note:** *This Jar is **NOT** cross-platform, so you **must** build the jar you submit on Linux.* 


## Credits 
The following free to use assets were used in this project:

craftpix.net (License: https://craftpix.net/file-licenses/)
- "Free Stone Tower Game Assets" - https://craftpix.net/freebies/free-stone-tower-game-assets/  
- "Free Battle Location Top Down Game Tileset Pack 1" - https://craftpix.net/freebies/free-battle-location-top-down-game-tileset-pack-1/
- "Free Top-Down Bushes Pixel Art" - https://craftpix.net/freebies/free-top-down-bushes-pixel-art/

freesound.org
- "piano groove 2.wav" by "timouse" (https://freesound.org/people/timouse/sounds/639565/) licensed under CCBY 4.0
- "projectile.wav" by "jeckkech" (https://freesound.org/people/jeckkech/sounds/391660/) licensed under CC0 1.0

gamedeveloperstudio.com (License: https://www.gamedeveloperstudio.com/license.php)
- "Windmill game asset" - https://www.gamedeveloperstudio.com/graphics/viewgraphic.php?item=1x481r9m1c7l9i8j84

kindpng.com
- "Dollar Pixel Art, HD Png Download" by "Clint Tucker" (https://www.kindpng.com/imgv/iwbTbxm_dollar-pixel-art-hd-png-download) licensed under Personal Use Only

flaticon.com (License: https://www.freepikcompany.com/legal/#nav-flaticon-agreement)
- "Fast Forward free icon" by "Pixel perfect" (https://www.flaticon.com/free-icon/fast-forward_776819)