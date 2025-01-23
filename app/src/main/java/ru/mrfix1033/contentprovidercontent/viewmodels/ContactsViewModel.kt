package ru.mrfix1033.contentprovidercontent.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mrfix1033.contentprovidercontent.models.ContactModel

class ContactsViewModel : ViewModel() {
    val contacts:MutableLiveData<MutableList<ContactModel>> by lazy {
        MutableLiveData<MutableList<ContactModel>>().apply {
            value = mutableListOf()
        }
    }
}