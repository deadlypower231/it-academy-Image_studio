package com.mironov.image.studio.services;

import com.mironov.image.studio.api.dao.IRoleDao;
import com.mironov.image.studio.api.dao.IUserDao;
import com.mironov.image.studio.api.dto.UserDto;
import com.mironov.image.studio.api.dto.UserRolesDto;
import com.mironov.image.studio.api.mappers.UserMapper;
import com.mironov.image.studio.api.services.IUserService;
import com.mironov.image.studio.entities.Role;
import com.mironov.image.studio.entities.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements IUserService {

    private final PasswordEncoder passwordEncoder;
    private final IUserDao userDao;
    private final IRoleDao roleDao;

    public UserService(PasswordEncoder passwordEncoder, IUserDao userDao, IRoleDao roleDao) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    public User getUser(long id) {
        return this.userDao.get(id);
    }

    @Override
    public UserDto findUserByName(String name) {
        return UserMapper.mapUserDto(this.userDao.getByName(name));
    }

    @Override
    public List<UserDto> getAll() {
        return UserMapper.mapUserDtos(this.userDao.getAll());
    }

    @Override
    @Transactional
    public void delete(long id) {
        this.userDao.delete(this.userDao.get(id));
    }

    @Override
    @Transactional
    public void updateUserRoles(UserRolesDto userRolesDto, long id) {
        User user = this.userDao.get(id);
        List<Role> rolesDB = this.roleDao.getAll();
        rolesDB.removeIf(x -> !userRolesDto.getRoles().toString().contains(x.toString()));
        user.setRoles(rolesDB);
        this.userDao.update(user);
    }

    //todo 2 метода в 1
    @Transactional
    public UserDto createUser(UserDto userDto) {
        User savedUser = this.userDao.create(UserMapper.createMapUser(userDto));
        savedUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return UserMapper.mapUserDto(savedUser);
    }

    @Transactional
    public void addUserRole(UserDto userDto) {
        List<Role> roles = this.roleDao.getAll();
        userDto.setRoles(Collections.singletonList(roles.get(0)));
        this.userDao.update(UserMapper.mapUser(userDto));
    }
}
