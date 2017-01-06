package com.multiapk.modules.computer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.multiapk.library.base.MFragment;


public class ComputerFragment extends MFragment {

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_computer, null);
        TextView textHospital = (TextView) contentView.findViewById(R.id.textComputer);
        textHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mFragmentActivity, "module-computer", Toast.LENGTH_SHORT).show();
            }
        });
        return contentView;
    }
}
