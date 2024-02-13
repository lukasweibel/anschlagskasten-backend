package ch.lukasweibel.anschlagkasten.messaging;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SmsNotifier {

    public static String ACCOUNT_SID;
    public static String AUTH_TOKEN;

    public SmsNotifier(@ConfigProperty(name = "twillio.account.sid") String accountSid,
            @ConfigProperty(name = "twillio.auth.token") String authToken) {
        ACCOUNT_SID = accountSid;
        AUTH_TOKEN = authToken;
    }

    public void sendMessage(String to, String messageString) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber("+447893947679"),
                messageString)
                .create();
        System.out.println(message.getSid());

    }
}
