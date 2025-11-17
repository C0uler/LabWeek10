package com.jordan.labweek10

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.jordan.labweek10.database.TotalDatabase
import com.jordan.labweek10.viewmodels.TotalViewModel
import com.jordan.labweek10.database.Total
import com.jordan.labweek10.database.TotalObject
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val db by lazy { prepareDatabase() }
    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepareViewModel()
        initializeValueFromDatabase()
    }

    override fun onStart(){
        super.onStart()
        val total= db.totalDao().getTotal(ID)

        if (total.isNotEmpty()) {
            val lastUpdateDate = total.first().total.date

            Toast.makeText(this, lastUpdateDate, Toast.LENGTH_LONG).show()
        }
    }

    override fun onPause() {
        super.onPause()
        val currentValue = viewModel.total.value ?: 0
        val newDate = java.util.Date().toString()
        val newTotalObject = TotalObject(value = currentValue, date = newDate)
        db.totalDao().update(Total(ID, newTotalObject))
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text = getString(R.string.text_total, total)
    }

    private fun prepareViewModel(){
        viewModel.total.observe(this) { total ->
            updateText(total)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java, "total-database"
        ).allowMainThreadQueries().build()
    }

    private fun initializeValueFromDatabase() {
        val total = db.totalDao().getTotal(ID)
        if (total.isEmpty()) {
            val initialObject = TotalObject(value = 0, date = "Never updated")
            db.totalDao().insert(Total(id = 1, total = initialObject))
        } else {
            viewModel.setTotal(total.first().total.value)
        }
    }

    companion object {
        const val ID: Long = 1
    }
}
