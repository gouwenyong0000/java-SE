# 基础知识

**springboot日志级别输出设置**

全局设置日志debug

```yaml
logging:
  level:
  root: debug
```


自定义包名下的日志级别设置

```yaml
logging:
  level:
    包名: 
```



# Spring Security 源码分析（一）：过滤器链

`Spring Security` 是一个能够为企业应用系统提供声明式的安全访问控制解决方案的安全框架，减少了为企业系统安全控制编写大量重复代码的工作，能够与 `Spring` 无缝集成。本文旨在从实际应用角度出发，阅读 `Spring Security` 源码，分析其实现原理。

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#注册过滤器链)注册过滤器链

引入 `spring-security` 包：

```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
      <version>2.1.3.RELEASE</version>
  </dependency>
```

对 `Web` 资源的控制通过 `WebSecurityConfiguration` 配置，其在启动时装配名为 `springSecurityFilterChain` 的 `bean`：

```java
  @Bean(name = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME)
  public Filter springSecurityFilterChain() throws Exception {
    boolean hasConfigurers = webSecurityConfigurers != null
        && !webSecurityConfigurers.isEmpty();
    if (!hasConfigurers) {
      WebSecurityConfigurerAdapter adapter = objectObjectPostProcessor
          .postProcess(new WebSecurityConfigurerAdapter() {
          });
      // 应用 WebSecurityConfigurerAdapter 中的各项配置
      webSecurity.apply(adapter);
    }
    // 生成 Spring Security 过滤器链
    return webSecurity.build();
  }
```

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#websecurityconfigureradapter-实现)WebSecurityConfigurerAdapter 实现

`spring-boot-autoconfigure` 在 `spring.factories` 中标识 `SecurityAutoConfiguration` 需要自动装配，在装配过程中又将 `SpringBootWebSecurityConfiguration` 引入，在此类中声明了一个继承自 `WebSecurityConfigurerAdapter` 的 `DefaultConfigurerAdapter`，由此装配了对 `WebSecurityConfigurerAdapter` 的默认实现，此类对 `spring security` 做了许多默认配置。 如果需要干预 `spring security` 配置，则需要继承 `WebSecurityConfigurerAdapter` 并装配到 `Spring` 容器中。

```java
@Configuration
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
@ConditionalOnMissingBean(WebSecurityConfigurerAdapter.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
public class SpringBootWebSecurityConfiguration {

  @Configuration
  @Order(SecurityProperties.BASIC_AUTH_ORDER)
  static class DefaultConfigurerAdapter extends WebSecurityConfigurerAdapter {

  }

}
```

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#abstractsecuritybuilder-build)AbstractSecurityBuilder#build

`WebSecurity` 实例 调用 `apply` 方法获取 `WebSecurityConfigurerAdapter` 中的配置，并调用 `build` 方法构造过滤器链，其实现为：

```java
  public final O build() throws Exception {
    if (this.building.compareAndSet(false, true)) {
      this.object = doBuild();
      return this.object;
    }
    throw new AlreadyBuiltException("This object has already been built");
  }

  protected final O doBuild() throws Exception {
    synchronized (configurers) {
      buildState = BuildState.INITIALIZING;

      beforeInit();
      init();

      buildState = BuildState.CONFIGURING;

      beforeConfigure();
      configure();

      buildState = BuildState.BUILDING;

      O result = performBuild();

      buildState = BuildState.BUILT;

      return result;
    }
  }

  /**
   * 内部配置初始化
   */
  private void init() throws Exception {
    Collection<SecurityConfigurer<O, B>> configurers = getConfigurers();

    for (SecurityConfigurer<O, B> configurer : configurers) {
      configurer.init((B) this);
    }

    for (SecurityConfigurer<O, B> configurer : configurersAddedInInitializing) {
      configurer.init((B) this);
    }
  }

  /**
    * 配置逻辑
    */
  private void configure() throws Exception {
    Collection<SecurityConfigurer<O, B>> configurers = getConfigurers();

    for (SecurityConfigurer<O, B> configurer : configurers) {
      configurer.configure((B) this);
    }
  }
```

可以看到构建操作为将通过 `apply` 方法应用进来的配置分别初始化和构建，链条为 `beforeInit -> init -> beforeConfigure -> configure -> performBuild`。`Spring Security` 中的 `AuthenticationManagerBuilder` （认证管理器生成配置）、`HttpSecurity` （过滤器管理器生成配置）、`WebSecurity` （过滤器生成配置） 都是继承 `AbstractConfiguredSecurityBuilder` 通过这个链条生成目标对象，这 3 个配置也是 `Spring Security` 的配置核心。

