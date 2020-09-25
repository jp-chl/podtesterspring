package jpvr.com.podtesterspring.endpoints;

import jpvr.com.podtesterspring.controller.templates.HelloWorldResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EndpointsTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @org.junit.jupiter.api.Test
    public void should_validate_hello_world_endpoint_invocation_count() {

        //final String response = this.restTemplate.getForObject("http://localhost:" + port + "/", String.class);

        HelloWorldResponseTemplate response = this.restTemplate.getForObject("http://localhost:" + port + "/", HelloWorldResponseTemplate.class);
        assertThat(response.getCount(), is(1));

        response = this.restTemplate.getForObject("http://localhost:" + port + "/", HelloWorldResponseTemplate.class);
        response = this.restTemplate.getForObject("http://localhost:" + port + "/", HelloWorldResponseTemplate.class);
        assertThat(response.getCount(), is(3));
    } // end should_validate_hello_world_endpoint_invocation_count()

    @org.junit.jupiter.api.Test
    public void check_liveness_probe_endpoint() throws Exception {

        this.mockMvc
                .perform(get("/healthz/ready"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("I am alive")));
    } // end void check_liveness_probe_endpoint()

    @org.junit.jupiter.api.Test
    public void check_liveness_probe_fail_after_invoke_misbehave_endpoint() throws Exception {

        this.mockMvc.perform(get("/misbehave"));

        this.mockMvc
                .perform(get("/healthz/ready"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(containsString("I am down")));
    } // end void check_liveness_probe_fail_after_invoke_misbehave_endpoint()

    @org.junit.jupiter.api.Test
    public void check_liveness_probe_ok_after_invoke_misbehave_after_behave_endpoint() throws Exception {

        this.mockMvc.perform(get("/misbehave"));

        this.mockMvc
                .perform(get("/healthz/ready"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(containsString("I am down")));

        this.mockMvc.perform(get("/behave"));

        this.mockMvc
                .perform(get("/healthz/ready"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("I am alive")));
    } // end void check_liveness_probe_ok_after_invoke_misbehave_after_behave_endpoint()
} // end class EndpointsTest
