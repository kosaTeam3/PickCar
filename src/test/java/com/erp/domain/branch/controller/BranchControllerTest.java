package com.erp.domain.branch.controller;

import com.erp.domain.branch.dto.request.CreateBranch;
import com.erp.domain.branch.dto.request.UpdateBranch;
import com.erp.domain.branch.service.BranchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BranchController.class)
class BranchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BranchService branchService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("생성 요청을 보낼 때 ")
    class PostBranch {
        @Test
        @DisplayName("성공한다.")
        @WithMockUser
        void createBranch() throws Exception {
            //given
            var dto = new CreateBranch(
                    1L,
                    "강남1지점",
                    "02-123-4567",
                    "서울특별시 강남구 테헤란로 123",
                    37.498095,
                    127.02761
            );

            //when & then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/manager/branches").with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("일부 항목이 null인 경우 실패한다")
        @WithMockUser
        void createBranchFail() throws Exception {
            //given
            var dto = new CreateBranch(
                    1L,
                    "강남1지점",
                    "02-123-4567",
                    "서울특별시 강남구 테헤란로 123",
                    null,
                    127.02761
            );

            //when & then
            mockMvc.perform(MockMvcRequestBuilders.post("/api/manager/branches").with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    @DisplayName("수정 요청을 보낼 때 성공한다.")
    @WithMockUser
    void updateBranch() throws Exception {
        //given
        var dto = new UpdateBranch(
                "변경지점명",
                null,
                "변경주소",
                20L,
                36.0,
                null
        );

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/manager/branches/{branchId}", 1L).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DisplayName("삭제 요청을 보낼 때 성공한다.")
    void deleteBranch() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/manager/branches/{branchId}", 1L).with(csrf()))
                .andExpect(status().isNoContent());
    }
}
