package ch.lukasweibel.anschlagkasten.messaging;

import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.lukasweibel.anschlagkasten.db.DbAccessor;
import ch.lukasweibel.anschlagkasten.model.Access;
import ch.lukasweibel.anschlagkasten.model.Contact;

@ApplicationScoped
public class Messanger {

    @Inject
    DbAccessor dbAccessor;

    @Inject
    SmsNotifier smsNotifier;

    public void triggerNotifications(String group) {
        ArrayList<Contact> contacts = dbAccessor.getContactsByGroup(group);
        for (Contact contact : contacts) {
            smsNotifier.sendMessage(contact.getPhoneNumber(),
                    "Es gibt einen neuen Anschlag für die Gruppe: " + group
                            + " https://anschlagskasten-web-fd337ce2917a.herokuapp.com/");
        }
    }

    public void sendLoginCode(String phoneNumber) {
        int code = (int) (Math.random() * (999999 - 100000 + 1)) + 100000;
        Access access = new Access();
        smsNotifier.sendMessage(phoneNumber, "Prüfcode " + code + " für Anschlagskasten Cevi Seuzach");
        access.setAccessToken(code);
        access.setPhoneNumber(phoneNumber);
        dbAccessor.setAccess(access);
    }

    public boolean validateLoginCode(String phoneNumber, int code) {
        ArrayList<Access> accesses = dbAccessor.getAccessesByPhoneNumber(phoneNumber);
        for (Access access : accesses) {
            System.out.println(access.getAccessToken() + " " + access.getPhoneNumber());
            if (access.getAccessToken() == code && access.getPhoneNumber().equals(phoneNumber)) {
                long currentTimeMillis = System.currentTimeMillis();

                if (currentTimeMillis - access.getTimestamp() <= 300000) {
                    // The timestamp is not older than 5 minutes
                    return true;
                }
            }
        }
        return false;
    }
}
