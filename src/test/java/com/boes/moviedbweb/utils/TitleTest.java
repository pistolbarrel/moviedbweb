package com.boes.moviedbweb.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TitleTest {

    @Test
    void extractYear() {
        String actual = Title.extractYear("Don Juan (or If Don Juan Were a Woman) (1973)");
        assertEquals("1973", actual);
    }

    @Test
    void extractTitle() {
        String actual = Title.extractTitle("Don Juan (or If Don Juan Were a Woman) (1973)");
        assertEquals("Don Juan (or If Don Juan Were a Woman)", actual);
    }

    @Test
    void extractYearEmpty() {
        String actual = Title.extractYear("Don Juan (or If Don Juan Were a Woman) ()");
        assertTrue(actual.isEmpty());
    }
}