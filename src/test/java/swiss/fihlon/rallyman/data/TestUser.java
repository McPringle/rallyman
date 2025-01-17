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
package swiss.fihlon.rallyman.data;

import java.time.LocalDateTime;

public interface TestUser {

    String EMAIL = "test@localhost";
    String PASSWORD = "testPassword12345";
    String PASSWORD_HASH = "$2a$10$o/h.m6vETYxnskJfz.65wuBkXNRQ102AR.Bs1Jr8l3XirCxabJbJa";
    String NAME = "Test User";
    LocalDateTime LAST_LOGIN = null;
}
