package com.example.ron.vkclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.vk.sdk.VKSdk;


public class LoginActivity extends ActionBarActivity {
    private static final int APP_ID = 5007572;
    private static final String SETTINGS = "friends,photos,status,wall,messages,offline";
    private static final String REDIRECT_URI = "http://oauth.vk.com/blank.html";
    private static final String DISPLAY = "touch";

    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_oauth);

        VKSdk.initialize(getApplicationContext());

        wv = (WebView)findViewById(R.id.webView);

        wv.setWebViewClient(new VkWebViewClient());

        String url = "https://oauth.vk.com/authorize?" +
                "client_id=" + APP_ID + "&" +
                "scope=" + SETTINGS + "&" +
                "redirect_uri=" + REDIRECT_URI + "&" +
                "display=" + DISPLAY + "&" +
                "response_type=token";
        wv.loadUrl(url);
        wv.setVisibility(View.VISIBLE);
    }

    class VkWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            parseUrl(url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Toast.makeText(getApplicationContext(), wv.getUrl() + " donwloaded", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseUrl(String url) {
        try {
            if (url == null) return;
            if (url.startsWith(REDIRECT_URI)) {
                if (!url.contains("error")) {
                    String[] auth = VKUtil.parseRedirectUrl(url);
                    wv.setVisibility(View.GONE);

                    //Build data
                    Intent intent = new Intent();
                    intent.putExtra("token", auth[0]);
                    intent.putExtra("uid", auth[1]);

                    //Return data
                    setResult(Activity.RESULT_OK, intent);
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            } else if (url.contains("error?err")) {
                setResult(RESULT_CANCELED);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            setResult(RESULT_CANCELED);
            finish();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
