package cn.com.choicesoft.util;

import android.content.Context;

import java.math.BigDecimal;
import java.util.*;

/**
 * ���ݴ�������
 * @Author:M.c
 * @CreateDate:2014-1-10
 * @Email:JNWSCZH@163.COM
 */
public class ValueUtil {
    /**
     * �ж�Object�Ƿ�Ϊ��
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
     * �ж�Object[]�Ƿ�Ϊ��
     * 
     * @param objs
     * @return
     */
    public static final boolean isEmpty(Object[] objs) {
        return null == objs || objs.length <= 0;
    }
    

    /**
     * �ж�Character�Ƿ�Ϊ��
     * 
     * @param packCharacter
     * @return
     */
    public static final boolean isEmpty(Character packCharacter) {
        return null == packCharacter;
    }

    /**
     * �ж�Byte�Ƿ�Ϊ��
     * 
     * @param packByte
     * @return
     */
    public static final boolean isEmpty(Byte packByte) {
        return null == packByte;
    }

    /**
     * �ж�Boolean�Ƿ�Ϊ��
     * 
     * @param packBoolean
     * @return
     */
    public static final boolean isEmpty(Boolean packBoolean) {
        return null == packBoolean;
    }

    /**
     * �ж�Short�Ƿ�Ϊ��
     * 
     * @param packShort
     * @return
     */
    public static final boolean isEmpty(Short packShort) {
        return null == packShort;
    }

    /**
     * �ж�Integer�Ƿ�Ϊ��
     * 
     * @param packInteger
     * @return
     */
    public static final boolean isEmpty(Integer packInteger) {
        return null == packInteger;
    }

    /**
     * �ж�Long�Ƿ�Ϊ��
     * 
     * @param packLong
     * @return
     */
    public static final boolean isEmpty(Long packLong) {
        return null == packLong;
    }

    /**
     * �ж�Float�Ƿ�Ϊ��
     * 
     * @param packFloat
     * @return
     */
    public static final boolean isEmpty(Float packFloat) {
        return null == packFloat;
    }

    /**
     * �ж�Double�Ƿ�Ϊ��
     * 
     * @param packDouble
     * @return
     */
    public static final boolean isEmpty(Double packDouble) {
        return null == packDouble;
    }

