package com.assignment.petamicoassignment.ui.userlist

import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.recyclerview.widget.DividerItemDecoration
import com.assignment.petamicoassignment.R
import com.assignment.petamicoassignment.apputils.MyCallback
import com.assignment.petamicoassignment.apputils.NetworkCheck
import com.assignment.petamicoassignment.apputils.NewUserCallback
import com.assignment.petamicoassignment.databinding.FragmentFirstBinding
import com.assignment.petamicoassignment.ui.addnewuser.AddNewUserDirections
import com.assignment.petamicoassignment.ui.addnewuser.AddNewUserViewModel
import com.assignment.petamicoassignment.ui.database.UserDao
import com.assignment.petamicoassignment.ui.database.UserDatabase
import com.assignment.petamicoassignment.ui.network.CreatedUserResponse
import com.assignment.petamicoassignment.ui.network.UserDataServer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserList : Fragment(),MyCallback{

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    private lateinit var dataSource : UserDao
    var arrayList : List<UserDataServer>? = null
    lateinit var adapter : UserAdapter
    private lateinit var viewModel: UserListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        val application : Application = requireActivity().application

        dataSource  = UserDatabase.getInstance(application).userDao

        val viewModelFactory = UserListViewModelFactory(dataSource,application)

        viewModel = ViewModelProvider(this,viewModelFactory).get(UserListViewModel::class.java)

        binding.userListViewModel = viewModel
        binding.lifecycleOwner = this


        binding.toolbar.title = "Assignment"

        binding.toolbar.overflowIcon = resources.getDrawable(R.drawable.ic_baseline_more_vert_24)

        binding.toolbar.inflateMenu(R.menu.menu_main)

        adapter = UserAdapter()
        binding.recyclerView.adapter = adapter

        adapter.setCallBack(this)


        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(decoration)

        viewModel.isDataGetSuccessfully.observe(viewLifecycleOwner, Observer {
            if(it){
                lifecycleScope.launchWhenCreated{
                    adapter.submitData(PagingData.from(viewModel.arrayList))
                    adapter.notifyDataSetChanged()
                }
                viewModel._isDataGetSuccessfully.value = false
            }
        })
        binding.toolbar.setOnMenuItemClickListener {it->
            when(it.itemId) {
                R.id.action_sort_a_z ->{
                    val list = adapter.snapshot().items.sortedBy { it.first_name }

                    lifecycleScope.launchWhenCreated{
                        adapter.submitData(PagingData.from(list))
                        adapter.notifyDataSetChanged()
                    }



                }

                R.id.action_sort_z_a ->{
                    val list = adapter.snapshot().items.sortedByDescending { it.first_name }

                    lifecycleScope.launchWhenCreated{
                        adapter.submitData(androidx.paging.PagingData.from(list))
                        adapter.notifyDataSetChanged()
                    }
                }

            }
            true
        }


        binding.searchLayout.autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.searchLayout.autoCompleteTextView.getText().toString().trim()
                        .isNotEmpty() && adapter.snapshot().items.size > 0
                ) {
                    val filterList: List<UserDataServer> = adapter.snapshot().items.filter {
                        it.first_name.contains(
                            binding.searchLayout.autoCompleteTextView.text.toString().trim(),
                            ignoreCase = true
                        )
                    }
                    lifecycleScope.launchWhenCreated {
                        adapter.submitData(PagingData.from(filterList))
                        adapter.notifyDataSetChanged()
                    }

                } else {

                    if (NetworkCheck().isInternetAvailable(requireContext())) {
                        lifecycleScope.launchWhenCreated {
                            viewModel.getSearchResultStream().collectLatest {
                                adapter.submitData(it)
                                adapter.notifyDataSetChanged()

                            }
                        }
                    } else {
                        viewModel.getLocalData()
                    }

                }
            }
        })

        viewModel.isButtonClicked.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(UserListDirections.actionFirstFragmentToSecondFragment())
                viewModel._isButtonClicked.value = false

            }
        })

        viewModel.userDeletedSuccessfully.observe(viewLifecycleOwner, Observer {
            if(it){
                Toast.makeText(requireContext(),"Deleted successfully...",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Please try again...",Toast.LENGTH_SHORT).show()
            }
        })

        if(NetworkCheck().isInternetAvailable(requireContext())) {
            lifecycleScope.launchWhenCreated {
                viewModel.getSearchResultStream().collectLatest {
                    viewModel.fillDataToDatabase(adapter.snapshot().items)
                    adapter.submitData(it)

                }
            }
        }else{
            viewModel.getLocalData()
        }


        binding.swiperefresh.setOnRefreshListener{
            if(NetworkCheck().isInternetAvailable(requireContext())) {
                adapter.refresh()
                binding.swiperefresh.isRefreshing = false

            }else{
                binding.swiperefresh.isRefreshing = false
            }
        }

        return binding.root

    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun deleteUser(id: Int,position:Int) {
        viewModel.deleteUser(id)
        val list: ArrayList<UserDataServer> = adapter.snapshot().items as ArrayList<UserDataServer>
        val listOne = list.filter { it.id!=id }
        lifecycleScope.launchWhenCreated {
            adapter.submitData(PagingData.from(listOne))
        }

    }


}