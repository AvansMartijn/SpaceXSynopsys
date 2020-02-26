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
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String IMAGE = "imgparam";
    private static final String NAME = "nameparam";



    // TODO: Rename and change types of parameters
    private String mName;
    private OnFragmentInteractionListener mListener;
    private EditText editTextName;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public void setImage(Uri uri){
        ImageView imageView = getView().findViewById(R.id.image_view_setting);
        imageView.setImageURI(uri);
    }

    public void setEditTextName(String string){
        editTextName.setText(string);
    }

    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String name) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        editTextName = view.findViewById(R.id.name_setting);
        editTextName.setText(mName);
        Button saveButton = view.findViewById(R.id.button_save);
        Button pickImageButton = view.findViewById(R.id.button_image_pick);

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                CharSequence input =  editTextName.getText();
                mListener.onInputSend(input);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onInputSend(CharSequence input);

        void onImageButtonClicked();
    }
}
