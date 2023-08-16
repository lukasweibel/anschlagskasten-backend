package ch.lukasweibel.anschlagkasten.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
    private String firstname;
    private String lastname;
    private String vulgo;
    private String stufe;
    private int id;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getVulgo() {
        return vulgo;
    }

    public void setVulgo(String vulgo) {
        this.vulgo = vulgo;
    }

    public String getStufe() {
        return stufe;
    }

    public void setStufe(String stufe) {
        this.stufe = stufe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
