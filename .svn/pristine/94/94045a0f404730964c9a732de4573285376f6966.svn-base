package cn.com.choicesoft.chinese.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.*;
import cn.com.choicesoft.R;
import cn.com.choicesoft.activity.ChioceActivity;
import cn.com.choicesoft.activity.MainActivity;
import cn.com.choicesoft.chinese.constants.ChineseConstants;
import cn.com.choicesoft.chinese.util.ChineseResultAlt;
import cn.com.choicesoft.chinese.util.ChineseServer;
import cn.com.choicesoft.chinese.util.ChineseViewUtil;
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ∏˘æ›Ã®Œª≤È—Ø’Àµ•-÷–≤Õ
 * @Author:M.c
 * @CreateDate:2014-2-27
 */
public class ChineseFind_orderByTaiNum extends Activity {
	private LoadingDialog dialog;
	public LoadingDialog getDialog() {
		if(dialog==null){
			dialog = new LoadingDialogStyle(this, this.getString(R.string.please_wait));
			dialog.setCancelable(true);
		}
		return dialog;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HashMap<String,Object> map=(HashMap)this.getIntent().getSerializableExtra("Food");
		ExitApplication.getInstance().addActivity(this);
		if (ChioceActivity.ispad) {
			setContentView(R.layout.chinese_activity_find_order_by_tai_num);
		} else {
			setContentView(R.layout.activity_find_order_by_tai_num);
		}
        
		Button backBut=(Button)findViewById(R.id.find_back);
        backBut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                ChineseFind_orderByTaiNum.this.finish();
            }
        });
        List<Map<String,String>> list=(List)map.get("food");
        int count=0;
        for(Map<String,String> food:list){
            count+=ValueUtil.isNaNofDouble(food.get("pcount"));
        }
        ((TextView)ChineseFind_orderByTaiNum.this.findViewById(R.id.find_text1)).
                setText(ChineseFind_orderByTaiNum.this.getString(R.string.all_dishes) +
                        count + ChineseFind_orderByTaiNum.this.getString(R.string.dao_zongji) +
                        ValueUtil.isNaNofBigDecimal(map.get("money").toString()).setScale(2,BigDecimal.ROUND_HALF_UP) + ChineseFind_orderByTaiNum.this.getString(R.string.money_unit));
        ListView listView = (ListView)ChineseFind_orderByTaiNum.this.findViewById(R.id.find_orderList);
        SimpleAdapter adapter=new SimpleAdapter(ChineseFind_orderByTaiNum.this,list,R.layout.find_order_item,
                new String[]{"pcname","pcount","price","unit","fujianame"},
                new int[]{R.id.find_disName,R.id.find_disNum,R.id.find_disPri,R.id.find_disUnit,R.id.find_disAdd});
        listView.setAdapter(adapter);//
        queryPayments(map.get("order").toString());
      //…Ë÷√◊Û≤‡Õº∆¨
  		ImageView leftimage = (ImageView)findViewById(R.id.find_LeftImage);
  		if (!ChioceActivity.ispad) {
  			leftimage.setVisibility(View.GONE);
  			LinearLayout layout = (LinearLayout)findViewById(R.id.find_order_quandanfujia_layout);
  			layout.setVisibility(View.GONE);
  			ListView listView2  = (ListView)findViewById(R.id.find_all_addtions_listView);
  			listView2.setVisibility(View.GONE);
  		}
	}

    public void queryPayments(String order){
        CList list = new CList();
        list.add("pdaid", SharedPreferencesUtils.getDeviceId(this));
        list.add("user", SharedPreferencesUtils.getUserCode(this));
        list.add("folioNo", order);
        new ChineseServer().connect(this, ChineseConstants.QUERYPAYMENTS, list, new OnServerResponse() {
            @Override
            public void onResponse(String result) {
                getDialog().dismiss();
                if (ValueUtil.isNotEmpty(result)) {
                    Map<String, String> map = new ComXmlUtil().xml2Map(result);
                    if (ValueUtil.isEmpty(map)) {
                        ToastUtil.toast(ChineseFind_orderByTaiNum.this, R.string.payment_error);
                        return;
                    }
                    if ("1".equals(map.get("result"))) {
                        String oStr = ChineseResultAlt.oStrAlt(map.get("oStr"));
                        List<Map<String,String>> mapList=new ArrayList<Map<String, String>>();
                        String[] strList=oStr.split("\\^");


                        for (String str : strList) {

                            if (strList.length==1){
                                break;
                            }
                            Map<String,String> payMap=new HashMap<String, String>();
                            String[] pay = str.split("@");
//                            if (pay.length==0){
//                                break;
//                            }
                            payMap.put("des",pay[0]);
                            payMap.put("money",pay[1]);
                            mapList.add(payMap);
                        }
                        if(ValueUtil.isNotEmpty(mapList)){
                            ListView payList=(ListView)ChineseFind_orderByTaiNum.this.findViewById(R.id.find_PayList);
                            SimpleAdapter payAdapter=new SimpleAdapter(ChineseFind_orderByTaiNum.this, mapList, R.layout.find_pay_item,
                                    new String[]{"des","money"}, new int[]{R.id.find_payMode,R.id.find_payPri});
                            payList.setAdapter(payAdapter);
                        }
                    } else {
                        ToastUtil.toast(ChineseFind_orderByTaiNum.this, R.string.payment_null);
                    }
                } else {
                    ToastUtil.toast(ChineseFind_orderByTaiNum.this, R.string.payment_null);
                }
            }

            @Override
            public void onBeforeRequest() {
                getDialog().show();
            }
        });
    }
	/**
	 * …Ë÷√◊Û≤‡Õº∆¨±≥æ∞
	 */
	@SuppressWarnings("deprecation")
	private void setBackground(ImageView imageView) {
		String path = SharedPreferencesUtils.getLeftBgPath(this);
    	if (path!=null &&  !path.equals("NotFind")  ) {
    		Bitmap bitmap = BitmapFactory.decodeFile(path);
    		if (bitmap==null || bitmap.getByteCount()==0) {
    			imageView.setBackgroundResource(R.drawable.order_logo);
			}else {
				BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
				imageView.setBackgroundDrawable(bitmapDrawable);
			}
		}else if (path!=null&&path.equals("NotFind")) {
			imageView.setBackgroundResource(R.drawable.order_logo);
		}
		
	}
}
