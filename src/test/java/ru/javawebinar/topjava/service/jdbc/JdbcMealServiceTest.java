package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles(profiles = {Profiles.JDBC, Profiles.POSTGRES_DB}, inheritProfiles = false)
public class JdbcMealServiceTest extends MealServiceTest {
}