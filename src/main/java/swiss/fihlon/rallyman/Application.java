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
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import swiss.fihlon.rallyman.configuration.AppConfig;
import swiss.fihlon.rallyman.security.SecurityConfiguration;

/**
 * <p>The entry point of the Spring Boot application.</p>
 *
 * <p>Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.</p>
 */
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(AppConfig.class)
@Push
@PageTitle("RallyMan - Organizing rallies as easily as possible")
@PWA(name = "RallyMan", shortName = "RallyMan")
@Theme("rallyman")
@SuppressWarnings({"FinalClass", "HideUtilityClassConstructor", "RegexpSingleline", "java:S106"})
public class Application implements AppShellConfigurator {

    private static final Option HASH_PASSWORD_OPTION = new Option("p", "password", true, "Hash password and exit");
    private static final Option HELP_OPTION = new Option("h", "help", false, "Show help and exit");

    public static void main(final @NotNull String[] args) throws ParseException {
        final var options = new Options();
        options.addOption(HASH_PASSWORD_OPTION);
        options.addOption(HELP_OPTION);

        final var cmd = new DefaultParser().parse(options, args);
        if (cmd.hasOption(HASH_PASSWORD_OPTION)) {
            final var password = cmd.getOptionValue(HASH_PASSWORD_OPTION);
            final var hashedPassword = SecurityConfiguration.PASSWORD_ENCODER.encode(password);
            System.out.println("Hashed password: " + hashedPassword);
        } else if (cmd.hasOption(HELP_OPTION)) {
            final var helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("java -jar apus.jar", options, true);
        } else {
            SpringApplication.run(Application.class, args);
        }
    }

}
