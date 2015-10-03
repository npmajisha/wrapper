import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.io.Files;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4"
			+ "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	private static String[] CRAWL_DOMAIN = { "http://losangeles.eventful.com/events/" };

	/**
	 * This method receives two parameters. The first parameter is the page in
	 * which we have discovered this new url and the second parameter is the new
	 * url. You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic). In this example,
	 * we are instructing the crawler to ignore urls that have css, js, gif, ...
	 * extensions and to only accept urls that are present in the crawl domain
	 */

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		return !urlFilter(href);
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();

			downloadPage(url, html);

			Set<WebURL> links = htmlParseData.getOutgoingUrls();

			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());
		}
	}

	private void downloadPage(String url, String html) {
		String storageFolderPath = new File(myController.getConfig().getCrawlStorageFolder()).getAbsolutePath();
		String filename = url.contains("?")
				? storageFolderPath + "/" + url.substring(url.lastIndexOf("/"), url.indexOf("?")) + ".html"
				: storageFolderPath + "/" + url.substring(url.lastIndexOf("/")) + ".html";
		try {
			Files.write(html.getBytes(), new File(filename));
			System.out.println("Stored:" + url);
		} catch (IOException iox) {
			System.out.println("Failed to write file: " + filename + iox);
		}
	}

	// Returns true if URL has to be filtered
	private boolean urlFilter(String url) {
		if (!FILTERS.matcher(url).matches()) {
			for (String domainUrl : CRAWL_DOMAIN) {
				if (url.startsWith(domainUrl)) {
					return false;
				}
			}
		}
		return true;
	}

}