![SecuirtyBuilder](https://wch853.github.io/img/security/SecurityBuilder.png)

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#websecurityconfigureradapter-配置)WebSecurityConfigurerAdapter 配置

由上文可知，过滤器链生成过程中调用了 `WebSecurityConfigurerAdapter` 的 `init` 和 `configure` 方法。

`init` 方法首先调用了 `getHttp` 方法，用于生成 `AuthenticationManager` 和 `HttpSecurity` 实例。

#### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#认证管理器-authenticationmanager-的配置)认证管理器 AuthenticationManager 的配置

来看看 `WebSecurityConfigurerAdapter` 关于认证管理器的组成：

```java
  /**
    * 认证管理器，管理多种认证方式（AuthenticationProvider），进行实际的认证调用
    */
  private AuthenticationManager authenticationManager;

  /**
    * 认证配置，装配认证方式，通过 @Autowired 自动注入
    */
  private AuthenticationConfiguration authenticationConfiguration;

  /**
    * 同于生成系统配置的认证管理器
    */
  private AuthenticationManagerBuilder authenticationBuilder;

  /**
    * 用于生成开发者可干预的认证管理器
    */
  private AuthenticationManagerBuilder localConfigureAuthenticationBldr;

  /**
    * true - 不使用可干预的认证管理器生成方式
    */
  private boolean disableLocalConfigureAuthenticationBldr;
```

`WebSecurityConfigurerAdapter` 在初始化过程中会调用 `authenticationManager` 方法配置认证管理器，当 `disableLocalConfigureAuthenticationBldr` 为 `true` 时会调用 `AuthenticationConfiguration#getAuthenticationManager` 生成认证管理器，当为 `false` 时会使用开发者干预过的 `localConfigureAuthenticationBldr` 生成认证管理器。

```java
  protected AuthenticationManager authenticationManager() throws Exception {
    if (!authenticationManagerInitialized) {
      // void configure(AuthenticationManagerBuilder auth); 开发者可以重载此方法干预认证管理器的成逻辑
      configure(localConfigureAuthenticationBldr);
      if (disableLocalConfigureAuthenticationBldr) {
        // 不需要干预，使用系统配置逻辑
        authenticationManager = authenticationConfiguration.getAuthenticationManager();
      } else {
        authenticationManager = localConfigureAuthenticationBldr.build();
      }
      authenticationManagerInitialized = true;
    }
    return authenticationManager;
  }
```

##### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#认证管理器的系统配置逻辑)认证管理器的系统配置逻辑

`WebSecurityConfigurerAdapter` 中通过 `@Autowired` 注入了 `AuthenticationConfiguration`，此类的主要功能是为 `AuthenticationManagerBuilder` 装配 `AuthenticationProvider`，可以装配的认证配置逻辑分为两类：

- （1）在 `spring context` 中查找 `UserDetailsService` 等类的相关实现，包装成 `DaoAuthenticationProvider` 配置到 `AuthenticationManagerBuilder` 中。

```java
  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {

    UserDetailsService userDetailsService = getBeanOrNull(UserDetailsService.class);

    // ...

    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);

    // ...

    auth.authenticationProvider(provider);
  }
```

`UserDetailsService` 的作用是通过用户名查找用户信息：

```java
public interface UserDetailsService {
  // 根据用户名查找用户信息
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
```

`Spring Security` 关于 `UserDetails` 的默认实现为 `org.springframework.security.core.userdetails.User`，它有一个构造方法，可以构造用户名、密码、是否激活、是否过期、是否凭证过期、是否锁定：

```java
public User(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities);
```

`DaoAuthenticationProvider` 继承自 `AbstractUserDetailsAuthenticationProvider`，这个抽象类实现了 `AuthenticationProvider` 接口的 `authenticate` 方法，此方法会调用子类实现的 `retrieveUser` 方法。`DaoAuthenticationProvider` 的实现是调用注入进来的 `UserDetailsService` 的 `loadUserByUsername` 方法。

```java
  protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    // ...

    UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);

    // ...
  }
```

取出账户信息后，`AbstractUserDetailsAuthenticationProvider#authenticate` 方法进行 `check`，如果密码错误，或者未激活、过期等都会抛出 `AuthenticationException` 异常。

![DaoAuthenticationProvider](https://wch853.github.io/img/security/DaoAuthenticationProvider.png)

在不自定义任何配置的情况下启动引入 `spring-boot-starter-security` 包的项目，会发现控制台输出了这样的日志：

```text
2049-04-09 00:00:00.000  INFO 8504 --- [  restartedMain] .s.s.UserDetailsServiceAutoConfiguration : Using generated security password: 1a1890f2-97d5-421d-a775-953f7641b579
```

打开 `UserDetailsServiceAutoConfiguration` 类，此类在没有 `UserDetailsService` 的自定义实现时，会装配一个实现了 `UserDetailsService` 接口的 `InMemoryUserDetailsManager`，默认生成一个登录名为 `user` 的用户，密码通过 `UUID` 随机生成，并在控制台输出。默认的用户配置取自 `SecurityProperties`，开发者可以在配置文件中加以覆盖。 到此为止，用户在不做任何额外配置的情况下，拥有了一个可以认证通过的账号。

- （2）在 `spring context` 中查找 `AuthenticationProvider` 的实现，直接配置到 `AuthenticationManagerBuilder` 中，但是 `spring` 没有装配 `AuthenticationProvider` 的默认实现。

```java
  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    // ...

    AuthenticationProvider authenticationProvider = getBeanOrNull(AuthenticationProvider.class);

    // ...

    auth.authenticationProvider(authenticationProvider);
  }
```

##### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#开发者可干预的认证管理器)开发者可干预的认证管理器

上文说到，系统会默认装配两类 `AuthenticationProvider`，如果开发者需要干预认证管理器的生成，同样可以提供这两类认证逻辑。 先看如何干预：`disableLocalConfigureAuthenticationBldr` 在 `WebSecurityConfigurerAdapter` 中默认为 `false`，`void configure(AuthenticationManagerBuilder auth)` 方法将此属性修改为 `true`，如果开发者需要干预，则需要覆盖此方法：

```java
  @Resource
  private UserDetailsService userDetailsService;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
      // 自定义 AuthenticationProvider 实现
      // .authenticationProvider(...)
      // 自定义 UserDetailsService 实现
      .userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
  }
```

开发者可以实现 `AuthenticationProvider` 或 `UserDetailsService` 接口，填充自定义用户逻辑。在注入自定义用户认证逻辑时，实际还是包装为 `AuthenticationProvider`，因此往后的认证逻辑就与系统的默认配置相符了。

```java
  public <T extends UserDetailsService> DaoAuthenticationConfigurer<AuthenticationManagerBuilder, T> userDetailsService(T userDetailsService) throws Exception {
    this.defaultUserDetailsService = userDetailsService;
    return apply(new DaoAuthenticationConfigurer<>(userDetailsService));
  }
```

在 `AuthenticationManagerBuilder` 配置完成后，`build` 方法会将所有的 `AuthenticationProvider` 交给 `ProviderManager` 管理。在进行认证时，`ProviderManager` 会逐个调用认证逻辑进行认证，有一个通过即认证成功。

##### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#密码加密管理)密码加密管理

`Spring Security` 默认要求用户密码加密。以上文中提到的 `UserDetailsServiceAutoConfiguration` 为例，生成的密码需要配置 `{noop}` 前缀：

```java
  /**
   * 随机密码配置
   */
	private String getOrDeducePassword(SecurityProperties.User user, PasswordEncoder encoder) {
		String password = user.getPassword();
		// ...

		return "{noop}" + password;
	}
```

这是因为如果不配置系统默认加密方式 `PasswordEncoder`，认证流程的校验密码环节会读取密码的前缀来判断密码使用的是哪种加密方式：

```java
  public boolean matches(CharSequence rawPassword, String prefixEncodedPassword) {
    // ...

    // 获取密码前缀（加密方式）
    String id = extractId(prefixEncodedPassword);
    PasswordEncoder delegate = this.idToPasswordEncoder.get(id);
    if (delegate == null) {
      // ...
      // 查不到前缀或者找不到前缀对应的加密方式，会抛出异常，因此是强制加密的
    }
    String encodedPassword = extractEncodedPassword(prefixEncodedPassword);
    return delegate.matches(rawPassword, encodedPassword);
  }
```

在项目开发中，为每个密码密文加一个前缀是不明智的，因此开发者如果需要自定义用户加载方式，可以在配置的同时手动指定加密方式：

```java
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // 自定义认证服务
    auth.userDetailsService(userDetailsService)
            // 配置 bCrypt 作为密码加密方式
            .passwordEncoder(new BCryptPasswordEncoder());
  }
```

`BCryptPasswordEncoder` 在加密时会随机生成盐值，在与密码明文进行匹配时，可以从密文中读取到使用的盐值，对明文加密后匹配。随机生成盐值的方式大大增强了密码的安全性。

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#过滤器管理器-httpsecurity-配置)过滤器管理器 HttpSecurity 配置

当认证管理器初始化完成，`WebSecurityConfigurerAdapter` 会继续配置 `HttpSecurity`，它用于配置 `web` 请求的安全配置，默认会应用到所有请求，开发者也可通过 `RequestMatcher` 配置例外。 来看看 `HttpSecurity` 的默认配置：

```java
  /**
    * 创建 HttpSecurity 实例
    */
  protected final HttpSecurity getHttp() throws Exception {
    // ...

    http = new HttpSecurity(objectPostProcessor, authenticationBuilder, sharedObjects);
    if (!disableDefaults) {
      http
        // csrf 跨站请求伪造保护
        .csrf().and()
        // 配置异步支持
        .addFilter(new WebAsyncManagerIntegrationFilter())
        // security 异常处理
        .exceptionHandling().and()
        // 将请求的 header 写入响应的 header
        .headers().and()
        // session 管理器，可以配置一个用户仅有一个会话有效
        .sessionManagement().and()
        // 保存认证信息（session维度）
        .securityContext().and()
        // 保存 request cache
        .requestCache().and()
        // 匿名认证配置
        .anonymous().and()
        // 配置重载 servlet 相关安全方法
        .servletApi().and()
        // 表单登录页配置
        .apply(new DefaultLoginPageConfigurer<>()).and()
        // 匹配 /logout 做登出逻辑，成功后跳转登录页
        .logout();

      // ...
    }
    // HttpSecurity 扩展配置
    configure(http);
    return http;
  }

  /**
   * HttpSecurity 扩展配置
   */
  protected void configure(HttpSecurity http) throws Exception {
    http
      // 约束基于 HttpServletRequest 的请求
      .authorizeRequests()
        // 任何请求 需要认证
        .anyRequest().authenticated()
        .and()
      // 表单登录
      .formLogin().and()
      // http basic 认证
      .httpBasic();
  }
```

与配置认证管理器相同的是，在配置 `HttpSecurity` 的过程中，留有一个名为 `configure` 的方法供开发者配置。默认的配置方法拦截了所有请求，要求必须经过身份认证才能正确访问 `web` 资源，默认有表单登录和 `http basic` 两种认证方式可以选择。`HttpSecurity` 提供的大多数配置方法，都是通过过滤器实现的。

#### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#form-login-表单登录)form login 表单登录

`formLogin` 方法引入了 `FormLoginConfigurer`，此类中配置了两个过滤器：

- `UsernamePasswordAuthenticationFilter`：在创建过滤器时默认使用 `/login POST` 作为表单登录请求，这个过滤器的过滤逻辑就是调用上文中配置的 `AuthenticationManager` 进行认证。

```java
  public UsernamePasswordAuthenticationFilter() {
    super(new AntPathRequestMatcher("/login", "POST"));
  }
```

如果认证成功，默认会保存认证信息，并重定向到相应的请求地址；如果认证失败，默认会重定向到登录页面。

- `DefaultLoginPageGeneratingFilter`：用于配置登录页面，登录页面默认的登录、登出、登录错误地址分别为 `/login /login?logout /login?error`，其初始化配置在 `HttpSecurity` 的默认配置中。过滤逻辑为当请求为这 3 个地址时，会生成一个表单登录的 `HTML` 并立即返回。

```java
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    boolean loginError = isErrorPage(request);
    boolean logoutSuccess = isLogoutSuccess(request);
    // 登录、登出或登录失败时跳转到登录页。
    if (isLoginUrlRequest(request) || loginError || logoutSuccess) {
      // 生成 HTML 表单
      String loginPageHtml = generateLoginPageHtml(request, loginError, logoutSuccess);
      response.setContentType("text/html;charset=UTF-8");
      response.setContentLength(loginPageHtml.getBytes(StandardCharsets.UTF_8).length);
      response.getWriter().write(loginPageHtml);

      return;
    }

    chain.doFilter(request, response);
  }
```

#### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#http-basic-认证)http basic 认证

`httpBasic` 方法配置了 `BasicAuthenticationFilter` 过滤器，其过滤逻辑是从取出 `Authorization` 头，请求头内容为 `username:password` 的 `Base64` 编码形式。在获取用户名、密码后，同样调用 `AuthenticationManager` 进行认证。

#### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#csrf-跨站请求伪造保护)csrf 跨站请求伪造保护

`csrf` 方法配置了 `CsrfFilter`，其过滤逻辑为默认放行 `GET` 等请求，其它请求需要进行 `CsrfToken` 校验。访问请求走到这个过滤器时，如果没有携带 `CsrfToken`，会新生成并放入请求中。过滤器链继续走到 `DefaultLoginPageGeneratingFilter`，由于在 `DefaultLoginPageConfigurer` 配置时，从请求中会取出 `CsrfToken` 交给 `DefaultLoginPageGeneratingFilter`，所以 `CsrfToken` 会一并生成 `HTML` 表单，我们使用默认的登录页面就能正确提交表单。

#### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#securitycontext-认证上下文)securityContext 认证上下文

`securityContext` 方法配置了 `SecurityContextPersistenceFilter`，其过滤逻辑为为每个会话创建一个 `SecurityContext`。

```java
  HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request, response);
  // 从 session 中取出或在 session中设置 SecurityContext
  SecurityContext contextBeforeChainExecution = repo.loadContext(holder);
  SecurityContextHolder.setContext(contextBeforeChainExecution);
```

在 `UsernamePasswordAuthenticationFilter` 中，如果认证成功，则会调用 `successfulAuthentication` 方法，将认证成功的 `Authentication` 信息放入 `SecurityContextHolder` 中。开发者由此可以使用 `SecurityContextHolder.getContext().getAuthentication();` 获取当前会话的认证用户信息。由于认证信息存放在 `session` 中，一旦用户认证成功，当访问其它请求时，经由此过滤器时，就可以直接取得认证信息，安全通过过滤器链。

#### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#authorizerequests-最后的守门员)authorizeRequests 最后的守门员

在经历一系列过滤逻辑之后，请求来到 `spring security` 最后的过滤器 `FilterSecurityInterceptor`，此过滤器通过调用 `authorizeRequests` 方法加载 `ExpressionUrlAuthorizationConfigurer` 配置：

```java
  public void configure(H http) throws Exception {
    // ...

    FilterSecurityInterceptor securityInterceptor = createFilterSecurityInterceptor(
        http, metadataSource, http.getSharedObject(AuthenticationManager.class));

    // ...

    http.addFilter(securityInterceptor);
    http.setSharedObject(FilterSecurityInterceptor.class, securityInterceptor);
  }

  private FilterSecurityInterceptor createFilterSecurityInterceptor(H http,
      FilterInvocationSecurityMetadataSource metadataSource,
      AuthenticationManager authenticationManager) throws Exception {
    FilterSecurityInterceptor securityInterceptor = new FilterSecurityInterceptor();
    securityInterceptor.setSecurityMetadataSource(metadataSource);
    // 创建权限验证配置，默认为 AffirmativeBased，即满足一项则鉴权成功
    securityInterceptor.setAccessDecisionManager(getAccessDecisionManager(http));
    // 配置认证管理器
    securityInterceptor.setAuthenticationManager(authenticationManager);
    securityInterceptor.afterPropertiesSet();
    return securityInterceptor;
  }
```

在此过滤器的逻辑中，视图对此次访问进行权限验证，如果无权限，则会抛出 `AccessDeniedException`：

```java
  // Attempt authorization
  try {
    this.accessDecisionManager.decide(authenticated, object, attributes);
  }
  catch (AccessDeniedException accessDeniedException) {
    publishEvent(new AuthorizationFailureEvent(object, attributes, authenticated, accessDeniedException));
    throw accessDeniedException;
  }
```

一旦抛出了异常，顺序排在 `FilterSecurityInterceptor` 前一位的过滤器 `ExceptionTranslationFilter` 正好就能捕捉到此异常。此过滤器在 `HttpSecurity` 通过调用`exceptionHandling` 方法配置 `ExceptionHandlingConfigurer` 声明。过滤逻辑如下：

```java
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    try {
      chain.doFilter(request, response);

      logger.debug("Chain processed normally");
    } catch (IOException ex) {
      throw ex;
    } catch (Exception ex) {
      // 捕捉 FilterSecurityInterceptor 过滤逻辑抛出的异常
      // ...

      if (ase == null) {
        ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(
            AccessDeniedException.class, causeChain);
      }

      if (ase != null) {
        // ...
        // 捕捉到 AccessDeniedException
        handleSpringSecurityException(request, response, chain, ase);
      }
      else {
        // ...
      }
    }
  }

  private void handleSpringSecurityException(HttpServletRequest request,
      HttpServletResponse response, FilterChain chain, RuntimeException exception)
      throws IOException, ServletException {
    if (exception instanceof AuthenticationException) {
      // ...
      // 认证异常
      sendStartAuthentication(request, response, chain,
          (AuthenticationException) exception);
    } else if (exception instanceof AccessDeniedException) {
      // 权限异常
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authenticationTrustResolver.isAnonymous(authentication) || authenticationTrustResolver.isRememberMe(authentication)) {
        // ...
        sendStartAuthentication(request, response, chain,
            new InsufficientAuthenticationException(
              messages.getMessage(
                "ExceptionTranslationFilter.insufficientAuthentication",
                "Full authentication is required to access this resource")));
      }
      else {
        // ...
        // 其它异常
        accessDeniedHandler.handle(request, response,
            (AccessDeniedException) exception);
      }
    }
  }

  protected void sendStartAuthentication(HttpServletRequest request,
      HttpServletResponse response, FilterChain chain,
      AuthenticationException reason) throws ServletException, IOException {
    // ...
    SecurityContextHolder.getContext().setAuthentication(null);
    // 缓存被中断的请求
    requestCache.saveRequest(request, response);
    // ...
    // 错误处理
    authenticationEntryPoint.commence(request, response, reason);
  }
```

`ExceptionTranslationFilter` 对 `AuthenticationException` 、 `AccessDeniedException` 和其它异常分别处理。`FormLoginConfigurer` 在初始化调用 `init` 方法时，对 `ExceptionTranslationFilter` 配置了 `LoginUrlAuthenticationEntryPoint`，因此对于认证和授权异常，会将请求重定向到登录页面。而对于其它异常，使用 `AccessDeniedHandlerImpl`，如果有错误页面，跳转到错误页面；否则发送 `403` 错误给客户端。

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#websecurityconfigureradapter-configure)WebSecurityConfigurerAdapter#configure

在 `WebSecurityConfigurerAdapter` 中 `void configure(WebSecurity web)` 方法默认为空实现，通过覆盖此方法，开发者可以配置不需要经过 `Spring Security` 认证的请求。

```java
  public void configure(WebSecurity web) throws Exception {
      // 忽略指定url的请求（不走过滤器链）
      web.ignoring().mvcMatchers("/**");
  }
```

#### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#websecurity-performbuild)WebSecurity#performBuild

完成加载 `WebSecurityConfigurerAdapter` 中的配置后，进入 `WebSecurity#performBuild`，生成真正的安全过滤器链：

```java
  protected Filter performBuild() throws Exception {
    // ...

    int chainSize = ignoredRequests.size() + securityFilterChainBuilders.size();
    List<SecurityFilterChain> securityFilterChains = new ArrayList<>(chainSize);
    // 上文配置的需要忽略的请求
    for (RequestMatcher ignoredRequest : ignoredRequests) {
      securityFilterChains.add(new DefaultSecurityFilterChain(ignoredRequest));
    }
    // 上文配置的 HttpSecurity 实例
    for (SecurityBuilder<? extends SecurityFilterChain> securityFilterChainBuilder : securityFilterChainBuilders) {
      // 调用 build 方法生成 DefaultSecurityFilterChain
      securityFilterChains.add(securityFilterChainBuilder.build());
    }
    // 过滤器链代理
    FilterChainProxy filterChainProxy = new FilterChainProxy(securityFilterChains);

    // ...
  }
```

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#filterchainproxy-执行各过滤器)FilterChainProxy 执行各过滤器

生成 `FilterChainProxy` 的过程中，先是加载开发者配置的需要忽略的请求，包装到 `DefaultSecurityFilterChain` 中，生成一个没有过滤器的过滤器链。随之又调用 `HttpSecurity#build` 方法生成真正有过滤逻辑的过滤器链：

```java
  private FilterComparator comparator = new FilterComparator();

  protected DefaultSecurityFilterChain performBuild() throws Exception {
    // 先对配置的各过滤器进行排序，再加到过滤器链中
    Collections.sort(filters, comparator);
    return new DefaultSecurityFilterChain(requestMatcher, filters);
  }
```

过滤器过滤顺序排序依赖 `FilterComparator`。自此，`springSecurityFilterChain` 配置完成并被加入全局请求过滤器列表。

当请求经由 `FilterChainProxy` 过滤时，先根据 `request` 匹配过滤器：

```java
  private List<Filter> getFilters(HttpServletRequest request) {
    for (SecurityFilterChain chain : filterChains) {
      if (chain.matches(request)) {
        return chain.getFilters();
      }
    }
    return null;
  }
```

当请求配置为忽略，会匹配到无过滤器的 `DefaultSecurityFilterChain`，因此无需经历 `Spring Security` 各过滤器；否则走正常过滤逻辑。 拿到请求匹配的过滤器列表后，`FilterChainProxy` 生成一个 `VirtualFilterChain`，`Spring Security` 相关过滤器的过滤逻辑均在此执行，当相关过滤器执行完毕，再回到 `Spring MVC` 的过滤器链上来继续执行。

```java
  private VirtualFilterChain(FirewalledRequest firewalledRequest, FilterChain chain, List<Filter> additionalFilters) {
    // Spring MVC 过滤器链
    this.originalChain = chain;
    // 请求匹配到的过滤器列表
    this.additionalFilters = additionalFilters;
    this.size = additionalFilters.size();
    this.firewalledRequest = firewalledRequest;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response)
      throws IOException, ServletException {
    if (currentPosition == size) {
      // ...

      // Spring Security 相关过滤器执行完毕，回到 Spring MVC 的过滤器链上来继续执行
      originalChain.doFilter(request, response);
    }
    else {
      currentPosition++;
      Filter nextFilter = additionalFilters.get(currentPosition - 1);

      // 按顺序来执行 Spring Security 相关过滤器
      nextFilter.doFilter(request, response, this);
    }
  }
```

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（一）：过滤器链.html#小结)小结

（1） `Spring Security` 开箱即用，拥有完善的默认配置机制，基于过滤器对 `web` 应用进行保护。 （2） 如果开发者需要对 `Spring Security` 自动配置进行干预，可以继承 `WebSecurityConfigurerAdapter` 并实现它的 3 个 `configure` 方法：

- `void configure(AuthenticationManagerBuilder auth)`：配置认证管理器，开发者需要实现 `UserDetailsService` 接口，编写自定义认证逻辑，并将接口实现注册到 `Spring` 容器，在此方法中指定认证逻辑实现。

```java
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
  }
```

- `void configure(HttpSecurity http)`：配置过滤器管理器，开发者在此方法中对默认的 `HttpSecurity` 进行修改：

```java
  protected void configure(HttpSecurity http) throws Exception {
      http
              // 表单登录
              .formLogin().and()
              // 关闭 csrf 保护
              .csrf().disable()
              // 任何请求都需要认证
              .authorizeRequests().anyRequest().authenticated();
  }
```

- `void configure(WebSecurity web)`：请求忽略配置，开发者在此可以配置不需要进行安全认证的请求：

```java
  public void configure(WebSecurity web) throws Exception {
      // 忽略指定url的请求（不走过滤器链）
      web.ignoring().mvcMatchers("/**");
  }
```

（3）重要的定义

- `springSecurityFilterChain`：`Spring Security` 过滤器链。
- `AuthenticationManager`：认证管理器，负责对用户身份进行认证。
- `AuthenticationProvider`：认证逻辑具体实现，由认证管理器调用。
- `UserDetailsService`：通过 `username` 认证，包装成 `AuthenticationProvider` 使用。
- `SecurityContextPersistenceFilter`：从会话中加载有效认证信息或创建默认认证信息上下文。
- `FilterSecurityInterceptor`：最终决定是否放行请求，如果需要认证而未认证，或没有相应的权限，都会判断请求失败。
- `FilterChainProxy`：`Spring Security` 过滤器代理，关于安全的过滤逻辑均在此过滤器中执行，执行完成后才回到 `Spring MVC` 过滤器链中继续执行。





# Spring Security 源码分析（二）：表单登录

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（二）：表单登录.html#自定义表单登录)自定义表单登录

`FormLoginConfigurer` 提供了 `loginPage` 和 `loginProcessingUrl` 方法分别用于配置登录页面和表单提交请求处理路径。继承 `WebSecurityConfigurerAdapter`，重载关于过滤器和忽略请求的配置方法：

```java
  public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      // 表单登录，配置表单页面和表单提交URL
      http.formLogin().loginPage("/login.html").loginProcessingUrl("/login").and()
      // 任何请求都需要认证
      .authorizeRequests().anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
      // 登录页面请求不走 Spring Security 过滤器链
      web.ignoring().mvcMatchers("/login.html");
    }
  }
```

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（二）：表单登录.html#loginpage-自定义登录页面)loginPage 自定义登录页面

```java
  protected T loginPage(String loginPage) {
    // 配置自定义登录
    setLoginPage(loginPage);
    // 更新登录错误 / 登出路径（基于 loginPage）
    updateAuthenticationDefaults();
    // 更新自定义标识
    this.customLoginPage = true;
    return getSelf();
  }
```

在配置自定义登录页面后，同时会更新认证错误认证策略：

```java
  private void setLoginPage(String loginPage) {
    this.loginPage = loginPage;
    // 更新认证异常时跳转页面
    this.authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint(loginPage);
  }
```

上一节中说到，当 `ExceptionTranslationFilter` 拦截到认证异常后，会调用 `LoginUrlAuthenticationEntryPoint#commence` 方法进行处理，其主要逻辑为将用户请求重定向到登录页面。

```java
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    // ...

    // 获取配置的 loginPage
    String loginForm = determineUrlToUseForThisRequest(request, response, authException);
    // 请求重定向
    RequestDispatcher dispatcher = request.getRequestDispatcher(loginForm);
    dispatcher.forward(request, response);

    // ...
  }
```

`loginPage` 方法更新 `customLoginPage` 标识后，`DefaultLoginPageGeneratingFilter` 的过滤逻辑也随之改变，其默认配置的需要拦截的相关路径为 `/login`，当开发者自定义登录页面路径后，经由此过滤器就不会再被拦截。

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（二）：表单登录.html#loginpage-定义表单提交路径)loginPage 定义表单提交路径

`loginPage` 方法中调用了 `updateAuthenticationDefaults` 方法，可见当不手动配置 `loginProcessingUrl` 时，会使用 `loginPage` 作为表单提交路径。

```java
  protected final void updateAuthenticationDefaults() {
    if (loginProcessingUrl == null) {
      // 默认实用 loginPage 作为表单提交路径
      loginProcessingUrl(loginPage);
    }

    // ...
  }
```

但是 `FormLoginConfigurer` 配置的 `UsernamePasswordAuthenticationFilter` 则在默认构造方法中指定表单提交路径为 `/login`，也就是说如果开发者配置 `loginPage` 为其它路径，就无法正常进行认证。

```java
  public UsernamePasswordAuthenticationFilter() {
    super(new AntPathRequestMatcher("/login", "POST"));
  }

  /**
   * 路径匹配才进行认证
   */
  protected boolean requiresAuthentication(HttpServletRequest request,
    HttpServletResponse response) {
    return requiresAuthenticationRequestMatcher.matches(request);
  }
```

因此为了 `UsernamePasswordAuthenticationFilter` 能够正常执行，开发者需要手动指定 `loginProcessingUrl`。

```java
  public T loginProcessingUrl(String loginProcessingUrl) {
    this.loginProcessingUrl = loginProcessingUrl;
    // 设置过滤器处理登录逻辑的请求URL（可以指定其它名称覆盖 /login）
    authFilter.setRequiresAuthenticationRequestMatcher(createLoginProcessingUrlMatcher(loginProcessingUrl));
    return getSelf();
  }
```

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（二）：表单登录.html#忽略对自定义登录页面的拦截)忽略对自定义登录页面的拦截

上文示例中配置的是对所有请求进行拦截，当过滤器链发现没有认证就会跳转到登录页面，但是访问登录页面也需要认证，这就会造成一直重定向，无法完成登录。因此需要配置登录页面请求不走 `Spring Security` 过滤器链，这样所有人就可以正常访问登录页面。

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（二）：表单登录.html#登录后处理)登录后处理

登录成功后是跳转到首页还是用户原先想访问的页面？失败后是仍跳转到登录页面还是自定义的错误页面？`Spring Security` 提供了若干方法用于开发者自定义登录后处理：

```java
  @Resource
  private AuthenticationSuccessHandler successHandler;

  @Resource
  private AuthenticationFailureHandler failureHandler;

  protected void configure(HttpSecurity http) throws Exception {
    // 表单登录
    http.formLogin()
            // 认证成功后重定向URL
            .successForwardUrl("/")
            // 认证失败后重定向URL
            .failureForwardUrl("/login.html?error")
            // 如果是从其它页面重定向到登录页面，则成功后跳转到原请求URL，否则跳转到指定URL
            .defaultSuccessUrl("/", false)
            // 认证失败后重定向URL
            .failureUrl("/login.html?error")
            // 自定义认证成功后处理器
            .successHandler(successHandler)
            // 自定义认证失败后处理器
            .failureHandler(failureHandler)
            .and()
            // 任何请求都需要认证
            .authorizeRequests().anyRequest().authenticated();
  }
```

同样是重载 `void configure(HttpSecurity http)` 方法，干预认证过滤器的生成逻辑。可以看到 `HttpSecurity` 提供了 4 个修改重定向地址的方法，而实际上他们最后都是对 `successHandler` 和 `failureHandler` 进行配置。在 `UsernamePasswordAuthenticationFilter` 的认证逻辑中，当认证成功后会调用 `successfulAuthentication` 方法，而在此方法中又调用了 `AuthenticationSuccessHandler#onAuthenticationSuccess` 方法，如下是认证成功后处理器的一类实现：

```java
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    request.getRequestDispatcher(forwardUrl).forward(request, response);
  }
```

但是在前后端分离的项目中，认证系统可能作为一个单独的后端模块单独拆出来，配置登录跳转就无法满足与前端交互的任务，因此开发者需要继承 `AuthenticationSuccessHandler` 和 `AuthenticationFailureHandler`，自定义认证后响应，以下为向前端返回认证失败的 `Json` 数据的一类实现：

```java
@Component
public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    // 状态码 401
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    // 设置返回类型为 json
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(JSONUtils.toJSONString(exception));
  }
}
```

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（二）：表单登录.html#自定义过滤器)自定义过滤器

`HttpSecurity` 提供了若干方法为 `web` 请求添加过滤器，例如默认表单、认证过期、`CSRF` ` 保护等。同时开发者可以定义自已的过滤器，并指定在整个过滤器中的位置。如果需要在表单中添加验证码校验逻辑，可以使用如下示例：

```java
  /**
  * 认证失败后处理器
  */
  @Resource
  private AuthenticationFailureHandler failureHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // 在认证登录前验证验证码
    http.addFilterBefore(new CaptchaFilter(failureHandler),
    UsernamePasswordAuthenticationFilter.class)；
  }
```

在上文中我们说到，登录表单提交请求无论是成功还是失败，默认都会交由认证处理器进行跳转。因此在整个过滤器链中，关于验证码的过滤逻辑需要排在 `UsernamePasswordAuthenticationFilter` 之前。

```java
  public HttpSecurity addFilterBefore(Filter filter, Class<? extends Filter> beforeFilter) {
    // 过滤器排序器注册
    comparator.registerBefore(filter.getClass(), beforeFilter);
    return addFilter(filter);
  }
```

所有 `Spring Security` 配置的过滤器，其顺序由 `FilterComparator` 管理：

```java
final class FilterComparator implements Comparator<Filter>, Serializable {

  /**
   * 初始化顺序
   */
  private static final int INITIAL_ORDER = 100;

  /**
   * order 步长
   */
  private static final int ORDER_STEP = 100;

  /**
   * 过滤器名称 - 在过滤器中的顺序（order 越小，排序越靠前）
   */
  private final Map<String, Integer> filterToOrder = new HashMap<>();

  FilterComparator() {
    Step order = new Step(INITIAL_ORDER, ORDER_STEP);
    // ...

    put(SecurityContextPersistenceFilter.class, order.next());
    // ...

    put(UsernamePasswordAuthenticationFilter.class, order.next());
    // ...

    put(FilterSecurityInterceptor.class, order.next());
    // ...
  }

  public void registerBefore(Class<? extends Filter> filter, Class<? extends Filter> beforeFilter) {
    Integer position = getOrder(beforeFilter);
    // ...

    // 注册自定义过滤器
    put(filter, position - 1);
  }
}
```

可以看到，`FilterComparator` 对 `Spring Security` 配置的默认过滤器维护了一个 `filterToOrder`，用于描述各个过滤器在过滤器链中的顺序，前后两个过滤器的顺序相差 100。`registerBefore` 方法将开发者自定义的过滤器注册到 `FilterComparator` 方法中，并指定其顺序与 `UsernamePasswordAuthenticationFilter` 相差 1。这样在过滤器中就能保证自定义的验证码过滤器 [CaptchaFilter](https://github.com/wch853/snippet/blob/master/security/security-web/src/main/java/com/wch/snippet/security/config/filter/CaptchaFilter.java) 能够在认证过滤器前一位执行。

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（二）：表单登录.html#记住我)记住我

在过滤器配置中，可以通过 `remember` 方法配置 `记住我` 功能：

```java
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // 在认证登录前验证验证码
    http.rememberMe().and()
        // 任何请求都需要认证
        .authorizeRequests().anyRequest().authenticated();
  }
