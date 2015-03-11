package crawler.html;

import crawler.ParserTool;

public class HtmlParser {
	
	public static HtmlNode parser(String page)
	{
		// remove quote
		page = ParserTool.removeQuote(page, "<!--", "-->");
		// remove javascript
		page = ParserTool.removeQuote(page, "<script", "/script>");
		page = ParserTool.removeQuote(page, "<!", ">");
		HtmlNode node = new HtmlNode(page);
		return node;
	}
}
