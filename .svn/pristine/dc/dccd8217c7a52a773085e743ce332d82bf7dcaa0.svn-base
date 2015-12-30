package cn.com.choicesoft.activity;


import android.content.SharedPreferences;
import cn.com.choicesoft.impl.DishDataManager;
import cn.com.choicesoft.impl.DishDataManagerImpl;
import android.app.Application;
import android.content.Context;
import com.example.pc700demo.SerialPort;
import com.example.pc700demo.SerialPortFinder;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * ผฬณะ AppliCation
 */
public class ChoiceApplication extends Application {

	
	public String radioBtnValue = "1";

	public DishDataManager mDishDataManager = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}


	public DishDataManager getDishDataManager(Context pContext) {

		mDishDataManager = DishDataManagerImpl.getDishDataManager(pContext);
		return mDishDataManager;
	}

    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPort mSerialPort = null;

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
			/* Read serial port parameters */
            SharedPreferences sp = getSharedPreferences("android_serialport_api.sample_preferences", MODE_PRIVATE);
            String path = sp.getString("serialPort", "");
            int baudrate = Integer.decode(sp.getString("baudrate", "-1"));

			/* Check parameters */
            if ( (path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

			/* Open the serial port */
            mSerialPort = new SerialPort(path, baudrate, 0);
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

}
