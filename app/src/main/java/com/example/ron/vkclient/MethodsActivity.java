package com.example.ron.vkclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;

/**
 * Created by Ron on 02.08.2015.
 */
public class MethodsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_methods);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class PlaceholderFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_methods, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            view.findViewById(R.id.users_get).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,
                            "id,first_name,last_name,sex,bdate,city,country,photo_50,photo_100," +
                                    "photo_200_orig,photo_200,photo_400_orig,photo_max,photo_max_orig,online," +
                                    "online_mobile,lists,domain,has_mobile,contacts,connections,site,education," +
                                    "universities,schools,can_post,can_see_all_posts,can_see_audio,can_write_private_message," +
                                    "status,last_seen,common_count,relation,relatives,counters"));
                    request.secure = false;
                    request.useSystemLanguage = false;
                    startApiCall(request);
                }
            });

            view.findViewById(R.id.friends_get).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,sex,bdate,city"));
                    startApiCall(request);
                }
            });
        }

        private void startApiCall(VKRequest request) {
            Intent i = new Intent(getActivity(), ApiCallActivity.class);
            i.putExtra("request", request.registerObject());
            startActivity(i);
        }
    }


}
