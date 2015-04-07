package com.boyi.shortmessage.model;

import java.io.Serializable;
import java.util.ArrayList;

public class UserAndClassifies implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1984345426467179144L;

    public User Appuser;
    public ArrayList<Classify> Classifies = new ArrayList<Classify>();
}
