/**
 * {@authour w1743063 2018615}
 *
 * <p>This class provides the Main menu fragment to the
 * {@link com.w1743063.PawPassion.MainMenu}
 * </p
 * * This method uses a {@link android.os.CountDownTimer }
 * * module to do the sequencing of the quiz.>
 * <p>
 * This generates the question to the screen
 * </p>
 */
package com.w1743063.PawPassion.Activities.IdentifyDog;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.w1743063.PawPassion.Activities.IdentifyBreed.FragBreedResult;
import com.w1743063.PawPassion.DogModules.DogBreed;
import com.w1743063.PawPassion.MainMenu;
import com.w1743063.PawPassion.R;
import com.w1743063.PawPassion.SystemModules.SystemUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Stack;
import java.util.function.Consumer;

// Foreach looper

public class IdentifyDog extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener {

    //    Logging purpose
    private static final String TAG = "{ IDENTIFY THE DOG (LOG)}";

    //    Fragment Manager to manage the FrameLayout such as question and result fragments.
    static FragmentManager fragmentManager;

    //    Random images from the entire breed list for the game play.
    private Stack<String> randomImageStack;

    //    GUI element to display timer ticking values.
    private LinearLayout timerContainer;

    //    Mystery image position ID.
    private static int selectedHiddenImage;

    //    The breed which is corresponding to the 'selectedHiddenImage'
    private static int selectedBreed;

    private int selectedImageResId;

    //    User play scores
    private static int[] gameSummary;

    //    To define the game status
    private static boolean isOnSession;
    // main menu Challenge switch

    private boolean lvlCheck;

    //  check whether question is set
    private boolean isSet;

    // count down module
    private CountDownTimer timerModule;

    // Initial pop screen to start the game
    private RelativeLayout starterScreen;

    // to display count down
    private TextView timer;

    //  manage Frame and fragments
    private Fragment fragManage;
    private TextView score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_dog);


        if (savedInstanceState != null) {
            fragManage = getSupportFragmentManager()
                    .getFragment(savedInstanceState, "GAME_FRAGMENT_A");

            fragManage = getSupportFragmentManager()
                    .getFragment(savedInstanceState, "GAME_FRAGMENT_Q");
        } else {
            fragManage = new FragBreedResult();
            fragManage = new FragBreedResult();
        }


        SystemUI ui = new SystemUI(getWindow().getDecorView());
        ui.run();

        ImageButton button_back = findViewById(R.id.button_back_dog);

        button_back.setOnClickListener(this);

        // setting score testier source
        score = findViewById(R.id.score_dog);

        // setting timer testier source
        timer = findViewById(R.id.text_timer_dog);

        // setting dummy text to timer
        timer.setText("⏱");

        // Initializing challenge mode timer module
        setupTimerQuestion();

        /*
          To manage the interactions of fragments associated
          with this activity.
          */
        fragmentManager = getSupportFragmentManager();


