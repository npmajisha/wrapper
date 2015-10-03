__author__ = 'Majisha'

from bs4 import BeautifulSoup
import os
import sys
import re
import json

def main():

    for filename in os.listdir('./'+sys.argv[1]):
        if filename.startswith('E'):
            event = {}
            inputfile = open('./'+sys.argv[1]+'/'+filename, "r+")
            outputfile = open('./'+sys.argv[2],"a")

            soup = BeautifulSoup(inputfile, "html.parser")
            #get event id
            event["EventId"] = soup.find("span",{"class": "track-object"})["data-id"]
            title_tag = soup.find("h1",{"itemprop":"name"})
            #get event title
            event_title = ""
            for tag in title_tag.findAll("span"):
                event_title += tag.text+" "
            event["EventTitle"] =  event_title
            event_start_date =  soup.find("div",{"class":"event-date"})["content"]
            event["eventTime"] = {}
            event["eventTime"]["date"] = event_start_date.split("T")[0]
            event["eventTime"]["time"] = event_start_date.split("T")[1]
            event["ticketPrice"] = {}
            event_low_price = soup.find("span", {"itemprop":"lowPrice"})
            if event_low_price is not None:
                event["ticketPrice"]["low"] = event_low_price.text
            else:
                event["ticketPrice"]["low"] = "N/A"

            event_high_price = soup.find("span", {"class":"highPrice"})
            if event_high_price is not None:
                event["ticketPrice"]["high"] = event_high_price.text
            else:
                event["ticketPrice"]["high"] = "N/A"

            event_performers = []

            event_performer_list = soup.find("ul", {"class":"event-performers-list"})
            for tag in event_performer_list.findAll("span",{"itemprop":"name"}):
                event_performers.append(tag.text)
            event["performers"] = event_performers

            event_address = {}
            event_address_tag = soup.find("p", {"itemprop":"address"})
            for tag in event_address_tag.findAll("span"):
                if tag.has_attr("itemprop"):
                    if tag["itemprop"] == "streetAddress":
                        event_address["streetAddress"] = tag.text
                    elif tag["itemprop"] == "addressLocality":
                        event_address["addressLocality"] = tag.text
                    elif tag["itemprop"] == "addressRegion":
                        event_address["addressRegion"] = tag.text
                    elif tag["itemprop"] == "postalCode":
                        event_address["postalCode"] = tag.text
            event["eventLocation"] = event_address

            event["event_categories"] = []
            event_category_tag = soup.find(text = re.compile(r"Categories:")).parent
            for tag in event_category_tag.findAll("a"):
                event["event_categories"].append(tag.text)

            outputfile.write(json.dumps(event, indent=4, sort_keys=True) + " ,\n")

    return


if __name__ == '__main__':
    main()
