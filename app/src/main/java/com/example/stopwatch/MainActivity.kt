package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    //Variables
    lateinit var stopwatch: Chronometer // the stopwatch
    var running = false                //is the stopwatch
    var offset: Long = 0            //The base offset

    // Key string for use with the bundle
    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get a reference to the stopwatch
        stopwatch = findViewById<Chronometer>(R.id.stopwatch)


        //Restore the previous state (Bundle)
        if(savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if(running){
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            }else{
                setBaseTime()
            }
        }


        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            if(!running){
                // Set base time - Function
                setBaseTime()
                // Start the stopwatch
                stopwatch.start()

                // Set running =true
                running= true

            }
        }

        // the pause button pauses the stopwatch if it's running
        val pauseButton = findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener {
            if (running) {
                // save offset <-- reset back down to 0
                saveOffset()
                // stop the stopwatch
                stopwatch.stop()
                // set running =false
                running = false
            }
        }
        val incButton = findViewById<Button>(R.id.increment)
        incButton.setOnClickListener {
            offset -= (+5000)
            setBaseTime()
        }
        val decButton = findViewById<Button>(R.id.decrement)
        decButton.setOnClickListener {
            offset -= (-5000)
            setBaseTime()
        }



    //the reset button sets the offset and stopwatch to 0
    val resetButton = findViewById<Button>(R.id.reset_button)
    resetButton.setOnClickListener {
        //offset set to 0
        offset = 0
        //reset stopwatch
        setBaseTime()
        }
    }


    override fun onStop(){
    super.onStop()
        if(running){
            setBaseTime()
            stopwatch.stop()
        }
    }
    override fun onRestart() {
        super.onRestart()
        if(running){
            setBaseTime()
            stopwatch.start()
            offset = 0
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY, running)
        savedInstanceState.putLong(BASE_KEY, stopwatch.base)
        super.onSaveInstanceState(savedInstanceState)
    }
    override fun onPause() {
        super.onPause()
        if(running){
            saveOffset()
            stopwatch.stop()
        }
    }
    override fun onResume(){
        super.onResume()
        if(running){
            setBaseTime()
            stopwatch.start()
            offset = 0
        }
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }

    private fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }
}