package com.myfactory.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myfactory.health.pojo.Permission;
import com.myfactory.health.pojo.Role;
import com.myfactory.health.pojo.User;
import com.myfactory.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Gakki
 * @version 1.0
 * @description: spring security框架的整合；登录用户的认证与授权
 * @date 2021/1/13 19:59
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //重写loadUserByUsername方法，需要给他三个参数：用户名，用户密码，权限集合；用户名跟用户密码都是通过查询数据库而得。
        //根据登录用户名查询用户权限User（用户的权限需要配置）中间表进行连接
        //t_user -> t_user_role -> t_role -> t_role_permission -> t_permission
        //找出用户所拥有的角色。以及角色下的用户权限
        //User.roles(角色集合).permissions(权限集合)
        User user = userService.findByUsername(username);
        if (user != null) {
            // 用户名
            // 密码
            String password = user.getPassword();
            // 权限集合
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            // 授权

            // 用户所拥有的角色权限
            SimpleGrantedAuthority sai = null;
            //用户的角色可能有多个
            Set<Role> roles = user.getRoles();

            if (null != roles) {
                for (Role role : roles) {

                    // 角色用关键字, 授予角色
                    sai = new SimpleGrantedAuthority(role.getKeyword());
                    //将用户的角色添加到权限集合中
                    authorities.add(sai);
                    // 权限, 角色下的所有权限

                    Set<Permission> permissions = role.getPermissions();

                    if (null != permissions) {
                        for (Permission permission : permissions) {
                            // 授予权限
                            sai = new SimpleGrantedAuthority(permission.getKeyword());
                            authorities.add(sai);
                        }
                    }
                }
            }
            return new org.springframework.security.core.userdetails.User(username, password, authorities);
        }

        //返回null，限制访问
        return null;
    }
}
