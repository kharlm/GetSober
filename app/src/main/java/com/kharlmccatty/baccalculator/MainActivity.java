package com.kharlmccatty.baccalculator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.widget.Toast;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;


public class MainActivity extends AppCompatActivity {

    private AdView mAdView;
    //used for register alarm manager
    PendingIntent pendingIntent;
    //used to store running alarmmanager instance
    AlarmManager alarmManager;
    //Callback function for Alarmmanager event
    BroadcastReceiver mReceiver;
    public boolean openDialog = true;

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    int count = 0;
    boolean male = true;
    boolean female = true;
    double bac;
    String level;
    int beer1;
    int wine1;
    int shot1;
    int other1;
    int weight1;
    int hour1;

    View.OnClickListener listener;
    View.OnClickListener listener1;
    View.OnClickListener listener2;
    View.OnClickListener listener3;

    public TextView mbac;
    public TextView status;
    public TextView timer;
    Button mbacbutton;
    Button uberbutton;
    Button alarmbutton;
    Button malarm;
    EditText mbeer;
    EditText mwine;
    EditText mshot;
    EditText mother;
    EditText mweight;
    EditText mhours;
    CountDownTimer mCountDownTimer;

    public static String PACKAGE_NAME;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RegisterAlarmBroadcast();

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefs.edit();
        boolean isUsedBefore = mPrefs.getBoolean(level, false);

        if (isUsedBefore == true) {
            //do nothing
        } else {
            launchDialog();
        }



        MobileAds.initialize(getApplicationContext(), " ca-app-pub-0591815898899876/3186840320");


// Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.


        PACKAGE_NAME = getApplicationContext().getPackageName();



        mAdView = (AdView) findViewById(R.id.adView);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("YOUR_DEVICE_HASH")
                .build();
        mAdView.loadAd(adRequest);

        //return true;

        mbeer = (EditText) findViewById(R.id.beer);
        mwine = (EditText) findViewById(R.id.wine);
        mshot = (EditText) findViewById(R.id.shot);
        mother = (EditText) findViewById(R.id.other);
        mweight = (EditText) findViewById(R.id.weight);
        mbacbutton = (Button) findViewById(R.id.button);
        uberbutton = (Button) findViewById(R.id.uber);
        alarmbutton = (Button) findViewById(R.id.alarm);
        //malarm= (Button)findViewById(R.id.alarm);
        mbac = (TextView) findViewById(R.id.bac);
        mhours = (EditText) findViewById(R.id.hours);
        status = (TextView) findViewById(R.id.status);
        timer = (TextView) findViewById(R.id.timer);







