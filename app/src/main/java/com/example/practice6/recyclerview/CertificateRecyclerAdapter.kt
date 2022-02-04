package com.example.practice6.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practice6.R
import com.example.practice6.model.CertificateItem
import com.example.practice6.utils.Constants.TAG

class CertificateRecyclerAdapter :
    RecyclerView.Adapter<CertificateViewHolder>() {

    private var certificateList = ArrayList<CertificateItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificateViewHolder {
        Log.d(TAG, "CertificateRecyclerAdapter - onCreateViewHolder() called")
        return CertificateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_certificate, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return this.certificateList.size
    }

    override fun onBindViewHolder(holder: CertificateViewHolder, position: Int) {
        holder.bindWithView(this, position, this.certificateList[position])
    }

    //외부에서 어댑터에 데이터 배열을 넘겨준다.
    fun submitList(certificateList: java.util.ArrayList<CertificateItem>) {
        Log.d(TAG, "CertificateRecyclerAdapter - submitList() called")
        this.certificateList = certificateList
    }

    // 자격증 삭제 -> 배열에서 삭제
    fun deleteItem(position: Int): Int {
        certificateList.removeAt(position)
        return certificateList.size
    }
}