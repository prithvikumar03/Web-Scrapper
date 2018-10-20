package com.scrapper;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.scrapper.ScrapeHyperlinks;
import com.scrapper.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrapeHyperlinks {

    private HashMap<String,List<String>> linksMap;
    protected HtmlDownloader oDownloader;
    protected String downloadPath = "";

    public ScrapeHyperlinks(String url, String downloadPath) throws IOException {

       this.downloadPath=downloadPath;
       linksMap = new HashMap<>();
       oDownloader = new HtmlDownloader();

       Set<String> hyperLinks = getHyperLinks(url);
       List<String> topLevelHlinks = hyperLinks.stream().filter(s -> (s.length() > 45 && s.length() < 50)).collect(Collectors.toList());
       Iterator<String> iterator = topLevelHlinks.iterator();
       
       while(iterator.hasNext()){
           String next = iterator.next();
           System.out.println(next);

           List<String> hyperLinks1 = new ArrayList<>();
           hyperLinks1.addAll(getHyperLinks(next.substring(next.indexOf("http"),next.length()-1)));
           linksMap.put( next.substring(next.indexOf("20")), hyperLinks1);

       }
    }


    public Set<String> getHyperLinks(String inputlink) throws IOException {

        Set<String> linkset = new LinkedHashSet<String>();
        try {
            // fetch the document over HTTP
            Document doc = Jsoup.connect(inputlink).get();

            // get the page title
            String title = doc.title();
            System.out.println("title: " + title);

            // get all links in page
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                // get the value from the href attribute
                String temp = "\nlink: " + link.attr("href");
                if(temp.contains(inputlink)){
                    linkset.add(temp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linkset;

    }

    protected void downloadPage(){

        Set<String> keySet = linksMap.keySet();
        Iterator<String> iterator = keySet.iterator();

        while(iterator.hasNext()){

            String next = iterator.next();
            String fileNamePart1 = next.substring(next.indexOf("20")).replace('/', '_');
            int i=1;
            Iterator<String> linkIterator = linksMap.get(next).iterator();

            while(linkIterator.hasNext()){

                String next1 = linkIterator.next();

                if(next1.length()>46){

                    String filePath = downloadPath + fileNamePart1 + i + ".html";
                    try {
                        oDownloader.downloadPage(next1.substring(next1.indexOf("http")),filePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    i++;

                }
            }
        }
    }

    public static void main(String[] args) throws IOException {

        //arguments
        //1. Link. (All hyper links in this link will be found and content will be downloaded as html)
        //2. Path of folder where all the contents have to be downloaded
        System.out.println(args[0]+args[1]);
        ScrapeHyperlinks scrapeHyperlinks = new ScrapeHyperlinks(args[0],args[1]);
        scrapeHyperlinks.downloadPage();

    }

}
