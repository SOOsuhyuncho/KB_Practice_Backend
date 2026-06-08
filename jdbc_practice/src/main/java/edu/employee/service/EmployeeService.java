package edu.employee.service;

import edu.employee.dao.EmployeeDao;
import edu.employee.dao.EmployeeDaoImpl;

import java.util.Scanner;

public class EmployeeService {

    private Scanner sc = new Scanner(System.in);

    private EmployeeDao dao = new EmployeeDaoImpl();

    public void displayMenu() {

        int menu = 0; // 메뉴 선택용 변수

        do {
            try {
                System.out.println("[직원 관리 시스템]");
                System.out.println("1. 마케팅부 직원 정보 조회");
                System.out.println("2. 부서·직급별 평균 급여 조회");
                System.out.println("3. 재직 중인 직원 목록 조회");
                System.out.println("4. 부서 급여 10% 인상");
                System.out.println("5. 휴대폰 번호 없는 직원 조회");
                System.out.println("0. 종료");
                System.out.print("메뉴 선택 >> ");

                menu = sc.nextInt();
                sc.nextLine(); // 입력 버퍼 개행문자 제거
                System.out.println(); // 줄바꿈

                switch (menu) {
                    case 1:
                        getDepartmentEmployees();
                        break;

                    case 2:
                        getDepartmentAvgSalary();
                        break;

                    case 3:
                        getWorkingEmployees();
                        break;

                    case 4:
                        increaseSalary();
                        break;

                    case 5:
                        getEmployeesWithoutPhone();
                        break;

                    case 0:
                        System.out.println("[프로그램 종료]");
                        break;

                    default:
                        System.out.println("잘못 입력하셨습니다. 메뉴를 다시 선택해주세요.");
                }

            } catch (Exception e) {
                sc.nextLine(); // 잘못된 입력 제거
                e.printStackTrace();
            }
        } while (menu != 0);
    }

    /**
     * 부서명을 입력받아 해당 부서에 근무하는 직원 정보를 조회
     *
     * 조회 컬럼 : 사원명(EMP_NAME), 부서명(DEPT_TITLE), 직급명(JOB_NAME), 보너스율(BONUS), 퇴직여부(ENT_YN)
     *
     * 요구사항
     * - 보너스율이 NULL인 경우 '보너스 없음'으로 표시
     * - 퇴직여부가 'N'이면 '재직', 'Y'이면 '퇴사'로 표시
     * - 보너스율 내림차순 정렬
     *
     */
    private void getDepartmentEmployees() {
        System.out.print("조회할 부서명 입력 (예: 기술지원부) >> ");
        String deptTitle = sc.nextLine();
        dao.getDepartmentEmployees(deptTitle);
    }

    /**
     * 부서별, 직급별 평균 급여 정보를 조회
     *
     * 조회 컬럼 : 부서명, 직급명, 사원수, 평균급여
     *
     * 요구사항:
     * - 재직 중인 직원만 조회
     * - 부서+직급 별 평균 급여가 300만원 이상인 경우만 조회
     * - 평균급여 반올림, 내림차순 정렬
     *
     */
    private void getDepartmentAvgSalary() {
        System.out.println("[부서·직급별 평균 급여 조회 결과]");
        dao.getDepartmentAvgSalary();
    }

    /**
     * 현재 재직 중인 직원 목록을 조회
     *
     * 조회 컬럼 : 부서명, 직급명, 사원명, 급여
     *
     * 요구사항:
     * - 재직 중인 직원만 조회
     * - 부서가 없는 직원도 조회
     * - 직급명 오름차순 정렬
     * - 상위 10명만 조회
     *
     */
    private void getWorkingEmployees() {
        System.out.println("[재직 중인 직원 상위 10명 조회 결과]");
        dao.getWorkingEmployees();
    }

    /**
     * 특정 부서 직원의 급여를 인상한다.
     *
     * 요구사항:
     * - 부서코드를 입력받는다.
     * - 해당 부서 직원들의 급여를 10% 인상한다.
     *
     * 출력 예시:
     * 부서코드 입력 >> D5
     * 5명의 급여가 10% 인상되었습니다.
     */
    private void increaseSalary() {
        System.out.print("부서코드 입력 (예: D5) >> ");
        String deptCode = sc.nextLine();
        dao.increaseSalary(deptCode);
    }

    /**
     * 휴대폰 번호가 없는 직원 정보를 조회한다.
     *
     * 조회 컬럼 : 사원명, 휴대폰번호, 부서명
     *
     * 요구사항:
     * - 휴대폰 번호가 NULL인 직원만 조회
     * - 휴대폰 번호가 NULL인 경우 '없음'으로 표시
     * - 사원명 내림차순 정렬
     *
     */
    private void getEmployeesWithoutPhone() {
        System.out.println("[휴대폰 번호 없는 직원 조회 결과]");
        dao.getEmployeesWithoutPhone();
    }

}