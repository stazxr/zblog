//package com.github.stazxr.zblog.base.security.jwt;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Slf4j
//public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
//    private Integer tokenExpireTime;
//
//    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, Integer tokenExpireTime) {
//        super(authenticationManager);
//        this.tokenExpireTime = tokenExpireTime;
//    }
//
//    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
//        super(authenticationManager, authenticationEntryPoint);
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String header = request.getHeader(SecurityConstant.HEADER);
//        if (StrUtil.isBlank(header)) {
//            header = request.getParameter(SecurityConstant.HEADER);
//        }
//        Boolean notValid = StrUtil.isBlank(header) || (!header.startsWith(SecurityConstant.TOKEN_SPLIT));
//        if (notValid) {
//            chain.doFilter(request, response);
//            return;
//        }
//        try {
//            UsernamePasswordAuthenticationToken 继承 AbstractAuthenticationToken 实现 Authentication 所以当在页面中输入用户名和密码之后首先会进入到
//            UsernamePasswordAuthenticationToken验证(Authentication)，
//            UsernamePasswordAuthenticationToken authentication = getAuthentication(header, response);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        } catch (Exception e) {
//            e.toString();
//        } chain.doFilter(request, response);
//    }
//
//    private UsernamePasswordAuthenticationToken getAuthentication(String header, HttpServletResponse response) {
//        用户名 String username = null;
//        权限 List<GrantedAuthority > authorities = new ArrayList<>();
//        try {
//            解析token Claims
//            claims = Jwts.parser().setSigningKey(SecurityConstant.JWT_SIGN_KEY).parseClaimsJws(header.replace(SecurityConstant.TOKEN_SPLIT, "")).getBody();
//            logger.info("claims：" + claims);
//            获取用户名 username = claims.getSubject();
//            logger.info("username：" + username);
//            获取权限 String authority = claims.get(SecurityConstant.AUTHORITIES).toString();
//            logger.info("authority：" + authority);
//            if (!StringUtils.isEmpty(authority)) {
//                authorities.add(new SimpleGrantedAuthority(authority));
//            }
//        } catch (ExpiredJwtException e) {
//            ResponseUtil.out(response, ResponseUtil.resultMap(false, 401, "登录已失效，请重新登录"));
//        } catch (Exception e) {
//            log.error(e.toString());
//            ResponseUtil.out(response, ResponseUtil.resultMap(false, 500, "解析token错误"));
//        } if (StrUtil.isNotBlank(username)) {
//            踩坑提醒 此处password不能为null User principal = new User(username, "", authorities);
//            return new UsernamePasswordAuthenticationToken(principal, null, authorities);
//        } return null;
//    }
//}
