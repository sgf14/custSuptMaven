package com.prod.custSuptMaven;

public class Attachment {
	//instantiate vars
    private String name;
    private byte[] contents;
    
    //getters and setters (book designates then accessors and mutators)
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public byte[] getContents(){
        return contents;
    }

    public void setContents(byte[] contents){
        this.contents = contents;
    }
}
