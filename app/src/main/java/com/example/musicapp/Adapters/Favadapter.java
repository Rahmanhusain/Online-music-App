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
import com.example.musicapp.database.dbhandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Favadapter extends RecyclerView.Adapter<Favadapter.Viewholder>{
    public ArrayList<Recmodel> FList;
    ArrayList<Recmodel>SList;
    Context Fc;
    boolean slist=false;

    public Favadapter(ArrayList<Recmodel> FList, Context fc) {
        this.FList = FList;
        this.SList=FList;
        Fc = fc;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(Fc).inflate(R.layout.fav_layout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        int pi=position;
        Recmodel model=FList.get(position);
        Picasso.get().load(model.getPic()).fit().into(holder.image);
        holder.text.setText(model.getSongname());
        holder.art.setText(model.getSong_artist());
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbhandler db=new dbhandler(Fc);
                db.delete(model.getSongname());
               Delete(pi);
            }
        });
    }

    @Override
    public int getItemCount() {
        return FList.size();
    }
    public void Filteredlist2(ArrayList<Recmodel> filterlist) {
        FList=filterlist;
        notifyDataSetChanged();
        slist=true;
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image,remove;
        TextView text,art;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image=itemView.findViewById(R.id.image);
            remove=itemView.findViewById(R.id.remove);
            text=itemView.findViewById(R.id.song_name);
            art=itemView.findViewById(R.id.artist_name);

        }

        @Override
        public void onClick(View view) {
            int position=this.getBindingAdapterPosition();
            Recmodel model=FList.get(position);
            String pic=model.getPic();
            String text= model.getSongname();
            Intent i=new Intent(Fc, MainActivity2.class);
            Bundle b=new Bundle();
            if(slist){
                for (int inc=0;inc<SList.size();inc++){
                    if(text.equals(SList.get(inc).getSongname())){
                        i.putExtra("rpic",pic);
                        i.putExtra("rtext",text);
                        b.putParcelableArrayList("list", SList);
                        i.putExtras(b);
                        i.putExtra("rpos",inc);
                        Fc.startActivity(i);
                        break;
                    }
                }
            }else {
                i.putExtra("rpic", pic);
                i.putExtra("rtext", text);
                b.putParcelableArrayList("list", FList);
                i.putExtras(b);
                i.putExtra("rpos", position);
                Fc.startActivity(i);
            }
        }
    }
    public void Delete(int pi){
        FList.remove(pi);
        notifyItemRemoved(pi);
    }
}
