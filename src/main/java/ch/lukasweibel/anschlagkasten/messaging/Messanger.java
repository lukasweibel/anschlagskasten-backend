package ch.lukasweibel.anschlagkasten.messaging;

import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.lukasweibel.anschlagkasten.db.DbAccessor;
import ch.lukasweibel.anschlagkasten.model.Contact;

@ApplicationScoped
public class Messanger {

    @Inject
    DbAccessor dbAccessor;

    @Inject
    SmsNotifier smsNotifier;

    public void triggerNotification(String group) {
        ArrayList<Contact> contacts = dbAccessor.getContactsByGroup(group);
        for (Contact contact : contacts) {
            smsNotifier.sendMessage(contact.getPhoneNumber(), group);
        }
    }

}
