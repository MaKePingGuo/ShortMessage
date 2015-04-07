package com.boyi.shortmessage.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MessageList implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2501860759542134400L;

    public ArrayList<Message> DownloadResult = new ArrayList<Message>();
}
