package aplicacion.principal

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrarUsuario : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var campoNuevoCorreo: EditText
    private lateinit var campoNuevaContraseña: EditText
    private lateinit var campoConfirmarContraseña: EditText
    private lateinit var btnRegistrarse: Button
    private lateinit var txtCorreoValido: TextView
    private lateinit var txtContraseñaValida: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)

        val actionBar: ActionBar? = supportActionBar

        if (actionBar != null) {
            //Poner el ícono al ActionBar
            actionBar.setIcon(R.mipmap.ic_launcher_foreground)
            actionBar.setDisplayShowHomeEnabled(true)
        }

        auth = Firebase.auth

        campoNuevoCorreo = findViewById(R.id.campoNuevoCorreo)
        campoNuevaContraseña = findViewById(R.id.campoNuevaContraseña)
        campoConfirmarContraseña = findViewById(R.id.campoConfirmarContraseña)
        btnRegistrarse = findViewById(R.id.btnRegistrarse)
        txtCorreoValido = findViewById(R.id.txtCorreoValido)
        txtContraseñaValida = findViewById(R.id.txtContraseñaValida)

        btnRegistrarse.isEnabled = false

        campoNuevoCorreo.addTextChangedListener(watcher)
        campoNuevaContraseña.addTextChangedListener(watcher)
        campoConfirmarContraseña.addTextChangedListener(watcher)

        btnRegistrarse.setOnClickListener {
            val correo = campoNuevoCorreo.text.toString()
            val contraseña = campoNuevaContraseña.text.toString()
            crearCuenta(correo, contraseña)
        }
    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val correoValido = isValidEmail(campoNuevoCorreo.text.toString())
            val contrasenaValida = campoNuevaContraseña.text.length >= 6
            val contrasenasCoinciden =
                campoNuevaContraseña.text.toString() == campoConfirmarContraseña.text.toString()

            if (correoValido){
                txtCorreoValido.setText("")
            }else{
                txtCorreoValido.setText("Ingrese un correo válido")
            }
            if (!contrasenaValida){
                txtContraseñaValida.setText("La contraseña debe tener al menos 6 caracteres")
            } else if (!contrasenasCoinciden){
                txtContraseñaValida.setText("Las contraseñas no coinciden")
            } else
                txtContraseñaValida.setText("")

            btnRegistrarse.isEnabled = correoValido && contrasenaValida && contrasenasCoinciden
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    // Función para validar el formato del correo electrónico
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun crearCuenta(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Error de autenticación.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    //Hacer algo si sale mal, o no hacer nada :D
                }
            }
        // [END create_user_with_email]
    }
}