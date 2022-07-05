# Recipe App
(WORK IN PROGRESS)

An Android app useful for saving recipes.

## Main features

### Saving a recipe

<img src="/docs/images/add_recipe_backdrop.png" alt="Save recipe backdrop" width="200"/> <img src="/docs/images/add_recipe_categories.png" alt="Save recipe categories" width="200"/>

### Recipe list


<img src="/docs/images/recipe_list.png" alt="Recipe list" width="200"/> <img src="/docs/images/recipe_list_filters.png" alt="Recipe list filters" width="200"/>

### Recipe details

<img src="/docs/images/recipe_detail.png" alt="Recipe details" width="200"/> 

### Preparation and cooking instructions

<img src="/docs/images/cooking.png" alt="Cooking" width="200"/>

## Architecture

The app is structured into 3 modules: app, domain and data.

The app module corresponds to the UI layer. The views are written in [Jetpack Compose](https://developer.android.com/jetpack/compose). The have a View Model associated.  
The View Model exposes the state to the view as a [StateFlow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/).

The domain module contains the use cases, models and the interfaces for the repositories.

The data module contains the implementations for the repositories and the data sources. The data sources are implemented using [SQLDelight](https://cashapp.github.io/sqldelight/) and [Proto DataStore](https://developer.android.com/topic/libraries/architecture/datastore#proto-datastore).

The dependency injection is realized with [Hilt](https://dagger.dev/hilt/).