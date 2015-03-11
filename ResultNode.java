package crawler;

public class ResultNode {
	public static final int ERROR_TIME_OUT = 1;
	public static final int ERROR_REQUEST_FAILED = 2;
	public static final int ERROR_CONNECT_FAILED = 3;
	public static final int ERROR_UNKNOWN_FAILED = 4;
	public String page;
	public int errorId;
}
