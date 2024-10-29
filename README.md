<h1 align="center">Gaztelu Bira App</h1>

<p align="center">
  <a href="https://www.github.com/sgaleraalq"><img src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white"/></a> 
</p>
<p align="center">This is just an app that measures stats and matches played by a local team in Pamplona, Navarra (Spain).<br>
The main purpose of the app is to gain experience using Android Studio in the Kotlin programming language.</p>

## Download
Go to the [Releases](https://github.com/sgaleraalq/GazteluBira/releases) to download the latest APK.

<h2 align="center"><b>SCREENS</b></h2>

<p align="center">The app contains 4 main screens, each one with a different purpose</p>

## Screen 1

<img src="/assets/gaztelu_screen_1.gif" align="right" width="180">

Main screen of the app. Shows a table containing each player with their respective stat:

<div style="overflow: auto;">
  <ul style="float: left; margin-right: 20px;">
    <li><img src="/assets/ic_football_ball.svg" alt="Goals" style="width: 16px; height: 16px;"> Goals</li>
    <li><img src="/assets/ic_football_shoe.svg" alt="Assists" style="width: 16px; height: 16px;"> Assists</li>
    <li><img src="/assets/ic_penalty.svg" alt="Penalties" style="width: 16px; height: 16px;"> Penalties produced</li>
    <li><img src="/assets/ic_clean_sheet.svg" alt="Clean sheet" style="width: 16px; height: 16px;"> Clean sheet when playing as a defender/goal keeper</li>
    <li><img src="/assets/ic_color_football_field.svg" alt="Games played" style="width: 16px; height: 16px;"> Games played</li>
  </ul>
</div>


If you press in any of the stats, it gets sorted from highest to lowest for that specific stat.

It also shows a card fifa-like of the highest percentage rated player. The percentage is just a sum of all the stats divided by the maximum possible value.

<p align="center">% = <sup><img src="/assets/ic_football_ball.svg" alt="Goals" style="width: 16px; height: 16px;"> + <img src="/assets/ic_football_shoe.svg" alt="Assists" style="width: 16px; height: 16px;"> + <img src="/assets/ic_penalty.svg" alt="Penalty" style="width: 16px; height: 16px;"> + <img src="/assets/ic_clean_sheet.svg" alt="Clean sheet" style="width: 16px; height: 16px;"></sup>/<sub><img src="/assets/ic_color_football_field.svg" alt="Games played" style="width: 16px; height: 16px;"></sub></p>

If user presses any of the players, a dialog of their specific stats will be shown.

Finally contains an admin button that makes you admin if you insert the correct password. Useful to insert games if needed.


## Screen 2

<img src="/assets/gaztelu_screen_2.gif" align="right" width="180">

Contains a Recycler View of all the games played by our team. If the match was won, it is shown in a green background color, if it was lost, it is shown in a red background color, and if it was a draw, it is shown in a yellow background color.

When you press in any of the matches, it redirects you to another activity which shows the specific information of that game. It contains the following information:

  - Result of the match
  - Goals and assists of each player
  - Starter team
  - Bench team


It also contains a button if you are logged as an admin to insert new games from mobile so you dont need to connect to firebase database.

--


## Screen 3

<img src="/assets/gaztelu_prev_to_last_screen.gif" align="right" width="180">

Contains a Recycler View of all the players with their images. When you press in any of the players, it redirects you to another activity which shows the specific information of that player. It contains the following information:
  
  - Name
  - Position
  - Goals of the player
  - Assists of the player
  - Penalties produced by the player
  - Clean sheets of the player
  - Games played by the player

--

--



## Screen 4

<img src="/assets/gaztelu_last_screen.gif" align="right" width="180">

In this last screen, it contains a way of comparing one player to another. When user compares two players, they will get redirect to another screen that shows a small animation of the comparison. After a few seconds, a new activity will be shown where each player will appear with its respective stats along with a Radar Chart that shows the comparison of each stat.

It uses power spinners to represent each individual stat and Radar Chart to compare them.

--

--

--

--

--

# Architecture
<p align="center">
    <img src="/assets/app_architecture.png">
</p>

# Tech stack & Open-source libraries
- Minimum SDK level 24
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
- DataBinding: Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
- [Glide](https://github.com/bumptech/glide), [GlidePalette](https://github.com/florent37/GlidePalette): Loading images from network.
- [Firebase](https://firebase.google.com/): Firebase is a platform developed by Google for creating mobile and web applications.
- [Navigation Component](https://developer.android.com/guide/navigation): Component that helps to implement navigation, from simple button clicks to more complex patterns, such as app bars and the navigation drawer.
- Custom Views
  - [ProgressView](https://github.com/skydoves/progressview): A polished and flexible ProgressView, fully customizable with animations.
  - [Radar Chart](https://github.com/PhilJay/MPAndroidChart):  A powerful & easy to use chart library for Android
  - [Lottie Animation](hhttps://github.com/airbnb/lottie-android): Lottie is a mobile library for Android and iOS that parses Adobe After Effects animations exported as json with Bodymovin and renders them natively on mobile!