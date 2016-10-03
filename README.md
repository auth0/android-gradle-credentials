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

Next, the plugin will try to find the **Auth0 Credentials** in one of the following locations and respecting this order:

1. Application's Module `build.gradle` file.
2. Project's `local.properties` file. **(RECOMMENDED)**

The advantage of using the `local.properties` file over the `build.gradle` approach is that as this file shouldn't be pushed to your repository, the credentials will remain private.

> If by any reason you add the `auth0` closure to your `build.gradle` file, it will ignore the values configured in the `local.properties` file.



### A) Credentials from the `build.gradle` file

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
    clientId='Owu62gnGsRYhk1v9SfB3c6IUbIJcRIze'   //Auth0 Client ID
    domain='domain.auth0.com'                     //Auth0 Domain
}
```

### B) Credentials from the `local.properties` file

In your project's `local.properties` file, add the following `auth0` key/values:

```groovy
sdk.dir=/usr/local/opt/android-sdk

// ...

auth0.clientId=Owu62gnGsRYhk1v9SfB3c6IUbIJcRIze     //Auth0 Client ID
auth0.domain=lbalmaceda.auth0.com                   //Auth0 Domain
```

> If the `local.properties` file doesn't exist, try to **sync** or **build** your project and the Android Studio IDE will generate it for you.
 

## Access the Credentials
After you **build** your project or [manually run](#run-the-plugin) the Auth0Credentials task, the credentials will be available in the Android Resources. Access them with the `R` constant:

```
`String clientId = getResources().getString(R.string.com_auth0_client_id);`
`String domain = getResources().getString(R.string.com_auth0_domain);`
```

> If the credentials couldn't be generated, because they weren't specified either in the `local.properties` file or in the `build.gradle` file, they will default to the `{CLIENT_ID}` and `{DOMAIN}` placeholders.


## Run the Plugin
The plugin runs automatically with each project **build**. If you want, you can manually run the task by calling `./gradlew generate{buildVariant}Auth0Credentials` with your preferred buildVariant.

```groovy
~/MyAndroidProject ❯❯❯ ./gradlew generateDebugAuth0Credentials

Parallel execution is an incubating feature.
Incremental java compilation is an incubating feature.
Auth0Credentials: Searching in build.gradle:auth0 closure
Auth0Credentials: Using ClientID=Owu62gnGsRYhk1v9SfB3c6IUbIJcRIze and Domain=lbalmaceda.auth0.com
:app:generateDebugAuth0Credentials UP-TO-DATE

BUILD SUCCESSFUL

Total time: 2.732 secs
```


The output will be located in the `build/intermediates/res/merged/{VARIANT}/values/auth0.xml`.


License
-------
This project is licensed under the [MIT License](LICENSE).