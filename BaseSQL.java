package crawler;

import java.sql.*;

public class BaseSQL {

	private boolean _login;
	protected Connection _conn;
	
	public BaseSQL(String usr, String pwd, String url)
	{

		set_login(false);
		
		try
		{
			Class.forName("org.postgresql.Driver");
			System.out.println("Success loading Driver!");
		} 

		catch(Exception e) 
		{
			System.out.println("Fail loading Driver!");
			e.printStackTrace();
		}

		try 
		{
			_conn = DriverManager.getConnection(url, usr, pwd);
			System.out.println("Success connecting server!");
			set_login(true);
		} 
		catch(SQLException e) 
		{
			System.out.println("Connection URL or username or password errors!");
			e.printStackTrace();
			set_login(false);
		}
	}
	
	public boolean is_login() {
		return _login;
	}
	
	private final void set_login(boolean login) {
		this._login = login;
	}
	
	public Boolean execute(String stateString)
	{
		return execute(stateString,false);
	}
	
	public Boolean execute(String stateString, boolean showError)
	{
		try {
			Statement stmt = _conn.createStatement();
			stmt.execute(stateString);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			if(showError)
			{
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public ResultSet executeQuery(String stateString)
	{
		ResultSet rs = null;
		try {
			Statement stmt = _conn.createStatement();
			rs = stmt.executeQuery(stateString);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public ResultSet executeQueryForOne(String stateString)
	{
		ResultSet rs = null;
		try {
			Statement stmt = _conn.createStatement();
			rs = stmt.executeQuery(stateString);
			if(rs.next())
			{
				return rs;
			}
			else
			{
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public static String mergeString(String a,boolean withComma)
	{
		if(a != null)
			return "'"+a.replaceAll("'", "''")+"'" + (withComma?",":"");
		else
			return "'" + a + "'"+ (withComma?",":"");
	}

	public static String mergeString(String a)
	{
		if(a != null)
			return "'"+a.replaceAll("'", "''")+"',";
		else
			return "'" + a + "',";
	}

	public static String mergeString(int a,boolean withComma)
	{
		return "'" + a + "'"+ (withComma?",":"");
	}

	public static String mergeString(int a)
	{
		return "'" + a + "',";
	}

	public static String mergeString(float a,boolean withComma)
	{
		return "'" + a + "'"+ (withComma?",":"");
	}

	public static String mergeString(float a)
	{
		return "'" + a + "',";
	}
}
