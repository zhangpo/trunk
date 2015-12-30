package cn.com.choicesoft.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ���ڴ�����
 * @Author:M.c
 * @CreateDate:2014-3-11
 * @Email:JNWSCZH@163.COM
 */
public class DateFormat {
	/**
	 * �������ں�ת����ʽ�õ��ַ���
	 * @param date ����
	 * @param formatType ת����ʽ ����  yyyy-MM-dd
	 * @return
	 */
	public static String getStringByDate(Date date,String formatType){
		return new SimpleDateFormat(formatType).format(null==date?new Date():date);
	}
	/**
	 * ���ݴ�������ں����� �õ�ָ��ʱ��  (���� getDateBefore��new Date(),"day",-1,5�� Ϊ�õ���ǰ����֮ǰ5�������)
	 * @param date ָ������
	 * @param type ��������  year month day
	 * @param beforeOrAfter  ֮ǰ����֮��  -1Ϊ֮ǰ 1Ϊ֮�� 
	 * @param number  ָ��������   ǰ5����  ��Ϊ5
	 * @return �������
	 */
	public static Date getDateBefore(Date date,String type,int beforeOrAfter,int number){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if(beforeOrAfter==1){
			if(type.equals("day")){
				calendar.add(Calendar.DATE, number);
			}else if(type.equals("month")){
				calendar.add(Calendar.MONTH, number);
			}else if(type.equals("year")){
				calendar.add(Calendar.YEAR, number);
			}
		}else{
			if(type.equals("day")){
				calendar.add(Calendar.DATE, -number);
			}else if(type.equals("month")){
				calendar.add(Calendar.MONTH, -number);
			}else if(type.equals("year")){
				calendar.add(Calendar.YEAR, -number);
			}
		}
		
		return calendar.getTime();
	}
	/**
	 * ���ݴ�������ڻ�ȡָ�����͵����ڶ���
	 * @param date ���������
	 * @param formatType ת����ʽ
	 * @return
	 */
	public static Date formatDate(Date date,String formatType){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(formatType);
			String dateString = sdf.format(null==date?new Date():date);
			return sdf.parse(dateString);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * ���ݴ�������ڻ�ȡָ�����͵����ڶ���
	 * @param date ���������
	 * @param formatType ת����ʽ
	 * @return
	 */
	public static String formatDateToString(Date date,String formatType){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(formatType);
			return sdf.format(null==date?new Date():date);
//			return sdf.parse(dateString);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
