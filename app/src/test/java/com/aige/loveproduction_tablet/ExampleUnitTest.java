package com.aige.loveproduction_tablet;

import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import org.json.JSONException;
import org.json.JSONObject;
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
        String json = " {gg:\"ffff\",ff:\"ff\"}";
        try {
            JSONObject jsonObject = new JSONObject(json);
            System.out.println(jsonObject.get("gg"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}