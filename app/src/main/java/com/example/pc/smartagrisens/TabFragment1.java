package com.example.pc.smartagrisens;

/**
 * Created by pc on 02/03/2016.
 */
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TabFragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       final Analyse an =  (Analyse) getActivity();
        LayoutInflater lf = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.tab_fragment_1, container, false);
        String om = an.getOM();
       TextView text = (TextView) v.findViewById(R.id.VOM);
        text.setText(om);

        Button OM = (Button) v.findViewById(R.id.button3);
        OM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                an.viewAll();
            }
        });

        return v;
    }


    }
