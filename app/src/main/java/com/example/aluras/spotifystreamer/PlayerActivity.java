package com.example.aluras.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;


public class PlayerActivity extends ActionBarActivity {

    private static TextView txtAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_player, container, false);

            Intent intent = getActivity().getIntent();
            String id = intent.getStringExtra(Intent.EXTRA_TEXT);

            txtAlbum = (TextView) rootView.findViewById(R.id.txtAlbum);

            txtAlbum.setText(id);

            new FetchTrackTask().execute(id);

            return rootView;
        }

        public class FetchTrackTask extends AsyncTask<String,Void,Track> {

            @Override
            protected Track doInBackground(String... params) {

                Track track = new Track();

                if(!params[0].equals("")){
                    SpotifyApi api = new SpotifyApi();
                    SpotifyService spotify = api.getService();

                    track = spotify.getTrack(params[0]);
                }

                return track;
            }

            @Override
            protected void onPostExecute(Track track) {
                if (track != null){
                    txtAlbum.setText(track.album.name);
                }else{
                    // Toast.makeText( ":( - Nenhuma faixa encontrada.", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


}
