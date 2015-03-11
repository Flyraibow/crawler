package crawler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserTool {
	
	private Matcher _matcher;

	public static Pattern getSearchReg(String preStr, String postStr)
	{
		Pattern pattern = Pattern.compile(preStr + "(.+?)" + postStr);
		return pattern;
	}
	
	public static Pattern getSearchReg(String preStr,String midStr, String postStr)
	{

		Pattern pattern = Pattern.compile(preStr + "(.+?)" + midStr + "(.+?)" + postStr);
		return pattern;
	}

	public static String removeQuote(String text, String start, String end)
	{
		String regexString = Pattern.quote(start) + "(.*?)" + Pattern.quote(end);
		Pattern pattern = Pattern.compile(regexString);
		String m[] = pattern.split(text);
		StringBuilder builder = new StringBuilder();
		for(String s : m) {
		    builder.append(s);
		}
		return builder.toString();
	}

	public static String getBetween(String text, String start, String end)
	{
		String regexString = Pattern.quote(start) + "(.*?)" + Pattern.quote(end);
		Pattern pattern = Pattern.compile(regexString);
		Matcher matcher = pattern.matcher(text);
		if(matcher.find())
			return matcher.group(1);
		else
			return null;
	}
	
	public static Matcher search(String reg, String text)
	{
		Pattern parttern = Pattern.compile(reg);
		return parttern.matcher(text);
	}
	
	public static String searchForString(String reg, String text)
	{
		Pattern parttern = Pattern.compile(reg);
		Matcher matcher = parttern.matcher(text);
		if(matcher.find())
		{
			return matcher.group(1);
		}
		return null;
	}
	
	public void setSearch(String articleReg, String text)
	{
		Pattern parttern = Pattern.compile(articleReg);
		_matcher = parttern.matcher(text);
	}
	
	public Matcher getMatcher()
	{
		return _matcher;
	}
	
	public String getResult(int group)
	{
		if(_matcher.find())
		{
			return _matcher.group(group);
		}
		else
		{
			return null;
		}
	}
}
