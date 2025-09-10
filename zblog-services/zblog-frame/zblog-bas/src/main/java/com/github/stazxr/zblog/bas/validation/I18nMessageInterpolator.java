package com.github.stazxr.zblog.bas.validation;

import com.github.stazxr.zblog.bas.common.CommonUtil;
import com.github.stazxr.zblog.bas.i18n.I18nUtils;

import javax.validation.MessageInterpolator;
import java.util.Locale;

public class I18nMessageInterpolator implements MessageInterpolator {
    @Override
    public String interpolate(String messageTemplate, Context context) {
        return interpolate(messageTemplate, context, Locale.getDefault());
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        if (CommonUtil.isValidatorTemplateMessage(messageTemplate)) {
            String code = messageTemplate.substring(1, messageTemplate.length() - 1);
            return I18nUtils.getMessage(code, null, locale);
        }

        return messageTemplate;
    }
}
