package ru.mrfix1033.contentprovidercontent.fragments

import android.Manifest
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.mrfix1033.contentprovidercontent.R
import ru.mrfix1033.contentprovidercontent.adapters.RecyclerViewContactAdapter
import ru.mrfix1033.contentprovidercontent.databinding.FragmentMainBinding
import ru.mrfix1033.contentprovidercontent.fragments.enumerations.Key
import ru.mrfix1033.contentprovidercontent.models.ContactModel
import ru.mrfix1033.contentprovidercontent.utils.FragmentReplacer
import ru.mrfix1033.contentprovidercontent.utils.PermissionUtils
import ru.mrfix1033.contentprovidercontent.utils.TelephonyUtils
import ru.mrfix1033.contentprovidercontent.utils.notifyObserver
import ru.mrfix1033.contentprovidercontent.viewmodels.ContactsViewModel

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactsViewModel: ContactsViewModel
    private lateinit var recyclerViewAdapter: RecyclerViewContactAdapter
    private lateinit var permissionUtils: PermissionUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentReplacer = requireActivity() as FragmentReplacer

        permissionUtils = PermissionUtils(this)

        contactsViewModel = ViewModelProvider(this)[ContactsViewModel::class.java]
        contactsViewModel.contacts.observe(viewLifecycleOwner) {
            recyclerViewAdapter.notifyItemInserted(0)
//            recyclerViewAdapter.notifyDataSetChanged()  // TODO
        }

        val toastPermissionDenied = {
            Toast.makeText(
                requireContext(),
                "Не предоставлено необходимое разрешение",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewAdapter = RecyclerViewContactAdapter(
            requireContext(),
            contactsViewModel.contacts.value!!,
            { contactModel ->
                permissionUtils.Check_Request_Execute(
                    requireContext(),
                    Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE
                ) { isGranted ->
                    if (!isGranted) toastPermissionDenied()
                    val phoneNumber = contactModel.phoneNumber
                    if (phoneNumber == null) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.phone_number_is_null),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Check_Request_Execute
                    }
                    TelephonyUtils(requireContext()).startCall(phoneNumber)
                }
            },
            { contactModel ->
                permissionUtils.Check_Request_Execute(
                    requireContext(),
                    Manifest.permission.SEND_SMS
                ) { isGranted ->
                    if (!isGranted) toastPermissionDenied()
                    val phoneNumber = contactModel.phoneNumber
                    if (phoneNumber == null) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.phone_number_is_null),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Check_Request_Execute
                    }
                    val smsFragment = SMSFragment()
                        .apply { arguments = bundleOf(Key.contactModel to contactModel) }
                    fragmentReplacer.replace(smsFragment)
                }
            })
        binding.recyclerView.adapter = recyclerViewAdapter

        permissionUtils.Check_Request_Execute(
            requireContext(),
            Manifest.permission.READ_CONTACTS
        ) { isGranted ->
            if (isGranted) fillContacts()
            else toastPermissionDenied()
        }
    }

    fun fillContacts() {
        requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null
        )!!.use {
            while (it.moveToNext()) {
                val contactModel = ContactModel(
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                )
                contactsViewModel.contacts.value!!.add(contactModel)
            }
            contactsViewModel.contacts.value!!.sortBy { contactModel -> contactModel.name }
            contactsViewModel.contacts.notifyObserver()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}