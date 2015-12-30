package cn.com.choicesoft.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * 数据库管理工具
 */
public class DBManager {
	/**
	 * sd卡数据库复制本地
	 */
	private final int BUFFER_SIZE = 1024;
	public static final String DB_NAME = "BookSystem.sqlite"; // 保存的数据库文件名
	public static final String PACKAGE_NAME = "cn.com.choicesoft";// 包名
	public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME + "/databases"; // 在手机里存放数据库的位置

	private SQLiteDatabase database;
	private Context context;

	public DBManager(Context context) {
		this.context = context;
	}

	/**
	 * 打开数据库
	 * 
	 * @return
	 */
	public SQLiteDatabase openDatabase() {
		this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
		return this.database;
	}

	/**
	 * 打开数据库
	 * 
	 * @param dbfile
	 *            应用数据库目录
	 * @return
	 */
	public SQLiteDatabase openDatabase(String dbfile) {
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			if (!(new File(dbfile).exists())) {// 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
				File dir = new File(DB_PATH);
				if (!dir.exists()) {// 如果文件夹不存在
					dir.mkdir();
				}
				Log.i("tag", "数据库文件不存在!");
				// 欲导入的数据库
				char[] buffer = new char[BUFFER_SIZE];
				// TODO 源数据库的存放位置
				String filePath = Environment.getExternalStorageDirectory()+"/BookSystem.sqlite";
				// 检查文件是否存在
				if (!new File(filePath).exists()) {
					Log.i("tag", " 数据库文件未找到!" + Environment.getExternalStorageDirectory().getAbsolutePath());
					throw new FileNotFoundException();
					// return null;
				} else {
					// 需用字节流
					File f=new File(filePath);
					String[] xx=f.list();
					fis = new FileInputStream(new File(filePath));
					fos = new FileOutputStream(new File(dbfile));
					byte[] buf = new byte[BUFFER_SIZE];
					int len = 0;
					while ((len = fis.read(buf, 0, buf.length)) != -1) {
						System.out.println(new String(buffer, 0, len));
						fos.write(buf, 0, len);
					}

				}
			}
			// 打开创建数据库
			Log.i("tag", "数据库路径为:" + dbfile);
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
			if (db == null) {
				Log.i("tag", "openDatabase 为空");
			}
			return db;
		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		} finally {
			try {
				if (null != fos) {
					fos.close();
				}
				if (null != fis) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
