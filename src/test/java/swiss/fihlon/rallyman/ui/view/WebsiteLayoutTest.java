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

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WebsiteLayoutTest {

    @Test
    void happyCase() {
        final var websiteLayout = new WebsiteLayout();
        final var main = websiteLayout.getChildren().findFirst().orElseThrow();
        assertEquals(0, main.getChildren().count());

        final var content1 = new Div("content1");
        websiteLayout.showRouterLayoutContent(content1);
        assertEquals(1, main.getChildren().count());
        assertEquals(content1, main.getChildren().findFirst().orElseThrow());

        final var content2 = new Div("content2");
        websiteLayout.showRouterLayoutContent(content2);
        assertEquals(1, main.getChildren().count());
        assertEquals(content2, main.getChildren().findFirst().orElseThrow());

        websiteLayout.removeRouterLayoutContent(content2);
        assertEquals(0, main.getChildren().count());
    }

    @Test
    void illegalArgument() {
        final var content = mock(HasElement.class);
        final var element = mock(Element.class);
        when(content.getElement()).thenReturn(element);
        when(element.getComponent()).thenReturn(Optional.empty());

        final var exception = assertThrows(IllegalArgumentException.class, () -> new WebsiteLayout().showRouterLayoutContent(content));
        assertEquals("The content must contain at least one child", exception.getMessage());
    }
}
