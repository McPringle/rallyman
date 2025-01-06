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

    private HttpServletRequest mockedRequest;
    private LoginAttemptService mockedLoginAttemptService;
    private DatabaseService mockedDatabaseService;

    @BeforeEach
    void beforeEach() {
        mockedRequest = mock(HttpServletRequest.class);
        mockedLoginAttemptService = mock(LoginAttemptService.class);
        mockedDatabaseService = mock(DatabaseService.class);
    }

    @Test
    void loadUserByUsernameSuccess() {
        when(mockedDatabaseService.getUserByEmail(TestUser.EMAIL)).thenReturn(
                Optional.of(new UserData(1L, TestUser.EMAIL, TestUser.PASSWORD_HASH, TestUser.NAME, TestUser.LAST_LOGIN)));

        final var securityService = new SecurityService(mockedRequest, mockedLoginAttemptService, mockedDatabaseService);
        final var userDetails = securityService.loadUserByUsername(TestUser.EMAIL);
        assertNotNull(userDetails);
        assertEquals(TestUser.EMAIL, userDetails.getUsername());
        assertEquals(TestUser.PASSWORD_HASH, userDetails.getPassword());
    }

    @Test
    void loadUserByUsernameNotFoundException() {
        when(mockedDatabaseService.getUserByEmail(TestUser.EMAIL)).thenReturn(Optional.empty());

        final var securityService = new SecurityService(mockedRequest, mockedLoginAttemptService, mockedDatabaseService);
        final var exception = assertThrows(UsernameNotFoundException.class, () -> securityService.loadUserByUsername(TestUser.EMAIL));
        assertEquals("User not found: " + TestUser.EMAIL, exception.getMessage());
    }

    @Test
    void loadUserByUsernameLockedException() {
        when(mockedDatabaseService.getUserByEmail(TestUser.EMAIL)).thenReturn(
                Optional.of(new UserData(1L, TestUser.EMAIL, TestUser.PASSWORD_HASH, TestUser.NAME, TestUser.LAST_LOGIN)));

        when(mockedRequest.getRemoteAddr()).thenReturn(TEST_IP);
        when(mockedLoginAttemptService.isBlocked(TEST_IP)).thenReturn(true);

        final var securityService = new SecurityService(mockedRequest, mockedLoginAttemptService, mockedDatabaseService);
        final var exception = assertThrows(LockedException.class, () -> securityService.loadUserByUsername(TestUser.EMAIL));
        assertEquals("Too many failed login attempts, IP address blocked for 24 hours!", exception.getMessage());
    }

    @Test
    void getClientIP() {
        when(mockedRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        final var securityService = new SecurityService(mockedRequest, mockedLoginAttemptService, mockedDatabaseService);
        assertEquals("127.0.0.1", securityService.getClientIP());

        when(mockedRequest.getHeader("X-Forwarded-For")).thenReturn("127.0.0.2");
        assertEquals("127.0.0.2", securityService.getClientIP());

        when(mockedRequest.getHeader("X-Forwarded-For")).thenReturn("127.0.0.3, 127.0.0.4");
        assertEquals("127.0.0.3", securityService.getClientIP());
    }

}
