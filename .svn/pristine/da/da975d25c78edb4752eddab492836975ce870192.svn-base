package cn.com.choicesoft.util;

import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.util.Log;
import android.webkit.WebView;
import cn.com.choicesoft.R;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

/**
 * APP更新 版本验证
 * @author M.c
 */
public class AppVersion {
    private Context mContext;
    public AppVersion(Context context){
        mContext=context;
    }
    public LoadingDialog dialog;
    private LoadingDialog getDialog(String text){
        dialog = new LoadingDialogStyle(mContext, text);
        dialog.setCancelable(true);
        return dialog;
    }
    private LoadingDialog getDialog(){
        if(dialog!=null){
            return dialog;
        }
        dialog = new LoadingDialogStyle(mContext, "请稍候...");
        dialog.setCancelable(true);
        return dialog;
    }
    /**
     * 版本更新验证
     */
    public void updateVer(){
        /*isTypUpdateWebService (String str,String code,String Typ,String xmlStr)*/
        final String scode=new ListProcessor().query("select scode from Storetables_mis group by scode",null,mContext,new ListProcessor.Result<String>() {
            @Override
            public String handle(Cursor c) {
                while (c.moveToNext()){
                    return c.getString(0);
                }
                return null;
            }
        });
        if(scode==null){
            return;
        }
        CList<Map<String,String>> list=new CList<Map<String,String>>();
        list.add("version",getVersion());//当前程序使用版本号
        list.add("code",scode);//当前程序店铺编码
        list.add("typ","PAD");//程序缩写
        list.add("xmlStr",getXmlStr());//为通信密码识别参数
        new Server().appUpdate(mContext, "isTypUpdateWebService","http://192.168.0.252:8021/autoUpdateService/webService/autoService/isTypUpdateWebService",
                "http://webService.choice.com/",list, new OnServerResponse() {

                    @Override
                    public void onResponse(String result) {
                        getDialog().dismiss();
                        if(ValueUtil.isNotEmpty(result)){
                            if("1".equals(result)){
                                appUpMsg(scode);
                            }
                        }
                    }

                    @Override
                    public void onBeforeRequest() {
                        getDialog("更新验证...").show();
                    }
                });
    }

    /**
     * 获取软件更新信息
     */
    public void appUpMsg(final String scode) {
        CList<Map<String, String>> list = new CList<Map<String, String>>();
        list.add("version", getVersion());//当前程序使用版本号
        list.add("code", scode);//当前程序店铺编码
        list.add("typ", "PAD");//程序缩写
        list.add("xmlStr", getXmlStr());//为通信密码识别参数
        new Server().appUpdate(mContext, "getTypUpdateCont", "http://192.168.0.252:8021/autoUpdateService/webService/autoService/getTypUpdateCont",
                "http://webService.choice.com/", list, new OnServerResponse() {
                    @Override
                    public void onResponse(String result) {
                        getDialog().dismiss();
                        if (ValueUtil.isNotEmpty(result)) {
                            WebView webView = new WebView(mContext);
                            webView.loadDataWithBaseURL(null, result, "text/html", "UTF-8", null);
                            new AlertDialog.Builder(mContext).setView(webView).setTitle("更新提示")
                                    .setNegativeButton("下载", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            appUrl(scode);
                                        }
                                    }).setPositiveButton("取消", null).show();
                        }
                    }

                    @Override
                    public void onBeforeRequest() {
                        getDialog("获取更新信息...").show();
                    }
                }
        );
    }

    /**
     * 获取软件更新地址
     */
    public void appUrl(String scode){
        CList<Map<String, String>> list = new CList<Map<String, String>>();
        list.add("version", getVersion());//当前程序使用版本号
        list.add("code", scode);//当前程序店铺编码
        list.add("typ", "PAD");//程序缩写
        list.add("xmlStr", getXmlStr());//为通信密码识别参数
        new Server().appUpdate(mContext, "findVersionPADWebService", "http://192.168.0.252:8021/autoUpdateService/webService/autoService/findVersionPADWebService",
                "http://webService.choice.com/", list, new OnServerResponse() {
                    @Override
                    public void onResponse(String result) {
                        getDialog().dismiss();
                        if (ValueUtil.isNotEmpty(result)) {
                            new AppUpdate(mContext,result).checkUpdateInfo();
                        }
                    }

                    @Override
                    public void onBeforeRequest() {
                        getDialog("准备下载...").show();
                    }
                }
        );
    }
    public String getXmlStr(){
        /*程序缩写+choicesoft+当前版本号+.001*/
        String str="PADchoicesoft"+getVersion()+".001";
        return Md5.Digest(str);
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
}
