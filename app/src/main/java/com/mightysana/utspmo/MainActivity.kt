package com.mightysana.utspmo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.AdapterView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ItemAdapter
    private lateinit var playerList: List<Player>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val titles = resources.getStringArray(R.array.player_name)
        val descriptions = resources.getStringArray(R.array.player_position)
        val images = resources.obtainTypedArray(R.array.player_images)
        val nationalities = resources.getStringArray(R.array.player_nationality)
        val birthDates = resources.getStringArray(R.array.player_birth_date)
        val heights = resources.getStringArray(R.array.player_height)
        val marketValues = resources.getStringArray(R.array.player_market_value)
        val numbers = resources.getStringArray(R.array.player_number).map { it.toDoubleOrNull()?.toInt() ?: 0 }

        playerList = titles.indices.map { index ->
            Player(
                name = titles[index],
                position = descriptions[index],
                imageResId = images.getResourceId(index, -1),
                nationality = nationalities[index],
                birthDate = birthDates[index],
                height = heights[index],
                number = numbers[index],
                marketValue = marketValues[index].toDouble()
            )
        }

        images.recycle()

        adapter = ItemAdapter(this, playerList)
        recyclerView.adapter = adapter

        setupPositionFilter()
    }

    private fun setupPositionFilter() {
        val positionSpinner = findViewById<Spinner>(R.id.positionSpinner)

        val positions = listOf("All", "AMF", "CB", "GK", "DMF", "CMF", "LWF", "RWF", "CF")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, positions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        positionSpinner.adapter = spinnerAdapter

        positionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedPosition = positions[position]
                filterByPosition(selectedPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun filterByPosition(position: String) {
        val filteredList = if (position == "All") {
            playerList
        } else {
            playerList.filter { it.position == position }
        }
        adapter.updateData(filteredList)
    }
}
