package Io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static Io.Main.isValidSearch;
import static Io.Main.run;

    public class RunTests {

        @Test
        public void doesEmptyClose(){
            Assertions.assertFalse(run(""));
        }

        @Test
        public void doesNonEmptyValidContinue(){
            Assertions.assertTrue(run("Sleep Token"));
        }

        @Test
        public void doesNonEmptyNonValidStop(){
            Assertions.assertFalse(run("asdfajfasdfnjdhbfd"));
        }

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


    }


