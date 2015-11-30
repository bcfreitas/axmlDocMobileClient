package axmldoc.bcfreitas.axmldocmobileclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;

import axmlDoc.AxmlDoc;
import axmlDoc.AxmlDoc.ReturnType;
import axmlDoc.AxmlDoc.HandleStrategy;

public class ChapterSelectActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    AxmlDoc axmlDoc;
    Long originalSize;
    String contents;
    Intent chapterContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_select);

        Intent intent = getIntent();
        String strategy = intent.getStringExtra("strategy");

        Log.i("MENSAGEM", strategy);
        Toast.makeText(getApplicationContext(), "Choosen strategy: " + strategy, Toast.LENGTH_SHORT).show();

        Log.i("MENSAGEM:", this.getApplicationContext().getFilesDir().toString());

        File fXmlFile = new File(this.getApplicationContext().getFilesDir(), "livro.xml");
        originalSize = fXmlFile.length();

        //if size 0 the sample file does not exist, extract from apk.
        if (originalSize == 0) {
            try {
                InputStream asset = getAssets().open("livro.xml");
                byte[] buffer = new byte[asset.available()];
                asset.read(buffer);

                OutputStream outStream = new FileOutputStream(fXmlFile);
                outStream.write(buffer);
            } catch (IOException e) {

            }

            originalSize = fXmlFile.length();
        }


        HandleStrategy strategyObject = null;

        switch (strategy) {
            case "LAZY":
                strategyObject = HandleStrategy.LAZY;
                break;
            case "LAZY_PERSIST":
                strategyObject = HandleStrategy.LAZY_PERSIST;
                break;
            case "LAZY_PERSIST_WITH_EXCLUSION":
                strategyObject = HandleStrategy.LAZY_PERSIST_WITH_EXCLUSION;
                break;
            case "EAGER":
                strategyObject = HandleStrategy.EAGER;
                break;
            case "EAGER_PERSIST":
                strategyObject = HandleStrategy.EAGER_PERSIST;
                break;
        }

        axmlDoc = new AxmlDoc(fXmlFile, strategyObject, ReturnType.TEXT);

        axmlDoc.getDocumentElement().normalize();

        Toast.makeText(getApplicationContext(), "Parsing xml file...", Toast.LENGTH_SHORT).show();
        new axmlDocParseTask().execute(axmlDoc);


    }

    public void proceed(){
        NodeList nList = axmlDoc.getElementsByTagName("livro");

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                TextView bookLabelAndName = new TextView(this.getApplicationContext());
                Element eElement = (Element) nNode;
                bookLabelAndName.setText("Example book title: " + eElement.getAttribute("titulo") + ". File size: " + originalSize + "B");

                LinearLayout chapterSelectLayout = (LinearLayout) findViewById(R.id.chapterSelectLayout);
                chapterSelectLayout.addView(bookLabelAndName);

                TextView chooseChapterLabel = new TextView(this.getApplicationContext());
                chooseChapterLabel.setText("Choose a chapter or back button for previous menu:");
                chooseChapterLabel.setTextSize(20);

                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                llp.setMargins(0, 30, 0, 0); // llp.setMargins(left, top, right, bottom);
                chooseChapterLabel.setLayoutParams(llp);

                chapterSelectLayout.addView(chooseChapterLabel);

                NodeList capitulos = nNode.getChildNodes();
                ListView chapterList = new ListView(this.getApplicationContext());

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

                for (int c = 0; c < capitulos.getLength(); c++) {
                    Node capituloNode = capitulos.item(c);
                    if (capituloNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element capitulo = (Element) capituloNode;
                        String texto = "Capitulo " + capitulo.getAttribute("numero") + ": "
                                + capitulo.getAttribute("titulo");
                        adapter.add(texto);
                    }
                }
                chapterList.setAdapter(adapter);
                chapterList.setOnItemClickListener(this);
                chapterSelectLayout.addView(chapterList);

            }


        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id ){
        String item = (String) parent.getItemAtPosition(position);

        chapterContents = new Intent(this, ChapterContents.class);

        new GetTextTask().execute(position + 1);

    }

    protected void getContents(){
        chapterContents.putExtra("contents", contents);

        startActivity(chapterContents);

    }

    private class axmlDocParseTask extends AsyncTask<AxmlDoc, Void, Void> {

        protected Void doInBackground(AxmlDoc... axmlDocs){
            axmlDocs[0].parse();
            return null;
        }

        protected void onPostExecute(Void nothing){
            proceed();
        }
    }

    private class GetTextTask extends AsyncTask<Integer, Void, Void> {

        protected Void doInBackground(Integer... pos) {
            contents = axmlDoc.getTextFromSingleNode("/livro/capitulo["+pos[0]+"]");
            getContents();
            return null;
        }
    }
}


