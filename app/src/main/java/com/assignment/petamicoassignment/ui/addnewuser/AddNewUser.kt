package com.assignment.petamicoassignment.ui.addnewuser

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.assignment.petamicoassignment.databinding.FragmentSecondBinding
import com.assignment.petamicoassignment.ui.network.CreatedUserResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewUser : BottomSheetDialogFragment() {

    private var _binding: FragmentSecondBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: AddNewUserViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        val viewModelFactory = AddNewUserViewModelFactory()

        viewModel = ViewModelProvider(this,viewModelFactory).get(AddNewUserViewModel::class.java)



        binding.addUserViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.isDataGetSuccessfully.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel._isDataGetSuccessfully.value = false

            }else{
                Toast.makeText(requireContext(),"Error...",Toast.LENGTH_SHORT).show()
                findNavController().navigate(AddNewUserDirections.actionSecondFragmentToFirstFragment())
            }
        })

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}