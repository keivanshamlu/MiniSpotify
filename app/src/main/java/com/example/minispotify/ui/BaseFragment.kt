package com.example.minispotify.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.minispotify.R
import com.example.minispotify.SessionManager
import com.example.minispotify.util.AuthStatus
import dagger.android.support.DaggerFragment
import javax.inject.Inject

open class BaseFragment: DaggerFragment()  {


    @Inject
    lateinit var sessionManager: SessionManager
    lateinit var navControllerr: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set up navControllerr
        navControllerr = Navigation.findNavController(view)
        keepTrackingOfUser()
    }


    /**
     * this method always observes user's state
     * , if it's authenticated navigates user into
     * searchFragment if state is logOut navigates
     * user into loginScreen
     */
    fun keepTrackingOfUser(){

        sessionManager.cachedUserLiveData.observe(viewLifecycleOwner){

            when(it.status){

                AuthStatus.AUTHENTICATED -> {

                    if (navControllerr.currentDestination!!.label == getString(R.string.fragment_login_lable)) {
                        navControllerr.navigate(R.id.action_loginFragment_to_searchFragment)
                    }
                }
                AuthStatus.LOADING -> {}
                AuthStatus.ERROR -> {

                    Toast.makeText(activity , it.message , Toast.LENGTH_LONG).show()
                }
                AuthStatus.LOG_OUT -> {

                    if(navControllerr.currentDestination!!.label == getString(R.string.search_fragment_lable)){

                        navControllerr.navigate(R.id.action_searchFragment_to_loginFragment)
                    }
                }
            }
        }
    }
}