```

`rememberMe` 方法通过 `RememberMeConfigurer` 配置 `RememberMeAuthenticationFilter`，在 `init` 方法中先生成了一个 `RememberMeServices`：

```java
  private RememberMeServices getRememberMeServices(H http, String key) throws Exception {
    // ...

    //
    AbstractRememberMeServices tokenRememberMeServices = createRememberMeServices(
        http, key);
    // 表单登录参数名默认为 remember-me
    tokenRememberMeServices.setParameter(this.rememberMeParameter);
    // Cookie 名称默认为 remember-me
    tokenRememberMeServices.setCookieName(this.rememberMeCookieName);
    // ...

    // 配置记住我过期时间，默认为两周
    if (this.tokenValiditySeconds != null) {
  		tokenRememberMeServices.setTokenValiditySeconds(this.tokenValiditySeconds);
    }
    // ...

    // 如果用户主动登出了，需要清除记住我功能做的相关配置
    this.logoutHandler = tokenRememberMeServices;
    this.rememberMeServices = tokenRememberMeServices;
    return tokenRememberMeServices;
  }
```

对于创建基础的 `AbstractRememberMeServices`，`Spring Security` 提供了两种方式，一种是 `TokenBasedRememberMeServices`：是否能够使用 `记住我` 功能、校验都只依赖请求中的 `Cookie`；另一种是配置 `PersistentTokenRepository`：`InMemoryTokenRepositoryImpl` 在内存中维护一个 `Map` 用于对 `记住我` 进行校验，`JdbcTokenRepositoryImpl` 会从数据库中查询相关的 `token` 进行校验。这两种方式都是依赖请求中携带的 `Cookie`。当用户提交登出请求，应该取消 `记住我` 功能，`AbstractRememberMeServices` 对此的实现为清除名为 `remember-me` 的 `Cookie`：

```java
  protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
    // ...
    Cookie cookie = new Cookie(cookieName, null);
    cookie.setMaxAge(0);
    cookie.setPath(getCookiePath(request));
    // ...

    response.addCookie(cookie);
  }
