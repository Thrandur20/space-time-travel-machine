package com.tudor.dodonete.spacetimetravelmachine.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PgiGeneratorImpl implements PgiGenerator {
    private static final Random randomSize = new Random();

    @Override
    public String createNewRandomUniquePGI() {
        int randomLetter = (int) (Math.random() * 52);
        char base = (randomLetter < 26) ? 'A' : 'a';
        char randChar = (char) (base + randomLetter % 26);
        int min = 4;
        int max = 10;
        int result = randomSize.nextInt(max - min) + min;
        String value = RandomStringUtils.randomAlphanumeric(result);
        return randChar + value;
    }
}
