package com.secutix.museum.tickets;
import com.secutix.museum.AbstractConfigTest;
import com.secutix.rule.RetryExtension;
import org.junit.jupiter.api.Test;

import com.secutix.util.Configuration;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(RetryExtension.class)
public class Import_Contr_TicketTest extends AbstractConfigTest {
    @Test
    public void testPostEndpoint() {
        System.out.println("This is a test");
    }

}
