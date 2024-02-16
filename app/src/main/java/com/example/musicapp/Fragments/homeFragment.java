package com.example.musicapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Adapters.RecAdapters;
import com.example.musicapp.Models.Recmodel;
import com.example.musicapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    RecyclerView rec;
    ProgressBar progressBar;
    public  ArrayList<Recmodel> List=new ArrayList<>();
   public  RecAdapters ad;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home2, container, false);
        rec=v.findViewById(R.id.rec);
        progressBar=v.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("songs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){

                    List.add(new Recmodel(ds.child("pic").getValue(String.class),ds.child("songname").getValue(String.class),ds.child("songurl").getValue(String.class),ds.child("artist").getValue(String.class)));
                }
                progressBar.setVisibility(View.INVISIBLE);
                ad=new RecAdapters(List, getContext());
                rec.setAdapter(ad);
                LinearLayoutManager ln=new LinearLayoutManager(getContext());
                rec.setLayoutManager(ln);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "error occurred", Toast.LENGTH_SHORT).show();
            }
        });
        setHasOptionsMenu(true);
        return  v;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(" iMusic");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menusearch,menu);
        MenuItem menuItem= menu.findItem(R.id.search_bar);
        SearchView edit= (SearchView) menuItem.getActionView();
        edit.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ArrayList<Recmodel>filterlist1=new ArrayList<>();
                boolean f=false;
                for (Recmodel item:List) {
                    if (item.getSongname().toLowerCase().contains(s.toLowerCase())){
                        filterlist1.add(item);
                        f=true;
                    }
                }
                if (ad!=null&&filterlist1!=null){
                    ad.Filteredlist(filterlist1);
                }
                if (!f){
                    Toast.makeText(getContext(), "Song Not Found", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });
    }

    public  void filter(String toString) {
        ArrayList<Recmodel>filterlist=new ArrayList<>();
        for (Recmodel item:List) {
            if (item.getSongname().toLowerCase().contains(toString.toLowerCase())){
                filterlist.add(item);
            }
        }
        if (ad!=null){
        ad.Filteredlist(filterlist);
    }
    }
}