/**
 * {@authour w1743063 2018615}
 *
 * <p>This class provides the Main menu fragment to the
 * {@link com.w1743063.PawPassion.MainMenu}
 * </p>
 * <p>
 * This generates the question to the screen
 * </p>
 */
package com.w1743063.PawPassion.Activities.IdentifyBreed;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.w1743063.PawPassion.DogModules.DogBreed;
import com.w1743063.PawPassion.R;
import com.w1743063.PawPassion.SystemModules.SystemUI;

import java.util.Collections;
import java.util.Objects;
import java.util.Stack;

public class IdentifyBreed extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    //    Logging purpose
    private static final String TAG = "IDENTIFY BREED";

    //    Fragment Manager to manage the FrameLayout such as question and result fragments.
    static FragmentManager fragmentManager;

    //    Random images from the entire breed list for the game play.
    private Stack<String> randomImageStack;

    //    GUI element to display timer ticking values.
    private LinearLayout timerContainer;

    //    The breed which is corresponding to the 'ImageView Src:image'
    private static int selectedBreed;

    //    User play scores
    private static int[] gameSummary;

    //    To define the game status
    private static boolean isOnSession;

    // to display count down
    private TextView timer;

    // count down module
    private CountDownTimer timerModule;

    /*
    This is the correct image id
    for the challenging level to get the correct answer of the question
    this value is passed when calling the resultFragment() method
    * */
    private int selectedImageResId;

    // Initial pop screen to start the game
    private RelativeLayout starterScreen;

    // main menu Challenge switch
    private boolean lvlCheck;
    private TextView score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_breed);

        // This is to hide hide the ui controls
        SystemUI ui = new SystemUI(getWindow().getDecorView());
        // Calling the method
        ui.run();

        // fetching the button from the view
        ImageButton button_back = findViewById(R.id.button_back_breed);
        // setting on click listener for the back button
        button_back.setOnClickListener(this);

        // fetch score from the view
        score = findViewById(R.id.score_breed);

