package com.example.minispotify.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.daggerkotlinn.di.ViewModelProviderFactory
import com.example.minispotify.R
import com.example.minispotify.databinding.FragmentTrackDetailsBinding
import com.example.minispotify.model.search.Item
import com.example.minispotify.model.trackDetails.AudioFeaturesResult
import com.example.minispotify.ui.BaseFragment
import com.example.minispotify.ui.activities.MainActivity
import com.example.minispotify.util.Resource
import com.example.minispotify.util.Status
import com.example.minispotify.viewModels.TrackDetailsViewModel
import kotlinx.android.synthetic.main.fragment_track_details.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class TrackDetailsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory

    lateinit var viewModel : TrackDetailsViewModel
    lateinit var currentItem : Item
    var navController: NavController? = null

    lateinit var mBinding: FragmentTrackDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //set data binding object
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_track_details, container, false)

        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up viewmodel and navcontroller
        viewModel = ViewModelProvider(activity as MainActivity, viewModelProvider).get(TrackDetailsViewModel::class.java)

        //use to set lifecycle of fragment to it's viewmodel
        viewModel.addLocationUpdates(lifecycle)
        navController = Navigation.findNavController(view)

        attachObservers()
        //request for more information of track
        (activity as MainActivity).idlingResource.increment()
        viewModel.getAudioFeatures(currentItem.id)

        //set objects to databinding object to use in layout
        mBinding.viewModel = viewModel
        mBinding.currentItem = currentItem
        mBinding.currentImage = currentItem.album.images.get(0)
        mBinding.currentAlbum = currentItem.album
        mBinding.currentArtist = currentItem.artists.get(0)
        mBinding.errorMode = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //gets track data from searchFragment in containt an Item object
        currentItem = arguments!!.getParcelable("trackItem")!!
    }

    /**
     * observes viewmodel's livedata
     */
    fun attachObservers(){

        // observes getaudioFeatures request result
        viewModel.audioFeaturesLiveData.observe(this , Observer {

            handleAudiofeaturesResult(it)
        })

        viewModel.isConnected.observe(this , Observer {

            handleConnectionStatus(it)
        })
    }

    /**
     * to observe user's device connection state
     */
    fun handleConnectionStatus(isConnected : Boolean?){

    }

    /**
     * handles result of getaudioFeatures request and calls setAudioFeaturesLoading()
     * when ever loading state changes , call setAudiofeatures when
     * request is successfull , shows error when request fails
     */
    fun handleAudiofeaturesResult(result : Resource<AudioFeaturesResult>?){

        when(result?.status){

            Status.SUCCESS -> {

                handleSuccess(result)
            }
            Status.ERROR -> {

               handleError(result)
            }
            Status.LOADING -> {

                handleLoading()
            }
        }
        if(result == null){

            mBinding.errorMode = true
            setAudioFeaturesError()
        }
    }


    /**
     * to handle successfull audio features request
     */
    fun handleSuccess(result : Resource<AudioFeaturesResult>?){

        (activity as MainActivity).idlingResource.decrement()
        mBinding.audioFeaturesResult = result?.data
        mBinding.audioFeaturesLoading = false
        mBinding.errorMode = false
    }

    /**
     * to handle error audio features request
     */
    fun handleError(result : Resource<AudioFeaturesResult>?){

        mBinding.errorMode = true
        (activity as MainActivity).idlingResource.decrement()
        Toast.makeText(activity , result?.message , Toast.LENGTH_LONG).show()
        setAudioFeaturesError()
    }

    /**
     * to handle loading audio features request
     */
    fun handleLoading(){

        mBinding.errorMode = false
        mBinding.audioFeaturesLoading = true
    }

    /**
     * set getaudioFeatures error view
     */
    fun setAudioFeaturesError(){
        connectionFailed.visibility = View.VISIBLE
        audioFeaturesLayout.visibility = View.GONE
        featuresProgressBar.visibility = View.GONE
    }

}
