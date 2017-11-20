package com.sanket.itunespodcast;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


public class PodcastUtil {
    static public class PodcastPullParser {
        static ArrayList<Podcast> parseNews(InputStream in) throws XmlPullParserException, IOException {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in, "UTF-8");
            Podcast podcast = null;
            ArrayList<Podcast> podCastList = new ArrayList<Podcast>();
            int event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("entry")) {
                            podcast = new Podcast();
                        } else if (parser.getName().equals("link")) {
                            String data = parser.getAttributeValue(null, "href");
                            if (data != null && podcast != null)
                                podcast.setNewsFeedLink(data);
                        } else if (parser.getName().equals("title")) {
                            String data = parser.nextText();
                            if (data != null && podcast != null)
                                podcast.setTitle(data);
                        } else if (parser.getName().equals("summary")) {
                            String data = parser.nextText();
                            if (data != null && podcast != null) {
                                //To skip html tags from description
                                String[] text = data.split("<");
                                if (text[0].length() == 0) {
                                    podcast.setPodcastDescription("No Description available");
                                } else {
                                    podcast.setPodcastDescription(text[0].trim());
                                }
                            }
                        } else if (parser.getName().equals("im:releaseDate")) {
                            String data = parser.getAttributeValue(null, "label");
                            if (data != null && podcast != null) {
                                DateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
                                try {
                                    Date date = formatter.parse(data.trim());
                                    podcast.setUpdatedDate(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }else if (parser.getName().equals("im:image") && parser.getAttributeValue(null,"height").equals("170"))
                        {
                            String data = parser.nextText();
                            podcast.setLargeImageUrl(data);


                        }

                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("entry")) {
                            podCastList.add(podcast);
                            podcast = null;
                        }
                        break;
                }
                event = parser.next();
            }
            Collections.sort(podCastList);
            return podCastList;
        }
    }
}