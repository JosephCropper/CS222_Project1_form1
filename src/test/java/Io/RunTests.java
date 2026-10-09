package Io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static Io.Main.*;

public class RunTests {


        @Test
        public void isValidSearchFindsMissingTrue(){
            String test = "missing person";
            Assertions.assertTrue(isValidSearch(test));
        }

        @Test
        public void isValidSearchFindsMissingFalse(){
            String test = "amissing\":truea";
            Assertions.assertFalse(isValidSearch(test));
        }

         @Test
        public void extractInformationHandlesRedirect() {
            String mockJson = "redirects\":[{\"to\":\"Frank Zappa\"}]revisions\":[{";
            String expected = "\nRedirected to Frank Zappa\n";
            Assertions.assertEquals(expected, extractInformation(mockJson, "Redirect"));
        }

        @Test
        public void extractInformationHandlesRevision() {
            String mockJson = "revisions\":[{\"user\":\"TestUser\",\"timestamp\":\"2026-10-02T12:00:00Z\"}]}]}}";
            String expected = "\n1  2026-10-02T12:00:00Z  TestUser";
            Assertions.assertEquals(expected, extractInformation(mockJson, "Revision"));
        }
    }


