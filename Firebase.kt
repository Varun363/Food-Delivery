package Zomato.Copy

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Using 'by lazy' avoids the memory leak warning
val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

data class User(
    val name: String = "",
    val email: String = "",
    val password: Int = 0,
    val confirmPassword: Int = 0
)

fun saveUserToFirestore(user: User) {
    db.collection("users").add(user)
        .addOnSuccessListener {
            println("User saved to Firestore with ID: ${it.id}")
        }
        .addOnFailureListener {
            println("Error saving user to Firestore: ${it.message}")
        }
}
