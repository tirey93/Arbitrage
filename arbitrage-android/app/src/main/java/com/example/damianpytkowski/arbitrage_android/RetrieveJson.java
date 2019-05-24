package com.example.damianpytkowski.arbitrage_android;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;

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
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private  Context context;
    private ArrayList<ArbiLine> articles;

    public RetrieveJson(ProgressBar progressBar, RecyclerView recyclerView, Context applicationContext) {
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
        this.context = applicationContext;
    }

    @Override
    protected Void doInBackground(String... strings) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.bringToFront();
        String s = "";
        try {
            s = getText("http://212.191.92.88:51180/MyLogFile.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.INVISIBLE);

        JsonClass js = null;
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
        js = gson.fromJson(s, JsonClass.class);



        articles = new ArrayList<>();
        /*for (int i = 0; i < 20; ++i)
            articles.add(new Article());
        */

        articles.addAll(js.getArbiLines().values());
        return null;
    }
    @Override
    protected void onPostExecute(Void param){
        // tworzymy adapter oraz łączymy go z RecyclerView
        MyAdapter myAdapter = new MyAdapter(articles,recyclerView,context);
        recyclerView.setAdapter(myAdapter);

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
