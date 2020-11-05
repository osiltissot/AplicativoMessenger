package com.example.aplicativomessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_cadastro.*
import java.util.*

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)


        register_button_register.setOnClickListener {
            fazerCadastro()
        }
        already_have_account.setOnClickListener {
            Log.d("CadastroActivity", "Tente mostrar a Login Activity")

            val intent = Intent (this, LoginActivity::class.java)
            startActivity(intent)
        }
        button_selecionefoto_teladecadastro.setOnClickListener{
            Log.d("Cadastro", "Tente mostrar as opções de fotos")


            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }
    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

         if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Log.d("CadastroActivity", "A foto foi selecionada")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            //button_selecionefoto_teladecadastro.setImageBitmap(bitmap)
             button_selecionefoto_teladecadastro.alpha = 0f

            val bitMapDrawable = BitmapDrawable(bitmap)
           button_selecionefoto_teladecadastro.setBackgroundDrawable(bitMapDrawable)
        }
    }
    private fun fazerCadastro(){
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()
        val username = username_edittext_register.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Favor digitar email e senha", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("CadastroActivity", "Email e $email")
        Log.d("CadastroActivity", "Senha: $password " )

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    Log.d("Cadastro", "Usuario criado com sucesso com uid: ${it.result?.user?.uid}")
                    uploadImageFirebaseStorage()
                }
                .addOnFailureListener {
                    Log.d("Cadastro", "Não foi possível cadastrar o usuário: ${it.message}")
                    Toast.makeText(this, "Não foi possível criar o usuário", Toast.LENGTH_SHORT).show()
                }
    }
    private fun uploadImageFirebaseStorage(){
        if (selectedPhotoUri==null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("Cadastro", "Sucesso no upload da foto ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {
                        it.toString()
                        Log.d("CadastroActivity", "local do arquivo: $it")
                        salvarUsuarioBancoDeDadosFirebase(it.toString())
                    }
                }
                .addOnFailureListener {
                    //nada
                }
    }
    private fun salvarUsuarioBancoDeDadosFirebase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, username_edittext_register.text.toString(),profileImageUrl)
        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("Cadastro", "Salvamos o usuário no DB Firebase")

             val intent = Intent (this, UltimasMensagensActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }
    }
}
class User (val uid: String, val username: String, val profileImageUrl: String) {
    constructor() : this ("", "", "")
}