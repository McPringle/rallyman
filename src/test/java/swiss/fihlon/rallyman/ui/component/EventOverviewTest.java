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
package swiss.fihlon.rallyman.ui.component;

import org.junit.jupiter.api.Test;
import swiss.fihlon.rallyman.data.entity.EventSummaryData;
import swiss.fihlon.rallyman.service.DatabaseService;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EventOverviewTest {

    @Test
    void happyCase() {
        final var databaseServiceMock = mock(DatabaseService.class);
        when(databaseServiceMock.getUpcomingEvents())
                .thenReturn(Stream.of(new EventSummaryData(42L, "Test Name", LocalDateTime.MAX, "Test Location")));

        final var eventOverview = new EventOverview(databaseServiceMock);
        assertEquals(1, eventOverview.getChildren().count());
        assertInstanceOf(EventSummary.class, eventOverview.getChildren().findFirst().orElseThrow());
    }

}