        listener = new View.OnClickListener() { //creates a new OnClickListener called listener {
            @Override
            public void onClick(View v) {


                // mbac.setText(String.valueOf(getBac()));


                String beer = mbeer.getText().toString();

                if (beer.isEmpty() || beer.length() == 0 || beer.equals("") || beer == null) {
                    beer = "0";
                }

                beer1 = new Integer(beer).intValue();


                String wine = mwine.getText().toString();
                if (wine.isEmpty() || wine.length() == 0 || wine.equals("") || wine == null) {
                    wine = "0";
                }
                wine1 = new Integer(wine).intValue();

                String shot = mshot.getText().toString();
                if (shot.isEmpty() || shot.length() == 0 || shot.equals("") || shot == null) {
                    shot = "0";
                }
                shot1 = new Integer(shot).intValue();

                String other = mother.getText().toString();
                if (other.isEmpty() || other.length() == 0 || other.equals("") || other == null) {
                    other = "0";
                }
                other1 = new Integer(other).intValue();

                String weight = mweight.getText().toString();
                if (weight.isEmpty() || weight.length() == 0 || weight.equals("") || weight == null) {
                    weight = "0";
                }
                weight1 = new Integer(weight).intValue();

                String hours = mhours.getText().toString();
                if (hours.isEmpty() || hours.length() == 0 || hours.equals("") || hours == null) {
                    hours = "1";
                }
                hour1 = new Integer(hours).intValue();
                String gender = Double.toString(getGender(male, female));

                getBac();

                mbac.setText(bac());


                if (bac <= .02) {

                    status.setText("Sober");
                }

                if (bac >= .02 && bac <= .039) {
                    status.setText("Legally Sober.Mildly Relaxed and a little light headed");
                }
                if (bac >= .04 && bac <= .079) {
                    status.setText("Legally Sober.Judgement is slightly impaired and lower inhibitions");
                }

                if (bac >= .08 && bac <= .099) {
                    status.setText("Legally Impaired.");
                }

                if (bac >= .10 && bac <= .129) {
                    status.setText("Legally Impaired.Slurred speech,balance anc vision impaired");
                }

                if (bac >= .13 && bac <= 0.159) {
                    status.setText("Legally Impaired.Loss of balance,blurred vision");


                }


                if (bac >= .16 && bac <= .199) {
                    status.setText("Legally Impaired.May pass out,Nausea");
                }
                if (bac >= .20 && bac <= .259) {
                    status.setText("Legally Impaired.Needs help standing,Nausea and vomitting");
                }
                if (bac >= .26)

                {
                    status.setText("Legally Impaired.Extremely life threatening needs medical attention");
                }


                if (mCountDownTimer != null) mCountDownTimer.cancel();

                mCountDownTimer = new CountDownTimer(getSober(), 1000) {// adjust the milli seconds here

                    public void onTick(long millisUntilFinished) {


                        long hr1 = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
                        long sub1 = hr1 * 60;
                        long min1 = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                        long sub2 = min1 * 60;


                        timer.setText("" + String.format("%d hr,%d min, %d sec",
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),

                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - sub1,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - sub2,
                                -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                    }


                    public void onFinish() {

                        timer.setText("0");
                    }


                };

                mCountDownTimer.start();
                //mcountDownTimer.cancel();

/*

                mbeer.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        getBac();
                        mbac.setText(bac());
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        getBac();
                        mbac.setText(bac());
                        //Field2.getText().clear();
                    }
                });

                mwine.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        getBac();
                        mbac.setText(bac());

                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        getBac();
                        mbac.setText(bac());
                        //Field2.getText().clear();
                    }
                });
                mshot.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        getBac();
                        mbac.setText(bac());

                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        getBac();
                        mbac.setText(bac());
                        //Field2.getText().clear();
                    }
                });
                mother.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        getBac();
                        mbac.setText(bac());

                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        getBac();

                        mbac.setText(bac());
                        //Field2.getText().clear();
                    }
                });
                mweight.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        getBac();
                        mbac.setText(bac());

                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        getBac();

                        mbac.setText(bac());
                        //Field2.getText().clear();
                    }
                });
                mhours.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        getBac();
                        mbac.setText(bac());

                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        getBac();

                        mbac.setText(bac());
                        //Field2.getText().clear();
                    }
                });
*/


            }


        };
        mbacbutton.setOnClickListener(listener);

        listener3 = new View.OnClickListener() { //creates a new OnClickListener called listener {
            @Override
            public void onClick(View v) {
                checkStatus();


                long sober = (long) (getSober());
                long hr1 = TimeUnit.MILLISECONDS.toHours(getSober());
                long sub1 = hr1 * 60;
                long min1 = TimeUnit.MILLISECONDS.toMinutes(getSober());
                long sub2 = min1 * 60;

                long convert = (min1 - sub1);
                int n = (int) hr1;
                int l = (int) convert;

                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.add(Calendar.HOUR, n);
                calendar.add(Calendar.MINUTE, l);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);


                //int work1=n+hour;
                //int work2=minute+l;
                if (getBac() >= .08) {
                    Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                    i.putExtra(AlarmClock.EXTRA_MESSAGE, "Time til Sober");
                    i.putExtra(AlarmClock.EXTRA_HOUR, hour);
                    i.putExtra(AlarmClock.EXTRA_MINUTES, minute);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "You don't need to set an Alarm you are below the legal limit", Toast.LENGTH_LONG).show();
                }
//onClickSetAlarm(null);
                //RegisterAlarmBroadcast();


            }
        };

        alarmbutton.setOnClickListener(listener3);
        listener1 = new View.OnClickListener() { //creates a new OnClickListener called listener {
            @Override
            public void onClick(View v) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.ubercab");
                if(launchIntent==null){

                    Toast.makeText(getApplicationContext(), "You don't have Uber installed on your phone", Toast.LENGTH_LONG).show();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ubercab&hl=en"));
                    startActivity(browserIntent);
                }
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }

            }
        };

        uberbutton.setOnClickListener(listener1);
        listener2 = new View.OnClickListener() { //creates a new OnClickListener called listener {
            @Override
            public void onClick(View v) {

            }

        };


    }

    public void checkStatus() {
        if (bac == .129) {
            status.setText("works");
        }
    }


    public void onRadioButtonClicked(View view) {
        RadioButton rb1 = (RadioButton) findViewById(R.id.female);
        //rb1.setOnCheckedChangeListener(rb1,true);
        RadioButton rb2 = (RadioButton) findViewById(R.id.male);
        //rb2.setOnCheckedChangeListener(this);

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.male:
                if (checked)
                    //set inch button to unchecked
                    this.male = true;
                this.female = false;
                rb1.setChecked(false);
                getBac();

                mbac.setText(bac());

                break;
            case R.id.female:
                if (checked)
                    //set MM button to unchecked
                    this.female = true;
                this.male = false;


                rb2.setChecked(false);
                getBac();

                mbac.setText(bac());


                break;
            // Is the button now checked?
            /*boolean checked = ((RadioButton) view).isChecked();

            // Check which radio button was clicked
            switch(view.getId()) {
                case R.id.male:
                    if (checked)
                        this.male=true;
                        break;
                case R.id.female:
                    if (checked)
                        this.female=true;
                        break;
            }
            */

        }
    }


    //@Override


    public double getGender(boolean male, boolean female) {
        if (this.male == true) {
            return .68;
        }
        if (this.female == true) {
            return .55;
        } else {
            return 0;
        }

    }

    public double getBeer() {
        return ((double) beer1) * 340.194 * .05;
    }

    public double getWine() {
        return ((double) wine1) * 141.748 * .12;
    }

    public double getShot() {
        return ((double) shot1) * 42.5243 * .4;
    }

    public double getOther() {
        return ((double) other1) * 14.0;
    }


    public double getWeight() {

        double weigh = ((double) weight1) * 453.592;
        return weigh;
    }

    public double getHours() {
        return ((double) hour1);


    }

    public double getBac() {


        double numerator = .789 * (getBeer() + getWine() + getShot() + getOther());
        double denominator = getWeight() * getGender(this.male, this.female);

        double bc = (numerator / denominator) * 100;
        bac = bc - (getHours() * .015);

        if(Double.isNaN(bac)){
            bac=0.000;
        }

        if(Double.isInfinite(bac)){
            bac=100;
        }


        Double toBeTruncated = new Double(bac);

        Double truncatedDouble = BigDecimal.valueOf(toBeTruncated)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
        bac = truncatedDouble;
        return bac;


    }


    public String bac() {
        String bac = Double.toString(getBac());
        return bac;
    }

    public int getSober() {
        double k = (getBac() - .08) / .015;
        double n = k * 3600000;
        return ((int) n);

    }


    //Register AlarmManager Broadcast receive.


    public void onClickSetAlarm(View v) {
        //Get the current time and set alarm after 10 seconds from current time
        // so here we get
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
    }

    private void RegisterAlarmBroadcast() {
        //Log.i("RegisterAlarmBroadcast()", "km");

        //This is the call back function(BroadcastReceiver) which will be call when your
        //alarm time will reached.
        mReceiver = new BroadcastReceiver() {
            private static final String TAG = "Alarm Example Receiver";

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "BroadcastReceiver::OnReceive() >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                Toast.makeText(context, "Congrats!. Your Alarm time has been reached", Toast.LENGTH_LONG).show();
            }
        };

        // register the alarm broadcast here
        registerReceiver(mReceiver, new IntentFilter("com.techblogon.alarmexample"));
        pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.techblogon.alarmexample"), 0);
        alarmManager = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
    }

    private void UnregisterAlarmBroadcast() {
        alarmManager.cancel(pendingIntent);
        getBaseContext().unregisterReceiver(mReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void launchDialog() {
        mEditor.putBoolean(level, true);
        mEditor.commit();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("Warning");

        // set dialog message
        alertDialogBuilder
                .setMessage("This is app only provides estimates and does not give the 100% accurate result of your current Blood Alcohol Level. Click Ok to continue")
                .setCancelable(false)
                /*.setPositiveButton(, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //don't open Dialog by next launch
                        openDialog = false;

                        // if this button is clicked, close
                        // current activity
                        MainActivity.this.finish();
                    }
                })
                */
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }




};
