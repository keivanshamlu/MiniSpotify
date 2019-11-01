package com.example.minispotify.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.daggerkotlinn.di.ViewModelProviderFactory
import com.example.minispotify.R
import com.example.minispotify.model.LoginType
import com.example.minispotify.model.User
import com.example.minispotify.ui.BaseFragment
import com.example.minispotify.ui.activities.MainActivity
import com.example.minispotify.util.AuthStatus
import com.example.minispotify.util.Constans.REQUEST_CODE
import com.example.minispotify.viewModels.LoginViewModel
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment() , View.OnClickListener{

    override fun onClick(p0: View?) {

        when(p0!!.id){

            R.id.loginFromApp -> AuthenticationClient.openLoginActivity(activity, REQUEST_CODE, request)
            R.id.loginFromBrowser -> {AuthenticationClient.openLoginInBrowser(activity, request)}
        }
    }

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory

    //we inject the request to authentication in the appModule
    @Inject
    lateinit var request : AuthenticationRequest

    lateinit var viewModel : LoginViewModel
    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //register viewmodel and navcontroller
        viewModel = ViewModelProvider(activity as MainActivity, viewModelProvider).get(LoginViewModel::class.java)
        navController = Navigation.findNavController(view)

        attachObservers()
        setUpViews()
    }

    /**
     * attach view.onclicklisteners to buttons
     */
    fun setUpViews(){

        loginFromApp.setOnClickListener(this)
        loginFromBrowser.setOnClickListener(this)
    }


    /**
     * observes viewmodel's liveDatas
     */
    fun attachObservers(){

        // observe user athentication
        viewModel.cachedUserLiveData.observe(activity as MainActivity , Observer {

            when(it.status){

                AuthStatus.AUTHENTICATED -> {
                }
                AuthStatus.LOADING -> {
                }
                AuthStatus.ERROR -> {
                }
                AuthStatus.LOG_OUT -> {
                }
            }
        })
    }

    /**
     * when user authenticates using application
     * , result will come up here
     * if authentication was successfull or not we
     * set in sessionManager and it will handle it all
     */
     override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            val response = AuthenticationClient.getResponse(resultCode, intent)

            when (response.type) {
                // Response was successful and contains auth token
                AuthenticationResponse.Type.TOKEN -> {

                    //login type -> from app
                    viewModel.setUser(User("Bearer "+response.accessToken , LoginType.FROM_APP))
                }

                // Auth flow returned an error
                AuthenticationResponse.Type.ERROR -> {

                    viewModel.setStateError(response.error)
                }
            }
        }
    }



}
