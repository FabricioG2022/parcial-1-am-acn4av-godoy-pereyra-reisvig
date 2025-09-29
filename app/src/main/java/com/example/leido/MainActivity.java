package com.example.leido;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private TextView tabLeidos;
    private TextView tabDeseados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Center title in ActionBar (keeps default ActionBar)
        if (getSupportActionBar() != null) {
            ActionBar ab = getSupportActionBar();
            ab.setDisplayShowTitleEnabled(false);

            TextView tv = new TextView(this);
            tv.setText(getString(R.string.app_name));
            tv.setTextSize(20);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(ContextCompat.getColor(this, android.R.color.white));

            ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
            );
            ab.setCustomView(tv, params);
            ab.setDisplayShowCustomEnabled(true);
        }

        // References to tabs
        tabLeidos = findViewById(R.id.tabLeidos);
        tabDeseados = findViewById(R.id.tabDeseados);

        // Click listeners
        tabLeidos.setOnClickListener(v -> {
            replaceFragment(new FragmentLeidos());
            setActiveTab(tabLeidos, tabDeseados);
        });

        tabDeseados.setOnClickListener(v -> {
            replaceFragment(new FragmentDeseados());
            setActiveTab(tabDeseados, tabLeidos);
        });

        // default: show Leidos
        if (savedInstanceState == null) {
            replaceFragment(new FragmentLeidos());
            setActiveTab(tabLeidos, tabDeseados);
        }
    }

    private void replaceFragment(Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, f)
                .commit();
    }

    private void setActiveTab(TextView active, TextView inactive) {
        if (active == null || inactive == null) return;

        active.setEnabled(false);
        active.setAlpha(1f);
        active.setTextColor(ContextCompat.getColor(this, R.color.tab_active_text));

        inactive.setEnabled(true);
        inactive.setAlpha(0.6f);
        inactive.setTextColor(ContextCompat.getColor(this, R.color.tab_inactive_text));
    }
}
