# RallyMan

[![All Tests](https://github.com/McPringle/rallyman/actions/workflows/all-tests.yml/badge.svg)](https://github.com/McPringle/rallyman/actions/workflows/all-tests.yml)
[![codecov](https://codecov.io/gh/McPringle/rallyman/graph/badge.svg?token=OPDR66ID7D)](https://codecov.io/gh/McPringle/rallyman)

**RallyMan is a web-based application for organizing rallies as easily as possible.**

## Contributing

You can find a lot of information on how you can contribute to *RallyMan* in the separate file [CONTRIBUTING.md](CONTRIBUTING.md). A curated list of contributors is available in the file [CONTRIBUTORS.md](CONTRIBUTORS.md).

## Communication

### Matrix Chat

There is a channel at Matrix for quick and easy communication. This is publicly accessible for everyone. For developers as well as users. The communication in this chat is to be regarded as short-lived and has no documentary character.

You can find our Matrix channel here: [@rallyman:ijug.eu](https://matrix.to/#/%23rallyman:ijug.eu)

### GitHub Discussions

We use the corresponding GitHub function for discussions. The discussions held here are long-lived and divided into categories for the sake of clarity. One important category, for example, is that for questions and answers.

Discussions on GitHub: https://github.com/McPringle/rallyman/discussions  
Questions and Answers: https://github.com/McPringle/rallyman/discussions/categories/q-a

## Configuration

The file `application.properties` contains only some default values. To override the default values and to specify other configuration options, just set them as environment variables. The following sections describe all available configuration options. You only need to specify these options if your configuration settings differ from the defaults.

### Server

The server runs on port 8080 by default. If you don't like it, change it:

```
PORT=8080
```

### Database

*RallyMan* needs a database to store the business data. By default, *RallyMan* comes with [MariaDB](https://mariadb.org/) drivers. MariaDB is recommended because we are using it during development, and it is highly tested with *RallyMan*. All free and open source JDBC compatible databases are supported, but you need to configure the JDBC driver dependencies accordingly. Please make sure that your database is using a Unicode character set to avoid problems storing data containing Unicode characters.

The `DB_USER` is used to access the *RallyMan* database including automatic schema migrations and needs `ALL PRIVILEGES`.

```
DB_URL=jdbc:mariadb://localhost:3306/rallyman?serverTimezone\=Europe/Zurich&allowMultiQueries=true
DB_USER=johndoe
DB_PASS=verysecret
```

The database schema will be migrated automatically by *RallyMan*.

#### Important MySQL and MariaDB configuration

MySQL and MariaDB have a possible silent truncation problem with the `GROUP_CONCAT` command. To avoid this it is necessary, to configure these two databases to allow multi queries. Just add `allowMultiQueries=true` to the JDBC database URL like in this example (you may need to scroll the example code to the right):

```
DB_URL=jdbc:mariadb://localhost:3306/rallyman?serverTimezone\=Europe/Zurich&allowMultiQueries=true
```

## Copyright and License

[AGPL License](https://www.gnu.org/licenses/agpl-3.0.de.html)

*Copyright (C) Marcus Fihlon and the individual contributors to **RallyMan**.*

This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
