package com.example.mars.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.*
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.mars.R
import com.example.mars.data.State
import com.example.mars.databinding.FragmentSecondBinding
import com.example.mars.entity.Photo
import com.example.mars.pagedlist.MyPagedListAdapter
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class SecondFragment : Fragment() {

    @Inject
    lateinit var viewModel: MainViewModel

    private val adapter = MyPagedListAdapter { photo, imageView ->
        onItemClick(photo, imageView)
    }

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val rover = arguments?.getString("rover")
                val date = arguments?.getString("date")
                return MainViewModel(rover!!, date!!) as T
            }
        }).get()

        binding.recyclerView.itemAnimator = SlideInLeftAnimator()
        binding.recyclerView.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

        viewModel.sendError()

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state.collect {
                    when (it) {
                        State.Error -> {
                            binding.textView.text = getString(R.string.no_pictures)
                        }

                        State.Success -> {
                            binding.textView.text = ""
                        }
                    }
                }
            }
        }

//        adapter.addLoadStateListener { loadState ->
//            if (loadState.refresh is LoadState.NotLoading) {
//                //binding.progressCircular.visibility = View.GONE
//            }
//        }

        adapter.loadStateFlow.onEach {
            binding.swipeRefresh.isRefreshing = it.refresh is LoadState.Loading
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.pagedMarsData.onEach {
            adapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onItemClick(item: Photo, imageView: ImageView) {
        val bundle = Bundle()
        val url = item.img_src
        val rover = item.rover.name
        val camera = item.camera.full_name
        val date = item.earth_date
        bundle.putString("url", url)
        bundle.putString("rover", rover)
        bundle.putString("camera", camera)
        bundle.putString("date", date)

        val extras = FragmentNavigatorExtras(imageView to resources.getString(R.string.transitionName))

        findNavController().navigate(
            R.id.action_secondFragment_to_thirdFragment,
            bundle,
            null,
            extras
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
