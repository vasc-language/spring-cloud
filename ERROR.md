OrderApplication 的错误日志
java.net.SocketTimeoutException: Connect timed out
at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(NioSocketImpl.java:546) ~[na:na]
at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:592) ~[na:na]
at java.base/java.net.Socket.connect(Socket.java:751) ~[na:na]
at java.base/sun.net.NetworkClient.doConnect(NetworkClient.java:178) ~[na:na]
at java.base/sun.net.www.http.HttpClient.openServer(HttpClient.java:531) ~[na:na]
at java.base/sun.net.www.http.HttpClient.openServer(HttpClient.java:636) ~[na:na]
at java.base/sun.net.www.http.HttpClient.<init>(HttpClient.java:280) ~[na:na]
at java.base/sun.net.www.http.HttpClient.New(HttpClient.java:386) ~[na:na]
at java.base/sun.net.www.http.HttpClient.New(HttpClient.java:408) ~[na:na]
at java.base/sun.net.www.protocol.http.HttpURLConnection.getNewHttpClient(HttpURLConnection.java:1319) ~[na:na]
at java.base/sun.net.www.protocol.http.HttpURLConnection.plainConnect0(HttpURLConnection.java:1252) ~[na:na]
at java.base/sun.net.www.protocol.http.HttpURLConnection.plainConnect(HttpURLConnection.java:1138) ~[na:na]
at java.base/sun.net.www.protocol.http.HttpURLConnection.connect(HttpURLConnection.java:1067) ~[na:na]
at java.base/sun.net.www.protocol.http.HttpURLConnection.getInputStream0(HttpURLConnection.java:1690) ~[na:na]
at java.base/sun.net.www.protocol.http.HttpURLConnection.getInputStream(HttpURLConnection.java:1614) ~[na:na]
at java.base/java.net.HttpURLConnection.getResponseCode(HttpURLConnection.java:531) ~[na:na]
at feign.Client$Default.convertResponse(Client.java:110) ~[feign-core-12.3.jar:na]
at feign.Client$Default.execute(Client.java:106) ~[feign-core-12.3.jar:na]
at org.springframework.cloud.openfeign.loadbalancer.LoadBalancerUtils.executeWithLoadBalancerLifecycleProcessing(
LoadBalancerUtils.java:56) ~[spring-cloud-openfeign-core-4.0.3.jar:4.0.3]
at org.springframework.cloud.openfeign.loadbalancer.LoadBalancerUtils.executeWithLoadBalancerLifecycleProcessing(
LoadBalancerUtils.java:91) ~[spring-cloud-openfeign-core-4.0.3.jar:4.0.3]
at org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient.execute(
FeignBlockingLoadBalancerClient.java:134) ~[spring-cloud-openfeign-core-4.0.3.jar:4.0.3]
at feign.SynchronousMethodHandler.executeAndDecode(SynchronousMethodHandler.java:100) ~[feign-core-12.3.jar:na]
at feign.SynchronousMethodHandler.invoke(SynchronousMethodHandler.java:70) ~[feign-core-12.3.jar:na]
at feign.ReflectiveFeign$FeignInvocationHandler.invoke(ReflectiveFeign.java:96) ~[feign-core-12.3.jar:na]
at jdk.proxy2/jdk.proxy2.$Proxy78.p1(Unknown Source) ~[na:na]
at ai.agent.order.controller.FeignController.o1(FeignController.java:25) ~[classes/:na]
at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103) ~[na:na]
at java.base/java.lang.reflect.Method.invoke(Method.java:580) ~[na:na]
at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:
205) ~[spring-web-6.0.14.jar:6.0.14]
at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:
150) ~[spring-web-6.0.14.jar:6.0.14]
at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(
ServletInvocableHandlerMethod.java:118) ~[spring-webmvc-6.0.14.jar:6.0.14]
at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(
RequestMappingHandlerAdapter.java:884) ~[spring-webmvc-6.0.14.jar:6.0.14]
at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(
RequestMappingHandlerAdapter.java:797) ~[spring-webmvc-6.0.14.jar:6.0.14]
at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:
87) ~[spring-webmvc-6.0.14.jar:6.0.14]
at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:
1081) ~[spring-webmvc-6.0.14.jar:6.0.14]
at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:
974) ~[spring-webmvc-6.0.14.jar:6.0.14]
at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:
1014) ~[spring-webmvc-6.0.14.jar:6.0.14]
at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:903) ~[spring-webmvc-6.0.14.jar:6.0.14]
at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564) ~[tomcat-embed-core-10.1.16.jar:6.0]
at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:
885) ~[spring-webmvc-6.0.14.jar:6.0.14]
at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658) ~[tomcat-embed-core-10.1.16.jar:6.0]
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:
205) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:
149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51) ~[tomcat-embed-websocket-10.1.16.jar:10.1.16]
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:
174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:
149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:
100) ~[spring-web-6.0.14.jar:6.0.14]
at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:
116) ~[spring-web-6.0.14.jar:6.0.14]
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:
174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:
149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:
93) ~[spring-web-6.0.14.jar:6.0.14]
at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:
116) ~[spring-web-6.0.14.jar:6.0.14]
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:
174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:
149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:
201) ~[spring-web-6.0.14.jar:6.0.14]
at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:
116) ~[spring-web-6.0.14.jar:6.0.14]
at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:
174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:
149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:
167) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:
90) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:
482) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:
115) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:
74) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:340) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:391) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:
63) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at
org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:896) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:
1744) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:
52) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:
1191) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at
org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:
61) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]

http://127.0.0.1:10030/feign/o1?id=1
报了500错误码

{
"timestamp": "2025-07-27T07:28:36.908+00:00",
"status": 500,
"error": "Internal Server Error",
"path": "/feign/o1"
}