    /**
     * �ж�Collection�Ƿ�Ϊ��
     * 
     * @param collection
     * @return
     */
    public static final boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.size() <= 0;
    }

    /**
     * �ж�Map�Ƿ�Ϊ��
     * 
     * @param map
     * @return
     */
    public static final boolean isEmpty(Map<?, ?> map) {
        return null == map || map.size() <= 0;
    }


    /**
     * �ж�Set�Ƿ�Ϊ��
     * 
     * @param set
     * @return
     */
    public static final boolean isEmpty(Set<?> set) {
        return null == set || set.size() <= 0;
    }

    /**
     * �ж�String�Ƿ�Ϊ��
     * 
     * @param str
     * @return
     */
    public static final boolean isEmpty(String str) {
        return null == str || str.trim().length() <= 0;
    }

    /**
     * �ж�String�Ƿ�Ϊ��
     * @param str
     * @return
     */
    public static final boolean isEmptyOrNull(String str) {
        return null == str || str.trim().length() <= 0||"null".equals(str.trim());
    }

    /**
     * �ж�StringBuilder�Ƿ�Ϊ��
     * 
     * @param strBuilder
     * @return
     */
    public static final boolean isEmpty(StringBuilder strBuilder) {
        return null == strBuilder || strBuilder.toString().trim().length() <= 0;
    }

    /**
     * �ж�StringBuffer�Ƿ�Ϊ��
     * 
     * @param strBuffer
     * @return
     */
    public static final boolean isEmpty(StringBuffer strBuffer) {
        return null == strBuffer || strBuffer.toString().trim().length() == 0;
    }

   
    /**
     * �ж�Object�Ƿ�Ϊ�ǿ�
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
     * �ж�Object[]�Ƿ�Ϊ�ǿ�
     * 
     * @param objs
     * @return
     */
    public static final boolean isNotEmpty(Object[] objs) {
        return null != objs && objs.length > 0;
    }

    /**
     * �ж�Character�Ƿ�Ϊ�ǿ�
     * 
     * @param packCharacter
     * @return
     */
    public static final boolean isNotEmpty(Character packCharacter) {
        return null != packCharacter;
    }

    /**
     * �ж�Byte�Ƿ�Ϊ�ǿ�
     * 
     * @param packByte
     * @return
     */
    public static final boolean isNotEmpty(Byte packByte) {
        return null != packByte;
    }

    /**
     * �ж�Boolean�Ƿ�Ϊ�ǿ�
     * 
     * @param packBoolean
     * @return
     */
    public static final boolean isNotEmpty(Boolean packBoolean) {
        return null != packBoolean;
    }

    /**
     * �ж�Short�Ƿ�Ϊ�ǿ�
     * 
     * @param packShort
     * @return
     */
    public static final boolean isNotEmpty(Short packShort) {
        return null != packShort;
    }

    /**
     * �ж�Integer�Ƿ�Ϊ�ǿ�
     * 
     * @param packInteger
     * @return
     */
    public static final boolean isNotEmpty(Integer packInteger) {
        return null != packInteger;
    }

    /**
     * �ж�Long�Ƿ�Ϊ�ǿ�
     * 
     * @param packLong
     * @return
     */
    public static final boolean isNotEmpty(Long packLong) {
        return null != packLong;
    }

    /**
     * �ж�Float�Ƿ�Ϊ�ǿ�
     * 
     * @param packFloat
     * @return
     */
    public static final boolean isNotEmpty(Float packFloat) {
        return null != packFloat;
    }

    /**
     * �ж�Double�Ƿ�Ϊ�ǿ�
     * 
     * @param packDouble
     * @return
     */
    public static final boolean isNotEmpty(Double packDouble) {
        return null != packDouble;
    }

    /**
     * �ж�Collection�Ƿ�Ϊ�ǿ�
     * 
     * @param collection
     * @return
     */
    public static final boolean isNotEmpty(Collection<?> collection) {
        return null != collection && collection.size() > 0;
    }

    /**
     * �ж�Map�Ƿ�Ϊ�ǿ�
     * 
     * @param map
     * @return
     */
    public static final boolean isNotEmpty(Map<?, ?> map) {
        return null != map && map.size() > 0;
    }


    /**
     * �ж�Set�Ƿ�Ϊ�ǿ�
     * 
     * @param set
     * @return
     */
    public static final boolean isNotEmpty(Set<?> set) {
        return null != set && set.size() > 0;
    }

    /**
     * �ж�String�Ƿ�Ϊ�ǿ�
     * 
     * @param str
     * @return
     */
    public static final boolean isNotEmpty(String str) {
        return null != str && str.trim().length() > 0;
    }

    /**
     * �ж�StringBuilder�Ƿ�Ϊ�ǿ�
     * 
     * @param strBuilder
     * @return
     */
    public static final boolean isNotEmpty(StringBuilder strBuilder) {
        return null != strBuilder && strBuilder.toString().trim().length() > 0;
    }

    /**
     * �ж�StringBuffer�Ƿ�Ϊ�ǿ�
     * 
     * @param strBuffer
     * @return
     */
    public static final boolean isNotEmpty(StringBuffer strBuffer) {
        return null != strBuffer && strBuffer.toString().trim().length() > 0;
    }

    /**
     * �ж��Ƿ�Ϊ��
     * 
     * @param b
     * @return
     */
    public static boolean isTrue(Boolean b) {
        return null == b ? false : b.booleanValue();
    }

    /**
     * �ж��Ƿ�Ϊ���ֻ�����ĸ
     * 
     * @param s
     * @return
     */
    public static boolean isNumberOrLetter(String s) {
        java.util.regex.Pattern patten = java.util.regex.Pattern.compile("[0-9A-Za-z]*");
        return patten.matcher(s).matches();
    }

    /**
     * �ж��Ƿ�Ϊ����
     * 
     * @param s
     * @return
     */
    public static boolean isNumber(String s) {
        java.util.regex.Pattern patten = java.util.regex.Pattern.compile("[0-9]*");
        return patten.matcher(s).matches();
    }
    //����------------------
    /**
     * DoubleС��λ��ȡ
     * @param val Ҫ��ȡ��ֵ
     * @param newScale ����λ��
     * @param roundingMode ����ģʽ
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
     * �ж��Ƿ�Ϊ����
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
     * �ж�val�Ƿ�Ϊ�����������װת��Double�󷵻�������Ƿ���0
     * @param val
     * @return
     */
    public static Double isNaNofDouble(String val){
    	return isNaN(val)? Double.valueOf(val):0.0;
    }
    /**
     * �ж�val�Ƿ�Ϊ�����������װת��Double�󷵻�������Ƿ���0
     * @param val
     * @return
     */
    public static BigDecimal isNaNofBigDecimal(String val){
    	return isNaN(val)? BigDecimal.valueOf(Double.valueOf(val)):BigDecimal.valueOf(0.0);
    }
    /**
     * �ж�val�Ƿ�Ϊ�����������װת��Double�����100���أ�������Ƿ���0
     * @param val
     * @return
     */
    public static Double isNaNDoubleDiv(String val){
    	return isNaN(val)? Double.valueOf(val)*0.01:0.0;
    }
    /**
     * �ж�val�Ƿ�Ϊ�����������װת��Integer�󷵻�������Ƿ���0
     * @param val
     * @return
     */
    public static Integer isNaNofInteger(String val){
    	return isNaN(val)?Integer.valueOf(val):0;
    }
    /**
     * �ж�val�Ƿ�Ϊ�����������װת��Integer�󷵻�������Ƿ���-99999999
     * @param val
     * @return
     */
    public static Double isNaNDouble(String val){
        return isNaN(val)?Double.parseDouble(val):-99999999;
    }
    /**
     * �ж�val�Ƿ�Ϊ�����������װת��Integer�󷵻�������Ƿ���0
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
    
	//��һ���ַ�����Сдת��Ϊ��д
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
	
	
	//��һ���ַ����д�дת��ΪСд
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
     * ��ȡ32λUUID
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
            throw new NullPointerException("�豸����Ϊ�գ�");
        }
    }
}
