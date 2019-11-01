package com.example.minispotify.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.daggerkotlinn.di.ViewModelProviderFactory
import com.example.minispotify.R
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set up viewmodel and navcontroller
        viewModel = ViewModelProvider(activity as MainActivity, viewModelProvider).get(
            TrackDetailsViewModel::class.java)
        navController = Navigation.findNavController(view)

        attachObservers()
        //request for more information of track
        viewModel.getAudioFeatures(currentItem.id)

        //pass given Item object from searchFragment
        setTrackAlreadyGivenDatas(currentItem)
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
    }

    /**
     * fetchs already given data that is given from
     * search fragment into view
     */
    fun setTrackAlreadyGivenDatas(item : Item){

        Glide.with(activity as MainActivity)
            .load(item.album.images.get(0).url)
            .placeholder(  R.drawable.ic_compact_disc )
            .into(trackCover)

        trackName.text = item.name
        artistName.text = item.artists.get(0).name
        albumName.text = item.album.name
        duration.text = (item.duration_ms/1000).toString()
        popularity.text = item.popularity.toString()
        track_number.text = item.track_number.toString()
        type.text = item.type

    }

    /**
     * fetchs result of getaudioFeatures request into view
     */
    fun setAudiofeatures(result : AudioFeaturesResult?){

        energy.text = result?.energy.toString()
        loudness.text = result?.loudness.toString()
        speechiness.text = result?.speechiness.toString()
    }

    /**
     * handles result of getaudioFeatures request and calls setAudioFeaturesLoading()
     * when ever loading state changes , call setAudiofeatures when
     * request is successfull , shows error when request fails
     */
    fun handleAudiofeaturesResult(result : Resource<AudioFeaturesResult>){

        when(result.status){

            Status.SUCCESS -> {
                setAudiofeatures(result.data)
                setAudioFeaturesLoading(false)
            }
            Status.ERROR -> {

                Toast.makeText(activity , result.message , Toast.LENGTH_LONG).show()
                setAudioFeaturesError()
            }
            Status.LOADING -> {

                setAudioFeaturesLoading(true)
            }
        }
    }

    /**
     * set getaudioFeatures error view
     */
    fun setAudioFeaturesError(){

        connectionFailed.visibility = View.VISIBLE
        audioFeaturesLayout.visibility = View.GONE
        featuresProgressBar.visibility = View.GONE
    }
    /**
     * set getaudioFeatures loading veiws
     */
    fun setAudioFeaturesLoading(isLoading : Boolean){

        if(isLoading){

            audioFeaturesLayout.visibility = View.GONE
            featuresProgressBar.visibility = View.VISIBLE
        }else{

            featuresProgressBar.visibility = View.GONE
            audioFeaturesLayout.visibility = View.VISIBLE
        }
    }
}
