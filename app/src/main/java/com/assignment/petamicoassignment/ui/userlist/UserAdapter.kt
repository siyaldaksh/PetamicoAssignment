package com.assignment.petamicoassignment.ui.userlist

import android.content.Context
import android.view.*
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.assignment.petamicoassignment.apputils.MyCallback
import com.assignment.petamicoassignment.databinding.ItemUserBinding
import com.assignment.petamicoassignment.ui.network.UserDataServer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter : PagingDataAdapter<UserDataServer, UserAdapter.UserViewHolder>(
DIFF_CALLBACK
){

    lateinit var context : Context
    lateinit var view : View

    private var mCallBack: MyCallback? = null

    fun setCallBack(callBack: MyCallback?) {
        mCallBack = callBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        context = parent.context

        return UserViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
       val data = getItem(position)
       holder.itemView.name_text.text = data?.first_name.plus(" ").plus( data?.last_name)
       holder.itemView.email_text.text = data?.email
       if(data?.first_name!=null) {
           holder.itemView.name_letter_text.text = data.first_name[0].toString()
       }

        holder.itemView.setOnLongClickListener {
            val deleteData = getItem(holder.absoluteAdapterPosition)
            if (deleteData != null) {
                showDeleteDialogue(deleteData.id,position)
            }
            true
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    private fun showDeleteDialogue(id : Int,position:Int) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Delete")
            .setMessage("Are you sure to delete?")
            .setPositiveButton("Yes") { dialogInterface, i ->
                mCallBack?.deleteUser(id,position)
                dialogInterface.dismiss()
            }
            .setNegativeButton("No") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserDataServer>() {
            override fun areItemsTheSame(oldItem: UserDataServer, newItem: UserDataServer) =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: UserDataServer, newItem: UserDataServer) =
                oldItem == newItem

        }
    }
}