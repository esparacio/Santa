package edu.elon.secretsanta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends Activity {

    private final String filename = "preferences.txt";
    int userID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SharedPreferences userIDSettings = getPreferences(0);
        //int userID = userIDSettings.getInt("userID", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("test print");
            Context context = getBaseContext();
            BufferedReader reader = null;
            try {
                InputStream in = context.openFileInput(filename);
                Scanner scanner = new Scanner(new InputStreamReader(in));
                int line = scanner.nextInt();
                userID = line;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        if (userID == 0) {
            //if statement which does not run if userID has been set already
            new Thread(new Runnable() {
                public void run() {
                    URL url = null;
                    try {
                        url = new URL("http://trumpy.cs.elon.edu:5000/secret/new");
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    Object[] params = new Object[1];
                    params[0] = url;
                    CurrentUser user = new CurrentUser();
                    int newUserID = (int) user.doInBackground(params);
                    //SharedPreferences userIDSettings = getPreferences(0);
                    //SharedPreferences.Editor editor = userIDSettings.edit();
                    //editor.putInt("userID", newUserID);
                    //editor.commit();
                    Context context = getBaseContext();
                    Writer writer = null;

                    try {
                        OutputStream out = context.openFileOutput(filename, Context.MODE_PRIVATE);
                        writer = new OutputStreamWriter(out);
                        writer.write(Integer.toString(newUserID));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (writer != null) {
                            try {
                                writer.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
        //int currentUserID = userIDSettings.getInt("userID", 0);
        //CurrentUser user = new CurrentUser();
        //user.setUserID(currentUserID);
        System.out.println("Current user ID = " + userID);
    }

    public void launchGroup(View view) {
        Intent intent = new Intent(this, GroupNum.class);
        startActivity(intent);
    }

    public void launchPerson(View view) {
        Intent intent = new Intent(this, Person.class);
        startActivity(intent);
    }

    public void launchWishlist(View view) {
        Intent intent = new Intent(this, Wishlist.class);
        startActivity(intent);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
