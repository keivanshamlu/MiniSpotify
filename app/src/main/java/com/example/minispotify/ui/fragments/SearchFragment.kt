package com.example.minispotify.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.daggerkotlinn.di.ViewModelProviderFactory
import com.example.minispotify.R
import com.example.minispotify.adapters.TracksAdapter
import com.example.minispotify.model.search.Item
import com.example.minispotify.model.search.SearchResult
import com.example.minispotify.ui.BaseFragment
import com.example.minispotify.ui.activities.MainActivity
import com.example.minispotify.util.Resource
import com.example.minispotify.util.Status
import com.example.minispotify.viewModels.SearchViewModel
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : BaseFragment(), TracksAdapter.Interaction {

    /**
     * observes onClick on recyclerView Items (Tracks)
     * set clicked Item that is parcable into bundle and
     * navigate user with that bundle
     */
    override fun onItemSelected(position: Int, item: Item) {

        val bundleToTrackDetailsFragment = bundleOf(
            "trackItem" to item
        )

        navController!!.navigate(R.id.action_searchFragment_to_trackDetailsFragment , bundleToTrackDetailsFragment)
    }

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory

    lateinit var tracksAdapter: TracksAdapter
    lateinit var viewModel: SearchViewModel

    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //register viewmodel and navcontroller
        viewModel = ViewModelProvider(activity as MainActivity, viewModelProvider).get(
            SearchViewModel::class.java
        )
        navController = Navigation.findNavController(view)
        attachObservers()
        attachTextWatcher()
        setUpRecyclerView()

    }

    /**
     * observes seachbar editText with Rxbinding library
     * i set debounce operation to prevent sequential server request
     */
    fun attachTextWatcher() {



        var editTextBinding = mainSearchText
            .textChanges()
            .debounce(800, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.toString().isEmpty()){
                    tracksRecyclerView.visibility = View.GONE
                }else{
                    Log.e("MainActivity", it.toString())
                    viewModel.searchSpotify(it.toString())
                }

            }, {
                Log.e("MainActivity", it.toString())
            })


    }

    /**
     * observes viewmodel's live datas
     */
    fun attachObservers() {

        // observes search in spotify result
        viewModel.tracksresultLiveData.observe(this, Observer {

            handleSearchResult(it)
        })
    }

    /**
     * handles result of search request and calls setSearchBarLoading()
     * when ever loading state changes , calls recyclerview when
     * request is successfull , shows error when request fails
     */
    fun handleSearchResult(result: Resource<SearchResult>) {

        when (result.status) {

            Status.ERROR -> {

                setSearchBarLoading(false)
                tracksRecyclerView.visibility = View.GONE
                Toast.makeText(activity , result.message , Toast.LENGTH_LONG).show()
            }
            Status.LOADING -> {
                setSearchBarLoading(true)
            }
            Status.SUCCESS -> {

                setSearchBarLoading(false)
                tracksRecyclerView.visibility = View.VISIBLE
                var searchResultData : SearchResult? = result.data
                tracksAdapter.submitList(searchResultData?.tracks!!.items)
            }
        }
    }

    /**
     * set seachbar loading veiws
     */
    fun setSearchBarLoading(isLoading : Boolean){

        if(isLoading){

            searchBarProgressBar.visibility = View.VISIBLE
        }else{

            searchBarProgressBar.visibility = View.GONE
        }
    }

    /**
     * setUps tracks recyclerview
     */
    fun setUpRecyclerView() {

        tracksRecyclerView.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        tracksAdapter = TracksAdapter(this)
        tracksRecyclerView.adapter = tracksAdapter

    }
}
