package com.erp.domain.branch.service;

import com.erp.domain.branch.dto.request.CreateBranch;
import com.erp.domain.branch.entity.Branch;
import com.erp.domain.branch.repository.BranchRepository;
import com.erp.domain.employee.entity.Employee;
import com.erp.domain.employee.repository.EmployeeRepository;
import com.erp.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    @Mock
    private BranchRepository branchRepository;
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private BranchService branchService;

    @Nested
    @DisplayName("Branch 생성시 ")
    class BranchCreate {

        @Test
        @DisplayName("성공한다")
        void success() {
            //given
            var dto = new CreateBranch(
                    1L,
                    "강남1지점",
                    "02-123-4567",
                    "서울특별시 강남구 테헤란로 123",
                    37.498095,
                    127.02761
            );

            var manager = Employee.builder()
                    .id(1L)
                    .name("홍길동")
                    .build();

            given(employeeRepository.findById(anyLong())).willReturn(Optional.of(manager));

            //when
            branchService.createBranch(dto);

            //then
            ArgumentCaptor<Branch> captor = ArgumentCaptor.forClass(Branch.class);

            verify(branchRepository).save(captor.capture());

            Branch saved = captor.getValue();
            assertThat(saved.getName()).isEqualTo("강남1지점");
            assertThat(saved.getPhoneNumber()).isEqualTo("02-123-4567");
            assertThat(saved.getAddress()).isEqualTo("서울특별시 강남구 테헤란로 123");
            assertThat(saved.getManagerName()).isEqualTo("홍길동");
            assertThat(saved.getManager()).isEqualTo(manager);
            assertThat(saved.getLatitude()).isEqualTo(37.498095);
            assertThat(saved.getLongitude()).isEqualTo(127.02761);
            assertThat(saved.getCarCount()).isEqualTo(0);
            assertThat(saved.getEmployeeCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("EmployeeId가 존재하지 않아도 성공한다")
        void successWithoutEmployeeId() {
            //given
            var dto = new CreateBranch(
                    null,
                    "강남1지점",
                    "02-123-4567",
                    "서울특별시 강남구 테헤란로 123",
                    37.498095,
                    127.02761
            );

            //when
            branchService.createBranch(dto);

            //then
            ArgumentCaptor<Branch> captor = ArgumentCaptor.forClass(Branch.class);

            verifyNoInteractions(employeeRepository);

            verify(branchRepository).save(captor.capture());

            Branch saved = captor.getValue();
            assertThat(saved.getName()).isEqualTo("강남1지점");
            assertThat(saved.getPhoneNumber()).isEqualTo("02-123-4567");
            assertThat(saved.getAddress()).isEqualTo("서울특별시 강남구 테헤란로 123");
            assertThat(saved.getManagerName()).isNull();
            assertThat(saved.getManager()).isNull();
            assertThat(saved.getLatitude()).isEqualTo(37.498095);
            assertThat(saved.getLongitude()).isEqualTo(127.02761);
            assertThat(saved.getCarCount()).isEqualTo(0);
            assertThat(saved.getEmployeeCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("존재하지 않는 EmployeeId는 실패한다")
        void failWithNotExistEmployeeId() {
            //given
            var dto = new CreateBranch(
                    999L,
                    "강남1지점",
                    "02-123-4567",
                    "서울특별시 강남구 테헤란로 123",
                    37.498095,
                    127.02761
            );

            given(employeeRepository.findById(anyLong())).willReturn(Optional.empty());

            //when & then
            assertThatThrownBy(() -> branchService.createBranch(dto))
                    .isInstanceOf(CustomException.class);

            then(branchRepository).should(never()).save(any());
        }
    }
}
