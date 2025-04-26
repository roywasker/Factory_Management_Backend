package com.roywasker.Factory_Management.Service

import com.roywasker.Factory_Management.model.Shift
import com.roywasker.Factory_Management.repository.ShiftsRepository
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ShiftsService(
    private val shiftsRepository: ShiftsRepository,
    private val employeesService: EmployeesService
) {

    fun getAllShifts(): List<Shift>{
        return shiftsRepository.findAll()
    }

    fun getShiftById(shiftsId: ObjectId): Shift{
        return shiftsRepository.findById(shiftsId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND)
        }
    }

    fun addShift(date: String, startingHour: Int, endingHour: Int, employees: List<String>): Shift{
        val newShift = Shift(
            date = date,
            startingHour = startingHour,
            endingHour = endingHour,
            employees = employees
        )
        return shiftsRepository.save(newShift)
    }

    fun updateShift(shiftId: ObjectId, date: String?, startingHour: Int?, endingHour: Int? , employee: List<String>?): Shift{
        if (date.isNullOrBlank() && startingHour == null && endingHour == null && employee == null){
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }

        val shift = getShiftById(shiftId)

        val updateShift = Shift(
            id = shiftId,
            date = date?: shift.date,
            startingHour = startingHour ?: shift.startingHour,
            endingHour = endingHour ?: shift.endingHour,
            employees = employee ?:shift.employees
        )

        return shiftsRepository.save(updateShift)
    }

    fun addEmployeeToShift(shiftId: ObjectId, employeeId: ObjectId): Shift{

        val shift = getShiftById(shiftId)
        val employee = employeesService.getEmployeeById(employeeId)

        val updateEmployeeList = shift.employees.toMutableList()
        updateEmployeeList.add(employee.id.toString())

        val updateShift = shift.copy(
            employees = updateEmployeeList
        )

        return shiftsRepository.save(updateShift)
    }

    fun removeEmployeeFromAllShift(employeeId: ObjectId){
        val allShifts = getAllShifts()

        allShifts.forEach { shift ->
            val updateEmployeeList = shift.employees.filter { employee ->
                employee != employeeId.toString()
            }
            val updateShift = shift.copy(
                employees = updateEmployeeList
            )
            shiftsRepository.save(updateShift)
        }
    }
}
