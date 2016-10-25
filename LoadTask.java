package edu.byui.falin.threading;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by lfalin on 5/31/16.
 */

// This class is our second subclass of AsyncTask. For details on how it works,
// see the notes in CreateTask.java. The important difference is in onProgressUpdate().
public class LoadTask extends AsyncTask<Void, String, Void> {

    String _fileName;
    ArrayAdapter<String> _adapter;
    Context _theContext;
    ProgressBar _progressBar;

    public LoadTask(String filename, ArrayAdapter<String> adapter, Context theContext, ProgressBar pb)
    {
        _progressBar = pb;
        _theContext = theContext;
        _fileName = filename;
        _adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        _progressBar.setProgress(0);

        Toast toast = Toast.makeText(_theContext, "Loading data...", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(_theContext.openFileInput(_fileName)))) {

            String theString;
            while((theString = reader.readLine()) != null) {
                publishProgress(theString);
                Thread.sleep(250);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... data) {

        // Here, we're doing two things. First, we take our data item which was just loaded from
        // the file, and add it to our Array Adapter
        _adapter.add(data[0]);

        // Second, we set our progress bar to how many items are in the data adapter.
        _progressBar.setProgress(_adapter.getCount());
    }

    @Override
    protected void onPostExecute(Void result) {
        _progressBar.setProgress(0);
        Toast toast = Toast.makeText(_theContext, "Data loaded.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
