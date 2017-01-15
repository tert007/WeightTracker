package com.greenkey.weighttracker;

import com.greenkey.weighttracker.entity.helper.TallHelper;
import com.greenkey.weighttracker.entity.helper.WeightHelper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void weightHelperIsCorrect1() throws Exception {
        int result = WeightHelper.getSecondPartOfValue(165.4563f);

        assertEquals(result, 5, 0.00f);
    }

    @Test
    public void weightHelperIsCorrect11() throws Exception {
        int result = WeightHelper.getSecondPartOfValue(0.0555f);

        assertEquals(result, 1, 0.00f);
    }


    @Test
    public void weightHelperIsCorrect2() throws Exception {
        float result = WeightHelper.reconvert(165.4f, WeightHelper.ENGLISH_SYSTEM_INDEX);

        assertEquals(result, 75, 0.05f);
    }


    @Test
    public void tallHelperIsCorrect() throws Exception {
        int[] requests = {148, 181, 206};
        String[] trueAnswers = {"4' 10\"", "5' 11\"", "6' 9\""};

        String[] response = new String[requests.length];
        for (int i = 0; i < requests.length; i++) {
            response[i] = TallHelper.convertToUnitSystem(requests[i], TallHelper.ENGLISH_SYSTEM_INDEX);
        }

        assertArrayEquals(response, trueAnswers);
    }


}