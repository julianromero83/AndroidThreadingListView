package edu.byui.falin.threading;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String FILENAME = "numbers.txt";

    ArrayAdapter<String> _adapter;
    List<String> _data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // _data is just an ArrayList of strings we're going to use to hold our data.
        _data = new ArrayList<String>();

        // When we create an array adapter, we tell it about the context from which it will be
        // run (this), the layout we want it to use for each item in the list, and the place
        // where it can find the data.
        // In this case, we're using one of the builtin layout styles, simple_list_item_1. We can
        // also create a custom layout if we wanted to.
        // Examples:
        // - http://www.ezzylearning.com/tutorial/customizing-android-listview-items-with-custom-arrayadapter
        // - http://www.vogella.com/tutorials/AndroidListView/article.html
        _adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _data);

        // Once we have finished creating the adapter, we just need to tell our ListView about it.
        // Whenever data is added to the adapter, it will store in the ArrayList, and tell
        // the listview that it needs to update its display.
        ListView theList = (ListView)findViewById(R.id.listView);
        theList.setAdapter(_adapter);

    }

    public void btnCreateClick(View theView) {

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);

        // CreateTask is a custom subclass of AsyncTask. Here we pass in the name of the file
        // we want it to create, a reference to the current application context, so it
        // knows which activity is using it, and a reference to the progress bar that we
        // want it to update.
        CreateTask task = new CreateTask(FILENAME, this, progressBar);
        task.execute();
    }

    public void btnLoadClick(View theView) {

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);

        // LoadTask is another custom subclass of AsyncTask. We use it like we did previously,
        // except we also pass in a reference to the array adapter, so that it knows
        // where to store the data it loads.
        LoadTask task = new LoadTask(FILENAME, _adapter, this, progressBar);
        task.execute();
    }

    public void btnClearClick(View theView) {

        // Clearing the list is as simple as clearing the adapter.
        _adapter.clear();

        // This wasn't required, but here we are just resetting the progress bar.
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(0);
    }


}
