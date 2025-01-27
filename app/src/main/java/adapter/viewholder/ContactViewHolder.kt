package adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto_16.R

class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val image = view.findViewById<ImageView>(R.id.image_contact)
    val textName = view.findViewById<TextView>(R.id.contact_name)
}