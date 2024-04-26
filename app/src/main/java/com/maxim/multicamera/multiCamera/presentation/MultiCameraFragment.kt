package com.maxim.multicamera.multiCamera.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.maxim.multicamera.chooseCamera.presentation.AddButton
import com.maxim.multicamera.core.presentation.BaseFragment
import com.maxim.multicamera.databinding.FragmentMultiCameraBinding

class MultiCameraFragment : BaseFragment<MultiCameraViewModel, FragmentMultiCameraBinding>(), AddButton {
    override val viewModelClass = MultiCameraViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMultiCameraBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observe(this) {
            it.show(this)
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun clear() {
        binding.physicalButtonGroupOne.removeAllViews()
        binding.physicalButtonGroupTwo.removeAllViews()
    }

    override fun addButton(name: String) {
        val buttonOne = RadioButton(requireContext())
        buttonOne.text = name
        val buttonTwo = RadioButton(requireContext())
        buttonTwo.text = name

        binding.physicalButtonGroupOne.addView(buttonOne)
        binding.physicalButtonGroupTwo.addView(buttonTwo)
    }
}