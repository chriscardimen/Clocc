package chriscardimen.pocketick;

import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity<Sender> extends AppCompatActivity {
    public static int time_vis = View.VISIBLE;
    public static int database_vis = View.VISIBLE;
    public static int clock_vis = View.INVISIBLE;
    public static int image_clock_vis = View.VISIBLE;
    public static int data_vis = View.INVISIBLE;
    public static int search_vis = View.INVISIBLE;
    public static int serial_vis = View.INVISIBLE;
    public static Timer time;
    public static TimerTask task;
    public static boolean onOff = true;
    public static Document doc = null;
    public static String serial_num_to_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.RGB_565);


        TextView clockTime = (TextView) findViewById(R.id.clockTime);
        TextView data_view = (TextView) findViewById(R.id.results);

        TextView serial = (TextView) findViewById(R.id.serial_num);
        Button database_button = (Button) findViewById(R.id.database_button);
        Button clock_button = (Button) findViewById(R.id.clock_button);
        ImageView image_clock = (ImageView) findViewById(R.id.clock_image);
        Button search = (Button) findViewById(R.id.search);



        data_view.setVisibility(data_vis);
        clockTime.setVisibility(time_vis);
        database_button.setVisibility(database_vis);
        clock_button.setVisibility(clock_vis);
        image_clock.setVisibility(image_clock_vis);
        search.setVisibility(search_vis);
        serial.setVisibility(serial_vis);



        /*
         * The following code is not my own. I found it on StackOverflow
         * while trying to figure out how to make a clock tick every second.
         * the run() method and below code in onCreate comes from:
         * https://stackoverflow.com/questions/14814714/update-textview-every-second.
         * What happens in updateTextView() I wrote myself.
         */

        time = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isOn()) {
                            updateTextView();
                        } else {
                            task.cancel();
                            time.cancel();
                            time.purge();
                        }
                    }

                    ;
                });

            }

        };
        time.schedule(task, 0, 375);
        ((TextView) findViewById(R.id.clockTime)).setVisibility(time_vis);
        ((Button) findViewById(R.id.database_button)).setVisibility(database_vis);
        ((Button) findViewById(R.id.clock_button)).setVisibility(clock_vis);
        ((ImageView) findViewById(R.id.clock_image)).setVisibility(image_clock_vis);
        ((Button) findViewById(R.id.search)).setVisibility(search_vis);
        ((TextView) findViewById(R.id.serial_num)).setVisibility(serial_vis);


    }

    public void updateTextView() {
        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.clockTime)).setVisibility(time_vis);
        ((Button) findViewById(R.id.database_button)).setVisibility(database_vis);
        ((Button) findViewById(R.id.clock_button)).setVisibility(clock_vis);
        ((ImageView) findViewById(R.id.clock_image)).setVisibility(image_clock_vis);
        ((Button) findViewById(R.id.search)).setVisibility(search_vis);
        ((TextView) findViewById(R.id.serial_num)).setVisibility(serial_vis);


        Calendar c = Calendar.getInstance();
        Date time = c.getTime();
        int am_or_pm = c.get(Calendar.AM_PM);
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        String am_pm = "";
        if (am_or_pm == 0) {
            am_pm = "am";
            if (hours == 0) {
                hours = 12;
            } else {
                hours = time.getHours();
            }
            minutes = time.getMinutes();
            seconds = time.getSeconds();
        } else {
            am_pm = "pm";
            if (hours == 12) {
                hours = 12;
            } else {
                hours = (time.getHours() - 12);
            }


            minutes = time.getMinutes();
            seconds = time.getSeconds();
        }
        String displayTime = hours + ":" + minutes + ":" + seconds + " " + am_pm;
        ((TextView) findViewById(R.id.clockTime)).setText(displayTime);
    }

    public void onButtonTap(View v) throws IOException, InterruptedException {
        switch (v.getId()) {
            //If flipButton is pressed
            case R.id.database_button:
                time_vis = View.INVISIBLE;
                image_clock_vis = View.INVISIBLE;
                data_vis = View.VISIBLE;
                database_vis = View.INVISIBLE;
                search_vis = View.VISIBLE;
                serial_vis = View.VISIBLE;
                clock_vis = View.VISIBLE;
                time = new Timer();
                task = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isOn()) {
                                    updateTextView();
                                } else {
                                    task.cancel();
                                    time.cancel();
                                    time.purge();
                                }
                            }

                            ;
                        });

                    }

                };
                time.schedule(task, 0, 375);
                ((TextView) findViewById(R.id.clockTime)).setVisibility(time_vis);
                ((Button) findViewById(R.id.database_button)).setVisibility(database_vis);
                ((Button) findViewById(R.id.clock_button)).setVisibility(clock_vis);
                ((ImageView) findViewById(R.id.clock_image)).setVisibility(image_clock_vis);
                ((Button) findViewById(R.id.search)).setVisibility(search_vis);
                ((TextView) findViewById(R.id.serial_num)).setVisibility(serial_vis);

                onOff = false;


                break;
            case R.id.clock_button:
                time_vis = View.VISIBLE;
                image_clock_vis = View.VISIBLE;
                database_vis = View.VISIBLE;
                data_vis = View.INVISIBLE;
                search_vis = View.INVISIBLE;
                clock_vis = View.INVISIBLE;
                serial_vis = View.INVISIBLE;

                onOff = true;
                time = new Timer();
                task = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isOn()) {
                                    updateTextView();
                                } else {
                                    task.cancel();
                                    time.cancel();
                                    time.purge();
                                }
                            }

                            ;
                        });

                    }

                };
                time.schedule(task, 0, 375);
                ((TextView) findViewById(R.id.clockTime)).setVisibility(time_vis);
                ((Button) findViewById(R.id.database_button)).setVisibility(database_vis);
                ((Button) findViewById(R.id.clock_button)).setVisibility(clock_vis);
                ((ImageView) findViewById(R.id.clock_image)).setVisibility(image_clock_vis);
                ((Button) findViewById(R.id.search)).setVisibility(search_vis);
                ((TextView) findViewById(R.id.serial_num)).setVisibility(serial_vis);
                ((TextView) findViewById(R.id.serial_num)).setMovementMethod(new ScrollingMovementMethod());

                break;

            case R.id.search:
                serial_num_to_search = ((TextView) findViewById(R.id.serial_num)).getText().toString();
                String to_send_to_channel = "";
                //String serial_num = to_split[1];
                // Initialize document


                // Try/catch statement to crawl over the movement info text
                connect(v);

                // Assign the document text to a sring
                String watch_database = doc.wholeText();
                watch_database = watch_database.replaceAll("\t", "");
                // Initialize the index to start at.
                int indexer = watch_database.indexOf("Manufacturer:");
                if (indexer < 0) {
                    to_send_to_channel += "This serial number does not exist in the database.";
                } else {
                    to_send_to_channel += "";
                    String information_we_want = watch_database.substring(indexer,
                            watch_database.indexOf("Data Confidence Rating"));
                    for (int x = 0; x < information_we_want.indexOf("Data Research")
                            || x <= information_we_want.indexOf("U.S."); ++x) {
                        char current = information_we_want.charAt(x);
                        if (current >= 65 && current <= 90) {

                            if (to_send_to_channel.length() > 2 && to_send_to_channel.charAt(to_send_to_channel.length() - 2) == ':') {
                                to_send_to_channel = to_send_to_channel.substring(0, to_send_to_channel.length() - 1);
                            }
                            to_send_to_channel += " "
                                    + information_we_want.substring(x, information_we_want.indexOf("\n", x)) + "\n";
                            x = information_we_want.indexOf("\n", x) - 1;
                        }

                    }
                    System.out.println(to_send_to_channel.charAt(to_send_to_channel.length()-2));
                    int count = 0;
                    if (to_send_to_channel.charAt(to_send_to_channel.length()-2) == ':') {
                        for (int x = 0; x < to_send_to_channel.length(); ++x){
                            if (to_send_to_channel.charAt(x) == '\n'){
                                ++count;
                            }
                        }
                        System.out.println(count);
                        String temp = "";
                        int max_count = count;
                        count = 0;
                        for (int x = 0; x < to_send_to_channel.length(); ++x){

                            if (count < max_count-1){
                                temp += to_send_to_channel.charAt(x);
                            }
                            if (to_send_to_channel.charAt(x) == '\n'){
                                ++count;
                            }
                        }
                        to_send_to_channel = temp;
                        //to_send_to_channel = to_send_to_channel.substring(0, to_send_to_channel.lastIndexOf('\n'));
                    }
                }

                ((TextView) findViewById(R.id.results)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.results)).setText(to_send_to_channel);
                ((TextView) findViewById(R.id.serial_num)).setMovementMethod(new ScrollingMovementMethod());

        }

    }


    public boolean isOn() {
        if (onOff) {
            return true;
        } else {
            return false;
        }
    }

    public void connect(View v) throws InterruptedException {
        FetchWebsiteData data = new FetchWebsiteData();
        synchronized (data) {
            data.execute();
            System.out.println(data.getStatus());
            data.wait();
            doc = data.document;
        }

    }

}

class FetchWebsiteData extends AsyncTask<Void, Void, Void> {
    public Document document;

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Void doInBackground(Void... params) {
        if (android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();


        try {
            // Connect to website
            document = Jsoup.connect("https://pocketwatchdatabase.com/search/result/elgin/" + MainActivity.serial_num_to_search).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("It is reached");
        synchronized (this) {
            this.notify();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        MainActivity.doc = document;
        System.out.print("It is done.");

    }
}



