package com.example.damianpytkowski.arbitrage_android;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.damianpytkowski.arbitrage_android.Common.ArbiLine;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;//ok
    private MyAdapter myAdapter;//bedziemy miec obiekt
    private SwipeRefreshLayout swipeContainer;//ok
    private SwipeController swipeController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //progressBar.setIndeterminate(false);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        //new MahClass(progressBar).execute();
        recyclerView = (RecyclerView) findViewById(R.id.articles);
        // w celach optymalizacji
        recyclerView.setHasFixedSize(true);

        // ustawiamy LayoutManagera
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ustawiamy animatora, który odpowiada za animację dodania/usunięcia elementów listy

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Context tx = this;
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);
                initializeObjects();
                new RetrieveJson(myAdapter, recyclerView).execute();
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RetrieveJson(myAdapter, recyclerView).execute();
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }
    private void initializeObjects(){
        myAdapter = new MyAdapter(recyclerView,getApplicationContext());
    }

}
