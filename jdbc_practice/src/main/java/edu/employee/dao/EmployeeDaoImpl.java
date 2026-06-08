package edu.employee.dao;

import edu.common.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployeeDaoImpl implements EmployeeDao {

    @Override
    public void getDepartmentEmployees(String deptTitle) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT E.EMP_NAME, D.DEPT_TITLE, J.JOB_NAME, " +
                "       IFNULL(CAST(E.BONUS AS CHAR), '보너스 없음') AS BONUS, " +
                "       CASE E.ENT_YN WHEN 'N' THEN '재직' ELSE '퇴사' END AS ENT_YN " +
                "FROM EMPLOYEE E " +
                "LEFT JOIN DEPARTMENT D ON E.DEPT_CODE = D.DEPT_ID " +
                "cLEFT JOIN JOB J ON E.JOB_CODE = J.JOB_CODE " +
                "WHERE D.DEPT_TITLE = ? " +
                "ORDER BY E.BONUS DESC";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, deptTitle);
            rs = pstmt.executeQuery();

            System.out.println("------------------------------------------------------------------");
            System.out.printf("%-10s | %-15s | %-10s | %-10s | %-5s\n", "EMP_NAME", "DEPT_TITLE", "JOB_NAME", "BONUS", "ENT_YN");
            System.out.println("------------------------------------------------------------------");

            boolean isEmpty = true;
            while (rs.next()) {
                isEmpty = false;
                System.out.printf("%-10s | %-15s | %-10s | %-10s | %-5s\n",
                        rs.getString("EMP_NAME"),
                        rs.getString("DEPT_TITLE"),
                        rs.getString("JOB_NAME"),
                        rs.getString("BONUS"),
                        rs.getString("ENT_YN"));
            }
            if (isEmpty) System.out.println("해당 부서에 직원이 없습니다.");
            System.out.println("------------------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(pstmt);
        }
    }

    @Override
    public void getDepartmentAvgSalary() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT IFNULL(D.DEPT_TITLE, '부서없음') AS DEPT_TITLE, J.JOB_NAME, " +
                "       COUNT(*) AS EMP_COUNT, ROUND(AVG(E.SALARY)) AS AVG_SALARY " +
                "FROM EMPLOYEE E " +
                "LEFT JOIN DEPARTMENT D ON E.DEPT_CODE = D.DEPT_ID " +
                "JOIN JOB J ON E.JOB_CODE = J.JOB_CODE " +
                "WHERE E.ENT_YN = 'N' " +
                "GROUP BY D.DEPT_TITLE, J.JOB_NAME " +
                "HAVING AVG(E.SALARY) >= 3000000 " +
                "ORDER BY AVG_SALARY DESC";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            System.out.println("-------------------------------------------------------");
            System.out.printf("%-15s | %-10s | %-5s | %-15s\n", "부서명", "직급명", "사원수", "평균급여");
            System.out.println("-------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-15s | %-10s | %-6d | %-15d\n",
                        rs.getString("DEPT_TITLE"),
                        rs.getString("JOB_NAME"),
                        rs.getInt("EMP_COUNT"),
                        rs.getInt("AVG_SALARY"));
            }
            System.out.println("-------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(pstmt);
        }
    }

    @Override
    public void getWorkingEmployees() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT IFNULL(D.DEPT_TITLE, '<null>') AS DEPT_TITLE, J.JOB_NAME, E.EMP_NAME, E.SALARY " +
                "FROM EMPLOYEE E " +
                "LEFT JOIN DEPARTMENT D ON E.DEPT_CODE = D.DEPT_ID " +
                "JOIN JOB J ON E.JOB_CODE = J.JOB_CODE " +
                "WHERE E.ENT_YN = 'N' " +
                "ORDER BY J.JOB_NAME ASC " +
                "LIMIT 10";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            System.out.println("-------------------------------------------------------");
            System.out.printf("%-15s | %-10s | %-10s | %-15s\n", "DEPT_TITLE", "JOB_NAME", "EMP_NAME", "SALARY");
            System.out.println("-------------------------------------------------------");

            int count = 1;
            while (rs.next()) {
                System.out.printf("%-2d %-15s | %-10s | %-10s | %-15d\n",
                        count++,
                        rs.getString("DEPT_TITLE"),
                        rs.getString("JOB_NAME"),
                        rs.getString("EMP_NAME"),
                        rs.getInt("SALARY"));
            }
            System.out.println("-------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(pstmt);
        }
    }

    @Override
    public void increaseSalary(String deptCode) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "UPDATE EMPLOYEE SET SALARY = SALARY * 1.1 WHERE DEPT_CODE = ?";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, deptCode);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                JDBCUtil.commit(conn);
                System.out.println(result + "명의 급여가 10% 인상되었습니다.");
            } else {
                JDBCUtil.rollback(conn);
                System.out.println("해당 부서코드를 가진 직원이 없거나 업데이트에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JDBCUtil.rollback(conn);
        } finally {
            JDBCUtil.close(pstmt);
        }
    }

    @Override
    public void getEmployeesWithoutPhone() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT E.EMP_NAME, IFNULL(E.PHONE, '없음') AS PHONE, D.DEPT_TITLE " +
                "FROM EMPLOYEE E " +
                "LEFT JOIN DEPARTMENT D ON E.DEPT_CODE = D.DEPT_ID " +
                "WHERE E.PHONE IS NULL " +
                "ORDER BY E.EMP_NAME DESC";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            System.out.println("-------------------------------------------------------");
            System.out.printf("%-10s | %-15s | %-15s\n", "EMP_NAME", "PHONE", "DEPT_TITLE");
            System.out.println("-------------------------------------------------------");

            int count = 1;
            while (rs.next()) {
                System.out.printf("%-2d %-10s | %-15s | %-15s\n",
                        count++,
                        rs.getString("EMP_NAME"),
                        rs.getString("PHONE"),
                        rs.getString("DEPT_TITLE"));
            }
            System.out.println("-------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(rs);
            JDBCUtil.close(pstmt);
        }
    }
}