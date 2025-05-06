package koo.ldap.ldaputil.webpasswd.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapClient;

@Configuration
public class LdapConfiguration {
    @Bean
    public LdapClient createLdapClient(ContextSource cs) {
        return LdapClient.create(cs);
    }
    // ContextSource is creted by LdapAutoConfiguration
    // 
}
