package com.example.aluras.spotifystreamer;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistFragment extends Fragment {

    private  ArrayAdapter<String> mArtistAdapter;


    public ArtistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_artist, container, false);

        ((TextView)rootView.findViewById(R.id.txtBuscaArtista)).setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH){
                    updateArtists(textView.getText().toString());
                    return true;
                }

                return false;
            }
        });

        ArrayList<String> dados = new ArrayList<String>();

        mArtistAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_artist, R.id.txtArtistName,dados);

        ListView listView =(ListView) rootView.findViewById(R.id.listViewArtist);

        listView.setAdapter(mArtistAdapter);

        return rootView;
    }


    private void updateArtists(String strNome){
        new FetchArtistTask().execute(strNome);
    }

    public class FetchArtistTask extends AsyncTask<String,Void,String[]>{
        
        @Override
        protected String[] doInBackground(String... params) {

            final ArrayList<String> dados = new ArrayList<String>();

            if(!params[0].equals("")){
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();

                ArtistsPager artists = spotify.searchArtists(params[0]);
                for (Artist artist : artists.artists.items){
                    if(artist != null){
                        dados.add(artist.name);
                    }
                }
            }

            return dados.toArray(new String[dados.size()]);
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if (strings != null && strings.length != 0){
                mArtistAdapter.clear();
                for(String dayForecastStr : strings){
                    mArtistAdapter.add(dayForecastStr);
                }
            }else{
                Toast.makeText(getActivity(), ":( - Nenhum artista encontrado.", Toast.LENGTH_SHORT).show();
            }

        }
    }


}
