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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import org.jetbrains.annotations.NotNull;
import swiss.fihlon.rallyman.data.entity.EventDetailData;
import swiss.fihlon.rallyman.util.FormatterUtil;

public class EventDetail extends Div {

    public EventDetail(@NotNull final EventDetailData eventDetailData) {
        addClassName("event-detail");

        add(createDiv("event-name", new Text(eventDetailData.name())));
        add(createDiv("event-description", new Text(eventDetailData.description())));
        add(createDiv("event-date", new Text(FormatterUtil.formatDateTime(eventDetailData.date()))));
        add(createDiv("event-location", new Text(eventDetailData.location())));
    }

    private Component createDiv(@NotNull final String className, @NotNull final Component... components) {
        final var div = new Div(components);
        div.addClassName(className);
        return div;
    }

}
