package com.example.aluras.spotifystreamer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistFragment extends Fragment {


    public ArtistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_artist, container, false);

        ArrayList<String> dados = new ArrayList<String>();

        dados.add("Titãs");
        dados.add("Engenhiros do Hawai");

        ArrayAdapter<String> artistAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_artist, R.id.txtArtistName,dados);

        ListView listView =(ListView) rootView.findViewById(R.id.listViewArtist);

        listView.setAdapter(artistAdapter);



        return rootView;
    }


}
