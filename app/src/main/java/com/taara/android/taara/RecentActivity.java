package com.taara.android.taara;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.taara.android.taara.fragments.RecentsMasterFragment;
import com.taara.android.taara.recents.RecentProductOccurrences;

public class RecentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EditProfileDialog.onProfileChangedListener, RecentsMasterFragment.OnListFragmentInteractionListener {
    FirebaseAuth mAuth;
    int mBackPressedCounter;
    public static String USER_ID;
    TextView txtfullname, txtEmail;
    String mFullName, mEmail;
    SharedPreferences sharedPreferences;
    NavigationView navigationView;
    FloatingActionButton floatingActionButton;
    public static boolean IS_CONNECTED;
    WebView offersWebView;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(null == networkInfo)) {
            IS_CONNECTED = true;

        } else {
            IS_CONNECTED = false;
        }
        setContentView(R.layout.activity_recent_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.your_actvity));


        navigationView = findViewById(R.id.nav_view);
        sharedPreferences = getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);

        offersWebView = findViewById(R.id.offersWebView);
        txtfullname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name_full);
        txtEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email_account_info);
        floatingActionButton = navigationView.getHeaderView(0).findViewById(R.id.editProfile);
        USER_ID = sharedPreferences.getString("USER_ID", "user id");
        mAuth = FirebaseAuth.getInstance();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileDialog editProfileDialog = new EditProfileDialog();
                editProfileDialog.show(getSupportFragmentManager(), "EDIT_PROFILE");
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE);
        Toast.makeText(getApplicationContext(), sharedPreferences.getString("FIRST_NAME", "default") + " logged in", Toast.LENGTH_LONG).show();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        WebSettings webSettings = offersWebView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        offersWebView.setWebViewClient(new WebViewClient());
        offersWebView.setWebChromeClient(new WebChromeClient());
        String url = getResources().getString(R.string.host) + "/taaraBackend/offers.html";
        offersWebView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            mBackPressedCounter += 1;
            if (mBackPressedCounter == 2) {
                finishAffinity();
            }
            Toast.makeText(getApplicationContext(), "PRESS 'BACK' AGAIN TO EXIT", Toast.LENGTH_SHORT).show();

            Thread resetCounter = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(2000);
                        mBackPressedCounter = 0;
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };


            resetCounter.start();


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account_information, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            Intent intent = new Intent(getApplicationContext(), HelpAndFeedback.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_exit) {
            finishAffinity();
            return true;
        }

        if (id == R.id.startShoppingMenu) {
            CheckInDialogFragment checkInDialogFragment = new CheckInDialogFragment();
            checkInDialogFragment.show(getSupportFragmentManager(), "CHECK_IM");
        }

        if (id == R.id.action_logout) {
            mAuth.signOut();
            LogIn.needToLogin = true;
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feedback) {
            Intent intent = new Intent(getApplicationContext(), HelpAndFeedback.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(getApplicationContext(), About.class);
            startActivity(intent);

        } else if (id == R.id.nav_notifications) {
            Intent intent = new Intent(getApplicationContext(), Notifications.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            LogIn.needToLogin = true;
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            String playStoreUrl = "https://play.google.com/taara";
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, playStoreUrl);
            String title = getResources().getString(R.string.share_app);
            Intent chooser = Intent.createChooser(sendIntent, title);

            if (sendIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            } else {
                Snackbar.make(navigationView, "Sorry.It seems you dont have a suitable sharing app", Snackbar.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_exit) {
            finishAffinity();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume() {
        mFullName = sharedPreferences.getString("FIRST_NAME", null) + " " + sharedPreferences.getString("SECOND_NAME", null);
        mEmail = sharedPreferences.getString("EMAIL", null);
        Log.i("profile", mFullName);
        txtfullname.setText(mFullName);
        txtEmail.setText(mEmail);
        super.onResume();
    }

    @Override
    public void updateNavigationDrawerHeader(SharedPreferences sharedPreferences) {
        mFullName = sharedPreferences.getString("FIRST_NAME", null) + " " + sharedPreferences.getString("SECOND_NAME", null);
        mEmail = sharedPreferences.getString("EMAIL", null);
        Log.i("profile", mFullName);
        txtfullname.setText(mFullName);
        txtEmail.setText(mEmail);
    }

    @Override
    public void onListFragmentInteraction(RecentProductOccurrences.ProductOccurrence item) {

    }
}