```

`RememberMeServices` 配置完成后，`RememberMeAuthenticationFilter` 被加到过滤器链中，其过滤逻辑如下：

```java
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    // ...

    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      // 没有经过认证，SecurityContext 为空，尝试使用 记住我 功能

      // 检验 Cookie，如果有效进行自动登录
      Authentication rememberMeAuth = rememberMeServices.autoLogin(request, response);

      if (rememberMeAuth != null) {
        // remeber-me Cookie 有效，走用户认证逻辑
        try {
          rememberMeAuth = authenticationManager.authenticate(rememberMeAuth);
          SecurityContextHolder.getContext().setAuthentication(rememberMeAuth);
          // ...
        } catch (AuthenticationException authenticationException) {
          // 认证失败了，也会清除 remeber-me Cookie
          rememberMeServices.loginFail(request, response);
          // ...
        }
      }

      chain.doFilter(request, response);
    } else {
      // ...

      chain.doFilter(request, response);
    }
  }
```

如果 `SecurityContext` 没有认证信息，过滤器会尝试使用 `记住我` 功能，调用 `autoLogin` 方法：

```java
  public final Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
    // 从请求中获取名为 remember-me 的 Cookie
    String rememberMeCookie = extractRememberMeCookie(request);
    // ...

    UserDetails user = null;
    try {
      // 原 Cookie 解码
      String[] cookieTokens = decodeCookie(rememberMeCookie);
      user = processAutoLoginCookie(cookieTokens, request, response);
      // 校验用户账号是否有效
      userDetailsChecker.check(user);
      // 创建认证信息
      return createSuccessfulAuthentication(request, user);
    } catch (CookieTheftException cte) {
      // 清理客户端的 remember-me Cookie
      cancelCookie(request, response);
      throw cte;
    }
    // ...

    cancelCookie(request, response);
    return null;
  }
```

`TokenBasedRememberMeServices` 中对于 `processAutoLoginCookie` 方法的实现为：

```java
  protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
    // ...

    long tokenExpiryTime;

    try {
      tokenExpiryTime = new Long(cookieTokens[1]).longValue();
    } catch (NumberFormatException nfe) {
      throw new InvalidCookieException(
          "Cookie token[1] did not contain a valid number (contained '"
              + cookieTokens[1] + "')");
    }

    // ...

    // 根据用户名加载用户信息
    UserDetails userDetails = getUserDetailsService().loadUserByUsername(cookieTokens[0]);

    // 根据过期时间、用户名、密码生成 MD5 签名
    String expectedTokenSignature = makeTokenSignature(tokenExpiryTime,
        userDetails.getUsername(), userDetails.getPassword());

    // 校验签名
    if (!equals(expectedTokenSignature, cookieTokens[2])) {
      throw new InvalidCookieException("Cookie token[2] contained signature '"
          + cookieTokens[2] + "' but expected '" + expectedTokenSignature + "'");
    }

    return userDetails;
  }
```

对 `Cookie` 进行解码后，根据登录用户的相关信息做 `MD5` 校验，如果认定 `Cookie` 中的 `Token` 有效，则会查询用户信息，生成认证身份，通过认证流程。那么，`记住我` 的 `Cookie` 是在何时放入的呢？在 `RememberMeConfigurer` 的初始化过程中，默认的 `remember-me` 表单名被传递给表单生成过滤器：

```java
private void initDefaultLoginFilter(H http) {
  DefaultLoginPageGeneratingFilter loginPageGeneratingFilter = http
    .getSharedObject(DefaultLoginPageGeneratingFilter.class);
  if (loginPageGeneratingFilter != null) {
    // 将表单名 remember-me 传递给默认表单生成过滤器，生成 记住我 复选框
    loginPageGeneratingFilter.setRememberMeParameter(getRememberMeParameter());
  }
}
```

表单中会根据传入的名称生成如下 `HTML` 代码：

```html
<p>
  <input type="checkbox" name="remember-me" /> Remember me on this computer.
</p>
```

`UsernamePasswordAuthenticationFilter` 在用户认证成功后会调用 `successfulAuthentication` 方法，在此方法中会取出 `remember-me` 参数，判断是否使用 `记住我` 功能。而后又调用了 `RememberMeServices` 的 `loginSuccess` 方法：

```java
  public final void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
    // 请求参数中是否要求使用记住我功能
    if (!rememberMeRequested(request, parameter)) {
      logger.debug("Remember-me login not requested.");
      return;
    }

    // 要求使用记住我功能，响应中构造 Cookie
    onLoginSuccess(request, response, successfulAuthentication);
  }
```

`TokenBasedRememberMeServices` 中对于 `onLoginSuccess` 方法的实现为：

```java
public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {

  String username = retrieveUserName(successfulAuthentication);
  String password = retrievePassword(successfulAuthentication);
  // ...

  // Cookie 失效时间
  int tokenLifetime = calculateLoginLifetime(request, successfulAuthentication);
  long expiryTime = System.currentTimeMillis();
  expiryTime += 1000L * (tokenLifetime < 0 ? TWO_WEEKS_S : tokenLifetime);

  // 构造 MD5 签名，与校验 Cookie 的逻辑一致
  String signatureValue = makeTokenSignature(expiryTime, username, password);

  // 在响应中添加 Cookie
  setCookie(new String[] { username, Long.toString(expiryTime), signatureValue },
            tokenLifetime, request, response);
  // ...
}
```

如果配置了 `PersistentTokenRepository` 其流程大致相同，区别在于 `Cookie` 的生成和校验逻辑不同，在此不多做赘述。

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（二）：表单登录.html#小结)小结

- 通过配置 `FormLoginConfigurer` 的 `loginPage` 和 `loginProcessingUrl` 可以切换默认登录页面和表单请求地址。

- 通过配置 `FormLoginConfigurer` 的 `successHandler` 和 `failureHandler` 可以干预认证成功 / 失败后的服务端控制行为。

- `Spring Security` 过滤器链中的过滤器执行顺序由 `FilterComparator` 管理。如果需要在过滤器链中增加自定义过滤器，可以通过 `HttpSecurity` 的 `addFilterBefore` 或者 `addFilterAfter` 方法将自定义过滤器加在指定过滤器前 / 后。

- 配置 `RememberMe` 后，认证成功和失败，响应都会返回 `Cookie` 给客户端，下一次未经认证即访问 `web` 资源时，会对请求携带的 `Cookie` 进行校验，如果有效则会自动登录，执行认证逻辑。

- 表单登录的相关扩展仍然离不开过滤器的支持，以下是几个较为重要的 `Spring Security` 过滤器：

  ![SpringSecuirty主要过滤器](https://wch853.github.io/img/security/SpringSecuirty%E4%B8%BB%E8%A6%81%E8%BF%87%E6%BB%A4%E5%99%A8.png)

# Spring Security 源码分析（三）：授权管理

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（三）：授权管理.html#url-访问权限配置)URL 访问权限配置

`Spring Security` 允许在过滤器配置中使用如下方式对特定 `URL` 做权限配置：

```java
  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
      // /api1/** 需要 ROLE_ADMIN 角色才能访问
      .antMatchers("/api1/**").hasRole("ADMIN")
      // /api2/** 需要 USER 权限才能访问
      .antMatchers("/api2/**").hasAuthority("USER")
      // /api3/** 允许任何人访问
      .antMatchers("/api3/**").permitAll()
      // 其它所有接口都需要认证后才能访问
      .anyRequest().authenticated();
  }
