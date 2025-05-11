package koo.ldap.ldaputil.webpasswd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;

@TestConfiguration
public class LdapTestConfiguration {

    // ContextSource is creted by LdapAutoConfiguration
    //
    @Bean
    public AuthenticationManager authenticationManager(BaseLdapPathContextSource contextSource) {
        LdapBindAuthenticationManagerFactory factory =
                new LdapBindAuthenticationManagerFactory(contextSource);
        factory.setUserSearchFilter("(uid={0})");
        factory.setUserSearchBase("ou=people");
        return factory.createAuthenticationManager();
    }

    @Bean
    public LdapUserDetailsManager ldapUserDetailsManager(ContextSource cs) {
        return new LdapUserDetailsManager(cs);
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.ldapAuthentication()
                .userSearchBase("dc=example,dc=com")
                .userDnPatterns("cn={0},ou=people,dc=example,dc=com")
                .groupSearchBase("ou=groups")
                .groupRoleAttribute("cn")
                // .groupSearchSubtree(true)
                .contextSource()
                    .url("ldap://localhost:20389")
                    .managerDn("uid=admin,ou=system")
                    .managerPassword("secret");
    }

}
