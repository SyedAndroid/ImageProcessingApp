package com.idt.syed.imageapplication.data;


import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by syed on 2017-09-13.
 */



public class ImageContract {


        public final static String CONTENT_AUTHORITY="com.idt.syed.imageapplication";

        public final static Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public final static String PATH_IMAGE ="/img";

    public static final class imageEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_IMAGE).build();
        public static final String IMAGE_TABLE_NAME = "image";

        public static final String url_picture = "picture";

    }
}



