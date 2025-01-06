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

import com.google.common.cache.LoadingCache;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginAttemptServiceTest {

    @Test
    void loginFailedWithException() throws ExecutionException {
        final LoadingCache<String, Integer> attemptsCacheMock = mock(LoadingCache.class);
        when(attemptsCacheMock.get(anyString())).thenThrow(new ExecutionException(new Exception()));

        final LoginAttemptService loginAttemptService = new LoginAttemptService(attemptsCacheMock);
        assertDoesNotThrow(() -> loginAttemptService.loginFailed("127.0.0.1"));
    }

    @Test
    void isBlockedWithException() throws ExecutionException {
        final LoadingCache<String, Integer> attemptsCacheMock = mock(LoadingCache.class);
        when(attemptsCacheMock.get(anyString())).thenThrow(new ExecutionException(new Exception()));

        final LoginAttemptService loginAttemptService = new LoginAttemptService(attemptsCacheMock);
        assertDoesNotThrow(() -> loginAttemptService.isBlocked("127.0.0.1"));
    }

}
