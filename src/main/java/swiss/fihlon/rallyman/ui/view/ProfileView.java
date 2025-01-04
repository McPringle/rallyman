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
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.jetbrains.annotations.NotNull;
import swiss.fihlon.rallyman.security.AuthenticatedUser;

@PermitAll
@Route(value = "profile", layout = WebsiteLayout.class)
public final class ProfileView extends VerticalLayout {

    public ProfileView(@NotNull final AuthenticatedUser authenticatedUser) {
        final var userData = authenticatedUser.orElseThrow();
        addClassName("profile-view");
        add(new H1("Welcome " + userData.name()));
        add(new Button("Logout", event -> UI.getCurrent().navigate(LogoutView.class)));
    }

}
