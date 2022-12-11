package com.example.easytutonotes;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import yuku.ambilwarna.AmbilWarnaDialog;

import org.w3c.dom.Text;

import java.text.DateFormat;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import yuku.ambilwarna.AmbilWarnaDialog;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    RealmResults<Note> notesList;
    MainActivity activity;

    int mDefaultColor;

    public MyAdapter(MainActivity activity, Context context, RealmResults<Note> notesList) {
        this.activity = activity;
        this.context = context;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.titleOutput.setText(note.getTitle());
        holder.descriptionOutput.setText(note.getDescription());
        String formatedTime = DateFormat.getDateTimeInstance().format(note.getCreatedTime());
        holder.timeOutput.setText(formatedTime);
        if(note.getImage() != null)
        {
            Bitmap image = getImage(note.getImage());
            Drawable d = new BitmapDrawable(context.getResources(), image);
            holder.Rlayout.setBackground(d);
        }



        holder.deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete the note
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                note.deleteFromRealm();
                realm.commitTransaction();
                Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        });
        holder.changecolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //openColorPicker(v, holder);
                    MainActivity act = activity;
                    int color;
                    act.openColorPicker(holder.Rlayout);
                    //Drawable drawable = holder.Rlayout.getBackground();
                    //drawable.setTint(color);
                }
        });
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titleOutput;
        TextView descriptionOutput;
        TextView timeOutput;
        ImageButton deleteNote;
        ImageButton changecolor;
        RelativeLayout Rlayout;
        String noteColor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOutput = itemView.findViewById(R.id.titleoutput);
            descriptionOutput = itemView.findViewById(R.id.descriptionoutput);
            timeOutput = itemView.findViewById(R.id.timeoutput);
            deleteNote = itemView.findViewById(R.id.deletenote);
            changecolor = itemView.findViewById(R.id.colorchangebutton);
            Rlayout = itemView.findViewById(R.id.Noterl);

           // noteColor = itemView.findViewById(R.id.Noterl).getBackground()
        }
    }

    public void openColorPicker( @NonNull MyAdapter.MyViewHolder holder) {
        if(holder != null)
        {
            AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(context, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                }
                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    mDefaultColor = color;
                    holder.Rlayout.setBackgroundColor(mDefaultColor);
                }
            });
            if(colorPicker != null)
                colorPicker.show();
        }
    }

}
