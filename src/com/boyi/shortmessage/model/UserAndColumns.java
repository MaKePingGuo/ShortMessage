package com.boyi.shortmessage.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UserAndColumns implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3011985539598366459L;

    public User Appuser;
    public ArrayList<Column> Columns = new ArrayList<Column>();
}
