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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public final class LoginAttemptService {

    private final LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        this(CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.DAYS)
                .build(new CacheLoader<>() {
                    @Override
                    public @NotNull Integer load(@NotNull final String ip) {
                        return 0;
                    }
                }));
    }

    @Autowired(required = false) // this constructor is for tests only
    public LoginAttemptService(@NotNull final LoadingCache<String, Integer> attemptsCache) {
        super();
        this.attemptsCache = attemptsCache;
    }

    public void loginSucceeded(@NotNull final String ip) {
        attemptsCache.invalidate(ip);
    }

    public void loginFailed(@NotNull final String ip) {
        int attempts;
        try {
            attempts = attemptsCache.get(ip);
        } catch (final ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(ip, attempts);
    }

    public boolean isBlocked(@NotNull final String ip) {
        try {
            return attemptsCache.get(ip) >= 3;
        } catch (final ExecutionException e) {
            return false;
        }
    }
}
