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
import com.vaadin.flow.component.login.LoginForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import swiss.fihlon.rallyman.data.TestUser;
import swiss.fihlon.rallyman.data.entity.Role;
import swiss.fihlon.rallyman.service.DatabaseService;
import swiss.fihlon.rallyman.ui.KaribuTest;

import java.util.List;

import static com.github.mvysny.kaributesting.v10.LocatorJ._assertOne;
import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class LoginIT extends KaribuTest {

    @Autowired
    private DatabaseService databaseService;

    @Test
    void loginAndLogout() {

        // not logged in: call to profile page forwards to login page
        UI.getCurrent().navigate(ProfileView.class);
        UI.getCurrent().getPage().reload();
        _assertOne(LoginView.class);
        _assertOne(LoginForm.class);

        // do a login and check for the change of the last login date and time
        assertNull(databaseService.getUserByEmail(TestUser.EMAIL).orElseThrow().lastLogin());
        login(TestUser.EMAIL, TestUser.PASSWORD, List.of(Role.USER));
        assertNotNull(databaseService.getUserByEmail(TestUser.EMAIL).orElseThrow().lastLogin());

        // logged in: profile page is accessible
        UI.getCurrent().navigate(ProfileView.class);
        UI.getCurrent().getPage().reload();
        _assertOne(ProfileView.class);

        // navigate to logout
        final var logoutButton = _get(Button.class, spec -> spec.withText("Logout"));
        _click(logoutButton);
        _assertOne(LogoutView.class);

        // do a fake logout
        logout();

        // not logged in: call to profile page forwards to login page again
        UI.getCurrent().navigate(ProfileView.class);
        UI.getCurrent().getPage().reload();
        _assertOne(LoginView.class);
        _assertOne(LoginForm.class);
    }

}
