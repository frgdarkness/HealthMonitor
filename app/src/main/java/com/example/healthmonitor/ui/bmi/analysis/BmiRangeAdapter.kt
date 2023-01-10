package com.example.healthmonitor.ui.bmi.analysis

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthmonitor.R
import com.example.healthmonitor.databinding.BmiItemBinding

class BmiRangeAdapter(private val data: List<BmiRangeEnum>) : RecyclerView.Adapter< BmiRangeAdapter.BmiRangeViewHolder>() {

//    private val data = mutableListOf<BmiRangeItem>()
//
//    fun setData(list: List<BmiRangeItem>) {
//        data.clear()
//        data.addAll(list)
//    }

    class BmiRangeViewHolder(val binding: BmiItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BmiRangeViewHolder {
        val binding = BmiItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BmiRangeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BmiRangeViewHolder, position: Int) {
        holder.binding.apply {
            val bmiRangeData = data[position]
            cardViewColor.setCardBackgroundColor(Color.parseColor(bmiRangeData.color))
            tvBmiType.text = bmiRangeData.displayName
            tvBmiRange.text = data.getOrNull(position)?.detail
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}