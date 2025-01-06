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
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import swiss.fihlon.rallyman.data.TestUser;
import swiss.fihlon.rallyman.data.entity.Role;
import swiss.fihlon.rallyman.security.AuthenticationFailureEventListener;
import swiss.fihlon.rallyman.security.AuthenticationSuccessEventListener;
import swiss.fihlon.rallyman.security.LoginAttemptService;
import swiss.fihlon.rallyman.service.DatabaseService;
import swiss.fihlon.rallyman.ui.KaribuTest;

import java.util.List;

import static com.github.mvysny.kaributesting.v10.LocatorJ._assertOne;
import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginIT extends KaribuTest {

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private LoginAttemptService loginAttemptService;

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

    @Test
    void blockIP() {
        final var ip = "127.0.0.1";

        final var authentication = mock(Authentication.class);
        final var request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn(ip);

        final var exception = new BadCredentialsException("Block IP Test");
        final var failureEvent = new AuthenticationFailureBadCredentialsEvent(authentication, exception);
        final var failureListener = new AuthenticationFailureEventListener(request, loginAttemptService);
        final var successEvent = new AuthenticationSuccessEvent(authentication);
        final var successListener = new AuthenticationSuccessEventListener(request, loginAttemptService, databaseService);

        assertFalse(loginAttemptService.isBlocked(ip));
        failureListener.onApplicationEvent(failureEvent); // 1st login fail
        assertFalse(loginAttemptService.isBlocked(ip));   // ok
        failureListener.onApplicationEvent(failureEvent); // 2nd login fail
        assertFalse(loginAttemptService.isBlocked(ip));   // ok
        failureListener.onApplicationEvent(failureEvent); // 3rd login fail
        assertTrue(loginAttemptService.isBlocked(ip));    // blocked
        successListener.onApplicationEvent(successEvent); // login success
        assertFalse(loginAttemptService.isBlocked(ip));   // ok
    }

}