```

`HttpSecurity#authorizeRequests` 方法引入了 `ExpressionUrlAuthorizationConfigurer` 配置了 `ExpressionInterceptUrlRegistry`，这是一个包含了多组 `RequestMatcher` 和不同访问权限的注册对象。`antMatchers` 方法生成指定路径匹配对象 `RequestMatcherConfigurer`，`hasRole` 、`hasAuthority` 等方法则根据配置的访问权限生成 `SpEL` 表达式，其最终目的是通过 `SpEL expressions` 来做权限控制：

```java
private static String hasRole(String role) {
  // 根据配置的角色添加了 ROLE_ 前缀
  return "hasRole('ROLE_" + role + "')";
}

private static String hasAuthority(String authority) {
  return "hasAuthority('" + authority + "')";
}
```

这些配置的 `SpEL` 表达式最终通过 `interceptUrl` 方法与对应的路径匹配对象注册到 `REGISTRY` 中：

```java
  public ExpressionInterceptUrlRegistry hasRole(String role) {
    return access(ExpressionUrlAuthorizationConfigurer.hasRole(role));
  }

  public ExpressionInterceptUrlRegistry hasAnyRole(String... roles) {
    return access(ExpressionUrlAuthorizationConfigurer.hasAnyRole(roles));
  }

  public ExpressionInterceptUrlRegistry access(String attribute) {
    if (not) {
      attribute = "!" + attribute;
    }
    interceptUrl(requestMatchers, SecurityConfig.createList(attribute));
    return ExpressionUrlAuthorizationConfigurer.this.REGISTRY;
  }
  private void interceptUrl(Iterable<? extends RequestMatcher> requestMatchers,
                            Collection<ConfigAttribute> configAttributes) {
    for (RequestMatcher requestMatcher : requestMatchers) {
      // 注册 路径匹配对象-访问权限 SpEL 表达式
      REGISTRY.addMapping(new AbstractConfigAttributeRequestMatcherRegistry.UrlMapping(
        requestMatcher, configAttributes));
    }
  }
```

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（三）：授权管理.html#载入权限配置)载入权限配置

在第一章中我们说到，通过调用 `HttpSecurity#authorizeRequests` 方法可以配置 `Spring Security` 最后的守门员 `FilterSecurityInterceptor`，这是过滤器链中的最后一个过滤器。

```java
  // 获取访问权限配置
  FilterInvocationSecurityMetadataSource metadataSource = createMetadataSource(http);
  // 使用访问权限配置创建 FilterSecurityInterceptor
  FilterSecurityInterceptor securityInterceptor = createFilterSecurityInterceptor(http, metadataSource, http.getSharedObject(AuthenticationManager.class));
```

在创建 `FilterSecurityInterceptor` 时要求传入 `metadataSource`，而这就是上文中所说的 `REGISTRY` 相关配置：

```java
  final ExpressionBasedFilterInvocationSecurityMetadataSource createMetadataSource(H http) {
    // 获取配置的 RequestMatcher 与 访问权限关系
    LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = REGISTRY.createRequestMap();
    // ...
    return new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap, getExpressionHandler(http));
  }
```

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（三）：授权管理.html#鉴权流程)鉴权流程

进入最后一个过滤器 `FilterSecurityInterceptor` 后，认证流程会调用 `AbstractSecurityInterceptor#beforeInvocation` 方法：

```java
  protected InterceptorStatusToken beforeInvocation(Object object) {
    // ...
    // 根据当前的 request 对象获取对应的访问权限
    Collection<ConfigAttribute> attributes = this.obtainSecurityMetadataSource().getAttributes(object);
    // ...

    try {
      // 决定是否允许访问
      this.accessDecisionManager.decide(authenticated, object, attributes);
    }
    // ...
  }
```

根据当前请求匹配到对应的访问权限后，使用 `AccessDecisionManager` 决定是否对当前请求放行，`AccessDecisionManager` 有三种实现：

- `AffirmativeBased`：任一个投票器通过即允许访问。
- `ConsensusBased`：投票器通过半数即运行访问。
- `UnanimousBased`：所有投票器通过才允许访问。

默认实现为 `AffirmativeBased`，投票器用来判定当前请求是否允许访问，默认实现为 `WebExpressionVoter` 这是一种根据 `SpEL` 表达式匹配来判断访问权限的投票器。

```java
  public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
    int deny = 0;
		// 遍历所有投票器
    for (AccessDecisionVoter voter : getDecisionVoters()) {
      // 根据用户权限列表做 SpEL 比较
      int result = voter.vote(authentication, object, configAttributes);
      switch (result) {
        case AccessDecisionVoter.ACCESS_GRANTED:
          // 有一个允许通过则结束投票
          return;
        case AccessDecisionVoter.ACCESS_DENIED:
          // 不允许通过次数+1
          deny++;
          break;
        default:
          break;
      }
    }
		// 投票不通过，抛出 AccessDeniedException
    if (deny > 0) {
      throw new AccessDeniedException(messages.getMessage(
        "AbstractAccessDecisionManager.accessDenied", "Access is denied"));
    }
  }
```

`AffirmativeBased` 的投票逻辑如上，只要有一个投票器投票通过则投票结束，否则抛出 `AccessDeniedException` 异常。

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（三）：授权管理.html#小结)小结

- 在配置过滤器链时，结合使用 `antMatchers` 和权限配置访问可以对匹配 `URL` 做权限控制。
- 通过比较用户认证信息的权限与请求的访问权限的 `SpEL` 表达式来最终确认是否可以访问。

# Spring Security 源码分析（四）：OAuth2 实现

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#oauth2)OAuth2

