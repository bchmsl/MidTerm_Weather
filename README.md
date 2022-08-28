
Weather App

This is a weather app. It shows forecast of upcoming 7 days including today's. It also has user authentification system.


## Features

- Fahrenheit/Celsius Toggle
- 7 Days Forecast with RecyclerVeiw
- Page with Detailed Information
- Choosing City with DataStore
- Networking with Retrofit, Moshi
- Firebase Authentification, Realtime Database, Storage
- Splash Screen
## Used Dependencies

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
![SignIn](https://user-images.githubusercontent.com/85778941/187092626-7189c7a5-a4f7-4b89-adfa-c992a77c6d56.jpg)

## Signup Pages
![SingUp](https://user-images.githubusercontent.com/85778941/187092637-9851ab6a-3dca-46e4-87d3-4e0b030f330f.jpg)
![SignUpContinue](https://user-images.githubusercontent.com/85778941/187092643-733874e0-f06b-46c5-8bdd-b776008ca158.jpg)

## Main Page
![Man Page](https://user-images.githubusercontent.com/85778941/187092654-c747edb6-353e-4f72-86c4-2c3206ff1cff.jpg)

## Detailed Forecast Page
![Detailed Forecast](https://user-images.githubusercontent.com/85778941/187092749-42e74d95-b642-4bbb-b992-50de71a61e55.jpg)

## Preferences Pages
![Preferences](https://user-images.githubusercontent.com/85778941/187092700-559d783b-545d-4d10-a51e-c2c41ae594d3.jpg)
![PreferencesUserInfoCange](https://user-images.githubusercontent.com/85778941/187092793-77848c44-6b74-484b-ad53-d6a57d5775b4.jpg)
![City Chooser](https://user-images.githubusercontent.com/85778941/187092781-6b877c12-7d30-4ae1-805f-266d19c3b4a5.jpg)

