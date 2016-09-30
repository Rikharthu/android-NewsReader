package com.murach.newsreader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.util.Log;

public class FileIO {
    
//    private final String URL_STRING = "http://www.delfi.lv/rss.php";
    private final String URL_STRING = "http://rss.cnn.com/rss/cnn_tech.rss";
    private final String FILENAME = "news_feed.xml";
    private Context context = null;
    
    public FileIO (Context context) {
        this.context = context;
    }
    
    public void downloadFile() {
        try{
            // get the URL
            URL url = new URL(URL_STRING);

            // get the input stream from passed url
            InputStream in = url.openStream();
            
            // get the output stream
            FileOutputStream out = 
                context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            // MODE_PRIVATE only allows the current app to work with that file

            // read input and write output
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            while (bytesRead != -1)
            {
                out.write(buffer, 0, bytesRead);
                bytesRead = in.read(buffer);
            }
            out.close();
            in.close();
        } 
        catch (IOException e) {
            Log.e("News reader", e.toString());
        }
    }
    
    public RSSFeed readFile() {
        try {
            // 1. get the XML reader (SAX api)
            // just a boilerplate code
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            // 2. set content handler
            RSSFeedHandler theRssHandler = new RSSFeedHandler();
            xmlreader.setContentHandler(theRssHandler);

            // 3. read the file from internal storage
            FileInputStream in = context.openFileInput(FILENAME);

            // 4. parse the data
            // sax parser will use this to read from inputstream and parsing
            InputSource is = new InputSource(in);
            // start parsing file
            xmlreader.parse(is);

            // 5. set the feed in the activity
            // retrieve feed from our handler object (that was set as content handler for xmlreader)
            RSSFeed feed = theRssHandler.getFeed();
            return feed;
        } 
        catch (Exception e) {
            Log.e("News reader", e.toString());
            return null;
        }
    }
}