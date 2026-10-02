package org.example;


import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {
    }


    public void searchWikiFor(String articleTitle){

        String encodedTitle = URLEncoder.encode(articleTitle, StandardCharsets.UTF_8);

        String url = "https://en.wikipedia.org/w/api.php"
                + "?action=query"
                + "&format=json"
                + "&titles=" + encodedTitle //search for this title
                + "&prop=redirects%7Crevisions" //get redirects and revisions
                + "&rvprop=ids%7Ctimestamp%7Cuser"
                + "&formatversion=2"; //revisionDetails

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("User-Agent", "JcropperCS222App, joseph.cropper@bsu.edu")
                .build();


        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response JSON: \n" + response.body());

            //todo:
            //find "redirects" and the "title":" to get the redirect page title
            //find "revisions", number of times they occur, "user":" for each, "timestamp":" for each

        }
        catch (Exception e){
            System.err.println("Error"
                    + "\nLocation: Main, main"
                    + "\nResponse to JSON request failed");

        }
    }

}