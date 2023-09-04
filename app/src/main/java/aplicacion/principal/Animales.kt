package aplicacion.principal

import android.annotation.SuppressLint
import android.content.res.Resources
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class Animales : AppCompatActivity() {

    private val sonidos = mutableListOf<MediaPlayer>()
    private lateinit var mAdView: AdView
    private var tono: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animales)

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val recursoLista = mutableListOf<CargaRecursos>()
        val stringRecurso = resources.getStringArray(R.array.animalesLista).toList()

        for (color in stringRecurso){
            recursoLista.add(CargaRecursos(color))
        }

        val manager = LinearLayoutManager(this)
        val decoracion = DividerItemDecoration(this, manager.orientation)
        val recyclerView = findViewById<RecyclerView>(R.id.rvAnimales)
        recyclerView.layoutManager = manager
        recyclerView.adapter = RecursosAdapter(recursoLista) { reproducirTono(it) }
        recyclerView.addItemDecoration(decoracion)
    }

    @SuppressLint("DiscouragedApi")
    private fun reproducirTono(recurso: CargaRecursos) {
        val nombreRecursos = recurso.etiquetaRecurso
        val idRecurso = resources.getIdentifier(nombreRecursos, "raw", packageName)
        try {
            if (tono != null) {
                if (tono!!.isPlaying) {
                    tono!!.seekTo(0)
                } else {
                    tono!!.start()
                }
            } else {
                // Crear una nueva instancia si tono es nulo
                tono = MediaPlayer.create(this, idRecurso)
                sonidos.add(tono!!)
                tono!!.start()
            }
        } catch (_: Resources.NotFoundException) {}
    }
    override fun onPause() {
        mAdView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mAdView.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdView.destroy()
        for (tono in sonidos) {
            tono.release()
        }
    }
}