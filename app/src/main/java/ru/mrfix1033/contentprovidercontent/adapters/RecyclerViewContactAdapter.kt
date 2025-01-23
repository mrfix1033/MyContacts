package ru.mrfix1033.contentprovidercontent.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mrfix1033.contentprovidercontent.databinding.ContactItemBinding
import ru.mrfix1033.contentprovidercontent.models.ContactModel

class RecyclerViewContactAdapter(
    private val context: Context,
    private val contacts: List<ContactModel>,
    private val callAction: (ContactModel) -> Unit,
    private val smsAction: (ContactModel) -> Unit
) :
    RecyclerView.Adapter<RecyclerViewContactAdapter.ContactViewHolder>() {
    inner class ContactViewHolder(val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ContactItemBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.binding.run {
            val contactModel = contacts[position]

            textViewName.text = contactModel.name
            textViewPhoneNumber.text = contactModel.phoneNumber

            imageViewCall.setOnClickListener {
                callAction(contactModel)
            }
            imageViewSMS.setOnClickListener {
                smsAction(contactModel)
            }
        }
    }
}
