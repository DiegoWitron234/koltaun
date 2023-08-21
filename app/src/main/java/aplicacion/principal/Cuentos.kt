package aplicacion.principal

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Cuentos.newInstance] factory method to
 * create an instance of this fragment.
 */
class Cuentos : Fragment() {

    private lateinit var tono1: MediaPlayer
    private lateinit var tono2: MediaPlayer
    private lateinit var tono3: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cuentos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tono1 = MediaPlayer.create(requireContext(), R.raw.tono)
        tono2 = MediaPlayer.create(requireContext(), R.raw.tono2)
        tono3 = MediaPlayer.create(requireContext(), R.raw.tono3)

        val imageButton1: ImageButton = view.findViewById(R.id.imgButton1)
        val imageButton2: ImageButton = view.findViewById(R.id.imgButton2)
        val imageButton3: ImageButton = view.findViewById(R.id.imgButton3)

        imageButton1.setOnClickListener {
            val idImagen = R.drawable.dibujotigrecachorro
            val idCancion = R.raw.eltigreyelcachorro
            val intent = Intent(context, AudioPlayerActivity::class.java)
            intent.putExtra("audioResourceId", idCancion)
            intent.putExtra("imageResourceId", idImagen)
            startActivity(intent)
        }

        imageButton2.setOnClickListener {
            val idImagen = R.drawable.dibujoconejo
            val idCancion = R.raw.cuentoconejo
            val intent = Intent(context, AudioPlayerActivity::class.java)
            intent.putExtra("audioResourceId", idCancion)
            intent.putExtra("imageResourceId", idImagen)
            startActivity(intent)
        }

        imageButton3.setOnClickListener {
            val idImagen = R.drawable.dibujoespantagentes
            val idCancion = R.raw.cuentoespantagente
            val intent = Intent(context, AudioPlayerActivity::class.java)
            intent.putExtra("audioResourceId", idCancion)
            intent.putExtra("imageResourceId", idImagen)
            startActivity(intent)
        }
    }

    private fun reproducirTono(tono: MediaPlayer) {
        // Verifica si el audio ya está reproduciendo y, si es así, detén la reproducción y reinicia
        if (tono.isPlaying) {
            tono.seekTo(0)
        } else {
            tono.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Libera los recursos de MediaPlayer cuando el fragmento se destruya para evitar pérdidas de memoria
        tono1.release()
        tono2.release()
        tono3.release()
    }

}