# CS222_Project1_form1

Created by Joseph Cropper

--------
Project function:
- Until the user enters in an invalid journal entry:
- The user is prompted to enter the name of a wikipedia journal entry
- The program sends a get request to WikiMedia API and obtains response
- If the user was redirected, the redirect is labelled immediately
- The program lists out the 15 most recent revisions made to the searched journal, naming the editor and the time edited in ISO 8601 format and UTC given time


# PROGRAM EXPLANATION

## Classes:
- Main

## Main:
###   public static void main(String[] args)
Runs "run" function. differentiated for testing purposes. 

### public static boolean run(String code)
Initializes scanner and loop. Immediately takes user input, checks if its empty. If its empty, the program cuts. Otherwise, calls searchWikiFor with the input.
"code" allows for testing differentials, where the input can be manually entered in in a test. This is also why there's a return boolean.

###   public static boolean searchWikiFor(String articleTitle) 
Return affects the run loop, true loops again, false breaks.
Main Driver. 
Encodes articleTitle to a proper URL format for safe searching in a String format using encodeSearchToUrl, stores it in a new string, Initializes get request to MediaWiki with the encoded url string and establishes return format JSON.

TRY to send request to MediaWiki, stores the response

{

IF if the search is deemed valid by isValidSearch

{

runs extractInformation with response data and appropriate information to print Redirect information, prints result

runs extractInformation with response data and appropriate information to print Revision information, prints result

and return true, as the search request is valid and the program may continue.

}

if the code post "if" statement runs, then the return true never hit, and thus isValidSearch would have needed deem the search invalid, thus returns false

}

CATCH{ any error and display the error location }

One final return false at the end as a catch-all failsafe.

###     public static String encodeSearchToUrl(String search) {
Encodes search to url-safe format for request sending, returns.


###     public static boolean isValidSearch(String jsonText)
Checks jsonText for "missing" keyword to see if the MediaWiki response states if no article was found, if "missing" is found, then returns false, else true.

###   extractInformation
Initializes a few key variables depending on which function it needs to run, namely a startSubstring, endSubstring, and keyword

The startSubstring seeks the initial keyword for searching based on the function, and checks to see if it appears. If it does, since all the data in the JSON follows it, it snips the input string to everything after it to the end of the endSubstring, which searches for a specific keyword based on the function that prevents the substring from reading data that may not apply.

From this point, a loop runs through the function:
- Search for the index of the keyword, a keyword for each individual function. (user:"editorOne")
- Cuts to just past the keyword, to directly expose the data(editorOne")
- Uses the quotation mark that follows raw data as a measure to extract the raw data itself (editorOne is added to the output)
- snips past the data and looks for the next iteration of the keyword, if none appear, the loop ends.
