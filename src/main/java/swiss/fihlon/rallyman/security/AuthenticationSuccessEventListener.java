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
package swiss.fihlon.rallyman.security;

import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import swiss.fihlon.rallyman.service.DatabaseService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static swiss.fihlon.rallyman.util.RequestUtil.getClientIP;

@Component
public final class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final @NotNull HttpServletRequest request;
    private final @NotNull LoginAttemptService loginAttemptService;
    private final @NotNull DatabaseService databaseService;

    public AuthenticationSuccessEventListener(@NotNull final HttpServletRequest request,
                                              @NotNull final LoginAttemptService loginAttemptService,
                                              @NotNull final DatabaseService databaseService) {
        this.request = request;
        this.loginAttemptService = loginAttemptService;
        this.databaseService = databaseService;
    }

    @Override
    public void onApplicationEvent(@NotNull final AuthenticationSuccessEvent e) {
        final var userEmail = e.getAuthentication().getName();
        final var instant = Instant.ofEpochMilli(e.getTimestamp());
        final var dateTime = LocalDateTime.ofInstant(instant, TimeZone.getDefault().toZoneId());
        databaseService.updateUserLoginDate(userEmail, dateTime);
        loginAttemptService.loginSucceeded(getClientIP(request));
    }

}
