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
 * APP�汾����
 */
public class AppUpdate {

    private Context mContext;

    //���صİ�װ��url
    private String apkUrl;


    private Dialog noticeDialog;

    private Dialog downloadDialog;
    public static final String PACKAGE_NAME = "cn.com.choicesoft";// ����
    /* ���ذ���װ·�� */
    private static final String savePath = Environment.getExternalStorageDirectory()+"/data/" + PACKAGE_NAME + "/updateChoice/";

    private static final String saveFileName = savePath + "Choice.apk";

    /* ��������֪ͨuiˢ�µ�handler��msg���� */
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
     * ��ȡ�汾��
     * @return ��ǰӦ�õİ汾��
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

    //�ⲿ�ӿ�����Activity����
    public void checkUpdateInfo(){
        if(ValueUtil.isNotEmpty(this.apkUrl)) {
            showDownloadDialog();
        }else{
            ToastUtil.toast(mContext, "��ȡ���ص�ַ����");
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
     * ����apk
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
                        //���½���
                        h.sendEmptyMessage(DOWN_UPDATE);
                        if(numread <= 0){
                            //�������֪ͨ��װ
                            h.sendEmptyMessage(DOWN_OVER);
                            downloadDialog.dismiss();
                            break;
                        }
                        fos.write(buf,0,numread);
                    }while(!interceptFlag);//���ȡ����ֹͣ����.
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
     * ��װapk
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
