package cn.com.choicesoft.util.wait;

import android.app.AlertDialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import cn.com.choicesoft.R;
import cn.com.choicesoft.util.SimpleThread;
import cn.com.choicesoft.util.ToastUtil;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

import java.io.IOException;
import java.net.Socket;

/**
 * M。c
 * 2015-04-27
 * Jnwsczh@163.com
 */
public class SearchWaitTVIP {
    private Context mContext;
    private LoadingDialog dialog;

    public SearchWaitTVIP(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 搜索服务
     */
    public void searchIp(){
        getDialog().show();
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        final String ip = getIp(ipAddress);
        new SimpleThread().setOnThreadRun(new SimpleThread.OnThreadRun() {
            @Override
            public void onRun(Handler h) {
                Socket socket=null;
                for(int i=1;i<256||socket!=null;i++) {
                    String tvip=ip+i;
                    try {
                        socket=new Socket(tvip,1991);
                        if (socket!=null){
                            new AlertDialog.Builder(mContext)
                                    .setTitle(R.string.hint)
                                    .setMessage("检查道IP为："+tvip)
                                    .setNegativeButton(R.string.cancle, null)
                                    .setPositiveButton(R.string.confirm, null).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Message message=new Message();
                message.what=0;
                if(socket!=null) {
                    message.what =1;
                }
                h.sendMessage(message);
            }

            @Override
            public void onHandleMessage(Message msg) {
                if(msg.what==0){
                    ToastUtil.toast(mContext,R.string.search_error);
                }
                getDialog().dismiss();
            }
        }).run();

    }

    /**
     * IP转换
     * @param i
     * @return
     */
    private String getIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + ".";
    }

    private LoadingDialog getDialog() {
        if (dialog == null) {
            dialog = new LoadingDialogStyle(mContext, mContext.getString(R.string.please_wait));
            dialog.setCancelable(true);
        }
        return dialog;
    }
}
