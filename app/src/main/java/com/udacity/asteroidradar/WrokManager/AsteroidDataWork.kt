package com.udacity.asteroidradar.WrokManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Data.Local.database.AsteroidDB
import com.udacity.asteroidradar.api.getPreviousDay
import com.udacity.asteroidradar.api.getSeventhDay
import com.udacity.asteroidradar.repos.AsteroidRepo
import retrofit2.HttpException

class AsteroidDataWork(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {

    // Name to uniquely identify our Work
    companion object {
        const val WORK_NAME = "RefreshAsteroidDataWork"
    }


    /**
     *  Background work to be carried out
     */
    override suspend fun doWork(): Result {

        // Database instance
        val database = AsteroidDB.getInstance(applicationContext)

        // Repository instance
        val asteroidRepository = AsteroidRepo(database)

        return try {
            // Deletes the previous day Asteroid from the database
            asteroidRepository.deletePreviousDayAsteroids()

            // Returns success for a successful work
            Result.success()
        } catch (exception: HttpException) {

            // Retry work in case of failure
            Result.retry()
        }
    }
}