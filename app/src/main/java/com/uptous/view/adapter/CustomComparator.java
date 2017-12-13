package com.uptous.view.adapter;

import com.uptous.model.ContactListResponseModel;

import java.util.Comparator;


public class CustomComparator implements Comparator<ContactListResponseModel> {


    @Override
    public int compare(ContactListResponseModel contactListResponseModel, ContactListResponseModel t1) {
        String SortName = contactListResponseModel.getLastName() + " " + contactListResponseModel.getFirstName();
        return SortName.compareToIgnoreCase(t1.getLastName() + " " + t1.getFirstName());
    }
}

