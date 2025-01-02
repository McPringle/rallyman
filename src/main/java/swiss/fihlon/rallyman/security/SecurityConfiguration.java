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
package swiss.fihlon.rallyman.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import swiss.fihlon.rallyman.ui.view.LoginView;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
@SuppressWarnings("checkstyle:DesignForExtension") // Spring configurations can be subclassed by the Spring Framework
public class SecurityConfiguration extends VaadinWebSecurity {

    @NotNull
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(10, new SecureRandom());

    @Override
    protected void configure(@NotNull final HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class, "/");
    }

    @Override
    protected void configure(@NotNull final WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().requestMatchers(
                // Client-side JS
                "/VAADIN/**",

                // the standard favicon URI
                "/favicon.ico",

                // the robots exclusion standard
                "/robots.txt",

                // web application manifest
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",

                // icons and images
                "/icons/**",
                "/images/**",
                "/styles/**",
                "/*.png" // TODO remove when 3rd party library bug was fixed:
                           // https://github.com/xdev-software/vaadin-maps-leaflet-flow/issues/485
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PASSWORD_ENCODER;
    }

}
