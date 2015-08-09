package com.example.ron.vkclient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginHttp extends AppCompatActivity {

    private static final int APP_ID = 5007572;
    private static final String URL_FOR_PARAMETERS = "https://oauth.vk.com/authorize?client_id=5007572&" +
            "scope=friend&" +
            "redirect_uri=http://oauth.vk.com/blank.html&" +
            "display=wap&" +
            "response_type=token&" +
            "v=5.35";
    private String URL_PERMISSON_PAGE;

    private WebView webView;
    private EditText eLogin, ePassword;
    private Button btnLogin;
    private TextView txtV;
    private String login, password;
//    private SearchToParameters searchToParameters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_act);

        webView = (WebView)findViewById(R.id.webView);
        eLogin = (EditText)findViewById(R.id.editLogin);
        ePassword = (EditText)findViewById(R.id.editPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        txtV = (TextView) findViewById(R.id.textView);

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = eLogin.getText().toString();
                password = ePassword.getText().toString();
                new SearchToParameters().execute(URL_FOR_PARAMETERS);

                webView.loadUrl(URL_PERMISSON_PAGE);
                webView.setVisibility(View.VISIBLE);
            }
        });

    }

    public class SearchToParameters extends AsyncTask<String, Void, String> {
        private String regularLg_h;
        private String regularIp_h;
        private String regular_origin;
        private String regular_to;
        public String site;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();

            try {
                URL url = new URL(params[0]);
                String inputLine;
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader buff = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((inputLine = buff.readLine()) != null) {
                    sb.append(inputLine);
                }

                buff.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            site = sb.toString();
            return site;
        }

        @Override
        protected void onPostExecute(String s) {
            parseParameters(s);
        }

        public void parseParameters(String site) {
            Pattern pat_lg_h = Pattern.compile("name=\"lg_h\" value=\"(.+?)\"");
            Matcher mat_lg_h = pat_lg_h.matcher(site);
            while (mat_lg_h.find()) {
                regularLg_h = mat_lg_h.group(1);
            }

            Pattern pat_ip_h = Pattern.compile("name=\"ip_h\" value=\"(.+?)\"");
            Matcher mat_ip_h = pat_ip_h.matcher(site);
            while (mat_ip_h.find()) {
                regularIp_h = mat_ip_h.group(1);
            }

            Pattern pat_to = Pattern.compile("name=\"to\" value=\"(.+?)\"");
            Matcher mat_to = pat_to.matcher(site);
            while (mat_to.find()) {
                regular_to = mat_to.group(1);
            }

            Pattern pat_origin = Pattern.compile("name=\"_origin\" value=\"(.+?)\"");
            Matcher mat_origin = pat_origin.matcher(site);
            while (mat_origin.find()) {
                regular_origin = mat_origin.group(1);
            }
            URL_PERMISSON_PAGE = "https://login.vk.com/?act=login&soft=1&utf8=1" +
                    "&ip_h=" + regularIp_h +
                    "&lg_h=" + regularLg_h +
                    "&to=" + regular_to  +
                    "&_origin=" + regular_origin +
                    "&email=" + login +
                    "&pass=" + password;
        }
    }

}
