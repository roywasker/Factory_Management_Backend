package com.roywasker.Factory_Management.Service

import com.roywasker.Factory_Management.model.Employee
import com.roywasker.Factory_Management.repository.EmployeesRepository
import org.bson.types.ObjectId
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class EmployeesService(
    private val employeesRepository: EmployeesRepository,
    @Lazy private val shiftsService: ShiftsService
) {

    fun getAllEmployees(): List<Employee> {
        return employeesRepository.findAll()
    }

    fun getEmployeeById(employeeId: ObjectId): Employee {
        return employeesRepository.findById(employeeId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    fun addEmployee(firstName: String, lastName: String, startWorkYear: Int, departmentId: String): Employee {
        val newEmployee = Employee(
            firstName = firstName,
            lastName = lastName,
            startWorkYear = startWorkYear,
            departmentId = departmentId
        )
        return employeesRepository.save(newEmployee)
    }

    fun updateEmployee (
        employeeId: ObjectId,
        firstName: String?,
        lastName: String?,
        startWorkYear: Int?,
        departmentId: String?
    ): Employee{
        if (firstName.isNullOrBlank() && lastName.isNullOrBlank() && startWorkYear == null && departmentId.isNullOrBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        }

        val employee = getEmployeeById(employeeId)

        val updatedEmployee = Employee(
            id = employeeId,
            firstName = firstName?: employee.firstName,
            lastName = lastName?: employee.lastName,
            startWorkYear = startWorkYear?: employee.startWorkYear,
            departmentId = departmentId?: employee.departmentId,
        )

        return employeesRepository.save(updatedEmployee)
    }

    fun deleteEmployee(employeeId: ObjectId) {
        shiftsService.removeEmployeeFromAllShift(employeeId)
        employeesRepository.deleteById(employeeId)
    }

    fun addEmployeeToDepartment(employeeId: ObjectId, departmentId: String): Employee {
        val employee = getEmployeeById(employeeId)

        val updatedEmployee = employee.copy(
            departmentId = departmentId,
        )
        return employeesRepository.save(updatedEmployee)
    }

    fun getEmployeesNotIncludedInDepartment (departmentId: ObjectId): List<Employee> {
        val employees = employeesRepository.findAll().toList()
        val employeeNotIncluded = employees.filter { employee -> employee.departmentId != departmentId.toString() }
        return employeeNotIncluded
    }
}

