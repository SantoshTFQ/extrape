package com.techlabs.extrape;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LinksFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_links, container, false);

        Button openReelButton = view.findViewById(R.id.open_reel_button);

        // Set a click listener on the button
        openReelButton.setOnClickListener(v -> {
            // Create an Intent to open the new Activity
            Intent intent = new Intent(getActivity(), ReelSetupActivity.class);
            // Start the new Activity
            startActivity(intent);
        });

        //startActivity(new Intent(getActivity(),ReelSetupActivity.class));
        // Create an Intent to open the new Activity
        return view;

    }
}