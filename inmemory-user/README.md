# Spring Security With InMemory User

For Basic setup of Spring security, Please check the previous project.<br>
[Basic Authentication](https://github.com/tkdtanu/spring-security/tree/master/basic-auth)

Approaches
----------
There are 2 ways we can create users on InMemory.<br>
I have added both the ways, but commented on of the approach.<br>

Check [SecurityConfig file](/inmemory-user/src/main/java/com/tkd/springsecurity/inmemoryuser/config/SecurityConfig.java) for full implementation.

1. Override `configure(AuthenticationManagerBuilder auth)` and use `auth` to build the Users.
```
protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}password").roles("USER")
                .and()
                .withUser("admin").password("{noop}admin").roles("ADMIN");
    }
```
Here we are creating 2 Users, one normal user and another ADMIN user.
Here `USER` and `AMDIN` are Roles. Which can be used for restricting APIs based on Roles.


2. Create a Bean of `UserDetailsService`.
```
@Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                if (Objects.equals(s, "user")) {
                    return User.withUsername("user").password("{noop}password").roles("USER").build();
                } else {
                    return User.withUsername("admin").password("{noop}admin").roles("ADMIN").build();
                }
            }

        };
    }
```

Restrict APIs Based on Role
---------------------------
On this project we have 2 URls.
1. http://localhost:7002/admin/welcome
2. http://localhost:7002/user/welcome

1st Endpoint is restricted only for `ADMIN` Roles.<br>
While 2nd Endpoint is accessible for both `USER` and `ADMIN` Role.

Process to restrict the URLs By Roles.
------------------------------------------
There are multiple ways to restrict the Urls by specific Role.
1. On SecurityConfig class by overriding `configure(HttpSecurity http)`.
```
@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                .and().formLogin()
                .and().logout();
    }
``` 

`.antMatchers("/admin/**").hasAnyRole("ADMIN")` This tells that if any URL matches with `/admin/**` then user who is accessing should have ADMIN role.<br>
`.antMatchers("/user/**").hasAnyRole("ADMIN", "USER")` This tells that if any URL matches with `/user/**` then user who is accessing should have ADMIN or USER role.<br>


2. Using Spring's Method Security & SPEL.
i.e Annotate `@PreAuthorize("hasRole('ROLE_ADMIN')")` on a method.<br>
We will discuss this later.

Login,Logout & Session
------------
While using BasicAuth we have to pass username password on every request.<bt>
But Here we are not using Http BasicAuth for authentication.<br>

So we have to first login before accessing any URL.<br>
If we try to access any URL without login spring will either route you to login page or it will give error page.<br>
It depends on yours configuration.

Here we are levaraing LOGIN page by spring's formlogin.<br>
Check `configure(HttpSecurity http)` method.<br>
`.and().formLogin()` This tells that, we are enabling formLogin which is provided by spring.
 `.and().logout();` This tells that, we are enabling Logout also for this project.
 
 Steps to verify the Urls
 ------------------------
1.Open Browser, and try to access http://localhost:7002/admin/welcome. <br>
![Access Admin Page](/inmemory-user/admin_page_access.png)

2.You will see, you have redirected to login page.<br>
![Login Page Redirect](/inmemory-user/redirected_login_page.png)

3.Here you have to give correct user details which has ADMIN role. Then you will be redirected to Admin welcome page.<br>
 user=`admin` password=`admin`
![Admin Page Welcome](/inmemory-user/admin_page_welcome.png) <br>
 Note. --> If you provide a user which doesn't have ADMIN role then, after login you will be redirected to ERROR page with 403 status.<br>
 
4. You can now access the User Welcome also, because you are a ADMIN user. Try to access http://localhost:7002/user/welcome. <br>
![User Page Welcome](/inmemory-user/user_page_welcome.png) <br>

5.Logout. Hit http://localhost:7002/logout to do logout.<br>
![Logout](/inmemory-user/logout.png) <br>

6. If you try to access the pages now, you will again redirected to Login page (step 2).<br>


Same as above way, you can try using USER role(`user` & `password`).


Unit Testing
--------
For writing Test We need to first Do login and then Access the Welcome pages by using same `session`.<br>
**So we have to Keep the session from 1st request of `Login` and then pass it on the next requests**.<br>
Otherwise Spring will not be identified if the requests and already Authenticated or not.

Check [InMemoryUserApplicationTests.java](/src/test/java/com/tkd/springsecurity/inmemoryuser/InMemoryUserApplicationTests.java)

1. First We do Login and store the `session` to pass over next request.
```
MockHttpSession sesssion = ((MockHttpSession) mvc.perform(SecurityMockMvcRequestBuilders
                .formLogin("/login").user("user").password("password"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andReturn().getRequest().getSession());
```

2. Access Admin Welcome page
```
mvc.perform(MockMvcRequestBuilders.get("/admin/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .session(sesssion))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> mvcResult.getResponse().getContentAsString().contains("Hey!! Welcome to Admin Page"));
```

3. Access User Welcome page
```
mvc.perform(MockMvcRequestBuilders.get("/user/welcome")
                .contentType(MediaType.APPLICATION_JSON)
                .session(sesssion))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(mvcResult -> mvcResult.getResponse().getContentAsString().contains("Hey!! Welcome to User Page"));
```

If we login with `USER` Role and try to access Admin Welcome page, then we will get `403, "Forbidden"`<br>.
Check the testcase `testUserLogin_AndThen_AccessWelcomePage_For_Admin_And_User`. 
