package ru.bmstu.iu9.vrsocialnetwork.data

import com.google.firebase.auth.FirebaseAuth

data class Account(val mAuth: FirebaseAuth?, val mProfileImageLink: String) {

}