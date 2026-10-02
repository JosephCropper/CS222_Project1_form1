package org.example;


import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {
        searchWikiFor("Sleep Token");
        searchWikiFor("Java");
        searchWikiFor("Java (software)");

    }


    //Sends Search request to wikimedia
    public static void searchWikiFor(String articleTitle) {
        System.out.println("\nSearching for " + articleTitle + "...");
        String url = encodeSearchToUrl(articleTitle);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("User-Agent", "JcropperCS222App, joseph.cropper@bsu.edu")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //System.out.println("Response JSON: \n" + response.body());
            extractRedirectInformation(response.body());

        } catch (Exception e) {
            System.err.println("Error"
                    + "\nLocation: Main, searchWikiFor"
                    + "\nResponse to JSON request failed\n"
                    + e);

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


    public static void extractRedirectInformation(String jsonText){
        extractInformation(jsonText, "redirects\":[{", "revisions\":[{", "\"title\":\"", "Redirect");
    }


    //todo
    //rename the things from redirect to a more neutral word
    public static void extractInformation(String jsonText, String startSearch,
                                          String endSearch, String keyword,
                                          String purpose) {
        int startSubstring = jsonText.indexOf(startSearch);
        int endSubstring = jsonText.indexOf(endSearch);

        if (startSubstring == -1) {
            System.out.println("No " + purpose + " Detected");
        }
        else{
            String redirectSubstring = jsonText.substring((startSubstring+10), endSubstring);

            int indexOfRedirect = 0;
            while (indexOfRedirect != -1){

                indexOfRedirect = redirectSubstring.indexOf(keyword);
                if (indexOfRedirect != -1){
                    redirectSubstring = redirectSubstring.substring((indexOfRedirect+9));

                    indexOfRedirect = redirectSubstring.indexOf("\"");
                    System.out.println(purpose + ": " + redirectSubstring.substring(0, indexOfRedirect));

                    redirectSubstring = redirectSubstring.substring(indexOfRedirect);

                }
            }
        }

    }

}

