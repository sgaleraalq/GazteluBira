<h1 align="center">Gaztelu Bira App</h1>

<p align="center">
  <a href="https://www.github.com/sgaleraalq"><img src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white"/></a> 
</p>
<p align="center">This is just an app that measures stats and matches played by a local team in Pamplona, Navarra (Spain).<br>
The main purpose of the app is to gain experience using Android Studio in the Kotlin programming language.</p>

<p align="center">
    <img src="assets/main_image.png" width="600" heigth="300"/>
</p>

## Download
Go to the [Releases](https://github.com/sgaleraalq/GazteluBira/releases) to download the latest APK.


# Tech stack & Open-source libraries
- Minimum SDK level 21
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
- DataBinding: Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
- [Glide](https://github.com/bumptech/glide), [GlidePalette](https://github.com/florent37/GlidePalette): Loading images from network.
// Missing firebase
- Custom Views
  - [ProgressView](https://github.com/skydoves/progressview): A polished and flexible ProgressView, fully customizable with animations.

 It is MVVM designed with the use of some dependecies such as:

    - Navigation Component
    - Google firebase

Main screen consists of: 
- Picture of current leader of the board (FIFA like)
- Individual stats for every player with arrows ups and downs