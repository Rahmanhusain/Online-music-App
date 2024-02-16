package com.example.musicapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Adapters.Favadapter;
import com.example.musicapp.Models.Recmodel;
import com.example.musicapp.R;
import com.example.musicapp.database.dbhandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link favFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public favFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment favFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static favFragment newInstance(String param1, String param2) {
        favFragment fragment = new favFragment();
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
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

RecyclerView recyclerView;
    Favadapter favad;
    public ArrayList<Recmodel> FavList=new ArrayList<>();
    dbhandler db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fav2, container, false);
        db=new dbhandler(getContext());
        //calling readdata method to initialize the favlist arraylist
        FavList= db.readdata();
        recyclerView=v.findViewById(R.id.favrec);
        if (FavList.isEmpty()){
            Toast.makeText(getContext(), "Sorry! nothing in Favourite", Toast.LENGTH_SHORT).show();
        }
        else {
            favad = new Favadapter(FavList, getContext());
            recyclerView.setAdapter(favad);
            LinearLayoutManager ln = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(ln);
        }
        setHasOptionsMenu(true);
        return  v;
    }

    @Override
    public void onResume() {
        super.onResume();
        //calling readdata method to initialize the favlist arraylist
        FavList= db.readdata();
        favad = new Favadapter(FavList, getContext());
            recyclerView.setAdapter(favad);
            LinearLayoutManager ln = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(ln);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(" Favourite");
        inflater.inflate(R.menu.favsearch,menu);
        MenuItem menuItem2= menu.findItem(R.id.search_bar2);
        SearchView fedit= (SearchView) menuItem2.getActionView();
        fedit.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ArrayList<Recmodel>filterlist2=new ArrayList<>();
                boolean f=false;
                for (Recmodel item:FavList) {
                    if (item.getSongname().toLowerCase().contains(s.toLowerCase())){
                        filterlist2.add(item);
                        f=true;
                    }
                }
                if (favad!=null&&filterlist2!=null){
                    favad.Filteredlist2(filterlist2);
                }
                if (!f){
                    Toast.makeText(getContext(), "Song Not Found", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return true;
            }
        });
    }
    public  void filter(String toString) {
        ArrayList<Recmodel>filterlist2=new ArrayList<>();
        for (Recmodel item:FavList) {
            if (item.getSongname().toLowerCase().contains(toString.toLowerCase())){
                filterlist2.add(item);
            }
        }
        if (favad!=null){
            favad.Filteredlist2(filterlist2);
        }
    }
}