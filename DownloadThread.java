package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Stack;


class DownloadThread implements Runnable {

	private int _interval;
	private int _errorInterval;
	private int _tryTimes;
	private int _defaultMethod;
	private Stack<Node> _nodeList;
	private Engine _engine;
	
	public DownloadThread(Engine engine, int interval, int errorInterval, int tryTimes, int defaultMethod)
	{
		_interval = interval;
		_errorInterval = errorInterval;
		_tryTimes = tryTimes;
		_defaultMethod = defaultMethod;
		_engine = engine;
	}

	public void set_nodeList(Stack<Node> _nodeList) {
		this._nodeList = _nodeList;
	}
	
	public void set_interval(int interval)
	{
		_interval = interval;
	}

	public void set_errorInterval(int errorInterval)
	{
		_errorInterval = errorInterval;
	}
	
	public void set_tryTimes(int tryTimes)
	{
		_tryTimes = tryTimes;
	}
	
	public void set_defaultMethod(int defaultMethod)
	{
		_defaultMethod = defaultMethod;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		Node node = null;
		while(_nodeList.size() > 0)
		{
			synchronized (this)
	    	{
				if(_nodeList.size() > 0)
				{
					node = _nodeList.pop();
				}
				else
				{
					_engine.threadDone();
					return;
				}
	    	}
			if(node != null)
			{
				int tryCount = 0;
				try {

		    		//System.out.println("sleep ");
					Thread.sleep(_interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ResultNode result;
				if(node.downloadMethod == 1)
					result = getContent1(node);
				else if(_defaultMethod == 0)
					result = getContent(node);
				else
					result = getContent1(node);
				while(result.errorId > 0 && tryCount < _tryTimes){

					++tryCount;
					try {

			    		//System.out.println("sleep ");
						Thread.sleep(_errorInterval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(node.downloadMethod == 1)
						result = getContent1(node);
					else if(_defaultMethod == 0)
						result = getContent(node);
					else
						result = getContent1(node);
				};
				if(result.errorId > 0 )
				{
					_engine.callback(node,result);
				}
				else
				{
					_engine.callback(node,result);
				}
			}
			else
			{
				_engine.threadDone();
				return;
			}
		}
		
		_engine.threadDone();
	}

	private ResultNode getContent(Node node){
		URL url = null;
		try
		{
			url = new URL(node.url);
		}
		catch( MalformedURLException e)
		{
            e.printStackTrace();

    		System.out.println("bad url: " + node.url);
			return null;
		}
		ResultNode res = new ResultNode();
        StringBuffer builder = new StringBuffer();
 
        int responseCode = -1;
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);
 
            responseCode = con.getResponseCode();
 
            if (responseCode == -1) {
        		System.out.println("connection failed" + url.toString());
        		res.errorId = ResultNode.ERROR_CONNECT_FAILED;
        		con.disconnect();
                return res;
            }
 
            if (responseCode >= 400) {
        		System.out.println("request failed" + url.toString());
        		res.errorId = ResultNode.ERROR_REQUEST_FAILED;
        		con.disconnect();
                return res;
            }
 
            InputStream is = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
 
            String str = null;
            while ((str = br.readLine()) != null)
                builder.append(str);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            
    		System.out.println("IOException: " + url.toString());
    		res.errorId = ResultNode.ERROR_UNKNOWN_FAILED;
        } finally {
            con.disconnect();
        }
        res.page = builder.toString();
        return res;
    }
	

	private ResultNode getContent1(Node node)
	{
		URL url = null;
		try
		{
			url = new URL(node.url);
		}
		catch( MalformedURLException e)
		{
            e.printStackTrace();

    		System.out.println("bad url: " + node.url);
			return null;
		}
		ResultNode res = new ResultNode();
		String line = null;
		StringBuffer document = new StringBuffer();
		try 
        {
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            while ((line = reader.readLine()) != null)
            	document.append(line);
            reader.close();
        }
        catch (MalformedURLException e) 
        {
            e.printStackTrace();
            res.errorId = ResultNode.ERROR_REQUEST_FAILED;
        }
        catch (IOException e)
        {
            e.printStackTrace(); 
            res.errorId = ResultNode.ERROR_CONNECT_FAILED;
        }
		res.page = document.toString();
		return res;
	}
}
