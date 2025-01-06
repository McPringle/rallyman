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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import swiss.fihlon.rallyman.data.TestUser;
import swiss.fihlon.rallyman.data.entity.UserData;
import swiss.fihlon.rallyman.service.DatabaseService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityServiceTest {

    private static final String TEST_IP = "127.0.0.1";

    private HttpServletRequest requestMock;
    private LoginAttemptService loginAttemptServiceMock;
    private DatabaseService databaseServiceMock;

    @BeforeEach
    void beforeEach() {
        requestMock = mock(HttpServletRequest.class);
        loginAttemptServiceMock = mock(LoginAttemptService.class);
        databaseServiceMock = mock(DatabaseService.class);
    }

    @Test
    void loadUserByUsernameSuccess() {
        when(databaseServiceMock.getUserByEmail(TestUser.EMAIL)).thenReturn(
                Optional.of(new UserData(1L, TestUser.EMAIL, TestUser.PASSWORD_HASH, TestUser.NAME, TestUser.LAST_LOGIN)));

        final var securityService = new SecurityService(requestMock, loginAttemptServiceMock, databaseServiceMock);
        final var userDetails = securityService.loadUserByUsername(TestUser.EMAIL);
        assertNotNull(userDetails);
        assertEquals(TestUser.EMAIL, userDetails.getUsername());
        assertEquals(TestUser.PASSWORD_HASH, userDetails.getPassword());
    }

    @Test
    void loadUserByUsernameNotFoundException() {
        when(databaseServiceMock.getUserByEmail(TestUser.EMAIL)).thenReturn(Optional.empty());

        final var securityService = new SecurityService(requestMock, loginAttemptServiceMock, databaseServiceMock);
        final var exception = assertThrows(UsernameNotFoundException.class, () -> securityService.loadUserByUsername(TestUser.EMAIL));
        assertEquals("User not found: " + TestUser.EMAIL, exception.getMessage());
    }

    @Test
    void loadUserByUsernameLockedException() {
        when(databaseServiceMock.getUserByEmail(TestUser.EMAIL)).thenReturn(
                Optional.of(new UserData(1L, TestUser.EMAIL, TestUser.PASSWORD_HASH, TestUser.NAME, TestUser.LAST_LOGIN)));

        when(requestMock.getRemoteAddr()).thenReturn(TEST_IP);
        when(loginAttemptServiceMock.isBlocked(TEST_IP)).thenReturn(true);

        final var securityService = new SecurityService(requestMock, loginAttemptServiceMock, databaseServiceMock);
        final var exception = assertThrows(LockedException.class, () -> securityService.loadUserByUsername(TestUser.EMAIL));
        assertEquals("Too many failed login attempts, IP address blocked for 24 hours!", exception.getMessage());
    }

}
