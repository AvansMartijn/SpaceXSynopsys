package com.example.spacexsynopsis;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class SettingsFragment extends Fragment {

    private static final String TITLE_NAME = "nameparam";
    private static final String EXTERNAL_SAVE_STRING = "exsaveparam";

    private String mTitleName;
    private String mExSaveString;
    private OnFragmentInteractionListener mListener;
    private EditText editTextName;
    private EditText editTextExSaveString;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public void setImage(Uri uri){
        if(getView() != null){
            ImageView imageView = getView().findViewById(R.id.image_view_setting);
            imageView.setImageURI(uri);
        }
    }

    public static SettingsFragment newInstance(String titleName, String exSaveString) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(TITLE_NAME, titleName);
        args.putString(EXTERNAL_SAVE_STRING, exSaveString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitleName = getArguments().getString(TITLE_NAME);
            mExSaveString = getArguments().getString(EXTERNAL_SAVE_STRING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        editTextName = view.findViewById(R.id.name_setting);
        editTextName.setText(mTitleName);
        editTextExSaveString = view.findViewById(R.id.edittext_external_storage);
        editTextExSaveString.setText(mExSaveString);
        Button saveButton = view.findViewById(R.id.button_save);
        Button pickImageButton = view.findViewById(R.id.button_image_pick);

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CharSequence input =  editTextName.getText();
                CharSequence input2 = editTextExSaveString.getText();
                mListener.onInputSend(input, input2);
            }
        });

        pickImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               mListener.onImageButtonClicked();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onInputSend(CharSequence input, CharSequence input2);

        void onImageButtonClicked();
    }
}
