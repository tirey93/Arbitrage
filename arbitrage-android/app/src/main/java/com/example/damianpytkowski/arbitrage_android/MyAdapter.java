package com.example.damianpytkowski.arbitrage_android;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import static android.support.v7.widget.helper.ItemTouchHelper.*;

class MyAdapter extends RecyclerView.Adapter {
    // źródło danych
    private ArrayList<Article> mArticles = new ArrayList<>();

    // obiekt listy artykułów
    private RecyclerView mRecyclerView;
    private Context mContext;
    // implementacja wzorca ViewHolder
    // każdy obiekt tej klasy przechowuje odniesienie do layoutu elementu listy
    // dzięki temu wywołujemy findViewById() tylko raz dla każdego elementu
    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mContent;

        public MyViewHolder(View pItem) {
            super(pItem);
            mTitle = (TextView) pItem.findViewById(R.id.article_title);
            mContent = (TextView) pItem.findViewById(R.id.article_subtitle);
        }
    }

    // konstruktor adaptera
    public MyAdapter(ArrayList<Article> pArticles, RecyclerView pRecyclerView, Context context){
        mArticles = pArticles;
        mRecyclerView = pRecyclerView;
        mContext = context;
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
        mArticles.remove(positionToDelete);
        // poniższa metoda w animowany sposób usunie element z listy
        notifyItemRemoved(positionToDelete);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        // uzupełniamy layout artykułu
        Article article = mArticles.get(i);
        ((MyViewHolder) viewHolder).mTitle.setText(article.getTitle());
        ((MyViewHolder) viewHolder).mContent.setText(article.getContent());
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}
