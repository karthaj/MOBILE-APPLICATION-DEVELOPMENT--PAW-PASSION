/**
 * This Enum hold all the available breed name in the application.
 *
 * <p>
 * when upgrading the system we can simply add enums and the array in source
 * the rest of the setting are automated so that updating this 2 place can
 * add NEW images and NEW breeds without any issues
 * </p>
 */
package com.w1743063.PawPassion.DogModules;

import com.w1743063.PawPassion.R;

/**
 * List of enums that's id fetched from the {@code R.array.beagle}
 */
public enum BreedList {
    /**
     * *More can be added
     * *the steps to follow is
     * *      1 : Add the source image to the Drawables
     * *      2 : Create Array List xml file in [Values] directory
     * *            <code>{
     * *              <integer-array name="beagle">
     * *                  <item>@drawable/ [ Image name ] </item>
     * *              </integer-array>
     * *                }
     * *         </code>
     * *      3 : Add the breed below and pass the {{ R.array.[breed array name] }}
     * *
     * *The rest system will do for you :)
     ***/

    BEAGLE(R.array.beagle),
    BLOODHOUND(R.array.bloodhound),
    BOSTON_BULL(R.array.boston_bull),
    BOXER(R.array.boxer),
    DOBERMAN(R.array.doberman),
    FRENCH_BULLDOG(R.array.french_bulldog),
    GERMAN_SHEPHERD(R.array.german_shepherd),
    GOLDEN_RETRIEVER(R.array.golden_retriever),
    GREAT_DANE(R.array.great_dane),
    MINIATURE_SCHNAUZER(R.array.miniature_schnauzer),
    POMERANIAN(R.array.pomeranian),
    PUG(R.array.pug),
    ROTTWEILER(R.array.rottweiler),
    SIBERIAN_HUSKY(R.array.siberian_husky),
    YORKSHIRE_TERRIER(R.array.yorkshire_terrier);

    int id;

    //  Constructor
    BreedList(int p) {
        id = p;
    }

    /**
     * <p>
     * his method deep searches the enum values
     * and return them matching value of the enum
     * </p>
     *
     * @param breedResId get the breed id to fetch the enum name
     * @return Enum value
     */
    public static String getBreed(int breedResId) {

//        Log.i("Breed", "getBreed: " + breedResId);

        for (BreedList e : BreedList.values()) {

            if (e.getId() == breedResId) // checks whether its equal

                return String.valueOf(e);

        }
        return "Not Found !"; // if not found
    }

    /**
     * <p>
     * This returns the id of the breed when the user pass the Name of the breed
     * </p>
     *
     * @param breed the name of the breed
     * @return Array id of the  breed source
     */
    public static int getResourceId(String breed) {

//        Log.i("Breed", "getBreed: " + breed);
        for (BreedList e : BreedList.values()) {

            if (e.name().equals(breed))

                return e.getId();
        }

        return -1; // if not found return -1
    }

    /*
     * Returns the id  if the breed array resource
     * */
    public int getId() {
        return id;
    }

}



