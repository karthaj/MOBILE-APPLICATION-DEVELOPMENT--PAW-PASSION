package com.w1743063.PawPassion.DogModules;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.function.BiConsumer;

@SuppressLint("Registered")
public class DogBreed extends AppCompatActivity {

    //    GET
    private Stack<String> randomImage;
    private LinkedHashMap<String, TypedArray> breedImages;
    private List<String> items;
    private Context context;

    /**
     * The constructor take in the context as param to fetch the
     * resource from the requesting class.
     * finally it updates the class
     *
     * @param context to get the current content to fetch resource
     */
    public DogBreed(Context context) {
//        Current context the object being created
        this.context = context;

        // TODO: 3/6/2020

        this.randomImage = new Stack<>();
        this.breedImages = new LinkedHashMap<>();
        this.items = new ArrayList<>();

//        updating the fields
        updateDogBreed();
    }

    /**
     * @return the randomImage array
     */
    public Stack<String> getRandomImage() {
        return randomImage;
    }

    /**
     * @return the item List as Strings
     */
    public List<String> getItems() {
        return items;
    }

    /**
     * Get breed images array
     *
     * @return breedImages
     */
    public LinkedHashMap<String, TypedArray> getBreedImages() {
        return breedImages;
    }

    /**
     * <p>
     * This method returns the list of gog images available in the system
     * this iterates through the {@code DogBreed } Enum and fetch the values
     * then fetched values are stripped for a user friendly look by
     * removing the '_' from the multi word breed names<p/>
     */
    public void fetchBreedList() {
//      Iterating through the breed  list and feeding into the list 'items'
        for (BreedList value : BreedList.values()) {

//          Inserting into the item list
            List<String> items = new ArrayList<>();
            items.add(
                    value.toString()
                            /* Stripping off "_" and replacing with '[empty space]'*/
                            .replace("_", " ")
            );
            this.items = items;
        }
    }


    @Override
    public int hashCode() {
        return Objects.hash(getRandomImage(), getBreedImages(), getItems());
    }

    /**
     * Updating the dog breed
     */
    public void updateDogBreed() {

//       Re-Fetch breed list
        fetchBreedList();

//        Recalling the initializeBreedArray to
        initializeBreedArray();

//        Refilling random image IDs
        setRandomImage();
    }

    @SuppressLint("UseSparseArrays")
    private void initializeBreedArray() {
        //        Filling the breed Image Array with Image resource ID
        for (BreedList value : BreedList.values()) {

//            Filling the Array with the breed index and image resources available.
            this.breedImages.put(
                    /*
                    fetching the breed id for prevent duplication
                    when selecting the 3 random images
                     * */
                    this.breedImages.size() + "@" + value.getId(),

                    /*
                     *  Inserting a new resource reference
                     */
                    context.getResources().obtainTypedArray(
                            /*
                             * Getting breed ID from enum
                             * */
                            value.getId()
                    )
            );
        }

    }

    // TODO: 3/6/2020  
    private void setRandomImage() {
        /*
         * Looping through the array and fetching the key and the value and inserting it into the
         * randomImage stack by calling the function {  pushImageRef(TypedArray v) }
         *
         * */
        this.breedImages.forEach(
                new BiConsumer<String, TypedArray>() {
                    @Override
                    public void accept(String breedID, TypedArray resourceRef) {
                        /*
                         * Feeding random number stack array with the corresponding breed index
                         * into the HashMap randomImageSelector
                         */
                        for (int i = 0; i < resourceRef.length(); i++) {

//                            Temporary variable to hold the reference string
                            String imageRef;

                            /*
                             * Fetching the image resource ''ID''
                             * Appending the breed id for easy identification of the image
                             * when dealing with the quiz.
                             * Splitting the image_Red and the Breed_Ref
                             *
                             ** ex: [ 200000@1@5 ]
                             **          '200000'   image ref
                             **               '@'
                             **               '1'   Key countID
                             **               '@'
                             **               '5'   Breed Resource ID (  Prevent duplication
                             **                                          when selecting the
                             **                                          3 random image ).
                             ** */
                            imageRef =
                                    resourceRef.getResourceId(i, -1) /* Image Reference Number */
                                            +
                                            "@" /* Symbol to .split() method   */
                                            +
                                            breedID; /* Key value referencing the breed*/

                            /*
                             * Pushing the values into the String Stack
                             */
                            randomImage.push(
                                    imageRef
                            );
                            /*
                             * When retrieving the string it has
                             * to be .split() to get both references
                             * */

                        }
                    }
                }
        );
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DogBreed)) return false;
        DogBreed dogBreed = (DogBreed) o;
        return getRandomImage().equals(dogBreed.getRandomImage()) &&
                getBreedImages().equals(dogBreed.getBreedImages()) &&
                getItems().equals(dogBreed.getItems());
    }

    @NonNull
    @Override
    public String toString() {
        return "DogBreed{" +
                "randomImage=" + randomImage +
                ", breedImages=" + breedImages +
                ", items=" + items +
                '}';
    }
}
