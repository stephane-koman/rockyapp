package com.rockyapp.rockyappbackend.customers.controller;

import com.rockyapp.rockyappbackend.common.AbstractControllerTest;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.customers.builder.CustomerBuilder;
import com.rockyapp.rockyappbackend.customers.dto.CustomerDTO;
import com.rockyapp.rockyappbackend.customers.entity.Customer;
import com.rockyapp.rockyappbackend.customers.mapper.CustomerMapper;
import com.rockyapp.rockyappbackend.customers.service.CustomerService;
import com.rockyapp.rockyappbackend.utils.CustomUtils;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;
import java.util.Locale;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest extends AbstractControllerTest {
    
    private static final String ENDPOINT_URL = "/api/v1/customer";

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(new ViewResolver() {
                    @Override
                    public View resolveViewName(String viewName, Locale locale) throws Exception {
                        return new MappingJackson2JsonView();
                    }
                })
                .build();
    }

    @Test
    public void searchCustomersByPageAndCriteria_shouldReturn_resultPagineOfCustomerDTO() throws Exception {
        Page<Customer> page = new PageImpl<>(Collections.singletonList(CustomerBuilder.getEntity()));

        CustomerMapper mapper = new CustomerMapper();

        ResultPagine<CustomerDTO> resultPagine = mapper.mapFromEntity(page);

        Mockito.when(customerService.search(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(resultPagine);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/search")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].name").value("ADMIN"));

        Mockito.verify(customerService, Mockito.times(1)).search(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(customerService);
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findCustomerById_shouldReturn_customerDTO() throws Exception {
        Mockito.when(customerService.findById(ArgumentMatchers.anyString())).thenReturn(CustomerBuilder.getDto());

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is("1")));

        Mockito.verify(customerService, Mockito.times(1)).findById("1");
        Mockito.verifyNoMoreInteractions(customerService);
    }

    @Test
    public void createCustomer_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(customerService).create(ArgumentMatchers.any(CustomerDTO.class));

        mockMvc.perform(
                        MockMvcRequestBuilders.post(ENDPOINT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(CustomerBuilder.getDto())))
                .andExpect(status().isOk());
        Mockito.verify(customerService, Mockito.times(1)).create(ArgumentMatchers.any(CustomerDTO.class));
        Mockito.verifyNoMoreInteractions(customerService);
    }

    @Test
    public void updateCustomer_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(customerService).update(ArgumentMatchers.anyString(), ArgumentMatchers.any());

        mockMvc.perform(
                        MockMvcRequestBuilders.put(ENDPOINT_URL + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(CustomerBuilder.getDto())))
                .andExpect(status().isOk());
        Mockito.verify(customerService, Mockito.times(1)).update(ArgumentMatchers.anyString(), ArgumentMatchers.any(CustomerDTO.class));
        Mockito.verifyNoMoreInteractions(customerService);
    }

    @Test
    public void deleteCustomer_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(customerService).delete(ArgumentMatchers.anyString());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(ENDPOINT_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        Mockito.verify(customerService, Mockito.times(1)).delete(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(customerService);
    }
}