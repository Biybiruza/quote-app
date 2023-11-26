package com.biybiruza.quoteapp;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biybiruza.quoteapp.data.Quote;
import com.biybiruza.quoteapp.data.QuoteData;
import com.biybiruza.quoteapp.networking.ApiClient;
import com.biybiruza.quoteapp.networking.ApiService;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.ViewHolderQuote> {

    private List<Quote> list;

    public QuoteAdapter(List<Quote> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderQuote onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote, parent, false);
        return new ViewHolderQuote(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolderQuote holder, int position) {
        Quote model = list.get(position);
        Glide.with(holder.itemView).load(model.getImgUrl()).into(holder.imgUrl);
        holder.tvQuote.setText(model.getQuote());
        holder.tvAuthor.setText(model.getAuthor());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolderQuote extends RecyclerView.ViewHolder {

        ImageView imgUrl;
        TextView tvQuote, tvAuthor;

        public ViewHolderQuote(@NonNull View itemView) {
            super(itemView);
            imgUrl = itemView.findViewById(R.id.iv_quote);
            tvQuote = itemView.findViewById(R.id.tv_quote);
            tvAuthor = itemView.findViewById(R.id.tv_author);
        }
    }
}
