package cn.com.choicesoft.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import cn.com.choicesoft.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * APP版本更新
 */
public class AppUpdate {

    private Context mContext;

    //返回的安装包url
    private String apkUrl;


    private Dialog noticeDialog;

    private Dialog downloadDialog;
    public static final String PACKAGE_NAME = "cn.com.choicesoft";// 包名
    /* 下载包安装路径 */
    private static final String savePath = Environment.getExternalStorageDirectory()+"/data/" + PACKAGE_NAME + "/updateChoice/";

    private static final String saveFileName = savePath + "Choice.apk";

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;


    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;

    private boolean interceptFlag = false;

    public AppUpdate(Context context){
        mContext=context;
    }
    public AppUpdate(Context context,String url) {
        this.mContext = context;
        this.apkUrl=url;
    }
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //外部接口让主Activity调用
    public void checkUpdateInfo(){
        if(ValueUtil.isNotEmpty(this.apkUrl)) {
            showDownloadDialog();
        }else{
            ToastUtil.toast(mContext, "获取下载地址错误！");
        }
    }
    private void showDownloadDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.app_downing);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.app_version_progress, null);
        mProgress = (ProgressBar)v.findViewById(R.id.version_progress);
        builder.setView(v);
        builder.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.setCancelable(false);
        downloadDialog.show();
        downloadApk();
    }

    /**
     * 下载apk
     */

    private void downloadApk(){
        SimpleThread thread=new SimpleThread();
        thread.setOnThreadRun(new SimpleThread.OnThreadRun() {
            @Override
            public void onRun(Handler h) {
                try {
                    URL url = new URL(apkUrl);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();

                    File file = new File(savePath);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    String apkFile = saveFileName;
                    File ApkFile = new File(apkFile);
                    FileOutputStream fos = new FileOutputStream(ApkFile);

                    int count = 0;
                    byte buf[] = new byte[1024];
                    do{
                        int numread = is.read(buf);
                        count += numread;
                        progress =(int)(((float)count / length) * 100);
                        //更新进度
                        h.sendEmptyMessage(DOWN_UPDATE);
                        if(numread <= 0){
                            //下载完成通知安装
                            h.sendEmptyMessage(DOWN_OVER);
                            downloadDialog.dismiss();
                            break;
                        }
                        fos.write(buf,0,numread);
                    }while(!interceptFlag);//点击取消就停止下载.
                    fos.close();
                    is.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onHandleMessage(Message msg) {
                switch (msg.what) {
                    case DOWN_UPDATE:
                        mProgress.setProgress(progress);
                        break;
                    case DOWN_OVER:
                        installApk();
                        break;
                    default:
                        break;
                }
            }
        });
        thread.run();
    }
    /**
     * 安装apk
     */
    private void installApk(){
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);

    }

}
