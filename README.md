# LingvoHub Telegram Bot

LingvoHub is a Java Telegram bot for browsing language courses, teachers, and lessons. Lesson progress and catalog data are stored locally in SQLite.

## Requirements

- Java 21
- Maven 3.9 or later
- A Telegram bot token from [@BotFather](https://t.me/BotFather)

## Local configuration

Copy `src/main/resources/application-local.example.yml` to the project root as `application-local.yml`. The local file is ignored by Git and excluded from the packaged JAR. Set your Telegram bot token, username, and administrator Telegram IDs in it. Do not commit real credentials.

The local profile uses `./database.db` in the project directory. Hibernate updates the existing schema in place; the application does not recreate the database on restart.

## Run locally

From IntelliJ IDEA, run `uz.lingvohub.bot.LingvoHubApplication` with the active profile set to `local` in the Run Configuration and the project root as the working directory.

From Windows PowerShell:

```powershell
.\run-local.ps1
```

From Git Bash:

```bash
./run-local.sh
```

The first run of either script creates the ignored root-level local configuration file from the example if it is missing. If an older ignored configuration exists under `src/main/resources`, the script copies it to the root first. Fill in credentials if needed, then run the script again.

You can also start the bot directly with Maven:

```powershell
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```

Or package and run it:

```powershell
mvn clean package
java -jar target/lingvohub-bot-1.0.0.jar --spring.profiles.active=local
```

To use environment variables instead of a local YAML file, set `TELEGRAM_BOT_TOKEN`, `TELEGRAM_BOT_USERNAME`, and `ADMIN_IDS`, then run without the `local` profile. The default SQLite file remains `database.db` in the current working directory.

## Tests

Run the automated tests with:

```powershell
mvn test
```

## Bot commands

- `/start` opens language selection.
- `/admin` opens the administrator menu for configured administrator IDs.
- `/stats` shows basic statistics to administrators.

The bot uses Telegram long polling. Closing the Spring application stops the polling session.
