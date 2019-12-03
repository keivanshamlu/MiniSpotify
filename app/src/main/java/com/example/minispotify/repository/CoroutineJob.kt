package com.example.minispotify.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoroutineJob {


    fun doSomeThing(){

        CoroutineScope(IO).launch {

            job1()
            job2()
            job3()

            withContext(Main){

                //return result
            }
        }
    }

    suspend fun job1(){

    }
    suspend fun job2(){

    }
    suspend fun job3(){

    }
}
