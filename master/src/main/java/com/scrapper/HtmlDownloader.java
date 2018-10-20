package com.scrapper;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlDownloader {

    public void downloadPage(String url, String filepath) throws Exception {
        try{

            final Connection.Response response = Jsoup.connect(url).execute();
            final Document doc = response.parse();

            final File f = new File(filepath);
            FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");

        }catch(Exception e){

        }
    }

    /*public static void main(String[] args) throws Exception {

        File folder = new File("D:\\AjitV\\Links071017");
        File[] listOfFiles = folder.listFiles();

        for (File avfile: listOfFiles
             ) {

            BufferedReader in = new BufferedReader(new FileReader(avfile));
            String line = in.readLine();

            while(line!=null){

                if(!line.equals("")){

                    try{

                        String url = line.substring(line.indexOf("http"));
                        String[] temp = line.split("/");
                        String filename = temp[temp.length-1];
                        new HtmlDownloader().downloadPage(url, filename);

                    }catch(Exception e){

                    }
                }
                line = in.readLine();
            }
        }
    }*/
}
