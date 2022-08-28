
Weather app

This is a weather app. It shows forecast of upcoming 7 days including todays'. It also has user authentification system.


## Features

- Fahrenheit/Celsius Toggle
- 7 Days Forecast with RecyclerVeiw
- Page with Detailed Information
- Choosing City with DataStore
- Networking with Retrofit, Moshi
- Firebase Authentification, Realtime Database, Storage
- Splash Screen
## Used dependencies

    // Retrofit
    def retrofitVersion = '2.9.0'
    implementation "com.squareup.retrofit2:retrofit:$retrofitVersion"
    implementation "com.squareup.retrofit2:converter-moshi:$retrofitVersion"

    // Glide
    def glideVersion = '4.13.2'
    implementation "com.github.bumptech.glide:glide:$glideVersion"
    annotationProcessor "com.github.bumptech.glide:compiler:$glideVersion"

    // Preferences Datastore
    def datastoreVersion = '1.0.0'
    implementation "androidx.datastore:datastore-preferences:$datastoreVersion"

    // Coroutines
    def coroutinesVersion = '1.6.4'
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"

    // Coroutine Lifecycles
    def lifecyclesVersion = '2.5.1'
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecyclesVersion"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecyclesVersion"

    // Fragment Navigation
    def navigationVersion = '2.5.1'
    implementation "androidx.navigation:navigation-fragment-ktx:$navigationVersion"
    implementation "androidx.navigation:navigation-ui-ktx:$navigationVersion"

    //Firebase
    implementation 'com.google.firebase:firebase-auth:21.0.7'
    implementation 'com.google.firebase:firebase-database:20.0.5'
    implementation 'com.google.firebase:firebase-storage:20.0.1'

    //Image Picker
    implementation 'com.github.dhaval2404:imagepicker:2.1'
    implementation "androidx.fragment:fragment-ktx:1.5.2"

    //Circle ImageView
    implementation 'de.hdodenhof:circleimageview:3.1.0'

    // Splash Screen
    implementation 'androidx.core:core-splashscreen:1.0.0'
## Login Page
## Signup Pages
## Main Page
## Detailed Forecast Page
## Preferences Pages