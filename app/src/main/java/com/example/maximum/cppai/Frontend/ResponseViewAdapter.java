package com.example.maximum.cppai.Frontend;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maximum.cppai.Backend.Response;
import com.example.maximum.cppai.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ResponseViewAdapter extends RecyclerView.Adapter<ResponseViewAdapter.ResponseViewHolder> {

    List<Response> responses;

    public ResponseViewAdapter(List<Response> responses){
        this.responses = responses;
    }

    @NonNull
    @Override
    public ResponseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card, viewGroup, false);
        ResponseViewHolder viewHolder = new ResponseViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResponseViewHolder responseViewHolder, int i) {
        Picasso.get().load(responses.get(i).getImageUrl()).into(responseViewHolder.photo);
        responseViewHolder.info.setText(responses.get(i).getDetailedDescription());
        responseViewHolder.learnMoreTxt.setText(Html.fromHtml("<a href="+responses.get(i).getContentUrl()+">Learn more...</a>"));
        responseViewHolder.learnMoreTxt.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int getItemCount() {
        return responses.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ResponseViewHolder extends RecyclerView.ViewHolder {
        CardView view;
        TextView info;
        TextView learnMoreTxt;
        ImageView photo;

        ResponseViewHolder(View itemView) {
            super(itemView);
            view = (CardView) itemView.findViewById(R.id.cardView);
            info = (TextView) itemView.findViewById(R.id.info);
            learnMoreTxt = (TextView) itemView.findViewById(R.id.link);
            photo = (ImageView) itemView.findViewById(R.id.img);
        }
    }

}
