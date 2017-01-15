package com.greenkey.weighttracker;

import com.greenkey.weighttracker.entity.helper.TallHelper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void weightHelperIsCorrect() throws Exception {
        /*float[] requests = {148f, 181f, 206f};
        String[] trueAnswers = {"4' 10\"", "5' 11\"", "6' 9\""};

        String[] response = new String[requests.length];
        for (int i = 0; i < requests.length; i++) {
            response[i] = TallHelper.convertToUnitSystem(requests[i]);
        }

        assertArrayEquals(response, trueAnswers);*/
    }


    @Test
    public void tallHelperIsCorrect() throws Exception {
        float[] requests = {148f, 181f, 206f};
        String[] trueAnswers = {"4' 10\"", "5' 11\"", "6' 9\""};

        String[] response = new String[requests.length];
        for (int i = 0; i < requests.length; i++) {
            response[i] = TallHelper.convertToUnitSystem(requests[i], TallHelper.ENGLISH_SYSTEM_INDEX);
        }

        assertArrayEquals(response, trueAnswers);
    }


}