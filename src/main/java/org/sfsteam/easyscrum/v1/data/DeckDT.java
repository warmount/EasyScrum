package org.sfsteam.easyscrum.v1.data;

import java.io.Serializable;

/**
 * Created by vkurinov on 14.06.13.
 */
public class DeckDT implements Serializable{

    private String name;
    private String deckString;

    public DeckDT(String name, String deckString) {
        if (name == null || name.trim().length()==0){
            name = deckString.length()>7?deckString.substring(0,6)+"..":deckString;
        }
        this.name = name;
        this.deckString = deckString;
    }

    public String getDeckString() {
        return deckString;
    }

    public void setDeckString(String deckString) {
        this.deckString = deckString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final String[] getDeckAsArray(){
        return deckString.split(",");
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DeckDT other = (DeckDT) obj;
        if (name != other.getName() && deckString != other.getDeckString())
            return false;
        return true;
    }

    @Override
    public int hashCode(){
        return 31 + name.hashCode() + deckString.hashCode();
    }
}
