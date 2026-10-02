# PROGRAM EXPLANATION

## Classes:
- Main

## Main:
###   public static void main(String[] args)
Initializes the scanner import for input and starts primary loop in a while loop on a boolean switch

Prompts user for article name, and immediately checks for empty entry.
- If entry is not empty, runs searchWikiFor with the input, and set loop to the return value.
- If entry is empty, breaks loop and program finishes with a System.err message

###   public static boolean searchWikiFor(String articleTitle) 
Encodes articleTitle to a proper URL format for safe searching in a String format, stores it in a new string

Initializes get request to MediaWiki with the encoded url string and establishes return format JSON

TRY to send request to MediaWiki, stores the response

{

IF if the search is deemed valid by isValidSearch

{

runs extractInformation with response data and appropriate information to print Redirect information

runs extractInformation with response data and appropriate information to print Revision information

and return true, as the search request is valid and the program may continue.

}

if the code post "if" statement runs, then the return true never hit, and thus isValidSearch would have needed deem the search invalid, thus returns false

}

CATCH{ any error and display the error location }

One final return false at the end as a catch-all failsafe.

###     public static String encodeSearchToUrl(String search) {
Encodes search to url-safe format for request sending, returns.

###     public static void extractRedirectInformation(String jsonText),     public static void extractRevisionInformation(String jsonText)
runs extractInformation with the proper formatting to print redirect and revision information respectively

###     public static boolean isValidSearch(String jsonText)
Checks jsonText for "missing" keyword to see if the MediaWiki response states if no article was found, if "missing" is found, then returns false, else true.

###   public static void extractInformation(String jsonText, String startSearch, String endSearch, String keyword, String purpose) 
purpose dictates the few switches between "Redirect" and "Revision" in this function

startSearch dictates the beginning keyword of the substring for the data needing parsed 

endSearch dictates the ending keyword of the substring for the data needing parsed

keyword dictates the keyword before the actual data wanting to be parsed (ie "title" for redirct, "user" for revision)

The rest of the function works as such:
- snip the original string into a substring that has the information only pertaining to the needed information (given by startsearch and endsearch)
- search for the keyword within the substring, and find the index of the soonest location of it
- snip the substring of everything up to the data wanted (Note: most data is held within quotations "". if you snip the substring all the way up to the data (time:"2026" -> 2026"), the next quotation can be used as a benchmark for the length of the data itself, letting us retrieve the data specifically no matter the length with a uniform piece of code. this is how we:)
- take the exact data from the substring and print it as required in formatting, then snip the data from the substring
- continue in a loop from searching for the keyword over and over until no keywords remain, in which end the loop and function
  
