package koo.ldap.ldaputil.webpasswd.frontend;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapClient;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.DefaultLdapUsernameToDnMapper;
import org.springframework.security.ldap.LdapUsernameToDnMapper;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Value("${spring.websecurity.debug:false}")
    boolean webSecurityDebug;

    @Value("dc=example,dc=com")
    private String userSearchBase;

    @Value("${app.userDnPatterns:uid={0},ou=people,dc=example,dc=com}")
    private String userDnPatterns;

    @Value("${app.userDnBase:ou=people}")
    private String userDnBase;

    @Value("${app.usernameAttribute:uid}")
    private String usernameAttribute;

    @Value("${app.userRoleAttribute:cn}")
    private String userRoleAttribute;

    @Value("${app.groupSearchBase:ou=groups,dc=example,dc=com}")
    private String groupSearchBase;

    @Value("${app.ldapUrl:ldap://localhost:20389}")
    private String ldapUrl;

    @Value("${app.ldapManagerDn:uid=admin,ou=system}")
    private String ldapManagerDn;

    @Value("${app.ldapManagerPassword:secret}")
    private String ldapManagerPassword;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(webSecurityDebug);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers(new AntPathRequestMatcher("/")).authenticated()
                .requestMatchers(new AntPathRequestMatcher("/changePassword")).authenticated()
                .requestMatchers(new AntPathRequestMatcher("/error")).authenticated()
                .anyRequest().permitAll())
                .formLogin(Customizer.withDefaults());


        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .userSearchBase(userSearchBase)
                .userDnPatterns(userDnPatterns)
                .groupSearchBase(groupSearchBase)
                .groupRoleAttribute(userRoleAttribute)
                .passwordEncoder(creatEncoder())
                .contextSource()
                    .url(ldapUrl)
                    .managerDn(ldapManagerDn)
                    .managerPassword(ldapManagerPassword);
    }

    private PasswordEncoder creatEncoder() {
        PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String migrated = "BCRYPT";
        Map<String, PasswordEncoder> encoderMap = Map.of(
            "SSHA", new LdapShaPasswordEncoderWrappper(new LdapShaPasswordEncoder()),
            "BCRYPT", new BCryptPasswordEncoder());
        
        return new DelegatingPasswordEncoder(migrated, encoderMap);
    }

    @Bean
    public LdapClient createLdapClient(ContextSource cs) {
        return LdapClient.create(cs);
    }

    @Bean
    public LdapUserDetailsManager ldapUserDetailsManager(ContextSource cs) {
        LdapUserDetailsManager ldapUserDetailsManager = new LdapUserDetailsManager(cs);

        
        LdapUsernameToDnMapper uMpper = new DefaultLdapUsernameToDnMapper(userDnBase, usernameAttribute);
        ldapUserDetailsManager.setUsernameMapper(uMpper);

        return ldapUserDetailsManager;

    }

    private class LdapShaPasswordEncoderWrappper implements PasswordEncoder {
        private PasswordEncoder delegate;
        public LdapShaPasswordEncoderWrappper(LdapShaPasswordEncoder encoder) {
            this.delegate = encoder;
        }

        @Override
        public String encode(CharSequence rawPassword) {
            return delegate.encode(rawPassword);
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return delegate.matches(rawPassword, "{SSHA}" + encodedPassword);
        }
    }
}