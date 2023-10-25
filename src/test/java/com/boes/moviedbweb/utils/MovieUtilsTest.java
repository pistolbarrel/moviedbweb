package com.boes.moviedbweb.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovieUtilsTest {

    @Test
    void splitAndGroomNames() {
        String input = "One; Two; ; ; Three  ; Four";
        String[] out = MovieUtils.splitAndGroomNames(input);

        int expectedLength = 4;
        assertEquals(expectedLength, out.length);

        List<String> listOut = Arrays.asList(out);
        assertTrue(listOut.contains("One"));
        assertTrue(listOut.contains("Two"));
        assertTrue(listOut.contains("Three"));
        assertTrue(listOut.contains("Four"));
    }
}