package com.ahiho.apps.beeenglish.util;

import com.ahiho.apps.beeenglish.model.realm_object.CommunicationObject;
import com.ahiho.apps.beeenglish.model.realm_object.GrammarObject;
import com.ahiho.apps.beeenglish.model.realm_object.VocabularyObject;
import com.ahiho.apps.beeenglish.model.realm_object.WordObject;
import com.ahiho.apps.beeenglish.model.realm_object.SampleObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by theptokim on 11/25/16.
 */

public class Identity {
    public static final Type WORD_TYPE = new TypeToken<List<WordObject>>() {
    }.getType();
    public static final Type SAMPLE_TYPE = new TypeToken<List<SampleObject>>() {
    }.getType();
    public static final Type GRAMMAR_TYPE = new TypeToken<List<GrammarObject>>() {
    }.getType();
    public static final Type VOCABULARY_TYPE = new TypeToken<List<VocabularyObject>>() {
    }.getType();
    public static final Type COMMUNICATION_TYPE = new TypeToken<CommunicationObject>() {
    }.getType();
    public static final int REQUEST_PERMISSION_CHANGE_WIFI_STATE = 1;
    public static final int REQUEST_PERMISSION_READ_WRITE_FILE = 2;
    public static final int REQUEST_PERMISSION_CAMERA = 3;

    public static final int FUN_ID_DICTIONARY = 0;
    public static final int FUN_ID_SAMPLE = 1;
    public static final int FUN_ID_GRAMMAR = 2;
    public static final int FUN_ID_SKILL = 3;
    public static final int FUN_ID_VOCABULARY = 4;
    public static final int FUN_ID_TEST = 5;
    public static final int FUN_ID_BOOK = 6;
    public static final int FUN_ID_COMMUNICATE = 7;

    public static final String EXTRA_PDF_FILE_NAME = "EXTRA_PDF_FILE_NAME";
    public static final String EXTRA_USER_NAME = "EXTRA_USER_NAME";
    public static final String EXTRA_PASSWORD = "EXTRA_PASSWORD";
    public static final String EXTRA_STATUS_TITLE = "EXTRA_STATUS_TITLE";
    public static final String EXTRA_STATUS_TEXT = "EXTRA_STATUS_TEXT";
    public static final String EXTRA_STATUS_BOOLEAN = "EXTRA_STATUS_BOOLEAN";
    public static final String EXTRA_ID_DOWNLOAD = "EXTRA_ID_DOWNLOAD";
    public static final String EXTRA_DICTIONARY_OBJECT = "EXTRA_DICTIONARY_OBJECT";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final String EXTRA_FUN_ID = "EXTRA_FUN_ID";
    public static final String EXTRA_GLOBAL_OBJECT_ID = "EXTRA_GLOBAL_OBJECT_ID";
    public static final String EXTRA_SAMPLE_DESCRIPTION = "EXTRA_SAMPLE_DESCRIPTION";
    public static final String EXTRA_SAMPLE_LINK = "EXTRA_SAMPLE_LINK";
    public static final String EXTRA_SAMPLE_NAME = "EXTRA_SAMPLE_NAME";
    public static final String EXTRA_NEW_TEXT = "EXTRA_NEW_TEXT";

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_ID_SUB = "EXTRA_ID_SUB";
    public static final String EXTRA_ID_SUB_SECOND = "EXTRA_ID_SUB_SECOND";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";

    public static final String DOWNLOAD_STATUS_BROADCAST = "DOWNLOAD_STATUS_BROADCAST";
    public static final String SEARCH_CHANGE_BROADCAST = "SEARCH_CHANGE_BROADCAST";
    public static final String EXPIRED_BROADCAST = "EXPIRED_BROADCAST";
    public static final String UPDATE_FIRST_BROADCAST = "UPDATE_FIRST_BROADCAST";


}
