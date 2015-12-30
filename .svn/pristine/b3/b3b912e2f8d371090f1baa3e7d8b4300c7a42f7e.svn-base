package cn.com.choicesoft.activity;

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
import cn.com.choicesoft.util.*;
import cn.com.choicesoft.view.LoadingDialog;
import cn.com.choicesoft.view.LoadingDialogStyle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *根据台位查询账单
 *@Author:M.c
 *@CreateDate:2014-2-27
 */
public class Find_orderByTaiNum extends BaseActivity {
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
		ExitApplication.getInstance().addActivity(this);
		if (ChioceActivity.ispad) {
			setContentView(R.layout.activity_find_order_by_tai_num_pad);
		}else{
			setContentView(R.layout.activity_find_order_by_tai_num);
		}
		Button backBut=(Button)findViewById(R.id.find_back);
		backBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				Find_orderByTaiNum.this.finish();
			}
		});
		//设置左侧图片
		ImageView leftimage = (ImageView)findViewById(R.id.find_LeftImage);
		if (!ChioceActivity.ispad) {
			leftimage.setVisibility(View.GONE);
		}
		setBackground(leftimage);
		CList<Map<String,String>> data=new CList<Map<String,String>>();
		data.add("deviceId", SharedPreferencesUtils.getDeviceId(this));
		data.add("userCode", SharedPreferencesUtils.getUserCode(this));
		data.add("tableNum",getIntent().getStringExtra("table"));
		data.add("manCounts","");
		data.add("womanCounts","");
		data.add("orderId","");
		data.add("chkCode","");
		data.add("comOrDetach","0");
		new Server().connect(this, "queryProduct", "ChoiceWebService/services/HHTSocket?/queryProduct", data, new OnServerResponse() {
			@Override
			public void onResponse(String result) {
				getDialog().dismiss();
				String[] res=ValueUtil.isNotEmpty(result)?result.split("@"):null;
				if(res!=null){
					String val=res[0].trim();
					if(!val.equals("0")){
						Toast.makeText(Find_orderByTaiNum.this, R.string.table_not_bill, Toast.LENGTH_LONG).show();
						return;
					}
					Map<String,Object> map=AnalyticalXmlUtil.analysisProductW(Find_orderByTaiNum.this,result);
					if(ValueUtil.isEmpty(map)){
						Toast.makeText(Find_orderByTaiNum.this, R.string.data_null, Toast.LENGTH_LONG).show();
						return;
					}
					List<Map<String,String>> orderList=(List<Map<String,String>>)map.get("orderList");
					if(ValueUtil.isNotEmpty(orderList)){
						int count=0;
						BigDecimal money=new BigDecimal(0.0);
						for(Map<String,String> order:orderList){
							if(ValueUtil.isEmpty(order.get("tpcode"))||(order.get("pcode")!=null&&order.get("pcode").equals(order.get("tpcode")))){
								count++;
								if(ValueUtil.isNaNofDouble(order.get("promonum"))<=0){
									money=money.add(ValueUtil.isNaNofBigDecimal(order.get("price")));
                                    if(ValueUtil.isNaNofDouble(order.get("fujiaprice"))>0){
                                        money=money.add(ValueUtil.isNaNofBigDecimal(order.get("fujiaprice")));
                                    }
								}
							}
						}
						((TextView)Find_orderByTaiNum.this.findViewById(R.id.find_text1)).setText(Find_orderByTaiNum.this.getString(R.string.all_dishes)+count+Find_orderByTaiNum.this.getString(R.string.dao_zongji)+money+Find_orderByTaiNum.this.getString(R.string.money_unit));
						ListView listView = (ListView)Find_orderByTaiNum.this.findViewById(R.id.find_orderList);
						SimpleAdapter adapter=new SimpleAdapter(Find_orderByTaiNum.this,orderList,R.layout.find_order_item,
								new String[]{"pcname","pcount","price","unit","fujianame"},new int[]{R.id.find_disName,R.id.find_disNum,R.id.find_disPri,R.id.find_disUnit,R.id.find_disAdd});
						listView.setAdapter(adapter);//
					}
					//--------------------------------------------全单附加项
					List<Map<String,String>> addItemList=(List<Map<String,String>>)map.get("addItem");
					if(ValueUtil.isNotEmpty(addItemList)){
						ListView allAddItem=(ListView)Find_orderByTaiNum.this.findViewById(R.id.find_all_addtions_listView);
						SimpleAdapter addItem=new SimpleAdapter(Find_orderByTaiNum.this,addItemList, R.layout.all_additem, new String[]{"itemName"}, new int[]{R.id.itemName});
						allAddItem.setAdapter(addItem);
					}
					//----------------------------------------------支付类型List
					List<Map<String,String>> payData=(List<Map<String,String>>)map.get("couponList");
					if(ValueUtil.isNotEmpty(payData)){
						ListView payList=(ListView)Find_orderByTaiNum.this.findViewById(R.id.find_PayList);
						SimpleAdapter payAdapter=new SimpleAdapter(Find_orderByTaiNum.this, payData, R.layout.find_pay_item,
								new String[]{"cName","cMoney"}, new int[]{R.id.find_payMode,R.id.find_payPri});
						payList.setAdapter(payAdapter);
					}
				}
			}
			
			@Override
			public void onBeforeRequest() {
				getDialog().show();
			}
		});
	
	}
	
	/**
	 * 设置左侧图片背景
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
