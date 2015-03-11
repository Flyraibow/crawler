package crawler;

public class BaseParser {

	protected BaseSQL _sql;
	protected Engine _engine;
	protected int _type;
	protected String _txt;
	
	public BaseParser(int type, String progressTxt)
	{
		_type = type;
		_txt = progressTxt;
	}
	
	public int getType()
	{
		return _type;
	}
	
	public void setEngine(Engine engine)
	{
		_engine = engine;
	}

	public void setSql(BaseSQL sql)
	{
		_sql = sql;
	}
	
	public String getName()
	{
		return null;
	}
	
	public void callback(Node node, ResultNode res)
	{
		//overwrite this function
	}
	
	public Node getNextNode()
	{
		Node node = new Node();
		return node;
	}
}
