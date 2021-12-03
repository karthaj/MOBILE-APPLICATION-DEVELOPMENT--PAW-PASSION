
/**
 * {@authour w1743063 2018615}
 *
 * <p>This class provides the question fragment to the
 * {@link com.w1743063.PawPassion.Activities.IdentifyDog.IdentifyDog}
 * </p>
 * <p>
 * This method uses a {@link android.os.CountDownTimer }
 * module to do the sequencing of the quiz.
 * </p>
 * <p>
 * After the user answer this method calls for the method from identifyDog class
 * to validate the answer
 * </p>
 */

package com.w1743063.PawPassion.Activities.IdentifyDog;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.w1743063.PawPassion.DogModules.BreedList;
import com.w1743063.PawPassion.R;

import java.util.ArrayList;
import java.util.Objects;

import static android.widget.ImageView.ScaleType.FIT_CENTER;


public class FragDogQuestion extends Fragment implements View.OnClickListener {

    private static final String TAG = "Fragment Question";

    //    Correct image ID
    private int CORRECT_IMAGE;

    /*
     * image resources array to set image resources to the image view
     * */
    private ArrayList<Integer> image_id;

    //  Default
    public FragDogQuestion() {
    }

    /**
     * TODO
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


//         Get image resources array to set image resources to the image view
        if (getArguments() == null) throw new AssertionError();
        image_id = getArguments().getIntegerArrayList("IMAGE_ID");


//         Selected Image Position Index
        int hiddenImagePos = getArguments().getInt("HIDDEN_IMAGE_POS");


//         Selected Breed ID
        int selectedBreedId = getArguments().getInt("SELECTED_BREED");


//      Selected image reference to show when the result is being displayed
        assert image_id != null;
        CORRECT_IMAGE = (image_id.get(hiddenImagePos));


//      layout that view referring to
        View view = inflater.inflate(R.layout.frag_identify_dog_q, container, false);


//         Setting the question images.
        setQuestion(
                image_id, // List of images to be displayed
                view,
                selectedBreedId // selected correct breed, received from arguments
        );


//        Returning the view after initializing view to inflate layout
        return view;
    }


    private void setQuestion(ArrayList<Integer> image_id, View view, int breedId) {

        /*
         TextView that displays the dog breed
          name to identify the dogs among the 3 pictures
          */
        TextView question = view.findViewById(R.id.text_q_breed);
        question.setShadowLayer(8.5f, -1, 3, Color.LTGRAY);

//        Setting the breed name
        question.setText(
                BreedList.getBreed(breedId)
                        /*
                          Since the name is fetched from the ENUM values
                          in default some values name has in '_' ex: [ "  SIBERIAN_HUSKY " ]
                          There fore to make user friendly the '_' is replaced with [space " "]

                          { SIBERIAN_HUSKY } as { SIBERIAN HUSKY }
                          */
                        .replace("_", " ")
        );

//        Array to store the option images
        ImageView[] optionImages = new ImageView[3];


//        Getting Images from the view
        optionImages[0] = view.findViewById(R.id.img_1); // index 0
        optionImages[1] = view.findViewById(R.id.img_2); // index 1
        optionImages[2] = view.findViewById(R.id.img_3); // index 2

        optionImages[0].setScaleType(FIT_CENTER);

//        Iterating the array and setting the image resource
        for (int i = 0; i < 3; i++) {
            optionImages[i].setScaleType(FIT_CENTER);

            assert image_id != null;
//            Setting image
            optionImages[i].setImageResource(image_id.get(i));
            Log.i(TAG, "setQuestion: " + image_id.get(i));

            /*
             Since this class implements {  View.OnClickListener }
             three images are being listen to its click event
             therefore initializing the { setOnClickListener() }
             by passing the current class.
             */
            optionImages[i].setOnClickListener(this);

        }

    }

    /*
     * Initializing the function on click universal
     * */
    @Override
    public void onClick(View v) {


//       int array with [ image ID ] and [ Correct Image Id ]
        int[] imgRes = new int[2];

        /*
           Getting the currently clicked image description.
           It's description is hardcoded with the index
           Then fetching the image corresponding to that id
        */
        int userClickedImage = Integer.parseInt(String.valueOf(v.getContentDescription()));

        imgRes[0] = image_id.get(userClickedImage);

//        Getting the correct Image ID
        imgRes[1] = CORRECT_IMAGE;

     /*
          Reference
          https://stackoverflow.com/questions/9343241/passing-data-between-a-fragment-and-its-container-activity
          <p>In your fragment you can call getActivity().

          This will give you access to the activity that created the fragment.
          From there you can obviously call any sort of accessor methods that are in the activity.

          e.g. for a method called getResult() on your Activity:</p>
          */

        ((IdentifyDog) Objects.requireNonNull(getActivity()))
                .checkAnswer(
                        imgRes, userClickedImage);

    }
}
