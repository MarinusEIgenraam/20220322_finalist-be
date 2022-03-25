package nl.finalist.server.service;

import nl.finalist.server.model.Message;

public class MessageServices {

    public static void example(Message bodyIn) {
        bodyIn.setName( "Hello, " + bodyIn.getName() );
        bodyIn.setId(bodyIn.getId()*10);
    }
}