package com.java.xdd.shiro.manager;

import com.java.xdd.shiro.pojo.User;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.subject.WebSubjectContext;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomCookieRememberMeManager extends CookieRememberMeManager{

    private static transient final Logger log = LoggerFactory.getLogger(CustomCookieRememberMeManager.class);

    /**
     * 默认的记住我的cookie名称
     */
    public static final String DEFAULT_REMEMBER_ME_COOKIE_NAME = "rememberMe";

    private Cookie cookie;

    /**
     * 默认的构造方法
     */
    public CustomCookieRememberMeManager() {
        Cookie cookie = new SimpleCookie(DEFAULT_REMEMBER_ME_COOKIE_NAME);
        cookie.setHttpOnly(true);
        //One year should be long enough - most sites won't object to requiring a user to log in if they haven't visited
        //in a year:
        cookie.setMaxAge(Cookie.ONE_YEAR);
        this.cookie = cookie;
    }


    /**
     *
     * @return
     */
    public Cookie getCookie() {
        return cookie;
    }

    /**
     *
     * @param cookie
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    /**
     * 生成序列化的值
     *      默认是将accountPrincipals类序列化，然后将序列化的值使用base64加密存储到cookie中
     * @param subject
     * @param accountPrincipals
     */
    @Override
    protected void rememberIdentity(Subject subject, PrincipalCollection accountPrincipals) {
        super.rememberIdentity(subject, accountPrincipals);
    }

    /**
     * 生成序列化的值
     *      默认是将accountPrincipals类序列化，然后将序列化的值使用base64加密存储到cookie中
     * @param subject
     * @param token
     * @param authcInfo
     */
    @Override
    public void rememberIdentity(Subject subject, AuthenticationToken token, AuthenticationInfo authcInfo) {
        super.rememberIdentity(subject, token, authcInfo);
    }

    /**
     * 将序列化的值使用base64加密存储
     * 存储在cookie中
     *
     * @param subject
     * @param serialized 该参数PrincipalCollection的序列化的字节数组
     */
    protected void rememberSerializedIdentity(Subject subject, byte[] serialized) {

        if (!WebUtils.isHttp(subject)) {
            if (log.isDebugEnabled()) {
                String msg = "Subject argument is not an HTTP-aware instance.  This is required to obtain a servlet " +
                        "request and response in order to set the rememberMe cookie. Returning immediately and " +
                        "ignoring rememberMe operation.";
                log.debug(msg);
            }
            return;
        }


        HttpServletRequest request = WebUtils.getHttpRequest(subject);
        HttpServletResponse response = WebUtils.getHttpResponse(subject);

        //base 64 encode it and store as a cookie:
        String base64 = Base64.encodeToString(serialized);

        Cookie template = getCookie(); //the class attribute is really a template for the outgoing cookies
        Cookie cookie = new SimpleCookie(template);
        cookie.setValue(base64);
        cookie.saveTo(request, response);

        Cookie cookie2 = new SimpleCookie(template);
        cookie2.setName("abc");
        cookie2.setValue("12345678987654321");
        cookie2.saveTo(request, response);
    }

    /**
     * 判断用户是否使用了记住我功能
     * @param token
     * @return
     */
    @Override
    protected boolean isRememberMe(AuthenticationToken token) {
        return super.isRememberMe(token);
    }

    /**
     * 记住我功能不安全，因为：只需要返回一个不为空值PrincipalCollection对象，则系统判定用户使用记住我功能登录成功
     *
     * @param subjectContext
     * @return
     */
    @Override
    public PrincipalCollection getRememberedPrincipals(SubjectContext subjectContext) {
        PrincipalCollection principals = null;

        try {
            byte[] re = this.getRememberedSerializedIdentity(subjectContext);
            if(re != null && re.length > 0) {
                principals = this.convertBytesToPrincipals(re, subjectContext);
            }

            // 构造一个不为空的SimplePrincipalCollection地对象，则系统判定用户已登录(测试)：开始
            User user = new User();
            user.setPassword("jflsdjf");
            principals = new SimplePrincipalCollection(user, "customRealm");
            // 构造一个不为空的SimplePrincipalCollection地对象，则系统判定用户已登录(测试)：结束
        } catch (RuntimeException var4) {
            principals = this.onRememberedPrincipalFailure(var4, subjectContext);
        }

        return principals;
    }

    /**
     *
     * @param subjectContext
     * @return
     */
    private boolean isIdentityRemoved(WebSubjectContext subjectContext) {
        ServletRequest request = subjectContext.resolveServletRequest();
        if (request != null) {
            Boolean removed = (Boolean) request.getAttribute(ShiroHttpServletRequest.IDENTITY_REMOVED_KEY);
            return removed != null && removed;
        }
        return false;
    }


    /**
     * 获取记住我的序列化信息
     * @param subjectContext
     * @return
     */
    protected byte[] getRememberedSerializedIdentity(SubjectContext subjectContext) {

        if (!WebUtils.isHttp(subjectContext)) {
            if (log.isDebugEnabled()) {
                String msg = "SubjectContext argument is not an HTTP-aware instance.  This is required to obtain a " +
                        "servlet request and response in order to retrieve the rememberMe cookie. Returning " +
                        "immediately and ignoring rememberMe operation.";
                log.debug(msg);
            }
            return null;
        }

        WebSubjectContext wsc = (WebSubjectContext) subjectContext;
        if (isIdentityRemoved(wsc)) {
            return null;
        }

        HttpServletRequest request = WebUtils.getHttpRequest(wsc);
        HttpServletResponse response = WebUtils.getHttpResponse(wsc);

        String base64 = getCookie().readValue(request, response);
        // Browsers do not always remove cookies immediately (SHIRO-183)
        // ignore cookies that are scheduled for removal
        if (Cookie.DELETED_COOKIE_VALUE.equals(base64)) return null;

        if (base64 != null) {
            base64 = ensurePadding(base64);
            if (log.isTraceEnabled()) {
                log.trace("Acquired Base64 encoded identity [" + base64 + "]");
            }
            byte[] decoded = Base64.decode(base64);
            if (log.isTraceEnabled()) {
                log.trace("Base64 decoded byte array length: " + (decoded != null ? decoded.length : 0) + " bytes.");
            }
            return decoded;
        } else {
            //no cookie set - new site visitor?
            return null;
        }
    }

    /**
     *
     * @param base64
     * @return
     */
    private String ensurePadding(String base64) {
        int length = base64.length();
        if (length % 4 != 0) {
            StringBuilder sb = new StringBuilder(base64);
            for (int i = 0; i < length % 4; ++i) {
                sb.append('=');
            }
            base64 = sb.toString();
        }
        return base64;
    }

    /**
     * 删除记住我的cookie信息
     * @param subject
     */
    protected void forgetIdentity(Subject subject) {
        if (WebUtils.isHttp(subject)) {
            HttpServletRequest request = WebUtils.getHttpRequest(subject);
            HttpServletResponse response = WebUtils.getHttpResponse(subject);
            forgetIdentity(request, response);
        }
    }

    /**
     * 删除记住我的cookie信息
     * @param subjectContext
     */
    public void forgetIdentity(SubjectContext subjectContext) {
        if (WebUtils.isHttp(subjectContext)) {
            HttpServletRequest request = WebUtils.getHttpRequest(subjectContext);
            HttpServletResponse response = WebUtils.getHttpResponse(subjectContext);
            forgetIdentity(request, response);
        }
    }

    /**
     * 删除记住我的cookie信息
     * @param request
     * @param response
     */
    private void forgetIdentity(HttpServletRequest request, HttpServletResponse response) {
        getCookie().removeFrom(request, response);
    }
}
