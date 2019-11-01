package com.example.minispotify.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.example.minispotify.R
import com.example.minispotify.SessionManager
import com.example.minispotify.model.LoginType
import com.example.minispotify.model.User
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() , View.OnClickListener {



    override fun onClick(p0: View?) {

        when(p0!!.id){

            R.id.logOutButton -> {performLogOut()}
        }
    }

    @Inject
    lateinit var request : AuthenticationRequest
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set onclick to logOut button
        logOutButton.setOnClickListener(this)
        var navController= Navigation.findNavController(this, R.id.nav_host_fragment)

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
    }

    /**
     * resuslt of AthenticationActivity in spotify
     * SDK will be here and we fire onActivityResult
     * of current showing fragment and we handle logging
     * in user in there
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        var fragment = navHostFragment!!.getChildFragmentManager().fragments[0]
        fragment!!.onActivityResult(requestCode, resultCode, intent)

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
