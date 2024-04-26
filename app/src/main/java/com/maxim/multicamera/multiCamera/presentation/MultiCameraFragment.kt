package com.maxim.multicamera.multiCamera.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import com.maxim.multicamera.core.presentation.BaseFragment
import com.maxim.multicamera.core.presentation.BundleWrapper
import com.maxim.multicamera.databinding.FragmentMultiCameraBinding

class MultiCameraFragment : BaseFragment<MultiCameraViewModel, FragmentMultiCameraBinding>(),
    MakeRadioButtons {
    override val viewModelClass = MultiCameraViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMultiCameraBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        viewModel.observe(this) {
            it.show(this, binding.startButton)
        }

        binding.startButton.setOnClickListener {
            viewModel.start()
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun makeButtons(names: List<String>, selection: Pair<Int, Int>) {
        binding.physicalButtonGroupOne.removeAllViews()
        binding.physicalButtonGroupTwo.removeAllViews()

        names.forEachIndexed { i, name ->
            val buttonOne = RadioButton(requireContext())
            buttonOne.isChecked = selection.first == i
            buttonOne.text = name
            buttonOne.setOnClickListener {
                viewModel.choose(0, i)
            }
            val buttonTwo = RadioButton(requireContext())
            buttonTwo.isChecked = selection.second == i
            buttonTwo.text = name
            buttonTwo.setOnClickListener {
                viewModel.choose(1, i)
            }

            binding.physicalButtonGroupOne.addView(buttonOne)
            binding.physicalButtonGroupTwo.addView(buttonTwo)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(BundleWrapper.Base(outState))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { viewModel.restore(BundleWrapper.Base(it)) }
    }
}

interface MakeRadioButtons {
    fun makeButtons(names: List<String>, selection: Pair<Int, Int>)
}