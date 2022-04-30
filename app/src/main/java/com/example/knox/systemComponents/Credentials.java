package com.example.knox.systemComponents;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "credentials")
public class Credentials {
    @ColumnInfo(name = "username")
    private String uName;

    @ColumnInfo(name = "password")
    private String passwd;

    @PrimaryKey
    private String url;

    public Credentials(){ }

    /****
     * Pre-Condition: the password MUST be AES encrypted before the object is created.
     * @param name - username for credential pair
     * @param pass - ENCRYPTED password for credential pair
     * @param URL - URL for webpage associated with credentials
     */
    public Credentials(String name, String pass, String URL){
        uName = name;
        passwd = pass;
        url = URL;
    }

    public String getUName() {return uName;}
    public String getPasswd(){return passwd;}

    public void setuName(String name){uName = name;}
    public void setPasswd(String pass){passwd = pass;}
}
