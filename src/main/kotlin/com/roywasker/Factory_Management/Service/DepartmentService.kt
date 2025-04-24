package com.roywasker.Factory_Management.Service

import com.roywasker.Factory_Management.model.Department
import com.roywasker.Factory_Management.repository.DepartmentRepository
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class DepartmentService(
    val departmentRepository: DepartmentRepository
) {
    fun getAllDepartments(): List<Department> {
        return departmentRepository.findAll()
    }

    fun getDepartmentById(userId: ObjectId): Department {
        return departmentRepository.findById(userId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found")
        }
    }

    fun addNewDepartment(name: String, manager: String): Department {
        val newDepartment = Department(
            name = name,
            manager = manager,
        )
        return departmentRepository.save(newDepartment)
    }

    fun updateDepartment(departmentId: ObjectId, name: String?, manager: String?): Department {
        if (name.isNullOrBlank() && manager.isNullOrBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "")
        }
        val department = getDepartmentById(departmentId)

        val updateDepartment = Department(
            id = departmentId,
            name = name?: department.name,
            manager = manager ?: department.manager,
        )

        return departmentRepository.save(updateDepartment)
    }

    fun deleteDepartment(departmentId: ObjectId) {
        departmentRepository.deleteById(departmentId)
    }
}