package com.example.healthmonitor.ui.pressure.calculator

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthmonitor.databinding.PressureRangeItemBinding
import com.example.healthmonitor.ui.pressure.PressureRangeEnum

class PressureRangeAdapter(val context: Context, val data: List<PressureRangeEnum>) : RecyclerView.Adapter<PressureRangeAdapter.PressureRangeViewHolder>() {

    class PressureRangeViewHolder(val binding: PressureRangeItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PressureRangeViewHolder {
        val binding = PressureRangeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PressureRangeViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: PressureRangeViewHolder, position: Int) {
        val item = data[position]
        holder.binding.apply {
            tvPressureType.text = item.displayName
            tvPressureRange.text = item.range
//            iconPressure.settin
//            ViewCompat.setBackgroundTintList(iconPressure, ColorStateList.valueOf(Color.RED))
//            ViewCompat.setBackgroundTintList(iconPressure, context.resources.getColorStateList(R.color.purple_200))
//            iconPressure.backgroundTintList = ColorStateList.valueOf(Color.RED)
//            ImageViewCompat.setImageTintList(iconPressure, ColorStateList.valueOf(R.color.purple_200));
//            iconPressure.setColorFilter(ContextCompat.getColor(context, R.color.bmi_normal))
            iconPressure.setColorFilter(Color.parseColor(item.color))
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}