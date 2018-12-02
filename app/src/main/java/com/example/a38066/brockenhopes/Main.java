package com.example.a38066.brockenhopes;

import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main extends AppCompatActivity {

    @BindView(R.id.switcher) Button switcher;
    @BindView(R.id.loadDir) Button loadDir;
    @BindView(R.id.photos) LinearLayout photos;
    boolean getStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void show(){
        photos.removeAllViews();
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/photo";
        File file = new File(path);
        for (File filePath : file.listFiles()){
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            Picasso.get().load(filePath).into(imageView);
            photos.addView(imageView);
        }
    }

    @OnClick(R.id.loadDir)
    public void loadDir(){
        try (FTPClientServer FTPServer = new FTPClientServer()){
            FTPServer.downloadDir();
            show();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @OnClick(R.id.switcher)
    public void switcher(){
        getStatus = !getStatus;
        if (getStatus) switcher.setText("Able");
        else switcher.setText("Disable");
        try (FTPClientServer FTPServer = new FTPClientServer()){
            FTPServer.sendFile(getStatus);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
