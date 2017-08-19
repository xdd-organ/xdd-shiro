package com.java.xdd.shiro.filter;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {


    /**
     * 该方法可以不用从写
     *          FormAuthenticationFiltershiro提供的登录的filter，
     *          如果用户未登录，即AuthenticatingFilter中的isAccessAllowed判断了用户未登录，
     *          则会调用onAccessDenied方法做用户登录操作。若用户请求的不是登录地址，
     *          则跳转到登录地址，并且返回false直接终止filter链。若用户请求的是登录地址，
     *          若果是post请求则进行登录操作，由AuthenticatingFilter中提供的executeLogin方法执行。
     *          否则直接通过继续执行filter链，并最终跳转到登录页面（因为用户请求的就是登录地址，
     *          若不是登录地址也会重定向到登录地址
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    //原FormAuthenticationFilter的认证方法
    @Override
    protected boolean onAccessDenied(ServletRequest request,
                                     ServletResponse response) throws Exception {
        //在这里进行验证码的校验

        //从session获取正确验证码
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession();
        //取出session的验证码（正确的验证码）
        String validateCode = (String) session.getAttribute("validateCode");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username + "--------" + password);


        //取出页面的验证码
        //输入的验证和session中的验证进行对比
        String randomcode = httpServletRequest.getParameter("randomcode");
        System.out.println(randomcode);
        /*if (randomcode != null && validateCode != null && !randomcode.equals(validateCode)) {
            //如果校验失败，将验证码错误失败信息，通过shiroLoginFailure设置到request中
            httpServletRequest.setAttribute("shiroLoginFailure", "randomCodeError");
            //拒绝访问，不再校验账号和密码
            return true;
        }*/
        return super.onAccessDenied(request, response);
    }

    /**
     * 该方法可以不用从写
     *      这个方法决定了是否能让用户登录
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        System.out.println("是否可以登录！");
        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * 该方法可以不用从写
     *      若登录成功返回false（FormAuthenticationFiltershiro的onLoginSuccess默认false），
     *      则表示终止filter链，直接重定向到成功页面，甚至不到达目标方法直接返回了。
     *      若登录失败，直接返回true（onLoginFailure返回false），继续执行filter链并最终跳转到登录页面，
     *      该方法还会设置一些登录失败提示 shiroLoginFailure，
     *      在目标方法中可以根据这个错误提示制定客户端更加友好的错误提示
     *
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        return super.onLoginSuccess(token, subject, request, response);
    }


    /**
     * 该方法可以不用从写
     *      在目标方法中可以根据这个错误提示制定客户端更加友好的错误提示
     *
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        return super.onLoginFailure(token, e, request, response);
    }
}
