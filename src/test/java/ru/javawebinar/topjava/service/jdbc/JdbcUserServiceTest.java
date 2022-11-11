package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTest;

@ActiveProfiles(profiles = {Profiles.JDBC, Profiles.POSTGRES_DB}, inheritProfiles = false)
public class JdbcUserServiceTest extends UserServiceTest {
}
