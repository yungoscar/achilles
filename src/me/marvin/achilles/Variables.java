package me.marvin.achilles;

import me.marvin.achilles.utils.config.ConfigPath;
import me.marvin.achilles.utils.config.InitializeAfterConfig;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Variables {
    public static class Database {
        public static class Credentials {
            @ConfigPath(path = "database.host", config = "settings")
            public static String HOST = "";
            @ConfigPath(path = "database.user", config = "settings")
            public static String USER = "";
            @ConfigPath(path = "database.password", config = "settings")
            public static String PASSWORD = "";
            @ConfigPath(path = "database.port", config = "settings")
            public static int PORT = 3306;
        }

        @ConfigPath(path = "database.async", config = "settings")
        public static boolean ASYNC = false;
        @ConfigPath(path = "database.pool-size", config = "settings")
        public static int POOL_SIZE = 1;
        @ConfigPath(path = "database.database", config = "settings")
        public static String DATABASE_NAME = "";
        @ConfigPath(path = "database.tables.bans", config = "settings")
        public static String BAN_TABLE_NAME = "";
        @ConfigPath(path = "database.tables.kicks", config = "settings")
        public static String KICK_TABLE_NAME = "";
        @ConfigPath(path = "database.tables.mutes", config = "settings")
        public static String MUTE_TABLE_NAME = "";
        @ConfigPath(path = "database.tables.blacklists", config = "settings")
        public static String BLACKLIST_TABLE_NAME = "";
        @ConfigPath(path = "database.tables.alts", config = "settings")
        public static String ALTS_TABLE_NAME = "";
        @ConfigPath(path = "server-name", config = "settings")
        public static String SERVER_NAME = "";
    }

    public static class Other {
        @ConfigPath(path = "punishment.active.true", config = "settings")
        public static String PUNISHMENT_ACTIVE_TRUE = "";
        @ConfigPath(path = "punishment.active.false", config = "settings")
        public static String PUNISHMENT_ACTIVE_FALSE = "";
    }

    public static class Date {
        @ConfigPath(path = "date-formatting.pattern-settings.locale", config = "settings")
        private static String LOCALE_STRING = "";
        @ConfigPath(path = "date-formatting.pattern-settings.pattern", config = "settings")
        private static String DATE_STRING = "";

        @ConfigPath(path = "date-formatting.year", config = "settings")
        public static String YEAR = "";
        @ConfigPath(path = "date-formatting.years", config = "settings")
        public static String YEARS = "";
        @ConfigPath(path = "date-formatting.month", config = "settings")
        public static String MONTH = "";
        @ConfigPath(path = "date-formatting.months", config = "settings")
        public static String MONTHS = "";
        @ConfigPath(path = "date-formatting.week", config = "settings")
        public static String WEEK = "";
        @ConfigPath(path = "date-formatting.weeks", config = "settings")
        public static String WEEKS = "";
        @ConfigPath(path = "date-formatting.day", config = "settings")
        public static String DAY = "";
        @ConfigPath(path = "date-formatting.days", config = "settings")
        public static String DAYS = "";
        @ConfigPath(path = "date-formatting.hour", config = "settings")
        public static String HOUR = "";
        @ConfigPath(path = "date-formatting.hours", config = "settings")
        public static String HOURS = "";
        @ConfigPath(path = "date-formatting.minute", config = "settings")
        public static String MINUTE = "";
        @ConfigPath(path = "date-formatting.minutes", config = "settings")
        public static String MINUTES = "";
        @ConfigPath(path = "date-formatting.second", config = "settings")
        public static String SECOND = "";
        @ConfigPath(path = "date-formatting.seconds", config = "settings")
        public static String SECONDS = "";

        public static Locale LOCALE;
        public static SimpleDateFormat DATE_FORMAT;

        @InitializeAfterConfig(config = "settings")
        private static void loadLocale() {
            LOCALE = new Locale.Builder().setLanguageTag(LOCALE_STRING).build();
            DATE_FORMAT = new SimpleDateFormat(DATE_STRING, LOCALE);
        }
    }
}