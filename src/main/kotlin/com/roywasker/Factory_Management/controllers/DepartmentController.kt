package com.roywasker.Factory_Management.controllers

import com.roywasker.Factory_Management.Service.DepartmentService
import com.roywasker.Factory_Management.model.Department
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/departments")
class DepartmentController (
    val departmentService: DepartmentService
) {

    data class DepartmentResponse(
        val id: String,
        val name: String,
        val manager: String,
    )

    data class DepartmentRequest(
        val name: String,
        val manager: String,
    )
    data class UpdateDepartmentRequest(
        val name: String?,
        val manager: String?,
    )

    @GetMapping
    fun getAllDepartments(): List<DepartmentResponse> {
        return departmentService.getAllDepartments().map {
            it.toDepartmentResponse()
        }
    }
    @GetMapping("/{departmentId}")
    fun getDepartmentById(@PathVariable departmentId: ObjectId): DepartmentResponse {
        return departmentService.getDepartmentById(departmentId).toDepartmentResponse()
    }

    @PostMapping
    fun addNewDepartment(@Valid @RequestBody body: DepartmentRequest) : DepartmentResponse {
        return departmentService.addNewDepartment(name = body.name, manager = body.manager).toDepartmentResponse()
    }

    @PutMapping("/{departmentId}")
    fun updateDepartment(@PathVariable departmentId: ObjectId, @RequestBody body: UpdateDepartmentRequest): DepartmentResponse {
        return departmentService.updateDepartment(departmentId = departmentId, name = body.name , manager = body.manager).toDepartmentResponse()
    }

    @DeleteMapping("/{departmentId}")
    fun deleteDepartment(@PathVariable departmentId: ObjectId) {
        departmentService.deleteDepartment(departmentId)
    }


    private fun Department.toDepartmentResponse(): DepartmentResponse {
        return DepartmentResponse(
            id = id.toString(),
            name = name,
            manager = manager
        )
    }
}