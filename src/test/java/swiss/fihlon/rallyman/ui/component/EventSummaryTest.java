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
import swiss.fihlon.rallyman.util.FormatterUtil;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventSummaryTest {

    @Test
    void happyCase() {
        final Long id = 42L;
        final String name = "Test Name";
        final LocalDateTime date = LocalDateTime.MAX;
        final String location = "Test Location";

        final var eventSummaryData = new EventSummaryData(id, name, date, location);

        final var eventSummary = new EventSummary(eventSummaryData);
        assertEquals(name, eventSummary.getComponentAt(0).getElement().getText());
        assertEquals(FormatterUtil.formatDateTime(date), eventSummary.getComponentAt(1).getElement().getText());
        assertEquals(location, eventSummary.getComponentAt(2).getElement().getText());
    }

}
