package cn.com.choicesoft.util;

import android.app.Activity;
import android.util.Log;
import cn.com.choicesoft.R;
import cn.com.choicesoft.bean.*;
import cn.com.choicesoft.exception.ServerResponseException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ����xml����
 * 
 */
public class AnalyticalXmlUtil {
	private static String TAG="AnalyticalXmlUtil";
	public static User loginReceiveXml(String result) throws ServerResponseException {
		User user = null;
		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance()
					.newPullParser();
			parser.setInput(new StringReader(result));
			// parser.setInput(inStream, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				// Log.d("WelcomeActivity", name + "");
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:// ��ʼԪ���¼�
					if ("response".equalsIgnoreCase(name)) {
						String response = parser.nextText();
						if (!"00".equals(response)) {
							throw new ServerResponseException(
									"response error... the error code is "
											+ response, response);
						}
					} else if ("user".equalsIgnoreCase(name)) {
						user = new User();
						Log.d("WelcomeActivity", name + "");
						user.setEmp(parser.getAttributeValue(null, "emp"));
						user.setName(parser.getAttributeValue(null, "name"));
						user.setDept(parser.getAttributeValue(null, "dept"));
						user.setPassword(parser.getAttributeValue(null, "pass"));
						user.setWaiter(parser.getAttributeValue(null, "waiter"));
					}
					// else if("emp".equalsIgnoreCase(name)){
					// user.setEmp(parser.nextText());
					//
					// }else if("name".equalsIgnoreCase(name)){
					// user.setName(parser.nextText());
					// }
					break;
				case XmlPullParser.END_TAG:// ����Ԫ���¼�
					break;
				}
				eventType = parser.next();// ��ȡ��һ���ڵ���?
			}
		} catch (XmlPullParserException e) {
			Log.d("WelcomeActivity", "������");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;

	}

	/**
	 * ����XNL����
	 * 
	 * @param cla
	 *            ������ʵ��(ʵ���ڵ������ֶα���ΪString����)
	 * @param result
	 * @Author:M.c
	 */
	public static List<Object> anaXML(Class<?> cla, String result) {
		List<Object> list = null;// return����
		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance()
					.newPullParser();
			parser.setInput(new StringReader(result));
			Method[] methods = cla.getMethods();// ��ȡʵ���ڵ����з���
			String className = cla.getSimpleName();// ʵ������
			int eventType = parser.getEventType();
			list = new ArrayList<Object>();// ʵ��ض���?
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();// ��ȡ�ڵ�Name
				if (className.equalsIgnoreCase(name)
						&& eventType == XmlPullParser.START_TAG) {// �ж��Ƿ�����ʵ������
					Object o = cla.newInstance();// ʵ�廯ʵ����
					for (int j = 0; j < methods.length; j++) {// ѭ��ʵ���ڵķ���
						String metName = methods[j].getName();// ��ȡ����Name
						for (int i = 0; i < parser.getAttributeCount(); i++) {// ѭ��xml�ڵ�ֵ
							String attributeName = "set"
									+ parser.getAttributeName(i);// Ϊ��ȡ��XML���ֶ�Name��set�ַ��Ա�����set����ƥ��
							if (attributeName.equalsIgnoreCase(metName)) {// �ж��ֶ�name��ʵ�巽�����Ƿ���ͬ
								Method setMethod = cla.getMethod(metName,String.class);
								setMethod.invoke(o, parser.getAttributeValue(i));
							}
						}
					}
					list.add(o);
				}
				eventType = parser.next();// ��ȡ��һ���ڵ���?
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * δ���˶������?
	 * @param result
	 * @return
	 */
	public static Map<String,Object> analysisProductW(Activity activity,String result){
		Map<String,Object> mapList=new HashMap<String,Object>();
		String[] res=result.split("#");
		String[] res1=res[0].split(";");
		String[] res2=ValueUtil.isNotEmpty(res[1])?res[1].split(";"):null;
		try {
			//------------------����----------------------
			List<Map<String,String>> orderList=new ArrayList<Map<String,String>>();
			for (int i = 0; i < res1.length; i++) {
				String[] str=res1[i].split("@");
				Map<String,String> map=new HashMap<String,String>();
				map.put("mark", str[0]);
				map.put("orderId", str[1]);
				map.put("pkId",str[2]);
				map.put("pcode",str[3]);
				if(ValueUtil.isNaNofDouble(str[9])>0){//�ж��Ƿ�Ϊ��Ʒ ����Ʒ��ʾ��
					map.put("pcname",str[4]+"-"+activity.getString(R.string.give)+"-"+str[9]);
				}else{
					map.put("pcname",str[4]);
				}
				map.put("tpcode",str[5]);
				map.put("tpname",str[6]);
				map.put("tpnum",str[7]);
				if(str[15].equals("1")){//����ǵ�һ��λȡpcount ����ǵڶ����? ȡweight[�ڶ���λ����]
					map.put("pcount",str[8]);
				}else if(str[15].equals("2")){
					map.put("pcount",str[14]);
				}
				map.put("price",str[12]);
				map.put("promonum",str[9]);
				map.put("fujiaprice",str[13]);
				if(ValueUtil.isNotEmpty(str[10])){
					map.put("fujiacode",str[10].replace("!", ";"));
					map.put("fujianame",str[11].replace("!", ";"));
				}
				map.put("weight",str[14]);
				map.put("weightflg",str[15]);
				map.put("unit",str[16]);
				map.put("istc",str[17]);
				if (str.length>18) {
					map.put("fujiacount", str[18]);
					map.put("unitcode", str[19]);
					map.put("unitname", str[20]);
					map.put("istemp", str[21]);
					if (str.length > 22) {
						map.put("tempcode", str[22]);
						map.put("tempname", str[23]);
					}
				}
				orderList.add(map);
			}
			//----
			mapList.put("orderList", orderList);
			//-------------�Ż�ȯ--------------------------
			if(ValueUtil.isNotEmpty(res2)){
				List<Map<String,String>> couponList=new ArrayList<Map<String,String>>();
				for (int i = 0; i < res2.length; i++) {
					String[] str=res2[i].split("@");
					Map<String,String> map=new HashMap<String,String>();
					map.put("mark", str[0]);
					map.put("orderId", str[1]);
					map.put("cName",str[2]);
					map.put("cMoney",str[3]);
					couponList.add(map);
				}
				mapList.put("couponList", couponList);
			}
			//--------------����---------------------------
			String[] res3=res[2].split("@");
			Map<String,String> manMap=new HashMap<String,String>();
			manMap.put("mark", res3[0]);
			manMap.put("manCounts",res3[1]);
			manMap.put("womanCounts","0");
			if (res.length>=4){
				mapList.put("orderMoney", res[4]);
			}
			if(res3.length>2){
				manMap.put("womanCounts",res3[2]);
			}
			mapList.put("manMap", manMap);
			//-----------------ȫ��������-----------------
			if(res.length<4||ValueUtil.isEmpty(res[3])){
				mapList.put("addItem", null);
			}else{
				String res4[]=res[3].split(";");
				List<Map<String,String>> listItem=null;
				if(res4!=null){
					listItem=new ArrayList<Map<String,String>>();
					for(String addItem:res4){
						String[] item =addItem.split("@");
						Map<String,String> map=new HashMap<String,String>();
                        if(item.length>1) {
                            map.put("itemCode", item[0]);
                            map.put("itemName", item[1]);
                        }else {
                            map.put("itemCode", item[0]);
                            map.put("itemName", item[0]);
                        }
						listItem.add(map);
					}
				}

				mapList.put("addItem", listItem);
			}
			return mapList;
		} catch (Exception e) {
			CSLog.e("������ݳ���?",e.getMessage());
		}
		return null;
	}
	/**
	 * ����Żݷ���?
	 * @param result
	 * @return
	 */
	public static String[] couponSplit(String result){
		if(ValueUtil.isEmpty(result)){
			return null;
		}
		String[] str=result.split("@");
		if(ValueUtil.isEmpty(str)||str.length<=0){
			return null;
		}
		String[] str1=str[1].split(";");
		if(ValueUtil.isEmpty(str1)||str1.length<=0){
			return null;
		}
		return str1;
	}
	
	public static HashMap<String, Object> regionAndTableReceiveXml(String result)
			throws ServerResponseException {
		ArrayList<Storetable> tables = null;
		ArrayList<Storearear> areas = null;
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		Storearear area = null;
		Storetable table = null;
		
		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new StringReader(result));
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:// ��ʼԪ���¼�
					if ("response".equalsIgnoreCase(name)) {
						String response = parser.nextText();
						if (!"00".equals(response)) {
							throw new ServerResponseException(
									"response error... the error code is "
											+ response, response);
						}
					} else if ("storearears".equalsIgnoreCase(name)) {//storearears
						areas = new ArrayList<Storearear>();
					} else if ("storearear".equalsIgnoreCase(name)) {
						area = new Storearear();
						area.setArearid(parser.getAttributeValue(null,"arearid"));
						area.setTblname(parser.getAttributeValue(null,"tblname"));
					} else if ("storetables".equalsIgnoreCase(name)) {
						tables = new ArrayList<Storetable>();
					} else if ("storetable".equalsIgnoreCase(name)) {
						table = new Storetable();
						table.setArearid(parser.getAttributeValue(null,"arearid"));
						table.setTablenum(parser.getAttributeValue(null,"tablenum"));
						table.setTblname(parser.getAttributeValue(null,"tblname"));
						table.setUsestate(parser.getAttributeValue(null,"usestate"));
					}
					break;
				case XmlPullParser.END_TAG:// ����Ԫ���¼�
					if("storearear".equalsIgnoreCase(name)){
						areas.add(area);
						area = null;
					}else if("storearears".equalsIgnoreCase(name)){
						hashmap.put("area", areas);
						areas = null;
					}else if("storetable".equalsIgnoreCase(name)){
						tables.add(table);
						table = null;
					}else if("storetables".equalsIgnoreCase(name)){
						hashmap.put("table", tables);
						tables = null;
					}
					break;
				}
				eventType = parser.next();// ��ȡ��һ���ڵ���?
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hashmap;

	}
	
	/**
	 * ��Ʒ������С��
	 * @param result
	 * @return
	 * @throws ServerResponseException
	 */
	public static HashMap<String, Object> grptypAndDishesReceiveXml(
			String result) throws ServerResponseException {
		ArrayList<Grptyp> grptyps = null;
		ArrayList<Dishes> dishelists = null;
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		Grptyp grptyp = null;
		Dishes dishes = null;

		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new StringReader(result));
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:// ��ʼԪ���¼�
					if ("response".equalsIgnoreCase(name)) {
						String response = parser.nextText();
						if (!"00".equals(response)) {
							throw new ServerResponseException(
									"response error... the error code is "
											+ response, response);
						}
					} else if ("classes".equalsIgnoreCase(name)) {
						grptyps = new ArrayList<Grptyp>();
					} else if ("class".equalsIgnoreCase(name)) {
						grptyp = new Grptyp();
//						grptyp.setCod(parser.getAttributeValue(null, "cod"));
						grptyp.setDes(parser.getAttributeValue(null, "des"));
					} else if ("products".equalsIgnoreCase(name)) {
						dishelists = new ArrayList<Dishes>();
					} else if ("product".equals(name)) {
						
						dishes = new Dishes();
						dishes.setId(parser.getAttributeValue(null,"pcode"));//id
						dishes.setName(parser.getAttributeValue(null,"pname"));//����
						dishes.setPrice(parser.getAttributeValue(null,"price1"));//�۸�
						dishes.setUnit(parser.getAttributeValue(null,"unit"));//��λ
						dishes.setType(parser.getAttributeValue(null,"class"));	//�������ִ���
						dishes.setIsChecked("false");//���ø���δѡ��״̬
					}
					break;
				case XmlPullParser.END_TAG:// ����Ԫ���¼�
					if("class".equalsIgnoreCase(name)){
						grptyps.add(grptyp);
						grptyp = null;
					}else if("classes".equalsIgnoreCase(name)){
						hashmap.put("grptyps", grptyps);
						grptyps = null;
					}else if("product".equalsIgnoreCase(name)){
						dishelists.add(dishes);
						dishes = null;
					}else if("products".equalsIgnoreCase(name)){
						hashmap.put("dishes", dishelists);
						dishelists = null;
					}
					break;
				}
				eventType = parser.next();// ��ȡ��һ���ڵ���?
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hashmap;

	}
	
	
	/**
	 * ̨λ������
	 * @param result
	 * @return
	 */
	public static ArrayList<Storetable> anaRegionAndTable(String result){
		String [] tables = ValueUtil.isEmpty(result)?null:result.split(";");
		ArrayList<Storetable> tableLists= new ArrayList<Storetable>();
		if(ValueUtil.isEmpty(tables)){
			return null;
		}
		for(String table:tables){
			String [] tableInfo=table.split("@");
			if(ValueUtil.isEmpty(tableInfo)||tableInfo.length<6){
				return null;
			}
			Storetable storeTable=new Storetable();
			storeTable.setFloorId(tableInfo[1]);//¥��
			storeTable.setArearid(tableInfo[2]);//����
			storeTable.setTablenum(tableInfo[3]);//̨λ����
			storeTable.setUsestate(tableInfo[4]);//̨λ״̬
			storeTable.setTblname(tableInfo[5]);//̨λ���?
			storeTable.setPerson(tableInfo[6]);//̨λ����
			tableLists.add(storeTable);
		}
		return tableLists;
	}
	public static ArrayList<Storetable> anaTable(String result){
		
		ArrayList<Storetable> tableLists= new ArrayList<Storetable>();
		String[]tables = result.split(";");
		for(String table:tables){
//			0@FloorId@areaId@tablenum@tableState@tablename@person; 
			String[] variable= table.split("@");
			Storetable storeTable = new Storetable();
			storeTable.setArearid(variable[2]);
			storeTable.setTablenum(variable[3]);
			storeTable.setTblname(variable[5]);
			storeTable.setUsestate(variable[4]);
			tableLists.add(storeTable);
		}
		return tableLists;
	}
	public static Map<String,String> getOrderMs(String data){
//		0@H000152;4;@H000152;4;0#0#1212@1212
		String[] val=data.split("#");
		Map<String,String> map=new HashMap<String,String>();
		if(val.length>=2){
			String[] orders=val[0].split("@");
			if(val[1].equals("1")){//1Ϊ��Ա 0�ǻ�Ա
				map.put("member", val[2]);
			}else{
				map.put("member", "");
			}
			//-----------�˵���Ϣ
			String[] order=orders[1].split(";");
			map.put("orderId", order[0]);
			map.put("manCs", order[1]);
			map.put("womanCs", order[2]);
			//-----------��Ա��Ϣ
		}
		return map;
	}
	public static List<Map<String,String>> getReserveTable(String data){
		//0;18254109366;H000353;152;2;(null)@0;15011111111;H000362;154;2;0
		String[] res=data.split("@");
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for (String val : res) {
			String[] tableVal=val.split(";");
			if(tableVal.length<5){
				return null;
			}
			Map<String,String> map=new HashMap<String,String>();
			map.put("phone", tableVal[1]);
			map.put("orderId", tableVal[2]);
			map.put("tabNum", tableVal[3]);
			map.put("manNum", tableVal[4]);
			map.put("womanNum", tableVal.length==6?tableVal[5]:"0");
			list.add(map);
		}
		return list;
	}

	/*0@     		0
	orderid@		1
	PKID@			2
	pcode@			3
	PCname@			4
	tpcode@			5
	TPNAME @ 		6
	TPNUM@			7
	pcount@ 		8
	promonum  @		9
	fujiacode@ 		10
	fujianame   @	11
	price@ 			12
	fujiaprice@		13
	weight@			14
	weightflg@ 		15
	unit@			16
	ISTC@			17
	rushcount@		18
	pullcount@		19
	isquit@			20
	quitcause@		21
	rushorcall@		22
	eachprice*/		//23
	/*rushCount   �߲˴���    
	pullCount   ���˵�����    
	IsQuit       �˲˱�־�����ò����˵Ĳ� 0�����? 1�� 
	QuitCause    �˲�ԭ��û�в����أ�    
	rushOrCall   1 ����  2 ����  0 ���������ֲ�����    
	eachPrice     ����

	fujiaCount   ���������� �����������?  !  ����
   Sublistid   �ײ���ϸΨһ��ʶ  ���ײ���ϸ���Ϊ��?
   UnitCode  �൥λ����
   unitName  �൥λ���?
   istemp    �Ƿ���ʱ��  0 ����ʱ��  1��ʱ��
	tempCode ��ʱ�˱���
   tempName ��ʱ�����? */
	public static List<Food> getFoodList(String data){
		String[] res=data.split("##");
		List<Food> list=null;
		if(res.length>1){
			list=new ArrayList<Food>();
			String[] foods=res[0].split(";");
			for(String foodString:foods){
				String[] f=foodString.split("@");
				if(f[20].equals("0")){
					continue;
				}
				Food food=new Food();
				food.setOrderId(f[1]);
				food.setPKID(f[2]);
				food.setPcode(f[3]);
				food.setPcname(f[4]);
				food.setTpcode(f[5]);
				food.setTpname(f[6]);
				food.setTpnum(f[7]);
				food.setPcount(f[8]);
				food.setPromonum(f[9]);
				food.setFujiacode(f[10]);
				food.setFujianame(f[11]);
				food.setPrice(f[12]);
				food.setFujiaprice(f[13]);
				food.setWeight(f[14]);
				food.setWeightflg(f[15]);
				food.setUnit(f[16]);
				food.setIstc(f[17].equals("1") ? true : false);
				food.setUrge(f[18]);//�߲˴���
				food.setOver(f[19]);//��������
				food.setIsquit(f[20]);//�˲˱�ʶ
				food.setQuitcause(f[21]);//�˲�ԭ��
				food.setRushorcall(f[22]);//1 ����  2 ����  0 ���������ֲ�����  
				food.setEachprice(f[23]);//����
				food.setComfujiacount(f[24]);
				food.setIsTemp(ValueUtil.isNaNofInteger(f[28]));
				if(ValueUtil.isNotEmpty(f[26])){
					food.setUnit(f[27]);
					food.setUnitCode(f[26]);
				}
				if(f.length>29) {
					if(ValueUtil.isNotEmpty(f[29])) {
						food.setPcode(f[29]);
					}
					food.setTempName(f[30]);
				}
				list.add(food);
			}
		}
		return list;
	}
	public static ArrayList<Grptyp> getDishType(String data){
		ArrayList<Grptyp> al = new ArrayList<Grptyp>();
		String[] types = data.split("@");
		String [] grptyps = types[1].split(";");
		for(String str:grptyps){
			Grptyp grptyp = new Grptyp();
			String grp [] = str.split("-");
//			grptyp.setCod(grp[1]);
			grptyp.setDes(grp[0]);
			al.add(grptyp);
		}
		return al;
	}
	/**
	 * ������Ա��Ϣ
	 */
	public static Map<String,Object> getVipInfo(String result){
		try {
			String[] b=result.split("@");
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("CardNumber", b[1]);//��Ա����
			map.put("CardType", b[2]);//��Ա������
			map.put("StoredCardsBalance", b[3]);//��ֵ���?
			map.put("IntegralOverall", b[4]);//������
			map.put("CouponsOverall", b[5]);//ȯ���?
			map.put("CouponsAvail", b[6]);//ȯ�������?
			String q[]=b[7].split(";");//ȯ��Ϣ�б�
			List<Map<String,String>> list=new ArrayList<Map<String,String>>();
			for(String quan:q){
				Map<String,String> m=new HashMap<String,String>();
				String [] qx=quan.split(",");
				m.put("Qid", qx[0]);
				m.put("Qmoney", ValueUtil.isNaNDoubleDiv(qx[1]).toString());
				m.put("Qname", qx[2]);
				m.put("Qnum", qx[3]);
				list.add(m);
			}
			map.put("TicketInfoList", list);
			if(b.length>8){
				map.put("pszName", b[8]);//����
				map.put("pszGender", b[9]);//�Ա�
				map.put("age", b[10]);//����
				map.put("zhengjian", b[11]);//֤����
				map.put("beforeEmail", b[12]);//����ǰ׺
				map.put("suffixEmail", b[13]);//������?
				map.put("joinDate", b[14]);//��������
			}else{
				map.put("pszName", "");//����
				map.put("pszGender", "");//�Ա�
				map.put("age", "");//����
				map.put("zhengjian", "");//֤����
				map.put("beforeEmail", "");//����ǰ׺
				map.put("suffixEmail", "");//������?
				map.put("joinDate", "");//��������
			}
			return map;
		} catch (Exception e) {
			Log.e("getVipInfo-��Ա�������??", e.getMessage());
		}
		return null;
	}

	/**
	 * ���̨λ�Ż�ȡ̨λ���?
	 * ̨λ��Ϣ����
	 * @param result
	 * @return
	 */
	public static List<Map<String,Object>> orderMsg(String result){
		String orders[]=result.split("&");
		try {
			if(orders!=null){
				List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
				for (int i=0;i<orders.length;i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					String table[] = orders[i].split(";");
					map.put("orderid",table[0].split("@")[1]);    //�˵���
					map.put("manCounts", table[1]);  //������
					map.put("womanCount", table[2]); //Ů����
					map.put("peopleCount", table[3]);  //������
					map.put("tableState", table[4]);  //̨λ�Ƿ�ռ��  0 δռ��  1 ռ��
					map.put("tableName", table[5]);  //��λ���?
					String tableMsg[] = table[6].split("#");
					map.put("fengtaiyn", tableMsg[0]);//�Ƿ�padԤ�������?   0 û��  1 �ѽ���
					map.put("isHuiyuan", tableMsg[1]); //�Ƿ��ǻ�Ա 0 ���ǻ�Ա 1 �ǻ�Ա ����Ƿǻ��? ��ڶ���?#��������Ϊ��
					if (tableMsg.length > 2) {
						map.put("telNumber", tableMsg[2]);//�ֻ��@��Ա����Ϣ ���card_QueryBalance------��ѯ��Ա����Ϣ�ӿڷ���ֵ
					}
					list.add(map);
				}
				return list;
			}
		} catch (Exception e) {
			Log.e(TAG,"���̨λ�Ż�ȡ�˵����?");
		}
		return null;
	}
}
