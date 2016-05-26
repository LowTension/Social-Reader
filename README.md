# Social Reader
Android application that grabs interesting articles from VK posts.

## Features
* View list of available articles
* Load new articles from VK
* Read articles, even offline
* Delete articles
* Archive articles

## Build
Open this project in Android Studio or Intellij IDEA.

## How it works
Posts are loading via VK SDK. Sources of posts are stored in [sources.xml](../master/app/src/main/res/values/sources.xml). Then filters are applied to each post. Example of filter: article should be more than 1000 characters long. If post passes all filters then it becomes an article and added to article list to show it to user.
