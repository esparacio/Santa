package edu.elon.secretsanta;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by pol10_000 on 12/7/2015.
 */
public class CurrentUser extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] params) {
        URL url = (URL)params[0];
        int userID = 0;
        try {
            URLConnection conn = url.openConnection();
            Scanner sc = new Scanner(new InputStreamReader(conn.getInputStream()));
            while ((sc.hasNextInt())) {
                userID= sc.nextInt();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userID;
    }
}
