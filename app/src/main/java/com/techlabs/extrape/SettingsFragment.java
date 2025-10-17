package com.techlabs.extrape;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.techlabs.extrape.user.LoginActivity;
import com.techlabs.extrape.user.SharedPrefManager;

public class SettingsFragment extends Fragment {

    Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        btnLogout = view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });


        return view;
    }

    private void LogOut() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Logout")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (d, w) -> {
                    FirebaseAuth.getInstance().signOut();
                    SharedPrefManager.getInstance(getActivity()).saveUserId(null);
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    getActivity().finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


}