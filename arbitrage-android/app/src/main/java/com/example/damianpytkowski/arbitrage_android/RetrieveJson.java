package com.example.damianpytkowski.arbitrage_android;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.damianpytkowski.arbitrage_android.Common.ArbiLine;
import com.example.damianpytkowski.arbitrage_android.Common.JsonClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class RetrieveJson extends AsyncTask<String, Void, Void> {
    private ArrayList<ArbiLine> arbiLinesList;
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;

    public RetrieveJson(MyAdapter myAdapter, RecyclerView recyclerView) {
        this.myAdapter = myAdapter;
        this.recyclerView = recyclerView;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String s = "";
        try {
            s = getText("http://212.191.92.88:51180/MyLogFile.json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonClass js = null;
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
        js = gson.fromJson(s, JsonClass.class);

        arbiLinesList = myAdapter.getmArbiLinesList();
        if(arbiLinesList == null){
            arbiLinesList = new ArrayList<>();
        }


        arbiLinesList.clear();
        arbiLinesList.addAll(js.getArbiLines().values());
        return null;
    }
    @Override
    protected void onPostExecute(Void param){
        myAdapter.setmArbiLinesList(arbiLinesList);
        myAdapter.notifyDataSetChanged();
        if(recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(myAdapter);
        }

        SwipeController swipeController = new SwipeController(myAdapter);
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);


    }

    public static String getText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        return response.toString();
    }
}
