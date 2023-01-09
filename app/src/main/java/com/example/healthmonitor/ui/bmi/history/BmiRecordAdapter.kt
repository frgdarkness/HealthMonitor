package com.example.healthmonitor.ui.bmi.history

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthmonitor.BMI
import com.example.healthmonitor.R
import com.example.healthmonitor.databinding.BmiHistoryItemBinding
import com.example.healthmonitor.ui.Helper
import com.example.healthmonitor.ui.bmi.BmiHelper
import com.example.healthmonitor.ui.bmi.analysis.BmiRangeAdapter
import com.example.healthmonitor.ui.bmi.analysis.getBmiRange

class BmiRecordAdapter(
    private val context: Context,
    private val onClick: (Long) -> Unit
) : RecyclerView.Adapter<BmiRecordAdapter.BmiRecordViewHolder>() {

    private val data = mutableListOf<BMI>()

    class BmiRecordViewHolder(val binding: BmiHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BmiRecordViewHolder {
        val binding =
            BmiHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BmiRecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BmiRecordViewHolder, position: Int) {
        val bmi = data[position]
        holder.binding.apply {
            tvWeightValue.text = bmi.weight.toString()
            val bmiRangeData = bmi.bmiValue.getBmiRange()
            val status = bmiRangeData.displayName
            val analysisData = context.getString(R.string.bmi_record_analysis, status, bmi.bmiValue, bmi.height)
            viewStatus.setBackgroundColor(Color.parseColor(bmiRangeData.color))
            tvBmiStatus.text = analysisData
            tvDateTime.text = Helper.convertMillisToDateTime(bmi.dateTime)
            cardViewBmiHistory.setOnClickListener {
                onClick(bmi.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun loadData(bmiList: List<BMI>) {
        data.clear()
        data.addAll(bmiList)
        notifyDataSetChanged()
    }
}