package com.java.xdd.shiro.filter;

import com.java.xdd.shiro.pojo.SessionInformation;
import com.java.xdd.shiro.pojo.User;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 控制一个用户能同时在几个地方登陆
 * 继承AccessControlFilter，
 *      最常用的，该filter中onPreHandle调用isAccessAllowed和onAccessDenied决定是否继续执行。
 *      一般继承该filter，isAccessAllowed决定是否继续执行。onAccessDenied做后续的操作，
 *      如重定向到另外一个地址、添加一些信息到request域等等。
 *
 * AccessControlFilter中的对onPreHandle方法做了进一步细化，
 *      isAccessAllowed方法和onAccessDenied方法达到控制效果。
 *      这两个方法都是抽象方法，由子类去实现。到这一层应该明白。
 *      isAccessAllowed和onAccessDenied方法会影响到onPreHandle方法，
 *      而onPreHandle方法会影响到preHandle方法，而preHandle方法会达到控制filter链是否执行下去的效果。
 *      所以如果正在执行的filter中isAccessAllowed和onAccessDenied都返回false，
 *      则整个filter控制链都将结束，不会到达目标方法（客户端请求的接口），
 *      而是直接跳转到某个页面（由filter定义的，将会在authc中看到）
 */
public class KickoutSessionControlFilter extends AccessControlFilter {
    /** 最多几个人同时登陆 */
    private int maxSession;
    /** 被踢出后，跳转路径 */
    private String kickoutUrl;
    /** 提出后者还是前者 */
    private boolean kickoutAfter;

    /** session管理 */
    private SessionManager sessionManager;
    /** 缓存管理 */
    private EhCacheManager cacheManager;

    /** 缓存 */
    private Cache<Object, Deque<Serializable>> cache;

    /** <principal:Object,SessionIdSet> */
    /**
     * key:UserDetails实现类
     * value:sessionId的集合
     */
    private final ConcurrentMap<String, Deque<String>> principals = new ConcurrentHashMap<>();

    /** <sessionId:Object,SessionInformation> */
    /**
     * key:sessionId
     * value:SessionInformation
     *      principal:UserDetails实现类
     *      sessionId:
     *      lastRequest:最后登录时间
     *      expired:是否过期
     */
    private final Map<String, SessionInformation> sessionIds = new ConcurrentHashMap<>();



    /**
     * isAccessAllowed判断了用户未登录
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        this.onAccessDenied(request, response);
        return subject.isAuthenticated();
    }


    /**
     * onAccessDenied方法做用户登录操作
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }

        Session session = subject.getSession();
        Object principal = subject.getPrincipal();
        PrincipalCollection principals = subject.getPrincipals();
        User user = (User) principal;
        // String username = (String) subject.getPrincipal();
        String username = user.getUsername();
        Serializable sessionId = session.getId();

        if (!sessionIds.containsKey(sessionId.toString())) {
            SessionInformation information = new SessionInformation();
            information.setExpired(false);
            information.setLastRequest(new Date());
            information.setSessionId(sessionId.toString());
            information.setUser(user);
            sessionIds.put(sessionId.toString(), information);
            Deque<String> sessionIds = this.principals.get(user.getUsername());
            if (sessionIds == null) {
                sessionIds = new ArrayDeque<>();
            }
            sessionIds.add(sessionId.toString());
            this.principals.put(user.getUsername(), sessionIds);
        }

        Deque<String> strings = this.principals.get(user.getUsername());
        if (strings.size() > maxSession) {
            String sessionId2;
            if (kickoutAfter) {
                sessionId2 = strings.removeFirst();
            } else {
                sessionId2 = strings.removeLast();
            }
            sessionIds.remove(sessionId2);
            Session session2 = sessionManager.getSession(new DefaultSessionKey(sessionId2));
            if (session2 != null) {
                session2.setTimeout(1000);
            }
        }

        System.out.println(this.sessionIds);
        System.out.println(this.principals);
        if (session.getTimeout() < 1001) {
            subject.logout();
            saveRequest(request);
            WebUtils.issueRedirect(request, response, kickoutUrl);
            return false;
        }







        //TODO 同步控制
        /*Deque<Serializable> deque = cache.get(username);
        if (deque == null) {
            deque = new LinkedList<>();
            cache.put(username, deque);
        }

        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            deque.push(sessionId);
        }

        //如果队列里的sessionId数超出最大会话数，开始踢人
        while (deque.size() > maxSession) {
            Serializable kickoutSessionId = null;

            if (kickoutAfter) { //如果踢出后者
                kickoutSessionId = deque.removeFirst();
            } else { //否则踢出前者
                kickoutSessionId = deque.removeLast();
            }
            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if (kickoutSession != null) {
                    //设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) { //ignore exception
                e.printStackTrace();
            }
        }

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute("kickout") != null) {
            //会话被踢出了
            try {
                subject.logout();
            } catch (Exception e) { //ignore
                e.printStackTrace();
            }
            saveRequest(request);
            WebUtils.issueRedirect(request, response, kickoutUrl);
            return false;
        }*/
        return true;
    }

    public int getMaxSession() {
        return maxSession;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public String getKickoutUrl() {
        return kickoutUrl;
    }

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public boolean isKickoutAfter() {
        return kickoutAfter;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public EhCacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(EhCacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.cache = cacheManager.getCache("shiroSessionCache");
    }
}
