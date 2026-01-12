package com.erp.domain.branch.service;

import com.erp.domain.branch.dto.request.CreateBranch;
import com.erp.domain.branch.dto.request.UpdateBranch;
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

    @Nested
    @DisplayName("Branch 업데이트 시 ")
    class BranchUpdate {

        @Test
        @DisplayName("성공한다")
        void success() {
            //given
            var oldManager = Employee.builder()
                    .id(10L)
                    .name("구매니저")
                    .build();

            var newManager = Employee.builder()
                    .id(20L)
                    .name("신매니저")
                    .build();

            var branch = Branch.builder()
                    .name("기존지점명")
                    .phoneNumber("02-000-0000")
                    .address("기존주소")
                    .manager(oldManager)
                    .managerName(oldManager.getName())
                    .latitude(37.0)
                    .longitude(127.0)
                    .employeeCount(3)
                    .carCount(5)
                    .build();

            var dto = new UpdateBranch(
                    "변경지점명",
                    null,
                    "변경주소",
                    20L,
                    36.0,
                    null
            );

            given(branchRepository.findById(anyLong())).willReturn(Optional.of(branch));
            given(employeeRepository.findById(anyLong())).willReturn(Optional.of(newManager));

            //when
            branchService.updateBranch(1L, dto);

            //then
            then(branchRepository).should().findById(anyLong());
            then(employeeRepository).should().findById(20L);

            assertThat(branch.getName()).isEqualTo("변경지점명");
            assertThat(branch.getPhoneNumber()).isEqualTo("02-000-0000");
            assertThat(branch.getAddress()).isEqualTo("변경주소");
            assertThat(branch.getManager()).isEqualTo(newManager);
            assertThat(branch.getManagerName()).isEqualTo("신매니저");
            assertThat(branch.getLatitude()).isEqualTo(36.0);
            assertThat(branch.getLongitude()).isEqualTo(127.0);
            assertThat(branch.getEmployeeCount()).isEqualTo(3);
            assertThat(branch.getCarCount()).isEqualTo(5);
        }

        @Test
        @DisplayName("EmployeeId가 존재하지 않아도 성공한다")
        void successWithoutEmployeeId() {
            //given
            var manager = Employee.builder()
                    .id(10L)
                    .name("홍길동")
                    .build();

            var branch = Branch.builder()
                    .name("기존지점명")
                    .phoneNumber("02-000-0000")
                    .address("기존주소")
                    .latitude(37.0)
                    .longitude(127.0)
                    .manager(manager)
                    .managerName("홍길동")
                    .employeeCount(3)
                    .carCount(5)
                    .build();

            var dto = new UpdateBranch(
                    null,
                    "02-123-4567",
                    null,
                    null,
                    null,
                    127.02761
            );

            given(branchRepository.findById(anyLong())).willReturn(Optional.of(branch));

            //when
            branchService.updateBranch(1L, dto);

            //then
            verifyNoInteractions(employeeRepository);
            then(branchRepository).should().findById(anyLong());

            assertThat(branch.getName()).isEqualTo("기존지점명");
            assertThat(branch.getPhoneNumber()).isEqualTo("02-123-4567");
            assertThat(branch.getAddress()).isEqualTo("기존주소");
            assertThat(branch.getManager()).isEqualTo(manager);
            assertThat(branch.getManagerName()).isEqualTo("홍길동");
            assertThat(branch.getLatitude()).isEqualTo(37.0);
            assertThat(branch.getLongitude()).isEqualTo(127.02761);
        }

        @Test
        @DisplayName("지점이 없으면 실패한다")
        void failWithNotExistBranch() {
            //given
            var dto = new UpdateBranch(
                    "변경지점명",
                    "02-123-4567",
                    "변경주소",
                    1L,
                    37.498095,
                    127.02761
            );

            given(branchRepository.findById(anyLong())).willReturn(Optional.empty());

            //when & then
            assertThatThrownBy(() -> branchService.updateBranch(999L, dto))
                    .isInstanceOf(CustomException.class);

            then(employeeRepository).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("존재하지 않는 EmployeeId면 실패한다")
        void failWithNotExistEmployeeId() {
            //given
            var manager = Employee.builder()
                    .id(10L)
                    .name("홍길동")
                    .build();

            var branch = Branch.builder()
                    .name("기존지점명")
                    .phoneNumber("02-000-0000")
                    .address("기존주소")
                    .manager(manager)
                    .managerName(manager.getName())
                    .latitude(37.0)
                    .longitude(127.0)
                    .employeeCount(3)
                    .carCount(5)
                    .build();

            var dto = new UpdateBranch(
                    "변경지점명",
                    null,
                    null,
                    999L, // 없는 직원
                    null,
                    null
            );

            given(branchRepository.findById(anyLong())).willReturn(Optional.of(branch));
            given(employeeRepository.findById(anyLong())).willReturn(Optional.empty());

            //when & then
            assertThatThrownBy(() -> branchService.updateBranch(1L, dto))
                    .isInstanceOf(CustomException.class);

            then(branchRepository).should().findById(anyLong());
            then(employeeRepository).should().findById(999L);

            assertThat(branch.getName()).isEqualTo("기존지점명");
            assertThat(branch.getManager()).isEqualTo(manager);
            assertThat(branch.getManagerName()).isEqualTo("홍길동");
        }
    }

    @Nested
    @DisplayName("Branch 삭제 시 ")
    class BranchDelete {

        @Test
        @DisplayName("성공한다")
        void success() {
            //given
            given(branchRepository.existsById(anyLong())).willReturn(true);

            //when
            branchService.deleteBranch(1L);

            //then
            then(branchRepository).should().existsById(1L);
            then(branchRepository).should().deleteById(1L);
        }

        @Test
        @DisplayName("지점이 없으면 실패한다")
        void failWithNotExistBranch() {
            //given
            given(branchRepository.existsById(anyLong())).willReturn(false);

            //when & then
            assertThatThrownBy(() -> branchService.deleteBranch(999L))
                    .isInstanceOf(CustomException.class);

            then(branchRepository).should().existsById(anyLong());
            then(branchRepository).should(never()).deleteById(anyLong());
        }
    }
}