`OAuth2`是一个开放[标准](https://tools.ietf.org/html/rfc6749#section-4.1.1)，允许用户在不将用户名和密码提供给第三方应用的情况下，授权第三方应用访问用户存储在其它服务提供者服务器上的资源。

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#角色)角色

`OAuth` 标准中定义了以下几种角色：

- `Resource Owner`：资源所有者。拥有访问受保护资源权限的实体，即用户。
- `Resource Server`：托管受保护资源的服务器，能够接收和响应持有 `access token` 的对受保护资源的请求。
- `Client` ：资源所有者许可访问受保护资源的第三方应用。
- `Authorization Server`：授权服务器，在对资源所有者及其权限认证完成后向第三方应用颁发 `access token`。

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#申请授权流程)申请授权流程

```text
 +--------+                               +---------------+
 |        |--(A)- Authorization Request ->|   Resource    |
 |        |                               |     Owner     |
 |        |<-(B)-- Authorization Grant ---|               |
 |        |                               +---------------+
 |        |
 |        |                               +---------------+
 |        |--(C)-- Authorization Grant -->| Authorization |
 | Client |                               |     Server    |
 |        |<-(D)----- Access Token -------|               |
 |        |                               +---------------+
 |        |
 |        |                               +---------------+
 |        |--(E)----- Access Token ------>|    Resource   |
 |        |                               |     Server    |
 |        |<-(F)--- Protected Resource ---|               |
 +--------+                               +---------------+
```

上图描述了 `OAuth 2.0` 四种角色之间的交互流程，包括以下步骤：

- （A）第三方应用向资源所有者请求授权。
- （B）第三方应用收到授权许可。
- （C）第三方应用向授权服务器出示授权许可。
- （D）授权服务器验证第三方应用身份和授权许可，颁发访问令牌。
- （E）第三方应用持有访问令牌向资源服务器请求受保护资源。
- （F）资源服务器验证访问令牌并响应。

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#授权方式)授权方式

- `authorization_code`：授权码模式，第三方应用引导资源所有者前往授权服务器进行授权，完成后引导资源所有者携带授权码会回到第三方应用，通过授权码第三方应用可以获取真正的访问令牌。
- `implicit`：隐式许可，相对于授权码模式，第三方应用不再需要授权码，而是在资源所有者授权后直接被颁发一个访问令牌。
- `password`：资源所有者密码凭据，将资源所有者的密码凭据直接作为获取访问令牌的授权许可。通常用于资源所有者高度信任第三方应用的情况。
- `client_credentials`：第三方应用凭证，仅使用 `client_id` 和 `client_secret` 进行授权，用来获取第三方应用下控制的资源，或者事先与授权服务器商定好的受保护资源。

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#spring-oauth2-自动装配)Spring OAuth2 自动装配

引入 `spring-security-oauth2-autoconfigure` 依赖：

```xml
<dependency>
	<groupId>org.springframework.security.oauth.boot</groupId>
	<artifactId>spring-security-oauth2-autoconfigure</artifactId>
</dependency>
```

此依赖中的 `spring.factories` 文件指定加载 `OAuth2AutoConfiguration`，此类通过 `@Import` 注解引入 `OAuth2AuthorizationServerConfiguration`，这是一个实现了 `AuthorizationServerConfigurer` 接口的类，接口中的注释说到：此接口中的方法都是用来配置 `OAuth2` 授权服务器的，如果使用了 `@EnableAuthorizationServer` 注解，这个接口的实现类都会自动被注入到 `Spring` 容器中。

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#授权服务器自动化配置)授权服务器自动化配置

通过添加 `@EnableAuthorizationServer` 注解，一系列默认配置就被引入 `Spring` 容器。

#### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#第三方应用认证配置)第三方应用认证配置

```java
@Configuration
public class ClientDetailsServiceConfiguration {

  @SuppressWarnings("rawtypes")
  private ClientDetailsServiceConfigurer configurer = new ClientDetailsServiceConfigurer(new ClientDetailsServiceBuilder());

  /**
   * 将第三方应用认证服务配置器注入 Spring 容器，使得认证服务可被干预
   */
  @Bean
  public ClientDetailsServiceConfigurer clientDetailsServiceConfigurer() {
    return configurer;
  }

  /**
   * 懒加载第三方应用认证服务，等待认证服务加载干预配置
   */
  @Bean
  @Lazy
  @Scope(proxyMode=ScopedProxyMode.INTERFACES)
  public ClientDetailsService clientDetailsService() throws Exception {
    return configurer.and().build();
  }

}
```

可以看到 `ClientDetailsServiceConfigurer` 的一个实例被注入到 `Spring` 容器中，此配置生成的 `ClientDetailsService` 实例标记了 `@Lazy` 注解，要求被懒加载，此实例加载其它配置后在第一次使用时进行实例化。

#### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#授权端点配置)授权端点配置

在 `OAuth2` 框架标准（[端点协议](https://tools.ietf.org/html/rfc6749)）中，获取授权需要经由两个授权服务器端点：

- `Authorization Endpoint`：通过客户端跳转引导资源所有者向第三方应用授权。
- `Token Endpoint`：第三方应用携带授权许可与授权服务器交换访问令牌。

`AuthorizationServerEndpointsConfiguration` 以 `Spring MVC` 接口的形式注入了两个端点的实现：

```java
@Configuration
@Import(TokenKeyEndpointRegistrar.class)
public class AuthorizationServerEndpointsConfiguration {

  /**
   * 授权端点配置
   */
  private AuthorizationServerEndpointsConfigurer endpoints = new AuthorizationServerEndpointsConfigurer();

  /**
   * 第三方应用认证服务
   */
  @Autowired
  private ClientDetailsService clientDetailsService;

  /**
   * 自定义授权服务器配置
   */
  @Autowired
  private List<AuthorizationServerConfigurer> configurers = Collections.emptyList();

  @PostConstruct
  public void init() {
    for (AuthorizationServerConfigurer configurer : configurers) {
      try {
        // 将自定义配置应用到授权服务器
        configurer.configure(endpoints);
      } catch (Exception e) {
        throw new IllegalStateException("Cannot configure enpdoints", e);
      }
    }
    endpoints.setClientDetailsService(clientDetailsService);
  }

  /**
   * 配置 /oauth/authorize 接口
   */
  @Bean
  public AuthorizationEndpoint authorizationEndpoint() throws Exception {
    // 声明 /oauth/authorize GET 和 POST 请求
    AuthorizationEndpoint authorizationEndpoint = new AuthorizationEndpoint();

    // 获取自定义授权端点配置的 URL 映射
    FrameworkEndpointHandlerMapping mapping = getEndpointsConfigurer().getFrameworkEndpointHandlerMapping();
    // 默认授权确认页面为 /oauth/confirm_access，允许自定义
    authorizationEndpoint.setUserApprovalPage(extractPath(mapping, "/oauth/confirm_access"));
    authorizationEndpoint.setProviderExceptionHandler(exceptionTranslator());
    // 默认授权失败页面为 /oauth/error，允许自定义
    authorizationEndpoint.setErrorPage(extractPath(mapping, "/oauth/error"));
    // 配置访问令牌授权器，访问令牌默认存储在内存中，允许多种授权方式
    authorizationEndpoint.setTokenGranter(tokenGranter());
    authorizationEndpoint.setClientDetailsService(clientDetailsService);
    authorizationEndpoint.setAuthorizationCodeServices(authorizationCodeServices());
    authorizationEndpoint.setOAuth2RequestFactory(oauth2RequestFactory());
    authorizationEndpoint.setOAuth2RequestValidator(oauth2RequestValidator());
    // 授权确认策略，允许配置为自动确认和跳转确认等多种方式
    authorizationEndpoint.setUserApprovalHandler(userApprovalHandler());
    // 跳转处理器，验证允许跳转的授权方式，验证跳转路径是否注册
    authorizationEndpoint.setRedirectResolver(redirectResolver());
    return authorizationEndpoint;
  }

  /**
   * 配置 /oauth/token 接口
   */
  @Bean
  public TokenEndpoint tokenEndpoint() throws Exception {
    // 声明 /oauth/token GET 和 POST 请求
    TokenEndpoint tokenEndpoint = new TokenEndpoint();
    tokenEndpoint.setClientDetailsService(clientDetailsService);
    tokenEndpoint.setProviderExceptionHandler(exceptionTranslator());
    // 配置访问令牌授权器
    tokenEndpoint.setTokenGranter(tokenGranter());
    tokenEndpoint.setOAuth2RequestFactory(oauth2RequestFactory());
    tokenEndpoint.setOAuth2RequestValidator(oauth2RequestValidator());
    // 配置 /oauth/token 接口允许的请求方法，默认只允许 POST 方法
    tokenEndpoint.setAllowedRequestMethods(allowedTokenEndpointRequestMethods());
    return tokenEndpoint;
  }

  /**
   * 配置 /oauth/check_token 接口
   */
  @Bean
  public CheckTokenEndpoint checkTokenEndpoint() {
    // 校验 token 是否有效并返回相关信息
    CheckTokenEndpoint endpoint = new CheckTokenEndpoint(getEndpointsConfigurer().getResourceServerTokenServices());
    endpoint.setAccessTokenConverter(getEndpointsConfigurer().getAccessTokenConverter());
    endpoint.setExceptionTranslator(exceptionTranslator());
    return endpoint;
  }

  /**
   * 配置授权确认页面，可以自定义覆盖
   */
  @Bean
  public WhitelabelApprovalEndpoint whitelabelApprovalEndpoint() {
    return new WhitelabelApprovalEndpoint();
  }

  /**
   * 配置授权错误页面，可以自定义覆盖
   */
  @Bean
  public WhitelabelErrorEndpoint whitelabelErrorEndpoint() {
    return new WhitelabelErrorEndpoint();
  }
  // ...

}
```

`Spring OAuth2` 为这些端点配置了 `@FrameworkEndpoint` 注解，通过此注解，端点的 `URL` 即其映射方法会被 `Spring MVC` 读取到，使得各个端点可以像其它 `REST` 接口一样提供服务。

#### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#授权服务器安全配置)授权服务器安全配置

`AuthorizationServerSecurityConfiguration` 继承了 `WebSecurityConfigurerAdapter`，`Spring Security` 允许在容器中配置多个 `WebSecurityConfigurerAdapter` 实例，不同实例分别构造成为 `SecurityFilterChain`，多个过滤器链交由 `FilterChainProxy` 进行代理，当接受到请求后，`Proxy` 会根据请求路径匹配相应的过滤器链：

```java
  private List<Filter> getFilters(HttpServletRequest request) {
    // 根据请求路径匹配过滤器链
    for (SecurityFilterChain chain : filterChains) {
      if (chain.matches(request)) {
        return chain.getFilters();
      }
    }
    return null;
  }
```

来看看 `AuthorizationServerSecurityConfiguration` 的实现：

```java
@Configuration
// 优先级高
@Order(0)
@Import({ ClientDetailsServiceConfiguration.class, AuthorizationServerEndpointsConfiguration.class })
public class AuthorizationServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

  /**
   * 自定义授权服务器配置
   */
  @Autowired
  private List<AuthorizationServerConfigurer> configurers = Collections.emptyList();

  /**
   * 第三方客户端认证配置
   */
  @Autowired
  private ClientDetailsService clientDetailsService;

  /**
   * 授权端点配置
   */
  @Autowired
  private AuthorizationServerEndpointsConfiguration endpoints;

  @Autowired
  public void configure(ClientDetailsServiceConfigurer clientDetails) throws Exception {
    for (AuthorizationServerConfigurer configurer : configurers) {
      // 干预第三方客户端认证配置
      configurer.configure(clientDetails);
    }
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // Over-riding to make sure this.disableLocalConfigureAuthenticationBldr = false
    // 默认使用 UserDetailsService 的实现类生成认证过滤器，而本套过滤器链是面向第三方应用认证而不是用户认证，所以不需要初始化 AuthenticationManager
  }

  /**
   * 过滤器配置
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // 授权服务器安全配置扩展
    AuthorizationServerSecurityConfigurer configurer = new AuthorizationServerSecurityConfigurer();
    // 自定义授权确认、授权失败页面路径映射
    FrameworkEndpointHandlerMapping handlerMapping = endpoints.oauth2EndpointHandlerMapping();
    http.setSharedObject(FrameworkEndpointHandlerMapping.class, handlerMapping);
    // 加载自定义授权服务器安全配置扩展
    configure(configurer);
    http.apply(configurer);

    String tokenEndpointPath = handlerMapping.getServletPath("/oauth/token");
    String tokenKeyPath = handlerMapping.getServletPath("/oauth/token_key");
    String checkTokenPath = handlerMapping.getServletPath("/oauth/check_token");
    if (!endpoints.getEndpointsConfigurer().isUserDetailsServiceOverride()) {
      // 从自定义配置和容器中取得配置的 AuthenticationManager，用于刷新 token
      UserDetailsService userDetailsService = http.getSharedObject(UserDetailsService.class);
      endpoints.getEndpointsConfigurer().userDetailsService(userDetailsService);
    }

    http
      .authorizeRequests()
      // 访问 /oauth/token 接口需要完全认证
      .antMatchers(tokenEndpointPath).fullyAuthenticated()
      // 配置访问 /oauth/token_key 和 /oauth/check_token 接口的权限
      .antMatchers(tokenKeyPath).access(configurer.getTokenKeyAccess())
      .antMatchers(checkTokenPath).access(configurer.getCheckTokenAccess())
      .and()
      .requestMatchers()
      .antMatchers(tokenEndpointPath, tokenKeyPath, checkTokenPath)
      .and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    http.setSharedObject(ClientDetailsService.class, clientDetailsService);
  }

  protected void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
    for (AuthorizationServerConfigurer configurer : configurers) {
      // 应用自定义授权服务器安全配置扩展
      configurer.configure(oauthServer);
    }
  }
}
```

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#授权服务器配置扩展)授权服务器配置扩展

#### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#默认扩展)默认扩展

在授权端点配置和授权服务器安全配置中，都注入了 `AuthorizationServerConfigurer` 接口的实现，`spring-security-oauth2-autoconfigure` 对此接口中的 3 个 `configure` 方法的实现如下：

```java
  /**
    * 第三方应用认证配置
    */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    // 根据配置初始化第三方认证信息，放入内存
    ClientDetailsServiceBuilder<InMemoryClientDetailsServiceBuilder>.ClientBuilder builder = clients
      .inMemory().withClient(this.details.getClientId());
    builder.secret(this.details.getClientSecret())
    // ...

    // 配置 token 有效时间，默认12小时
    if (this.details.getAccessTokenValiditySeconds() != null) {
      builder.accessTokenValiditySeconds(
        this.details.getAccessTokenValiditySeconds());
    }
    // 配置刷新令牌有效时间，默认30天
    if (this.details.getRefreshTokenValiditySeconds() != null) {
      builder.refreshTokenValiditySeconds(
        this.details.getRefreshTokenValiditySeconds());
    }
    // 配置运行跳转的 URL
    if (this.details.getRegisteredRedirectUri() != null) {
      builder.redirectUris(
        this.details.getRegisteredRedirectUri().toArray(new String[0]));
    }
  }

  /**
    * 授权端点自定义配置
    */
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints)
    throws Exception {
    if (this.tokenConverter != null) {
      // 配置 /oauth/check_token 接口验证验证成功返回 token 信息逻辑
      endpoints.accessTokenConverter(this.tokenConverter);
    }
    if (this.tokenStore != null) {
      // 配置 token 存储策略，默认在内存中
      endpoints.tokenStore(this.tokenStore);
    }
    if (this.details.getAuthorizedGrantTypes().contains("password")) {
      // 配置用户认证管理器，用于密码模式中对用户身份进行认证
      endpoints.authenticationManager(this.authenticationManager);
    }
  }

  /**
    * 配置授权安全策略
    */
  @Override
  public void configure(AuthorizationServerSecurityConfigurer security)
    throws Exception {
    security.passwordEncoder(NoOpPasswordEncoder.getInstance());
    if (this.properties.getCheckTokenAccess() != null) {
      // 配置 /oauth/check_token 接口访问权限
      security.checkTokenAccess(this.properties.getCheckTokenAccess());
    }
    if (this.properties.getTokenKeyAccess() != null) {
      // 配置 /oauth/token_key 接口访问权限
      security.tokenKeyAccess(this.properties.getTokenKeyAccess());
    }
    if (this.properties.getRealm() != null) {
      // 配置 realm 名称，默认为 realm
      security.realm(this.properties.getRealm());
    }
  }
```

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#oauth2-授权)OAuth2 授权

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#授权码授权)授权码授权

#### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#获取授权码)获取授权码

`Spring OAuth2` 装配了 `AuthorizationEndpoint` 用于实现授权码模式中的 `/authroize` 接口。在启动项目分析这个接口的实现之前，我们需要增加一个默认的 `WebSecurityConfigurerAdapter` 实现：

```java
@Configuration
@EnableAuthorizationServer
public class OAuth2SecurityConfig extends WebSecurityConfigurerAdapter {
}
```

在第一章的分析中，我们说到如果 `Spring` 容器中有 `WebSecurityConfigurerAdapter` 的实例，那么默认的实例将不会被装配。引入 `OAuth2` 相关依赖后，自动配置的 `AuthorizationServerSecurityConfiguration` 就是一个 `WebSecurityConfigurerAdapter` 实例，而其中并没有对 `/authorize` 接口做保护（匹配不到过滤器链，会直接访问），而此接口中需要用户的认证信息，所以我们需要另外添加 `WebSecurityConfigurerAdapter` 实现，用于配置 `/authorize` 接口需要认证后才能访问。

添加此配置后启动应用，在浏览器中访问：

```text
http://localhost:8080/oauth/authorize?response_type=code&client_id=wch&state=xyz&scope=all&redirect_uri=http://localhost:8080
```

传参为 `OAuth2` 标准获取授权码的标准参数：

- `response_type` ：传固定值 `code` 表示使用授权码模式申请授权。
- `client_id`：第三方应用认证账号。
- `state`：用于保持请求和回调的状态，会照原样回传此参数。
- `scope`：申请 `scope` 权限。
- `redirect_uri`：授权回调地址。

来看看 `GET /authorize` 接口：

```java
  @RequestMapping(value = "/oauth/authorize")
  public ModelAndView authorize(Map<String, Object> model, @RequestParam Map<String, String> parameters, SessionStatus sessionStatus, Principal principal) {
    // 提取请求参数，生成 AuthorizationRequest
    AuthorizationRequest authorizationRequest = getOAuth2RequestFactory().createAuthorizationRequest(parameters);
    // 授权类型
    Set<String> responseTypes = authorizationRequest.getResponseTypes();
    // ...

    try {
      // 验证用户是否已认证
      if (!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()) {
        throw new InsufficientAuthenticationException("User must be authenticated with Spring Security before authorization can be completed.");
      }
      // 查询第三方应用信息
      ClientDetails client = getClientDetailsService().loadClientByClientId(authorizationRequest.getClientId());
      // ...
      // 配置回调地址
      authorizationRequest.setRedirectUri(resolvedRedirect);
      // 验证参数中的 scope 是否有效
      oauth2RequestValidator.validateScope(authorizationRequest, client);
      // 验证请求授权的 scope 是否默认确认授权。userApprovalHandler 可配置，默认为所有请求的 scope 自动确认授权方可直接授权
      authorizationRequest = userApprovalHandler.checkForPreApproval(authorizationRequest, (Authentication) principal);
      // ...

      if (authorizationRequest.isApproved()) {
        // 如果默认确认授权
        if (responseTypes.contains("token")) {
          // 隐式授权模式直接返回访问令牌
          return getImplicitGrantResponse(authorizationRequest);
        }
        if (responseTypes.contains("code")) {
          // 授权码模式直接返回授权码
          return new ModelAndView(getAuthorizationCodeResponse(authorizationRequest, (Authentication) principal));
        }
      }

      // 如果没有自动授权，跳转到 /oauth/confirm_access 进行确认授权
      model.put("authorizationRequest", authorizationRequest);
      return getUserApprovalPageResponse(model, authorizationRequest, (Authentication) principal);

    }
    // ...

  }
```

如果没用配置自动确认， `GET /authorize` 接口并不会直接返回授权码，而是会跳转到 `/oauth/confirm_access` 页面进行二次授权确认。端点配置中配置了 `WhitelabelApprovalEndpoint` 用于生成默认的二次确认页面，此页面中有一个表单，将用户的确认结果发往 `POST /oauth/authorize` 接口，在此接口中的验证逻辑中，如果用户进行了确认授权，则会在跳转地址中携带授权码。

#### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#换取访问令牌)换取访问令牌

拥有授权码即可访问 `OAuth2` 标准中获取访问令牌的接口 `POST /token`：

```text
http://localhost:8080/oauth/token?grant_type=authorization_code&code=E2voni&scope=all&redirect_uri=http://localhost:8080
```

传参为 `OAuth2` 标准获取访问令牌的标准参数：

- `grant_type`：为固定值 `authorization_code`。
- `code`：获取的授权码。
- `scope`：申请的 `scope` 。
- `redirect_uri` ：需传入获取授权码使用的跳转路径用于验证。

`AuthorizationServerSecurityConfiguration` 在配置过滤器时指定 `/oauth/token` 需要进行 `fullyAuthenticated` 认证后才能访问，但是用于认证的并不是注入到 `Spring` 容器中的 `UserDetailsService` 实现，而是上文提到的配置懒加载的 `ClientDetailsService` ，`AuthorizationServerSecurityConfigurer#init` 配置如下：

```java
  @Override
  public void init(HttpSecurity http) throws Exception {
    // ...

    if (passwordEncoder != null) {
      // 有密码的配置
      // ...
    else {
      // 无密码的配置
      // 配置认证服务为第三方认证服务
      http.userDetailsService(new ClientDetailsUserDetailsService(clientDetailsService()));
    }
    // 配置不对 SecurityContext 进行存储和读取
    http.securityContext().securityContextRepository(new NullSecurityContextRepository()).and().csrf().disable()
      // 配置 http basic 认证
      .httpBasic().realmName(realm);
    // ...
  }
```

因此，访问 `/token` 请求还需要加上用于第三方认证的 `header`：

```text
Authorization: Basic d2NoOndjaA==
```

`Basic` 后的编码为配置的 `clien_id:client_secret` 的 `Base64` 编码。

`DefaultTokenServices#createAccessToken` 方法为生成访问令牌的逻辑，其传入参数为不同授权方式生成的 `OAuth2Authentication` 认证对象。授权码模式获是根据授权码获取存储在 `authorizationCodeStore` 中的认证对象的。随后在 `DefaultTokenServices#createAccessToken` 方法中创建真正的访问令牌，并以 `Json` 的形式返回：

```json
{
  "access_token": "9d77b23b-b397-4e73-bf01-291ef474862f",
  "token_type": "bearer",
  "refresh_token": "c38cea17-28c8-43af-ab11-fcacc90c6ef7",
  "expires_in": 43199,
  "scope": "all"
}
```

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#隐式授权)隐式授权

隐式授权相对于授权码模式省略了使用授权码换取访问令牌的过程，直接通过浏览器访问 `/authroize` 即可获得访问令牌：

```text
http://localhost:8080/oauth/authorize?response_type=token&client_id=wch&state=xyz&scope=all&redirect_uri=http://localhost:8080
```

传参为 `OAuth2` 标准获取访问令牌的标准参数：

- `response_type`：为固定值 `token`。
- `client_id`：第三方应用认证账号。
- `state`：用于保持请求和回调的状态，会照原样回传此参数。
- `scope`：申请 `scope` 权限。
- `redirect_uri` ：需传入获取授权码使用的跳转路径用于验证。

在用户认证成功后，路径参数中会包含访问令牌：

```text
http://localhost:8080/#access_token=9d77b23b-b397-4e73-bf01-291ef474862f&token_type=bearer&state=xyz&expires_in=40375
```

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#密码授权)密码授权

密码授权要求将资源所有者的认证信息提交给 `/token` 接口，访问令牌在认证成功后以 `Json` 的形式颁发给第三方客户端，请求参数如下：

```text
POST http://localhost:8080/oauth/token?grant_type=password&username=user&password=123&scope=all
```

密码模式同样需要加上第三方应用认证信息的 `header`。

传参为 `OAuth2` 标准获取访问令牌的标准参数：

- `grant_type`：为固定值 `password`。
- `username`：资源所有者认证账号。
- `password`：资源所有者认证密码。
- `scope`：申请 `scope` 权限。

密码授权模式获取 `OAuth2` 认证信息的逻辑在 `ResourceOwnerPasswordTokenGranter#getOAuth2Authentication` 方法中：

```java
  // 从请求参数中读取账户密码
  String username = parameters.get("username");
  String password = parameters.get("password");
  // ...

  Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password);
  // ...
  try {
    // 对用户信息进行认证
    userAuth = authenticationManager.authenticate(userAuth);
  }
  // ...
```

可以看到用于认证用户信息的是一个 `AuthenticationManager` 实例。在第一章中曾说道，`Spring Security` 会配置 `AuthenticationConfiguration` 到 `Spring` 容器中，此配置会加载 `UserDetailsService` ，并最终组装成为 `AuthenticationManager`。而 `OAuth2AuthorizationServerConfiguration` 在构造函数中将 `AuthenticationConfiguration` 注入，并将生成的认证管理器加载到授权端点配置中。在配置默认的 `tokenGranter` 时，传入认证管理器。比较特殊的是之前介绍的认证过程都是在过滤器链中完成的，而密码模式则在生成访问令牌的过程中对用户身份进行认证。

```java
  private List<TokenGranter> getDefaultTokenGranters() {
    // ...

    if (authenticationManager != null) {
      // 传入用户认证管理器，密码模式有了校验用户身份的能力
      tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientDetails, requestFactory));
    }
    return tokenGranters;
  }
```

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#第三方应用凭证授权)第三方应用凭证授权

第三方应用凭证授权仅需要验证第三方应用自身的信息，访问 `/token` 接口的参数如下：

```text
POST http://localhost:8080/oauth/token?grant_type=client_credentials&scope=all
```

传参为 `OAuth2` 标准获取访问令牌的标准参数：

- `grant_type`：为固定值 `client_credentials`。
- `scope`：申请 `scope` 权限。

第三方应用凭证授权同样需要加上第三方应用认证信息的 `header`。此种授权方式是无法区分不同用户权限的。

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#刷新令牌)刷新令牌

出于安全方面的考虑，用于获取资源的访问令牌设置的失效时间比较短，在授权服务器颁发访问令牌的同时，还会携带一个 `refresh_token`。当用户使用过期的访问令牌访问资源服务器，会收到一个 `401` 响应，随后第三方应用可以携带被颁发的刷新令牌重新向授权服务器申请访问令牌，其流程如下：

```text
+--------+                                           +---------------+
|        |--(A)------- Authorization Grant --------->|               |
|        |                                           |               |
|        |<-(B)----------- Access Token -------------|               |
|        |               & Refresh Token             |               |
|        |                                           |               |
|        |                            +----------+   |               |
|        |--(C)---- Access Token ---->|          |   |               |
|        |                            |          |   |               |
|        |<-(D)- Protected Resource --| Resource |   | Authorization |
| Client |                            |  Server  |   |     Server    |
|        |--(E)---- Access Token ---->|          |   |               |
|        |                            |          |   |               |
|        |<-(F)- Invalid Token Error -|          |   |               |
|        |                            +----------+   |               |
|        |                                           |               |
|        |--(G)----------- Refresh Token ----------->|               |
|        |                                           |               |
|        |<-(H)----------- Access Token -------------|               |
+--------+           & Optional Refresh Token        +---------------+
```

上图所示流程包含以下步骤：

- （A）第三方应用向授权服务器出示授权许可。
- （B）授权服务器验证第三方应用身份和授权许可，颁发访问令牌和刷新令牌。
- （C）第三方应用持有访问令牌向资源服务器请求受保护资源。
- （D）资源服务器验证访问令牌并返回受保护资源。
- （E）第三方应用重复（C）请求，直到访问令牌过期。
- （F）由于访问令牌已经过期，资源服务器返回无效令牌错误。
- （G）第三方应用向授权服务器出示刷新令牌。
- （H）授权服务器验证用户身份后颁发一个新的访问令牌。

在 `Spring Security` 的实现中，`TokenStore` 中维护了 `refresh_token` 与用户认证信息的映射，当授权服务器收到类型为 `refresh_token` 的授权请求时，会取出对应的用户认证信息，重新使用用户认证管理器进行认证。如果认证成功则会颁发新的访问令牌和刷新令牌。如果刷新令牌也是过期的，同样会刷新失败，`DefaultTokenServices#refreshAccessToken` 方法对刷新令牌的实现逻辑如下：

```java
  @Transactional(noRollbackFor={InvalidTokenException.class, InvalidGrantException.class})
  public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest) throws AuthenticationException {
    // ...

    // 查询存储的 refresh_token
    OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(refreshTokenValue);
    // ..
    // 获取刷新令牌对应的用户认证信息
    OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(refreshToken);
    if (this.authenticationManager != null && !authentication.isClientOnly()) {
      // 进行用户认证
      Authentication user = new PreAuthenticatedAuthenticationToken(authentication.getUserAuthentication(), "", authentication.getAuthorities());
      user = authenticationManager.authenticate(user);
      Object details = authentication.getDetails();
      authentication = new OAuth2Authentication(authentication.getOAuth2Request(), user);
      authentication.setDetails(details);
    }
    // ...

    if (isExpired(refreshToken)) {
      // 如果刷新令牌过期，授权失败
      tokenStore.removeRefreshToken(refreshToken);
      throw new InvalidTokenException("Invalid refresh token (expired): " + refreshToken);
    }
    // 重新创建用户认证信息
    authentication = createRefreshedAuthentication(authentication, tokenRequest);

    if (!reuseRefreshToken) {
      // 是否重用 refresh_token
      tokenStore.removeRefreshToken(refreshToken);
      refreshToken = createRefreshToken(authentication);
    }

    // 重新创建访问令牌
    OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
    tokenStore.storeAccessToken(accessToken, authentication);
    if (!reuseRefreshToken) {
      tokenStore.storeRefreshToken(accessToken.getRefreshToken(), authentication);
    }
    return accessToken;
  }
```

对用户重新认证的逻辑需要传入用户认证管理器 `authenticationManager`，通过自定义授权端点时，通过 `AuthorizationServerEndpointsConfiguration#userDetailsService` 方法配置用户认证服务。

刷新令牌访问 `/token` 接口的参数如下：

```text
POST http://localhost:8080/oauth/token?grant_type=refresh_token&refresh_token=95ba5fd8-1b9e-4142-81fa-b41cdc27a769&scope=all
```

传参为 `OAuth2` 标准获取访问令牌的标准参数：

- `grant_type`：为固定值 `refresh_token`。
- `refresh_token`：刷新令牌值。
- `refresh_token`：申请的 `scope` 权限。

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#访问资源服务器)访问资源服务器

启动资源服务器需要配置 `@EnableResourceServer` 注解，此注解引入了 `ResourceServerConfiguration`，这是一个 `WebSecurityConfigurerAdapter` 实例，其重载了配置过滤器的配置方法：

```java
  protected void configure(HttpSecurity http) throws Exception {
    // 资源服务器保护配置
    ResourceServerSecurityConfigurer resources = new ResourceServerSecurityConfigurer();
    ResourceServerTokenServices services = resolveTokenServices();
    if (services != null) {
      resources.tokenServices(services);
    } else {
      if (tokenStore != null) {
        resources.tokenStore(tokenStore);
      } else if (endpoints != null) {
        // Spring 容器中有授权端点实例，说明资源服务器和授权服务器在同一实例中，则使用相同的 TokenStore
        resources.tokenStore(endpoints.getEndpointsConfigurer().getTokenStore());
      }
    }
    if (eventPublisher != null) {
      resources.eventPublisher(eventPublisher);
    }
    for (ResourceServerConfigurer configurer : configurers) {
      // 自定义资源保护配置
      configurer.configure(resources);
    }
    http.authenticationProvider(new AnonymousAuthenticationProvider("default"))
      .exceptionHandling()
      .accessDeniedHandler(resources.getAccessDeniedHandler()).and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
      .csrf().disable();
    http.apply(resources);
    if (endpoints != null) {
      // 授权端点中的路径排除在外，不受资源服务器保护
      http.requestMatcher(new NotOAuthRequestMatcher(endpoints.oauth2EndpointHandlerMapping()));
    }
    for (ResourceServerConfigurer configurer : configurers) {
      // 自定义过滤器配置
      configurer.configure(http);
    }
    if (configurers.isEmpty()) {
      // 默认在没有自定义配置的情况下访问所有路径都需要权限
      http.authorizeRequests().anyRequest().authenticated();
    }
  }
```

此配置中引人了 `ResourceServerSecurityConfigurer` 用于配置 `OAuth2` 资源服务器认证过滤器 `OAuth2AuthenticationProcessingFilter`，其过滤逻辑如下：

```java
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    // ...

    try {
      // 读取 Authorization 头，取出 Bearer 的值，即访问令牌
      Authentication authentication = tokenExtractor.extract(request);
      if (authentication == null) {
        // ...
      } else {
        // ...

        // 认证逻辑为验证 TokenStore 中的访问令牌
        Authentication authResult = authenticationManager.authenticate(authentication);
        // ...

        // 认证信息存入 SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authResult);

      }
    }

    // ...

    chain.doFilter(request, response);
  }
```

通过实现 `ResourceServerConfigurer` 接口并注入 `Spring` 容器，可以实现对资源服务器保护服务的干预。 `OAuth2AutoConfiguration` 中引入了 `OAuth2ResourceServerConfiguration` 配置，是对 `ResourceServerConfigurer` 的默认实现，其配置了所有请求都需要经过认证。

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（四）：OAth2实现.html#小结)小结

- `OAuth2` 是一个通用的 `web` 安全标准，`Spring Security` 对 `OAuth2` 有一系列开箱即用的实现。
- `OAuth2` 标准提供了 4 种授权（颁发访问令牌）和 1 种刷新令牌的方式。
- 资源服务器通过解析 `Authorization` 头获取访问令牌，对访问令牌进行验证判断是否运行访问受保护资源。

# Spring Security 源码分析（五）：JWT 实现

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（五）：JWT实现.html#jwt)JWT

`JWT(Json Web Token)` 是一个开放[标准](https://tools.ietf.org/html/rfc7519)，它定义了一种紧凑和自包含的方式，用于在各方之间作为 `JSON` 对象安全地传输信息。

- 紧凑：`token` 值是一个很小的 `Base64` 编码的字符串，可以通过 `http` 请求参数或者 `header` 传递。
- 自包含：`token` 可以包含很多信息，包括用户名、权限、过期时间等，支持开发者自定义。

通常一个 `JWT` 字符串的[解析结果](https://jwt.io/)如下：

![JWT编码解码](https://wch853.github.io/img/security/JWT%E7%BC%96%E7%A0%81%E8%A7%A3%E7%A0%81.png)

`JWT` 串由 3 部分组成：

- `header`：头部，用于标识 `token` 为 `JWT` 类型和使用的签名算法。
- `payload`：有效数据，`JWT` 自包含的信息。
- `signature`：对头部和有效信息的签名。

因此 `JWT` 能够安全地传输安全信息。

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（五）：JWT实现.html#使用-jwt-替换默认-token-实现)使用 JWT 替换默认 token 实现

`Spring Security` 提供了诸多的 `TokenStore` 实现，如存在内存中的 `InMemoryTokenStore` 、存在数据库中的 `JdbcTokenStore`、存在 `Redis` 中的 `RedisTokenStore`，这些都是通过将生成的 `token` 存储下来，当第三方应用请求受保护资源部时，会去 `TokenStore` 查询是否有相应的令牌。仅将令牌存储在内存中不支持分布式环境；存储在数据库或 `Redis` 中，每次请求都去查询又会增加后端的负担；一旦服务器宕机，势必又要影响用户访问。而 `JWT` 对于令牌的实现由于自包含的特性，能有效解决上述问题。

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（五）：JWT实现.html#jwttokenstore)JwtTokenStore

无论是 4 种授权方式的哪一种，在授权认证完成后，都是通过在 `AbstractTokenGranter` 中调用 `AuthorizationServerTokenServices#createAccessToken` 方法颁发令牌的，`JWT` 由于其自包含的特性，是不会存储在后端应用中的，因此每次都需要申请授权都会直接创建新的令牌。普通令牌中只有 `scope`、`refresh_token` 等基本信息，`JWT` 如何实现其自包含特性呢？在创建令牌时，`DefaultTokenServices#createAccessToken` 方法使用了 `TokenEnhancer` 向 `JWT` 中添加附加信息：

```java
	private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, OAuth2RefreshToken refreshToken) {
		// ...
		return accessTokenEnhancer != null ? accessTokenEnhancer.enhance(token, authentication) : token;
	}
```

因此需要配置 `JwtAccessTokenConverter` 来增强 `JWT` 的构成。

### [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（五）：JWT实现.html#jwtaccesstokenconverter)JwtAccessTokenConverter

在授权端点配置 `AuthorizationServerEndpointsConfigurer` 中，我们可以配置 `JwtAccessTokenConverter` ：

```java
  public AuthorizationServerEndpointsConfigurer accessTokenConverter(AccessTokenConverter accessTokenConverter) {
    // 配置 JwtAccessTokenConverter
    this.accessTokenConverter = accessTokenConverter;
    return this;
  }
  private TokenEnhancer tokenEnhancer() {
  	if (this.tokenEnhancer == null && accessTokenConverter() instanceof JwtAccessTokenConverter) {
      // JwtAccessTokenConverter 也实现了 TokenEnhancer 接口
  		tokenEnhancer = (TokenEnhancer) accessTokenConverter;
  	}
  	return this.tokenEnhancer;
  }

  private TokenStore tokenStore() {
    if (tokenStore == null) {
      if (accessTokenConverter() instanceof JwtAccessTokenConverter) {
        // 如果配置了 JwtAccessTokenConverter，那么配置 JwtTokenStore
        this.tokenStore = new JwtTokenStore((JwtAccessTokenConverter) accessTokenConverter());
      } else {
      	this.tokenStore = new InMemoryTokenStore();
    	}
    }
  	return this.tokenStore;
  }
```

一旦设置了 `JwtAccessTokenConverter` 就可以默认配置 `tokenEnhancer`，并将 `tokenStore` 设置为 `JwtTokenStore`。`JwtAccessTokenConverter#enhance` 方法中对于增加 `JWT` 附加信息的逻辑如下：

```java
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
    DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);
    Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());
    String tokenId = result.getValue();
    if (!info.containsKey(TOKEN_ID)) {
      // 增加 jti，即授权服务器生成的原始访问令牌字符串
      info.put(TOKEN_ID, tokenId);
    } else {
      tokenId = (String) info.get(TOKEN_ID);
    }
    result.setAdditionalInformation(info);
    // 按照 JWT 生成算法拼装 JWT
    result.setValue(encode(result, authentication));
    OAuth2RefreshToken refreshToken = result.getRefreshToken();
    if (refreshToken != null) {
      // 拼接刷新令牌的 JWT
      // ...
    }
    return result;
  }
```

在对 `payload` 部分编码时调用了 `DefaultAccessTokenConverter#convertAccessToken` 方法：

```java
  public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
    // ...
		// 增加用户名及其权限信息
    if (!authentication.isClientOnly()) {
      response.putAll(userTokenConverter.convertUserAuthentication(authentication.getUserAuthentication()));
    } else {
      if (clientToken.getAuthorities()!=null && !clientToken.getAuthorities().isEmpty()) {
        response.put(UserAuthenticationConverter.AUTHORITIES,
                     AuthorityUtils.authorityListToSet(clientToken.getAuthorities()));
      }
    }
		// 增加 scope 信息
    if (token.getScope()!=null) {
      response.put(scopeAttribute, token.getScope());
    }
    // 增加原始访问令牌
    if (token.getAdditionalInformation().containsKey(JTI)) {
      response.put(JTI, token.getAdditionalInformation().get(JTI));
    }
		// 增加令牌过期时间
    if (token.getExpiration() != null) {
      response.put(EXP, token.getExpiration().getTime() / 1000);
    }
    // 增加授权类型
    if (includeGrantType && authentication.getOAuth2Request().getGrantType()!=null) {
      response.put(GRANT_TYPE, authentication.getOAuth2Request().getGrantType());
    }
		// 增加其他附加信息
    response.putAll(token.getAdditionalInformation());
		// ...
    return response;
  }
```

`JWT` 的加密、解密是通过 `Spring Security` 提供的工具 `JwtHelper` 实现的，开发者可以自定义秘钥。

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（五）：JWT实现.html#tokenkeyendpoint)TokenKeyEndpoint

关于 `JWT`，`Spring Security` 还留有一个彩蛋：在配置授权端点时，引入了 `TokenKeyEndpointRegistrar` 配置，当 `Spring` 容器中有 `JwtAccessTokenConverter` 实例时会注册 `TokenKeyEndpoint`，此配置提供了 `/oauth/token_key` 接口用于查询生成 `JWT` 签名的算法以及用于验证的密钥。

`/oauth/token_key` 接口默认拒绝任何访问请求，通过授权服务器安全配置扩展设置接口的访问权限：

```java
@Override
public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
  security.tokenKeyAccess("authenticated");
}
```

## [#](https://wch853.github.io/posts/security/SpringSecurity源码分析（五）：JWT实现.html#小结)小结

- `JWT` 是一种紧凑和自包含的 `token` 实现方式，其有效数据包含了用户的认证信息，并通过加密签名来保证安全性。
- `JWT` 可以存储在客户端，由于其无状态特性，天然支持分布式。由于自包含有效数据，避免了每次访问资源服务器都需要查询后端数据。
- 在 `Spring Security` 中通过配置 `JwtAccessTokenConverter` 来使用 `JWT`。开发者可以干预 `JWT` 中的附加信息和加密方式等。