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

import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FormatterUtil {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm";
    public static final String DATE_TIME_PATTERN = "%s %s".formatted(DATE_PATTERN, TIME_PATTERN);

    public static DateTimeFormatter dateFormatter() {
        return DateTimeFormatter.ofPattern(DATE_PATTERN);
    }

    public static DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    }

    public static String formatDate(@Nullable final LocalDate date) {
        return date != null ? date.format(dateFormatter()) : "";
    }

    public static String formatDateTime(@Nullable final LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(dateTimeFormatter()) : "";
    }

    private FormatterUtil() {
        throw new IllegalStateException("Utility class");
    }

}
