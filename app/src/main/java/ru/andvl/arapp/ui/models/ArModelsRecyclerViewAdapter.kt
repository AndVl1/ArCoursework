package ru.andvl.arapp.ui.models

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.github.terrakok.cicerone.Router
import ru.andvl.arapp.R

import ru.andvl.arapp.models.ModelsListContent.ListItem
import ru.andvl.arapp.ui.ar.ArActivity

/**
 * [RecyclerView.Adapter] that can display a [ListItem].
 */
class ArModelsRecyclerViewAdapter(
    private val values: List<ListItem>,
    private val router: Router
) : RecyclerView.Adapter<ArModelsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.models_list_element, parent, false)
        return ViewHolder(view, router)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(private val view: View, val router: Router) : RecyclerView.ViewHolder(view) {

        private val imagePreview: ImageView = view.findViewById(R.id.item_preview)
        private val modelNameView: TextView = view.findViewById(R.id.modelName)

        fun bind(item: ListItem) {
            imagePreview.load(item.gifLink)
            modelNameView.text = item.name
            view.setOnClickListener {
                val intent = Intent(view.context, ArActivity::class.java)
                intent.putExtra(MODEL_LINK_EXTRA, item.modelLink)
                view.context.startActivity(intent)
//                don't know how to make it with single activity :c
//                router.navigateTo(Screens.camera(item.modelLink), false)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + modelNameView.text + "'"
        }
    }
    companion object {
        private const val MODEL_LINK_EXTRA = "MODEL_LINK"
    }
}
