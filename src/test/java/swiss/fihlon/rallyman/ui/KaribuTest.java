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
package swiss.fihlon.rallyman.ui;

import com.github.mvysny.fakeservlet.FakeRequest;
import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.spring.MockSpringServlet;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import swiss.fihlon.rallyman.data.entity.Role;

import java.util.List;

/**
 * An abstract class which sets up Spring, Karibu-Testing and your app.
 * The easiest way to use this class in your tests is having your test class to extend
 * this class.
 */
@SpringBootTest
@DirtiesContext
public abstract class KaribuTest {

    @Autowired
    private ApplicationContext applicationContext;

    @RegisterExtension
    protected static final GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig()
                    .withUser("rallyman", "s3cr3t"))
            .withPerMethodLifecycle(false);

    private static Routes routes;

    @BeforeAll
    public static void discoverRoutes() {
        routes = new Routes().autoDiscoverViews("swiss.fihlon.rallyman");
    }

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    public void setup() throws FolderException {
        final Function0<UI> uiFactory = UI::new;
        final var servlet = new MockSpringServlet(routes, applicationContext, uiFactory);
        MockVaadin.setup(uiFactory, servlet);
        greenMail.purgeEmailFromAllMailboxes();
    }

    @AfterEach
    public void tearDown() {
        MockVaadin.tearDown();
    }

    protected void login(@NotNull final String user, @NotNull final String password, @NotNull final List<Role> roles) {
        // taken from https://www.baeldung.com/manually-set-user-authentication-spring-security
        // also see https://github.com/mvysny/karibu-testing/issues/47 for more details.
        final var authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
        final var authenticationToken = new UsernamePasswordAuthenticationToken(user, password, authorities);
        final var securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authenticationToken);

        // however, you also need to make sure that ViewAccessChecker works properly;
        // that requires a correct FakeRequest.userPrincipal and FakeRequest.isUserInRole()
        final var request = (FakeRequest) VaadinServletRequest.getCurrent().getRequest();
        request.setUserPrincipalInt(authenticationToken);
        request.setUserInRole((principal, role) -> roles.contains(Role.valueOf(role)));
    }

    @AfterEach
    protected void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        if (VaadinServletRequest.getCurrent() != null) {
            final var request = (FakeRequest) VaadinServletRequest.getCurrent().getRequest();
            request.setUserPrincipalInt(null);
            request.setUserInRole((principal, role) -> false);
        }
    }

}
