package axmldoc.bcfreitas.axmldocmobileclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChapterContents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_contents);

        Intent intent = getIntent();
        String contents = intent.getStringExtra("contents");

        LinearLayout chapterContentsLayout = (LinearLayout) findViewById(R.id.chapterContentsLayout);

        TextView contentsView = new TextView(getApplicationContext());

        contentsView.setText(contents);

        chapterContentsLayout.addView(contentsView);




    }

}
