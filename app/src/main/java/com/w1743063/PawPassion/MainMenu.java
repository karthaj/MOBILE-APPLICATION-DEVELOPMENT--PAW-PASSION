/**
 * {@authour w1743063 2018615}
 *
 * <p>This class provides the Main menu fragment to the
 * {@link com.w1743063.PawPassion.MainActivity}
 * </p>
 * <p>
 * This has a splash screen animation and the rout to the activity pages
 * </p>
 */
package com.w1743063.PawPassion;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import com.w1743063.PawPassion.Activities.IdentifyBreed.IdentifyBreed;
import com.w1743063.PawPassion.Activities.IdentifyDog.IdentifyDog;
import com.w1743063.PawPassion.Activities.SearchDogBreed;

import java.util.Objects;

public class MainMenu extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {

    // logging purpose
    private final String TAG = "HOME_SCREEN";
    //  Title name
    private TextView appName;
    // Button layout [ 3 buttons ]
    private LinearLayout layoutButtons;
    //
    private Button identifyDog, identifyBreed, searchBreed;
    // challenge switch
    private Switch lvlSwitch;
    // mai view
    private View view;
    // Logo and background
    private ImageView gbImg, logoImageWht;
    // exit button
    private ImageButton buttonExit;


    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {


        view = inflater.inflate(R.layout.activity_home_screen, container, false);
        /*
         * App name
         * */
        appName = view.findViewById(R.id.app_name);

        gbImg = view.findViewById(R.id.img_app_bg);
        /*
         * Image views
         * */


        logoImageWht = view.findViewById(R.id.img_Logo);

        layoutButtons = view.findViewById(R.id.buttonsLayout);

        /*
         * buttons
         * */
        identifyDog = view.findViewById(R.id.btn_identify_dog);

        identifyBreed = view.findViewById(R.id.btn_identify_breed);

        searchBreed = view.findViewById(R.id.btn_search_breed);

        buttonExit = view.findViewById(R.id.btn_exit);

        /*
         * Switch
         * */
        lvlSwitch =

                view.findViewById(R.id.lvl_switch);

        lvlSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView label = Objects.requireNonNull(getActivity()).findViewById(R.id.lvl_label);

                if (!isChecked) {
                    ObjectAnimator.ofObject(
                            label, // Object to animating
                            "textColor", // Property to animate
                            new ArgbEvaluator(), // Interpolation function
                            Color.rgb(255, 20, 5),// Start color
                            Color.BLACK // End color
                    ).setDuration(600).start(); // Duration in milliseconds
                } else {
                    ObjectAnimator.ofObject(
                            label, // Object to animating
                            "textColor", // Property to animate
                            new ArgbEvaluator(), // Interpolation function
                            Color.BLACK, // Start color
                            Color.rgb(255, 20, 0) // End color
                    ).setDuration(600).start(); // Duration in milliseconds
                }
            }
        });
        /*
         * Initializing on click listeners to the buttons
         */
        identifyBreed.setOnClickListener(this);
        identifyDog.setOnClickListener(this);
        searchBreed.setOnClickListener(this);
        buttonExit.setOnClickListener(this);

