/*
  This is a custom method to hide the UI
  controls of the system to get a fullscreen view
  View.SYSTEM_UI_FLAG_IMMERSIVE
  | View.SYSTEM_UI_FLAG_FULLSCREEN
  | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
  | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
  | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
  <p></p>
 */

package com.w1743063.PawPassion.SystemModules;

import android.os.Handler;
import android.view.View;

public class SystemUI implements Runnable {

    private View activity;
    private Handler h = new
            Handler();

    //    Getting the current working activity
    public SystemUI(View activity) {
        super();
        this.activity = activity;
    }


    /*
     * This hides the entire default ui system controls
     * */
    private void hideControls() {
        activity.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
    }


    @Override
    public void run() {

        /*
         * The thread is declared so the this will be running continuously
         * until the program ends
         * */
        h.postDelayed(this, 1000);
        hideControls();
    }

}
