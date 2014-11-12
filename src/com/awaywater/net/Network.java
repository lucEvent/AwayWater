package com.awaywater.net;

public interface Network {

	public static final int RESULT_OK = 1;
	public static final int RESULT_ERROR = 2;
	public static final int RESULT_OTHER = 3;

	public void message(int resultCode, int requestCode, Object result);
}