//        main animation
        splashScreenAnim();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void splashScreenAnim() {

        final Drawable vector = getResources().getDrawable(R.drawable.logo_black);
        final ImageView logoImageWht = view.findViewById(R.id.img_Logo);


        logoImageWht.animate().setStartDelay(2000).rotationY(90f).setListener
                (new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {
//                        animFontColor();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        logoImageWht.setImageDrawable(vector);
                        logoImageWht.setRotationY(90f);
                        logoImageWht.animate().rotationY(360f).setListener(null);

                        animFontColor(Color.WHITE, Color.rgb(235, 177, 49));

                        gbImg.animate().translationY(-3000).setDuration(1200);

                        /*Animations of the logo and title*/
                        byAnimations();
                        buttonsAnimate();

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }
                });

    }


    // TODO: 3/7/2020 Share intent radius adjust

    private void shareIntent(Class<?> toClass, Button button) {

        Intent intent = new Intent(getActivity(), toClass);
        Bundle data = new Bundle();//Use bundle to pass data
        data.putString("data", "This is Argument Fragment");//put string, int, etc in bundle with a key value

        // Pass data object in the bundle and populate details activity.
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(Objects.requireNonNull(getActivity()), button, "title");

        Bundle bd = new Bundle();
        bd.putAll(options.toBundle());
        bd.putBoolean("lvl", lvlSwitch.isChecked());
        bd.putString("str", "4");
        intent.putExtras(bd);

        startActivity(intent, bd);
    }

    private void buttonsAnimate() {
        // Exit Button Rotate | translationX | alpha
        buttonExit.animate().translationX(0).alpha(1)
                .rotation(-720).setDuration(900).setStartDelay(900).start();
        buttonExit.animate().alpha(1).start();
        // Buttons container entry alpha | translationY
        layoutButtons.animate().translationY(110).alpha(1)
                .setDuration(500).setStartDelay(300).start();

        /*
         * Floating affect to the 3 main button
         * */
        searchBreed.animate().translationY(-30).setDuration(350).setStartDelay(500).start();
        identifyBreed.animate().translationY(-30).setDuration(450).setStartDelay(600).start();
        identifyDog.animate().translationY(-30).setDuration(550).setStartDelay(700).start();


    }

    private void byAnimations() {

        /*
         * ints to hols the values corresponding to the orientation
         * */
        int logoY;
        int btY;
        float d;

        // check int for orientation
        int orientation = 0;

        try {
            orientation = getResources().getConfiguration().orientation;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If the device in landscape
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Values for the landscape mode
            d = 0.8f;
            logoY = -35;
            btY = -100;
        } else {

            // Values for the portrait mode
            d = 0.4f;
            logoY = -253;
            btY = -459;
        }


        /*
         * Below animation is done to get a simultaneous animations
         *
         * */
//        Logo animation scaling Shrink the logo
        ObjectAnimator scaleDownX =
                ObjectAnimator.ofFloat(
                        logoImageWht, "scaleX", d);

        //        Title animation scaling
        ObjectAnimator scaleDownY =
                ObjectAnimator.ofFloat(
                        logoImageWht, "scaleY", d);

        /*Setting durations for animation*/
        scaleDownX.setDuration(1300);
        scaleDownY.setDuration(900);

        /*
         * Animating the position translationY of logo
         * */
        ObjectAnimator logoMoveY =
                ObjectAnimator.ofFloat(
                        logoImageWht, "translationY", logoY);

        /*
         * Animating the position translationY of  title
         * */
        ObjectAnimator titleMoveY =
                ObjectAnimator.ofFloat(
                        appName, "translationY", btY);

        /*Setting durations for animation*/
        logoMoveY.setDuration(1500);
        titleMoveY.setDuration(900);

        /*
         * Setting animators
         * */
        AnimatorSet scaleDown = new AnimatorSet();
        AnimatorSet moveUp = new AnimatorSet();

        // simultaneous animations
        scaleDown.play(scaleDownX).with(scaleDownY);

        moveUp.play(logoMoveY);
        moveUp.play(titleMoveY);

        moveUp.start();
        scaleDown.start();
    }

    private void animFontColor(int color1, int color2) {
        ObjectAnimator.ofObject(
                appName, // Object to animating
                "textColor", // Property to animate
                new ArgbEvaluator(), // Interpolation function
                color1, // Start color
                color2 // End color
        ).setDuration(1500).start(); // Duration in milliseconds
    }

    /**
     * Confirmation  dialog to terminate
     *
     * @return confBuilder
     */
    private AlertDialog confirmation() {

        //  Dialog box builder
        AlertDialog.Builder confBuilder =
                new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        // message
        confBuilder.setMessage("Are you sure, you want close Paw Passion?");

        // Yes button
        confBuilder.setPositiveButton("YES ✔️", this);

        // no Button
        confBuilder.setNegativeButton("NO ❌", this);


        return confBuilder.create();
    }

    @Override
    public void onClick(View v) {
        // create the alert dialog
        AlertDialog userConfirmation = confirmation();
        switch (v.getId()) {

            case (R.id.btn_identify_breed):

                // Identify breed activity
                shareIntent(IdentifyBreed.class, (Button) v);
                break;

            case (R.id.btn_identify_dog):

                // Identify dog activity
                shareIntent(IdentifyDog.class, (Button) v);
                break;

            case (R.id.btn_search_breed):
                shareIntent(SearchDogBreed.class, (Button) v);

                break;
            case (R.id.btn_exit):
                // Wait for the confirmation and exit
                userConfirmation.show();

                break;
            default:
                break;

        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == -1) {
//            Yes button has been pressed
            // Show exit message
            Toast.makeText(
                    getActivity(), "Paw Passion ended :/ See you again!.. ",
                    Toast.LENGTH_LONG).show();

            Objects.requireNonNull(getActivity()).finish();
        } else {
//            No button has been pressed
            Toast.makeText(
                    getActivity(), "Good decision,\uD83D\uDC36", Toast.LENGTH_SHORT).show();
        }
    }


}
