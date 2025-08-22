package com.interbanking.autentication.utils;

import com.interbanking.commons.models.enums.CodeEnum;

public interface Messenger {

    String getMessage (CodeEnum code);

    String getMessage(CodeEnum code, Object... args);
}
