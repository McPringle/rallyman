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
import swiss.fihlon.rallyman.data.TestUser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GravatarUtilTest {

    @Test
    void getGravatarAddressWithoutSize() {
        final var address = GravatarUtil.getGravatarAddress(TestUser.EMAIL);
        assertEquals("https://www.gravatar.com/avatar/8ea890a677d6a223c591a1beea6ea9d2?d=mp&s=80", address);
    }

    @Test
    void getGravatarAddressWithSize100() {
        final var address = GravatarUtil.getGravatarAddress(TestUser.EMAIL, 100);
        assertEquals("https://www.gravatar.com/avatar/8ea890a677d6a223c591a1beea6ea9d2?d=mp&s=100", address);
    }

    @Test
    void getGravatarAddressWithInvalidSize() {
        final var tooSmall = assertThrows(IllegalArgumentException.class,
                () -> GravatarUtil.getGravatarAddress(TestUser.EMAIL, 0));
        assertEquals("The size must be between 1 and 2'048!", tooSmall.getMessage());

        final var tooBig = assertThrows(IllegalArgumentException.class,
                () -> GravatarUtil.getGravatarAddress(TestUser.EMAIL, 2049));
        assertEquals("The size must be between 1 and 2'048!", tooBig.getMessage());
    }

    @Test
    void getGravatarAddressWithUppercaseMail() {
        final var address = GravatarUtil.getGravatarAddress(TestUser.EMAIL.toUpperCase());
        assertEquals("https://www.gravatar.com/avatar/8ea890a677d6a223c591a1beea6ea9d2?d=mp&s=80", address);
    }

    @Test
    void privateConstructorWithException() {
        final var cause = assertThrows(InvocationTargetException.class, () -> {
            Constructor<GravatarUtil> constructor = GravatarUtil.class.getDeclaredConstructor();
            if (Modifier.isPrivate(constructor.getModifiers())) {
                constructor.setAccessible(true);
                constructor.newInstance();
            }
        }).getCause();

        assertInstanceOf(IllegalStateException.class, cause);
        assertEquals("Utility class", cause.getMessage());
    }

}
