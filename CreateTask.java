package edu.byui.falin.threading;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.FileOutputStream;


// This is a subclass of AsycnTask we have created in order to create a file on a background
// thread. For more details on AsyncTask,
// see: https://developer.android.com/reference/android/os/AsyncTask.html
//
// Since this is a templated class, we can use whatever datatypes we want. The ones we choose
// will be the types used as the parameter types for doInBackground(), onProgressUpdate(), and
// onPostExecute(), respectively.
public class CreateTask extends AsyncTask<Void, Integer, Void> {

    String _fileName;
    Context _theContext;
    ProgressBar _progressBar;

    public CreateTask(String filename, Context theContext, ProgressBar pb)
    {
        _progressBar = pb;
        _theContext = theContext;
        _fileName = filename;
    }

    @Override
    protected void onPreExecute() {
        // When the btnCreateClick() method in MainActivity calls task.execute(), the first thing
        // that happens is the task's onPreExecute() method is called on the main UI thread.
        // This gives us a chance to do any UI tasks we want to do before the process starts.
        _progressBar.setProgress(0);

        // For more information about Toasts, see:
        // https://developer.android.com/guide/topics/ui/notifiers/toasts.html
        Toast toast = Toast.makeText(_theContext, "Creating file...", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        // After the onPreExecute() method finishes, doInBackground() is called.
        // Here, we're creating our file, writing 10 items to it, and pausing after each item is
        // added, just to simulate a long-running task.
        try(FileOutputStream outputStream = _theContext.openFileOutput(_fileName, Context.MODE_PRIVATE)) {

            for(int i = 1; i <= 10; i++) {
                String line = String.format("%d\n", i);
                outputStream.write(line.getBytes());

                // This is how we trigger the onProgressUpdate() method, which will update the
                // progress bar for us.
                publishProgress(i);
                Thread.sleep(250);
            }

            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... data) {

        // This method gets called whenever there is a call to publishProgress(). The
        // important part to note is that the item passed into publishProgress() is actually an
        // array of length 1, so we have to use array syntax to retrieve the value.
        // Since this method is always called on the main thread automatically, we can update
        // the UI here.
        _progressBar.setProgress(data[0]);
    }

    @Override
    protected void onPostExecute(Void result) {

        // Once doInBackground() finishes, this method is called on the main thread to
        // allow us to do any post-processing UI updates we need to do.
        _progressBar.setProgress(0);

        Toast toast = Toast.makeText(_theContext, "File created.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
