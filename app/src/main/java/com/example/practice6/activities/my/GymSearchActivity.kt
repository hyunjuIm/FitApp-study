package com.example.practice6.activities.my

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practice6.App
import com.example.practice6.R
import com.example.practice6.model.GymItem
import com.example.practice6.recyclerview.GymRecyclerAdapter
import com.example.practice6.retrofit.RetrofitManager
import com.example.practice6.utils.Constants.TAG
import com.example.practice6.utils.RESPONSE_STATUS
import com.example.practice6.utils.SharedPrefManager
import com.example.practice6.utils.textChangesToFlow
import kotlinx.android.synthetic.main.activity_gym_search.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext

class GymSearchActivity : AppCompatActivity() {

    private var gymList = ArrayList<GymItem>()

    //어댑터
    private var gymRecyclerViewAdapter: GymRecyclerAdapter? = null

    // Job : 백그라운드에서 실행되는 작업
    private var myCoroutineJob: Job = Job()
    // get() : 연산자(operator) 함수로써 주어진 key에 해당하는 컨텍스트 요소를 반환
    private val myCoroutineContext: CoroutineContext
        get() = Dispatchers.IO + myCoroutineJob


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym_search)

        gymRecyclerViewSetting(gymList)

        btn_gym_create.setOnClickListener {
            val intent = Intent(App.instance, GymCreateActivity::class.java)
            startActivity(intent)
        }

        GlobalScope.launch(context = myCoroutineContext) {
            val editTextFlow = search_gym.textChangesToFlow()
            editTextFlow
                .debounce(800)
                .filter { // true, false로 적용됨
                    it?.length!! > 0
                }
                .onEach { // 흘러들어온 데이터(flow)를 받음
                    Log.d(TAG, "flow를 받는다 $it")
                    searchGymListApiCall(it.toString())
                }
                .launchIn(this)
        }

    }

    override fun onDestroy() {
        myCoroutineContext.cancel()
        super.onDestroy()
    }

    // 시설 리스트 초기 셋팅
    private fun gymRecyclerViewSetting(gymList: ArrayList<GymItem>) {
        this.gymRecyclerViewAdapter = GymRecyclerAdapter(this)
        this.gymRecyclerViewAdapter!!.submitList(gymList)

        gym_recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        gym_recycler_view.adapter = this.gymRecyclerViewAdapter
    }

    // 시설 검색
    private fun searchGymListApiCall(searchWord: String) {
        RetrofitManager.instance.searchGymList(token = SharedPrefManager.getToken(),
            gymName = searchWord,
            completion = { responseState, list ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        if (list != null) {
                            this.gymList.clear()
                            this.gymList = list
                            this.gymRecyclerViewAdapter?.submitList(this.gymList)
                            this.gymRecyclerViewAdapter?.notifyDataSetChanged()
                        }
                    }
                    RESPONSE_STATUS.FAIL -> {
                        Log.d(TAG, "GymSearchActivity - searchGymListApiCall() 시설 검색 실패")
                    }
                }
            })
    }

}