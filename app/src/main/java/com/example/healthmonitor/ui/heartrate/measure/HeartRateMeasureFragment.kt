package com.example.healthmonitor.ui.heartrate.measure

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.healthmonitor.R
import com.example.healthmonitor.databinding.FragmentHeartRateBinding
import com.example.healthmonitor.databinding.FragmentHeartRateMeasureBinding
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeartRateMeasureFragment : Fragment() {

    private lateinit var viewModel: HeartRateMeasureViewModel
    private lateinit var binding: FragmentHeartRateMeasureBinding
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private var cameraExecutor: ExecutorService? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeartRateMeasureBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HeartRateMeasureViewModel::class.java]

        initView()
        initEvent()
        observerViewModel()
        return binding.root
    }

    private val measureCalculator = object : OutputAnalyzer.MeasureCalculator {
        override suspend fun calculateMeasure(): Int {
            return withContext(Dispatchers.Main) {
                val bitmap = binding.viewFinder.bitmap
                val width = binding.viewFinder.width
                val height = binding.viewFinder.height
                val pixelCount = width * height
                var measurement = 0
                val pixels = IntArray(pixelCount) { 0 }
                bitmap?.getPixels(pixels, 0, width, 0, 0, width, height)
                for (pixelIndex in 0 until pixelCount) {
                    measurement += pixels[pixelIndex] shr 16 and 0xff
                }
                Log.d("asd", "measurement = $measurement")
                measurement
            }
        }
    }

    private val outputAnalyzer = OutputAnalyzer(measureCalculator)

    private fun initView() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
    }

    private fun bindPreview() {
        ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            preview  = Preview.Builder().build()
            val cameraSelector : CameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            cameraProvider?.unbindAll()
            preview?.let { preview ->
                preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                val camera = cameraProvider?.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)
                camera?.cameraInfo?.hasFlashUnit()?.let {
                    camera.cameraControl.enableTorch(true)
                }
            }
        }, ContextCompat.getMainExecutor(requireContext()))
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun stopPreview() {
        cameraProvider?.unbindAll()
        if (cameraExecutor?.isShutdown == false) cameraExecutor?.shutdown()
    }

    private fun initEvent() {
        binding.btnStartMeasure.setOnClickListener {
            if (viewModel.isMeasuring.value == true) return@setOnClickListener
//            Log.d("asd", "start measure")
//            CoroutineScope(Dispatchers.Main).launch {
//                viewModel.test {
//                    Random.nextInt(100)
//                }
//            }
            viewModel.setMeasureState(true)
            bindPreview()
            var currentProgress = 0
            CoroutineScope(Dispatchers.Default).launch {
                binding.progressBarTime.progress = 0f
                val data = outputAnalyzer.measure()
                withContext(Dispatchers.Main) {
                    data.collect {
                        val (pulse, progress) = it
                        if (progress != -1) {
                            binding.progressBarTime.progress = progress.toFloat()
                        }
                        if (pulse != -1) binding.tvRateValue.text = pulse.toString()
//                        binding.progressBarTime.progress = it.first.toFloat()
//                        if (progress == 100) {
//                            findNavController().navigate(
//                                HeartRateMeasureFragmentDirections.actionHeartRateMeasureFragmentToManualInputHeartRateFragment(pulse)
//                            )
//                            viewModel.setMeasureState(false)
//                        }
                    }
                    viewModel.setMeasureState(false)
                    findNavController().navigate(
                        HeartRateMeasureFragmentDirections.actionHeartRateMeasureFragmentToManualInputHeartRateFragment(binding.tvRateValue.text.toString().toIntOrNull() ?: 0)
                    )
                }
                Log.d("asd", "data result = $data, done")
//                withContext(Dispatchers.Main) {
//                    tvData.text = data
//                }

            }
        }
        binding.tvDescription.setOnClickListener {
            stopPreview()
        }
    }

    private fun observerViewModel() {
        viewModel.isMeasuring.observe(viewLifecycleOwner, Observer { isMeasuring ->
            if (!isMeasuring) stopPreview()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPreview()
    }
}