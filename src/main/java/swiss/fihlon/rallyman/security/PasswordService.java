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

import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public final class PasswordService implements PasswordEncoder {

    private static final int MAX_LOG_ROUNDS = 10; // The runtime increases exponentially!
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(MAX_LOG_ROUNDS, new SecureRandom());

    @Override
    public String encode(@NotNull final CharSequence rawPassword) {
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    @Override
    public boolean matches(@NotNull final CharSequence rawPassword, @NotNull final String encodedPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean upgradeEncoding(@NotNull final String encodedPassword) {
        return PASSWORD_ENCODER.upgradeEncoding(encodedPassword);
    }

}
