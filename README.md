# Auth0 Android Gradle-Credentials Plugin

[![JitPack](https://jitpack.io/v/auth0/android-gradle-credentials.svg)](https://jitpack.io/#auth0/android-gradle-credentials/master-SNAPSHOT) [![Gradle](https://img.shields.io/badge/gradle--plugin-latest-orange.svg)](https://plugins.gradle.org/plugin/com.auth0.android.gradle-credentials) [![GitHub tag](https://img.shields.io/github/tag/auth0/android-gradle-credentials.svg)](https://github.com/auth0/android-gradle-credentials/releases) [![license](https://img.shields.io/github/license/mashape/apistatus.svg)](http://choosealicense.com/licenses/mit/)

This plugin generates an XML file with the required Auth0 credentials. It creates a new a Task `generate{buildVariant}Auth0Credentials` to generate the String resources in each build folder. This task attaches automatically to the Build process, and can handle incremental builds.

## Setup

### Using Gradle
This is the **RECOMMENDED** approach. Add the Gradle's Maven url to the buildscript/repositories list and also add the plugin to the buildscript/classpath inside the project's `build.gradle` file:g

```groovy
buildscript {
    repositories {
        jcenter()
        //...
        maven { url "https://plugins.gradle.org/m2/" }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.2.1'
        //...
        classpath 'gradle.plugin.com.auth0.android:gradle-credentials:1.0.0'
    }
}
```

Go to your application's `build.gradle` file and apply the **gradle-credentials** plugin after the **android.application** plugin.

```groovy
apply plugin: 'com.android.application'
apply plugin: 'com.auth0.android.gradle-credentials'

// ...
```


### Using JitPack

Add JitPack to the buildscript/repositories list and also add the plugin to the buildscript/classpath inside the project's `build.gradle` file:

```groovy
buildscript {
    repositories {
        jcenter()
        //...
        maven { url "https://jitpack.io" }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.2.1'
        //...
        classpath 'com.github.auth0:android-gradle-credentials:master-SNAPSHOT'
    }
}
```

Go to your application's `build.gradle` file and apply the **gradle-credentials** plugin after the **android.application** plugin.

```groovy
apply plugin: 'com.android.application'
apply plugin: 'com.auth0.android.gradle-credentials'

// ...
```


### Using a fresh local build

1. Clone or download this project.
2. Run `./gradlew clean build install` so the plugin installs in the **mavenLocal()** repository.

Add the plugin dependency. Go to your project's `build.gradle` file and add:

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

## How it Works

The plugin will try to find the **Auth0 Credentials** in one of the following locations in this order:

1. User's Home `local.properties` file. This is the **RECOMMENDED** location as you only need to setup once your credentials and it will work for every project.
2. Project's `local.properties` file. This location is also **RECOMMENDED** as this file shouldn't be committed/pushed to the repository, the credentials will always remain private.
3. The Application's Module `build.gradle` file.

** If either the `domain` or the `clientId` key/values are missing, the plugin will try to search for them in the next location. If they can't be found in the last location, it will throw an exception and won't let you build the project. **

### Credentials in a local.properties file

Find the `local.properties` file in your User's Home directory or in your Project's Home directory. Next, add the following `auth0` key/values:

```groovy
// ...

auth0.domain=lbalmaceda.auth0.com                   //Auth0 Domain
auth0.clientId=1Wu12gnhRRYh31v9SfB3c6I3bIJdRIze     //Auth0.Lock Client ID
auth0.guardian.domain=guardian-domain               //Auth0.Guardian Domain
```

> If the `local.properties` file doesn't exist in your Project's Home directory, try to **sync** or **build** your project and the Android Studio IDE will generate it for you. If you want to use your User's Home directory, you will need to create it manually.


### Credentials in a build.gradle file

Find your Application's `build.gradle` file. Next, add the `auth0` closure with `domain` and `lock.clientId` key/values.

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
    domain 'lbalmaceda.auth0.com'                 //Auth0 Domain
    clientId '1Wu12gnhRRYh31v9SfB3c6I3bIJdRIze'   //Auth0.Lock Client ID
    guardian {
        domain 'guardian-domain'                  //Auth0.Guardian Domain
    }
}
```


## Access the Credentials
After you **build** your project or [manually run](#run-the-plugin) the Auth0Credentials task, the credentials will be available in the Android Resources. Access them with the `R` constant:

```
`String domain = getResources().getString(R.string.com_auth0_domain);`
`String clientId = getResources().getString(R.string.com_auth0_client_id);`
`String guardianDomain = getResources().getString(R.string.com_auth0_guardian_domain);`
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
