package com.uptous.view.adapter;

import com.uptous.model.ContactListResponseModel;

import java.util.Comparator;


public class CustomComparator implements Comparator<ContactListResponseModel> {


    @Override
    public int compare(ContactListResponseModel contactListResponseModel, ContactListResponseModel t1) {
        String SortName = contactListResponseModel.getFirstName() + " " + contactListResponseModel.getLastName();
        return SortName.compareToIgnoreCase(t1.getFirstName() + " " + t1.getLastName());
    }
}

