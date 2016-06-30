package edu.orissermaroix.vocabpaper;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by VieVie31 on 30/06/2016.
 */
public class SetWallpaperActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //import my dico
        ArrayList<MyWord> myWords = WordListFileManagement.getWordsToStudy();
        StringBuilder s = new StringBuilder();
        for (MyWord w : myWords) {
            s.append(w.getEnglish());
            s.append(",");
            s.append(w.getFrench());
            s.append("\n");
        }

        //print it in the text field
        EditText et = (EditText) findViewById(R.id.editText);
        et.setText(s.toString());
    }

    public void onClick(View view) {
        //save the dico
        EditText et = (EditText) findViewById(R.id.editText);
        WordListFileManagement.writeStringToFile(et.getText().toString());

        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, MyWallpaperService.class));
        startActivity(intent);
    }
}