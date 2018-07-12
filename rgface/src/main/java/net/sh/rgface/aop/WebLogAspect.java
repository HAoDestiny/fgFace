package net.sh.rgface.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import net.sh.rgface.entity.RequestLogEntity;
import net.sh.rgface.entity.session.AdminSession;
import net.sh.rgface.entity.session.PersonnelSession;
import net.sh.rgface.po.base.ResultPo;
import net.sh.rgface.serive.admin.AdminRequestLogService;
import net.sh.rgface.util.Tool;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by DESTINY on 2018/5/23.
 */

@Slf4j
@Aspect
@Component
public class WebLogAspect {

    @Resource
    private AdminRequestLogService adminRequestLogService;

    private PersonnelSession personnelSession;

    //	　　早在JDK 1.2的版本中就提供Java.lang.ThreadLocal，ThreadLocal为解决多线程程序的并发问题提供了一种新的思路。使用这个工具类可以很简洁地编写出优美的多线程程序。
    //	　　当使用ThreadLocal维护变量时，ThreadLocal为每个使用该变量的线程提供独立的变量副本，所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
    //	　　从线程的角度看，目标变量就象是线程的本地变量，这也是类名中“Local”所要表达的意思。
    //	　　所以，在Java中编写线程局部变量的代码相对来说要笨拙一些，因此造成线程局部变量没有在Java开发者中得到很好的普及。
    //	ThreadLocal的接口方法
    //	ThreadLocal类接口很简单，只有4个方法，我们先来了解一下：
    //	void set(Object value)设置当前线程的线程局部变量的值。
    //	public Object get()该方法返回当前线程所对应的线程局部变量。
    //	public void remove()将当前线程局部变量的值删除，目的是为了减少内存的占用，该方法是JDK 5.0新增的方法。需要指出的是，当线程结束后，对应该线程的局部变量将自动被垃圾回收，所以显式调用该方法清除线程的局部变量并不是必须的操作，但它可以加快内存回收的速度。
    //	protected Object initialValue()返回该线程局部变量的初始值，该方法是一个protected的方法，显然是为了让子类覆盖而设计的。这个方法是一个延迟调用方法，在线程第1次调用get()或set(Object)时才执行，并且仅执行1次。ThreadLocal中的缺省实现直接返回一个null。
    //	　　值得一提的是，在JDK5.0中，ThreadLocal已经支持泛型，该类的类名已经变为ThreadLocal<T>。API方法也相应进行了调整，新版本的API方法分别是void set(T value)、T get()以及T initialValue()。
    ThreadLocal<RequestLogEntity> requestLogEntityThreadLocal = new ThreadLocal<RequestLogEntity>();

    private int startTime = 0;

    /**
     * 定义一个切入点.
     * 解释下：
     * <p>
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 任意包名
     * ~ 第三个 * 代表任意方法.
     * ~ 第四个 * 定义在web包或者子包
     * ~ 第五个 * 任意方法
     * ~ .. 匹配任意数量的参数.
     */
    @Pointcut(value = "execution(public * net.sh.rgface.controller..*.*(..)) " +
            "&& !within(net.sh.rgface.controller.admin.AdminMobileController)" +
            "&& !within(net.sh.rgface.controller.admin.AdminRecordingController)" +
            "&& !within(net.sh.rgface.controller.personnel.*)" +
            "&& !within(net.sh.rgface.controller.user.*)" +
            "&& !within(net.sh.rgface.controller.WebTimeController)" +
            "&& !within(net.sh.rgface.controller.admin.AdminRequestLogController)" +
            "&& !@annotation(net.sh.rgface.annotations.NotAspect)")
    public void setRequestLog() {}

