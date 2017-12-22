package br.com.valdecipedroso.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import br.com.valdecipedroso.bakingapp.data.Step;

public class StepDetailFragment extends Fragment {

    private static List<Step> mStep;
    private static int mPositionStep;
    private static boolean mTwoPane;
    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    public static StepDetailFragment newInstance(List<Step> step, int position, boolean twoPane) {
        StepDetailFragment fragment = new StepDetailFragment();
        mStep = step;
        mTwoPane = twoPane;
        mPositionStep = position;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        if(!getResources().getBoolean(R.bool.isTablet) &&
                (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            && !TextUtils.isEmpty(mStep.get(mPositionStep).getVideoURL())){
            rootView = inflater.inflate(R.layout.fragment_fullscreen, container, false);
        }else{
            rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            LinearLayout layoutButtons = rootView.findViewById(R.id.navigation_step);
            layoutButtons.setVisibility(mTwoPane ? View.INVISIBLE : View.VISIBLE);
            Button next = rootView.findViewById(R.id.button_step_next);
            Button prev = rootView.findViewById(R.id.button_step_prev);
            next.setVisibility(mPositionStep < mStep.size()-1 ? View.VISIBLE : View.INVISIBLE );
            prev.setVisibility(mPositionStep > 0 ? View.VISIBLE : View.INVISIBLE );
            next.setOnClickListener(onClickListener(1));
            prev.setOnClickListener(onClickListener(-1));
            TextView description = rootView.findViewById(R.id.fragment_detail_tv_step_description);
            ImageView image = rootView.findViewById(R.id.step_image);

            if(mStep.get(mPositionStep).getDescription() != null) {
                description.setText(mStep.get(mPositionStep).getDescription());
            }

            if(!TextUtils.isEmpty(mStep.get(mPositionStep).getThumbnailURL())){
                image.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(mStep.get(mPositionStep).getThumbnailURL())
                        .thumbnail(0.5f)
                        .into(image);
            }
        }

        mPlayerView = rootView.findViewById(R.id.step_player_view);

        if (!TextUtils.isEmpty(mStep.get(mPositionStep).getVideoURL())) {
            mPlayerView.setVisibility(View.VISIBLE);
            initializePlayer(getContext(), Uri.parse(mStep.get(mPositionStep).getVideoURL()));
        }

        return rootView;
    }

    private View.OnClickListener onClickListener(final int i) {
       return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationPosition(i);
            }
        };
    }

    private void NavigationPosition(int i) {
        mPositionStep = mPositionStep + i;
        android.support.v4.app.FragmentTransaction ftr = getFragmentManager().beginTransaction();
        ftr.detach(StepDetailFragment.this).attach(StepDetailFragment.this).commit();
    }


    private void initializePlayer(Context context, Uri mediaUri) {
        if (mExoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(context, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void releasePlayer() {
        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
