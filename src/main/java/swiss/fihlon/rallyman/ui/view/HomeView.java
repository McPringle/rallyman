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
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.jetbrains.annotations.NotNull;
import swiss.fihlon.rallyman.security.AuthenticatedUser;
import swiss.fihlon.rallyman.service.DatabaseService;
import swiss.fihlon.rallyman.ui.component.EventOverview;

@AnonymousAllowed
@Route(value = "", layout = WebsiteLayout.class)
public final class HomeView extends Div {

    public HomeView(final @NotNull DatabaseService databaseService,
                    final @NotNull AuthenticatedUser authenticatedUser) {
        addClassName("home-view");
        add(new H1("Welcome to RallyMan"));

        if (authenticatedUser.isLoggedIn()) {
            add(new RouterLink("Logout", LogoutView.class));
        } else {
            add(new RouterLink("Login", LoginView.class));
            add(new RouterLink("Register", LoginView.class));
        }

        add(new EventOverview(databaseService));
    }

}
