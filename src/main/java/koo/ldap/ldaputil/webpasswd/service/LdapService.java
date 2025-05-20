package koo.ldap.ldaputil.webpasswd.service;

import static org.springframework.ldap.query.LdapQueryBuilder.query;
import java.util.List;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class LdapService {

    Logger logger = LoggerFactory.getLogger(LdapService.class);

    @Autowired
    private LdapClient ldapClient;

    @Autowired
    private LdapUserDetailsManager ldapUserDetailsManager;

    

    // @Autowired
    // private LdapUserDetailsManager ldapUserDetailsManager;

    public boolean checkAuth(String user, String password) {
        try {
            ldapClient.authenticate().query(query().base("ou=people")
            .where("objectclass").is("person").and("uid").is(user)).password(password).execute();
            return true;
        } catch (Exception e) {
            logger.info("login failed. user = " + user, e);
            return false;
        }
    }

    public List<Attributes>  searchUser(String user) {
        List<Attributes> result = ldapClient.search().query(query().base("ou=people")
        .where("objectclass").is("person")).toList((Attributes attrs) ->  attrs);

        return result;
    }

    public String convert(Attributes attrs) {
        Attribute attr = attrs.get("uid");
        return attr == null ? "" :  attr.toString();
    }

    public boolean updatePassword(String oldPassword, String newPassword) {
        
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UserDetails userDetails = User.withUsername(username).password(newPassword).build();
            
            ldapUserDetailsManager.changePassword(oldPassword, newPassword);
        return false;
    }

}
