package com.biybiruza.quoteapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.biybiruza.quoteapp.R;
import com.biybiruza.quoteapp.databinding.FragmentSplashBinding;

public class SplashFragment extends Fragment {

    public SplashFragment() {
        super(R.layout.fragment_splash);
    }

    private FragmentSplashBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSplashBinding.bind(view);

        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_quoteFragment);
            }
        },2000);
    }
}