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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FormatterUtilTest {

    @Test
    void testFormatDate() {
        assertEquals("1970-01-02", FormatterUtil.formatDate(LocalDate.ofEpochDay(1L)));
        assertEquals("", FormatterUtil.formatDate(null));
    }

    @Test
    void testFormatDateTime() {
        assertEquals("0001-01-01 01:01", FormatterUtil.formatDateTime(LocalDateTime.of(1, 1, 1, 1, 1)));
        assertEquals("", FormatterUtil.formatDateTime(null));
    }

    @Test
    @SuppressWarnings("PMD.AvoidAccessibilityAlteration") // this is exactly what we want to test
    void privateConstructorWithException() {
        final var cause = assertThrows(InvocationTargetException.class, () -> {
            Constructor<FormatterUtil> constructor = FormatterUtil.class.getDeclaredConstructor();
            if (Modifier.isPrivate(constructor.getModifiers())) {
                constructor.setAccessible(true);
                constructor.newInstance();
            }
        }).getCause();
        assertInstanceOf(IllegalStateException.class, cause);
        assertEquals("Utility class", cause.getMessage());
    }

}
