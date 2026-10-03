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
        run("fromMain");
        System.exit(0);
    }



    public static boolean run(String code){
        Scanner scan = new Scanner(System.in);
        boolean loop = true;
        String input;

        while (loop) {
            if (code.equals("fromMain")) {

                System.out.println("Enter article name: ");
                input = scan.nextLine();

            }
            else{
                input = code;

            }
            if (input.isEmpty()) {

                System.err.println("No article entered. Closing...");
                return false;

            }
            else {
                loop = searchWikiFor(input);
                if (!code.equals("fromMain")){
                    return loop;
                }
            }
        }
        return false;
    }




    //Sends Search request to wikimedia
    public static boolean searchWikiFor(String articleTitle) {

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

            System.err.println("Main main | Network error, closing..." + "\n" + e);
            return false;

        }
        try{
            if (isValidSearch(jsonResponse)) {

                System.out.print(extractInformation(jsonResponse, "Redirect"));
                System.out.println(extractInformation(jsonResponse, "Revision"));
                System.out.println("\n---------------------------\n");
                return true;

            }
            return false;

        }
        catch (Exception e) {

            System.err.println("Error"
                    + "\nLocation: Main, searchWikiFor"
                    + "\nResponse to JSON request failed\n"
                    + e);

        }

        return false;
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
            System.err.println("Invalid search, closing...");
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

