package koo.ldap.ldaputil.webpasswd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import javax.naming.directory.Attributes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest()
@ContextConfiguration(classes={LdapTestConfiguration.class})
@TestPropertySource(locations = "ldap-test.properties")
public class LdapServiceTest {

    @Autowired
    private LdapService target;

    @Test
    public void testCheckAuth() throws Exception {

        assertTrue(target.checkAuth("sample001", "password"));
        assertFalse(target.checkAuth("sample001", "passwd"));

        assertTrue(target.checkAuth("sample002", "password"));
        assertFalse(target.checkAuth("sample002", "passwd"));
    }

    @Test
    public void testSearch() {
        List<Attributes> result = target.searchUser("");

        System.out.println(result.toString());
        assertEquals(2, result.size());
    }

    public LdapService getTarget() {
        return target;
    }

    public void setTarget(LdapService target) {
        this.target = target;
    }

    // @Test
    // public void testUpdatePassword() {
    //     target.updatePassword(null, null);
    // }
}
