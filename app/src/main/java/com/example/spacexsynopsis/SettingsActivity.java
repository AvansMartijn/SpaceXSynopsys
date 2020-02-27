package com.example.spacexsynopsis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.OnFragmentInteractionListener {
    private SettingsFragment settingsFragment;
    public static final String FILE_NAME = "pref_string.txt";
    private static int STORAGE_PERMISSION_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String prefString = loadPreferences();
        String exPrefString = loadExternalPreferences();
        settingsFragment = SettingsFragment.newInstance(prefString, exPrefString);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.settings_fragment, settingsFragment, "settings_fragment").commit();

        setTitle(prefString);





    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Uri selectedImage = imageReturnedIntent.getData();
        settingsFragment.setImage(selectedImage);

    }

    public void saveExternalPreferences(String string){
        //do external write
        if(isExternalStorageWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            File textFile = new File(getExternalFilesDir(null), "external_save.txt");
            try{
                FileOutputStream fos = new FileOutputStream(textFile, false);
                fos.write(string.getBytes());
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "Can not save external preference", Toast.LENGTH_SHORT).show();
        }

    }

    public String loadExternalPreferences(){

        if(isExternalStorageWritable() && checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
            StringBuilder sb = new StringBuilder();
            try{
                File textFile = new File(getExternalFilesDir(null), "external_save.txt");
                FileInputStream fis = new FileInputStream(textFile);

                if(fis != null){
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader buff = new BufferedReader(isr);

                    String line = null;
                    while((line = buff.readLine()) != null){
                        sb.append(line + "\n");
                    }
                    fis.close();
                }
                return sb.toString();

            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "External preference not readable", Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        if(check != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                   new String[] {permission},
                    STORAGE_PERMISSION_CODE);
        }
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    private boolean isExternalStorageWritable(){
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }




    public void savePreferences(String string) {

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(string.getBytes());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String loadPreferences() {
        FileInputStream fis = null;
        String prefString = "Set your own name!";
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            prefString = sb.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return prefString;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInputSend(CharSequence input, CharSequence input2) {
        savePreferences(input.toString());
        saveExternalPreferences(input2.toString());
        setTitle(input.toString());
    }

    @Override
    public void onImageButtonClicked() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
    }
}
