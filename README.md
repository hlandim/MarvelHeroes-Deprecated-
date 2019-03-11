# MarvelHeroes

Architecture
![alt tag](/imgs/googleArchitecture.png)

All cominication between View <-> ViewModel are made using DataBinding. 

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
