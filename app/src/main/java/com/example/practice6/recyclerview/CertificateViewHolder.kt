package com.example.practice6.recyclerview

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.practice6.App
import com.example.practice6.model.CertificateItem
import com.example.practice6.retrofit.RetrofitManager
import com.example.practice6.utils.Constants.TAG
import com.example.practice6.utils.RESPONSE_STATUS
import com.example.practice6.utils.SharedPrefManager
import kotlinx.android.synthetic.main.list_item_certificate.view.*
import java.text.FieldPosition

class CertificateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val certificateItemName = itemView.certificate_name
    private val certificateItemIssuer = itemView.certificate_issuer
    private val certificateItemDate = itemView.certificate_date

    private val deleteCertificate = itemView.delete_certificate

    fun bindWithView(
        adapter: CertificateRecyclerAdapter,
        position: Int,
        certificateItem: CertificateItem,
    ) {
        Log.d(TAG, "CertificateViewHolder - bindWithView() called")

        certificateItemName.text = certificateItem.name
        certificateItemIssuer.text = certificateItem.issuer
        certificateItemDate.text = certificateItem.acquisitionDate

        // 자격증 삭제
        deleteCertificate.setOnClickListener {
            val certificateId = certificateItem.id

            RetrofitManager.instance.deleteCertificate(token = SharedPrefManager.getToken(),
                certificateId = certificateId,
                completion = { responseState ->
                    when (responseState) {
                        RESPONSE_STATUS.OKAY -> {
                            val size = adapter.deleteItem(position)
                            adapter.notifyItemRemoved(position)
                            adapter.notifyItemRangeChanged(position, size)
                            Toast.makeText(App.instance, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                        RESPONSE_STATUS.FAIL -> {
                            Log.d(TAG, "CertificateViewHolder - bindWithView() 자격증 삭제 실패")
                        }
                    }
                })
        }
    }
}