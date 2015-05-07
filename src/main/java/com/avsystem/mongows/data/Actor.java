package com.avsystem.mongows.data;

/**
 * Created by MKej
 */
public class Actor extends DataObject {
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + getId() +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
