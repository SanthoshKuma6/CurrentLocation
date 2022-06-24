package com.example.currentlocation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currentlocation.databinding.FragmentLocationAdapterBinding
import com.example.currentlocation.user.LocationTable


class LocationAdapter : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {
    var locationListener:((location:LocationTable)->Unit)?=null
   inner class LocationViewHolder(private val binding: FragmentLocationAdapterBinding):RecyclerView.ViewHolder(binding.root) {

        fun setViews(locationTable: LocationTable){
            binding.latValue.text= " - ".plus(locationTable.lat)
            binding.lonValue.text= " - " .plus(locationTable.lan)
        }
       init {
           binding.root.setOnClickListener {
               try {
                   val position = adapterPosition
                   if (position != RecyclerView.NO_POSITION){
                       locationListener!!.invoke(differ.currentList[position])
                   }
               }catch (e:NullPointerException){
                   e.printStackTrace()
               }

           }

       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return LocationViewHolder(FragmentLocationAdapterBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
       val sample = differ.currentList[position]
        holder.setViews(sample)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    val callback = object : DiffUtil.ItemCallback<LocationTable>(){
        override fun areItemsTheSame(oldItem: LocationTable, newItem: LocationTable): Boolean {
            return oldItem.lan == newItem.lan
        }

        override fun areContentsTheSame(oldItem: LocationTable, newItem: LocationTable): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this,callback)
}