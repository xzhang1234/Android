package io.github.xzhang1234.flickrbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra("PHOTO");
        if(photo != null) {
            TextView photoTitle = (TextView) findViewById(R.id.photo_title);
            photoTitle.setText("Title: " + photo.getTitle());

            TextView photoTags = (TextView) findViewById(R.id.photo_tags);
            photoTags.setText("Tags: " + photo.getTags());

            TextView photoAuthor = (TextView) findViewById(R.id.photo_author);
            photoAuthor.setText(photo.getAuthor());

            ImageView photoImage = (ImageView) findViewById(R.id.photo_image);
            Picasso.with(this).load(photo.getLink())
                    .error(R.drawable.ic_add_a_photo_black_48dp)
                    .placeholder(R.drawable.ic_add_a_photo_black_48dp)
                    .into(photoImage);
        }

    }


}
