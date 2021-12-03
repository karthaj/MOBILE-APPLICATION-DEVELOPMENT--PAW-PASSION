package com.w1743063.PawPassion.Activities.IdentifyDog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.w1743063.PawPassion.R;

import java.util.Objects;

import static android.widget.ImageView.ScaleType.FIT_CENTER;

public class FragDogResult extends Fragment implements View.OnClickListener {

    // debugging purpose
    private static final String TAG = "Fragment Result";

    // if challenge mode on
    private boolean skipQuestion;

    // count down module
    private CountDownTimer timerModule;

    //  Next button to keep playing the game
    private Button button_next;

    //  Default
    public FragDogResult() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*
        Check whether the player won the game

            public static <T> T requireNonNull(T obj) {
                  if (obj == null)
                     throw new NullPointerException();
                  return obj;
        */
//        fetching the answer state[ correct:true | wrong:false ]
        boolean winState = Objects.requireNonNull(
                getArguments())
                .getBoolean("CORRECT");

        //        Getting both correct and user selected answer
        int[] img_resources = getArguments().getIntArray("IMG_RESOURCE");

//      Auto next clicker to skip question
        skipQuestion = getArguments().getBoolean("AUTO_CLICK");


        //  Result of correct and incorrect answers
        int[] gameSummary = getArguments().getIntArray("SUMMARY");

        // setting the auto next clicker
        nextClicker();

//      layout that view referring to
        View view;



        /*
          The boolean value is passed from the IdentifyDog activity
          on after verifying the user answer*
         */
        if (winState) {

//            Fetching the layout for the correct answer
            view = inflater.inflate(
                    R.layout.frag_identify_dog_c,
                    container,
                    false
            );

//            when the guess was correct the fragCorrect() displays
            fragCorrect(gameSummary,
                    Objects.requireNonNull(img_resources)[0], view
            );

            button_next = view.findViewById(R.id.button_next_dog_c);
            button_next.setOnClickListener(this);
        } else {

            //            Fetching the layout for the wrong answer
            view =
                    inflater.inflate(
                            R.layout.frag_identify_dog_w,
                            container,
                            false
                    );

//            when the guess was wrong the wrong fragment displays
            assert img_resources != null;

            // setting the frag wrong to show the result
            fragWrong(gameSummary, img_resources, view);


            button_next = view.findViewById(R.id.button_next_dog_w);
            button_next.setOnClickListener(this);
        }


        // TODO: 3/6/2020
        if (skipQuestion)
            timerModule.start();

//        Returning the view after initializing view to inflate layout
        return view;
    }


    /*
     * Timer Module
     *
     * Initiating with the counter values with 9599 ms
     * */
    private void nextClicker() {

        timerModule = new CountDownTimer(4599, 1) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long t) {

                /* Ticker from ms to sec */
                int i = (int) (t / 1000) + 1;

//                setting the button text appending the count down
                button_next.setText("NEXT (" + i + ")");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                // if challenge mode is on
                // perform auto click
                if (skipQuestion)
                    button_next.performClick();
            }
        };
    }


    /**
     * This method is dealing the WRONG answer by the user.
     *
     * @param gameSummary  the result of the corrects' and wrongs'
     * @param img_resource the image of the Correct and Wrong Resource ID
     * @param view         The view that referring the context
     */
    private void fragWrong(int[] gameSummary, int[] img_resource, View view) {

//        Setting the summary of the game
        setSummary(gameSummary, view, R.id.text_result_dog_w);

        /*
         * Image Viewers
         */
        ImageView userAnswer = view.findViewById(R.id.img_user_answer_w);
        ImageView crtAnswer = view.findViewById(R.id.img_correct_answer_w);

        /*
          The indexing is done by the FragQuestion 'onClick(View v)'
                int array with [ image ID ] and [ Correct Image Id ]          */
        int id = img_resource[0] != -99 ?
                img_resource[0] : R.drawable.time_out;

        // setting the image for the wrong answer
        userAnswer.setImageResource(id);

        // setting the image for the correct answer
        crtAnswer.setImageResource(img_resource[1]);

    }


    /**
     * This method is dealing the WRONG answer by the user.
     *
     * @param gameSummary  the result of the corrects' and wrongs'.
     * @param img_resource the image of the Correct and Wrong Resource ID.
     * @param view         The view that referring the context.
     */
    private void fragCorrect(int[] gameSummary, int img_resource, View view) {

//        Setting the summary of the game
        setSummary(gameSummary, view, R.id.text_result_dog_c);

        /*
         * Image Viewers
         * */
        ImageView userAnswer = view.findViewById(R.id.img_correct_answer_c);
        userAnswer.setScaleType(FIT_CENTER);
        userAnswer.setImageResource(img_resource);
    }

    /**
     * Setting the summary for the TextView Summary
     *
     * @param gameSummary The summary of the game Nos of Corrects' and wrongs'
     * @param view        The view that referring the context.
     * @param textViewId  Fragment id of correct or wrong
     */
    private void setSummary(int[] gameSummary, View view, int textViewId) {

        // Fetching the TextView
        TextView summary = view.findViewById(textViewId);

//        Setting shadow
        summary.setShadowLayer(6.5f, -1, 3, Color.LTGRAY);

        //        Concatenating the result
        String result = gameSummary[0] + " / " + gameSummary[1];

        //        Setting the text value to the summary TextView
        summary.setText(result);

    }

    @Override
    public void onStop() {
        super.onStop();
        timerModule.cancel();
    }

    @Override
    public void onClick(View v) {

        timerModule.cancel();

        (
                (IdentifyDog) Objects.requireNonNull(
                        getActivity()
                )
        ).nextQuestion(); /* Calling the method of the IdentifyDog */
    }
}
