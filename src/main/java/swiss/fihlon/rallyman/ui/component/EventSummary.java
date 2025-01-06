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

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import org.jetbrains.annotations.NotNull;
import swiss.fihlon.rallyman.data.entity.EventSummaryData;
import swiss.fihlon.rallyman.util.FormatterUtil;

import static swiss.fihlon.rallyman.util.ComponentUtil.createDiv;

public class EventSummary extends Div {

    public EventSummary(final @NotNull EventSummaryData eventSummaryData) {
        addClassName("event-summary");

        add(createDiv("event-name", new Text(eventSummaryData.name())));
        add(createDiv("event-date", new Text(FormatterUtil.formatDateTime(eventSummaryData.date()))));
        add(createDiv("event-location", new Text(eventSummaryData.location())));

        addClickListener(event -> UI.getCurrent().navigate("/event/" + eventSummaryData.id()));
    }

}
