package com.rockyapp.rockyappbackend.permissions.controller;

import com.rockyapp.rockyappbackend.common.AbstractControllerTest;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.permissions.dto.PermissionDTO;
import com.rockyapp.rockyappbackend.permissions.dto.SimplePermissionDTO;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.permissions.mapper.SimplePermissionMapper;
import com.rockyapp.rockyappbackend.permissions.service.PermissionService;
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

public class PermissionControllerTest extends AbstractControllerTest {
    private static final String ENDPOINT_URL = "/api/v1/permission";
    @InjectMocks
    private PermissionController permissionController;

    @Mock
    private PermissionService permissionService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(permissionController)
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
    public void findAllByPage() throws Exception {
        Page<Permission> page = new PageImpl<>(Collections.singletonList(PermissionBuilder.getEntity()));

        SimplePermissionMapper mapper = new SimplePermissionMapper();

        ResultPagine<SimplePermissionDTO> resultPagine = mapper.mapFromEntity(page);

        Mockito.when(permissionService.search(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(resultPagine);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/search")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].name").value("toto"));

        Mockito.verify(permissionService, Mockito.times(1)).search(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(permissionService);
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void getById() throws Exception {
        Mockito.when(permissionService.findById(ArgumentMatchers.anyLong())).thenReturn(PermissionBuilder.getDto());

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(1)));
        Mockito.verify(permissionService, Mockito.times(1)).findById(1L);
        Mockito.verifyNoMoreInteractions(permissionService);
    }

    @Test
    public void save() throws Exception {
        Mockito.doNothing().when(permissionService).create(ArgumentMatchers.any(PermissionDTO.class));

        mockMvc.perform(
                        MockMvcRequestBuilders.post(ENDPOINT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(PermissionBuilder.getDto())))
                .andExpect(status().isOk());
        Mockito.verify(permissionService, Mockito.times(1)).create(ArgumentMatchers.any(PermissionDTO.class));
        Mockito.verifyNoMoreInteractions(permissionService);
    }

    @Test
    public void update() throws Exception {
        Mockito.doNothing().when(permissionService).update(ArgumentMatchers.anyLong(), ArgumentMatchers.any());

        mockMvc.perform(
                        MockMvcRequestBuilders.put(ENDPOINT_URL + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(PermissionBuilder.getDto())))
                .andExpect(status().isOk());
        Mockito.verify(permissionService, Mockito.times(1)).update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(PermissionDTO.class));
        Mockito.verifyNoMoreInteractions(permissionService);
    }

    @Test
    public void delete() throws Exception {
        Mockito.doNothing().when(permissionService).delete(ArgumentMatchers.anyLong());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(ENDPOINT_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        Mockito.verify(permissionService, Mockito.times(1)).delete(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(permissionService);
    }
}