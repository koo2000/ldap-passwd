package koo.ldap.ldaputil.webpasswd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import javax.naming.directory.Attributes;
import org.apache.directory.server.annotations.CreateLdapServer;
import org.apache.directory.server.annotations.CreateTransport;
import org.apache.directory.server.core.annotations.ApplyLdifFiles;
import org.apache.directory.server.core.annotations.CreateDS;
import org.apache.directory.server.core.annotations.CreatePartition;
import org.apache.directory.server.core.integ.AbstractLdapTestUnit;
import org.apache.directory.server.core.integ.ApacheDSTestExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "ldap-test.properties")
@ExtendWith({ApacheDSTestExtension.class})
@CreateDS(name = "myds",
        partitions = {@CreatePartition(name = "example", suffix = "dc=example,dc=org")})
@CreateLdapServer(
        transports = {@CreateTransport(protocol = "LDAP", address = "localhost", port = 20050)})
@ApplyLdifFiles({"example.ldif"})
public class LdapServiceTest extends AbstractLdapTestUnit {

    @Autowired
    private LdapService target;

    @BeforeEach
    void init() throws Exception {

    }

    @Test
    void testCheckAuth() throws Exception {

        assertTrue(target.checkAuth("sample001", "password"));
        assertFalse(target.checkAuth("sample001", "passwd"));

        assertTrue(target.checkAuth("sample002", "password"));
        assertFalse(target.checkAuth("sample002", "passwd"));
    }

    @Test
    void testSearch() {
        List<Attributes> result = target.searchUser("");

        System.out.println(result.toString());
        assertEquals(2, result.size());
    }
}
