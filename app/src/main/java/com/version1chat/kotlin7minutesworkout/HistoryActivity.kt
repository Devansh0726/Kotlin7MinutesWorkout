package com.version1chat.kotlin7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.version1chat.kotlin7minutesworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolBarHistoryActivity)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "HISTORY"
        }

        binding?.toolBarHistoryActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        val dao = (applicationContext as WorkOutApp).db.historyDao()
        getAllCompletedDates(dao)
    }

    private fun getAllCompletedDates(historyDao: HistoryDao){
        lifecycleScope.launch {
            historyDao.fetchAllDates().collect(){ allCompletedDateList ->
               if (allCompletedDateList.isNotEmpty()){
                   binding?.tvHistory?.visibility = View.VISIBLE
                   binding?.rvHistory?.visibility = View.VISIBLE

                   binding?.tvNoDataAvailable?.visibility = View.GONE

                   binding?.rvHistory?.layoutManager = LinearLayoutManager(this@HistoryActivity)
                   val dates = ArrayList<String>()
                   for (date in allCompletedDateList){
                       dates.add(date.date)
                   }
                   val historyAdapter = HistoryAdapter(dates)
                   binding?.rvHistory?.adapter = historyAdapter

               } else{
                   binding?.tvHistory?.visibility = View.INVISIBLE
                   binding?.rvHistory?.visibility = View.INVISIBLE

                   binding?.tvNoDataAvailable?.visibility = View.VISIBLE
               }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}