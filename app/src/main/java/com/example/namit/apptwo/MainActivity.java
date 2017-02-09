package com.example.namit.apptwo;

import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.namit.apptwo.ADBHelper.FeedEntry.COLUMN_NAME_SUBTITLE;
import static com.example.namit.apptwo.ADBHelper.FeedEntry.COLUMN_NAME_TITLE;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView mListView = (ListView)findViewById(R.id.sqList);

        final FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplicationContext());

        final Button sqwr = (Button)findViewById(R.id.write);
        sqwr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ((EditText)findViewById(R.id.heading)).getText().toString();
                String subtitle = ((EditText)findViewById(R.id.subheading)).getText().toString();
                // Gets the data repository in write mode
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_TITLE, title);
                values.put(ADBHelper.FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);

// Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(ADBHelper.FeedEntry.TABLE_NAME, null, values);

                Snackbar.make(findViewById(R.id.content_main), "Written", Snackbar.LENGTH_SHORT).show();


            }
        });

        //Reading From DB


        final Button sqrd = (Button)findViewById(R.id.find);
        sqrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = mDbHelper.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
                String[] projection = {
                        _ID,
                        COLUMN_NAME_TITLE,
                        ADBHelper.FeedEntry.COLUMN_NAME_SUBTITLE
                };

// Filter results WHERE "title" = 'My Title'
                String selection = COLUMN_NAME_TITLE + " = ?";
                String[] selectionArgs = {((EditText)findViewById(R.id.crit)).getText().toString()};

// How you want the results sorted in the resulting Cursor
                String sortOrder =
                        ADBHelper.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

                Cursor c = db.query(
                        ADBHelper.FeedEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        selectionArgs,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        sortOrder                                 // The sort order
                );

                StringBuffer output;
                final ArrayList res = new ArrayList<String>();

                if(c.getCount()>0){
                    while (c.moveToNext()) {
                        output = new StringBuffer();
                        // Update the progress message
                /*updateBarHandler.post(new Runnable() {
                    public void run() {
                        pDialog.setMessage("Reading contacts : "+ counter++ +"/"+cursor.getCount());
                    }
                });*/
                        String id = c.getString(c.getColumnIndex(_ID));
                        String name = c.getString(c.getColumnIndex(COLUMN_NAME_TITLE));
                        String sname = c.getString(c.getColumnIndex(COLUMN_NAME_SUBTITLE));
                        output.append("ID:" + id + "\nTitle:" + name + "Subtitle:" + sname);
                        Log.i("Found:", output.toString());
                        ((TextView)findViewById(R.id.hello)).setText(((TextView)findViewById(R.id.hello)+"\n"+output.toString()));
                        res.add(output.toString());
                    }
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.sqtext, res);
                            mListView.setAdapter(adapter);
                        }
                    });*/
                }
                else {
                    Snackbar.make(findViewById(R.id.content_main), "Not Found", Snackbar.LENGTH_SHORT).show();
                }
            }
        });



        final Intent k = new Intent(this, HelloService.class);
        final boolean servRunning = false;

        final Button ser = (Button)findViewById(R.id.serv);
        ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    startService(k);

            }
        });

        //startService(k);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent impl = new Intent();
                impl.setAction(Intent.ACTION_SEND);
                impl.putExtra(Intent.EXTRA_TEXT, "You have a message.");
                startActivity(impl);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        Button printIt = (Button)findViewById(R.id.gettingText);
        final RadioGroup r = (RadioGroup)findViewById(R.id.lang);
        printIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ((EditText)findViewById(R.id.textEntered)).getText().toString();
                int sel = r.getCheckedRadioButtonId();
                if (sel == R.id.eng)
                    s = "Hello "+s;
                else if (sel == R.id.frn)
                    s = "Bonjour "+s;
                else if(sel == R.id.hnd)
                    s = "Namskar "+s;
                else if(sel == R.id.knd)
                    s = "Namashkara "+s;
                else if(sel == R.id.esp)
                    s = "Hola "+s;
                else
                    s = "Domo "+s;
                ((TextView)findViewById(R.id.hello)).setText(s);
            }
        });

        Button call = (Button)findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ((EditText)findViewById(R.id.phno)).getText().toString();
                Intent c = new Intent(Intent.ACTION_CALL);
                s="tel:"+s;
                c.setData(Uri.parse(s));
                startActivity(c);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, Main2Activity.class);
            startActivity(i);
            Log.d("SOMETHING", "abc");

        }

        else if(id == R.id.anotherText) {
            //Snackbar.make(findViewById(R.id.content_main), "some text", Snackbar.LENGTH_SHORT).show();
            Intent i = new Intent(this, MapsActivity.class);
            startActivity(i);
            /*Fragment nFragement = new AFragment();
            FragmentTransaction t = getFragmentManager().beginTransaction();
            t.replace(R.id.aFragement, nFragement);
            t.commit();*/

        }

        else if(id == R.id.yetAnother) {
            Intent i = new Intent(this, Main5Activity.class);
            startActivity(i);
        }

        else {
            Intent i = new Intent(this, webbing.class);
            startActivity(i);
            Log.d("SOMETHING", "abc");
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


}