    /**
     * 前置通知，方法调用前被调用
     *
     * @param joinPoint
     */
    @Before("setRequestLog()")
    public void doBefore(JoinPoint joinPoint) {

        startTime = (int) (System.currentTimeMillis() / 1000);

        log.info(" ---------------- WebLogAspect.doBefore() 前置通知");

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = requestAttributes.getRequest();
        HttpSession httpSession = httpServletRequest.getSession();

        RequestLogEntity requestLogEntity = new RequestLogEntity();

        AdminSession adminSession = (AdminSession) httpSession.getAttribute("Admin");

        personnelSession = (PersonnelSession) httpSession.getAttribute("Personnel");

        log.info(" ---------------- startTime: " + startTime);
        log.info(" ---------------- ip: " + Tool.getIpAddress(httpServletRequest));
        log.info(" ---------------- URL: " + httpServletRequest.getRequestURL().toString());
        log.info(" ---------------- type: " + httpServletRequest.getHeader("X-Requested-With")); //请求类型
        log.info(" ---------------- method: " + httpServletRequest.getMethod());

        if ("post".equalsIgnoreCase(httpServletRequest.getMethod())) {
            try {
                log.info(" ---------------- param: " + Tool.getPostData(httpServletRequest));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if ("get".equalsIgnoreCase(httpServletRequest.getMethod())) {
            log.info(" ---------------- param: " + JSON.toJSONString(httpServletRequest.getParameterMap(), SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue));
        }

        log.info(" ---------------- className: " + joinPoint.getSignature().getName());

        if (personnelSession != null) {
            return;
        }

        if (adminSession != null) {
            log.info(" ---------------- accountId: " + adminSession.getId() + ", accountName: " + adminSession.getAccountName());

            requestLogEntity.setAccountId(adminSession.getId());
        }

        requestLogEntity.setIp(Tool.getIpAddress(httpServletRequest));
        requestLogEntity.setUrl(httpServletRequest.getRequestURL() + "");
        requestLogEntity.setType(httpServletRequest.getHeader("X-Requested-With"));
        requestLogEntity.setMethod(httpServletRequest.getMethod());
        requestLogEntity.setClassMethod(joinPoint.getSignature().getName());
        requestLogEntity.setStartTime(startTime);
        requestLogEntity.setCreateTime((int) (System.currentTimeMillis() / 1000));
        requestLogEntity.setDeleteTag(0);

        requestLogEntityThreadLocal.set(requestLogEntity); //线程保存到本地

    }


    /**
     * 后置返回通知
     * 这里需要注意的是:
     * 如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
     * 如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     * returning 限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
     *
     * @param joinPoint
     * @param returnData
     */
    @AfterReturning(value = "setRequestLog()", returning = "returnData")
    public void doAfterReturning(JoinPoint joinPoint, ResultPo returnData) {

        // 处理完请求，返回内容
        log.info(" ---------------- WebLogAspect.doAfterReturning() 后置返回通知");

        log.info(" ---------------- RETURN DATA:" + returnData.toString());

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse httpServletResponse = requestAttributes.getResponse();

        if (personnelSession != null) {
            return;
        }

        log.info(" ---------------- endTime: " + System.currentTimeMillis());
        log.info(" ---------------- 时间差: " + (System.currentTimeMillis() - startTime));

        log.info(" ---------------- statusCode: " + httpServletResponse.getStatus());

        RequestLogEntity requestLogEntity = requestLogEntityThreadLocal.get();
        requestLogEntity.setStatusCode(httpServletResponse.getStatus());
        requestLogEntity.setEndTime((int) (System.currentTimeMillis() / 1000));

        if ("SUCCESS".equals(returnData.getStatus())) {
            requestLogEntity.setActionStatus(1);
        }

        adminRequestLogService.addRequestLog(requestLogEntity);
    }


    /**
     * 后置异常通知
     * 定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法；
     * throwing 限定了只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
     * 对于throwing对应的通知方法参数为Throwable类型将匹配任何异常。
     *
     * @param joinPoint
     * @param exception
     */
    @AfterThrowing(value = "setRequestLog()", throwing = "exception")
    public void doAfterThrowingAdvice(JoinPoint joinPoint, Throwable exception) {

        log.info(" ---------------- WebLogAspect.doAfterThrowingAdvice() 后置异常通知");

        log.info(" ---------------- RETURN EXCEPTION DATA:" + exception.toString());

        RequestLogEntity requestLogEntity = requestLogEntityThreadLocal.get();

        if (requestLogEntity != null) {

            requestLogEntity.setStatusCode(200);
            requestLogEntity.setEndTime((int) (System.currentTimeMillis() / 1000));
            requestLogEntity.setActionStatus(0);

            adminRequestLogService.addRequestLog(requestLogEntity);
        }


    }

    /**
     * 后置最终通知（目标方法只要执行完了就会执行后置通知方法）
     *
     * @param joinPoint
     */
    @After(value = "setRequestLog()")
    public void doAfterAdvice(JoinPoint joinPoint) {

        log.info("---------------- WebLogAspect.doAfterAdvice()  后置通知");

    }

    /**
     * 环绕通知：
     * 环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
     * 环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     */
//    @Around(value = "setRequestLog()")
//    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
//
//        System.out.println("环绕通知的目标方法名：" + proceedingJoinPoint.getSignature().getName());
//
//        try {
//
//            //obj之前可以写目标方法执行前的逻辑
//            Object obj = proceedingJoinPoint.proceed();//调用执行目标方法
//
//            return obj;
//
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//
//        return null;
//    }
}
