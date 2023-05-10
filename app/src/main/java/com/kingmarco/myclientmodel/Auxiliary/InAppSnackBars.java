package com.kingmarco.myclientmodel.Auxiliary;

/** The interface to create and personalize the behavior of the snackbars in the classes that use it*/
public interface InAppSnackBars {
    void showSnackBar(String text);
    void showSnackBar(String text, int backgroundColor);
    void showSnackBar(String text,int backgroundColor ,int textColor);
}

