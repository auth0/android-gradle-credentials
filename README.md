# Auth0 Android Gradle-Credentials Plugin

This plugin generates an XML file with the required Auth0 credentials. It creates a new a Task `generate{buildVariant}Auth0Credentials` to generate the String resources in each build folder. This task attaches automatically to the Build process, and can handle incremental builds.

## Compile

1. Clone or download this project.
2. Run `./gradlew clean build install` so the plugin installs in the **mavenLocal()** repository.

## Setup

First you need to add the plugin dependency. Go to your project's `build.gradle` file and add:

```groovy
buildscript {
    repositories {
        mavenLocal()
        //...
    }
    dependencies {
        classpath 'com.auth0.android:gradle-credentials:1.0.0'
        //...
    }
}
```

Go to your application's `build.gradle` file and apply the **gradle-credentials** plugin after the **android.application** plugin.

```groovy
apply plugin: 'com.android.application'
apply plugin: 'com.auth0.android.gradle-credentials'

// ...
```

Next, the plugin will try to find the **Auth0 Credentials** in one of the following locations in this order:

1. First in the Project's `local.properties` file. This is the **RECOMMENDED** location. If the file doesn't contain the `auth0.clientId` or the `auth0.domain` keys, it will try to search them in the next location.
2. The Application's Module `build.gradle` file.

The advantage of using the `local.properties` file over the `build.gradle` approach is that as this file shouldn't be commited/pushed to your repository, the credentials will always remain private.

> If by any reason you add the `auth0` keys in the `local.properties` file, it will ignore the values defined in the `build.gradle` file.

** The plugin will throw an exception if the credentials couldn't be found at any location. **

### A) Add the Credentials [RECOMMENDED WAY] 

In your project's `local.properties` file, add the following `auth0` key/values:

```groovy
sdk.dir=/usr/local/opt/android-sdk

// ...

auth0.clientId=1Wu12gnhRRYh31v9SfB3c6I3bIJdRIze     //Auth0 Client ID
auth0.domain=lbalmaceda.auth0.com                   //Auth0 Domain
```

> If the `local.properties` file doesn't exist, try to **sync** or **build** your project and the Android Studio IDE will generate it for you.
 

### B) Add the Credentials 

In your application's `build.gradle` file, add the `auth0` closure with the `clientId` and `domain` key/values.

```groovy
// Plugins are applied here

android {
    // ...
}

dependencies {
    // ...
}

// ...

auth0 {
    clientId='1Wu12gnhRRYh31v9SfB3c6I3bIJdRIze'   //Auth0 Client ID
    domain='domain.auth0.com'                     //Auth0 Domain
}
```


## Access the Credentials
After you **build** your project or [manually run](#run-the-plugin) the Auth0Credentials task, the credentials will be available in the Android Resources. Access them with the `R` constant:

```
`String clientId = getResources().getString(R.string.com_auth0_client_id);`
`String domain = getResources().getString(R.string.com_auth0_domain);`
```


## Run the Plugin
The plugin runs automatically with each project **build**. If you want, you can manually run the task by calling `./gradlew generate{buildVariant}Auth0Credentials` with your preferred buildVariant.

```groovy
~/MyAndroidProject ❯❯❯ ./gradlew generateDebugAuth0Credentials

Parallel execution is an incubating feature.
Incremental java compilation is an incubating feature.
Auth0Credentials: Searching in build.gradle:auth0 closure
Auth0Credentials: Using ClientID=1Wu12gnhRRYh31v9SfB3c6I3bIJdRIze and Domain=lbalmaceda.auth0.com
:app:generateDebugAuth0Credentials UP-TO-DATE

BUILD SUCCESSFUL

Total time: 2.732 secs
```


The output will be located in the `build/intermediates/res/merged/{VARIANT}/values/auth0.xml`.


License
-------
This project is licensed under the [MIT License](LICENSE).