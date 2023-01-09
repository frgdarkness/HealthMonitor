package com.example.healthmonitor.ui.heartrate.history

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthmonitor.HeartRate
import com.example.healthmonitor.data.local.model.HeartRateData
import com.example.healthmonitor.databinding.HeartRateItemBinding
import com.example.healthmonitor.ui.Helper
import com.example.healthmonitor.ui.heartrate.getPulseRange

class HeartRateAdapter(
    private val context: Context,
    private val onClick: (Long) -> Unit
) : RecyclerView.Adapter<HeartRateAdapter.HeartRateViewHolder>(){

    val data = mutableListOf<HeartRateData>()

    class HeartRateViewHolder(val binding: HeartRateItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeartRateViewHolder {
        val binding = HeartRateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeartRateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeartRateViewHolder, position: Int) {
        val item = data[position]
        holder.binding.apply {
            val pulseRange = item.pulse.getPulseRange()
            tvDateTime.text = Helper.convertMillisToDateTime(item.dateTime)
            tvPulseValue.text = "Pulse: ${item.pulse} BPM"
            tvStatus.text = pulseRange.displayName
            tvStatus.setTextColor(Color.parseColor(pulseRange.color))
            imageHeart.setColorFilter(Color.parseColor(pulseRange.color))
            cardViewHeartRate.setOnClickListener {
                onClick(item.id)
            }
//            tvStatus
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun loadData(list: List<HeartRateData>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }
}