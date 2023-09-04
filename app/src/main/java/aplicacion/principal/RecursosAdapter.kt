package aplicacion.principal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecursosAdapter(
    private val listaRecursos: List<CargaRecursos>,
    private val onClickListener: (CargaRecursos) -> Unit
) : RecyclerView.Adapter<RecursosHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecursosHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RecursosHolder(layoutInflater.inflate(R.layout.layout_recursos, parent, false))
    }

    override fun getItemCount() = listaRecursos.size

    override fun onBindViewHolder(holder: RecursosHolder, position: Int) {
        val item = listaRecursos[position]
        holder.render(item, onClickListener)
    }

}