package com.example.aluras.spotifystreamer;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TracksActivityFragment extends Fragment {


    private  ArrayAdapter<Track> mTrackAdapter;

    public TracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_tracks, container, false);

        Intent intent = getActivity().getIntent();
        String id = intent.getStringExtra(Intent.EXTRA_TEXT);

        ArrayList<Track> dados = new ArrayList<Track>();

        mTrackAdapter = new TrackAdapter(getActivity(),dados);

        ListView listView =(ListView) rootView.findViewById(R.id.listViewTracks);

        listView.setAdapter(mTrackAdapter);

        updateTracks(id);

        return rootView;
    }

    private void updateTracks(String strId){
        new FetchTracksTask().execute(strId);
    }

    public class TrackAdapter extends ArrayAdapter<Track> {

        public TrackAdapter(Context context, ArrayList<Track> tracks) {
            super(context, 0, tracks);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Track track = getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_track,parent,false);
            }
            TextView tvTrackName = (TextView) convertView.findViewById(R.id.txtTrackName);
            TextView tvAlbumName = (TextView) convertView.findViewById(R.id.txtAlbum);
            ImageView ivTrackImage = (ImageView) convertView.findViewById(R.id.imgTrack);

            List<Image> imagens = track.album.images;

            if (imagens.size() > 0){
                        Picasso.with(getActivity())
                                .load(imagens.get(0).url)
                                .resize(100, 100)
                                .into(ivTrackImage);
            }else{
                ivTrackImage.setImageResource(R.mipmap.ic_disk);
            }

            tvTrackName.setText(track.name);
            tvAlbumName.setText(track.album.name);
            return convertView;
        }
    }


    public class FetchTracksTask extends AsyncTask<String,Void,Track[]> {

        @Override
        protected Track[] doInBackground(String... params) {

            final ArrayList<Track> dados = new ArrayList<Track>();

            if(!params[0].equals("")){
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();

                Map<String,Object> queryParam = new HashMap<String,Object>();

                queryParam.put("country","BR");

                Tracks tracks = spotify.getArtistTopTrack(params[0], queryParam);

                for (Track track : tracks.tracks){
                    if(track != null){
                        dados.add(track);
                    }
                }
            }

            return dados.toArray(new Track[dados.size()]);
        }

        @Override
        protected void onPostExecute(Track[] tracks) {
            mTrackAdapter.clear();
            if (tracks != null && tracks.length != 0){
                for(Track track : tracks){
                    mTrackAdapter.add(track);
                }
            }else{
                Toast.makeText(getActivity(), ":( - Nenhuma faixa encontrada.", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
