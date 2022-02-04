package com.example.practice6.recyclerview

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.practice6.model.GymItem
import com.example.practice6.utils.Constants.TAG
import kotlinx.android.synthetic.main.list_item_gym.view.*

class GymItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val gymItemName = itemView.item_gym_name
    private val gymItemAddress = itemView.item_gym_address

    @SuppressLint("SetTextI18n")
    fun bindWithView(activity: Activity, gymItem: GymItem) {
        Log.d(TAG, "GymItemViewHolder - bindWithView() called")
        gymItemName.text = gymItem.name
        gymItemAddress.text = "${gymItem.address} ${gymItem.buildingNum}"

        itemView.setOnClickListener {
            activity.intent.putExtra("id", gymItem.id)
            activity.intent.putExtra("name", gymItem.name)
            activity.setResult(AppCompatActivity.RESULT_OK, activity.intent)
            activity.finish()
        }
    }

}