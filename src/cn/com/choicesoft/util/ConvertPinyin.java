package cn.com.choicesoft.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 拼音处理
 */
public class ConvertPinyin {
	/**
	 * 汉字转全拼
	 * @param name
	 * @return
	 * @throws BadHanyuPinyinOutputFormatCombination
	 */
	public static String convertQuanPin(String name) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        StringBuffer builder = new StringBuffer();
        ArrayList<String> list=new ArrayList<String>();
        list.add("");
		char [] cha = name.toCharArray();
		for(int i = 0;i<cha.length;i++) {
            ArrayList<String> tag= (ArrayList<String>) list.clone();
            try {
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(cha[i], format);
                if (null != pinyins) {
                    for (int j = 0; j < pinyins.length; j++) {
                        if (j > 0) {
                            if (pinyins[j].equals(pinyins[j - 1])) {
                                continue;
                            }
                            ArrayList<String> temp = (ArrayList<String>) tag.clone();
                            addValue(temp, pinyins[j]);
                            list.addAll(temp);
                        } else {
                            addValue(list, pinyins[j]);
                        }
                    }
                } else {//如果不是汉字，toHanyuPinyinStringArray会返回null
                    addValue(list, cha[i]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
        }
            return list.toString();
	}
	
	
	/**
	 * 汉字转简拼
	 * @param name
	 * @return
	 */
	public static String convertJianPin(String name) {
        StringBuffer builder = new StringBuffer();
        ArrayList<String> list=new ArrayList<String>();
        list.add("");
        for (int i = 0; i < name.length(); i++) {
            ArrayList<String> tag= (ArrayList<String>) list.clone();
            char ch = name.charAt(i);
            String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(ch);
            if (null != pinyins) {
                for (int j=0;j<pinyins.length;j++){
                    if(j>0){
                        if(pinyins[j].charAt(0)==pinyins[j-1].charAt(0)){
                            continue;
                        }
                        ArrayList<String> temp= (ArrayList<String>) tag.clone();
                        addValue(temp, pinyins[j].charAt(0));
                        list.addAll(temp);
                    }else{
                        addValue(list,pinyins[j].charAt(0));
                    }
                }
            } else {//如果不是汉字，toHanyuPinyinStringArray会返回null
               addValue(list,ch);
            }
        }
        return list.toString();
    }

    public static void addValue(List<String> list,Object pinyin){
        for (int i=0;i<list.size();i++){
            list.set(i,list.get(i)+pinyin);
        }
    }
}
