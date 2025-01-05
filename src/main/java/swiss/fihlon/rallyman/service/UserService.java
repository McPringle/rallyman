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
package swiss.fihlon.rallyman.service;

import org.jetbrains.annotations.NotNull;
import swiss.fihlon.rallyman.data.entity.UserData;
import swiss.fihlon.rallyman.service.getter.DSLContextGetter;

import java.time.LocalDateTime;
import java.util.Optional;

import static swiss.fihlon.rallyman.data.db.Tables.USER;

interface UserService extends DSLContextGetter, MailService {

    default Optional<UserData> getUserByEmail(@NotNull final String email) {
        return dsl().selectFrom(USER)
                .where(USER.EMAIL.eq(email))
                .limit(1)
                .fetchOptionalInto(UserData.class);
    }

    default void updateUserLoginDate(@NotNull final String userEmail, @NotNull final LocalDateTime dateTime) {
        dsl().update(USER)
                .set(USER.LAST_LOGIN, dateTime)
                .where(USER.EMAIL.eq(userEmail))
                .execute();
    }
}
