package com.example.practice6.recyclerview

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practice6.R
import com.example.practice6.model.GymItem
import com.example.practice6.utils.Constants.TAG

class GymRecyclerAdapter(private val activity: Activity) :
    RecyclerView.Adapter<GymItemViewHolder>() {

    private var gymList = ArrayList<GymItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GymItemViewHolder {
        Log.d(TAG, "GymRecyclerAdapter - onCreateViewHolder() called")
        return GymItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_gym, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return this.gymList.size
    }

    override fun onBindViewHolder(holder: GymItemViewHolder, position: Int) {
        holder.bindWithView(activity, this.gymList[position])
    }

    //외부에서 어댑터에 데이터 배열을 넘겨준다.
    fun submitList(gymList: java.util.ArrayList<GymItem>) {
        Log.d(TAG, "GymRecyclerAdapter - submitList() called")
        this.gymList = gymList
    }
}