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
import com.vaadin.flow.router.RouterLink;
import org.junit.jupiter.api.Test;
import swiss.fihlon.rallyman.data.TestUser;
import swiss.fihlon.rallyman.data.entity.Role;
import swiss.fihlon.rallyman.ui.KaribuTest;

import java.util.List;

import static com.github.mvysny.kaributesting.v10.LocatorJ._assertNone;
import static com.github.mvysny.kaributesting.v10.LocatorJ._assertOne;

class HomeIT extends KaribuTest {

    @Test
    @SuppressWarnings("java:S2699") // false positive: Karibu assertions are not recognized
    void homeViewNotLoggedIn() {
        UI.getCurrent().navigate(HomeView.class);
        UI.getCurrent().getPage().reload();

        _assertOne(HomeView.class);
        _assertOne(RouterLink.class, spec -> spec.withText("Login"));
        _assertOne(RouterLink.class, spec -> spec.withText("Register"));
        _assertNone(RouterLink.class, spec -> spec.withText("Logout"));
    }

    @Test
    @SuppressWarnings("java:S2699") // false positive: Karibu assertions are not recognized
    void homeViewLoggedIn() {
        login(TestUser.EMAIL, TestUser.PASSWORD, List.of(Role.USER));

        UI.getCurrent().navigate(HomeView.class);
        UI.getCurrent().getPage().reload();

        _assertOne(HomeView.class);
        _assertNone(RouterLink.class, spec -> spec.withText("Login"));
        _assertNone(RouterLink.class, spec -> spec.withText("Register"));
        _assertOne(RouterLink.class, spec -> spec.withText("Logout"));

        logout();
    }

}
