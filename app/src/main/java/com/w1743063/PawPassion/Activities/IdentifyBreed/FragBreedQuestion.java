/**
 * {@authour w1743063 2018615}
 *
 * <p>This class provides the question fragment to the
 * {@link com.w1743063.PawPassion.Activities.IdentifyBreed.IdentifyBreed}
 * </p>
 * <p>
 * This method uses a {@link android.os.CountDownTimer }
 * module to do the sequencing of the quiz.
 * </p>
 * <p>
 * After the user answer this method calls for the method from identifyBreed class
 * to validate the answer
 * </p>
 */


package com.w1743063.PawPassion.Activities.IdentifyBreed;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.w1743063.PawPassion.DogModules.BreedList;
import com.w1743063.PawPassion.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragBreedQuestion extends Fragment implements View.OnClickListener {

    //    Debugger purpose
    private static final String TAG = "Fragment Question";

    /*
     * To pick the breed from the dropdown
     * */
    private Spinner spinner;

    /*
     * this stores the correct breed id,
     * To Check user answer with the correct answer,
     * */
    private int breedResourceID;

    /*
     * Users answer as id of the breed
     * */
    private int userAnswerId;

    /*
     * Timer to activate challenge mode
     * */
    private CountDownTimer timerModule;

    /*
     * If challenge mode skip question
     * */
    private boolean skipQuestion;

    /*
     * Submit button
     * */
    private Button button_submit;

    //    Default
    public FragBreedQuestion() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {

        /*
         * Getting the image to display image view to guess the breed.
         */
        assert getArguments() != null;
        int imageResourceID = getArguments().getInt("IMAGE_RES_ID");

        /*
         * Getting the BreedID to verify the breed.
         */
        breedResourceID = getArguments().getInt("BREED_RES_ID");

        //      Auto next clicker to skip question
        skipQuestion = getArguments().getBoolean("AUTO_CLICK");


//        Log.i(TAG, "onCreateView: " + imageResourceID);

        /*
         * layout that view referring to
         */
        View view = inflater.inflate(R.layout.frag_identify_breed_q, container, false);

        /*
         * Setting the image to he ImageView
         */
        setImage(imageResourceID, view);

        /*
         * Populating the drop down
         */
        spinner = setSpinner(view);

        /*
        submit button
        */
        button_submit = view.findViewById(R.id.button_submit);

        /*
         * Setting the listener
         *
         * the  on click even will make a call to the method in 'IdentifyBreed.checkAnswer()'
         * */
        button_submit.setOnClickListener(this);

        /*
         * Checks whether the challenge mode is on
         * if so it activates the timer mode top switch question every 10 sec
         * */
        if (skipQuestion) {

            /*
             * Initializing the timer Countdown module
             * */
            setupTimerQuestion();

            /*
             * Starting the timer in main activity to display to the user
             * the current ticks
             *
             * Since the timer element is im parent activity
             * calling a method to activate timer
             * */
            ((IdentifyBreed) Objects.requireNonNull(getActivity())).startTimer();

            /*
             * Starting the questions sequencer
             * */
            timerModule.start();
        }

//        Returning the inflater with view initialized
        return view;

    }


    private boolean checkAnswer() {
        /*
         * Replacing the [' '] with ['_']
         *
         * input     : GRATE DANE
         * OUTPUT    : GRETE_DANE
         * */
        String userAnswer = spinner.getSelectedItem().toString()
                .replace(" ", "_");

        /*
        Fetching the breed array resource from the Enum BreedList
        getResourceId()
        *  */
        userAnswerId = BreedList.getResourceId(userAnswer);

        /* Correct answer
         * This variable holds the boolean value which was derived from
         * validating user answer under the condition
         *
         * { resourceId == breedID }
         * resourceId : id of the breed USER selected
         * breedID : id of the breed corresponding to the image
         *
         * */

        return userAnswerId == breedResourceID;
    }


    /**
     * This method sets populates the spinner
     * for the user to pick a breed and submit.
     *
     * @param view the current view interacting with.
     */
    private Spinner setSpinner(View view) {
        /*
         * Spinner to let the user to select the guss
         * for the image viewed image in "imageResourceID"
         */
        Spinner breedListSp = view.findViewById(R.id.spinner_answer);

        //set the spinners adapter to the previously created one.
        breedListSp.setAdapter(
                new ArrayAdapter<>(
                        Objects.requireNonNull(getActivity()),
                        android.R.layout.simple_spinner_dropdown_item,
                        fetchBreedList())
        );


        /*
         * returning the breeds spinner
         * */
        return breedListSp;
    }


    /**
     * This method sets the image to the Image viewer.
     *
     * @param view            the current view interacting with.
     * @param imageResourceID the id of the image.
     */
    private void setImage(int imageResourceID, View view) {
        /*
         * ImageViewer where the question is set
         */
        ImageView quesImageView = view.findViewById(R.id.img_question);

        /*
         * Setting the question image to the 'quesImageView'.
         */
        quesImageView.setImageResource(imageResourceID);
    }

    /**
     * <p>
     * This method returns the list of gog images available in the system
     * this iterates through the {@code DogBreed } Enum and fetch the values
     * then fetched values are stripped for a user friendly look by
     * removing the '_' from the multi word breed names<p/>
     *
     * @return List<String> This return the list of breed names.
     */
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


        /*
         * The color changer is called from the parent
         *  activity to do a color change in the timer
         * */
        ((IdentifyBreed) Objects.requireNonNull(getActivity())).timerColorAnim();

        /*
         * returning thew breeds list to populate Spinner list of items
         * */
        return items;
    }


    /**
     * <p>
     * Setting up the timer module to do the count down from [10 - 1]
     * and then switch the question
     * </p>
     *
     */
    private void setupTimerQuestion() {

        /*
         * Timer Module
         *
         * Initiating with the counter values with 9599 ms
         * */
        timerModule = new CountDownTimer(9599, 1) {
            @Override
            public void onTick(long t) {
//                letting the time run out
//                Doing nothing :) it just ticks like a good boy :)
            }


            /**
             * On Finish method trigger when the timer runs out
             */
            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {

                /*
                 * Checking weather the Challenge mode is on
                 * */
                if (skipQuestion)
                    /*
                     * Calling the performClick() method of the submit button
                     * */
                    button_submit.performClick();

                /*
                 * Terminating the timer module.
                 * */
                timerModule.cancel();
            }
        };
    }


    /**
     * This class implements the View.OnClickListener interface
     * therefore this is an over ridden method of the interface
     * to listen to the button click
     *
     * @param v View that was clicked
     * */
    @Override
    public void onClick(View v) {

        /*
         * Checking whether the required view was pressed
         * */
        if (v.getId() == R.id.button_submit)

            /*
             * Validating the current activity that its not null
             * to avoid error throws and crash
             * */
            if (getActivity() != null)

                /* todo Refer whats behind scene
                 * Calling the method from IdentifyBreed class to check the answer
                 * */
                ((IdentifyBreed)
                        getActivity())
                        .showResult( /* method */
                                checkAnswer(), /* Validation of the user input  */
                                userAnswerId, /* User answer Id */
                                breedResourceID /* Correct breed id */
                        );
    }
}
