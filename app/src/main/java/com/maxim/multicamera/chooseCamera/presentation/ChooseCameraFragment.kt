package com.maxim.multicamera.chooseCamera.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.maxim.multicamera.core.presentation.BaseFragment
import com.maxim.multicamera.databinding.FragmentChooseCameraBinding

class ChooseCameraFragment : BaseFragment<ChooseCameraViewModel, FragmentChooseCameraBinding>(),
    AddButton {
    override val viewModelClass = ChooseCameraViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentChooseCameraBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(this) {
            it.show(this, binding.failTextView)
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun clear() {
        binding.buttonLayout.removeAllViews()
    }

    override fun addButton(name: String) {
        val button = Button(requireContext())
        button.text = name
        val index = binding.buttonLayout.childCount
        button.setOnClickListener {
            viewModel.choose(index)
        }
        binding.buttonLayout.addView(button)
    }
}

interface AddButton {
    fun clear()
    fun addButton(name: String)
}