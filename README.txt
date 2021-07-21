This project 'NewsBreeze' follows an offline-first architecture with MVVM design pattern.
I am assuming that the 'BreakingNewsAPI' will be called only once when the app starts.
When the user navigates back to Home Screen, the API will not be called again when the user
navigates back from any other screen to Home Screen. The search feature will query the locally cached data.

Dependencies/Features Used -

Kotlin - Coding language
Room - For local storage and caching
Retrofit - For network calls
Dagger Hilt - For dependency injection
Fragment and Activity KTX - Viewmodel injection delegate (by viewModels())
Navigation Component - For navigation of fragments
Coroutines - Asysnchronous operations (DB and Network operations)
Lottie - For animations
Glide - Image URL loading
Material - For widgets (RecyclerView)
View Binding - Layout binding

Project Structure and Design -

This app has one activity (MainActivity) and three fragments (HomeFragment,SavedNewsFragment
,DetailNewsFragment). Each of the fragments has a unique viewmodel.There is one repository
class (MainRepository) which the viewmodels use for any db operation or the network call.
Repository, viewmodels and fragments are located in the 'ui' package.

Under the package 'di', there is a class called 'AppModule' from where the dependencies
are injected.

Under the 'data' package there are retrofit and room classes which are then build
as a dependency in the 'AppModule' class.

Under the 'adapter' class, there are adapter for RecyclerViews present in HomeFragment
and SavedNewsFragment. I have used 'ListAdapter' which allows to merge in our custom
DiffUtil class.

The search feature matches the 'title' of the cached news and according return the
results as a 'Flow' to the UI. To achieve 'search', I have used 'flatMapLatest' operator
of the Kotlin Flow API. Also, I have used Kotlin channels to communicate
from viewmodel to fragment, as Channels can be collected as a Flow from a lifecycle aware
'lifecyclescope'.

Lastly there is a 'SplashActivity' which shows a Lottie animation of a newspaper. 

NOTE : The 'BreakingNews' API provides only 200 characters for the 'content' of any news if
a person uses as a free account. That's why on 'DetailNewsFragment' you will see something
like '...[+2500 chars]' at the end of the content.