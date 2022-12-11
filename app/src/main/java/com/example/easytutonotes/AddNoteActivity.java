package com.example.easytutonotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import io.realm.Realm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddNoteActivity extends AppCompatActivity {
    ImageView imgGallery;
    Button btnGallery;
    Bitmap image;
    byte[] imageArray;


    private final int GALLERY_REQ_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        btnGallery = findViewById(R.id.setImage);
        imgGallery = findViewById(R.id.ImageView);
        EditText titleInput = findViewById(R.id.titleinput);
        EditText descriptionInput = findViewById(R.id.descriptioninput);
        MaterialButton saveBtn = findViewById(R.id.savebtn);
       // ImageButton deleteNote = findViewById(R.id.deletenote);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                long createdTime = System.currentTimeMillis();

                realm.beginTransaction();
                Note note = realm.createObject(Note.class);
                note.setTitle(title);
                note.setDescription(description);
                note.setCreatedTime(createdTime);
                if(image != null)
                {
                    imageArray = getBytes(image);
                    note.setImage(imageArray);
                    imageArray = null;
                }
                else
                {
                    //Bitmap map = BitmapFactory.decodeResource(getResources(), R.drawable.rounded_corner);
                    Bitmap map = ((BitmapDrawable)getResources().getDrawable(R.drawable.thinner)).getBitmap();
                    byte[] def = getBytes(map);
                    note.setImage(def);
                }
                realm.commitTransaction();
                Toast.makeText(getApplicationContext(),"Note saved",Toast.LENGTH_SHORT).show();
                finish();


            }
        });


    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode==GALLERY_REQ_CODE){
                imgGallery.setImageURI(data.getData());
                Uri imageUri = data.getData();
                try {
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    image = BitmapFactory.decodeStream(imageStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}