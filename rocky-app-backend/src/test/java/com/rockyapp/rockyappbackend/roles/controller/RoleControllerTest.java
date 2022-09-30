package com.rockyapp.rockyappbackend.roles.controller;

import com.rockyapp.rockyappbackend.common.AbstractControllerTest;
import com.rockyapp.rockyappbackend.common.pagination.ResultPagine;
import com.rockyapp.rockyappbackend.roles.builder.RoleBuilder;
import com.rockyapp.rockyappbackend.roles.dto.RoleDTO;
import com.rockyapp.rockyappbackend.roles.dto.SimpleRoleDTO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.roles.mapper.SimpleRoleMapper;
import com.rockyapp.rockyappbackend.roles.service.RoleService;
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

public class RoleControllerTest extends AbstractControllerTest {
    
    private static final String ENDPOINT_URL = "/api/v1/role";

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleService roleService;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(roleController)
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
    public void searchRolesByPageAndCriteria_shouldReturn_resultPagineOfRoleDTO() throws Exception {
        Page<Role> page = new PageImpl<>(Collections.singletonList(RoleBuilder.getEntity()));

        SimpleRoleMapper mapper = new SimpleRoleMapper();

        ResultPagine<SimpleRoleDTO> resultPagine = mapper.mapFromEntity(page);

        Mockito.when(roleService.search(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(resultPagine);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/search")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.results[0].name").value("ADMIN"));

        Mockito.verify(roleService, Mockito.times(1)).search(ArgumentMatchers.any(), ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(roleService);
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void findRoleById_shouldReturn_roleDTO() throws Exception {
        Mockito.when(roleService.findById(ArgumentMatchers.anyLong())).thenReturn(RoleBuilder.getDto());

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(1)));
        Mockito.verify(roleService, Mockito.times(1)).findById(1L);
        Mockito.verifyNoMoreInteractions(roleService);
    }

    @Test
    public void createRole_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(roleService).create(ArgumentMatchers.any(RoleDTO.class));

        mockMvc.perform(
                        MockMvcRequestBuilders.post(ENDPOINT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(RoleBuilder.getDto())))
                .andExpect(status().isOk());
        Mockito.verify(roleService, Mockito.times(1)).create(ArgumentMatchers.any(RoleDTO.class));
        Mockito.verifyNoMoreInteractions(roleService);
    }

    @Test
    public void updateRole_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(roleService).update(ArgumentMatchers.anyLong(), ArgumentMatchers.any());

        mockMvc.perform(
                        MockMvcRequestBuilders.put(ENDPOINT_URL + "/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CustomUtils.asJsonString(RoleBuilder.getDto())))
                .andExpect(status().isOk());
        Mockito.verify(roleService, Mockito.times(1)).update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(RoleDTO.class));
        Mockito.verifyNoMoreInteractions(roleService);
    }

    @Test
    public void deleteRole_shouldReturn_successCode() throws Exception {
        Mockito.doNothing().when(roleService).delete(ArgumentMatchers.anyLong());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(ENDPOINT_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        Mockito.verify(roleService, Mockito.times(1)).delete(Mockito.anyLong());
        Mockito.verifyNoMoreInteractions(roleService);
    }
}