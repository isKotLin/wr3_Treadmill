package com.vigorchip.treadmill.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.activity.MainActivity;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.module.MyTime;
import com.vigorchip.treadmill.module.SportData;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

/**
 * 视频
 */
public class VideoFragment extends BaseFragment {
    private View view;
    private static float speeds;//播放速度
    private static Handler handler = new Handler();
    private long currentPosition;
    private VideoView videoView;//private MediaController mMediaController;
    private static MediaPlayer mPlayer;
    private Uri[] rawID = {//Uri.parse("/sdcard/VID_20161003_104308.mp4"),
            Uri.parse("/system/treadmill/video_02.mp4"),//Uri.parse("/storage/usbhost1/1111.MOV"),
            Uri.parse("/system/treadmill/video_01.mp4"),
            Uri.parse("/system/treadmill/video_03.mp4"),//Uri.parse("/system/treadmill/video_04.mp4"),Uri.parse("/system/treadmill/video_05.mp4"),Uri.parse("/system/treadmill/video_06.mp4"),Uri.parse("/system/treadmill/video_07.mp4")
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);
        return view;
    }

    public static void pausess() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

    public static void startsss() {
        if (mPlayer != null) {
            mPlayer.start();
        }
    }

    public static void setMPSpeed(float speed) {
        speeds = speed;
        if (SportData.isRunning()) {
            speeds = speeds > 2 ? speeds = 2 : speeds < 0.5 ? speeds = 0.5f : speeds;
            if (mPlayer != null)
                mPlayer.setPlaybackSpeed(speeds);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            videoView = view.findViewById(R.id.videoView);
//            videoView.getHolder().setFixedSize(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        mMediaController = new MediaController(getContext());// videoView.setMediaController(mMediaController);//绑定控制器
//        videoView.setHardwareDecoder(true);
//            videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);//设置播放画质 低画质
            videoView.setVideoURI(rawID[((MainActivity) getActivity()).getVideo_index()]);
//            videoView.setVideoPath("http://www.midea.com/video/masvod/public/2015/02/28/20150228_14bcec18032_r1_800k.mp4");
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mMediaPlayer) {
                    mPlayer = mMediaPlayer;
                    speeds = (MyTime.getmSpeed() - MyApplication.getInstance().MINSPEED) / ((float) ((MyApplication.getInstance().MAXSPEED - MyApplication.getInstance().MINSPEED) / 12)) / 10 + 0.5f;
                    speeds = speeds > 2 ? speeds = 2 : speeds < 0.5 ? speeds = 0.5f : speeds;
                    mPlayer.setPlaybackSpeed(speeds);
                    if (!SportData.isRunning() || MyTime.getStartOrStop() != 1)
                        handler.post(run);
                    else
                        MyTime.setmSpeed(MyTime.getmSpeed());
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.seekTo(0);
                    mp.start();
                }
            });
        } catch (Exception e) {
            showToast(getString(R.string.play_error));//e.printStackTrace();
        }
    }


    private Runnable run = new Runnable() {

        public void run() {// 获得当前播放时间和当前视频的长度
            currentPosition = videoView.getCurrentPosition();
            if (currentPosition > 0) {
                mPlayer.pause();
                handler.removeCallbacks(run);
            } else
                handler.postDelayed(run, 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        Log.i("---------------------------------------","onPause");
        if (videoView != null) {
            try {// videoView.stopPlayback(); videoView.suspend();
                videoView.pause();
                mPlayer.stop();
            } catch (Exception e) {
                showToast(getString(R.string.stop_error));
            }
        } else {
            showToast(getString(R.string.isnull));
        }
    }
}