package axmldoc.bcfreitas.axmldocmobileclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import axmlDoc.AxmlDoc;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //File fXmlFile = new File("livro.xml");
        //Long originalSize = fXmlFile.length();

        TextView readABookOption = (TextView) findViewById(R.id.opcao1);

        readABookOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readABook();
            }
        });
    }

    protected void readABook(){
        Intent chapterSelect = new Intent(this, ChooseStrategyActivity.class);
        startActivity(chapterSelect);
    }
}
