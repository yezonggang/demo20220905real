package com.quick.start.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quick.start.demo.VO.LoginInfoVO;
import com.quick.start.demo.entity.*;
import com.quick.start.demo.framework.exception.ApiError;
import com.quick.start.demo.framework.exception.ApiErrorEnum;
import com.quick.start.demo.framework.response.Either;
import com.quick.start.demo.framework.response.ResponseData;
import com.quick.start.demo.framework.response.ResponseMsgUtil;
import com.quick.start.demo.mapper.*;
import com.quick.start.demo.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.start.demo.utils.JsonWebTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yzg
 * @since 2022-09-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService, UserDetailsService {


    @Autowired
    UserMapper userMapper;
    @Autowired
    AccountStateMapper accountStateDao;
    @Autowired
    RefreshTokenMapper refreshTokenDao;
    @Autowired
    JsonWebTokenUtil jwtTokenUtil;
    @Autowired
    RoleUserMapper roleUserMapper;

    @Autowired
    @Qualifier("vueExecutor")
    Executor vueExecutor;

    @Autowired
    RoleMapper roleDao;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);




    @Override
    public UserDetails loadUserByUsername(String s) {
        logger.info(String.format("xxxxxx%s", s));
        LambdaQueryWrapper<UserEntity> queryWrapper = new QueryWrapper<UserEntity>().lambda().eq(UserEntity::getUsername, s);
        UserEntity user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            SysUserDetailEntity detail = new SysUserDetailEntity();
            detail.setId(Math.toIntExact(user.getId()));
            detail.setUsername(user.getUsername());
            detail.setPassword(user.getPassword());
            logger.info(String.format("xxxxxxxxxxuser_id,%s", user.getId()));
            AccountStateEntity accountState = accountStateDao.selectOne(new QueryWrapper<AccountStateEntity>().lambda().eq(AccountStateEntity::getUserid, user.getId()));
/*                detail.setAccountNonExpired(accountState.getAccountNonExpired() == 1);
                detail.setAccountNonLocked(accountState.getAccountNonLocked() == 1);
                detail.setEnabled(accountState.getEnabled() == 1);
                detail.setCredentialsNonExpired(accountState.getCredentialsNonExpired() == 1);*/
            detail.setAccountNonExpired(true);
            detail.setAccountNonLocked(true);
            detail.setEnabled(true);
            detail.setCredentialsNonExpired(true);
            //查询用户权限
            List<GrantedAuthority> authorities = new ArrayList<>();
            // 从RoleUser表拿到admin用户所有的role角色，他可以是admin超用户，也有普通用户权限
            LambdaQueryWrapper<RoleUserEntity> roleUserEntityQueryWrapper = new QueryWrapper<RoleUserEntity>().lambda().eq(RoleUserEntity::getUserId, user.getId());
            List<RoleUserEntity> roleUserEntities = roleUserMapper.selectList(roleUserEntityQueryWrapper);
            for (RoleUserEntity roleUserEntity : roleUserEntities) {
                RoleEntity roleTemp = roleDao.selectOne(new QueryWrapper<RoleEntity>().lambda().eq(RoleEntity::getId, roleUserEntity.getRoleId()));
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleTemp.getCode());
                authorities.add(authority);
            }
            detail.setAuthorities(authorities);
            return detail;
        } else {
            throw new UsernameNotFoundException("该账号不存在");
        }
    }

    public CompletableFuture<Either<ApiError,LoginInfoVO>> loginInfo(HttpServletRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            // 因为所有url都被过滤，所以这里只需要拿到role就行，不需要考虑token是否过期问题
            String token = request.getHeader(jwtTokenUtil.getHeader());
            LoginInfoVO loginInfoVO = new LoginInfoVO();
            if (StringUtils.hasLength(token) && !token.equals("null")) {
                String username = jwtTokenUtil.getUsernameIgnoreExpiration(token);
                loginInfoVO.setName(username);
                LambdaQueryWrapper<RoleEntity> queryWrapper = new QueryWrapper<RoleEntity>().lambda().eq(RoleEntity::getName, username);
                List<RoleEntity> roleEntities = roleDao.selectList(queryWrapper);
                List<String> roleList = new ArrayList<>();
                for (RoleEntity roleEntity : roleEntities) {
                    roleList.add(roleEntity.getName());
                }
                loginInfoVO.setRoles(roleList);
                return Either.Right(loginInfoVO);
            }
            return Either.Left(ApiError.from(ApiErrorEnum.TOKEN_EXPIRED));
        },vueExecutor);
    }



    /**
     * 刷新令牌
     */
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(jwtTokenUtil.getHeader());
        if (StringUtils.hasLength(token) && !token.equals("null")) {
            String username = jwtTokenUtil.getUsernameIgnoreExpiration(token);
            //如果jwt过期，则获取refresh_token，判断refresh_token是否过期，不过期则刷新token返回前端
            String refreshToken = refreshTokenDao.getRefreshToken(username);
            UserDetails userDetails = loadUserByUsername(username);
            if (jwtTokenUtil.validateToken(refreshToken, userDetails)) {
                try {
                    String newToken = jwtTokenUtil.refreshToken(token);
                    ResponseMsgUtil.sendSuccessMsg("刷新jwt", newToken, response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ResponseMsgUtil.sendFailMsg("登录状态过期请重新登录", response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                ResponseMsgUtil.sendFailMsg("登录状态过期请重新登录", response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public CompletableFuture<Either<ApiError,List<UserEntity>>> getUserEntitySimple(){
        return CompletableFuture.supplyAsync(() -> {
            List<UserEntity> userEntities = userMapper.selectList(null);
            if (userEntities.size() == 0) {
                return Either.Left(ApiError.from(ApiErrorEnum.CHECK_DATABASE_WRONG));
            } else {
                return Either.Right(userEntities);
            }
        }, vueExecutor);
    }

}
