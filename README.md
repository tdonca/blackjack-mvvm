# Overview
Blackjack Android Application: [Google Play Store Listing](https://play.google.com/store/apps/details?id=com.tudordonca.android.blackjackmvvm) 
  
  
### MVVM - LiveData & RxJava  
This is a Blackjack application (temporarily text-only) that focuses on Object Oriented Principles for the game mechanics.  
The user interface follows the MVVM architecture, using LiveData for UI updates in reaction to game events, and the internal game 
mechanics uses RxJava for handling user input events and their effects.
  
    
#### Blackjack Game Mechanics  
The [GameExecution](app/src/main/java/com/tudordonca/android/blackjackmvvm/gameplay/GameExecution.java) class.  
This class handles the mechanics of a round of play, and it uses RxJava's BehaviorSubject to send out important events for the UI classes to handle.
All data updates are wrapped in an Event class, for consistency. The GameExecution class uses [GameEvent](app/src/main/java/com/tudordonca/android/blackjackmvvm/GameEvent.java) wrappers.

#### UI Activity Data Management
The [GameRepository](app/src/main/java/com/tudordonca/android/blackjackmvvm/GameRepository.java) class.  
This class handles event processing from the GameExecution class, and it creates relevant LiveData objects 
for the View to update the UI accordingly. All data updates are wrapped in an Event class, for consistency. 
The GameRepository class uses [UIEvent](app/src/main/java/com/tudordonca/android/blackjackmvvm/UIEvent.java) wrappers.  


