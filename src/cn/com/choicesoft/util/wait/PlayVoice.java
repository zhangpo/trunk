package cn.com.choicesoft.util.wait;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import cn.com.choicesoft.constants.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 播放声音
 * M。c
 * 2015-04-29
 * Jnwsczh@163.com
 */
public class PlayVoice {
    private static List<String> paths =new ArrayList<String>();
    private static MediaPlayer mediaPlayer;

    /**
     * 开始播放
     * @param context
     * @param names 音频文件名
     */
    public static void play(final Context context,String[] names){
        final String path;
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
             path= Constants.RES_PATH_MEMORY_DIR;
        }else{
             path=Constants.RES_PATH_SDCARD_DIR;
        }
        for(String name:names) {
            File file = new File(path+name+".mp3");
            if(file.exists()){
                paths.add(file.getPath());
            }
        }
        if (paths.size() > 0) {
            boolean playing=false;
            try{
                if(mediaPlayer!=null) {
                    playing = mediaPlayer.isPlaying();
                }
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
            if (mediaPlayer == null||!playing) {
                mediaPlayer = MediaPlayer.create(context, Uri.parse(paths.get(0)));
                paths.remove(0);
                recursion(context, mediaPlayer);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if(paths.size()>0) {
                            mp = MediaPlayer.create(context, Uri.parse(paths.get(0)));
                            paths.remove(0);
                            recursion(context, mp);
                            mp.start();
                        }else {
                            mp.release();
                        }
                    }
                });
            }
        }

    }

    /**
     * 递归添加播放数据
     * @param mp
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void recursion(Context context,MediaPlayer mp){
        if(paths.size()>0) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(paths.get(0)));
            mp.setNextMediaPlayer(mediaPlayer);
            paths.remove(0);
            recursion(context,mediaPlayer);
        }
    }

}
