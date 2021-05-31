package com.ian.payment.payoneer.ui;

import android.os.Bundle;

import com.ian.payment.payoneer.R;
import com.ian.payment.payoneer.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new PayMethodsFragment())
                .commit();


    }
}