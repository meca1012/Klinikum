package de.klinikum.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "luceneSearchRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class LuceneSearchRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -352944296024350425L;
   
    @XmlElement(name = "patientURI")
    private String patientUri;
    
    @XmlElement(name = "searchString")
    private String searchString;
    
    
    public String getPatientUri() {
        return patientUri;
    }

    public void setPatientUri(String patientUri) {
        this.patientUri = patientUri;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }


    
    
}
