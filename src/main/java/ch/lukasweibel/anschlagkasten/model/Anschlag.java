package ch.lukasweibel.anschlagkasten.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ch.lukasweibel.helper.ObjectIdDeserializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Anschlag {
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private String _id;
    private long creationDate;
    private List<Comment> comments;
    private String date;
    private String createDate;
    private String endPlace;
    private String endTime;
    private String finalWord;
    private String introducing;
    private String itemsToBring;
    private String name;
    private String startPlace;
    private String startTime;
    private String title;
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(String endPlace) {
        this.endPlace = endPlace;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFinalWord() {
        return finalWord;
    }

    public void setFinalWord(String finalWord) {
        this.finalWord = finalWord;
    }

    public String getIntroducing() {
        return introducing;
    }

    public void setIntroducing(String introducing) {
        this.introducing = introducing;
    }

    public String getItemsToBring() {
        return itemsToBring;
    }

    public void setItemsToBring(String itemsToBring) {
        this.itemsToBring = itemsToBring;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

}