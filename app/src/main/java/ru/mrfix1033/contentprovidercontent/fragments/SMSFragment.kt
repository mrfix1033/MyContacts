package ru.mrfix1033.contentprovidercontent.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.mrfix1033.contentprovidercontent.databinding.FragmentSMSBinding
import ru.mrfix1033.contentprovidercontent.fragments.enumerations.Key
import ru.mrfix1033.contentprovidercontent.models.ContactModel
import ru.mrfix1033.contentprovidercontent.utils.TelephonyUtils

class SMSFragment : Fragment() {
    private var _binding: FragmentSMSBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSMSBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contactModel = requireArguments().getParcelable<ContactModel>(Key.contactModel)!!

        binding.textViewName.text = contactModel.name
        binding.buttonSend.setOnClickListener {
            val message = binding.editTextMessage.text.toString()
            TelephonyUtils(requireContext()).sendSMS(contactModel.phoneNumber!!, message)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}