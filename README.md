# Marvel Heroes

## Simple app that consuming [Marvel Api](https://developer.marvel.com/)

[Marvel Heroes Apk](https://drive.google.com/open?id=1_bGH295fUg9EW-UKInXt7qiUwsIx4yZg)

### Main features:
- List Heroes
- Search hero
- Hero Details
- Mark Hero favorite

Architecture
MVVM
![alt tag](/imgs/googleArchitecture.png)

- Communications between View <-> ViewModel are made using DataBinding and LiveData. 
- Using [ROOM](https://developer.android.com/topic/libraries/architecture/room) library to save/remove favorites heroes.
- Using [Glide](https://github.com/bumptech/glide) library to download and cache images.
- Using [Retrofit](https://square.github.io/retrofit/) library to create interfaces with MarvelApi.
- Using [Mockito](https://github.com/mockito/mockito) library to create tests.
- Using [Gson](https://github.com/google/gson) library to make MarvelApi json response parse.
- Using [AndroidViewAnimations](https://github.com/daimajia/AndroidViewAnimations) library to create participation screen animations
- Handling network connection lost.
- Avoiding lost data when configurations changes.
- Heroes list pagination with UI feedback\'s.
- Hero details with comics, events, stories and series participation\'s.
- Custom transitions between list and hero details.
- Hero search by name.

# Classes
Activities:
- [MainActivity](app/src/main/java/com/hlandim/marvelheroes/MainActivity.kt) -> Launcher activity, handle app permissions, configure toolbar buttons( SearchView and Favorites ) and show Fragment to list heroes.
- [HeroActivity](app/src/main/java/com/hlandim/marvelheroes/view/details/HeroActivity.kt) -> Hero details (Name, Photo, List of participation\'s) and Favorite/Unfavorite hero.

Fragments: 
- [HeroesFragment](app/src/main/java/com/hlandim/marvelheroes/view/list/HeroesFragment.kt) -> List heroes, favorites and search result.
- [ParticipationFragment](app/src/main/java/com/hlandim/marvelheroes/view/details/ParticipationFragment.kt) -> Show hero participation(comic, event, storie and series) details.

Adapters:
- [ParticipationAdapter](app/src/main/java/com/hlandim/marvelheroes/view/details/ParticipationAdapter.kt) -> Adapter for participation\'s ListView 
- [HeroesAdapter](app/src/main/java/com/hlandim/marvelheroes/view/list/HeroesAdapter.kt) -> Adapter with pagination for heroes list.

ViewModel:
- [HeroesViewModel](app/src/main/java/com/hlandim/marvelheroes/viewmodel/HeroesViewModel.kt) -> Responsible for get heroes list, search hero feature. This class use LiveData/MutableLiveData so the changes can be reflected automatic on layout. Do communication\'s with repository.
- [HeroViewModel](app/src/main/java/com/hlandim/marvelheroes/viewmodel/HeroViewModel.kt) -> Responsible for keep the hero used in Hero details screen, Favorite/Unfavorite repository request.

Database:
- [HeroesRepository](app/src/main/java/com/hlandim/marvelheroes/database/HeroesRepository.kt) -> Responsible for communication with [MarvelAPI](https://developer.marvel.com/) and Room database abstraction library.
- [AppDataBase](app/src/main/java/com/hlandim/marvelheroes/database/AppDataBase.kt) -> Room database configuration.
- [Converter](app/src/main/java/com/hlandim/marvelheroes/database/Converter.kt) -> Keep the converters methods used in database one-to-many relationship.

Web:
- [MarvelApi](app/src/main/java/com/hlandim/marvelheroes/web/mavel/MarvelApi.kt) -> Interface responsable for retrofit configuration and Marvel Api endpoints.
- [HeroesService](app/src/main/java/com/hlandim/marvelheroes/web/mavel/HeroesService.kt) -> Responsible for use MarvelApi to make call to the endpoints.

Tests:
- [HeroesViewModelTest](app/src/test/java/com/hlandim/marvelheroes/viewmodel/HeroesViewModelTest.kt) -> Tests for getHeroes, searchHero and showFavoritesHeroes
- [HeroViewModelTest](app/src/test/java/com/hlandim/marvelheroes/viewmodel/HeroViewModelTest.kt) -> Tests for mark/unmark favorite hero.


# Screens

 Listing heroes             |  Hero details             | Searching Hero             | Listing favorites heroes              
:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------
<img src="https://github.com/hlandim/MarvelHeroes/blob/development/imgs/listing.gif" width="180" height="320">  |    <img src="https://github.com/hlandim/MarvelHeroes/blob/development/imgs/hero_details.gif" width="180" height="320">  |    <img src="https://github.com/hlandim/MarvelHeroes/blob/development/imgs/searching.gif" width="180" height="320">  |    <img src="https://github.com/hlandim/MarvelHeroes/blob/development/imgs/favorites.gif" width="180" height="320">


<p>
 <h3>Gif used in loading requests</h3>
  <img src="https://github.com/hlandim/MarvelHeroes/blob/development/app/src/main/res/raw/search_hero_loading.gif" width="300" height="178">
  <h1>Thanks</h1>
</p>
