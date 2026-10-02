package Io;

import java.util.Scanner;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        while (true){
            System.out.println("Enter article name: ");
            String input = scan.nextLine();
            if (input == ""){
                System.err.println("No article entered. Closing...");
                break;
            }
            else{
                searchWikiFor(input);
            }
        }
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
            extractRevisionInformation(response.body());

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
                + "&redirects=true"
                + "&prop=revisions"
                + "&rvprop=ids%7Ctimestamp%7Cuser"
                + "&rvlimit=15"
                + "&formatversion=2"); //revisionDetails

    }



    public static void extractRedirectInformation(String jsonText){
        extractInformation(jsonText, "redirects\":[{", "revisions\":[{", "\"to\":\"", "Redirect");
    }

    public static void extractRevisionInformation(String jsonText){
        extractInformation(jsonText,  "revisions\":[{", "}]}]}}", "\"user\":\"", "Revision");
    }


    //todo
    //rename the things from redirect to a more neutral word
    public static void extractInformation(String jsonText, String startSearch,
                                          String endSearch, String keyword,
                                          String purpose) {
        int startSubstring = jsonText.indexOf(startSearch);
        int endSubstring = jsonText.indexOf(endSearch);

        //changes index for print substrings depending on the information being presented and known patterns
        int bound = 0;
        if (purpose == "Redirect"){
            bound = 6;
        }
        else if (purpose == "Revision"){
            bound = 8;
        }


        if (startSubstring == -1) {
            System.out.println("No " + purpose + " Detected");

        }
        else{

            String textSubstring = jsonText.substring((startSubstring+10), endSubstring);
            int storedIndexOf = 0;

            while (storedIndexOf != -1){

                storedIndexOf = textSubstring.indexOf(keyword);

                if (storedIndexOf != -1){

                    textSubstring = textSubstring.substring((storedIndexOf+bound));
                    storedIndexOf = textSubstring.indexOf("\"");
                    System.out.println(purpose + ": " + textSubstring.substring(0, storedIndexOf));

                    if (bound == 8){

                        int timeIndex = textSubstring.indexOf("timestamp\":\"");
                        textSubstring = textSubstring.substring((timeIndex+12));
                        timeIndex = textSubstring.indexOf("\"");

                        System.out.println("Time: " + textSubstring.substring(0, timeIndex));
                    }

                    textSubstring = textSubstring.substring(storedIndexOf);

                }
            }
        }
    }
}

