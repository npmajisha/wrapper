import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class CrawlerController {
public static void main(String[] args) throws Exception {
		
		
		String userAgentIdentifier = args[0];
		int numberOfCrawlers = Integer.parseInt(args[1]);
		String crawlStorageFolder = args[2];
		int maxDepthOfCrawling = 4;

		String[] seedUrls = { "http://losangeles.eventful.com/events/categories"};

		CrawlConfig config = new CrawlConfig();
		config.setUserAgentString(userAgentIdentifier);
		config.setMaxDepthOfCrawling(maxDepthOfCrawling);
		config.setCrawlStorageFolder(crawlStorageFolder);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		for (String seedUrl : seedUrls) {
			controller.addSeed(seedUrl);
		}

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(Crawler.class, numberOfCrawlers);
	}

}
