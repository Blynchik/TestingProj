package com.dmdev;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.CreateUserDto;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.exception.ValidationException;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class StartTest {

    @Nested
    @DisplayName(value = "UserDao tests")
    class UserDaoTest extends IntegrationTestBase {

        private UserDao userDao;

        @BeforeEach
        void prepareTests() {
            userDao = UserDao.getInstance();
        }

        @Test
        void testFindAll() {
            //given
            User expectedUser = new User();
            User currentUser;
            //when
            List<User> users = userDao.findAll();

            //then
            assertEquals(5, users.size());

            currentUser = users.get(0);
            expectedUser.setId(1);
            expectedUser.setName("Ivan");
            expectedUser.setBirthday(LocalDate.of(1990, 1, 10));
            expectedUser.setEmail("ivan@gmail.com");
            expectedUser.setPassword("111");
            expectedUser.setRole(Role.ADMIN);
            expectedUser.setGender(Gender.MALE);
            assertUsers(expectedUser, currentUser);

            currentUser = users.get(1);
            expectedUser.setId(2);
            expectedUser.setName("Petr");
            expectedUser.setBirthday(LocalDate.of(1995, 10, 19));
            expectedUser.setEmail("petr@gmail.com");
            expectedUser.setPassword("123");
            expectedUser.setRole(Role.USER);
            expectedUser.setGender(Gender.MALE);
            assertUsers(expectedUser, currentUser);

            currentUser = users.get(2);
            expectedUser.setId(3);
            expectedUser.setName("Sveta");
            expectedUser.setBirthday(LocalDate.of(2001, 12, 23));
            expectedUser.setEmail("sveta@gmail.com");
            expectedUser.setPassword("321");
            expectedUser.setRole(Role.USER);
            expectedUser.setGender(Gender.FEMALE);
            assertUsers(expectedUser, currentUser);

            currentUser = users.get(3);
            expectedUser.setId(4);
            expectedUser.setName("Vlad");
            expectedUser.setBirthday(LocalDate.of(1984, 3, 14));
            expectedUser.setEmail("vlad@gmail.com");
            expectedUser.setPassword("456");
            expectedUser.setRole(Role.USER);
            expectedUser.setGender(Gender.MALE);
            assertUsers(expectedUser, currentUser);

            currentUser = users.get(4);
            expectedUser.setId(5);
            expectedUser.setName("Kate");
            expectedUser.setBirthday(LocalDate.of(1989, 8, 9));
            expectedUser.setEmail("kate@gmail.com");
            expectedUser.setPassword("777");
            expectedUser.setRole(Role.ADMIN);
            expectedUser.setGender(Gender.FEMALE);
            assertUsers(expectedUser, currentUser);
        }

        @Test
        void testFindById_IfExist() {
            //given
            User expectedUser = new User(1, "Ivan", LocalDate.of(1990, 1, 10), "ivan@gmail.com", "111", Role.ADMIN, Gender.MALE);
            //when
            Optional<User> user = userDao.findById(1);
            //then
            if (user.isPresent()) {
                User currentUser = user.get();
                assertUsers(expectedUser, currentUser);
            } else {
                fail("Wrong ID");
            }
        }

        @Test
        void testFindById_NotExist() {
            //given
            //when
            Optional<User> user = userDao.findById(100);
            //then
            assertTrue(user.isEmpty());
        }

        @Test
        void testSave_withCorrectData() {
            //given
            User expectedUser = new User(6, "Ivan", LocalDate.of(1992, 10, 13), "ivanoff@gmail.com", "111", Role.ADMIN, Gender.MALE);
            //when
            User currentUser = userDao.save(new User("Ivan", LocalDate.of(1992, 10, 13), "ivanoff@gmail.com", "111", Role.ADMIN, Gender.MALE));
            //then
            if (currentUser != null) {
                assertUsers(expectedUser, currentUser);
            }
        }

        @Test
        void testSave_WithUncorrectData() {
            //given
            //then
            //when
            assertThrows(Exception.class, () -> userDao.save(new User()));
        }

        @Test
        void testSave_WhenIdNotUnique() {
            //given
            User expectedUser = new User(1, "Ivan", LocalDate.of(1992, 10, 13), "ivanoff@gmail.com", "111", Role.ADMIN, Gender.MALE);
            //when
            User currentUser = userDao.save(new User(1, "Ivan", LocalDate.of(1992, 10, 13), "ivanoff@gmail.com", "111", Role.ADMIN, Gender.MALE));
            //then
            if (currentUser != null) {
                assertNotEquals(expectedUser.getId(), currentUser.getId());
                assertEquals(expectedUser.getName(), currentUser.getName());
                assertEquals(expectedUser.getBirthday(), currentUser.getBirthday());
                assertEquals(expectedUser.getEmail(), currentUser.getEmail());
                assertEquals(expectedUser.getPassword(), currentUser.getPassword());
                assertEquals(expectedUser.getRole(), currentUser.getRole());
                assertEquals(expectedUser.getGender(), currentUser.getGender());
            }
        }

        @Test
        void testSave_WhenEmailNotUnique() {
            //given
            User testUser = new User("Ivan", LocalDate.of(1992, 10, 13), "ivan@gmail.com", "111", Role.ADMIN, Gender.MALE);
            //then
            //when
            assertThrows(Exception.class, () -> userDao.save(testUser));
        }

        @Test
        void findByEmailAndPassword_WhenCorrect() {
            //given
            User expectedUser = new User(1, "Ivan", LocalDate.of(1990, 1, 10), "ivan@gmail.com", "111", Role.ADMIN, Gender.MALE);
            //when
            Optional<User> optionalUser = userDao.findByEmailAndPassword(expectedUser.getEmail(), expectedUser.getPassword());
            //then
            if (optionalUser.isPresent()) {
                User currentUser = optionalUser.get();
                assertUsers(expectedUser, currentUser);
            } else {
                fail("Wrong credentials");
            }
        }

        @Test
        void findByEmailAndPassword_WhenUncorrect() {
            //given
            //when
            Optional<User> optionalUser = userDao.findByEmailAndPassword("noone@mail.ru", "999");
            //then
            assertTrue(optionalUser.isEmpty());
        }

        @Test
        void delete_ifExist() {
            //given
            //when
            //then
            assertTrue(userDao.delete(1));
        }

        @Test
        void delete_ifNotExist() {
            //given
            //when
            //then
            assertFalse(userDao.delete(100));
        }

        @Test
        void update_ifExist() {
            //given
            User expectedUser = new User(1, "Vasiliy", LocalDate.of(1999, 9, 9), "999@gmail.com", "999", Role.USER, Gender.FEMALE);
            //when
            userDao.update(new User(1, "Vasiliy", LocalDate.of(1999, 9, 9), "999@gmail.com", "999", Role.USER, Gender.FEMALE));
            //then
            Optional<User> optionalUser = userDao.findById(1);
            if (optionalUser.isPresent()) {
                User currentUser = optionalUser.get();
                assertUsers(expectedUser, currentUser);
            } else {
                fail("Wrong ID");
            }
        }

        @Test
        void update_ifNotExist() {
            //given
            User testUser = new User(100, "Vasiliy", LocalDate.of(1999, 9, 9), "999@gmail.com", "999", Role.USER, Gender.FEMALE);
            //when
            userDao.update(testUser);
            //then
            var optionalUser = userDao.findById(100);
            assertTrue(optionalUser.isEmpty());
        }
    }

    @Nested
    @DisplayName(value = "UserDto tests")
    class UserDtoTest {

        private final Integer ID = 1;
        private final String NAME = "Test";
        private final LocalDate BIRTHDAY = LocalDate.of(1999, 9, 9);
        private final String EMAIL = "9999@example.com";
        private final String PASSWORD = "password123";
        private final String IMAGE = "lhvKVkjhvUVkjb8Y9";
        private final Role ROLE = Role.ADMIN;
        private final Gender GENDER = Gender.FEMALE;

        @Test
        void creatUserDtoTest() {
            //given
            CreateUserDto userDto = CreateUserDto.builder()
                    .name(NAME)
                    .birthday(BIRTHDAY.toString())
                    .email(EMAIL)
                    .password(PASSWORD)
                    .role(ROLE.name())
                    .gender(GENDER.name())
                    .build();
            //when
            //then
            assertEquals(NAME, userDto.getName());
            assertEquals(BIRTHDAY.toString(), userDto.getBirthday());
            assertEquals(EMAIL, userDto.getEmail());
            assertEquals(PASSWORD, userDto.getPassword());
            assertEquals(ROLE.name(), userDto.getRole());
            assertEquals(GENDER.name(), userDto.getGender());
        }

        @Test
        void testCreateUserDtoEquals_ifTrue(){
            //given
            CreateUserDto userDto1 = CreateUserDto.builder()
                    .name(NAME)
                    .birthday(BIRTHDAY.toString())
                    .email(EMAIL)
                    .password(PASSWORD)
                    .role(ROLE.name())
                    .gender(GENDER.name())
                    .build();

            CreateUserDto userDto2 = CreateUserDto.builder()
                    .name(NAME)
                    .birthday(BIRTHDAY.toString())
                    .email(EMAIL)
                    .password(PASSWORD)
                    .role(ROLE.name())
                    .gender(GENDER.name())
                    .build();

            assertTrue(userDto1.equals(userDto2));
        }

        @Test
        void testCreateUserDtoEquals_ifFalse(){
            //given
            CreateUserDto userDto1 = CreateUserDto.builder()
                    .name(NAME)
                    .birthday(BIRTHDAY.toString())
                    .email(EMAIL)
                    .password(PASSWORD)
                    .role(ROLE.name())
                    .gender(GENDER.name())
                    .build();

            CreateUserDto userDto2 = CreateUserDto.builder()
                    .name("WRONG")
                    .birthday(BIRTHDAY.toString())
                    .email("WRONG")
                    .password("WRONG")
                    .role(ROLE.name())
                    .gender(GENDER.name())
                    .build();

            assertFalse(userDto1.equals(userDto2));
        }

        @Test
        void userDtoTest() {
            //given
            UserDto userDto = UserDto.builder()
                    .id(ID)
                    .name(NAME)
                    .birthday(BIRTHDAY)
                    .email(EMAIL)
                    .image(IMAGE)
                    .role(ROLE)
                    .gender(GENDER)
                    .build();
            //when
            //then
            assertEquals(ID, userDto.getId());
            assertEquals(NAME, userDto.getName());
            assertEquals(BIRTHDAY, userDto.getBirthday());
            assertEquals(EMAIL, userDto.getEmail());
            assertEquals(IMAGE, userDto.getImage());
            assertEquals(ROLE, userDto.getRole());
            assertEquals(GENDER, userDto.getGender());
        }

        @Test
        void testUserDtoEquals_ifTrue(){
            //given
            UserDto userDto1 = UserDto.builder()
                    .id(ID)
                    .name(NAME)
                    .birthday(BIRTHDAY)
                    .email(EMAIL)
                    .image(null)
                    .role(ROLE)
                    .gender(GENDER)
                    .build();

            UserDto userDto2 = UserDto.builder()
                    .id(ID)
                    .name(NAME)
                    .birthday(BIRTHDAY)
                    .email(EMAIL)
                    .image(null)
                    .role(ROLE)
                    .gender(GENDER)
                    .build();

            assertTrue(userDto1.equals(userDto2));
        }

        @Test
        void testUserDtoEquals_ifFalse(){
            //given
            UserDto userDto1 = UserDto.builder()
                    .id(ID)
                    .name(NAME)
                    .birthday(BIRTHDAY)
                    .email(EMAIL)
                    .image(null)
                    .role(ROLE)
                    .gender(GENDER)
                    .build();

            UserDto userDto2 = UserDto.builder()
                    .id(2)
                    .name("WRONG")
                    .birthday(BIRTHDAY)
                    .email("WRONG")
                    .image(null)
                    .role(ROLE)
                    .gender(GENDER)
                    .build();

            assertNotEquals(userDto1, userDto2);
        }

    }

    @Nested
    @DisplayName(value = "User tests")
    class UserTest {
        private final Integer ID = 1;
        private final String NAME = "Test";
        private final LocalDate BIRTHDAY = LocalDate.of(1999, 9, 9);
        private final String EMAIL = "9999@example.com";
        private final String PASSWORD = "password123";
        private final Role ROLE = Role.ADMIN;
        private final Gender GENDER = Gender.FEMALE;

        @Test
        void userTest() {
            //given
            User user = User.builder()
                    .id(ID)
                    .name(NAME)
                    .birthday(BIRTHDAY)
                    .email(EMAIL)
                    .password(PASSWORD)
                    .role(ROLE)
                    .gender(GENDER)
                    .build();
            //when
            //then
            assertEquals(ID, user.getId());
            assertEquals(NAME, user.getName());
            assertEquals(BIRTHDAY, user.getBirthday());
            assertEquals(EMAIL, user.getEmail());
            assertEquals(PASSWORD, user.getPassword());
            assertEquals(ROLE, user.getRole());
            assertEquals(GENDER, user.getGender());
        }
    }

    @Nested
    @DisplayName(value = "Mapper tests")
    class MapperTest {

        private final Integer ID = 1;
        private final String NAME = "Test";
        private final LocalDate BIRTHDAY = LocalDate.of(1999, 9, 9);
        private final String EMAIL = "9999@example.com";
        private final String PASSWORD = "password123";
        private final String IMAGE = "lhvKVkjhvUVkjb8Y9";
        private final Role ROLE = Role.ADMIN;
        private final Gender GENDER = Gender.FEMALE;

        private CreateUserMapper createUserMapper;
        private UserMapper userMapper;

        @BeforeEach
        void prepareMapperTest() {
            createUserMapper = CreateUserMapper.getInstance();
            userMapper = UserMapper.getInstance();
        }

        @Test
        void createUserMapperMapTest_WithAllData() {
            //given
            CreateUserDto userDto = CreateUserDto.builder()
                    .name(NAME)
                    .birthday(BIRTHDAY.toString())
                    .email(EMAIL)
                    .password(PASSWORD)
                    .role(ROLE.name())
                    .gender(GENDER.name())
                    .build();
            User expectedUser = new User(NAME, BIRTHDAY, EMAIL, PASSWORD, ROLE, GENDER);
            //when
            User currentUser = createUserMapper.map(userDto);
            //then
            assertUsers(expectedUser, currentUser);
        }

        @Test
        void createUserMapperMapTest_WithNotAllData() {
            //given
            CreateUserDto userDto = CreateUserDto.builder()
                    .name(NAME)
                    .birthday(BIRTHDAY.toString())
                    .email(EMAIL)
                    .password(PASSWORD)
                    .build();
            User expectedUser = new User(NAME, BIRTHDAY, EMAIL, PASSWORD, null, null);
            //when
            User currentUser = createUserMapper.map(userDto);
            //then
            assertUsers(expectedUser, currentUser);
        }

        @Test
        void userMapperMapTest_WithAllData() {
            //given
            User user = User.builder()
                    .id(ID)
                    .name(NAME)
                    .birthday(BIRTHDAY)
                    .email(EMAIL)
                    .role(ROLE)
                    .gender(GENDER)
                    .build();

            UserDto expectedUserDto = UserDto.builder()
                    .id(ID)
                    .name(NAME)
                    .birthday(BIRTHDAY)
                    .email(EMAIL)
                    .image(null)
                    .role(ROLE)
                    .gender(GENDER)
                    .build();
            //when
            UserDto currentUserDto = userMapper.map(user);
            //then
            assertUserDto(expectedUserDto, currentUserDto);
        }
    }

    @Nested
    @DisplayName(value = "Service tests")
    class ServiceTest extends IntegrationTestBase {

        private UserService userService;

        @BeforeEach
        void prepareTest() {
            userService = UserService.getInstance();
        }

        @Test
        void testLogin_WithCorrectData() {
            //given
            String password = "111";
            UserDto expectedUserDto = UserDto.builder()
                    .id(1)
                    .name("Ivan")
                    .birthday(LocalDate.of(1990, 1, 10))
                    .email("ivan@gmail.com")
                    .image(null)
                    .role(Role.ADMIN)
                    .gender(Gender.MALE)
                    .build();
            //when
            var optionalUserDto = userService.login(expectedUserDto.getEmail(), password);
            //then
            if (optionalUserDto.isPresent()) {
                UserDto currentUserDto = optionalUserDto.get();
                assertUserDto(expectedUserDto, currentUserDto);
            } else {
                fail("Wrong credentials");
            }
        }

        @Test
        void testLogin_WithUncorrectData() {
            //given
            //when
            var optionalUserDto = userService.login("wrong@mail.com", "9999");
            //then
            assertTrue(optionalUserDto.isEmpty());
        }

        @Test
        void testCreate_WhenCorrect() {
            //given
            CreateUserDto userDto = CreateUserDto.builder()
                    .name("Test")
                    .birthday("1999-09-09")
                    .email("test@mail.com")
                    .password("999")
                    .role("USER")
                    .gender("MALE")
                    .build();
            //when
            UserDto currentUserDto = userService.create(userDto);
            //then
            assertEquals(currentUserDto.getName(), userDto.getName());
            assertEquals(currentUserDto.getBirthday().toString(), userDto.getBirthday());
            assertEquals(currentUserDto.getEmail(), userDto.getEmail());
            assertNull(currentUserDto.getImage());
            assertEquals(currentUserDto.getRole().name(), userDto.getRole());
            assertEquals(currentUserDto.getGender().name(), userDto.getGender());
        }

        @Test
        void testCreate_WithUncorrectBirthday() {
            //given
            CreateUserDto userDto = CreateUserDto.builder()
                    .name("Test")
                    .birthday("1999-99-99")
                    .email("test@mail.com")
                    .password("999")
                    .role("USER")
                    .gender("MALE")
                    .build();
            //when
            //then
            assertThrows(ValidationException.class, () -> userService.create(userDto));
        }

        @Test
        void testCreate_WithUncorrectGender(){
            //given
            CreateUserDto userDto = CreateUserDto.builder()
                    .name("Test")
                    .birthday("1999-09-09")
                    .email("test@mail.com")
                    .password("999")
                    .role("USER")
                    .gender("XY")
                    .build();
            //when
            //then
            assertThrows(ValidationException.class, ()->userService.create(userDto));
        }

        @Test
        void testCreate_WithUncorrectRole(){
            //given
            CreateUserDto userDto = CreateUserDto.builder()
                    .name("Test")
                    .birthday("1999-09-09")
                    .email("test@mail.com")
                    .password("999")
                    .role("MODERATOR")
                    .gender("MALE")
                    .build();
            //when
            //then
            assertThrows(ValidationException.class, ()->userService.create(userDto));
        }
    }

    private void assertUsers(User expectedUser, User currentUser) {
        assertEquals(expectedUser.getId(), currentUser.getId());
        assertEquals(expectedUser.getName(), currentUser.getName());
        assertEquals(expectedUser.getBirthday(), currentUser.getBirthday());
        assertEquals(expectedUser.getEmail(), currentUser.getEmail());
        assertEquals(expectedUser.getPassword(), currentUser.getPassword());
        assertEquals(expectedUser.getRole(), currentUser.getRole());
        assertEquals(expectedUser.getGender(), currentUser.getGender());
    }

    private void assertUserDto(UserDto expectedUserDto, UserDto currentUserDto) {
        assertEquals(expectedUserDto.getId(), currentUserDto.getId());
        assertEquals(expectedUserDto.getName(), currentUserDto.getName());
        assertEquals(expectedUserDto.getBirthday(), expectedUserDto.getBirthday());
        assertEquals(expectedUserDto.getEmail(), expectedUserDto.getEmail());
        assertEquals(expectedUserDto.getImage(), expectedUserDto.getImage());
        assertEquals(expectedUserDto.getRole(), expectedUserDto.getRole());
        assertEquals(expectedUserDto.getGender(), expectedUserDto.getGender());
    }
}
