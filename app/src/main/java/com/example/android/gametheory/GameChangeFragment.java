package com.example.android.gametheory;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameChangeFragment extends Fragment {
    private static final String TAG = ".GameChangeFragment";

    DatabaseReference nowRef;

    ListView titleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_change, container, false);

        nowRef = FirebaseDatabase.getInstance().getReference("game").child("now");

        String [] titles = getResources().getStringArray(R.array.title);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, titles);

        titleView = v.findViewById(R.id.lv_title);
        titleView.setAdapter(adapter);
        titleView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nowRef.setValue(position).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            CustomUtils.displayToast(getContext(), "게임이 변경되었습니다.");
                            ManagerActivity activity = (ManagerActivity) getActivity();
                            activity.fragmentReplace(1);
                        }
                    }
                });
            }
        });

        return v;
    }



}
