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
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import swiss.fihlon.rallyman.data.entity.Role;
import swiss.fihlon.rallyman.service.DatabaseService;

import java.util.Set;

import static swiss.fihlon.rallyman.util.RequestUtil.getClientIP;

@Service
public final class SecurityService implements UserDetailsService {

    private final @NotNull HttpServletRequest request;
    private final @NotNull LoginAttemptService loginAttemptService;
    private final @NotNull DatabaseService databaseService;

    public SecurityService(@NotNull final HttpServletRequest request,
                           @NotNull final LoginAttemptService loginAttemptService,
                           @NotNull final DatabaseService databaseService) {
        this.request = request;
        this.loginAttemptService = loginAttemptService;
        this.databaseService = databaseService;
    }

    @Override
    public @NotNull UserDetails loadUserByUsername(@NotNull final String username) throws LockedException, UsernameNotFoundException {
        if (loginAttemptService.isBlocked(getClientIP(request))) {
            throw new LockedException("Too many failed login attempts, IP address blocked for 24 hours!");
        }

        return databaseService.getUserByEmail(username)
                .map(userData -> new User(userData.email(), userData.passwordHash(),
                        Set.of(new SimpleGrantedAuthority(Role.USER.name()))))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

}
