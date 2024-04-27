package com.multicamera.multicamera.chooseCamera.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.multicamera.multicamera.core.presentation.BaseFragment
import com.multicamera.multicamera.core.presentation.BundleWrapper
import com.multicamera.multicamera.databinding.FragmentChooseCameraBinding

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

    override fun makeButtons(names: List<String>) {
        binding.buttonLayout.removeAllViews()
        names.forEach {
            val button = Button(requireContext())
            button.text = it
            val index = binding.buttonLayout.childCount
            button.setOnClickListener {
                viewModel.choose(index)
            }
            binding.buttonLayout.addView(button)
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

interface AddButton {
    fun makeButtons(names: List<String>)
}