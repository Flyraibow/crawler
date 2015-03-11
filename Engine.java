package crawler;
import java.util.Stack;
import java.util.HashMap;

public class Engine {
	
	private int _threadNumber;
	private int _interval;
	private int _errorInterval;
	private int _tryTimes;
	private int _defaultMethod;
	private Stack<Node> _stack;
	private DownloadThread _thread;
	private int _threadCount;
	private int _state;
	private HashMap<Integer,BaseParser> _parserMap;
	
	public Engine(int threadNumber, int interval, int errorInterval, int tryTimes, int defaultMethod)
	{
		_thread = new DownloadThread(this,interval,errorInterval,tryTimes,defaultMethod);
		_threadNumber = threadNumber;
		_interval = interval;
		_errorInterval = (interval);
		_tryTimes = (tryTimes);
		_defaultMethod = (defaultMethod);
		_stack = new Stack<Node>();
		_threadCount = 0;
		set_state(0);
		_parserMap = new HashMap<Integer, BaseParser>();
	}
	
	public void pushNode(Node node)
	{
		_stack.push(node);
	}
	
	public void pushParser(BaseParser parser)
	{
		_parserMap.put(parser.getType(), parser);
		parser.setEngine(this);
	}
	
	public void removeParser(BaseParser parser)
	{
		_parserMap.remove(parser.getType());
		System.out.println("Parser " + parser.getName() + " stopped");
	}
	
	public void run()
	{
		for (BaseParser value : _parserMap.values()) {
		    Node node = value.getNextNode();
		    if(node != null)
		    {
		    	_stack.push(node);
		    }
		}
		if(_stack.size() > 0)
		{
			Stack<Node> stack = new Stack<Node>();
			stack.addAll(_stack);
			_thread.set_nodeList(stack);
			int count = _threadNumber>stack.size()?stack.size():_threadNumber;
			_threadCount = count;
			Thread[] tList = new Thread[count];
			for(int i = 0; i < count; ++i)
			{
				tList[i] = new Thread(_thread,String.valueOf(i));
				tList[i].start();
			}
			_stack.clear();
		}
		else
		{
			set_state(0);
		}
	}
	
	// one node is download
	public void callback(Node node, ResultNode res)
	{
		if(res.errorId > 0)
		{
			return;
		}
		BaseParser parser = _parserMap.get(node.type);
		if(parser != null)
		{
			parser.callback(node, res);
		}
	}
	
	// all threads complete its task;
	public void threadDone()
	{
		_threadCount--;
		if(_threadCount == 0)
		{
			run();
		}
	}
	
	public int get_interval() {
		return _interval;
	}

	public void set_interval(int interval) {
		this._interval = interval;
		_thread.set_interval(interval);
	}

	public int get_tryTimes() {
		return _tryTimes;
	}

	public void set_tryTimes(int tryTimes) {
		this._tryTimes = tryTimes;
		_thread.set_interval(tryTimes);
	}

	public int get_errorInterval() {
		return _errorInterval;
	}

	public void set_errorInterval(int errorInterval) {
		this._errorInterval = errorInterval;
		_thread.set_interval(errorInterval);
	}

	public int get_defaultMethod() {
		return _defaultMethod;
	}

	public void set_defaultMethod(int defaultMethod) {
		this._defaultMethod = defaultMethod;
		_thread.set_interval(defaultMethod);
	}

	public int get_state() {
		return _state;
	}

	public void set_state(int state) {
		this._state = state;
	}
}
