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

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import swiss.fihlon.rallyman.service.DatabaseService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class AuthenticatedUserTest {

    /**
     * Spring security usually adds an `AnonymousAuthenticationToken` to the
     * security context when there is no user logged in. This should be
     * ignored and there should be no user data available in that case.
     */
    @Test
    void ignoreAnonymousAuthenticationToken() {
        final var originalSecurityContext = SecurityContextHolder.getContext();
        try {
            final var authenticationToken = new AnonymousAuthenticationToken("test", "test",
                    List.of(new SimpleGrantedAuthority("test")));
            final var securityContext = new SecurityContextImpl(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

            final var databaseService = mock(DatabaseService.class);
            final var authenticatedUser = new AuthenticatedUser(databaseService);
            final var userData = authenticatedUser.get();

            verifyNoInteractions(databaseService);
            assertTrue(userData.isEmpty());
        }
        finally {
            SecurityContextHolder.setContext(originalSecurityContext);
        }
    }

}
