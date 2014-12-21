package br.com.agrego.sys.exception;


public class MyException extends Exception  {

	private static final long serialVersionUID = 1L;

	private int notification;
	
	public MyException(String message) {
		super(message);
	}

	public MyException(int notification, String message) {
		super(message);
		this.notification = notification;
	}

	public int getNotification() {
		return notification;
	}

	public void setNotification(int notification) {
		this.notification = notification;
	}

}
