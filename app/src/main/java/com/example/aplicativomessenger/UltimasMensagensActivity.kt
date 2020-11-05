package com.example.aplicativomessenger

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class UltimasMensagensActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ultimas_mensagens)

        verifiqueUsuarioEstaLogado()

        }
    private fun verifiqueUsuarioEstaLogado(){
    val uid = FirebaseAuth.getInstance().uid
    if (uid == null){
        val intent = Intent (this, CadastroActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
  }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.novas_mensagens -> {
val intent = Intent (this, NovasMensagensActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_log_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, CadastroActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
        }
            return super.onOptionsItemSelected(item)
    }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            menuInflater.inflate(R.menu.menu_navegacao, menu)
            return super.onCreateOptionsMenu(menu)

        }
    }
