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

        navController!!.navigate(
            R.id.action_searchFragment_to_trackDetailsFragment,
            bundleToTrackDetailsFragment
        )
    }

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory

    lateinit var tracksAdapter: TracksAdapter
    lateinit var viewModel: SearchViewModel

    var requesting = false
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


        //in doOnNext we check if entered text is not empty and to equal to last searched text
        //and after that we set counting idling resources to increment one
        //because thats the time that request happens

        var editTextBinding = mainSearchText
            .textChanges()
            .doOnNext {
                if (!it.toString().isEmpty() && viewModel.lastSearchedText.value != it.toString()) {

                    (activity as MainActivity).idlingResource.increment()
                }
            }
            .debounce(800, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.toString().isEmpty()) {
                    tracksRecyclerView.visibility = View.GONE
                } else {
                    Log.e("MainActivity", it.toString())

                    if (viewModel.lastSearchedText.value != it.toString()) {
                        requesting = true

                        viewModel.searchSpotify(it.toString())
                    }
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
        viewModel.isConnected.observe(this, Observer {

            internetConnectionStatus(it)
        })
    }

    /**
     * keep tracks of user connection state
     */
    fun internetConnectionStatus(isConnected: Boolean) {

    }

    /**
     * handles result of search request and calls setSearchBarLoading()
     * when ever loading state changes , calls recyclerview when
     * request is successfull , shows error when request fails
     */
    fun handleSearchResult(result: Resource<SearchResult>) {


        when (result.status) {

            Status.ERROR -> {

                handleError(result)
            }
            Status.LOADING -> {
                setSearchBarLoading(true)
            }
            Status.SUCCESS -> {

                handleSuccess(result)
            }
        }
    }

    /**
     * handles error request state
     * this method calls in two diffrent situation
     * 1. when we have a request and $result is the response
     * of that request
     * 2. when user navigates back from trackDetails fragment
     * and data sets again from viewmodel
     * so when we set idling resource data , we should know about
     * that and we handle it with requesting var
     */
    fun handleError(result: Resource<SearchResult>) {

        if (requesting) {

            (activity as MainActivity).idlingResource.decrement()
            requesting = false
        }
        setSearchBarLoading(false)
        tracksRecyclerView.visibility = View.GONE
        Toast.makeText(activity, result.message, Toast.LENGTH_LONG).show()
    }

    /**
     * handles success request state
     * this method calls in two diffrent situation
     * 1. when we have a request and $result is the response
     * of that request
     * 2. when user navigates back from trackDetails fragment
     * and data sets again from viewmodel
     * so when we set idling resource data , we should know about
     * that and we handle it with requesting var
     */
    fun handleSuccess(result: Resource<SearchResult>) {

        if (requesting) {

            (activity as MainActivity).idlingResource.decrement()
            requesting = false
        }
        setSearchBarLoading(false)
        tracksRecyclerView.visibility = View.VISIBLE
        var searchResultData: SearchResult? = result.data
        tracksAdapter.submitList(searchResultData?.tracks!!.items)
    }

    /**
     * set seachbar loading veiws
     */
    fun setSearchBarLoading(isLoading: Boolean) {

        if (isLoading) {

            searchBarProgressBar.visibility = View.VISIBLE
        } else {

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
