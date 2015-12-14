package hu.android.ait.americanabroad;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;
import hu.android.ait.americanabroad.converters.ConvertDistance;
import hu.android.ait.americanabroad.converters.ConvertLength;
import hu.android.ait.americanabroad.converters.ConvertMoney;
import hu.android.ait.americanabroad.converters.ConvertTemperature;
import hu.android.ait.americanabroad.converters.CurrencyHelper;
import hu.android.ait.americanabroad.data.MoneyResult;
import hu.android.ait.americanabroad.network.HttpAsyncTask;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public final static String HOME = "US";
    public final static String HOME_CURRENCY = "USD";

    private DrawerLayout drawerLayout;

    private TextView tvXInfo;
    private TextView tvUSInfo;
    private EditText etUS;
    private EditText etX;

    String lastUS;
    String lastX;

    String lastCountryCode;

    private View draggedView = null;
    private Button btnConvert;
    private MyTouchListener myTouchListener = new MyTouchListener();


    CurrencyHelper currencyHelper;
    LocationManager locationManager;
    MoneyResult moneyResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        View convertTemp = findViewById(R.id.ivTemp);
        View convertMoney = findViewById(R.id.ivMoney);
        final View convertDistance = findViewById(R.id.ivDistance);
        View convertLength = findViewById(R.id.ivLength);

        convertTemp.setOnTouchListener(myTouchListener);
        convertMoney.setOnTouchListener(myTouchListener);
        convertDistance.setOnTouchListener(myTouchListener);
        convertLength.setOnTouchListener(myTouchListener);

        btnConvert = (Button) findViewById(R.id.btnGetRates);
        btnConvert.setOnDragListener(new MyDragListener());

        tvXInfo = (TextView) findViewById(R.id.tvXInfo);
        tvUSInfo = (TextView) findViewById(R.id.tvUSInfo);

        etUS = (EditText) findViewById(R.id.etUS);
        etUS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(etUS.getText().equals(""))) {
                    tvUSInfo.setText(etUS.getHint());
                } else {
                    tvUSInfo.setText("");
                }
            }
        });

        etX = (EditText) findViewById(R.id.etX);
        etX.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!(etX.getText().equals(""))) {
                    tvXInfo.setText(etX.getHint());
                    if (etX.getHint() == null){
                        tvXInfo.setText(getString(R.string.msgNoInternet));
                    }
                } else {
                    tvXInfo.setText("");
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!(etX.getText().equals(""))) {
                    tvXInfo.setText(etX.getHint());
                } else {
                    tvXInfo.setText("");
                }
            }
        });

        lastX = "";
        lastUS = "";

        new HttpAsyncTask(getApplicationContext()).execute(
                getString(R.string.url) + HOME_CURRENCY
        );
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        startLocationMonitoring();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.action_dist:
                                btnConvert.setText(getString(R.string.distance));
                                etUS.setHint(R.string.txt_miles);
                                etX.setHint(R.string.txt_km);
                                resetFields();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_temp:
                                btnConvert.setText(getString(R.string.temp));
                                etUS.setHint(R.string.txt_fahr);
                                etX.setHint(R.string.txt_cel);
                                resetFields();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_length:
                                btnConvert.setText(getString(R.string.length));
                                etUS.setHint(R.string.txt_in);
                                etX.setHint(R.string.txt_cm);
                                resetFields();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_money:
                                btnConvert.setText(getString(R.string.money));
                                etUS.setHint(HOME_CURRENCY);
                                etX.setHint(lastCountryCode);
                                resetFields();

                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_about:
                                MessageFragment dialog = new MessageFragment().newInstance(getString(R.string.txt_about), getString(R.string.msg_about));
                                dialog.show(getSupportFragmentManager(), MessageFragment.TAG);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                            case R.id.action_help:
                                MessageFragment dialog2 = new MessageFragment().newInstance(getString(R.string.txt_help), getString(R.string.msg_help));
                                dialog2.show(getSupportFragmentManager(), MessageFragment.TAG);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                break;
                        }
                        return false;
                    }
                }

        );


        btnConvert.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              convert();
                                          }
                                      }

        );
    }


    public void convert() {
        switch (btnConvert.getText().toString()) {
            case "Distance":
                ConvertDistance converter = new ConvertDistance();
                if (!(lastUS.equals(etUS.getText().toString())) && (!etUS.getText().toString().equals(""))) {
                    etX.setText(converter.usToX(etUS.getText().toString()));
                    etUS.setText(converter.formatUS(etUS.getText().toString()));
                    lastUS = etUS.getText().toString();
                } else if (!(lastX.equals(etX.getText().toString())) && (!etX.getText().toString().equals(""))) {
                    etUS.setText(converter.xToUS(etX.getText().toString()));
                    etX.setText(converter.formatX(etX.getText().toString()));
                    lastX = etX.getText().toString();
                }
                break;
            case "Money":
                startLocationMonitoring();
                new HttpAsyncTask(getApplicationContext()).execute(
                        getString(R.string.url) + HOME_CURRENCY
                );
                if (lastCountryCode != null) {
                    if (currencyHelper != null) {
                        tvXInfo.setText("");
                        ConvertMoney convertMoney = new ConvertMoney(currencyHelper);
                        if (!(lastUS.equals(etUS.getText().toString())) && (!etUS.getText().toString().equals(""))) {
                            etX.setText(convertMoney.usToX(etUS.getText().toString()));
                            etUS.setText(convertMoney.formatUS(etUS.getText().toString()));
                            lastUS = etUS.getText().toString();
                            lastX = etX.getText().toString();
                        } else if (!(lastX.equals(etX.getText().toString())) && (!etX.getText().toString().equals(""))) {
                            etUS.setText(convertMoney.xToUS(etX.getText().toString()));
                            etX.setText(convertMoney.formatX(etX.getText().toString()));
                            lastX = etX.getText().toString();
                            lastUS = etUS.getText().toString();
                        } else {
                            tvXInfo.setText(R.string.msgNoInternet);
                        }
                    }
                } else {
                    tvXInfo.setText(R.string.msgNoInternet);
                }
                stopLocationMonitoring();
                break;
            case "Temperature":
                ConvertTemperature convertTemperature = new ConvertTemperature();
                if (!(lastUS.equals(etUS.getText().toString())) && (!etUS.getText().toString().equals(""))) {
                    etX.setText(convertTemperature.usToX(etUS.getText().toString()));
                    etUS.setText(convertTemperature.formatUS(etUS.getText().toString()));
                    lastUS = etUS.getText().toString();
                    lastX = etX.getText().toString();
                } else if (!(lastX.equals(etX.getText().toString())) && (!etX.getText().toString().equals(""))) {
                    etUS.setText(convertTemperature.xToUS(etX.getText().toString()));
                    etX.setText(convertTemperature.formatX(etX.getText().toString()));
                    lastX = etX.getText().toString();
                    lastUS = etUS.getText().toString();
                }
                break;
            case "Length":
                ConvertLength convertLength = new ConvertLength();
                if (!(lastUS.equals(etUS.getText().toString())) && (!etUS.getText().toString().equals(""))) {
                    etX.setText(convertLength.usToX(etUS.getText().toString()));
                    etUS.setText(convertLength.format(etUS.getText().toString()));
                    lastX = etX.getText().toString();
                    lastUS = etUS.getText().toString();
                } else if (!(lastX.equals(etX.getText().toString())) && (!etX.getText().toString().equals(""))) {
                    etUS.setText(convertLength.xToUS(etX.getText().toString()));
                    etX.setText(convertLength.format(etX.getText().toString()));
                    lastX = etX.getText().toString();
                    lastUS = etUS.getText().toString();
                }
                break;
        }
    }

    private void resetFields() {
        etUS.setText("");
        etX.setText("");
        lastUS = "";
        lastX = "";
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        String countryCode = getCountryCode(lat, lng);
        if (countryCode != lastCountryCode) {
            if (!(moneyResult == null)) {
                currencyHelper = new CurrencyHelper(new Locale(getString(R.string.english_lng_code), countryCode));
//                Log.d("LOGLOG", "here: " + currencyHelper.getCurrency().getCurrencyCode());
                lastCountryCode = currencyHelper.getCurrency().getCurrencyCode();
                currencyHelper.setRATE(moneyResult.getRates().getCurrencyRate(lastCountryCode));
                stopLocationMonitoring();
            }
        }

    }

    public void onEventMainThread(MoneyResult moneyResult) {
        this.moneyResult = moneyResult;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }


    private String getCountryCode(double lat, double lng) {
        String result;
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList =
                    geocoder.getFromLocation(lat, lng, 1);
            result = addressList.get(0).getCountryCode();
        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage();
        }
        return result;

    }


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationMonitoring();
    }

    private void startLocationMonitoring() {
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void stopLocationMonitoring() {
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder =
                        new View.DragShadowBuilder(
                                view);
                view.startDrag(data, shadowBuilder, view, 0);
                draggedView = view;

                return true;
            }
            return false;
        }
    }

    private final class MyDragListener implements View.OnDragListener {
        String temporaryText = btnConvert.getText().toString();

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    if (draggedView != null) {
                        switch (draggedView.getId()) {
                            case R.id.ivDistance:
                                btnConvert.setText(R.string.distance);
                                break;
                            case R.id.ivMoney:
                                btnConvert.setText(R.string.money);
                                break;
                            case R.id.ivTemp:
                                btnConvert.setText(R.string.temp);
                                break;
                            case R.id.ivLength:
                                btnConvert.setText(R.string.length);
                                break;
                        }
                        break;
                    }
                case DragEvent.ACTION_DRAG_EXITED:
                    btnConvert.setText(temporaryText);
                    break;
                case DragEvent.ACTION_DROP:
                    if (draggedView != null) {
                        switch (draggedView.getId()) {
                            case R.id.ivDistance:
                                btnConvert.setText("Distance");
                                etUS.setHint(R.string.txt_miles);
                                etX.setHint(R.string.txt_km);
                                resetFields();
                                break;
                            case R.id.ivMoney:
                                btnConvert.setText("Money");
                                etUS.setHint(HOME_CURRENCY);
                                etX.setHint(lastCountryCode);
                                resetFields();
                                break;
                            case R.id.ivTemp:
                                btnConvert.setText("Temperature");
                                etUS.setHint(R.string.txt_fahr);
                                etX.setHint(R.string.txt_cel);
                                resetFields();
                                break;
                            case R.id.ivLength:
                                btnConvert.setText("Length");
                                etUS.setHint(R.string.txt_in);
                                etX.setHint(R.string.txt_cm);
                                resetFields();
                                break;
                        }
                        temporaryText = btnConvert.getText().toString();
                    }
                    break;
            }
            return true;
        }
    }


}
