package com.delitto.izumo.framework.base.msg;

import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;

public class MsgBuilder {
    private final MessageChainBuilder builder = new MessageChainBuilder();

    public MsgBuilder() {

    }

    public MsgBuilder text(String text) {
        builder.append(new PlainText(text));
        return this;
    }

    public MsgBuilder at(Long id) {
        builder.append(new At(id));
        return this;
    }

    public MsgBuilder atAll() {
        builder.append(AtAll.INSTANCE);
        return this;
    }

    public MsgBuilder image(Contact contact, String path2File) {
        Image image = contact.uploadImage(ExternalResource.create(new File(path2File)));
        builder.append(image);
        return this;
    }


    public MessageChain voice(Contact contact, String path2File) {
        Voice voice = ExternalResource.uploadAsVoice(ExternalResource.create(new File(path2File)), contact);
        return builder.append(voice).build();
    }

    public MessageChain lightApp(JSONObject jsonObject) {
        return builder.append(new LightApp(jsonObject.toJSONString())).build();
    }

    public MessageChain build() {
        return builder.build();
    }

    public MsgBuilder add(MsgBuilder builder) {
        this.builder.append(builder.build());
        return this;
    }

    public int size() {
        return this.builder.size();
    }


}
