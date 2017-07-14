/*
 * Copyright (c) RedDotDev 2017.
 *
 * The following codes are property of the RedDotDev.
 * Unless subject to explicit written approval, there should be no reproduction of the codes in any means.
 */

package sg.reddotdev.sharkfin.manager.deprecated;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class FourDRetrieve extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        try {
            Log.d("Process", "Connecting");
            Document doc = Jsoup.connect(strings[0]).get();
            Log.d("Process", "Connected");
            Element testNode = doc.select("div.four-d-results").first();
            logLargeString(testNode.toString());

        } catch (IOException e) {
            Log.d("Process", "Something went wrong!");
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    public void logLargeString(String str) {
        if(str.length() > 3000) {
            Log.i("Process", str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i("Process", str); // continuation
        }
    }
}
