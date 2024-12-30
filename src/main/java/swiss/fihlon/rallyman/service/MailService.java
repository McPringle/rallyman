/*
 * RallyMan - Organizing rallies as easily as possible
 * Copyright (C) Marcus Fihlon and the individual contributors to RallyMan.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package swiss.fihlon.rallyman.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.mail.SimpleMailMessage;
import swiss.fihlon.rallyman.data.entity.MailTemplateId;
import swiss.fihlon.rallyman.service.getter.ConfigurationGetter;
import swiss.fihlon.rallyman.service.getter.DSLContextGetter;
import swiss.fihlon.rallyman.service.getter.MailSenderGetter;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface MailService extends ConfigurationGetter, DSLContextGetter, MailSenderGetter, MailTemplateService {

    default void sendMail(@NotNull final MailTemplateId mailTemplateId,
                          @Nullable final Map<String, String> variables,
                          @NotNull final String... emailAddresses) {
        final var mailTemplateRecord = getMailTemplate(mailTemplateId).orElseThrow();
        final var message = new SimpleMailMessage();
        message.setTo(emailAddresses);
        message.setFrom(appConfig().defaultMailFromAddress());
        message.setSubject(replaceVariables(mailTemplateRecord.getSubject(), variables));
        message.setText(replaceVariables(mailTemplateRecord.getContent(), variables));
        mailSender().send(message);
    }

    private String replaceVariables(@NotNull final String text,
                                    @Nullable final Map<String, String> variables) {
        String returnValue = text;
        if (variables != null) {
            for (final String key : variables.keySet()) {
                final var value = Matcher.quoteReplacement(variables.get(key));
                final var regex = Pattern.quote("${%s}".formatted(key));
                returnValue = returnValue.replaceAll(regex, value);
            }
        }
        return returnValue;
    }

}
