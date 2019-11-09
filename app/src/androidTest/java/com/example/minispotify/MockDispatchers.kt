package com.example.minispotify

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest


/**
 * for implementing request responses when application is under testing
 *
 */
internal class MockServerDispatcher(var responseCode : Int) {

    /**
     * Return ok response from mock server
     */
    internal inner class RequestDispatcher : Dispatcher() {

        /**
         * in this method we get the request and specify it as
         * request path prefix and according to that we get
         * response from assets json file that contains fake response
         */
        override fun dispatch(request: RecordedRequest): MockResponse {


            if (request.path!!.startsWith( "/v1/search")) {
                val jsonfile: String = getInstrumentation().getTargetContext().assets.open("search_result.json").bufferedReader().use {it.readText()}
                return MockResponse().setResponseCode(responseCode).setBody(jsonfile)
            }

            else if(request.path!!.startsWith( "/v1/audio-features")){

                val jsonfile: String = getInstrumentation().getTargetContext().assets.open("audio_features_result.json").bufferedReader().use {it.readText()}
                return MockResponse().setResponseCode(responseCode).setBody(jsonfile)
            }

            return MockResponse().setResponseCode(404)
        }
    }


    /**
     * Return error response from mock server
     */
    internal inner class ErrorDispatcher : Dispatcher() {

        override fun dispatch(request: RecordedRequest): MockResponse {

            return MockResponse().setResponseCode(400)

        }
    }
}