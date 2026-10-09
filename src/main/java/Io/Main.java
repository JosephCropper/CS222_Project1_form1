package Io;
//
import java.util.Scanner;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class Main {

    public static void main(String[] args) {
        JavafxWindow guiWindow = new JavafxWindow();
        guiWindow.runWindow(args);
        System.exit(0);
    }

    public static String run(String input){
        if (input.isEmpty()) {
            return ("!No Input Detected!");
        }
        else {
            return searchWikiFor(input);
        }
    }

    //Sends Search request to wikimedia
    public static String searchWikiFor(String articleTitle) {

        String url = encodeSearchToUrl(articleTitle);
        String jsonResponse = "";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("User-Agent", "JcropperCS222App, joseph.cropper@bsu.edu")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            jsonResponse = response.body();

        }
        catch (Exception e) {
            return("!Network Error!");
        }
        try{
            if (isValidSearch(jsonResponse)) {
                String outputReturn = "";
                outputReturn += (extractInformation(jsonResponse, "Redirect"));
                outputReturn += (extractInformation(jsonResponse, "Revision"));
                outputReturn +=("\n---------------------------\n");
                return outputReturn;

            }
            return ("!No Wikipedia Article Detected!");
        }
        catch (Exception e) {
            return("!Issue Extracting Information!");
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

    public static boolean isValidSearch(String jsonText){
        if (jsonText.indexOf("missing\":true") != -1){
            return false;
        }
        return true;
    }



    public static String extractInformation(String jsonText, String purpose) {

        int startSubstring = -1;
        int endSubstring = -1;
        int iterations = 0;
        String keyword = "";
        String output = "";

        //changes index for print substrings depending on the information being presented and known patterns
        int bound = 0;
        if (purpose.equals("Redirect")){
            bound = 6;
            startSubstring = jsonText.indexOf("redirects\":[{");
            endSubstring = jsonText.indexOf("revisions\":[{");
            keyword = "\"to\":\"";

        }
        else if (purpose.equals("Revision")){
            bound = 8;
            startSubstring = jsonText.indexOf("revisions\":[{");
            endSubstring = jsonText.indexOf("}]}]}}");
            keyword = "\"user\":\"";

        }


        if (startSubstring != -1) {

            String textSubstring = jsonText.substring((startSubstring+10), endSubstring);
            int storedIndexOf = 0;

            while (storedIndexOf != -1){

                storedIndexOf = textSubstring.indexOf(keyword);

                if (storedIndexOf != -1){

                    textSubstring = textSubstring.substring((storedIndexOf+bound));
                    storedIndexOf = textSubstring.indexOf("\"");

                    if (bound == 6) {
                        output = output + ("\nRedirected to "  + textSubstring.substring(0, storedIndexOf) + "\n");

                    }
                    else if (bound == 8){
                        iterations++;
                        String username = textSubstring.substring(0, storedIndexOf);
                        int timeIndex = textSubstring.indexOf("timestamp\":\"");
                        textSubstring = textSubstring.substring((timeIndex+12));
                        timeIndex = textSubstring.indexOf("\"");

                        output = output + ("\n" + iterations + "  " + textSubstring.substring(0, timeIndex) + "  " +  username);

                    }

                    textSubstring = textSubstring.substring(storedIndexOf);

                }
            }
        }

        return output;

    }

}

