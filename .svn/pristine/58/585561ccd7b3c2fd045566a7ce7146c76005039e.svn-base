package cn.com.choicesoft.exception;

/**
 * 自定义Exception
 *
 */
public class ServerResponseException extends Exception{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 41938692327060685L;
	
	private String errorCode;

	public ServerResponseException(String msg,String errorCode){
		super(msg);
		this.errorCode = errorCode;
	}
	
	public ServerResponseException(String msg,Throwable t,String errorCode){
		super(msg,t);
		this.errorCode = errorCode;
	}
	
	public ServerResponseException(Throwable t,String errorCode){
		super(t);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
