package cn.com.choicesoft.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

/**
 * 日志处理
 */
public class CSLog {
    private static Boolean LOG_SWITCH =true; // 日志文件总开关
	private static Boolean LOG_WRITE_TO_FILE =true;// 日志写入文件开关
	private static char LOG_TYPE ='v';// 输入日志类型，w代表只输出告警信息等，v代表输出所有信息
    private static String LOG_PATH_SDCARD_DIR= Environment.getExternalStorageDirectory()+"/data/cn.com.choicesoft.Pad/log/";// 日志文件在sdcard中的路径
    private static String LOG_PATH_MEMORY_DIR="/data" + Environment.getDataDirectory().getAbsolutePath() + "/cn.com.choicesoft/log/";//日志文件在内存中的路径(日志文件在安装目录中的路径)="/sdcard/data/cn.com.choicesoft.phone/log";// 日志文件在sdcard中的路径
    private static int SDCARD_LOG_FILE_SAVE_DAYS = 7;// sd卡中日志文件的最多保存天数
	private static String LOGFILENAME = "ChoiceLog.txt";// 本类输出的日志文件名称
	private static SimpleDateFormat logSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格式
	private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式

	public static void w(String tag, Object msg) { // 警告信息
		log(tag, msg.toString(), 'w');
	}

	public static void e(String tag, Object msg) { // 错误信息
		log(tag, msg.toString(), 'e');
	}

	public static void d(String tag, Object msg) {// 调试信息
		log(tag, msg.toString(), 'd');
	}

	public static void i(String tag, Object msg) {//
		log(tag, msg.toString(), 'i');
	}

	public static void v(String tag, Object msg) {
		log(tag, msg.toString(), 'v');
	}

	public static void w(String tag, String text) {
		log(tag, text, 'w');
	}

	public static void e(String tag, String text) {
		log(tag, text, 'e');
	}

	public static void d(String tag, String text) {
		log(tag, text, 'd');
	}

	public static void i(String tag, String text) {
		log(tag, text, 'i');
	}

	public static void v(String tag, String text) {
		log(tag, text, 'v');
	}

	/**
	 * 根据tag, msg和等级，输出日志
	 * 
	 * @param tag
	 * @param msg
	 * @param level
	 * @return void
	 * @since v 1.0
	 */
	private static void log(String tag, String msg, char level) {
		if (LOG_SWITCH) {
			if ('e' == level && ('e' == LOG_TYPE || 'v' == LOG_TYPE)) { // 输出错误信息
				Log.e(tag, msg);
			} else if ('w' == level && ('w' == LOG_TYPE || 'v' == LOG_TYPE)) {
				Log.w(tag, msg);
			} else if ('d' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
				Log.d(tag, msg);
			} else if ('i' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
				Log.i(tag, msg);
			} else {
				Log.v(tag, msg);
			}
			if (LOG_WRITE_TO_FILE)
				writeLogtoFile(String.valueOf(level), tag, msg);
		}
	}

	/**
	 * 打开日志文件并写入日志
	 * 
	 * @return
	 * **/
	private static void writeLogtoFile(String logtype, String tag, String text) {// 新建或打开日志文件
		Date nowtime = new Date();
		String needWriteFiel = logfile.format(nowtime);
		String needWriteMessage = logSdf.format(nowtime) + "    " + logtype+ "    " + tag + "    " + text;
		File f=new File(getCurrLogType());
        if(!f.exists()){
            f.mkdirs();
        }
		try {
            File file=new File(f, needWriteFiel+ LOGFILENAME);
            if(!file.exists()){
                file.createNewFile();
                delFile();
            }
			FileWriter filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
			BufferedWriter bufWriter = new BufferedWriter(filerWriter);
			bufWriter.write(needWriteMessage);
			bufWriter.newLine();
			bufWriter.close();
			filerWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除制定的日志文件
	 * */
	public static void delFile() {// 删除日志文件
		File file = new File(getCurrLogType());
        File [] files=file.listFiles();
        try {
            for(int i=0;i<files.length;i++){
                String fileName=files[i].getName();
                String fileDate=fileName.substring(0,fileName.indexOf(LOGFILENAME));
                if(logfile.parse(fileDate).compareTo(getDateBefore())<=0){
                    if (files[i].exists()) {
                        files[i].delete();
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
	}

	/**
	 * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
	 * */
	private static Date getDateBefore() {
		Date nowtime = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowtime);
		now.set(Calendar.DATE, now.get(Calendar.DATE)- SDCARD_LOG_FILE_SAVE_DAYS);
		return now.getTime();
	}

    public static String getCurrLogType(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return LOG_PATH_MEMORY_DIR;
        }else{
            return LOG_PATH_SDCARD_DIR;
        }
    }

}
