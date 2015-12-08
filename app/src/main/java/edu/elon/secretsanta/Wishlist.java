package edu.elon.secretsanta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedInputStream;
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
import java.net.URLConnection;
import java.util.Scanner;

public class Wishlist extends Activity {

    private EditText editName;
    private EditText editGroup;
    private EditText editWishes;
    private final String filename = "preferences.txt";
    private final String groupfile = "group.txt";
    int userID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        editName = (EditText) findViewById(R.id.name);
        editGroup = (EditText) findViewById(R.id.group);
        editWishes = (EditText) findViewById(R.id.wishes);
    }

    public void launchSent(View view) {
        new Thread(new Runnable() {
            public void run() {
                String userName = editName.getText().toString();
                String userGroup = editGroup.getText().toString();
                String userWishes = editWishes.getText().toString();
                System.out.println("Wishes set to " + userWishes);
                System.out.println("User ID get method sez" + userID);
                URL setUserInfo = null;
                URL addUserGroup = null;
                try {
                    setUserInfo = new URL("http://trumpy.cs.elon.edu:5000/secret/update/" + userID + "?name=" + userName + "&wish=~" + userWishes);
                    addUserGroup = new URL("http://trumpy.cs.elon.edu:5000/secret/join/" + userGroup + "/" + userID);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                Context context = getBaseContext();
                Writer writer = null;

                try {
                    OutputStream out = context.openFileOutput(groupfile, Context.MODE_PRIVATE);
                    writer = new OutputStreamWriter(out);
                    writer.write(userGroup);
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
                try {
                    URLConnection openUserInfo = setUserInfo.openConnection();
                    URLConnection openUserGroup = addUserGroup.openConnection();
                    InputStream infoIn = new BufferedInputStream(openUserInfo.getInputStream());
                    InputStream groupIn = new BufferedInputStream(openUserGroup.getInputStream());
                    Scanner scOne = new Scanner(infoIn);
                    Scanner scTwo = new Scanner(groupIn);
                    while ((scOne.hasNext())) {
                        String testOne = scOne.next();
                    }
                    while ((scTwo.hasNext())) {
                        String testTwo = scTwo.next();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }).start();
            //Scanner infoScanner = new Scanner(new InputStreamReader(openUserInfo.getInputStream()));
            //Scanner groupScanner = new Scanner(new InputStreamReader(openUserGroup.getInputStream()));
            //while ((infoScanner.hasNext())) {
            //    String confirm = infoScanner.next();
            //    System.out.println(confirm);
            //}
            //while ((groupScanner.hasNext())) {
            //    String confirm = infoScanner.next();
            //    System.out.println(confirm);
            //}

        Intent intent = new Intent(this, Sent.class);
        startActivity(intent);
    }

    public void launchNaught(View view) {
        Intent intent = new Intent(this, Naught.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wishlist, menu);
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
