package com.media_mosaic.httpwww.doubloons.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.Request;
import com.media_mosaic.httpwww.doubloons.Activitys.MapsActivity;
import com.media_mosaic.httpwww.doubloons.Adapters.Featured_adapter;
import com.media_mosaic.httpwww.doubloons.DB.ReadPref;
import com.media_mosaic.httpwww.doubloons.Data_Model.featured_model;
import com.media_mosaic.httpwww.doubloons.R;
import com.media_mosaic.httpwww.doubloons.interfaces.DataAppResponse;
import com.media_mosaic.httpwww.doubloons.network.DataAPICALL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class Featured_doubloonFragment extends Fragment {
    private DataAPICALL groceryAPICALL;
    private DataAppResponse groceryAppResponse;
    List<featured_model> itemList;
    private RecyclerView featured_doubloon_recycler_view;
    private ProgressDialog pDialog;
    Featured_adapter featured_adapter;
    ReadPref readPref;
    SwipeRefreshLayout featured_doubloon_swifeRefresh;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readPref = new ReadPref(getActivity());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured_doubloon, container, false);
        featured_doubloon_recycler_view=(RecyclerView)view.findViewById(R.id.featured_doubloon_recycler_view);
        featured_doubloon_swifeRefresh=(SwipeRefreshLayout)view.findViewById(R.id.featured_doubloon_swifeRefresh);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        GetResponse();
        HashMap<String,String> params = new HashMap<>();
        params.put("lat", String.valueOf(MapsActivity.latitude));
        params.put("lng",String.valueOf(MapsActivity.longitude));
        params.put("category","featured");
        params.put("user_id",readPref.getuserId());
        Log.d("TAG@123",params.toString());
        groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
        groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/categoryDoubloons",params,"item");
        featured_doubloon_swifeRefresh.setColorSchemeResources(R.color.colorAccent,
                R.color.colorPrimary,
                R.color.white);
        featured_doubloon_swifeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();
                GetResponse();
                HashMap<String,String> params = new HashMap<>();
                params.put("lat", String.valueOf(MapsActivity.latitude));
                params.put("lng",String.valueOf(MapsActivity.longitude));
                params.put("category","featured");
                params.put("user_id",readPref.getuserId());
                groceryAPICALL= new DataAPICALL(getActivity(),groceryAppResponse);
                groceryAPICALL.sendData(Request.Method.POST, "http://www.doubloon.media-mosaic.in/apis/categoryDoubloons",params,"item");
                featured_doubloon_swifeRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    private void GetResponse()
    {

        groceryAppResponse= new DataAppResponse() {
            @Override
            public void onResponse(String response, String responseType) {
                Log.d("TAG@123",response);
                String dhjdj=response.toString();
                pDialog.dismiss();
                itemList= new ArrayList<>();
                try {
                    JSONObject jsonObjectdata=new JSONObject(response);
                    JSONArray jsonArray= jsonObjectdata.getJSONArray("Doubloon_app");

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        featured_model featured_model=new featured_model();
                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                        featured_model.setId(jsonObject.getString("id"));
                        featured_model.setName(jsonObject.getString("name"));
                        featured_model.setAddress(jsonObject.getString("Address"));
                        featured_model.setDiscount(jsonObject.getString("discount"));
                        featured_model.setGroup_type(jsonObject.getString("group_type"));
                        featured_model.setLat(jsonObject.getString("lat"));
                        featured_model.setLongi(jsonObject.getString("long"));
                        featured_model.setPost_type(jsonObject.getString("post_type"));
                        featured_model.setRunningCount(jsonObject.getString("runningCount"));
                        featured_model.setSuccessCount(jsonObject.getString("successCount"));
                        featured_model.setTime_limit(jsonObject.getString("time_limit"));
                        featured_model.setStarted(jsonObject.getString("started"));
                        featured_model.setFinished(jsonObject.getString("finished"));
                        itemList.add(featured_model);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("TAG@123","Item list size:"+itemList.size());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                featured_doubloon_recycler_view.setLayoutManager(mLayoutManager);
                featured_doubloon_recycler_view.setItemAnimator(new DefaultItemAnimator());
                featured_adapter= new Featured_adapter(getActivity(),itemList);
                featured_doubloon_recycler_view.setAdapter(featured_adapter);

            }

            @Override
            public void onError(String error, String responseType) {


            }
        };



    }


}
