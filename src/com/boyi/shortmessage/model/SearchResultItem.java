package com.boyi.shortmessage.model;

import java.io.Serializable;

public class SearchResultItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7905779748552873376L;

    public String Classify_id;
    public String Classify_name;
    public String Column_id;
    public String Content;
    public String Edit_date;
    public String Edit_time;
    public String Sms_id;
    public String Times;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((Classify_id == null) ? 0 : Classify_id.hashCode());
        result = prime * result
                + ((Classify_name == null) ? 0 : Classify_name.hashCode());
        result = prime * result
                + ((Column_id == null) ? 0 : Column_id.hashCode());
        result = prime * result + ((Content == null) ? 0 : Content.hashCode());
        result = prime * result
                + ((Edit_date == null) ? 0 : Edit_date.hashCode());
        result = prime * result
                + ((Edit_time == null) ? 0 : Edit_time.hashCode());
        result = prime * result + ((Sms_id == null) ? 0 : Sms_id.hashCode());
        result = prime * result + ((Times == null) ? 0 : Times.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SearchResultItem other = (SearchResultItem) obj;
        if (Classify_id == null) {
            if (other.Classify_id != null)
                return false;
        } else if (!Classify_id.equals(other.Classify_id))
            return false;
        if (Classify_name == null) {
            if (other.Classify_name != null)
                return false;
        } else if (!Classify_name.equals(other.Classify_name))
            return false;
        if (Column_id == null) {
            if (other.Column_id != null)
                return false;
        } else if (!Column_id.equals(other.Column_id))
            return false;
        if (Content == null) {
            if (other.Content != null)
                return false;
        } else if (!Content.equals(other.Content))
            return false;
        if (Edit_date == null) {
            if (other.Edit_date != null)
                return false;
        } else if (!Edit_date.equals(other.Edit_date))
            return false;
        if (Edit_time == null) {
            if (other.Edit_time != null)
                return false;
        } else if (!Edit_time.equals(other.Edit_time))
            return false;
        if (Sms_id == null) {
            if (other.Sms_id != null)
                return false;
        } else if (!Sms_id.equals(other.Sms_id))
            return false;
        if (Times == null) {
            if (other.Times != null)
                return false;
        } else if (!Times.equals(other.Times))
            return false;
        return true;
    }
}
