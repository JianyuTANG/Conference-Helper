package com.example.myapplication.scholar.info;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.home.User;
import com.example.utils.CommonInterface;
import com.example.utils.Global;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScholarInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScholarInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAME = "param1";
    private static final String ARG_ID = "param2";

    // TODO: Rename and change types of parameters
    private String name;
    private int user_id;
    private String mParam2;

    public ScholarInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScholarInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScholarInfoFragment newInstance(String param1, int param2) {
        ScholarInfoFragment fragment = new ScholarInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, param1);
        args.putInt(ARG_ID, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            user_id = getArguments().getInt(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scholar_info, container, false);

        FloatingActionButton button = view.findViewById(R.id.scholar_start_chat_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO start a chat @chen yuce
                System.out.println("tjy " + user_id);
                boolean res = Global.addToContact(String.valueOf(user_id), name);
                if(res){
                    Toast.makeText(getContext(), "成功加入通讯录！", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "该用户已在通讯录中！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}