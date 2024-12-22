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
import swiss.fihlon.rallyman.data.entity.EventDetailData;
import swiss.fihlon.rallyman.util.FormatterUtil;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventDetailTest {

    @Test
    void happyCase() {
        final Long id = 42L;
        final String name = "Test Name";
        final String description = "Test Description";
        final LocalDateTime date = LocalDateTime.MAX;
        final String location = "Test Location";

        final var eventDetailData = new EventDetailData(id, name, description, date, location);

        final var eventDetail = new EventDetail(eventDetailData);
        assertEquals(name, eventDetail.getComponentAt(0).getElement().getText());
        assertEquals(description, eventDetail.getComponentAt(1).getElement().getText());
        assertEquals(FormatterUtil.formatDateTime(date), eventDetail.getComponentAt(2).getElement().getText());
        assertEquals(location, eventDetail.getComponentAt(3).getElement().getText());
    }

}
