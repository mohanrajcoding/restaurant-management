package com.batman.util;

public class BSUtil {

    public boolean nullCheckString(String string){
        System.out.println("Print :"+ string);
        return string!=null && !string.isEmpty();
    }

    public boolean nullCheck(Object object ) {
        return object !=null;
    }
}
