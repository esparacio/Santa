package edu.elon.secretsanta;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Person extends Activity {

    private TextView nameView;
    private TextView wishesView;
    private final String filename = "preferences.txt";
    private final String groupfile = "group.txt";
    int userID = 0;
    int group = 0;
    int matchedUserID = 0;
    String matchedUserName = null;
    String matchedUserWishes = null;

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
        setContentView(R.layout.activity_person);
        new Thread(new Runnable() {
            public void run() {
                System.out.println("enter thread for matched user");
                URL url = null;
                try {
                    //THIS IS HARDCODED, CHANGE IT
                    //int group = 556;
                    Context context = getBaseContext();
                    BufferedReader reader = null;
                    try {
                        InputStream in = context.openFileInput(groupfile);
                        Scanner scanner = new Scanner(new InputStreamReader(in));
                        int line = scanner.nextInt();
                        group = line;
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

                    url = new URL("http://trumpy.cs.elon.edu:5000/secret/match/" + group + "/" + userID);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                URLConnection conn = null;
                try {
                    conn = url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scanner sc = null;
                try {
                    sc = new Scanner(new InputStreamReader(conn.getInputStream())).useDelimiter("~");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while ((sc.hasNextInt())) {
                    matchedUserID = sc.nextInt();
                    System.out.println("userID of matched user is " + matchedUserID);
                }
                URL matchedUserURL = null;
                try {
                    matchedUserURL = new URL("http://trumpy.cs.elon.edu:5000/secret/get/" + matchedUserID);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                URLConnection matchedUserConn = null;
                try {
                    matchedUserConn = matchedUserURL.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scanner matchedUserSC = null;
                try {
                    matchedUserSC = new Scanner(new InputStreamReader(matchedUserConn.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("matched user info here");
                if ((matchedUserSC.hasNext())) {
                    matchedUserName = matchedUserSC.next();
                    System.out.println("matched user is "+ matchedUserName);
                }
                if ((matchedUserSC.hasNext())) {
                    matchedUserWishes = matchedUserSC.next();
                    System.out.println("matched user wishes are "+ matchedUserWishes);
                }
                nameView = (TextView) findViewById(R.id.personname);
                wishesView = (TextView) findViewById(R.id.personwishes);
                nameView.setText(matchedUserName);
                wishesView.setText(matchedUserWishes);
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_person, menu);
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
