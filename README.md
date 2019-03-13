# MarvelHeroes

Architecture
MVVM
![alt tag](/imgs/googleArchitecture.png)

- Cominication between View <-> ViewModel are made using DataBinding and LiveData. 
- Using [ROOM](https://developer.android.com/topic/libraries/architecture/room) to save/remove favorites heroes.
- Using [Glide](https://github.com/bumptech/glide) to download and cache images.
- Using [Retrofit](https://square.github.io/retrofit/) to create interfaces with MarvelApi.
- Using [Mockito](https://github.com/mockito/mockito) to create tests.
- Using [Gson](https://github.com/google/gson) to make MarvelApi json response parse.
- Handling network conection lost.
- Avoiding lost data when configurations changes.
- Heroes list paginations with UI feedbacks.
- Hero details with comics, events, stories and series participations.
- Custom transitions between list and hero details.
- Hero searhc by name.

# Classes
Acitivities:
- [MainActivity](app/src/main/java/com/hlandim/marvelheroes/MainActivity.kt) -> Launcher activity, handle app permissions, configure tollbar buttons( SearchView and Favorites ) and show Fragment to list heroes.
- [HeroActivity](app/src/main/java/com/hlandim/marvelheroes/view/details/HeroActivity.kt) -> Hero details (Name, Photo, List of participations) and Favorite/Unfavorite hero.

Fragments: 
- [HeroesFragment](app/src/main/java/com/hlandim/marvelheroes/view/list/HeroesFragment.kt) -> List heroes, favorites and search result.
- [ParticipationFragment](app/src/main/java/com/hlandim/marvelheroes/view/details/ParticipationFragment.kt) -> Show hero participation(comic, event, storie and series) details.

Adapters:
- [ParticipationAdapter](app/src/main/java/com/hlandim/marvelheroes/view/details/ParticipationAdapter.kt) -> Adapter for participations ListView 
- [HeroesAdapter](app/src/main/java/com/hlandim/marvelheroes/view/list/HeroesAdapter.kt) -> Adapter with pagination for heroes list.

ViewModel:
- [HeroesViewModel](app/src/main/java/com/hlandim/marvelheroes/viewmodel/HeroesViewModel.kt) -> Responsable for get heroes list, search hero feature. This class use LiveData/MutableLiveData so the changes can be reflected automatic on layout. Do comunications with repository.
- [HeroViewModel](app/src/main/java/com/hlandim/marvelheroes/viewmodel/HeroViewModel.kt) -> Responsable for keep the hero used in Hero details screen, Favorite/Unfavorite repository request.

Database:
- [HeroesRepository](app/src/main/java/com/hlandim/marvelheroes/database/HeroesRepository.kt) -> Responsable for communication with [MarvelAPI](https://developer.marvel.com/) and Room database abstration library.
- [AppDataBase](app/src/main/java/com/hlandim/marvelheroes/database/AppDataBase.kt) -> Room database configuration.
- [Converter](app/src/main/java/com/hlandim/marvelheroes/database/Converter.kt) -> Keep the converters methods usaded in database one-to-many relationship.

Web:
- [MarvelApi](app/src/main/java/com/hlandim/marvelheroes/web/mavel/MarvelApi.kt) -> Interface responsable for retrofit configuration and Marvel Api endpoints.
- [HeroesService](app/src/main/java/com/hlandim/marvelheroes/web/mavel/HeroesService.kt) -> Responsable for use MarvelApi to make call to the endpoints.

Tests:
- [HeroesViewModelTest](app/src/test/java/com/hlandim/marvelheroes/viewmodel/HeroesViewModelTest.kt) -> Tests for getHeroes, searchHero and showFavoritesHeroes
- [HeroViewModelTest](app/src/test/java/com/hlandim/marvelheroes/viewmodel/HeroViewModelTest.kt) -> Tests for mark/unmark favorite hero.


