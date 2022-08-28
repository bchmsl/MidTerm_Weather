package com.bchmsl.midterm_weather.utils.firebase

import android.net.Uri
import com.bchmsl.midterm_weather.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import java.io.File

object Firebase {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Users")

    fun signIn(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)

    fun signUp(email: String, password: String) =
        firebaseAuth.createUserWithEmailAndPassword(email, password)

    fun addToDatabase(user: User): Task<Void>? =
        firebaseAuth.currentUser?.uid?.let { databaseReference.child(it).setValue(user) }

    fun uploadProfilePic(imageUri: Uri?) =
        FirebaseStorage.getInstance().getReference("Users/${firebaseAuth.currentUser?.uid}")
            .putFile(imageUri!!)

    fun getUserInfo() =
        firebaseAuth.currentUser?.uid?.let { databaseReference.child(it).get() }

    fun getUserImage(file: File): FileDownloadTask {
        val storageReference =
            FirebaseStorage.getInstance().getReference("Users/${firebaseAuth.currentUser?.uid}")
        return storageReference.getFile(file)
    }

    fun updateUserInfo(firstName: String, lastName: String, imageUri: Uri) {
        val uid = firebaseAuth.currentUser?.uid
        val currentUserInfo = uid?.let { databaseReference.child(it) }
        currentUserInfo?.child("firstName")?.setValue(firstName)
        currentUserInfo?.child("lastName")?.setValue(lastName)
        val storageReference = FirebaseStorage.getInstance().getReference("Users/$uid")
        storageReference.putFile(imageUri)
    }

    fun resetPassword(email: String) = firebaseAuth.sendPasswordResetEmail(email)

    fun emailVerificationStatus() = firebaseAuth.currentUser?.isEmailVerified

    fun sendEmailVerification() = firebaseAuth.currentUser?.sendEmailVerification()

    fun signOut() = firebaseAuth.signOut()

    fun getUserFirstName(): Task<DataSnapshot>? {
        val uid = firebaseAuth.currentUser?.uid
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        return uid?.let { databaseReference.child(it).get() }
    }

}