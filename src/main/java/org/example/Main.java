package org.example;


import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {
        searchWikiFor("Java");
    }


    //Sends Search request to wikimedia
    public static void searchWikiFor(String articleTitle) {

        String url = encodeSearchToUrl(articleTitle);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("User-Agent", "JcropperCS222App, joseph.cropper@bsu.edu")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response JSON: \n" + response.body());
            extractRedirectInformation(response.body());

        } catch (Exception e) {
            System.err.println("Error"
                    + "\nLocation: Main, searchWikiFor"
                    + "\nResponse to JSON request failed");

        }

    }


    //Encodes title to a URL for safe sending to WikiMedia
    public static String encodeSearchToUrl(String search) {
        String encodedTitle = URLEncoder.encode(search, StandardCharsets.UTF_8);

        return ("https://en.wikipedia.org/w/api.php"
                + "?action=query"
                + "&format=json"
                + "&titles=" + encodedTitle //search for this title
                + "&prop=redirects%7Crevisions" //get redirects and revisions
                + "&rvprop=ids%7Ctimestamp%7Cuser"
                + "&formatversion=2"); //revisionDetails

    }


    //Extract Redirects
    public static void extractRedirectInformation(String jsonText) {
        int startSubstring = jsonText.indexOf("redirects\":[{");
        int endSubstring = jsonText.indexOf("revisions\":[{");

        if (startSubstring == -1) {
            System.out.println("No Redirects Detected");
        }
        else{
            String redirectSubstring = jsonText.substring((startSubstring+10), endSubstring);

            int indexOfRedirectTitle = 0;
            while (indexOfRedirectTitle != -1){

                indexOfRedirectTitle = redirectSubstring.indexOf("\"title\":\"");
                redirectSubstring = redirectSubstring.substring((indexOfRedirectTitle+9));
                System.out.println(redirectSubstring);

                indexOfRedirectTitle = -1;
            }
        }
    }
}

