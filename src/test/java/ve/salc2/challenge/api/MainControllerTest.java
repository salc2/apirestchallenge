package ve.salc2.challenge.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import ve.salc2.challenge.NtwentysixApplication;
import ve.salc2.challenge.domain.Transaction;
import ve.salc2.challenge.domain.TransactionRepository;
import ve.salc2.challenge.domain.TransactionService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NtwentysixApplication.class)
@WebAppConfiguration
public class MainControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private TransactionService transactionService;

    @Before
    public void setUp() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        repository.deleteAll();
    }

    @Test
    public void createTransaction() throws Exception {
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Transaction(800.0, 1489888641573L))))
                .andExpect(status().isCreated());
    }

    @Test
    public void createTransactionsEachSecond() throws Exception {

        long initTime = System.currentTimeMillis();
        long sixtySecBefore = initTime - (60 * 1000);

        List<Transaction> tenTransactions =
                LongStream.rangeClosed(sixtySecBefore, initTime)
                        .boxed()
                        .filter(t -> t % 1000 == 0)
                        .map(t -> new Transaction(100.0, t))
                        .collect(Collectors.toList());

        tenTransactions.forEach(transactionService::ingest);
        ResultActions result = mockMvc.perform(get("/statistics"));

        long timeFirstTransactions = tenTransactions.get(0).getTimestamp();
        Long endTime = (System.currentTimeMillis() - timeFirstTransactions) / 1000;

        if (endTime > 59) {
            int secondLess = endTime.intValue() - 59;
            result.andExpect(jsonPath("$.sum", is((60 - secondLess) * 100.0)))
                    .andExpect(jsonPath("$.count", is(60 - secondLess)));
        } else {
            result.andExpect(jsonPath("$.sum", is(6000.0)))
                    .andExpect(jsonPath("$.count", is(60)));
        }

    }

}
