package com.mctrip.modules.device.ios;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mctrip.library.base.MFragment;


public class IosFragment extends MFragment {
    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_ios, null);
        Button buttonIos = (Button) contentView.findViewById(R.id.buttonIos);
        buttonIos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mFragmentActivity, "this is ios child module", Toast.LENGTH_SHORT).show();
            }
        });
        return contentView;
    }
}
