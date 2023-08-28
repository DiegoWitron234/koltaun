package aplicacion.principal

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
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
    private lateinit var checkboxAceptar: CheckBox

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

        checkboxAceptar = findViewById(R.id.checkboxAceptar)

        val termsAndPrivacyTextView = findViewById<TextView>(R.id.termsAndPrivacyTextView)

        val message = "Confirma que has leído y aceptas los términos y condiciones y el aviso de privacidad."

        val spannableString = SpannableString(message)

        // Definir los clics en los enlaces
        val termsClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                openWebPage("https://drive.google.com/file/d/1bRcUV6G8qncZ0i-nLLMNvfMKFbNok2UB/view?usp=sharinghttps://drive.google.com/file/d/1bRcUV6G8qncZ0i-nLLMNvfMKFbNok2UB/view?usp=sharing")
            }
        }

        val privacyClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                openWebPage("https://drive.google.com/file/d/188t2xfCTc9XaVemcxVrBvnrCuvA5QEcn/view?usp=drive_link")
            }
        }

        // Aplicar los estilos y enlaces a las partes del texto
        spannableString.setSpan(termsClickableSpan, 37, 59, 0)
        spannableString.setSpan(privacyClickableSpan, 65, 84, 0)

        termsAndPrivacyTextView.text = spannableString
        termsAndPrivacyTextView.movementMethod = LinkMovementMethod.getInstance()

        btnRegistrarse.setOnClickListener {
            val correo = campoNuevoCorreo.text.toString()
            val contraseña = campoNuevaContraseña.text.toString()
            if (checkboxAceptar.isChecked){
                crearCuenta(correo, contraseña)
                //Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this, "Debe aceptar los términos y condiciones", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
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