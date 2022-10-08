package com.rockyapp.rockyappbackend.products.controller;

import com.rockyapp.rockyappbackend.common.AbstractControllerTest;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.product_types.mapper.ProductTypeMapper;
import com.rockyapp.rockyappbackend.products.builder.ProductBuilder;
import com.rockyapp.rockyappbackend.products.dto.ProductCreaDTO;
import com.rockyapp.rockyappbackend.products.dto.SimpleProductDTO;
import com.rockyapp.rockyappbackend.products.entity.Product;
import com.rockyapp.rockyappbackend.products.mapper.SimpleProductMapper;
import com.rockyapp.rockyappbackend.products.service.ProductService;
import com.rockyapp.rockyappbackend.utils.CustomUtils;
import com.rockyapp.rockyappbackend.volumes.mapper.VolumeMapper;
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

public class ProductControllerTest extends AbstractControllerTest {
    
    private static final String ENDPOINT_URL = "/api/v1/product";

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private ProductTypeMapper productTypeMapper;

    @Mock
    private VolumeMapper volumeMapper;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
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
    public void searchProductsByPageAndCriteria_shouldReturn_resultPagineOfProductDTO() throws Exception {
        Page<Product> page = new PageImpl<>(Collections.singletonList(ProductBuilder.getEntity()));

        SimpleProductMapper mapper = new SimpleProductMapper(productTypeMapper, volumeMapper);

        ResultPagine<SimpleProductDTO> resultPagine = mapper.mapFromEntity(page);

        Mockito.when(productService.search(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(resultPagine);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/search")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].name").value("fraise"));

        Mockito.verify(productService, Mockito.times(1)).search(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(productService);
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findProductById_shouldReturn_productDTO() throws Exception {
        Mockito.when(productService.findById(ArgumentMatchers.anyString())).thenReturn(ProductBuilder.getDto());

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/0001"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is("0001")));

        Mockito.verify(productService, Mockito.times(1)).findById("0001");
        Mockito.verifyNoMoreInteractions(productService);
    }

    @Test
    public void createProduct_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(productService).create(ArgumentMatchers.any(ProductCreaDTO.class));

        mockMvc.perform(
                        MockMvcRequestBuilders.post(ENDPOINT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(ProductBuilder.getDto())))
                .andExpect(status().isOk());
        Mockito.verify(productService, Mockito.times(1)).create(ArgumentMatchers.any(ProductCreaDTO.class));
        Mockito.verifyNoMoreInteractions(productService);
    }

    @Test
    public void updateProduct_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(productService).update(ArgumentMatchers.anyString(), ArgumentMatchers.any(ProductCreaDTO.class));

        mockMvc.perform(
                        MockMvcRequestBuilders.put(ENDPOINT_URL + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(ProductBuilder.getDto())))
                .andExpect(status().isOk());
        Mockito.verify(productService, Mockito.times(1)).update(ArgumentMatchers.anyString(), ArgumentMatchers.any(ProductCreaDTO.class));
        Mockito.verifyNoMoreInteractions(productService);
    }

    @Test
    public void deleteProduct_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(productService).delete(ArgumentMatchers.anyString());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(ENDPOINT_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        Mockito.verify(productService, Mockito.times(1)).delete(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(productService);
    }
}