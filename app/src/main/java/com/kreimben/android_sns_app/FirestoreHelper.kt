package com.kreimben.android_sns_app

import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreHelper {
    private var db: FirebaseFirestore = Firebase.firestore
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val defaultProfileImg = "https://firebasestorage.googleapis.com/v0/b/android-sns-app.appspot.com/o/profile_images%2FN3jquxgS7rSLhI0M1KfgBieZVP73_Sat%20Nov%2012%2010%3A22%3A57%20GMT%202022?alt=media&token=248542a1-7855-417b-89f2-7ee739688c72"
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
        var currentUserImg = currentUser?.photoUrl
        if(currentUserImg == null ){
            currentUserImg= Uri.parse(defaultProfileImg)
        }
        db.collection("user").document(currentUser!!.uid).set(
            hashMapOf(
                "email" to currentUser.email,
                "uid" to currentUser.uid,
                "displayname" to currentUser.displayName,
                "following" to null,
                "photourl" to currentUserImg
            )
        ).addOnCompleteListener {
            println("currentUser upload completed!")
        }.addOnFailureListener {
            println("currentUser upload failed!")
        }
    }

    fun updateFollower(followingUID: String, btn: Button) {
        val doc = db.collection("user").document(currentUser!!.uid)

        doc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                var f = it.result.data?.get("following")
                if (f == null) {
                    f = mutableListOf<String>()
                }

                Log.d(null, f.toString())
                if((f as MutableList<String>).contains(followingUID)){
                    (f as MutableList<String>).remove(followingUID)
                    btn.setText("Follow")
                    btn.setBackgroundColor(Color.parseColor("#2EFE9A"))
                    btn.setTextColor(Color.parseColor("#000000"))
                }
                else{
                    (f as MutableList<String>).add(followingUID)
                    btn.setText("UnFollow")
                    btn.setBackgroundColor(Color.parseColor("#050fff"))
                    btn.setTextColor(Color.parseColor("#FFFFFF"))
                }

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

    fun checkFollowing(followingUID: String, btn: Button) {
        val doc = db.collection("user").document(currentUser!!.uid)

        doc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                var f = it.result.data?.get("following")
                if (f == null) {
                    f = mutableListOf<String>()
                }
                Log.d(null, f.toString())
                if((f as MutableList<String>).contains(followingUID)){
                    btn.setText("UnFollow")
                    btn.setBackgroundColor(Color.parseColor("#050fff"))
                    btn.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else{
                    btn.setText("Follow")
                    btn.setBackgroundColor(Color.parseColor("#2EFE9A"))
                    btn.setTextColor(Color.parseColor("#000000"))
                }

                Log.d(null, f.toString())

            }
        }
    }



}

