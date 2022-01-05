package com.dana.wordsearch.xml;

import android.content.Context;
import android.content.res.AssetManager;


import com.dana.wordsearch.model.Word;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;


public class WordXmlDataSource {

    private static final String ASSET_WORD_BANK_FILE = "words.xml";

    private AssetManager mAssetManager;


    public WordXmlDataSource(Context context) {
        mAssetManager = context.getAssets();
    }


    private InputSource getXmlInputSource() throws IOException {
        return new InputSource(mAssetManager.open(ASSET_WORD_BANK_FILE));
    }
    public List<Word> getWords() {
        try {
            XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            SaxWordBankHandler wordBankHandler = new SaxWordBankHandler();
            reader.setContentHandler(wordBankHandler);
            reader.parse(getXmlInputSource());

            return wordBankHandler.getWords();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
