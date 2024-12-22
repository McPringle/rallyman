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
import swiss.fihlon.rallyman.data.entity.EventDetailData;
import swiss.fihlon.rallyman.data.entity.EventSummaryData;
import swiss.fihlon.rallyman.service.getter.DSLContextGetter;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static swiss.fihlon.rallyman.data.db.Tables.EVENT;
import static swiss.fihlon.rallyman.data.db.Tables.LOCATION;

interface EventService extends DSLContextGetter {

    default @NotNull Stream<EventSummaryData> getUpcomingEvents() {
        return dsl().select(EVENT.ID, EVENT.NAME, EVENT.DATE, LOCATION.NAME)
                .from(EVENT)
                .leftJoin(LOCATION).on(EVENT.LOCATION_ID.eq(LOCATION.ID))
                .where(EVENT.DATE.ge(LocalDateTime.now()))
                .orderBy(EVENT.DATE.asc())
                .fetchStreamInto(EventSummaryData.class);
    }

    default @NotNull Optional<EventDetailData> getEvent(long id) {
        return dsl().select(EVENT.ID, EVENT.NAME, EVENT.DESCRIPTION, EVENT.DATE, LOCATION.NAME)
                .from(EVENT)
                .leftJoin(LOCATION).on(EVENT.LOCATION_ID.eq(LOCATION.ID))
                .where(EVENT.ID.eq(id))
                .fetchOptionalInto(EventDetailData.class);
    }

}
