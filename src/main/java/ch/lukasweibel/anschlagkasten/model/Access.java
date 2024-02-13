package ch.lukasweibel.anschlagkasten.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Access {
    private String phoneNumber;
    private int accessToken;
    private long timestamp;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getAccessToken() {
        return accessToken;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAccessToken(int code) {
        this.accessToken = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
