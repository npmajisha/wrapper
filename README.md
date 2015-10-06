# README #

### What is this repository for? ###

This is a wrapper implementation for the site http://losangeles.eventful.com/events. A wrapper is basically used to extract semi-structured data from the webpages. crawler4j was used to scrape web pages and the web pages are stored locally. In the wrapper implementation Beautiful soup is used to extract relevant information about events, which is stored in json format.

Data that is extracted from these pages are:
-Event Id

-Event Location

-Event Time

-Event Category

-Performers

-Ticket price(if any)

### How do I get set up? ###

You can run the crawler by passing the following arguments:

1. UserAgent

2. number of parallel crawler threads

3. path to crawl storage folder

You can run the EventWrapper by passing the following arguments:

1.path to the crawl storage folder

2.name of the output file where extracted data is dumped in json format
