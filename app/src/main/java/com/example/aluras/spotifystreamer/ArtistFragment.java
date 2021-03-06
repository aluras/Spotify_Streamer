package com.example.aluras.spotifystreamer;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistFragment extends Fragment {

    private  ArrayAdapter<Artist> mArtistAdapter;


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

        ArrayList<Artist> dados = new ArrayList<Artist>();

        mArtistAdapter = new ArtistAdapter(getActivity(),dados);

        ListView listView =(ListView) rootView.findViewById(R.id.listViewArtist);

        listView.setAdapter(mArtistAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Artist item = (Artist)adapterView.getItemAtPosition(i);

                Intent trackIntent = new Intent(getActivity(),TracksActivity.class);
                trackIntent.putExtra(Intent.EXTRA_TEXT, item.id);
                startActivity(trackIntent);
            }
        });

        return rootView;
    }


    private void updateArtists(String strNome){
        new FetchArtistTask().execute(strNome);
    }

    public class ArtistAdapter extends ArrayAdapter<Artist>{

        public ArtistAdapter(Context context, ArrayList<Artist> artists) {
            super(context, 0, artists);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Artist artist = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_artist,parent,false);
            }
            TextView tvNomeArtista = (TextView) convertView.findViewById(R.id.txtArtistName);
            ImageView ivFiguraArtista = (ImageView) convertView.findViewById(R.id.imgArtist);

            List<Image> imagens = artist.images;

            if (imagens.size() > 0){
                for(Image image : imagens){
                    if(image.width == 300){
                        Picasso.with(getActivity())
                                .load(image.url)
                                .resize(100,100)
                                .into(ivFiguraArtista);
                    }
                }
            }else{
                ivFiguraArtista.setImageResource(R.mipmap.ic_disk);
            }

            tvNomeArtista.setText(artist.name);
            return convertView;
        }
    }


    public class FetchArtistTask extends AsyncTask<String,Void,Artist[]>{
        
        @Override
        protected Artist[] doInBackground(String... params) {

            final ArrayList<Artist> dados = new ArrayList<Artist>();

            if(!params[0].equals("")){
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();

                ArtistsPager artists = spotify.searchArtists(params[0]);
                for (Artist artist : artists.artists.items){
                    if(artist != null){
                        dados.add(artist);
                    }
                }
            }

            return dados.toArray(new Artist[dados.size()]);
        }

        @Override
        protected void onPostExecute(Artist[] artists) {
            mArtistAdapter.clear();
            if (artists != null && artists.length != 0){
                 for(Artist artist : artists){
                    mArtistAdapter.add(artist);
                }
            }else{
                Toast.makeText(getActivity(), ":( - Nenhum artista encontrado.", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
