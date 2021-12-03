/**
 * {@authour w1743063 2018615}
 *
 * <p>This class provides the Result fragment to the
 * {@link com.w1743063.PawPassion.Activities.IdentifyBreed.IdentifyBreed}
 * </p>
 * <p>
 * This generates the result to the screen
 * </p>
 */

package com.w1743063.PawPassion.Activities.IdentifyBreed;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.w1743063.PawPassion.DogModules.BreedList;
import com.w1743063.PawPassion.R;

public class FragBreedResult extends Fragment implements View.OnClickListener {

    /*
     * Timer to activate challenge mode
     * */
    private CountDownTimer timerModule;

    /*
     * To display next question
     * */
    private Button button_next;

    /*
     * If challenge mode skip question
     * */
    private boolean skipQuestion;


    //    Default
    public FragBreedResult() {
    }


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {


//      Is the answer correct
        assert getArguments() != null;
        boolean correct = getArguments().getBoolean("CORRECT");

//      Auto next clicker to skip question
        skipQuestion = getArguments().getBoolean("AUTO_CLICK");

//      Getting both correct and user selected answer
        int answerIDUser = getArguments().getInt("answerIDUser");
        int answerIDCrt = getArguments().getInt("answerIDCrt");


//      Result of correct and incorrect answers
        int[] gameSummary = getArguments().getIntArray("SUMMARY");


//      layout that view referring to
        View view;

        /*
         * If the answer was correct this fi condition executes
         *
         * correct refers to the users answer is correct or wrong
         * */
        if (correct) {
            view =
                    inflater.inflate(
                            R.layout.frag_identify_breed_c, container, false);

//            Fetching the fragment correct
            fragCorrect(view, gameSummary, answerIDUser);

//            Setting the button listener by passing the button
//            corresponding to the fragment
//            else this will throw a Null pointer exception
            setButtonListener(view, R.id.button_next_breed_c);
        } else {
            view =
                    inflater.inflate(
                            R.layout.frag_identify_breed_w,
                            container,
                            false);

//            Fetching the fragment wrong
            fragWrong(view, gameSummary, answerIDUser, answerIDCrt);

            //            Setting the button listener by passing the button
//            corresponding to the fragment
//            else this will throw a Null pointer exception
            setButtonListener(view, R.id.button_next_breed_w);
        }

        /*
         * Checks whether the challenge mode is on
         * if so it activates the timer mode top switch perform
         * click on the button at 3 sec count down
         * */
        if (skipQuestion) {

            /*
             * Initializing the timer Countdown module
             * */
            nextClicker();

            /*
             * Starting the module
             * */
            timerModule.start();
        }

        /*
         * Returning the view
         * */
        return view;
    }

    private void setButtonListener(View view, int button) {

        button_next = view.findViewById(button);

        button_next.setOnClickListener(this);

    }


    /**
     * This method is dealing the WRONG answer by the user.
     *
     * @param gameSummary  the result of the corrects' and wrongs'
     * @param answerIDUser the image of the user Breed ID
     * @param view         The view that referring the context
     */
    private void fragCorrect(View view, int[] gameSummary, int answerIDUser) {

//        Setting the summary of the game
        setSummary(gameSummary, view, R.id.text_result_breed_c);
    }


    /**
     * This method is dealing the WRONG answer by the user.
     *
     * @param gameSummary  the result of the corrects' and wrongs'
     * @param answerIDUser the user selected Breed ID
     * @param answerIDCrt  the correct Breed ID
     * @param view         The view that referring the context
     */
    private void fragWrong(View view, int[] gameSummary, int answerIDUser, int answerIDCrt) {

//        Setting the summary of the game
        setSummary(gameSummary, view, R.id.text_result_breed_w);

        /*
         Text view of the results answers
        */
        TextView userAnswer = view.findViewById(R.id.text_ans_user_breed_w);
        TextView crtAnswer = view.findViewById(R.id.text_crt_user_breed_w);


        /*
         * Setting the correct answer.
         * */
        crtAnswer.setText(
                BreedList.getBreed(answerIDCrt).replace("_", " ")
        );

        /*
         * Setting the user answer.
         * */
        userAnswer.setText(
                BreedList.getBreed(answerIDUser).replace("_", " ")
        );


    }


    /**
     * Setting the summary for the TextView Summary
     *
     * @param gameSummary The summary of the game Nos of Corrects' and wrongs'
     * @param view        The view that referring the context.
     * @param textViewId  Fragment id of correct or wrong
     */
    private void setSummary(int[] gameSummary, View view, int textViewId) {

        //        Fetching the TextView
        TextView summary = view.findViewById(textViewId);

        /*
        Setting the shadow
        */
        summary.setShadowLayer(7.5f, -1, 3, Color.LTGRAY);

        /*
        Concatenation the result of the game
        * */
        String result = gameSummary[0] + " / " + gameSummary[1];


        //        Setting the text value to the summary TextView
        summary.setText(result);
    }

    /**
     * <p>
     * Setting up the timer module to do the count down from [4 - 1]
     * and then perform click
     * </p>
     */
    private void nextClicker() {

        /*
         * Timer Module
         *
         * Initiating with the counter values with 9599 ms
         * */
        timerModule = new CountDownTimer(4599, 1) {

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long t) {

                /* Ticker from ms to sec */
                int i = (int) (t / 1000) + 1;

                /*  setting the text form the button next
                 * with the counter decrementing
                 * */
                button_next.setText("NEXT (" + i + ")");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                /*
                 * Checking weather the Challenge mode is on
                 * */
                if (skipQuestion) {

                    /*
                     * Calling the performClick() method of the next button
                     * */
                    button_next.performClick();
                }
            }
        };
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
        /*
         * Checking whether the required view was pressed
         * */
        if (getActivity() != null)
            ((IdentifyBreed) (getActivity())).nextQuestion();
    }
}
