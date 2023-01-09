package com.example.healthmonitor.ui.pressure.history

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthmonitor.PRESSURE
import com.example.healthmonitor.databinding.ItemPressureBinding
import com.example.healthmonitor.ui.Helper
import com.example.healthmonitor.ui.pressure.getPressureRange

class PressureAdapter(
    private val context: Context,
    private val onClick: (Long) -> Unit
) : RecyclerView.Adapter<PressureAdapter.PressureViewHolder>() {

    private val data = mutableListOf<PRESSURE>()

    class PressureViewHolder(val binding: ItemPressureBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PressureViewHolder {
        val binding = ItemPressureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PressureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PressureViewHolder, position: Int) {
        val pressureData = data[position]
        holder.binding.apply {
            val pressureRange = getPressureRange(pressureData.sys.toInt(), pressureData.dia.toInt())
            tvDateTime.text = Helper.convertMillisToDateTime(pressureData.dateTime)
            tvSys.text = pressureData.sys.toString()
            tvDia.text = pressureData.dia.toString()
            viewStatus.setBackgroundColor(Color.parseColor(pressureRange.color))
            tvStatus.text = pressureRange.displayName

            cardViewPressure.setOnClickListener {
                onClick(pressureData.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun loadData(pressureList: List<PRESSURE>) {
        data.clear()
        data.addAll(pressureList)
        notifyDataSetChanged()
    }
}