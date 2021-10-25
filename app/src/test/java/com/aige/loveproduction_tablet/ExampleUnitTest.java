package com.aige.loveproduction_tablet;

import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        List<String> list = new ArrayList<>();
        String[] s1 = new String[]{"1","2","3","4"};
        String[] s2 = new String[]{"5","6","7"};
        list.toArray(s1);
        list.toArray(s2);
        for (String s : list) {
            System.out.println("*****************************");
            System.out.println(s);
        }
    }


}