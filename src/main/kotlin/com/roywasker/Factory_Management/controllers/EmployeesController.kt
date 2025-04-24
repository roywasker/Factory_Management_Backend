package com.roywasker.Factory_Management.controllers

import com.roywasker.Factory_Management.Service.EmployeesService
import com.roywasker.Factory_Management.controllers.DepartmentController.DepartmentResponse
import com.roywasker.Factory_Management.model.Department
import com.roywasker.Factory_Management.model.Employee
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/employees")
class EmployeesController(
    private val employeesService: EmployeesService
) {

    data class EmployeeResponse(
        val id: String,
        val firstName: String,
        val lastName: String,
        val startWorkYear: Int,
        val departmentId: String,
    )

    data class EmployeeRequest(
        val firstName: String,
        val lastName: String,
        val startWorkYear: Int,
        val departmentId: String,
    )

    data class UpdateEmployeeRequest(
        val firstName: String?,
        val lastName: String?,
        val startWorkYear: Int?,
        val departmentId: String?,
    )

    data class addDepartmentResponse(
        val departmentId: String,
    )

    @GetMapping
    fun getAllDepartments(): List<EmployeeResponse> {
        return employeesService.getAllEmployees().map {
            it.toEmployeeResponse()
        }
    }

    @GetMapping("/{employeeId}")
    fun getEmployeeById(@PathVariable employeeId: ObjectId): EmployeeResponse {
        return employeesService.getEmployeeById(employeeId).toEmployeeResponse()
    }

    @PostMapping
    fun addEmployee(@Valid @RequestBody body: EmployeeRequest): EmployeeResponse {
        return employeesService.addEmployee(
            firstName = body.firstName,
            lastName = body.lastName,
            startWorkYear = body.startWorkYear,
            departmentId = body.departmentId
        ).toEmployeeResponse()
    }

    @PutMapping("/{employeeId}")
    fun updateEmployee(
        @PathVariable employeeId: ObjectId,
        @RequestBody body: UpdateEmployeeRequest
    ) : EmployeeResponse {
        return employeesService.updateEmployee(employeeId = employeeId, firstName = body.firstName,
            lastName = body.lastName, startWorkYear = body.startWorkYear,
            departmentId = body.departmentId).toEmployeeResponse()
    }

    @DeleteMapping("/{employeeId}")
    fun deleteEmployee(@PathVariable employeeId: ObjectId) {
        employeesService.deleteEmployee(employeeId)
    }

    @PostMapping("/{employeeId}")
    fun addEmployeeToDepartment(@PathVariable employeeId: ObjectId, @Valid @RequestBody body:addDepartmentResponse): EmployeeResponse {
        return employeesService.addEmployeeToDepartment(employeeId = employeeId, departmentId = body.departmentId).toEmployeeResponse()
    }

    @GetMapping("/employee/{departmentId}")
    fun getEmployeesNotIncludedInDepartment(@PathVariable departmentId: ObjectId) : List<EmployeeResponse> {
        return employeesService.getEmployeesNotIncludedInDepartment(departmentId).map { it.toEmployeeResponse() }
    }

    private fun Employee.toEmployeeResponse(): EmployeeResponse {
        return EmployeeResponse(
            id = id.toString(),
            firstName = firstName,
            lastName = lastName,
            startWorkYear = startWorkYear,
            departmentId = departmentId,

        )
    }
}