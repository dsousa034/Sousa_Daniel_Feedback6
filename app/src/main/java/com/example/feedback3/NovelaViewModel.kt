package com.example.feedback3

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NovelaViewModel : ViewModel() {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("novelas")
    private val usersDatabase: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    private val _novelas = MutableStateFlow<List<Novela>>(emptyList())
    val novelas = _novelas.asStateFlow()

    init {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val novelasList = mutableListOf<Novela>()
                snapshot.children.forEach { data ->
                    data.getValue(Novela::class.java)?.let { novelasList.add(it) }
                }
                _novelas.value = novelasList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("NovelaViewModel", "Database error: ${error.message}")
            }
        })
    }

    fun agregarNovela(novela: Novela) {
        database.child(novela.id.toString()).setValue(novela)
    }

    fun eliminarNovela(id: String) {
        database.child(id).removeValue()
    }

    fun toggleFavorito(id: String, esFavorita: Boolean) {
        database.child(id).child("esFavorita").setValue(!esFavorita)
    }

    fun registrarUsuario(nombre: String, apellido: String) {
        val userId = usersDatabase.push().key ?: return
        val usuario = Usuario(id = userId, nombre = nombre, apellido = apellido)
        usersDatabase.child(userId).setValue(usuario)
    }
}