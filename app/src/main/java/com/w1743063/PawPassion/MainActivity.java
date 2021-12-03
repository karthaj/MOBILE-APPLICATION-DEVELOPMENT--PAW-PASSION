package com.w1743063.PawPassion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.w1743063.PawPassion.SystemModules.SystemUI;

public class MainActivity extends AppCompatActivity {

    private static FragmentManager fragmentManager;
    private MainMenu main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fragmentManager = getSupportFragmentManager();
        main = new MainMenu();
        fragmentManager.beginTransaction()
                .replace(
                        R.id.main_menu,
                        main
                ).commit();

        SystemUI ui = new SystemUI(getWindow().getDecorView());
        ui.run();
    }


}
