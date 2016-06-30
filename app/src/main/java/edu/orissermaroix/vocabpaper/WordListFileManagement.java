package edu.orissermaroix.vocabpaper;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by VieVie31 on 30/06/2016.
 */
public class WordListFileManagement {
    public static boolean actualiseWordList = false;


    public static void writeStringToFile(String data) {
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(sdcard, "myWords.csv");

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(data);
            bw.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        actualiseWordList = true;
    }

    public static void writeToFile(ArrayList<MyWord> words) {
        StringBuilder s = new StringBuilder();
        for (MyWord w : words) {
            s.append(w.getEnglish());
            s.append(",");
            s.append(w.getFrench());
            s.append("\n");
        }

        String data = s.toString();
        writeStringToFile(data);
    }

    public static ArrayList<MyWord> getWordsToStudy() {
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "myWords.csv"); //myWords.txt contains each word comma separated

        ArrayList<MyWord> myWords = new ArrayList<MyWord>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String[] parts;

            while ((line = br.readLine()) != null) {
                try {
                    parts = line.split(",");
                    myWords.add(new MyWord(parts[0], parts[1]));
                } catch (Exception e) {
                    // just a bad line ? maybe
                }
            }

            br.close();
        } catch (IOException e) {
            // TODO : catch the exception... :p
            Log.e("READ failed !!", "can't import myWords.csv !! :'(\n" + e.toString());
        }

        return myWords;
    }

}
