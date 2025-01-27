package adapter.listener

import com.example.projeto_16.Model.ContactModel

class ContactOnClickListener (val clickListener: (contactModel: ContactModel) -> Unit){
    fun onClick(contact: ContactModel) = clickListener
}