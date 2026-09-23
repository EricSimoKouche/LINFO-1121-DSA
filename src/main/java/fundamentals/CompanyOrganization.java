package fundamentals;

import java.util.*;
import java.util.function.Predicate;

/**
 * In this exercise, you will model a corporate organization hierarchy.
 *
 * The organization is a tree-based data structure where each node is a Department.
 * Each Department can contain direct employees as well as child sub-departments.
 */
public class CompanyOrganization {

    /**
     * Represents an individual employee.
     * Characterized by an ID, name, role, and monthly salary.
     */
    public static class Employee {
        private final int id;
        private final String name;
        private final String role;
        private final int salary;

        public Employee(int id, String name, String role, int salary) {
            this.id = id;
            this.name = name;
            this.role = role;
            this.salary = salary;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        public int getSalary() {
            return salary;
        }
    }

    /**
     * Represents a department within the organization.
     */
    public static class Department implements Iterable<Employee> {

        // TODO: Declare necessary member variables here
        // (e.g., department name, list of employees, list of sub-departments)
        private final String name;
        private final List<Employee> employees;
        private final List<Department> subDepartments;

        /**
         * Constructs a new Department with the given name.
         * The department initially has no employees and no sub-departments.
         *
         * @param name the name of the department
         */
        public Department(String name) {
            // TODO
            this.name = name;
            this.employees = new ArrayList<>();
            this.subDepartments = new ArrayList<>();
        }

        public String getName() {
            // TODO
            return this.name;
        }

        /**
         * Adds an employee directly under this department.
         */
        public void addEmployee(Employee employee) {
            // TODO
            employees.add(employee);
        }

        /**
         * Adds a child department under this department.
         */
        public void addSubDepartment(Department department) {
            // TODO
            subDepartments.add(department);
        }

        /**
         * Computes the total monthly payroll of this department and all
         * its nested sub-departments.
         *
         * @return total salary sum of all employees in the subtree
         */
        public int getTotalPayroll() {
            // TODO
            int totalPayroll = 0;
            for (Employee employee: employees)
                totalPayroll += employee.getSalary();

            for (Department subDepartment : subDepartments)
                totalPayroll += subDepartment.getTotalPayroll();

            return totalPayroll;
        }

        /**
         * Helper method returning a flat list of all employees in this department
         * and all of its sub-departments.
         */
        protected List<Employee> getAllEmployees() {
            // TODO
            List<Employee> allEmployees = new ArrayList<>(employees);
            for (Department subDepartment : subDepartments)
                allEmployees.addAll(subDepartment.getAllEmployees());
            return allEmployees;
        }

        /**
         * Returns an iterator over all employees in this department and its
         * sub-departments in arbitrary order.
         */
        @Override
        public Iterator<Employee> iterator() {
            // TODO
            return new EmployeeIterator(this, f -> true);
        }

        /**
         * Returns an iterator over employees in this department and its
         * sub-departments that satisfy the given filter.
         *
         * @param filter a predicate to filter employees of interest
         */
        public Iterator<Employee> iterator(Predicate<Employee> filter) {
            // TODO
            return new EmployeeIterator(this, filter);
        }
    }

    /**
     * Iterator that traverses all employees matching a predicate
     * within a departmental hierarchy.
     */
    static class EmployeeIterator implements Iterator<Employee> {

        // TODO: Declare necessary fields (e.g., stack or queue, filter)
        private final Deque<Employee> employeeStack;
        private final Predicate<Employee> filter;

        public EmployeeIterator(Department root, Predicate<Employee> filter) {
            // TODO: Collect matching employees
            this.employeeStack = new ArrayDeque<>();
            this.filter = filter;
            pushAllEmployee(root);
        }

        private void pushAllEmployee(Department department) {
            List<Employee> allEmployees = department.getAllEmployees();
            for (Employee employee : allEmployees) {
                if (filter == null || filter.test(employee))
                    employeeStack.push(employee);
            }
        }

        @Override
        public boolean hasNext() {
            // TODO
            return !employeeStack.isEmpty();
        }

        @Override
        public Employee next() {
            // TODO
            return employeeStack.pop();
        }
    }

    static class LasyEmployeeIterator implements Iterator<Employee> {
        private final Deque<Department> departmentStack;
        private final Predicate<Employee> filter;

        private Iterator<Employee> currentDepartmentEmployees;
        private Employee nextEmployee;

        public LasyEmployeeIterator(Department root, Predicate<Employee> filter) {
            this.departmentStack = new ArrayDeque<>();
            this.filter = filter;
            this.currentDepartmentEmployees = Collections.emptyIterator();
            this.nextEmployee = null;

            if (root != null)
                this.departmentStack.push(root);
        }

        @Override
        public boolean hasNext() {
            if (nextEmployee != null)
                return true;
            advance();
            return nextEmployee != null;
        }

        @Override
        public Employee next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Employee result = nextEmployee;
            nextEmployee = null;
            return result;
        }

        private void advance() {
            while (nextEmployee == null) {
                // 1. Drain the employees of the current department first
                if (currentDepartmentEmployees.hasNext()) {
                    Employee candidate = currentDepartmentEmployees.next();
                    if (filter == null || filter.test(candidate)) {
                        nextEmployee = candidate;
                        return;
                    }
                    continue;
                }

                // 2. If no more employees in current department, pop next department from
                if (departmentStack.isEmpty())
                    return;

                Department currentDept = departmentStack.pop();
                currentDepartmentEmployees = currentDept.employees.iterator();

                // 3. Push sub-departments tp visit them later
                for (Department subDept : currentDept.subDepartments)
                    departmentStack.push(subDept);
            }
        }
    }
}