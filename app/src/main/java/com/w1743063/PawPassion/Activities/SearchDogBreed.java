/**
 * @author W1743063 2018615
 * <p>
 * This activity has the slide show management and search breeds in the system
 * <p>
 * once a valid breed is searched the image viewer will play the image
 * slide show and switch image at every 5 second
 * <p>
 * for the infinite loop of the image sequence with a random order of images in
 * every sequence
 * <code>
 * int moder = imageSources.size();
 * <p>
 * if (picker % moder == moder - 1)
 * Collections.shuffle(imageSources);
 * <p>
 * sliderScreen.setImageResource(
 * imageSources.get(picker++ % moder)
 * );
 * </code>
 * <p>
 * and a stop button is animated for a better ui feature
 */


package com.w1743063.PawPassion.Activities;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.w1743063.PawPassion.DogModules.BreedList;
import com.w1743063.PawPassion.R;
import com.w1743063.PawPassion.SystemModules.SystemUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SearchDogBreed extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {
    // Debug purpose
    private final String TAG = "SEARCH BREED";
    //
    // The session is on when the play starts
    private boolean isOnSession;
    // Timers to show the tick countdown
    private TextView timer;
    // Timer Module to get the sequence
    private CountDownTimer timerModule;

    // Autocomplete text
    private AutoCompleteTextView textKeyWord;

    // Image resources ID.
    private List<Integer> imageSources = new ArrayList<>();


    // the indexing for the image resource from typed array..
    private int picker;


    // Set defines whether the question is visible.
    private boolean set = false;

    // Timeline to show the count down progress.
    private ProgressBar timerLine;

    // Image viewer to show the question image
    private ImageView sliderScreen;


    //  Stop Button
    private ImageButton button_stop;


    // Button search
    private ImageButton button_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dog_breed);

        // This is to hide hide the ui controls
        SystemUI ui = new SystemUI(getWindow().getDecorView());
        // Calling the method
        ui.run();

        /*
         * Get buttons
         * */
        ImageButton button_back = findViewById(R.id.button_back_search);
        ImageButton button_info = findViewById(R.id.button_hint_search);

        /*
         * Adapter to set items to spinner
         * */
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this,
                        android.R.layout.simple_dropdown_item_1line,
                        fetchBreedList()
                );

        /*
         * Get timer and set
         * */
        timer = findViewById(R.id.text_timer_search);
        timer.setText("⏱");

        //  Initialize the timer to do the slide show
        setupTimer();

        // get the progress bar to indicate the timer progress
        timerLine = findViewById(R.id.timer_progress);

        // ImageViewer to show the slide show
        sliderScreen = findViewById(R.id.img_viewer_search);

        // Search button
        button_search = findViewById(R.id.button_submit_keyword);

        // stop button
        button_stop = findViewById(R.id.button_stop_slide);



        /*
         * Set listener
         * */
        button_search.setOnClickListener(this);
        button_stop.setOnClickListener(this);
        button_back.setOnClickListener(this);
        button_info.setOnClickListener(this);

        /*
         * editText to get user desired keyword.
         * */
        textKeyWord = findViewById(R.id.search_text);
        textKeyWord.setShadowLayer(6.5f, -1, 3, Color.LTGRAY);
        textKeyWord.setElevation(3);

        /*
         * Timer line Properties
         * */
        timerLine.setFocusable(false);
        timerLine.setPadding(10, 10, 10, 10);
        timerLine.setVerticalScrollbarPosition(10);
        textKeyWord.setAdapter(adapter);

        textKeyWord.setText("");

    }


    /*
    Timer Module
    Initiating with the counter values with 9599 ms
     * */
    private void setupTimer() {

        timerModule = new CountDownTimer(5199, 1) {

            @Override
            public void onTick(long t) {

                /*
                 Set progress to the bar
                 */
                timerLine.setProgress((int) ((4999 - t) / 50), true);

                /* Ticker from ms to sec */
                int i = (int) ((5199 - t) / 1000);

                /*
                 * setting the text form the timer
                 * with the counter decrementing
                 */
                timer.setText(
                        (String.valueOf(i))
                );


                /*
                 *  is true when the question is set
                 * */
                if (!set) {

                    /*
                     * Animate the font color
                     * */
                    timerColorAnim();

                    /*
                     * Setting Image
                     * */
                    setImage();

                    set = true; // set true if the question is display
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {

                timerLine.setProgress(0); // reset progress bar.

                set = false; // set true if the question is display

                timerModule.start(); // start the timer module
            }
        };
    }

    private void timerColorAnim() {
        ObjectAnimator.ofObject(
                timer, // Object to animating
                "textColor", // Property to animate
                new ArgbEvaluator(), // Interpolation function
                Color.GREEN, // Start color
                Color.RED   // End color
        ).setDuration(4500).start(); // Duration in milliseconds
    }


    @SuppressLint("Recycle")
    private void setImage() {

        int moder = imageSources.size(); // get size to determine mod

        /*
         * IF THE CYCLE FINISHED , shuffle the list for a new random sequence
         * */
        if (picker % moder == moder - 1)
            Collections.shuffle(imageSources);


        /*Log.i(TAG, "setImage: " +
                moder + "*-*-*-" + picker + "*-" + picker % moder
        );*/

        /*
         * Setting image to the image viewer
         * */
        sliderScreen.setImageResource(
                // Mods the value to keep the bound within the array size
                imageSources.get(picker++ % moder)
        );
    }

    private void stopSlide() {
        if (isOnSession) {

            textKeyWord.setText(""); // reset text box

            animateStop(); // animate the whistle

            timer.setText("⏱"); // initial

            sliderScreen.setImageResource(R.drawable.source); // set Image to the viewer

            timerModule.cancel();  // cancel the timer

            timerLine.setProgress(0, true); // resetting the progress

            isOnSession = false;

        } else {

            /*
             * Roasting uo the toast for a quick notification on
             * Clicking on the stop button.
             * */
            Toast.makeText(this,
                    "\uD83D\uDE01 Are you boarded \uD83D\uDE2C",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private List<String> fetchBreedList() {
//          Inserting into the item list
        List<String> items = new ArrayList<>();

        //      Iterating through the breed  list and feeding into the list 'items'
        for (BreedList value : BreedList.values()) {

            /*
                 Adding the Breed name to the list
            */
            items.add(
                    value.toString()
                            /* Stripping off "_" and replacing with '[empty space]'*/
                            .replace("_", " ")
            );
        }

//        return  the Breed list to use in the adapter
        return items;
    }


    /**
     * <p>
     * This method is called on the press of the submit search button.
     * <p>
     * This captures the text box for the desired breed list.
     * then it is replaced with a "_" on blank space to get the
     * source if of the breed image to fetch the array of images from the
     * drawable resource.
     * {@code textKeyWord.getText()).toUpperCase().replace(" ", "_"); }
     * <p>
     * then is selected from the breed is available
     * {@code  BreedList.getResourceId(breedSelected); }
     * and
     * then obtain the typed array
     * {@code }obtainTypedArray(breedId); }
     * <p>
     * Once breed is found the typed array is looped through to fetch the image
     * resource ID to use in the image viewer.
     * <code>
     * for (int i = 0; i < typedArrayImages.length(); i++) {
     * imageSources.add(
     * typedArrayImages.getResourceId(i, -1)
     * );
     * }
     * </code>
     *
     *
     *
     *
     * </p>
     */
    @SuppressLint("Recycle")
    private void searchBreed() {

        String breedSelected =
                String.valueOf(textKeyWord.getText()).toUpperCase()
                        .replace(" ", "_");

        if (breedSelected.isEmpty()) {
            Toast.makeText(this,
                    "Enter a valid Breed",
                    Toast.LENGTH_SHORT).show();

        } else {

            /*
             * Returns -1 if not found
             * */
            int breedId = BreedList.getResourceId(breedSelected);

            if (breedId != -1) {

                // This stores the image resources
                TypedArray typedArrayImages = getResources().obtainTypedArray(breedId);


                // Loop to fetch int id of the resource
                for (int i = 0; i < typedArrayImages.length(); i++) {
                    imageSources.add(
                            typedArrayImages.getResourceId(i, -1)
                    );
                }

                animateStop(); // animate stop button

                hideKeyboard(); // hide the keyboard

                set = false; // set false because the question is not yet set

                isOnSession = true; // yes now starting of a new session

                timerModule.start(); // staring the ticker that implements the method calls

            } else

                /*
                 * ---- Toast ----- Toast ------ Toast ----- Toast
                 * :P :P :P :P
                 * this is an invalid breed condition
                 *  */
                Toast.makeText(this,
                        "Invalid Breed",
                        Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * Animate the Stop button
     * */
    private void animateStop() {

        /*
         * If session is on scale down
         */
        if (!isOnSession) {

            // rotate
            button_stop.animate()
                    .rotation(1080f).setDuration(600).start();

            // scale up
            button_stop.animate()
                    .scaleX(1f).setDuration(500).setStartDelay(300);

            // scale up
            button_stop.animate()
                    .scaleY(1f).setDuration(600).setStartDelay(300);

        } else {

            // rotate
            button_stop.animate()
                    .rotation(360f).setDuration(600).start();
            // scale down
            button_stop.animate()
                    .scaleX(.7f).setDuration(600).setStartDelay(300);
            // scale down
            button_stop.animate()
                    .scaleY(.7f).setDuration(500).setStartDelay(300);
        }
    }

    /**
     * This class implements the View.OnClickListener interface
     * therefore this is an over ridden method of the interface
     * to listen to the button click
     *
     * @param v View that was clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //        The view ID is filtered by its Resource ID.
            case (R.id.button_submit_keyword): //Back Button

                isOnSession = false; // ending the current session

                /*
                 * Animation for the stop button
                 * */
                button_search.animate().rotation(360f).setDuration(480).start();
                button_search.animate().rotation(-360f).setDuration(500).setStartDelay(500).start();

                searchBreed();

                break;

            case (R.id.button_stop_slide):

                stopSlide();

                break;

            case (R.id.button_back_search): // Back Button

                /*
                 * if the session is on ,this prevents from closing.
                 * */
                if (!isOnSession) {

                    timerModule.cancel(); //  Terminate the timer Module

                    SearchDogBreed.super.finish(); // Close this activity

                } else

                    confirmation(); // Confirmation in it has the close method

                break;

            case (R.id.button_hint_search): // List breeds

                info(); // This poops up a dialog box with available breed names
                break;

            default:
                break;
        }

    }

    /*
     * Hide the Keyboard
     */
    private void hideKeyboard() {
        InputMethodManager systemService =
                (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        Objects.requireNonNull(systemService).hideSoftInputFromWindow(
                Objects.requireNonNull(
                        this.getCurrentFocus())
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS
        );
    }


    /**
     * This method  Pops up a dialog box with some available breed list
     * <p>
     * finally it pops open the dialog box,
     * touch on the background close the dialog box
     */
    @SuppressLint("ResourceType")
    public void info() {


        // Dialog box builder
        Dialog confirmationBuilder = new Dialog(this);


        // Passing the layout Resource id to set te content
        confirmationBuilder.setContentView(R.layout.dialog_info);

        /*
         * Showing the dialog box,
         * an interruption on screen ans the box disappears
         */
        confirmationBuilder.show();
    }


    /**
     * This class implements the View.OnClickListener interface
     * therefore this is an over ridden method of the interface
     * to listen to the button click
     *
     * @param dialog View that was clicked
     * @param which  the button
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == -1) {

            /*
             * Tossing up the toast
             * */
            Toast.makeText(
                    this, "Session ended \uD83D\uDC4B", Toast.LENGTH_SHORT).show();


            // Ending the activity
            SearchDogBreed.super.finish();
        } else {

            /*
             * A quick toast for brunch :)
             * Happy feedback
             * */
            Toast.makeText(
                    this,
                    "Good decision," +
                            "\uD83D\uDC36 ", // emoji
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Confirmation  dialog to terminate
     */
    public void confirmation() {


        //  Dialog box builder
        AlertDialog.Builder confBuilder = new AlertDialog.Builder(this);

        // message
        confBuilder.setMessage("Are you sure, You wanted to close session?");

        // Yes button
        confBuilder.setPositiveButton("YES ✔️", this);

        // no Button
        confBuilder.setNegativeButton("NO ❌", this);

        // create the alert dialog
        AlertDialog alertDialog = confBuilder.create();


        alertDialog.show(); // show message
    }

}
