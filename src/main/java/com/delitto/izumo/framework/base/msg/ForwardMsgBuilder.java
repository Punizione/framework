package com.delitto.izumo.framework.base.msg;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.ForwardMessage;
import net.mamoe.mirai.message.data.Message;

public class ForwardMsgBuilder {
    private final Bot bot;
    private final net.mamoe.mirai.message.data.ForwardMessageBuilder builder;
    private MessageNode current;

    public ForwardMsgBuilder(Bot bot, Contact contact){
        this.bot = bot;
        this.builder = new net.mamoe.mirai.message.data.ForwardMessageBuilder(contact);
    }

    public ForwardMsgBuilder botSays(String message){
        return botSays(new MsgBuilder().text(message).build());
    }

    public ForwardMsgBuilder botSaysImage(String path) { return botSays(new MsgBuilder().image(builder.getContext(), path).build()); }

    public ForwardMsgBuilder botSays(Message message){
        builder.says(bot, message);
        return this;
    }

    public MessageNode botSays(){
        if (current != null){
            builder.says(bot, current.builder.build());
        }
        current = new MessageNode(this);
        return current;
    }


    public ForwardMessage build(){
        if (current != null){
            builder.says(bot, current.builder.build());
        }
        return builder.build();
    }

    public static class MessageNode{
        private final ForwardMsgBuilder parent;
        private final MsgBuilder builder;
        private MessageNode(ForwardMsgBuilder parent){
            this.parent = parent;
            this.builder = new MsgBuilder();
        }

        public MessageNode plaintext(String text){
            builder.text(text);
            return this;
        }

        public MessageNode image(Contact contact, String file){
            builder.image(contact, file);
            return this;
        }

        public MessageNode botSays(){
            return parent.botSays();
        }


        public ForwardMessage build(){
            return parent.build();
        }

    }
}
