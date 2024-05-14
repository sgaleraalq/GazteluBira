<h1 align="center">Gaztelu Bira App</h1>

<p align="center">
  <a href="https://www.github.com/sgaleraalq"><img src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white"/></a> 
</p>
<p align="center">This is just an app that measures stats and matches played by a local team in Pamplona, Navarra (Spain).<br>
The main purpose of the app is to gain experience using Android Studio in the Kotlin programming language.</p>

<h2 align="center">Screens</h2>

The app contains 4 main screens, each one with a different purpose.

### Screen 1

Main screen of the app. Shows player stats with each stat:

<div style="overflow: auto;">
  <ul style="float: left; margin-right: 20px;">
    <li><img src="/assets/ic_football_ball.svg" alt="Goals" style="width: 16px; height: 16px;"> Goals</li>
    <li><img src="/assets/ic_football_shoe.svg" alt="Assists" style="width: 16px; height: 16px;"> Assists</li>
    <li><img src="/assets/ic_penalty.svg" alt="Penalties" style="width: 16px; height: 16px;"> Penalties produced</li>
    <li><img src="/assets/ic_clean_sheet.svg" alt="Clean sheet" style="width: 16px; height: 16px;"> Clean sheet when playing as a defender/goal keeper</li>
    <li><img src="/assets/ic_color_football_field.svg" alt="Games played" style="width: 16px; height: 16px;"> Games played</li>
  </ul>

  <div style="float: right; margin-left: 20px;">
    <img src="/assets/gaztelu_screen_1.gif" width="240">
  </div>
</div>

## Download
Go to the [Releases](https://github.com/sgaleraalq/GazteluBira/releases) to download the latest APK.


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

## Features
The app contains 4 different screens based upon the player's stats and matches played by the team.

Data is added manually to Firebase Firestore, and the app retrieves it from there following MVVM architecture.