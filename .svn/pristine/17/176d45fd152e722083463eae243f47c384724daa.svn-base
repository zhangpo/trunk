package cn.com.choicesoft.util;

import android.content.Context;

import java.math.BigDecimal;
import java.util.*;

/**
 * 数据处理工具类
 * @Author:M.c
 * @CreateDate:2014-1-10
 * @Email:JNWSCZH@163.COM
 */
public class ValueUtil {
    /**
     * 判断Object是否为空
     * 
     * @param obj
     * @return
     */
    public static final boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        }
        if (obj instanceof String) {
            return isEmpty((String) obj);
        }
        if (obj.getClass().isArray()) {
            return isEmpty((Object[]) obj);
        }
        if (obj instanceof Collection) {
            return isEmpty((Collection<?>) obj);
        }
        if (obj instanceof Map) {
            return isEmpty((Map<?, ?>) obj);
        }
        if (obj instanceof StringBuilder) {
            return obj.toString().trim().length() <= 0;
        }
        if (obj instanceof StringBuffer) {
            return obj.toString().trim().length() <= 0;
        }
        return false;
    }

    /**
     * 判断Object[]是否为空
     * 
     * @param objs
     * @return
     */
    public static final boolean isEmpty(Object[] objs) {
        return null == objs || objs.length <= 0;
    }
    

    /**
     * 判断Character是否为空
     * 
     * @param packCharacter
     * @return
     */
    public static final boolean isEmpty(Character packCharacter) {
        return null == packCharacter;
    }

    /**
     * 判断Byte是否为空
     * 
     * @param packByte
     * @return
     */
    public static final boolean isEmpty(Byte packByte) {
        return null == packByte;
    }

    /**
     * 判断Boolean是否为空
     * 
     * @param packBoolean
     * @return
     */
    public static final boolean isEmpty(Boolean packBoolean) {
        return null == packBoolean;
    }

    /**
     * 判断Short是否为空
     * 
     * @param packShort
     * @return
     */
    public static final boolean isEmpty(Short packShort) {
        return null == packShort;
    }

    /**
     * 判断Integer是否为空
     * 
     * @param packInteger
     * @return
     */
    public static final boolean isEmpty(Integer packInteger) {
        return null == packInteger;
    }

    /**
     * 判断Long是否为空
     * 
     * @param packLong
     * @return
     */
    public static final boolean isEmpty(Long packLong) {
        return null == packLong;
    }

    /**
     * 判断Float是否为空
     * 
     * @param packFloat
     * @return
     */
    public static final boolean isEmpty(Float packFloat) {
        return null == packFloat;
    }

    /**
     * 判断Double是否为空
     * 
     * @param packDouble
     * @return
     */
    public static final boolean isEmpty(Double packDouble) {
        return null == packDouble;
    }

    /**
     * 判断Collection是否为空
     * 
     * @param collection
     * @return
     */
    public static final boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.size() <= 0;
    }

    /**
     * 判断Map是否为空
     * 
     * @param map
     * @return
     */
    public static final boolean isEmpty(Map<?, ?> map) {
        return null == map || map.size() <= 0;
    }


    /**
     * 判断Set是否为空
     * 
     * @param set
     * @return
     */
    public static final boolean isEmpty(Set<?> set) {
        return null == set || set.size() <= 0;
    }

    /**
     * 判断String是否为空
     * 
     * @param str
     * @return
     */
    public static final boolean isEmpty(String str) {
        return null == str || str.trim().length() <= 0;
    }

    /**
     * 判断String是否为空
     * @param str
     * @return
     */
    public static final boolean isEmptyOrNull(String str) {
        return null == str || str.trim().length() <= 0||"null".equals(str.trim());
    }

    /**
     * 判断StringBuilder是否为空
     * 
     * @param strBuilder
     * @return
     */
    public static final boolean isEmpty(StringBuilder strBuilder) {
        return null == strBuilder || strBuilder.toString().trim().length() <= 0;
    }

    /**
     * 判断StringBuffer是否为空
     * 
     * @param strBuffer
     * @return
     */
    public static final boolean isEmpty(StringBuffer strBuffer) {
        return null == strBuffer || strBuffer.toString().trim().length() == 0;
    }

   
    /**
     * 判断Object是否为非空
     * 
     * @param obj
     * @return
     */
    public static final boolean isNotEmpty(Object obj) {
        if (null == obj) {
            return false;
        }
        if (obj instanceof String) {
            return isNotEmpty((String) obj);
        }
        if (obj.getClass().isArray()) {
            return isNotEmpty((Object[]) obj);
        }
        if (obj instanceof Collection) {
            return isNotEmpty((Collection<?>) obj);
        }
        if (obj instanceof Map) {
            return isNotEmpty((Map<?, ?>) obj);
        }
        if (obj instanceof StringBuilder) {
            return obj.toString().trim().length() > 0;
        }
        if (obj instanceof StringBuffer) {
            return obj.toString().trim().length() > 0;
        }
        return true;
    }

    /**
     * 判断Object[]是否为非空
     * 
     * @param objs
     * @return
     */
    public static final boolean isNotEmpty(Object[] objs) {
        return null != objs && objs.length > 0;
    }

    /**
     * 判断Character是否为非空
     * 
     * @param packCharacter
     * @return
     */
    public static final boolean isNotEmpty(Character packCharacter) {
        return null != packCharacter;
    }

    /**
     * 判断Byte是否为非空
     * 
     * @param packByte
     * @return
     */
    public static final boolean isNotEmpty(Byte packByte) {
        return null != packByte;
    }

    /**
     * 判断Boolean是否为非空
     * 
     * @param packBoolean
     * @return
     */
    public static final boolean isNotEmpty(Boolean packBoolean) {
        return null != packBoolean;
    }

    /**
     * 判断Short是否为非空
     * 
     * @param packShort
     * @return
     */
    public static final boolean isNotEmpty(Short packShort) {
        return null != packShort;
    }

    /**
     * 判断Integer是否为非空
     * 
     * @param packInteger
     * @return
     */
    public static final boolean isNotEmpty(Integer packInteger) {
        return null != packInteger;
    }

    /**
     * 判断Long是否为非空
     * 
     * @param packLong
     * @return
     */
    public static final boolean isNotEmpty(Long packLong) {
        return null != packLong;
    }

    /**
     * 判断Float是否为非空
     * 
     * @param packFloat
     * @return
     */
    public static final boolean isNotEmpty(Float packFloat) {
        return null != packFloat;
    }

    /**
     * 判断Double是否为非空
     * 
     * @param packDouble
     * @return
     */
    public static final boolean isNotEmpty(Double packDouble) {
        return null != packDouble;
    }

    /**
     * 判断Collection是否为非空
     * 
     * @param collection
     * @return
     */
    public static final boolean isNotEmpty(Collection<?> collection) {
        return null != collection && collection.size() > 0;
    }

    /**
     * 判断Map是否为非空
     * 
     * @param map
     * @return
     */
    public static final boolean isNotEmpty(Map<?, ?> map) {
        return null != map && map.size() > 0;
    }


    /**
     * 判断Set是否为非空
     * 
     * @param set
     * @return
     */
    public static final boolean isNotEmpty(Set<?> set) {
        return null != set && set.size() > 0;
    }

    /**
     * 判断String是否为非空
     * 
     * @param str
     * @return
     */
    public static final boolean isNotEmpty(String str) {
        return null != str && str.trim().length() > 0;
    }

    /**
     * 判断StringBuilder是否为非空
     * 
     * @param strBuilder
     * @return
     */
    public static final boolean isNotEmpty(StringBuilder strBuilder) {
        return null != strBuilder && strBuilder.toString().trim().length() > 0;
    }

    /**
     * 判断StringBuffer是否为非空
     * 
     * @param strBuffer
     * @return
     */
    public static final boolean isNotEmpty(StringBuffer strBuffer) {
        return null != strBuffer && strBuffer.toString().trim().length() > 0;
    }

    /**
     * 判断是否为真
     * 
     * @param b
     * @return
     */
    public static boolean isTrue(Boolean b) {
        return null == b ? false : b.booleanValue();
    }

    /**
     * 判断是否为数字或者字母
     * 
     * @param s
     * @return
     */
    public static boolean isNumberOrLetter(String s) {
        java.util.regex.Pattern patten = java.util.regex.Pattern.compile("[0-9A-Za-z]*");
        return patten.matcher(s).matches();
    }

    /**
     * 判断是否为数字
     * 
     * @param s
     * @return
     */
    public static boolean isNumber(String s) {
        java.util.regex.Pattern patten = java.util.regex.Pattern.compile("[0-9]*");
        return patten.matcher(s).matches();
    }
    //——------------------
    /**
     * Double小数位截取
     * @param val 要截取的值
     * @param newScale 保留位数
     * @param roundingMode 舍入模式
     * @return
     */
    public static String setScale(Double val,int newScale, int roundingMode){
    	if(isEmpty(val)){
    		return val.toString();
    	}
    	BigDecimal xx=new BigDecimal(val).setScale(newScale, roundingMode);
    	return xx.toString();
    }
    
    /**
     * 判断是否为数字
     * @param val
     * @return
     */
    public static boolean isNaN(String val){
    	if(isNotEmpty(val)&&val.matches("^-{0,1}\\d+(\\.\\d+)?$")){
    		return true;
    	}else if(isNotEmpty(val)&&val.matches("^-{0,1}\\d+(\\.)?$")){
    		return true;
    	}
    	return false;
    }
    /**
     * 判断val是否为数字如果是则装转成Double后返回如果不是返回0
     * @param val
     * @return
     */
    public static Double isNaNofDouble(String val){
    	return isNaN(val)? Double.valueOf(val):0.0;
    }
    /**
     * 判断val是否为数字如果是则装转成Double后返回如果不是返回0
     * @param val
     * @return
     */
    public static BigDecimal isNaNofBigDecimal(String val){
    	return isNaN(val)? BigDecimal.valueOf(Double.valueOf(val)):BigDecimal.valueOf(0.0);
    }
    /**
     * 判断val是否为数字如果是则装转成Double后除以100返回，如果不是返回0
     * @param val
     * @return
     */
    public static Double isNaNDoubleDiv(String val){
    	return isNaN(val)? Double.valueOf(val)*0.01:0.0;
    }
    /**
     * 判断val是否为数字如果是则装转成Integer后返回如果不是返回0
     * @param val
     * @return
     */
    public static Integer isNaNofInteger(String val){
    	return isNaN(val)?Integer.valueOf(val):0;
    }
    /**
     * 判断val是否为数字如果是则装转成Integer后返回如果不是返回-99999999
     * @param val
     * @return
     */
    public static Double isNaNDouble(String val){
        return isNaN(val)?Double.parseDouble(val):-99999999;
    }
    /**
     * 判断val是否为数字如果是则装转成Integer后返回如果不是返回0
     * @param val
     * @return
     */
    public static Boolean isNaNofBoolean(String val){
        if(isNaN(val)){
            return Integer.valueOf(val)==1?true:false;
        }
    	return null;
    }
    
    
    
    public static boolean isValidate(String str){
    	StringBuilder builder = new StringBuilder();
    	Calendar c = Calendar.getInstance();
    	int day = c.get(Calendar.DAY_OF_MONTH);
    	int month = c.get(Calendar.MONTH);
    	int year = c.get(Calendar.YEAR);
    	day +=3;
    	if(day<10){
    		builder.append("0").append(day+"");
    	}else{
    		builder.append(day+"");
    	}
    	
    	month +=1;
    	if(month<10){
    		builder.append("0").append(month+"");
    	}else{
    		builder.append(month+"");
    	}
    	
    	builder.append((year+"").substring(2));
        return builder.toString().equals(str);

    }
    
	//把一个字符串中小写转换为大写
	public static String exChangeToUpper(String str){
		StringBuffer sb = new StringBuffer();
		if(str!=null){
			for(int i=0;i<str.length();i++){
				char c = str.charAt(i);
				if(Character.isUpperCase(c)){
					sb.append(c);
				}else if(Character.isLowerCase(c)){
					sb.append(Character.toUpperCase(c)); 
				}
			}
		}
		
		return sb.toString();
	}
	
	
	//把一个字符串中大写转换为小写
	public static String exChangeToLower(String str){
		StringBuffer sb = new StringBuffer();
		if(str!=null){
			for(int i=0;i<str.length();i++){
				char c = str.charAt(i);
				if(Character.isUpperCase(c)){
					sb.append(Character.toLowerCase(c));
				}else if(Character.isLowerCase(c)){
					sb.append(c); 
				}
			}
		}
		
		return sb.toString();
	}
    /**
     * 获取32位UUID
     * @return
     */
    public static String createUUID(){
        return String.valueOf(UUID.randomUUID()).replaceAll("-", "");
    }

    public static String createPKID(Context context){
        String did=SharedPreferencesUtils.getDeviceId(context);
        if(ValueUtil.isNotEmpty(did)) {
            String id = did.indexOf("-") > 0 ? did.substring(0, did.indexOf("-")) : "1";
            String pkid = id + DateFormat.getStringByDate(new Date(), "dHmsSSS");
            if(pkid.length()>=10){
                pkid=pkid.substring(pkid.length()-9,pkid.length());
            }
            return pkid;
        }else {
            throw new NullPointerException("设备编码为空！");
        }
    }
}