//        Check the status of the level switch activate timer if true
        timerContainer = findViewById(R.id.timer_layout_dog);



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

        /*
         * Initializing the array
         */
        gameSummary = new int[2];


        /*
         Checking the level switch is on or off to switch the timer for the hard level
         */
        levelCheck();

    }

    private void startGame() {
          /*
         Checking the level switch is on or off
         to switch the timer for the hard level
         */
        if (!lvlCheck) {
            displayQuestion();
        } else {
            isSet = false;
            timerModule.start();
        }
    }

    /* todo
     * This triggers the next question to the user
     * The method is called externally
     *
     */
    public void nextQuestion() {
        setScoreView();
        if (!lvlCheck) {
            displayQuestion();
        } else {
            isSet = false;

            timerModule.start();
        }
    }


    private void setupTimerQuestion() {

        timerModule = new CountDownTimer(9599, 1) {
            @Override
            public void onTick(long t) {
                if (!isSet) {
                    timerColorAnim();
                    displayQuestion();
                    isSet = true;
                }
                int i = (int) (t / 1000) + 1;

                timer.setText(
                        (String.valueOf(i))
                );
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                timerColorAnim();
                isSet = false;
                checkAnswer(
                        new int[]{-99, selectedImageResId}
                        , -99);
            }
        };
    }


    @SuppressLint("ObjectAnimatorBinding")
    private void timerColorAnim() {
        ObjectAnimator.ofObject(
                timer, // Object to animating
                "textColor", // Property to animate
                new ArgbEvaluator(), // Interpolation function
                Color.GREEN, // Start color
                Color.RED   // End color
        ).setDuration(9500).start(); // Duration in milliseconds
    }


    /*
     * Check whether the level switch is on if so to turn on timer
     * */
    private void levelCheck() {
        /*
         Get the intent of the previous activity
          */
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
        starterScreen = findViewById(R.id.starter_Filter_dog);
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
//       Hiding the starter screen and starting the game play.
            starterScreen.setVisibility(View.VISIBLE);
//          Showing  the timer since the level switch is on
            timerContainer.setVisibility(LinearLayout.VISIBLE);

            ImageButton ib = findViewById(R.id.button_play_dog);

            ib.setOnClickListener(this);

        } else {
//       Hiding the starter screen and starting the game play.
            starterScreen.setVisibility(View.GONE);
//          Showing  the timer since the level switch is on
            timerContainer.setVisibility(LinearLayout.GONE);

            startGame();
        }

    }


    /**
     * <p>
     * This fetches the images from the 'selectedImages' array by popping 3 images.
     * this method calls in the  fetchThreeImages() which selects 3 random images into the
     * selected image array.
     * </p>
     * <p>
     * {@code fetchThreeImages() } is declared for code readability and a better understanding
     * in how the image is randomly selected in a unique way.
     * </p>
     */
    private void displayQuestion() {

//        Final 3 images going into the game round
        ArrayList<Integer> selectedImages = new ArrayList<>();

//      To store the images Resource ID
        List<Integer> pickedBreeds = new ArrayList<>();

        Random ran = new Random();

        /*<p>
         * This method is to fetch 3 random image and
         * insert into the selected image array to
         * pass into the fragment arguments
         *  </p>
         * */
        fetchThreeImages(selectedImages, pickedBreeds, ran);

        Log.i("TAG", "selectedImage: " + selectedHiddenImage);

//        Question answer image source
        selectedImageResId = selectedImages.get(selectedHiddenImage);

//        Setting up the question for the user
        manageFrame(selectedImages);

//        Setting true to prevent user from doing todo
        isOnSession = true;
        timerColorAnim();
    }


    /**
     * <p>
     * This method iterates a for loop and pops to fetch 3 images
     * from the {@code randomImageStack}. So once the 3 random images
     * and 3 random breed has been selected the values are stored in
     * {@code selectedImages}.
     * <p>
     * But sometimes the image from the same breed may be selected to the
     * {@code randomImageStack} their fore the loop decrements the incrementer value
     * and re runs the loop for a new image, thus the last popped image's breed is
     * already selected to the array, this time it saves in a {@code temp}
     * will be put back into the { randomImageStack } after successfully selecting
     * the 3 random images.
     * </p>
     */
    private void fetchThreeImages(
            ArrayList<Integer> selectedImages,
            List<Integer> pickedBreeds,

            Random ran) {
        //      Randomly picked breed for the game
        selectedHiddenImage = (ran.nextInt(3));

        /*
         To store the image references if it is
         already selected to the selectedImages array.
         */
        List<String> tempImage = new ArrayList<>();


        /*
         Fetching the 3 images for the options
        * */
        for (int i = 0; i < 3; i++) {

//           Shuffling the random image array
            Collections.shuffle(randomImageStack);

//          Splitting the string and segmenting the image resource id and the breed ID.
            String pickedImage;
            try {
                pickedImage = randomImageStack.pop();
            } catch (EmptyStackException e) {
                // TODO: 3/7/2020
                endGame();
                Intent ii = new Intent(this, MainMenu.class);
                startActivity(ii);
                return;
            }

            /*
            Splitting the image_Ref and the Breed_Ref
            the string is as [ imageID @ keyValue @ BreedID ]
             */
            String[] recId = pickedImage.split("@");

//                Checking pickedBreeds is not empty
            if (!pickedBreeds.isEmpty()) {

//                    Checking whether the breed is already picked by the current game
                if (

                        pickedBreeds.contains(  /* checking for existence */
                                (
                                        /* currently selected breed */
                                        Integer.parseInt(recId[2])


                                )
                        )
                ) {
                    Log.i(TAG, "fetchThreeImages: The image already exist");
                    /*
                     * if selected, decrementing the incrementer
                     *  for a re-loop of the current value
                     * */
                    i--;
                    /*
                     * <p> this stores the resource which its breed was already selected
                     * more than once for the play.</p>
                     *
                     * <p>inserting ti to the temp since these
                     * images are never used in the game </p>
                     */
                    tempImage.add(pickedImage);

//                    Shuffling the random stack
                    Collections.shuffle(randomImageStack);

                }
                /*<p> Else inserting the selected image reference
                 * into the  [ selectedImages ] array </p>
                 *  */
                else {
                    /*
                     * Method to store the image reference to the selected image array
                     * */
                    storeReference(selectedImages, i, recId);

                    /*
                    Inserting into the picked breed list to avoid images being selected from the same breed
                     */
                    pickedBreeds.add(Integer.valueOf(recId[2]));

                    // TODO: 3/7/2020
                    if (selectedHiddenImage != i)
                        tempImage.add(pickedImage);
                }
            } else {
                /*
                 * Method to store the image reference to the selected image array
                 * */
                storeReference(selectedImages, i, recId);

                /*
                Inserting into the picked breed list to avoid images being selected from the same breed
                 */
                pickedBreeds.add(Integer.valueOf(recId[2]));
                // TODO: 3/7/2020
                if (selectedHiddenImage != i)
                    tempImage.add(pickedImage);

            }
        }

        /*
         * Re inserting the image reference into the pop stack for reuse as correct answers
         * */
        tempImage.forEach(
                new Consumer<String>() {
                    @Override
                    public void accept(String e) {
                        randomImageStack.push(e);
                    }
                }
        );
        tempImage.clear();
    }

    /**
     * <p> This method stores the reference of the image and the breed to the respective arrays
     * Reason for declaring a method is that this set of functions are used at 2 places thus
     * this makes less code duplication </p>
     *
     * @param selectedImages Hidden correct breed's image position
     * @param i              incrementer and the image position
     * @param recId          this is an array of strings which has
     *                       [ image_Red , count ,Breed_Ref ]
     */
    private void storeReference(ArrayList<Integer> selectedImages, int i, String[] recId) {
        /*
         * this is the image that is selected
         * behind the scene for the correct answer
         *   */
        if (selectedHiddenImage == i) {
            /*
             * Retaining the correct breed id to compare with the user guessing
             *
             */
            selectedBreed = Integer.parseInt(recId[2]);

//                Inserting the image reference for the selected image array
            selectedImages.add(Integer.valueOf(recId[0]));

        } else {

            /*
             *  Inserting the image reference for the optional selected image array
             *  Two optional image for challenging the player
             *  recId[0] = the reference of the image
             * */
            selectedImages.add(Integer.valueOf(recId[0]));

//            /*
//             * Re inserting the image reference into the pop stack for reuse as correct answers
//             * */
//            tempImage.add(pickedImage);
        }
    }


    /**
     * <p>
     * This method displays the question by loading up the argument bundle with the
     * necessary data.
     * Then finally triggers the fragment into the FrameLayout.
     * </p>
     *
     * @param imgRes (int[]) this arg gets the image resource for
     *               the 3 images recourse id to display the question options.
     */
    private void manageFrame(ArrayList<Integer> imgRes) {

//        Declaring the fragment manager
        fragManage = new FragDogQuestion();

//        Declaring the Bundle to pass data to the frag arguments
        Bundle resData = new Bundle();

        /*
        This value decides which is the image corresponding to
        the breed displayed as the question
        inserting the value to the args bundle.
         */
        resData.putInt("HIDDEN_IMAGE_POS", selectedHiddenImage);


//        was the question answered
        resData.putBoolean("AUTO_CLICK", lvlCheck);

        /*
         This value is to retrieve the Corresponding breed of the selected image
        */
        resData.putInt("SELECTED_BREED", selectedBreed);

//       Inserting Image resource ID
        resData.putIntegerArrayList("IMAGE_ID", imgRes);

//        Setting the bundle to the argument
        fragManage.setArguments(resData);

//        Beginning the Transaction along with the arguments
        fragmentManager.beginTransaction()
                .replace(
                        R.id.dog_fragmentContainer, // FrameLayout Container
                        fragManage // Arguments
                ).commit();
    }

    /**
     * This method checks weather the answer is correct or wrong
     * and then set the corresponding values and pass it into the
     * {@code resultFragment()}
     *
     * @param imgResourceId gets two indexes with
     *                      [ Answer Image ID ] and [ Correct Image Id ]
     * @param userAnswer    is the image position the user pressed on
     */
    public void checkAnswer(int[] imgResourceId, int userAnswer) {

//        To hold the value is
        boolean isCorrect;
        timerModule.cancel();

        if (userAnswer == selectedHiddenImage) {
//           Number of Correct
            gameSummary[0] += 1;

//            Number of games played
            gameSummary[1] += 1;

//            Setting true that the answer is correct
            isCorrect = true;

        } else {
//            Setting true that the answer is wrong
            isCorrect = false;
            gameSummary[1] += 1;
        }

        // calls the result fragment passing { isCorrect, imgResourceId }
        resultFragment(isCorrect, imgResourceId);

    }

    /**
     * <p>
     * This method displays the result by loading up the argument bundle with the
     * necessary data.
     * Then finally triggers the fragment into the FrameLayout.
     * </p>
     *
     * @param correctAnswer (boolean) this arg gets a boolean value weather
     *                      the answer is correct or wrong.
     * @param imgRes        (int[]) this arg gets the image resource for the correct and the user
     *                      answer image recourse id to display the result.
     */
    private void resultFragment(boolean correctAnswer, int[] imgRes) {

        score.setVisibility(View.INVISIBLE);

        /*
        Bundle contains :   Correct Answer
                            Correct and user input Image resource
                            Game Summary (Correct / incorrect)
        */
        Bundle resData = new Bundle();

//        Declaring the fragment manager
        fragManage = new FragDogResult();

//        Is the answer correct
        resData.putBoolean("CORRECT", correctAnswer);

        //        Is the answer correct
        resData.putBoolean("AUTO_CLICK", lvlCheck);


//        Both correct and user selected answer
        resData.putIntArray("IMG_RESOURCE", imgRes);

//        Result of correct and incorrect answers
        resData.putIntArray("SUMMARY", gameSummary);

//        Amending the data bundle to the fragment arguments
        fragManage.setArguments(resData);

//        Beginning the fragment
        fragmentManager.beginTransaction()
                .replace(
                        R.id.dog_fragmentContainer,
                        fragManage
                ).commit();
    }

    private void setScoreView() {

        //        Setting shadow
        score.setShadowLayer(6.5f, -1, 3, Color.LTGRAY);

        //        Concatenating the result
        String result = gameSummary[0] + " / " + gameSummary[1];

        //        Setting the text value to the summary TextView
        score.setText(result);

        score.setVisibility(View.VISIBLE);
    }


    /*
     * When the entire images resource is used the game automatically ends.
     * */
    private void endGame() {
        Toast.makeText(
                this,
                "You were questioned with entire breed images",
                Toast.LENGTH_LONG).show();

//        Entering home page
        IdentifyDog.super.finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.button_back_dog):
                /*
                 * if the session is on ,this prevents from closing.
                 * */
                if (!isOnSession) {

                    timerModule.cancel(); // Terminate the timer Module

                    IdentifyDog.super.finish(); // Close this activity

                } else

                    /*
                     * Confirmation in it has the close method
                     * */
                    confirmation();

                break;
            case (R.id.button_play_dog): // Big blue button Play

                /*
                 * Removing the filter to start the game
                 * */
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


    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == -1) {
//            Yes button has been pressed
            timerModule.cancel();
            Toast.makeText(
                    this, "Session ended \uD83D\uDC4B", Toast.LENGTH_SHORT).show();
            IdentifyDog.super.finish();
        } else {

//            No button has been pressed
            Toast.makeText(
                    this, "Good decision,\uD83D\uDC36", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "GAME_FRAGMENT_Q", fragManage);
        getSupportFragmentManager().putFragment(outState, "GAME_FRAGMENT_A", fragManage);


    }
}
