package chriscardimen.pocketick;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

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
    public static int info_vis = View.INVISIBLE;
    public static int spinner_vis = View.INVISIBLE;
    public static int nightday_vis = View.INVISIBLE;
    public static Timer time;
    public static TimerTask task;
    public static boolean onOff = true;
    public static Document doc = null;
    public static String serial_num_to_search;
    public static String brand_to_search;
    public static boolean night_vs_day;
    public static boolean help;


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
        ImageButton nightDay = (ImageButton) findViewById(R.id.nightDay_button);
        ImageButton info = (ImageButton) findViewById(R.id.info_button);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        night_vs_day = true;
        help = true;

        data_view.setVisibility(data_vis);
        clockTime.setVisibility(time_vis);
        database_button.setVisibility(database_vis);
        clock_button.setVisibility(clock_vis);
        image_clock.setVisibility(image_clock_vis);
        search.setVisibility(search_vis);
        serial.setVisibility(serial_vis);
        info.setVisibility(info_vis);
        spinner.setVisibility(spinner_vis);
        nightDay.setVisibility(nightday_vis);

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
        ((ImageButton) findViewById(R.id.info_button)).setVisibility(info_vis);
        ((Spinner) findViewById(R.id.spinner)).setVisibility(spinner_vis);
        ((ImageButton) findViewById(R.id.nightDay_button)).setVisibility(nightday_vis);


    }

    public void updateTextView() {

        ((TextView) findViewById(R.id.clockTime)).setVisibility(time_vis);
        ((Button) findViewById(R.id.database_button)).setVisibility(database_vis);
        ((Button) findViewById(R.id.clock_button)).setVisibility(clock_vis);
        ((ImageView) findViewById(R.id.clock_image)).setVisibility(image_clock_vis);
        ((Button) findViewById(R.id.search)).setVisibility(search_vis);
        ((TextView) findViewById(R.id.serial_num)).setVisibility(serial_vis);
        ((ImageButton) findViewById(R.id.info_button)).setVisibility(info_vis);
        ((Spinner) findViewById(R.id.spinner)).setVisibility(spinner_vis);
        ((ImageButton) findViewById(R.id.nightDay_button)).setVisibility(nightday_vis);



        Calendar c = Calendar.getInstance();
        Date time = c.getTime();
        int am_or_pm = c.get(Calendar.AM_PM);
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        String am_pm = "";
        String minute_string = "";
        String second_string = "";

        if (am_or_pm == 0) {
            am_pm = "am";
            if (time.getHours() == 0) {
                hours = 12;
            } else {
                hours = time.getHours();
            }



            minutes = time.getMinutes();
            if (minutes < 10) {
                minute_string = "0" + minutes;
            }
            else{
                minute_string += minutes;
            }
            seconds = time.getSeconds();
            if (seconds < 10) {
                second_string = "0" + seconds;
            }
            else{
                second_string += seconds;
            }

        } else {
            am_pm = "pm";
            if (time.getHours() == 12) {
                hours = 12;
            } else {
                hours = (time.getHours() - 12);
            }


            minutes = time.getMinutes();
            if (minutes < 10) {
                minute_string = "0" + minutes;
            }
            else{
                minute_string += minutes;
            }
            seconds = time.getSeconds();
            if (seconds < 10) {
                second_string = "0" + seconds;
            }
            else{
                second_string += seconds;
            }
        }
        String displayTime = hours + ":" + minute_string + ":" + second_string + " " + am_pm;
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
                info_vis = View.VISIBLE;
                spinner_vis = View.VISIBLE;
                nightday_vis = View.VISIBLE;
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
                ((ImageButton) findViewById(R.id.info_button)).setVisibility(info_vis);
                ((Spinner) findViewById(R.id.spinner)).setVisibility(spinner_vis);
                ((ImageButton) findViewById(R.id.nightDay_button)).setVisibility(nightday_vis);


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
                info_vis = View.INVISIBLE;
                spinner_vis = View.INVISIBLE;
                nightday_vis = View.INVISIBLE;
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
                ((TextView) findViewById(R.id.results)).setVisibility(data_vis);
                ((TextView) findViewById(R.id.serial_num)).setMovementMethod(new ScrollingMovementMethod());
                ((ImageButton) findViewById(R.id.info_button)).setVisibility(info_vis);
                ((Spinner) findViewById(R.id.spinner)).setVisibility(spinner_vis);
                ((ImageButton) findViewById(R.id.nightDay_button)).setVisibility(nightday_vis);


                break;

            case R.id.search:
                serial_num_to_search = ((TextView) findViewById(R.id.serial_num)).getText().toString();
                Spinner spinner = (Spinner)findViewById(R.id.spinner);
                brand_to_search = spinner.getSelectedItem().toString();

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
                            watch_database.indexOf("Data Verification Reports"));
                    for (int x = 0; x < information_we_want.indexOf("Data Research")
                            || x <= information_we_want.indexOf("U.S.") || x<= information_we_want.indexOf("Railroad Grade"); ++x) {
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
                    //System.out.println(to_send_to_channel.charAt(to_send_to_channel.length() - 2));
                    int count = 0;
                    if (to_send_to_channel.length() > 2 &&
                            to_send_to_channel.charAt(to_send_to_channel.length() - 2) == ':') {
                        for (int x = 0; x < to_send_to_channel.length(); ++x) {
                            if (to_send_to_channel.charAt(x) == '\n') {
                                ++count;
                            }
                        }
                        System.out.println(count);
                        String temp = "";
                        int max_count = count;
                        count = 0;
                        for (int x = 0; x < to_send_to_channel.length(); ++x) {

                            if (count < max_count - 1) {
                                temp += to_send_to_channel.charAt(x);
                            }
                            if (to_send_to_channel.charAt(x) == '\n') {
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

                break;
        }


    }

    public void onToggleTap(View v) {
        if (help){
            help = false;
            onActive();
        }
        else{
            onInactive();
            help = true;
        }
    }

    public void onNightMode(View v){
        if (night_vs_day){
            night_vs_day = false;
            toNight();
        }
        else{
            toDay();
            night_vs_day = true;
        }
    }

    public void toNight(){
        //System.out.println("The timer is on night mode");
        ImageButton nightDay = (ImageButton)findViewById(R.id.nightDay_button);
        nightDay.setImageResource(R.drawable.clocc_night);
        nightDay.setBackgroundColor(getResources().getColor(R.color.night));


        View rootView = getWindow().getDecorView();
        rootView.setBackgroundColor(getResources().getColor(R.color.night));

        Window mainWindow = getWindow();
        mainWindow.setStatusBarColor(getResources().getColor(R.color.night));
        mainWindow.setNavigationBarColor(getResources().getColor(R.color.night));

        TextView clockTime = (TextView) findViewById(R.id.clockTime);
        clockTime.setBackgroundColor(getResources().getColor(R.color.night));
        clockTime.setTextColor(Color.WHITE);

        TextView data_view = (TextView) findViewById(R.id.results);
        data_view.setBackgroundColor(getResources().getColor(R.color.night));
        data_view.setTextColor(Color.WHITE);


        TextView serial = (TextView) findViewById(R.id.serial_num);
        serial.setBackgroundColor(getResources().getColor(R.color.night));
        serial.setTextColor(Color.WHITE);
        serial.setHintTextColor(Color.WHITE);

        Button database_button = (Button) findViewById(R.id.database_button);
        database_button.setBackgroundColor(getResources().getColor(R.color.night));
        database_button.setTextColor(Color.WHITE);

        Button clock_button = (Button) findViewById(R.id.clock_button);
        clock_button.setBackgroundColor(getResources().getColor(R.color.night));
        clock_button.setTextColor(Color.WHITE);

        ImageView image_clock = (ImageView) findViewById(R.id.clock_image);
        image_clock.setImageResource(R.drawable.icon_inverted);

        ImageView boxView = (ImageView) findViewById(R.id.boxView);
        boxView.setImageResource(R.color.night);

        Button search = (Button) findViewById(R.id.search);
        search.setBackgroundColor(getResources().getColor(R.color.night));
        search.setTextColor(Color.WHITE);

        ImageButton info = (ImageButton) findViewById(R.id.info_button);
        info.setBackgroundColor(getResources().getColor(R.color.night));
        info.setImageResource(R.drawable.help_night);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setBackgroundColor(getResources().getColor(R.color.night));

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerItems, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }

    public void toDay(){
        //System.out.println("The timer is on day mode");
        ImageButton nightDay = (ImageButton)findViewById(R.id.nightDay_button);
        nightDay.setImageResource(R.drawable.clocc_day);
        nightDay.setBackgroundColor(Color.WHITE);


        View rootView = getWindow().getDecorView();
        rootView.setBackgroundColor(Color.WHITE);

        Window mainWindow = getWindow();
        mainWindow.setStatusBarColor(Color.WHITE);
        mainWindow.setNavigationBarColor(Color.WHITE);

        TextView clockTime = (TextView) findViewById(R.id.clockTime);
        clockTime.setBackgroundColor(Color.WHITE);
        clockTime.setTextColor(Color.BLACK);

        TextView data_view = (TextView) findViewById(R.id.results);
        data_view.setBackgroundColor(Color.WHITE);
        data_view.setTextColor(getResources().getColor(R.color.serial));


        TextView serial = (TextView) findViewById(R.id.serial_num);
        serial.setBackgroundColor(Color.WHITE);
        serial.setTextColor(Color.BLACK);
        serial.setHintTextColor(getResources().getColor(R.color.serial));

        Button database_button = (Button) findViewById(R.id.database_button);
        database_button.setBackgroundColor(Color.WHITE);
        database_button.setTextColor(Color.BLACK);

        Button clock_button = (Button) findViewById(R.id.clock_button);
        clock_button.setBackgroundColor(Color.WHITE);
        clock_button.setTextColor(Color.BLACK);

        ImageView image_clock = (ImageView) findViewById(R.id.clock_image);
        image_clock.setImageResource(R.drawable.icon);

        ImageView boxView = (ImageView) findViewById(R.id.boxView);
        boxView.setImageResource(R.color.white);

        Button search = (Button) findViewById(R.id.search);
        search.setBackgroundColor(Color.WHITE);
        search.setTextColor(Color.BLACK);

        ImageButton info = (ImageButton) findViewById(R.id.info_button);
        info.setBackgroundColor(Color.WHITE);
        info.setImageResource(R.drawable.help_day);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setBackgroundColor(Color.WHITE);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerItems, R.layout.spinner_night_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_night_item);
        spinner.setAdapter(adapter);

    }

    public void onActive() {
        TextView clockTime = (TextView) findViewById(R.id.clockTime);
        TextView data_view = (TextView) findViewById(R.id.results);

        TextView serial = (TextView) findViewById(R.id.serial_num);
        Button database_button = (Button) findViewById(R.id.database_button);
        Button clock_button = (Button) findViewById(R.id.clock_button);
        ImageView image_clock = (ImageView) findViewById(R.id.clock_image);
        Button search = (Button) findViewById(R.id.search);
        ImageButton info = (ImageButton) findViewById(R.id.info_button);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        data_view.setText("Serial# textbox:\nEnter the serial number of a given pocketwatch movement to find " +
                "out more information. \nIf the given input does not register, please check the movement again to make sure that the " +
                "serial number is correct. \nAdditionally, verify that the correct manufacturer is selected. If it still does not register, please " +
                "cross reference your movement with the website provided below." +
                "\n\nManufacturer select:\nDefault is \"Ball\". Please click the word to select other manufacturers." +
                "\n\nSun/Moon button:\nChanges screen between day & night mode." +
                "\n\nGo button: Searches the database for the given serial number & manufacturer." +
                "\n\nClock button:\nBrings up the clock screen." +
                "\n\nDatabase button:\nBrings up the database screen." +
                "\n\nAll information displayed comes from: \nhttps://pocketwatchdatabase.com/.\nThe founders and " +
                "upkeepers of this web database have generously given their consent for the data used to be implemented " +
                "into this application. \n\n" +
                "I, Chris Cardimen, do not posses, nor do I attempt to display, any of this data as my own. " +
                "\n\nMy contact information is as follows - I would be more than happy to address any questions and/or inquiries." +
                "\n\nEmail: christophercardimen@gmail.com" +
                "\nPhone: (586)-337-2305" +
                "\n\nThank you so much for using this app!\nHave a fantastic day :)");
        data_view.setVisibility(View.VISIBLE);
        clockTime.setVisibility(View.INVISIBLE);
        database_button.setVisibility(View.INVISIBLE);
        clock_button.setVisibility(View.INVISIBLE);
        image_clock.setVisibility(View.INVISIBLE);
        search.setVisibility(View.INVISIBLE);
        serial.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
    }

    public void onInactive() {
        TextView clockTime = (TextView) findViewById(R.id.clockTime);
        TextView data_view = (TextView) findViewById(R.id.results);

        TextView serial = (TextView) findViewById(R.id.serial_num);
        Button database_button = (Button) findViewById(R.id.database_button);
        Button clock_button = (Button) findViewById(R.id.clock_button);
        ImageView image_clock = (ImageView) findViewById(R.id.clock_image);
        Button search = (Button) findViewById(R.id.search);
        ImageButton info = (ImageButton) findViewById(R.id.info_button);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        data_view.setText("");

        data_view.setVisibility(data_vis);
        clockTime.setVisibility(time_vis);
        database_button.setVisibility(database_vis);
        clock_button.setVisibility(clock_vis);
        image_clock.setVisibility(image_clock_vis);
        search.setVisibility(search_vis);
        serial.setVisibility(serial_vis);
        info.setVisibility(info_vis);
        spinner.setVisibility(spinner_vis);
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

           document = Jsoup.connect("https://pocketwatchdatabase.com/search/result/" + MainActivity.brand_to_search.toLowerCase() + "/"
                   + MainActivity.serial_num_to_search + "/").get();
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



