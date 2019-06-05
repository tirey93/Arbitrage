package com.example.damianpytkowski.arbitrage_android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.damianpytkowski.arbitrage_android.Common.ArbiLine;

class MyAdapter extends RecyclerView.Adapter {
    // źródło danych
    private ArrayList<ArbiLine> mArbiLinesList = new ArrayList<>();

    // obiekt listy artykułów
    private RecyclerView mRecyclerView;
    private Context mContext;
    // implementacja wzorca ViewHolder
    // każdy obiekt tej klasy przechowuje odniesienie do layoutu elementu listy
    // dzięki temu wywołujemy findViewById() tylko raz dla każdego elementu
    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mAsk;
        public TextView mRoi;

        public MyViewHolder(View pItem) {
            super(pItem);
            mAsk = (TextView) pItem.findViewById(R.id.tvAsk);
            mRoi = (TextView) pItem.findViewById(R.id.tvRoi);
        }
    }

    // konstruktor adaptera
    public MyAdapter(RecyclerView pRecyclerView, Context context){
        mRecyclerView = pRecyclerView;
        mContext = context;
    }

    public void setmArbiLinesList(ArrayList<ArbiLine> mArbiLinesList) {
        this.mArbiLinesList = mArbiLinesList;
    }

    public ArrayList<ArbiLine> getmArbiLinesList() {
        return mArbiLinesList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        // tworzymy layout artykułu oraz obiekt ViewHoldera
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.article_layout, viewGroup, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SecondActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        // tworzymy i zwracamy obiekt ViewHolder
        return new MyViewHolder(view);
    }

    public void remove(View v) {
        int positionToDelete = mRecyclerView.getChildAdapterPosition(v);

        // usuwamy element ze źródła danych
        mArbiLinesList.remove(positionToDelete);
        // poniższa metoda w animowany sposób usunie element z listy
        notifyItemRemoved(positionToDelete);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        // uzupełniamy layout artykułu
        ArbiLine arbiLine = mArbiLinesList.get(i);
        ((MyViewHolder) viewHolder).mAsk.setText(arbiLine.getMarketAsk());
        ((MyViewHolder) viewHolder).mRoi.setText(arbiLine.getAsset() + " (" + arbiLine.getLatest().getRoi().toString() + " )");
    }

    @Override
    public int getItemCount() {
        return mArbiLinesList.size();
    }
}
