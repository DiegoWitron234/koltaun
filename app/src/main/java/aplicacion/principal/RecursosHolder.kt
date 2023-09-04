package aplicacion.principal

import android.content.DialogInterface.OnClickListener
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

class RecursosHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imgRecurso: ImageButton = view.findViewById(R.id.imgRecurso)
    fun render(atributo: CargaRecursos, onClickListener: (CargaRecursos) -> Unit) {
        val etiquetaRecurso = atributo.etiquetaRecurso
        val idRecurso = itemView.resources.getIdentifier(
            etiquetaRecurso,
            "mipmap",
            itemView.context.packageName
        )
        imgRecurso.setBackgroundResource(idRecurso)
        imgRecurso.setOnClickListener { onClickListener(atributo) }
    }
}