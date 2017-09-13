package com.idt.syed.imageapplication;

/**
 * Created by syed on 2017-09-13.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageLinkValidator{

    private Pattern pattern;
    private Matcher matcher;

    private static final String IMAGE_PATTERN =
            "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

    public ImageLinkValidator(){
        pattern = Pattern.compile(IMAGE_PATTERN);
    }

    /**
     * Validate image with regular expression
    */
    public boolean validate(final String image){

        matcher = pattern.matcher(image);
        return matcher.matches();
      }
}
