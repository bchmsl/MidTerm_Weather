package com.bchmsl.midterm_weather.firebase

import android.net.Uri
import com.bchmsl.midterm_weather.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import java.io.File

object Firebase {
    val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Users")

    fun signIn(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)

    fun signUp(email: String, password: String) =
        firebaseAuth.createUserWithEmailAndPassword(email, password)

    fun addToDatabase(uid: String, user: User): Task<Void> =
        databaseReference.child(uid).setValue(user)

    fun uploadProfilePic(uid: String?, imageUri: Uri?) =
        FirebaseStorage.getInstance().getReference("Users/$uid").putFile(imageUri!!)

    fun getUserInfo(uid: String) =
        databaseReference.child(uid).get()

    fun getUserImage(uid: String, file: File): FileDownloadTask {
        val storageReference = FirebaseStorage.getInstance().getReference("Users/$uid")
        return storageReference.getFile(file)
    }

    fun updateUserInfo(uid: String, firstName: String, lastName: String, imageUri: Uri){
        val currentUserInfo = databaseReference.child(uid)
        currentUserInfo.child("firstName").setValue(firstName)
        currentUserInfo.child("lastName").setValue(lastName)
        val storageReference = FirebaseStorage.getInstance().getReference("Users/$uid")
        storageReference.putFile(imageUri)
    }

    fun resetPassword(email: String) = firebaseAuth.sendPasswordResetEmail(email)

    fun emailVerificationStatus() = firebaseAuth.currentUser?.isEmailVerified

    fun sendEmailVerification() = firebaseAuth.currentUser?.sendEmailVerification()

    fun signOut() = firebaseAuth.signOut()

}