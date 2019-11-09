package com.example.minispotify.ui.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.idling.CountingIdlingResource
import com.example.minispotify.R
import com.example.minispotify.managers.RequestManager
import com.example.minispotify.managers.SessionManager
import com.example.minispotify.model.LoginType
import com.example.minispotify.model.User
import com.example.minispotify.receivers.ConnectivityReceiver
import com.example.minispotify.util.Constans.REQUEST_CODE
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() , View.OnClickListener , ConnectivityReceiver.ConnectivityReceiverListener {

    override fun onNetworkConnectionChanged(isConnected: Boolean) {

        requestManager.setConnectionStatus(isConnected)
    }


    override fun onClick(p0: View?) {

        when(p0!!.id){

            R.id.logOutButton -> {performLogOut()}
        }
    }

    @Inject
    lateinit var request : AuthenticationRequest
    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var requestManager: RequestManager

    lateinit var navController : NavController

    //for spresso testing
    var idlingResource = CountingIdlingResource("loginIdlingResource")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set onclick to logOut button
        logOutButton.setOnClickListener(this)
         navController= Navigation.findNavController(this, R.id.nav_host_fragment)

        //we observe navController DestinationChangedListener
        //to find out current showing fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->

            // we define fragment lable to activity toobar
            toolbarTextView.text = destination.label

            //if saerch fragment is showing, we visible logOut button
            if(destination.label == resources.getText(R.string.search_fragment_lable)){

                logOutButton.visibility = View.VISIBLE
            }else{

                logOutButton.visibility = View.GONE
            }
        }

        //register connectivity listener for observing connection status
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }



    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    /**
     * when user authenticates using application
     * , result will come up here
     * if authentication was successfull or not we
     * set in sessionManager and it will handle it all
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)


        if (requestCode == REQUEST_CODE) {

            //this code runs when authentication is done
            // so we have to decrement idlingresource
            idlingResource.decrement()

            val response = AuthenticationClient.getResponse(resultCode, intent)

            when (response.type) {
                // Response was successful and contains auth token
                AuthenticationResponse.Type.TOKEN -> {

                    //login type -> from app
                    sessionManager.setUser(User("Bearer "+response.accessToken , LoginType.FROM_APP))
                }

                // Auth flow returned an error
                AuthenticationResponse.Type.ERROR -> {

                    sessionManager.setStateError(response.error)
                }
            }
        }

    }

    /**
     * if user authenticate using browser , the result
     * will come up here , we set datas in session manager
     * and session manager will handle it itself
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        var uri = intent?.getData()
        if (uri != null) {

            //this code runs when authentication is done
            // so we have to decrement idlingresource
            idlingResource.decrement()

            var response = AuthenticationResponse.fromUri(uri)

            when (response.getType()) {

                AuthenticationResponse.Type.TOKEN -> {

                    sessionManager.setUser(User("Bearer "+response.accessToken , LoginType.FROM_BROWSER))
                }
                AuthenticationResponse.Type.CODE -> {
                }
                AuthenticationResponse.Type.ERROR -> {
                }
                AuthenticationResponse.Type.UNKNOWN -> {
                }
                AuthenticationResponse.Type.EMPTY -> {
                }
            }


        }
    }

    /**
     * log outs user considering user LoginType
     * , we navigate user to login screen ,
     * if login type is FROM_BROWSER , we open log out webpage
     */
    fun performLogOut(){

        when(sessionManager.cachedUserLiveData.value?.data?.loginType){

            LoginType.FROM_BROWSER -> {performLogOutFromBrowser()}
            LoginType.FROM_APP -> {performLogOutFromApp()}
        }
        sessionManager.logOutUser()
    }

    fun performLogOutFromApp(){

        // this is not avaible now
        // we should call AuthenticationClient#clearCookies
        // here but there is a bug labled open isuue in spotify
        // authenrication SDK and clearCookies() is not avaible
        // so we navigate user to the login screen but cookies are still
        // avaible and when user press login from application , he/she will
        // eventually navigate to search screen
    }

    /**
     * navigates user to logout webpage
     */
    fun performLogOutFromBrowser(){

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.spotify.com"))
        startActivity(browserIntent)
    }
}
