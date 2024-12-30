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

import com.icegreen.greenmail.util.GreenMailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import swiss.fihlon.rallyman.data.entity.MailTemplateId;
import swiss.fihlon.rallyman.ui.KaribuTest;

import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MailServiceIT extends KaribuTest {

    @Autowired
    private DatabaseService databaseService;

    @Test
    void sendMailWithVariables() {
        final var toAddress = "test@fihlon.swiss";
        databaseService.sendMail(MailTemplateId.TEST, Map.of("foobar", "content of foobar"), toAddress);
        await().atMost(2, SECONDS).untilAsserted(() -> {
            final var receivedMessage = greenMail.getReceivedMessages()[0];
            assertEquals("This is a test", receivedMessage.getSubject());
            assertEquals("The template variable \"foobar\" was set to \"content of foobar\".", GreenMailUtil.getBody(receivedMessage));
            assertEquals(1, receivedMessage.getAllRecipients().length);
            assertEquals(toAddress, receivedMessage.getAllRecipients()[0].toString());
        });
    }

    @Test
    void sendMailWithEmptyVariables() {
        final var toAddress = "test@fihlon.swiss";
        databaseService.sendMail(MailTemplateId.TEST, Map.of(), toAddress);
        await().atMost(2, SECONDS).untilAsserted(() -> {
            final var receivedMessage = greenMail.getReceivedMessages()[0];
            assertEquals("This is a test", receivedMessage.getSubject());
            assertEquals("The template variable \"foobar\" was set to \"${foobar}\".", GreenMailUtil.getBody(receivedMessage));
            assertEquals(1, receivedMessage.getAllRecipients().length);
            assertEquals(toAddress, receivedMessage.getAllRecipients()[0].toString());
        });
    }

    @Test
    void sendMailWithNullVariables() {
        final var toAddress = "test@fihlon.swiss";
        databaseService.sendMail(MailTemplateId.TEST, null, toAddress);
        await().atMost(2, SECONDS).untilAsserted(() -> {
            final var receivedMessage = greenMail.getReceivedMessages()[0];
            assertEquals("This is a test", receivedMessage.getSubject());
            assertEquals("The template variable \"foobar\" was set to \"${foobar}\".", GreenMailUtil.getBody(receivedMessage));
            assertEquals(1, receivedMessage.getAllRecipients().length);
            assertEquals(toAddress, receivedMessage.getAllRecipients()[0].toString());
        });
    }

}
