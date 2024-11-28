package com.example.feedback3

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.AsyncTask
import android.util.Log

class MyJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("MyJobService", "Job started")

        // Perform your background task here
        BackgroundTask(this).execute(params)

        // Return true as the job is still running
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("MyJobService", "Job stopped")

        // Return true to reschedule the job, false to drop it
        return true
    }

    private class BackgroundTask(val jobService: JobService) : AsyncTask<JobParameters, Void, JobParameters?>() {
        override fun doInBackground(vararg params: JobParameters?): JobParameters? {
            // Simulate a long-running task
            Thread.sleep(3000)
            return params[0]
        }

        override fun onPostExecute(result: JobParameters?) {
            Log.d("MyJobService", "Job finished")
            jobService.jobFinished(result, false)
        }
    }
}