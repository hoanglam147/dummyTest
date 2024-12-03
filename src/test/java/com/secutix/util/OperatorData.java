package com.secutix.util;

import com.secutix.object.Operator;

public enum OperatorData implements Operator {
    NO_LOGIN_DATA("", ""),
    STXCAT22_AC("STXCAT22_AC", "P@ssw0rd"),
    STXCAT17_WS("STXCAT17_WS", "P@ssw0rd2020");
    private final String login;
    private final String password;
    OperatorData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getPassword() {
        return password;
    }


}
