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
package swiss.fihlon.rallyman.util;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestUtilTest {

    @Test
    void getClientIP() {
        final var requestMock = mock(HttpServletRequest.class);

        when(requestMock.getRemoteAddr()).thenReturn("127.0.0.1");
        assertEquals("127.0.0.1", RequestUtil.getClientIP(requestMock));

        when(requestMock.getHeader("X-Forwarded-For")).thenReturn("127.0.0.2");
        assertEquals("127.0.0.2", RequestUtil.getClientIP(requestMock));

        when(requestMock.getHeader("X-Forwarded-For")).thenReturn("127.0.0.3, 127.0.0.4");
        assertEquals("127.0.0.3", RequestUtil.getClientIP(requestMock));
    }

    @Test
    void privateConstructorWithException() {
        final var cause = assertThrows(InvocationTargetException.class, () -> {
            Constructor<RequestUtil> constructor = RequestUtil.class.getDeclaredConstructor();
            if (Modifier.isPrivate(constructor.getModifiers())) {
                constructor.setAccessible(true);
                constructor.newInstance();
            }
        }).getCause();

        assertInstanceOf(IllegalStateException.class, cause);
        assertEquals("Utility class", cause.getMessage());
    }

}
