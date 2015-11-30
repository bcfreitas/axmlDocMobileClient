package axmldoc.bcfreitas.axmldocmobileclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ChooseStrategyActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_strategy);

        ListView strategyOptions = (ListView) findViewById(R.id.strategy_options);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.strategy, android.R.layout.simple_list_item_1);

        strategyOptions.setAdapter(adapter);

        strategyOptions.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id ){
        String item = (String) parent.getItemAtPosition(position);

        Intent chapterSelectActitivity = new Intent(this, ChapterSelectActivity.class);

        chapterSelectActitivity.putExtra("strategy", item);

        startActivity(chapterSelectActitivity);


    }

}

