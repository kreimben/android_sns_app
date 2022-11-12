package com.kreimben.android_sns_app

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreHelper {
    private var db: FirebaseFirestore = Firebase.firestore

    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun updateProfile() {
        if (currentUser != null) {
            val doc = db.collection("user").document(currentUser.uid)

            doc.get()
                .addOnCompleteListener {

                    if (it.isSuccessful && it.result.exists()) {
                        doc.update("email", currentUser.email)
                            .addOnFailureListener {
                                Log.e(null, it.message.toString())
                            }

                        doc.update("displayname", currentUser.displayName)
                            .addOnFailureListener {
                                Log.e(null, it.message.toString())
                            }

                        doc.update("photourl", currentUser.photoUrl)
                            .addOnFailureListener {
                                Log.e(null, it.message.toString())
                            }

                    } else {
                        this.createProfile()
                    }
                }
        }
    }

    private fun createProfile() {
        db.collection("user").document(currentUser!!.uid).set(
            hashMapOf(
                "email" to currentUser.email,
                "uid" to currentUser.uid,
                "displayname" to currentUser.displayName,
                "following" to null,
                "photourl" to currentUser.photoUrl
            )
        ).addOnCompleteListener {
            println("currentUser upload completed!")
        }.addOnFailureListener {
            println("currentUser upload failed!")
        }
    }

    fun addFollower(followingUID: String) {
        val doc = db.collection("user").document(currentUser!!.uid)

        doc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                var f = it.result.data?.get("following")
                if (f == null) {
                    f = mutableListOf<String>()
                }

                Log.d(null, f.toString())
                (f as MutableList<String>).add(followingUID)
                Log.d(null, f.toString())

                doc.update(
                    "following", f
                )
                    .addOnFailureListener {
                        Log.e(null, it.message.toString())
                    }
            }
        }
    }

    fun removeFollower(followingUID: String) {
        val doc = db.collection("user").document(currentUser!!.uid)

        doc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                var f = it.result.data?.get("following")
                if (f != null) {
                    Log.d(null, f.toString())
                    (f as MutableList<String>).remove(followingUID)
                    Log.d(null, f.toString())

                    doc.update(
                        "following", f
                    )
                        .addOnFailureListener {
                            Log.e(null, it.message.toString())
                        }
                } else {
                    Log.e(null, "Nothing to remove! cuz your follower is nothing")
                }
            }
        }
    }
}