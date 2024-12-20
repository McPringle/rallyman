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
package swiss.fihlon.rallyman;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>The entry point of the Spring Boot application.</p>
 *
 * <p>Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.</p>
 */
@SpringBootApplication
@EnableScheduling
@Push
@PageTitle("RallyMan - Organizing rallies as easily as possible")
@PWA(name = "RallyMan", shortName = "RallyMan")
@Theme("rallyman")
@SuppressWarnings({"FinalClass", "HideUtilityClassConstructor"})
public class Application implements AppShellConfigurator {

    public static void main(@NotNull final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
