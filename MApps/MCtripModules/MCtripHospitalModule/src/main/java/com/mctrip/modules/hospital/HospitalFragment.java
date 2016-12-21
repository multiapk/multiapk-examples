package com.mctrip.modules.hospital;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mctrip.library.base.MFragment;

public class HospitalFragment extends MFragment {

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_hospital, null);
        TextView textHospital = (TextView) contentView.findViewById(R.id.textHospital);
        textHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mFragmentActivity, "医院模块", Toast.LENGTH_SHORT).show();
            }
        });
        return contentView;
    }
}
