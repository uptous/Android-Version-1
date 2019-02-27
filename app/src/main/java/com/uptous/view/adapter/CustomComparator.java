package com.uptous.view.adapter;

import com.uptous.model.ContactListResponseModel;

import java.io.Console;
import java.util.Comparator;


public class CustomComparator implements Comparator<ContactListResponseModel> {


    @Override
    public int compare(ContactListResponseModel contact1, ContactListResponseModel contact2) {
//        String SortName = contact1.getFirstName() + " " + contact1.getLastName();
//        return SortName.compareToIgnoreCase(contact2.getFirstName() + " " + contact2.getLastName());

//        String SortName = contact1.getLastName() + " " + contact1.getFirstName();
        if (contact1.getLastName().equals(contact2.getLastName()))
            return contact1.getFirstName().compareToIgnoreCase(contact2.getFirstName());
        else
            return contact1.getLastName().compareToIgnoreCase(contact2.getLastName());


//       self.fullListArr = self.fullListArr.sorted(by: { (contact1, contact2) -> Bool in
//            if contact1.lastName == contact2.lastName {
//                return contact1.firstName! < contact2.firstName!
//            }else {
//                return contact1.lastName! < contact2.lastName!
//            }

    }


}

