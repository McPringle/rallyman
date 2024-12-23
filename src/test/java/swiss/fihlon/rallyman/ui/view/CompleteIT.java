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
package swiss.fihlon.rallyman.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import swiss.fihlon.rallyman.data.db.tables.records.EventRecord;
import swiss.fihlon.rallyman.data.db.tables.records.LocationRecord;
import swiss.fihlon.rallyman.service.DatabaseService;
import swiss.fihlon.rallyman.ui.KaribuTest;
import swiss.fihlon.rallyman.ui.component.EventDetail;
import swiss.fihlon.rallyman.ui.component.EventOverview;
import swiss.fihlon.rallyman.ui.component.EventSummary;
import swiss.fihlon.rallyman.util.FormatterUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.github.mvysny.kaributesting.v10.LocatorJ._assertNone;
import static com.github.mvysny.kaributesting.v10.LocatorJ._assertOne;
import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static swiss.fihlon.rallyman.data.db.Tables.EVENT;
import static swiss.fihlon.rallyman.data.db.Tables.LOCATION;

class CompleteIT extends KaribuTest {

    @Autowired
    private DatabaseService databaseService;

    private LocationRecord locationRecord;
    private EventRecord eventRecord;

    @BeforeEach
    void beforeEach() {
        locationRecord = databaseService.dsl().newRecord(LOCATION);
        locationRecord.setId(21L);
        locationRecord.setName("New Location");
        locationRecord.setLatitude(BigDecimal.ZERO);
        locationRecord.setLongitude(BigDecimal.ZERO);
        locationRecord.store();

        eventRecord = databaseService.dsl().newRecord(EVENT);
        eventRecord.setId(42L);
        eventRecord.setName("New Event");
        eventRecord.setDescription("New Description");
        eventRecord.setDate(LocalDateTime.now().plusYears(1));
        eventRecord.setLocationId(locationRecord.getId());
        eventRecord.store();
    }

    @AfterEach
    void afterEach() {
        eventRecord.delete();
        locationRecord.delete();
    }

    @Test
    @SuppressWarnings("java:S2699") // false positive: Karibu assertions are not recognized
    void homeView() {
        UI.getCurrent().navigate(HomeView.class);
        UI.getCurrent().getPage().reload();
        _assertOne(HomeView.class);
        _assertOne(EventOverview.class);
        _assertOne(EventSummary.class);
        _assertNone(EventDetail.class);

        final var eventSummary = _get(Div.class, spec -> spec.withClasses("event-summary"));
        _click(eventSummary);
        _assertNone(HomeView.class);
        _assertNone(EventOverview.class);
        _assertNone(EventSummary.class);
        _assertOne(EventDetail.class);
    }

    @Test
    void eventDetailViewNotFound() {
        final var ui = UI.getCurrent();
        assertThrows(NotFoundException.class, () -> ui.navigate("/event/1234567890"));
    }

    @Test
    void eventDetailView() {
        UI.getCurrent().navigate("/event/42");
        _assertOne(EventDetailView.class);

        final var eventName = _get(Div.class, spec -> spec.withClasses("event-name"));
        assertEquals(eventRecord.getName(), eventName.getText());
        final var eventDescription = _get(Div.class, spec -> spec.withClasses("event-description"));
        assertEquals(eventRecord.getDescription(), eventDescription.getText());
        final var eventDate = _get(Div.class, spec -> spec.withClasses("event-date"));
        assertEquals(FormatterUtil.formatDateTime(eventRecord.getDate()), eventDate.getText());
        final var eventLocation = _get(Div.class, spec -> spec.withClasses("event-location"));
        assertEquals(locationRecord.getName(), eventLocation.getText());
        _assertOne(Div.class, spec -> spec.withClasses("event-map"));
    }

}
