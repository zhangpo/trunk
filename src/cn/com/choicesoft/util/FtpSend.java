package cn.com.choicesoft.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import cn.com.choicesoft.R;
import it.sauronsoftware.ftp4j.*;

import java.io.File;
import java.io.IOException;

/**
 * FTP发送
 * Created by M.c on 2014/6/16.
 */
public class FtpSend {
    private static String LOG_PATH_SDCARD_DIR= Environment.getExternalStorageDirectory()+"/data/cn.com.choicesoft.Pad/.order/";
    private static String LOG_PATH_MEMORY_DIR="/data" + Environment.getDataDirectory().getAbsolutePath() + "/cn.com.choicesoft/.order/";
    private Context mContext;
    public FtpSend(Context context){
        this.mContext=context;
    }

    /**
     * 保存菜品至文件
     * @return
     */
    public FtpSend saveFile(String content){
        File f=new File(getCurrLogType());
        if(!f.exists()){
            f.mkdirs();
        }
        File file=new File(f, "order.order");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }
    /**
     * FTP上传
     * @param name
     */
    public void send(String name) {
        String ip = SharedPreferencesUtils.getFtpIp(mContext);
        if (ip != null) {
            if (!(ip.split(":").length > 1)) {
                Toast.makeText(mContext, R.string.ftp_adds_error, Toast.LENGTH_LONG).show();
                return;
            }
            FTPClient ftp = new FTPClient();
            try {
                ftp.connect(ip);
                Log.d("H3c", ftp.toString());
                ftp.login(SharedPreferencesUtils.getFtpUsername(mContext), SharedPreferencesUtils.getFtpPwd(mContext));
                ftp.setCharset("utf8");
                FTPFile[] list = ftp.list();
                for (FTPFile file : list) {
                    Log.d("H3c", file.getName());
                }
                File uploadFile = new File(getCurrLogType() +name);
                ftp.upload(uploadFile);
            } catch (IllegalStateException e) {
                Log.d("H3c", "IllegalStateException");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("H3c", "IOException");
                e.printStackTrace();
            } catch (FTPIllegalReplyException e) {
                Log.d("H3c", "FTPIllegalReplyException");
                e.printStackTrace();
            } catch (FTPException e) {
                Log.d("H3c", "FTPException");
                e.printStackTrace();
            } catch (FTPDataTransferException e) {
                Log.d("H3c", "FTPDataTransferException");
                e.printStackTrace();
            } catch (FTPAbortedException e) {
                Log.d("H3c", "FTPAbortedException");
                e.printStackTrace();
            } catch (FTPListParseException e) {
                Log.d("H3c", "FTPListParseException");
                e.printStackTrace();
            }
            try {
                ftp.disconnect(true);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FTPIllegalReplyException e) {
                e.printStackTrace();
            } catch (FTPException e) {
                e.printStackTrace();
            }
        }
    }
    public static String getCurrLogType(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return LOG_PATH_MEMORY_DIR;
        }else{
            return LOG_PATH_SDCARD_DIR;
        }
    }
}
