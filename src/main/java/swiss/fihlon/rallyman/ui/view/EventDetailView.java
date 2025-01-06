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

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.jetbrains.annotations.NotNull;
import swiss.fihlon.rallyman.service.DatabaseService;
import swiss.fihlon.rallyman.ui.component.EventDetail;

@AnonymousAllowed
@Route(value = "event/:id", layout = WebsiteLayout.class)
public final class EventDetailView extends Div implements BeforeEnterObserver {

    private final transient @NotNull DatabaseService databaseService;

    public EventDetailView(final @NotNull DatabaseService databaseService) {
        this.databaseService = databaseService;
        addClassName("event-detail-view");
        add(new H1("Event Details"));
    }

    @Override
    public void beforeEnter(final @NotNull BeforeEnterEvent beforeEnterEvent) {
        final var params = beforeEnterEvent.getRouteParameters();
        final var id = Long.parseLong(params.get("id").orElseThrow(NotFoundException::new));
        final var eventDetailData = databaseService.getEvent(id).orElseThrow(NotFoundException::new);
        add(new EventDetail(eventDetailData));
    }

}
