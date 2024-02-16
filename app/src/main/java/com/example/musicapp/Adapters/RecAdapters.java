package com.example.musicapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.MainActivity2;
import com.example.musicapp.Models.Recmodel;
import com.example.musicapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecAdapters extends RecyclerView.Adapter<RecAdapters.Viewholder> {
    ArrayList<Recmodel>List;
    ArrayList<Recmodel>SList;
    Context c;
    boolean slist=false;
    public RecAdapters(ArrayList<Recmodel> list, Context c) {
        List = list;
        SList=list;
        this.c = c;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.rec_layout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
    Recmodel model=List.get(position);
    Picasso.get().load(model.getPic()).fit().into(holder.image);
    holder.text.setText(model.getSongname());
    holder.art.setText(model.getSong_artist());
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public void Filteredlist(ArrayList<Recmodel> filterlist) {
        List=filterlist;
        notifyDataSetChanged();
        slist=true;
    }


    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView text,art;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image=itemView.findViewById(R.id.image);
            text=itemView.findViewById(R.id.song_name);
            art=itemView.findViewById(R.id.artist_name);
        }

        @Override
        public void onClick(View view) {
            int position=this.getBindingAdapterPosition();
            Recmodel model=List.get(position);
            String pic=model.getPic();
           String text= model.getSongname();
            Intent i=new Intent(c, MainActivity2.class);
            Bundle b=new Bundle();
            if(slist){
                for (int inc=0;inc<SList.size();inc++){
if(text.equals(SList.get(inc).getSongname())){
    i.putExtra("rpic",pic);
    i.putExtra("rtext",text);
    b.putParcelableArrayList("list", SList);
    i.putExtras(b);
    i.putExtra("rpos",inc);
    c.startActivity(i);
    break;
}
                }
            }
            else {
                i.putExtra("rpic", pic);
                i.putExtra("rtext", text);
                b.putParcelableArrayList("list", List);
                i.putExtras(b);
                i.putExtra("rpos", position);
                c.startActivity(i);
            }
        }
    }
}
