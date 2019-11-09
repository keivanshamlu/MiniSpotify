package com.example.minispotify.util


import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class LiveDataTestUtil<T> {

    @Throws(InterruptedException::class)
    fun getValue(liveData: LiveData<T>): T? {

        val data = ArrayList<T>()

        // latch for blocking thread until data is set
        val latch = CountDownLatch(1)

        val observer = object : Observer<T> {
            override fun onChanged(t: T) {
                data.add(t)
                latch.countDown() // release the latch
                liveData.removeObserver(this)
            }
        }
        try {
            liveData.observeForever(observer)
        }catch (e : Exception){
            liveData.observeForever(observer)
        }

        try {
            latch.await(2, TimeUnit.SECONDS) // wait for onChanged to fire and set data
        } catch (e: InterruptedException) {
            throw InterruptedException("Latch failure")
        }

        return liveData.value
    }

}

