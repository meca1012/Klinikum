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
     * LuceneSearchRequest.java
     * 
     * Purpose: DomainObject to exchange request RestConnectionPoint
     * for searching through the LuceneIndex
     * 
     * @author Spironto Team 1
     * @version 1.0 20/06/13
     */

    private static final long serialVersionUID = -352944296024350425L;
   
    /**
     * Identifier for patient 
     */
    @XmlElement(name = "patientUri")
    private String patientUri;
    
    /**
     * SearchString including wildcards.
     * Look for usage here
     * http://lucene.apache.org/core/old_versioned_docs/versions/2_9_1/queryparsersyntax.html
     * LinkDate: 21.06.2013
     */
    @XmlElement(name = "searchString")
    private String searchString;
    
    /**
     * Getter for patientUri
     * @return simple return this.patientUri
     */
    public String getPatientUri() {
        return patientUri;
    }

    /**
     * Setter for patientUri
     * @return  simple sets this.patientUri
     */
    public void setPatientUri(String patientUri) {
        this.patientUri = patientUri;
    }

    /**
     * Getter for searchString
     * @return simple return this.searchString
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     * Setter for searchString
     * @return simple sets this.searchString
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }


    
    
}
