package com.roywasker.Factory_Management.controllers

import com.roywasker.Factory_Management.Service.ShiftsService
import com.roywasker.Factory_Management.model.Employee
import com.roywasker.Factory_Management.model.Shift
import jakarta.validation.Valid
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/shifts")
class ShiftsController (
    private val shiftsService: ShiftsService
) {

    data class ShiftsResponse(
        val id: String,
        val date: String,
        val startingHour: Int,
        val endingHour: Int,
        val employees: List<String>
    )

    data class ShiftsRequest(
        val date: String,
        val startingHour: Int,
        val endingHour: Int,
        val employees: List<String>
    )

    data class UpdateShiftsRequest(
        val date: String?,
        val startingHour: Int?,
        val endingHour: Int?,
        val employees: List<String>?
    )

    data class addToShiftsRequest(
        val employeeId: String,
    )

    @GetMapping
    fun getAllShifts():List<ShiftsResponse>{
        return shiftsService.getAllShifts().map { it.toShiftSResponse() }
    }

    @GetMapping("/{shiftId}")
    fun getShift(@PathVariable shiftId: ObjectId): ShiftsResponse {
        return shiftsService.getShiftById(shiftId).toShiftSResponse()
    }

    @PostMapping
    fun addShift(@Valid @RequestBody body: ShiftsRequest): ShiftsResponse {
        return shiftsService.addShift(body.date, body.startingHour, body.endingHour, body.employees).toShiftSResponse()

    }

    @PutMapping("/{shiftId}")
    fun updateShift(@PathVariable shiftId: ObjectId, @RequestBody body: UpdateShiftsRequest): ShiftsResponse {
        return shiftsService.updateShift(shiftId, body.date, body.startingHour,
            body.endingHour, body.employees).toShiftSResponse()
    }

    @PostMapping("/{shiftId}")
    fun addEmployeeToShift(@PathVariable shiftId: ObjectId, @Valid @RequestBody body: addToShiftsRequest): ShiftsResponse{
        return shiftsService.addEmployeeToShift(shiftId, ObjectId(body.employeeId)).toShiftSResponse()
    }


    private fun Shift.toShiftSResponse(): ShiftsResponse {
        return ShiftsResponse(
            id = id.toString(),
            date = date,
            startingHour = startingHour,
            endingHour = endingHour,
            employees = employees
        )
    }
}