package com.example.managebyclesystem.service;

import com.example.managebyclesystem.model.Staff;
import com.example.managebyclesystem.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private StaffRepository staffRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Staff staff = staffRepository.findByUsername(username) //
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với tên đăng nhập: " + username));

        if (staff.getStaffStatus() == Staff.StaffStatus.Disable) { //
            throw new UsernameNotFoundException("Tài khoản người dùng đã bị vô hiệu hóa: " + username);
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(staff.getRole().name()); //
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);

        return new User(staff.getUsername(), staff.getPassword(), authorities); //
    }
}