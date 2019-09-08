package org.fkjava.wechat.message.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class OutMessage {

    private String account;
    @JsonProperty("touser")
    private String toUser;
    @JsonProperty("msgtype")
    private String msgType;
}
