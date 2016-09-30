package com.murach.newsreader;

import android.util.Log;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

/** This class is used to parse the XML file
 * Is SAX parser's event handler*/
public class RSSFeedHandler extends DefaultHandler {
    // define model objects
    RSSFeed feed;
    RSSItem item;

    // used to determine when the various elements of the XML file are being parsed
    boolean feedTitleHasBeenRead = false;
    boolean feedPubDateHasBeenRead = false;
    
    boolean isTitle = false;
    boolean isDescription = false;
    boolean isLink = false;
    boolean isPubDate = false;
    
    RSSFeedHandler() {}

    /** Returns RSSFeed object that was constructed from XML when SAX finished parsing it */
    public RSSFeed getFeed() {
        return feed;
    }

    // executed when parser starts reading the XML document
    public void startDocument() throws SAXException {
        // instantiate model objects so that they can be used by other methods in this class
        feed = new RSSFeed();
        item = new RSSItem(); // an item to temporarily store feed data
    }

    @Override
    public void endDocument() throws SAXException { }

    // Executed after parser reads a start element such as <item>
    @Override
    public void startElement(String namespaceURI, String localName, 
            String qName, Attributes atts) throws SAXException {
        Log.i("RSSHandler", "startElement()\t"+qName);
        // qName i qualified name of the element (if tag was <item> then qName will be "item")

        // depending on passed element name check whether this element is needed by the News Reader app
        if (qName.equals("item")) {
            // create a new item
            item = new RSSItem();
            return;
        }
        else if (qName.equals("title")) {
            isTitle = true;
            return;
        }
        else if (qName.equals("description")) {
            isDescription = true;
            return;
        }
        else if (qName.equals("link")) {
            isLink = true;
            return;
        }
        else if (qName.equals("pubDate")) {
            isPubDate = true;
            return;
        }
    }

    // executed after the parser reads an end elements such as </item>
    public void endElement(String namespaceURI, String localName, 
            String qName) throws SAXException
    {
        Log.i("RSSHandler", "endElement()\t\t"+qName);
        // add current item to the feed
        if (qName.equals("item")) {
            feed.addItem(item);
            Log.d("RSSFeedHandler",item.toString());
            return;
        }
    }

    // Executed when parser reads the characters within an element (<item>This will trigger characters()</item>)
    @Override
    public void characters(char ch[], int start, int length)
    {
        String s = new String(ch, start, length);
//        Log.i("RSSHandler", "s: " + s);
        Log.e("RSSHandler", "characters()\t\t\t"+s);

        // store this string in the appropriate RSSFeed or RSSItem
        /* Feed and each item have both <title> and <pubDate> elements
         <channel>
            <title><![CDATA[CNN.com - RSS Channel - Tech]]></title>
            <item>
                <title><![CDATA[Samsung recalls all Galaxy Note 7 phones]]></title>
            ...
         Thus for these elements check if title has already been read.
         If not - set the title in the RSSFeed object
         else set the title in the RSSItem object
         */
        if (isTitle) {
            Log.d("RSSHandler","isTitle");
            if (feedTitleHasBeenRead == false) {
                // title has not been read before
                // => this belong to RSSFeed
                feed.setTitle(s);
                feedTitleHasBeenRead = true;
            } 
            else {
                // title was read before
                // => this belongs to RSSItem
                item.setTitle(s);
            }
            isTitle = false;
        }
        else if (isLink) {
            Log.d("RSSHandler","isLink");
            item.setLink(s);
            isLink = false;
        }
        else if (isDescription) {
            Log.d("RSSHandler","isDescription");
            if (s.startsWith("<")) {
                item.setDescription("No description available.");
            }
            else {
                item.setDescription(s);
            }
            isDescription = false;
        }
        else if (isPubDate) {
            Log.d("RSSHandler","isPubDate");
            if (feedPubDateHasBeenRead == false) {
                feed.setPubDate(s);
                feedPubDateHasBeenRead = true;
            }
            else {
                item.setPubDate(s);
            }
            isPubDate = false;
        }        
    }

    /* How it Works?
    * 1. startDocument() is called, where new feed and item objects are instantiated
    * 2. startElement() is called for each tag, followed by characters() and endElement()
    *
    * For example:
    * startElement() is called, qName = "description"
    * isDescription is set to true in our if-else clauses
    * characters() is called, else if (isDescription) is triggered
    * we know that if first character inside <description> tag is "<"
    * then there is no description text available.
    * Else item.setDescription(s);
    * In the end set isDescription to false, so it wont trigger again for this item.
    * endElement() is called. If QName is "item" then we finished parsing 1 item
    * => add it to feed's list
    * . . . parse more items . . .
    * endDocument() called. Parsing finished, we can use getFeed */

}