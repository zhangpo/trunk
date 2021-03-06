package cn.com.choicesoft.activity.wait;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import cn.com.choicesoft.R;
import cn.com.choicesoft.adapter.wait.WaitAdapter;
import cn.com.choicesoft.bean.wait.Wait;
import cn.com.choicesoft.bean.wait.WaitType;
import cn.com.choicesoft.constants.Constants;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;
import cn.com.choicesoft.view.wait.MyRadioGroup;
import com.example.pc700demo.SerialPort;
import com.zj.btsdk.BluetoothService;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * 等位主界面
 * M。c
 * 2015-04-21
 * Jnwsczh@163.com
 */
public class WaitMain extends Activity implements View.OnClickListener, View.OnTouchListener {
    private SerialPort mSerialPort = null;
    String choosed_serial ="/dev/ttySAC3";
    int choosed_buad = 38400;

    private MyRadioGroup waitRadios;//等位类别
    private ListView waitList;//等位信息
    private Button waitTuichu, waitTV;//退菜按钮
    private ImageView quhao;//显示弹出窗按钮
    private PopupWindow popupWindow;//取号弹出窗
    private EditText phone,people;//取号电话，人数
    private Spinner spinner;   //等位类型选择
    private LoadingDialog dialog;
    private RadioButton lineBut;//当前类别按钮
    private static final int STATE_ALL=40111831;//全部按钮ID
    private static final int HISTORY=32904223;//历史按钮ID
    private String url;
    private String store;
    public Map<String,String> callAmount=new HashMap<String, String>();
    private List<WaitType> waitTypeList;
    //滑动事件用到的变量
    private float mPosX;
    private float mPosY;
    private float mCurrentPosX;
    private float mCurrentPosY;
    BluetoothService mService = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_main);
        url=SharedPreferencesUtils.getWaitUrl(this);
        store=SharedPreferencesUtils.getShopEncode(this);
        waitTypeList=new ArrayList<WaitType>();
        mService = new BluetoothService(WaitMain.this, mHandler);
        if (SharedPreferencesUtils.getBuleToolthaddress(WaitMain.this)!=null&&SharedPreferencesUtils.getBuleToolthaddress(WaitMain.this).length()>0){
            BluetoothDevice con_dev = mService.getDevByMac(SharedPreferencesUtils.getBuleToolthaddress(WaitMain.this));
            mService.connect(con_dev);
        }


        if(ValueUtil.isNotEmpty(url)){
            if(ValueUtil.isNotEmpty(store)) {
                iniView();
                iniData();
            }else {
                ToastUtil.toast(this,R.string.store_null);
                this.finish();
            }
        }else{
            ToastUtil.toast(this,R.string.wait_url_null);
            this.finish();
        }
    }

    /**
     * 初始化控件
     */
    private void iniView(){
        waitList= (ListView) this.findViewById(R.id.wait_main_list);
        waitRadios= (MyRadioGroup) this.findViewById(R.id.wait_main_radios);
        waitTuichu= (Button) this.findViewById(R.id.wait_main_tuichu_but);
        waitTV = (Button) this.findViewById(R.id.wait_main_tv_but);
        quhao = (ImageView) this.findViewById(R.id.wait_main_up_qh);
    }

    /**
     * 初始化数据
     */
    private void iniData(){
        waitTuichu.setOnClickListener(this);//退出按钮
        quhao.setOnTouchListener(this);
        waitTV.setOnClickListener(this);
        queryNo(null, null, null);
//        open();
    }

    public void open() {
        // 打开
        try {
            if (mSerialPort == null) {
                CSLog.i("等位打印","打开Open");
                mSerialPort = new SerialPort(choosed_serial, choosed_buad, 0);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mSerialPort != null) {
            CSLog.i("等位打印","开始start");
            new readThread().start();
        }
    }

    /**
     * 为list赋值
     * @param waitTypes
     */
    private void setView(List<WaitType> waitTypes,RadioButton view){
        List<Wait> waits=new ArrayList<Wait>();
        int count=0;
        for(int i=0;i<waitTypes.size();i++){
            if((view==null||view.getTag().equals("N"))||view.getTag().equals(waitTypes.get(i).getVcode())) {
                if (!"L".equals(waitTypes.get(i).getVcode())) {
                    count = count + waitTypes.get(i).getWaits().size();
                    waits.addAll(waitTypes.get(i).getWaits());
                }
            }
            if("L".equals(waitTypes.get(i).getVcode())) {
                if(view!=null&&view.getId()==HISTORY) {
                    count=count+waitTypes.get(i).getWaits().size();
                    waits.addAll(waitTypes.get(i).getWaits());
                }
            }
        }
        //------------------------------------------------
        if(view==null){//判断是不是第一次进入界面
            view= (RadioButton) waitRadios.findViewById(STATE_ALL);
        }
        if(lineBut!=null){
            lineBut.setChecked(true);
        }
        Button button=(Button) ((FrameLayout) view.getParent()).findViewById(R.id.wait_tab_radio_num);
        if(count>0){//判断当前类别下是否有数据
            button.setText(String.valueOf(count));
            button.setVisibility(View.VISIBLE);
        }else{
            button.setVisibility(View.GONE);
        }
        //------------------------------------------------
        waitList.setAdapter(null);
        waitList.setAdapter(new WaitAdapter(waits, this, new WaitAdapter.ClickQH() {
            @Override
            public void quhao(String tele, String sta, String rec, String typ) {
                guoHao(tele, sta, rec, typ);
                callAmount.remove(rec);
            }

            @Override
            public void record(String code, String count) {
                callAmount.put(code, count);
            }

            @Override
            public String getCallAmount(String code) {
                return callAmount.get(code);
            }

        }));
    }


    /**
     * 上拉取号界面
     */
    public void showQhao(){
        if(popupWindow==null) {
            //-----------------------------数字键适配---------------------
            Point point=new Point();
            this.getWindowManager().getDefaultDisplay().getSize(point);
            int w=point.x/6-10;
            int h= (int) (w/1.5);
            TableRow.LayoutParams layoutParams=new TableRow.LayoutParams(w, h);//数字键适配
            //---------------------------取号键适配-------------------------------
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(w*2, (int) (h*1.5));
            //----------------------------------------------------------
            View view = this.getLayoutInflater().inflate(R.layout.wait_alert_view, null);
            Button dismiss = (Button) view.findViewById(R.id.wait_up_down_but);//关闭alert按钮
            Button qx= (Button) view.findViewById(R.id.wait_x_but);//清空按钮
            Button del= (Button) view.findViewById(R.id.wait_del_but);//
            Button confirm= (Button) view.findViewById(R.id.wait_alert_confirm_but);//
            qx.setLayoutParams(layoutParams);
            del.setLayoutParams(layoutParams);
            confirm.setLayoutParams(params);
            people= (EditText) view.findViewById(R.id.wait_alert_people_edit);
            phone= (EditText) view.findViewById(R.id.wait_alert_phone_edit);
            popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnimationStyle(R.anim.bottom_in);
            spinner = (Spinner) view.findViewById(R.id.dia_chineseyixuan_spinner);
            List<Map<String, String>> simpleLists = new ArrayList<Map<String,String>>();
            for (WaitType waitType:waitTypeList){
                Map<String,String>simple=new HashMap<String, String>();
                simple.put("DES",waitType.getVname());
                simple.put("SNO",waitType.getVcode());
                simpleLists.add(simple);
            }
            SimpleAdapter adapter = new SimpleAdapter(this,simpleLists,R.layout.spinner_item,new String[]{"DES","SNO"},new int[]{R.id.spinner_name,R.id.spinner_code});
            spinner.setAdapter(adapter);
            for(int i=0;i<10;i++){//动态循环获取数字按钮变量
                try {
                    Button button= (Button) view.findViewById(R.id.class.getField("wait_" + i + "_but").getInt("wait_" + i + "_but"));
                    final String num = String.valueOf(i);
                    button.setLayoutParams(layoutParams);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            numClick(num);
                        }
                    });
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            dismiss.setOnTouchListener(this);
            del.setOnClickListener(this);
            qx.setOnClickListener(this);
            confirm.setOnClickListener(this);
        }
        popupWindow.showAtLocation(quhao, Gravity.BOTTOM, 0, 0);
    }


    /**
     * 创建类别按钮
     * @param name 显示名称
     * @param num 数量
     */
    private void creatWaitRad(String name,String code,int id,String num){
        View view=this.getLayoutInflater().inflate(R.layout.wait_tab_but,null);
        RadioButton radioButton= (RadioButton) view.findViewById(R.id.wait_tab_radio);
        Button button= (Button) view.findViewById(R.id.wait_tab_radio_num);
        radioButton.setText(name);
        radioButton.setTag(code);
        if(ValueUtil.isNaNofInteger(num)>0) {
            button.setVisibility(View.VISIBLE);
            button.setText(num);
        }
        radioButton.setId(id);
        if(lineBut!=null&&lineBut.getTag().equals(code)){
            radioButton.setChecked(true);
            radioButton.setId(lineBut.getId());
        }
        radioButton.setOnClickListener(new RadioButtonClick());
        waitRadios.addView(view);
    }

    /**
     * 输入事件
     * @param num
     */
    public void numClick(String num){
        if(phone.isFocused()){
            phone.getText().insert(phone.getSelectionStart(), num);
        }else if(people.isFocused()){
            people.getText().insert(people.getSelectionStart(), num);
        }
    }

    /**
     * 设置叫号电视IP
     */
    public void setWiatTVIP(){
        LinearLayout linearLayout= (LinearLayout) this.getLayoutInflater().inflate(R.layout.wait_ip_set,null);
        final EditText editText= (EditText) linearLayout.findViewById(R.id.wait_ip_edit);
        editText.setText(SharedPreferencesUtils.getWaitTV(this));
        Button button= (Button) linearLayout.findViewById(R.id.wait_ip_but);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*new SearchWaitTVIP(WaitMain.this).searchIp();*/
            }
        });
        new AlertDialog.Builder(this,R.style.edittext_dialog).setTitle(R.string.ip_setting)
                .setView(linearLayout)
                .setNegativeButton(R.string.cancle, null)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ip = "^\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\." +
                                "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)" +
                                "\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]" +
                                "\\d|25[0-5])\\b$";
                        String ipadd = editText.getText().toString();
                        if (!ipadd.matches(ip)) {
                            ToastUtil.toast(WaitMain.this, R.string.ip_error);
                        } else {
                            SharedPreferencesUtils.setWaitTV(WaitMain.this, ipadd);
                        }
                    }
                }).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wait_main_tuichu_but://退出
                if (mService != null)
                    mService.stop();
                mService = null;
                WaitMain.this.finish();
                break;
            case R.id.wait_x_but: //取消
                if(phone.isFocused()){
                    phone.setText("");
                }else if(people.isFocused()){
                    people.setText("");
                }
                break;
            case R.id.wait_del_but://删除
                if(phone.isFocused()){
                    int index=phone.getSelectionStart();
                    if(index>0){
                        phone.getText().delete(index-1, index);
                    }
                }else if(people.isFocused()){
                    int index=people.getSelectionStart();
                    if(index>0){
                        people.getText().delete(index - 1, index);
                    }
                }
                break;
            case R.id.wait_alert_confirm_but://确认叫号
                jiaoHao();
                break;
            case R.id.wait_main_tv_but://TVip设置
                setWiatTVIP();
                break;
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            // 按下
            case MotionEvent.ACTION_DOWN:
                mPosX = event.getX();
                mPosY = event.getY();
                break;
            // 移动
            case MotionEvent.ACTION_MOVE:
                mCurrentPosX = event.getX();
                mCurrentPosY = event.getY();
                if (mCurrentPosY - mPosY < 0 && Math.abs(mCurrentPosX - mPosX) < 10) {
                    v.setVisibility(View.INVISIBLE);//隐藏自己
                    showQhao();//显示出取号弹窗
                } else if(mCurrentPosY - mPosY > 0 && Math.abs(mCurrentPosX - mPosX) < 10) {
                    if(popupWindow!=null) {
                        quhao.setVisibility(View.VISIBLE);
                        popupWindow.dismiss();
                    }
                }
                break;
            // 拿起
            case MotionEvent.ACTION_UP:
                if(v.getId()== R.id.wait_up_down_but){//隐藏事件
                    quhao.setVisibility(View.VISIBLE);
                    popupWindow.dismiss();
                }else{
                    v.setVisibility(View.INVISIBLE);//隐藏自己
                    showQhao();//显示出取号弹窗
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 获取等位信息
     * @param lineno
     * @param history
     * @param view
     */
    private void queryNo(final String lineno, final String history, final View view){
        CList<NameValuePair> list=new CList<NameValuePair>();
        list.addNameValue("pk_store", SharedPreferencesUtils.getShopEncode(this));
        list.addNameValue("lineno", lineno == null ? "" : lineno);
        list.addNameValue("history", history == null ? "" : history);
        new Server().httpPost(this, list, Constants.WaitHttp.QUERY_NO, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                CSLog.i("queryNo", result);
                getDialog().dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    List<WaitType> waitTypes = new ArrayList<WaitType>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        WaitType type = new WaitType();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        type.setVcode(getValue(jsonObject, "vcode"));
                        type.setVname(getValue(jsonObject, "vname"));
                        type.setVinit(getValue(jsonObject, "vinit"));
                        JSONArray ja = jsonObject.getJSONArray("waits");
                        List<Wait> list = new ArrayList<Wait>();
                        for (int j = 0; j < ja.length(); j++) {
                            Wait wait = new Wait();
                            JSONObject jo = ja.getJSONObject(j);
                            wait.setPax(getValue(jo, "pax"));
                            String rec=ValueUtil.isEmpty(type.getVinit())?getValue(jo, "rec"):type.getVinit()+"-"+getValue(jo, "rec");
                            wait.setRec(rec);
                            wait.setSta(getValue(jo, "sta"));
                            wait.setCalllineno(getValue(jo, "calllineno"));
                            wait.setTblnum(getValue(jo, "tblnum"));
                            wait.setWtime(getValue(jo,"wtime"));
                            wait.setTele(getValue(jo, "tele"));
                            wait.setBegintime(getValue(jo, "begintime"));
                            wait.setWaittimes(getValue(jo, "waittimes"));
                            wait.setType(type);
                            list.add(wait);
                        }
                        type.setWaits(list);
                        waitTypes.add(type);
                    }
                    if (waitTypeList.size()==0)
                        waitTypeList.addAll(waitTypes);
                    if (waitRadios.getChildCount() <= 0 || (ValueUtil.isEmpty(lineno) && ValueUtil.isEmpty(history))) {
                        waitRadios.removeAllViews();
                        int count = 0;
                        int passCount = 0;
                        for (int i = 0; i < waitTypes.size(); i++) {
                            WaitType type = waitTypes.get(i);
                            if (!"L".equals(type.getVcode())) {
                                creatWaitRad(type.getVname(), type.getVcode(), -1, String.valueOf(type.getWaits().size()));
                                count = count + type.getWaits().size();
                            } else {
                                passCount = passCount + type.getWaits().size();
                            }
                        }
                        //---------------全部按钮-------------
                        View all = WaitMain.this.getLayoutInflater().inflate(R.layout.wait_tab_but, null);
                        RadioButton radioButton = (RadioButton) all.findViewById(R.id.wait_tab_radio);
                        Button button = (Button) all.findViewById(R.id.wait_tab_radio_num);
                        if (count > 0) {
                            button.setVisibility(View.VISIBLE);
                        }
                        button.setText(String.valueOf(count));
                        radioButton.setText(R.string.state_all);
                        radioButton.setId(STATE_ALL);
                        if (lineBut == null) {
                            radioButton.setChecked(true);
                            lineBut=radioButton;
                        }
                        radioButton.setTag("N");
                        radioButton.setOnClickListener(new RadioButtonClick());
                        waitRadios.addView(all);
                        //---------------历史按钮------------
                        View ls = WaitMain.this.getLayoutInflater().inflate(R.layout.wait_tab_but, null);
                        RadioButton lsrb = (RadioButton) ls.findViewById(R.id.wait_tab_radio);
                        Button lsb = (Button) ls.findViewById(R.id.wait_tab_radio_num);
                        if (passCount > 0) {
                            lsb.setVisibility(View.VISIBLE);
                        }
                        lsb.setText(String.valueOf(passCount));
                        lsrb.setText(R.string.history);
                        lsrb.setId(HISTORY);
                        lsrb.setTag("Y");
                        lsrb.setOnClickListener(new RadioButtonClick());
                        waitRadios.addView(ls);
                    }
                    setView(waitTypes, (RadioButton) view);
                } catch (JSONException e) {
                    ToastUtil.toast(WaitMain.this, R.string.gain_wait_error);
                    WaitMain.this.finish();
                }
            }

            @Override
            public void onBeforeRequest() {
                getDialog().show();
            }
        });
    }
    /**
     * 就餐、取消
     * @param tele
     * @param sta 状态C:取消号,D:叫号
     */
    public void guoHao(String tele, final String sta,String rec,String typ){
        CList<NameValuePair> list=new CList<NameValuePair>();
        list.addNameValue("pk_store",SharedPreferencesUtils.getShopEncode(this));
        list.addNameValue("tele",tele);
        list.addNameValue("sta",sta);
        list.addNameValue("rec",ValueUtil.isEmpty(rec)?"0":rec.substring(rec.lastIndexOf("-")+1,rec.length()));
        list.addNameValue("lineno",typ);
        new Server().httpPost(this, list, Constants.WaitHttp.CANCEL_SEAT, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                CSLog.i("CANCEL_SEAT", result);
                if("0".equals(result)){
                    ToastUtil.toast(WaitMain.this,R.string.success);
                }else{
                    if("D".equals(sta)){
                        ToastUtil.toast(WaitMain.this,R.string.jiaohao_error);
                    }else if("A".equals(sta)){
                        ToastUtil.toast(WaitMain.this,R.string.chexiao_error);
                    }else{
                        ToastUtil.toast(WaitMain.this,R.string.quxiao_error);
                    }
                }
                queryNo(null, null, lineBut);
            }

            @Override
            public void onBeforeRequest() {
                getDialog().show();
            }
        });
    }
    /**
     * 获取号码
     */
    public void jiaoHao(){
        //取号不需要桌台类型,如果需要,下面方法可获取
        //String lineno=lineBut.getTag().toString();
        CList<NameValuePair> list = new CList<NameValuePair>();
        list.addNameValue("pk_store", SharedPreferencesUtils.getShopEncode(this));
        list.addNameValue("pnum", people.getText().toString());
        list.addNameValue("tele", phone.getText().toString());
        String lineno="";
        if (Integer.parseInt(people.getText().toString())>1){
            lineno=waitTypeList.get(spinner.getSelectedItemPosition()).getVcode();
        }
        list.addNameValue("wechat","");

        list.addNameValue("lineno",lineno);

        new Server().httpPost(this, list, Constants.WaitHttp.TAKE_NO, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                CSLog.i("TAKE_NO", result);
                getDialog().dismiss();
                if(ValueUtil.isNotEmpty(result)){
                    String[] msg=result.split("-");
                    String errorMsg=result;
                    if(msg!=null||result.indexOf("rec")>0) {
                        if (msg[0].equals("ERROR")) {
                            switch (ValueUtil.isNaNofInteger(msg[1])) {
                                case 9001:
                                    errorMsg = WaitMain.this.getString(R.string.store_error);
                                    break;
                                case 9002:
                                    errorMsg = WaitMain.this.getString(R.string.people_error);
                                    break;
                                case 9003:
                                    errorMsg = WaitMain.this.getString(R.string.phone_error);
                                    break;
                                case 9004:
                                    errorMsg = WaitMain.this.getString(R.string.wait_type_error1);
                                    break;
                                case 9005:
                                    errorMsg = WaitMain.this.getString(R.string.wait_type_error2);
                                    break;
                                case 9006:
                                    errorMsg = WaitMain.this.getString(R.string.store_error);
                                    break;
                                case 9007:
                                    errorMsg = WaitMain.this.getString(R.string.store_time_error);
                                    break;
                                case 9008:
                                    errorMsg = WaitMain.this.getString(R.string.wait_repetition);
                                    break;
                            }
                            ToastUtil.toast(WaitMain.this, errorMsg);
                        } else {
                            ToastUtil.toast(WaitMain.this, R.string.take_success);
                            if (SharedPreferencesUtils.getBuleToolthaddress(WaitMain.this)!=null&&SharedPreferencesUtils.getBuleToolthaddress(WaitMain.this).length()>0){
                                getPrintMsg(result);
                            }else {
                                printMsg(result);
                            }
                        }
                    }
                }else{
                    ToastUtil.toast(WaitMain.this, R.string.take_failure);
                }
                queryNo(null, null, lineBut);
            }

            @Override
            public void onBeforeRequest() {
                getDialog().show();
            }
        });
    }
    public void getPrintMsg(final String str1){
        new Server().connect(this, "printWaitMsg", "ChoiceWebService/services/HHTSocket?/printWaitMsg", null, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);


                        mService.write(Align_Center_Title());
                        mService.sendMessage("欢迎光临" + getValue(json, "STORENAME"), "GBK");
                        // 执行打印命令
                        mService.write(Align_Left());
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        mService.sendMessage("取号时间:" + df.format(new Date()), "GBK");
                        mService.sendMessage("================================", "GBK");
                        mService.write(Align_Center_Title());
                        JSONObject json1 = new JSONObject(str1);
                        String hao = "";
                        if (getValue(json1, "vinit") != null) {
                            hao += (getValue(json1, "vinit") + "-" + getValue(json1, "rec"));
                        } else {
                            hao = getValue(json1, "rec");
                        }
                        mService.sendMessage(hao, "GBK");
                        mService.write(Align_Left());
                        mService.sendMessage("================================", "GBK");
                        mService.sendMessage("请收好本单以便叫号", "GBK");
                        mService.sendMessage(getValue(json, "STORENAME"), "GBK");
                        mService.sendMessage("TEL:" + getValue(json, "TEL"), "GBK");
                        mService.sendMessage("  ", "GBK");
                        mService.sendMessage("  ", "GBK");
                        mService.sendMessage("  ", "GBK");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    List<WaitType> waitTypes = new ArrayList<WaitType>();
//                    for (int i = 0; i < jsonArray.length(); i++) {
                }


                if (ValueUtil.isEmpty(result) || !result.equals("OK")) {
                    ToastUtil.toast(WaitMain.this, R.string.print_wait_failure);
                }
            }

            @Override
            public void onBeforeRequest() {
            }
        });
    }

    /**
     * 字符串左对齐
     *
     * @param
     * @return
     */
    public byte[] Align_Left() {
        // 字体大小(不变)
        byte[] cmd = new byte[50];
        cmd[0] = 0x1d;
        cmd[1] = 0x21;
        cmd[2] = 0x00;
        // 字体 左对齐
        cmd[3] = 0x1b;
        cmd[4] = 0x61;
        cmd[5] = 0x00;
        // 执行打印命令
        return cmd;
    }
    /**
     * 字符串居中对齐 字体变大 表名
     *
     * @param str
     * @return
     */
    public byte[] Align_Center_Title() {
        // 粗体打印（禁止）
        byte[] cmd = new byte[50];
        // 字体大小(两倍)
        cmd[0] = 0x1d;
        cmd[1] = 0x21;
        cmd[2] = 0x11;
        // 字体 居中对齐
        cmd[3] = 0x1b;
        cmd[4] = 0x61;
        cmd[5] = 0x01;
        return cmd;
    }

    public void printMsg(final String str){
//        new Thread() {
//            @Override
//            public void run() {
//                byte[] buffer = null;
//                try {
//                    CSLog.i("等位打印","获取数据："+str);
//                    buffer = str.getBytes("GBK");
//                } catch (Exception e) {
//                    buffer = str.getBytes();
//                }
//                try {
//                    CSLog.i("等位打印","开始print");
//                    mSerialPort.getOutputStream().write(buffer);
//                } catch (Exception e) {
//                    CSLog.e("WaitMain-打印",e.getMessage());
//                }
//            }
//        }.start();
        CList cList=new CList();
        cList.addMap("JsonData",str);
        cList.addMap("vscode",SharedPreferencesUtils.getShopEncode(this));
        new Server().connect(this, "printWaitNum", "ChoiceWebService/services/HHTSocket?/printWaitNum", cList, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                if(ValueUtil.isEmpty(result)||!result.equals("OK")){
                    ToastUtil.toast(WaitMain.this,R.string.print_wait_failure);
                }
            }
            @Override
            public void onBeforeRequest() {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mSerialPort.close();
    }

    /**
     * 获取JSONObject数据
     * @param object
     * @param key
     * @return
     */
    private String getValue(JSONObject object,String key){
        try {
            return object.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 等待窗
     * @return
     */
    public LoadingDialog getDialog() {
        if (dialog == null) {
            dialog = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
            dialog.setCancelable(true);
        }
        return dialog;
    }
    class RadioButtonClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            lineBut= (RadioButton) v;
            if (v.getTag().equals("N")) {
                queryNo(null, "N",v);
            } else if (v.getTag().equals("Y")) {
                queryNo(null, "Y",v);
            } else {
                queryNo(v.getTag().toString(), "N",v);
            }
        }
    }
    class readThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    byte[] buffer = new byte[300];
                    if (mSerialPort.getInputStream() == null)
                        return;
                    mSerialPort.getInputStream().read(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:   //已连接
                            Toast.makeText(getApplicationContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_CONNECTING:  //正在连接
                            Log.d("蓝牙调试", "正在连接.....");
                            break;
                        case BluetoothService.STATE_LISTEN:     //监听连接的到来
                        case BluetoothService.STATE_NONE:
                            Log.d("蓝牙调试","等待连接.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };
}