//        setting up timer for the challenge mode
        timer = findViewById(R.id.text_timer_breed);
        timer.setText("⏱");

        // initializing
        setupTimerQuestion();

        /*
          To manage the interactions of fragments associated
          with this activity.
          */
        fragmentManager = getSupportFragmentManager();


        // Check the status of the level switch activate timer if true
        timerContainer = findViewById(R.id.timer_layout_breed);


        /*
        Declaring and instantiating the DogBreed class
        to get the resources necessary for the game.
        */
        DogBreed dogBreed = new DogBreed(this);

        /*
         Fetching the resource for the images into a Stack
         So when the image is select it pops out,
         therefore the image wont be duplicated.
        */
        randomImageStack = dogBreed.getRandomImage();

        // Initializing the array
        gameSummary = new int[2];


        //  Checking the level switch is on or off to switch the timer for the hard level
        levelCheck();
    }

    private void startGame() {

          /*
         Checking the level switch is on or off
         to switch the timer for the hard level
         */
        displayQuestion();
    }

    /**
     * This triggers the next question to the user
     * The method is called externally
     */
    public void nextQuestion() {
        setScoreView();
        timerModule.cancel();
        displayQuestion();
    }


    void startTimer() {
        timerModule.start();
    }


    /*
     * Timer Module
     *
     * Initiating with the counter values with 9599 ms
     * */
    void setupTimerQuestion() {
        timerModule = new CountDownTimer(9599, 1) {
            @Override
            public void onTick(long t) {

                /* Ticker from ms to sec */
                int i = (int) (t / 1000) + 1;

                /* setting the text form the timer
                 * with the counter decrementing
                 * */
                timer.setText(
                        (String.valueOf(i))
                );
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
//                Do nothing
            }
        };
    }


    private void setScoreView() {
        //        Setting shadow
        score.setShadowLayer(6.5f, -1, 3, Color.LTGRAY);

        //        Concatenating the result
        String result = gameSummary[0] + " / " + gameSummary[1];

        //        Setting the text value to the summary TextView
        score.setText(result);

        // set visibility
        score.setVisibility(View.VISIBLE);
    }

    /**
     * <p>
     * Nothing important but just beautify to the timer
     * animating the color from green to red
     * </p>
     */
    void timerColorAnim() {

        /*
         * Animate object property
         * */
        ObjectAnimator.ofObject(
                timer, // Object to animating
                "textColor", // Property to animate
                new ArgbEvaluator(), // Interpolation function
                Color.GREEN, // Start color
                Color.RED   // End color
        ).setDuration(9500).start(); // Duration in milliseconds
    }


    /**
     * Image are fetched from the DogBreed class into the 'randomImageStack'
     * <p>
     * first the  string is fetched from the array
     * The image reference is popped from the random stack 'randomImageStack'
     * Splitting the image_Ref and the Breed_Ref
     * the string is as [ imageID @ keyValue @ BreedID ]
     */
    private void displayQuestion() {

        // Initializing the timer
        startTimer();

        // Shuffling the random stack for a randomization of items order
        Collections.shuffle(randomImageStack);

        // Split
        String[] recId = randomImageStack.pop()
                .split("@");

        //   The image resource ID is picked
        selectedImageResId = Integer.parseInt(recId[0]);

        /*
         * The breed array resource id is saved
         * to fetch the id Value from ENUM DogList
         * */
        selectedBreed = Integer.parseInt(recId[2]);

        //   Setting up the question for the user
        manageFrame(selectedImageResId);

        //   Setting true to prevent user from doing todo
        isOnSession = true;

    }


    /**
     * Check whether the level switch is on if so to turn on timer
     */
    private void levelCheck() {


        // Get the intent of the previous activity
        Intent intent = getIntent();

        /*
          get the attached bundle from the intent
          Fetching the values sent from home screen activity
          */
        Bundle extras = intent.getExtras();

        /*
         * Start screen
         * Assigning the Start screen with the play button
         */
        starterScreen = findViewById(R.id.starter_Filter_breed);

        /*
         * Confirming the availability assert
         * Checks for switch state to set level
          if lvl has false the timer hides
          else the timer will be visible
          */
        assert extras != null;
        lvlCheck = (Objects.requireNonNull(
                extras.getBoolean("lvl")
        ));

        if (lvlCheck) {
            // Hiding the starter screen and starting the game play.
            starterScreen.setVisibility(View.VISIBLE);
            // Showing  the timer since the level switch is on
            timerContainer.setVisibility(LinearLayout.VISIBLE);


            // The big blue play button
            ImageButton ib = findViewById(R.id.button_play_breed);

            // Setting the listener
            ib.setOnClickListener(this);

        } else {
//       Hiding the starter screen and starting the game play.
            starterScreen.setVisibility(View.GONE);
//          Showing  the timer since the level switch is on
            timerContainer.setVisibility(LinearLayout.GONE);

            /*
             * Action Begins
             * The game starts
             * */
            startGame();
        }

    }


    /**
     * <p>
     * This method displays the question by loading up the argument bundle with the
     * necessary data.
     * Then finally triggers the fragment into the FrameLayout.
     * </p>
     *
     * @param imgRes (int) this arg gets the image resource for the
     *               image recourse id to display the question .
     */
    private void manageFrame(int imgRes) {

        //        Declaring the fragment manager
        Fragment fragBreedQuestion = new FragBreedQuestion();

        //        Declaring the Bundle to pass data to the frag arguments
        Bundle resData = new Bundle();

        //       Inserting Image selected resource ID
        resData.putInt("IMAGE_RES_ID", imgRes);

        //       Inserting selected breed Resource ID
        resData.putInt("BREED_RES_ID", selectedBreed);

        //        Is the answer correct
        resData.putBoolean("AUTO_CLICK", lvlCheck);

        //        Setting the bundle to the argument
        fragBreedQuestion.setArguments(resData);

        //        Beginning the Transaction along with the arguments
        fragmentManager.beginTransaction()
                .replace(
                        R.id.breed_fragmentContainer, // FrameLayout Container
                        fragBreedQuestion // Arguments
                ).commit();
    }

    /**
     * This method is called externally from the fragment question to display the result
     *
     * @param correctId    the correct breed id which is selected for the current play.
     * @param isCorrect    Validates the user answer against the correct this boolean holds it.
     * @param userAnswerId the users answer which is sent bu
     */
    public void showResult(boolean isCorrect, int userAnswerId, int correctId) {

        score.setVisibility(View.INVISIBLE);
        // Terminating the timer
        timerModule.cancel();

        /*
         * If the users answer is correct
         * */
        if (isCorrect) {

            gameSummary[0] += 1; // Number of Correct

            gameSummary[1] += 1; // Number of games played

        } else {
            gameSummary[1] += 1; // Number of games played
        }

        /*
         * Calling the fragment for the result
         * */
        resultFragment(isCorrect, userAnswerId, correctId);
    }


    /**
     * <p>
     * This is the result fragment that display the result
     * after each answering of the question
     * <p>
     * This method bundles up all the
     * resources required to the result fragment ans the n
     * passes it to the fragment arguments
     * <p>
     * Bundle contains :
     * ---* Correct Answer
     * ---* Correct and user input Image resource
     * ---* Game Summary (Correct / incorrect)
     * ---* Challenge mode state
     *
     *
     * </p>
     *
     * @param isCorrect     the result of the corrects' and wrongs'
     * @param userAnswerId  the user selected Breed ID
     * @param correctAnswer the correct Breed ID
     */
    private void resultFragment(boolean isCorrect, int userAnswerId, int correctAnswer) {

        /*
        Bundle to hold the values
        * */
        Bundle resData = new Bundle();

//        Declaring the fragment manager
        Fragment fragDogResult = new FragBreedResult();

//        Is the answer correct
        resData.putBoolean("CORRECT", isCorrect);

//        Is the answer correct
        resData.putBoolean("AUTO_CLICK", lvlCheck);


//       User Selected answer selected answer
        resData.putInt("answerIDUser", userAnswerId);

//        Correct Answer
        resData.putInt("answerIDCrt", correctAnswer);

//        Result of correct and incorrect answers
        resData.putIntArray("SUMMARY", gameSummary);

//        Amending the data bundle to the fragment arguments
        fragDogResult.setArguments(resData);

//        Beginning the fragment
        fragmentManager.beginTransaction()
                .replace(
                        R.id.breed_fragmentContainer, // FrameLayout
                        fragDogResult // argument
                ).commit();
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

        // The view ID is filtered by its Resource ID.
        switch (v.getId()) {
            case (R.id.button_back_breed): //Back Button

                /*
                 * if the session is on ,this prevents from closing.
                 * */
                if (!isOnSession) {

                    // Terminate the timer Module
                    timerModule.cancel();

                    // Close this activity
                    IdentifyBreed.super.finish();

                } else

                    // Confirmation in it has the close method
                    confirmation();

                break;
            case (R.id.button_play_breed): // Big blue button Play

                // Removing the filter to start the game
                starterScreen.setVisibility(View.GONE);

                /*
                 * Action :)
                 * the game starts
                 * */
                startGame();

                break;
            default:
                break;
        }
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

            /*
             * Ending the activity
             * */
            IdentifyBreed.super.finish();
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
        AlertDialog userConfirmation = confBuilder.create();

        // show message
        userConfirmation.show();
    }
}
